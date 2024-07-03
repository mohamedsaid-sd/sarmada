package com.sarmada.medical;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class splash extends AppCompatActivity {

    SharedPreferences preferences ;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // المتغيير المحلي
        preferences = getSharedPreferences( "LOGIN" , Context.MODE_PRIVATE);



        // التحقق من تصفح جميع الشاشات
        if(preferences.getString("showallintro" , "").equals("true")){
            // نعم تصفح جميع الصفحات
            // انتقل الي صفحة الدخول مباشرة
            // كود ال splash
            new Handler(Looper.getMainLooper()).postDelayed(()->{
                        // دالة فتح الصفحة الجديدة
                        startActivity(new Intent(this , login.class));
                        finish();
                    }
                    // الزمن المطلوب تاخيره وهو 5 ثوان
                    , 6000 );
        }else{
            // لم يتصفح الشاشات بعد
            // قم بعرض الشاشات
            // كود ال splash
            new Handler(Looper.getMainLooper()).postDelayed(()->{
                        // دالة فتح الصفحة الجديدة
                        startActivity(new Intent(this , intro1.class));
                        finish();
                    }
                    // الزمن المطلوب تاخيره وهو 5 ثوان
                    , 6000 );
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void method_check_version() {
        StringRequest request_display_versions = new StringRequest(Request.Method.GET,
                config.display_versions,
                response -> {
                    Log.e("VERSIONS" , response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i <jsonArray.length() ; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            if(object.getString("name").equals("sarmada")){
                                String code = object.getString("code");
                                String date = object.getString("date");
                                checkVersion(Integer.parseInt(code) , date);
                                Log.e("VERSIONS" , object.getString("code")+"|"+object.getString("date"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {}
        );
        RequestHandler.getInstance(this).addToRequestQueue(request_display_versions);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkVersion(int newVersionCode , String date) {

        // calender now
        Calendar now = Calendar.getInstance();
        // calender date
        Calendar finish = Calendar.getInstance();

        if(newVersionCode < BuildConfig.VERSION_CODE ) {

            Log.e("VERSIONSDATE", newVersionCode + "/" + BuildConfig.VERSION_CODE);
            // في حالة كان اصدار قواعد البيانات اصغر من اصدار  التطبيق
            // هذا يعني انه يتوفر اصدار جديد
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date d = format.parse(date);
                finish.setTime(d);
                Log.e("VERSIONSDATE", "" + d.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}