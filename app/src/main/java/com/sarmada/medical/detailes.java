package com.sarmada.medical;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class detailes extends AppCompatActivity {
    LinearLayout layoutcv ;
    TextView txtname , txtphone , txtaddress , txtwork , txtmap , txtcv  ;
    String id , name , phone , address , link , work , img , sh_sub , cv , sh_login ;
    ImageView imgview ;
    ServicepAdabter adapter ;
    ArrayList<service_item> items ;
    RecyclerView recycle_servies ;
    SharedPreferences preferences ;
    ProgressBar progressBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailes);

        //local variable
        preferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        sh_sub = preferences.getString("sh_sub" , "0");
        sh_login = preferences.getString("sh_login" , "0");

        //الربط بالواجهه
        fun_casting();
        // تعريف خلية بعدد عنصرين في الصف الواحد
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this , 1);
        // خلية مع تحريك افقي
        gridLayoutManager.setOrientation( GridLayoutManager.VERTICAL );
        // ربط القائمة المتحركة مع الخلية المعرفة
        recycle_servies.setLayoutManager(gridLayoutManager);
        Initilize();
        // جلب المتغييرات
        fun_get_intent();
        // اسناد المتغيرات للواجهات
        txtname.setText(name);
        txtaddress.setText(address);
        txtwork.setText(work);

        Picasso.get().load(config.QR_IMG_URL+img).into(imgview);
        //load the services
        loadservice();
        Log.e("RRRRRRRRRRTTTTTTTTTTtttt" , cv );
        if(cv.intern().equals("") || cv.intern().isEmpty() || cv.intern().equals(" ")){
            layoutcv.setVisibility(View.GONE);
        }else{
            txtcv.setText(cv);
        }

        // check if customer login
        if(sh_login.equals("1")) {
            // yes login set phone number
            txtphone.setText(phone);
            txtmap.setText("افتح الخريطة ");
        }else {
            // no not login notify user to login
            txtphone.setText(" اشترك لعرض رقم الهاتف ");
            txtmap.setText("اشترك لفتح الخريطة ");
        }

    }

    private void Initilize() {
        items = new ArrayList<>();
    }

    public void loadservice(){
        //get the sp
        StringRequest request_medical_service = new StringRequest(
                Request.Method.POST,
                config.display_services,
                response -> {
                    Log.e("TTTTTTTT" , ""+response);
                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject object = array.getJSONObject(i);
                            int price = Integer.parseInt(object.getString("price"));
                            int vip   = Integer.parseInt(object.getString("vip"));
                            int basic   = Integer.parseInt(object.getString("basic"));

                            String vip_price =  String.valueOf(price - (price * vip / 100 ));
                            String basic_price = String.valueOf(price - (price * basic / 100 ));;

                            if(sh_sub.contains("VIP"))
                            items.add (
                                    new service_item(object.getString("id") ,
                                            object.getString("service") ,
                                            object.getString("price") ,
                                            object.getString("vip") ,
                                            vip_price));
                            else
                                items.add (
                                        new service_item(object.getString("id") ,
                                                object.getString("service") ,
                                                object.getString("price") ,
                                                object.getString("basic") ,
                                                basic_price));

                        }
                        adapter = new ServicepAdabter( items , getApplicationContext());
                        // ربط القائمة المتحركة مع كلاس الربط
                        recycle_servies.setAdapter( adapter );
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
                parms.put("id" , id);
                return parms;
            }
        }  ;
        RequestHandler.getInstance(this).addToRequestQueue(request_medical_service);

    }

    private void fun_get_intent() {
        id      = getIntent().getExtras().getString("id");
        name    = getIntent().getExtras().getString("name");
        phone   = getIntent().getExtras().getString("phone");
        address = getIntent().getExtras().getString("address");
        link = getIntent().getExtras().getString("link");
        work    = getIntent().getExtras().getString("work_time");
        img     = getIntent().getExtras().getString("img");
        cv      = getIntent().getExtras().getString("cv");
    }

    private void fun_casting() {
        txtname          = findViewById(R.id.txtname);
        txtphone         = findViewById(R.id.txtphone);
        txtaddress       = findViewById(R.id.txtaddress);
        txtwork          = findViewById(R.id.txtwork);
        txtmap           = findViewById(R.id.txtmap);
        imgview          = findViewById(R.id.img);
        recycle_servies  = findViewById(R.id.recycle_servies);
        progressBar      = findViewById(R.id.progressBar);
        txtcv            = findViewById(R.id.cv);
        layoutcv         = findViewById(R.id.layoutcv);
    }

    public void fun_open_map(View view) {
        // check if customer login
        if(sh_login.equals("1")) {
            // yes login open map
            String uri = link;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }else {
            // no not login open ads page
            startActivity(new Intent( this , ads.class )
                       .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public void fun_call_number(View view) {
        // check if customer login
        if(sh_login.equals("1")) {
            // yes login deal phone number
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+phone));
            startActivity(intent);
        }else {
            // no not login open ads page
            startActivity(new Intent( this , ads.class )
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
