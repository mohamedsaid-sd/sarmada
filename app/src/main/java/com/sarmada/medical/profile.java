package com.sarmada.medical;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;


public class profile extends AppCompatActivity {

    SharedPreferences preferences ;
    TextView txtname , txtphone , txtsub ;
    //String id ;
    ImageView imgqrcode  , imgprofile;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Casting();
        //local variable
        preferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        String id = preferences.getString("sh_id" , "0");
        String name = preferences.getString("sh_name" , "Null");
        String phone = preferences.getString("sh_phone" , "Null");
        String sub = preferences.getString("sh_sub" , "Null");
        String img = preferences.getString("sh_img" , "Null");

        StringRequest request_display_profile = new StringRequest(Request.Method.GET,
                config.display_profile+"?id="+id,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.optJSONArray("data");
                        Log.e("UUUUUUUUUU" , "Readyyyyyyyyyyyyy"+response+id+config.QR_IMG_URL);
                        for (int i = 0; i < Objects.requireNonNull(array).length() ; i++) {
                            JSONObject object =  array.getJSONObject(i);
                            String qr = object.getString( "id" );
                            String qr_image = object.getString( "qr_image" );
                            Log.e("TTTTTTTTTTT" , "Readyyyyyyyyyyyyy"+response+id+config.QR_IMG_URL+object.getString("qr_image"));
                            Picasso.get().load(config.QR_IMG_URL + qr_image).into(imgqrcode);
                            progressBar.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> { });
        RequestHandler.getInstance(this).addToRequestQueue(request_display_profile);

        txtname.setText(name);
        txtphone.setText(phone);
        txtsub.setText(sub);

        Picasso.get().load(config.QR_IMG_URL+img).into(imgprofile);

    }

    private void Casting() {
        imgqrcode = findViewById(R.id.imgqrcode);
        txtname   = findViewById(R.id.txtname);
        txtphone  = findViewById(R.id.txtphone);
        txtsub    = findViewById(R.id.txtsub);
        progressBar = findViewById(R.id.progressBar);
        imgprofile  = findViewById(R.id.imgprofile);
    }

    public void fun_log_out(View view) {
        startActivity(new Intent(this , login.class));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sh_login" , "0");
        editor.putString("sh_id" , null);
        editor.putString("sh_name" , null);
        editor.putString("sh_phone" , null);
        editor.putString("sh_sb" , null);
        editor.apply();
        Toast.makeText(this, "تم تسجيل الخروج", Toast.LENGTH_SHORT).show();
    }

    public void fun_feedback(View view) {
        // الذهاب الي صفحة المشكلات والمقترحات
        startActivity( new Intent( this , feedback.class ) );
    }

    public void fun_back(View view) {
        super.onBackPressed();
    }
}