package com.sarmada.medical;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    EditText edusername , edpassword  ;
    String   username   , pasword  , id , name , phone , sub , img , level , school ;
    TextView txtalert ;
    SharedPreferences preferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Casting();
        //local variable
        preferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        if(preferences.getString("sh_login", "FIll").equals("1")){
            String id = preferences.getString("sh_id" , "0");
            String name = preferences.getString("sh_name" , "Null");
            String phone = preferences.getString("sh_phone" , "Null");
            String sub = preferences.getString("sh_sub" , "Null");
            String img = preferences.getString("sh_img" , "Null");
            String level = preferences.getString("sh_level" , "Null");
            String school = preferences.getString("sh_school" , "Null");
            autologin( id , name , phone , sub , img , level , school );
        }else{
            Toast.makeText(this, "مرحبا بك !", Toast.LENGTH_SHORT).show();
        }

        // التحقق من متغيرات التسجيل الجديد
        if(!preferences.getString("sh_user" , "0").equals("0")){
            edusername.setText(preferences.getString("sh_user" , "0"));
            edpassword.setText(preferences.getString("sh_pass" , "0"));
        }

    }

    private void Casting() {
        edusername = findViewById(R.id.edusername);
        edpassword = findViewById(R.id.edpassword);
        txtalert   = findViewById(R.id.txtalert);
    }

    public void fun_login(View view) {

        username = edusername.getText().toString().intern();
        pasword = edpassword.getText().toString().intern();

        if(username.isEmpty() || pasword.isEmpty()){
            Toast.makeText(this, "جميع الحقول مطلوبة", Toast.LENGTH_SHORT).show();
        }else{
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("جاري تسجيل الدخول ... ");
            dialog.setCancelable(false);
            dialog.show();
            StringRequest request_add_delegate = new StringRequest(Request.Method.POST,
                    config.login_customer,
                    response -> {
                        try {
                            Log.e("LOGINLOG" , response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.get("data").equals(0)) {
                                dialog.dismiss();
                                Toast.makeText(this, "الحساب غير نشط", Toast.LENGTH_LONG).show();
                            } else {
                                id    = jsonObject.getString("data");
                                name  = jsonObject.getString("name");
                                phone = jsonObject.getString("phone");
                                sub   = jsonObject.getString("sub");
                                img   = jsonObject.getString("img");
                                level   = jsonObject.getString("level");
                                school   = jsonObject.getString("school");
                                dialog.dismiss();
                                autologin( id , name , phone , sub , img , level , school );
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            Toast.makeText(this, "خطا  في غملية تسجيل الدخول", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        dialog.dismiss();
                        Toast.makeText(this, "خطا  في غملية تسجيل الدخول", Toast.LENGTH_SHORT).show();
                     }) {
                @NonNull
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("user", username);
                    map.put("pass", pasword);
                    return map;

                }
            };
            RequestHandler.getInstance(this).addToRequestQueue(request_add_delegate);
        }
    }

    public void autologin( String id , String name , String phone , String sub , String img , String level , String school  ){

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sh_login" , "1");
        editor.putString("sh_id" , id);
        editor.putString("sh_name" , name);
        editor.putString("sh_phone" , phone);
        editor.putString("sh_sub" , sub);
        editor.putString("sh_img" , img);
        editor.putString("sh_level" , level);
        editor.putString("sh_school" , school);
        editor.apply();

        if(level.equals("0")){
            startActivity(new Intent( this , home.class));
            finish();
        }else{
            startActivity(new Intent( this , select.class));
            finish();
        }

    }

    public void new_account(View view) {
        startActivity(new Intent( this , ads.class));
    }

    public void open_barcode(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(login.this)
                .setCaptureActivity(CaptureActivity.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt(" ضع البطاقة امام الكاميرا ");
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null){
            String contents =   intentResult.getContents();
            if(contents != null){


                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("جاري تسجيل الدخول ... ");
                dialog.setCancelable(false);
                dialog.show();
                StringRequest request_add_delegate = new StringRequest(Request.Method.POST,
                        config.login_qr_code,
                        response -> {

                            Log.e("test" , response);
                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.get("data").equals(0)) {
                                    dialog.dismiss();
                                    Toast.makeText(this, "بطاقة غير سارية", Toast.LENGTH_SHORT).show();
                                    txtalert.setVisibility(View.VISIBLE);
                                } else {
                                    id    = jsonObject.getString("data");
                                    name  = jsonObject.getString("name");
                                    phone = jsonObject.getString("phone");
                                    sub   = jsonObject.getString("sub");
                                    img   = jsonObject.getString("img");
                                    level   = jsonObject.getString("level");
                                    school   = jsonObject.getString("school");

                                    dialog.dismiss();
                                   autologin( id , name , phone , sub , img  , level , school );
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                dialog.dismiss();
                                Toast.makeText(this, "خطا  في غملية تسجيل الدخول", Toast.LENGTH_SHORT).show();
                            }
                        },
                        error -> {
                            dialog.dismiss();
                            Toast.makeText(this, "خطا  في غملية تسجيل الدخول", Toast.LENGTH_SHORT).show();
                        }) {
                    @NonNull
                    @Override
                    protected Map<String, String> getParams() {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("qr", contents);
                        return map;

                    }
                };
                RequestHandler.getInstance(this).addToRequestQueue(request_add_delegate);
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void show_pass(View view) {
        edpassword.setTransformationMethod(null);
    }

    public void fun_free_login(View view) {
        startActivity( new Intent(this , home.class) );
    }
}