package com.sarmada.medical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.sarmada.medical.items.doctors_Items;

public class medical_cat extends AppCompatActivity {

    EditText edsearch ;
    DoctorsAdabter adapter ;
    RecyclerView medical_cat_recycel ;
    ArrayList<doctors_Items> items , searchitems ;
    String cat_id , cat_name , main_cat;
    ProgressBar progressBar ;
    ImageView imageView6 ;

    @SuppressLint({"UseCompatLoadingForDrawables", "ObsoleteSdkInt"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_cat);
        // الحصول علي المتغيرات من الصفحة السابقة
        // رقم الفئة
        cat_id = getIntent().getExtras().getString("cat_id");
        // اسم الفئة
        cat_name = getIntent().getExtras().getString("cat_name");
        // رقم المجموعة
        main_cat = getIntent().getExtras().getString("maincat");

        //Toast.makeText(this, "main"+main_cat, Toast.LENGTH_SHORT).show();

        Casting();
        // تعريف خلية بعدد عنصرين في الصف الواحد
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this , 1);
        // خلية مع تحريك افقي
        gridLayoutManager.setOrientation( GridLayoutManager.VERTICAL );
        // ربط القائمة المتحركة مع الخلية المعرفة
        medical_cat_recycel.setLayoutManager(gridLayoutManager);

        // نهيئة قائمة الفئات
        items = new ArrayList<>();
        searchitems = new ArrayList<>();
        LoadData();

        edsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter = null ;
                searchitems.clear();
                String stsearch = edsearch.getText().toString();
                for (int j = 0; j < items.size(); j++) {
                    if(items.get(j).getLocation().contains(stsearch) || items.get(j).getLocation().contains(stsearch)){
                        searchitems.add( new doctors_Items(
                                items.get(j).getImg(),
                                items.get(j).getId(),
                                items.get(j).getName() ,
                                items.get(j).getPhone() ,
                                cat_name ,
                                items.get(j).getLocation(),
                                items.get(j).getWork_time(),
                                items.get(j).getMaxbasic(),
                                items.get(j).getMaxvip(),
                                items.get(j).getLink(),
                                items.get(j).getCv() ,
                                cat_id));
                    }
                }
                adapter = new DoctorsAdabter( searchitems , getApplicationContext());
                // ربط القائمة المتحركة مع كلاس الربط
                medical_cat_recycel.setAdapter( adapter );
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(getIntent().getExtras().getString("maincat").equals("8")){
            // سيف
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                imageView6.setImageDrawable(getResources().getDrawable(R.drawable.banar7,getApplicationContext().getTheme()));
            }else{
                imageView6.setImageDrawable(getResources().getDrawable(R.drawable.banar7));
            }
        }else if(getIntent().getExtras().getString("maincat").equals("17")){
            // العذبي
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                imageView6.setImageDrawable(getResources().getDrawable(R.drawable.banner5,getApplicationContext().getTheme()));
            }else{
                imageView6.setImageDrawable(getResources().getDrawable(R.drawable.banner5));
            }
        }else if(getIntent().getExtras().getString("maincat").equals("33")){
            // FAUDA !
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                imageView6.setImageDrawable(getResources().getDrawable(R.drawable.banar6,getApplicationContext().getTheme()));
            }else{
                imageView6.setImageDrawable(getResources().getDrawable(R.drawable.banar6));
            }
        }else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                imageView6.setImageDrawable(getResources().getDrawable(R.drawable.banar1,getApplicationContext().getTheme()));
            }else{
                imageView6.setImageDrawable(getResources().getDrawable(R.drawable.banar1));
            }
        }

    }

    private void LoadData() {
        StringRequest request_medical_service = new StringRequest(
                Request.Method.POST,
                config.display_medical_cat,
                response -> {
                    Log.e("TRACK" , response);
                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject object = array.getJSONObject(i);
                            String link = object.getString("link");

                            items.add (new doctors_Items(
                                            object.getString("img"),
                                            object.getString("id"),
                                            object.getString("name") ,
                                            object.getString("phone") ,
                                            cat_name ,
                                            object.getString("address"),
                                            object.getString("work_time"),
                                            object.getString("maxbasic"),
                                            object.getString("maxvip"),
                                            link,
                                            object.getString("cv") ,
                                            cat_id));

                            Log.e("Doctorresponce: " , link);
                        }
                        adapter = new DoctorsAdabter( items , getApplicationContext());
                        // ربط القائمة المتحركة مع كلاس الربط
                        medical_cat_recycel.setAdapter( adapter );
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }
        ){
            @NonNull
            @Override
            protected Map<String, String> getParams()  {
                HashMap<String,String> parms = new HashMap<>();
                parms.put("cat_id" , cat_id);
                parms.put("mcat" , main_cat);
                return parms;
            }
        } ;
        RequestHandler.getInstance(this).addToRequestQueue(request_medical_service);

    }

    private void Casting() {
        medical_cat_recycel = findViewById(R.id.medical_cat_recycel);
        progressBar         = findViewById(R.id.progressBar);
        edsearch            = findViewById(R.id.edsearch);
        imageView6          = findViewById(R.id.imageView6);
    }
}