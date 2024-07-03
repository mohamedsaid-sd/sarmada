package com.sarmada.medical;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class setting extends AppCompatActivity {

    SharedPreferences preferences ;
    String id  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        preferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        id = preferences.getString("sh_id" , "0").toString();

    }

    public void fun_go_home(View view) {
        startActivity( new Intent( this , home.class));
        finish();
    }

    public void fun_change_profile(View view) {

    }

    public void fun_change_password(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout  = getLayoutInflater().inflate(R.layout.change_password, null);
        TextView title        = layout.findViewById(R.id.txt_title);
        TextView alert        = layout.findViewById(R.id.txt_alert);
        EditText old_pass     = layout.findViewById(R.id.txt_old_pass);
        EditText new_pass     = layout.findViewById(R.id.txt_new_pass);
        EditText confirm_pass = layout.findViewById(R.id.txt_confirm_pass);
        Button  btn_confirm   = layout.findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(view1 -> {
            String oldpass    = old_pass.getText().toString().intern();
            String newpass    = new_pass.getText().toString().intern();
            String confirmpass = confirm_pass.getText().toString().intern();
            if(oldpass.isEmpty() || newpass.isEmpty() || confirmpass.isEmpty())
                Toast.makeText(this, "الرجاء ادخال جميع الحقول", Toast.LENGTH_SHORT).show();
            else {
                if (!confirmpass.equals(newpass)) {
                    Toast.makeText(this, "كلمة المرور غير متطابقة", Toast.LENGTH_SHORT).show();
                } else {

                    StringRequest request_change_password = new StringRequest(
                            Request.Method.POST,
                            config.change_password,
                            response -> {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.get("data").equals(0)) {
                                        // كلمة المرور غير مطابقة
                                        alert.setVisibility(View.VISIBLE);
                                        alert.setText("خطأ في كلمة المرور");
                                    }else{
                                        // كلمة المرور مطابقة
                                        alert.setVisibility(View.VISIBLE);
                                        alert.setTextColor(Color.GREEN);
                                        alert.setText("تم تغيير كلمة المرور بنجاح");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            },
                            error -> {

                            }
                         ){
                        @NonNull
                        @Override
                        protected Map<String, String> getParams(){
                            HashMap<String , String> map = new HashMap<>();
                            map.put("id" , id);
                            map.put("old_pass" , oldpass);
                            map.put("new_pass" , newpass);
                            return map ;
                        }
                    };
                    RequestHandler.getInstance(this).addToRequestQueue(request_change_password);
                }
            }
        });

        title.setOnClickListener(view1 -> {
            builder.setCancelable(true);
            Toast.makeText(setting.this, "الغاء", Toast.LENGTH_SHORT).show();
        });

        builder.setView(layout);
        AlertDialog dialog = builder.show();

//        IntentIntegrator intentIntegrator = new IntentIntegrator(setting.this)
//                .setCaptureActivity(CaptureActivity.class);
//
//        intentIntegrator.setOrientationLocked(false);
//        intentIntegrator.setPrompt(" ضع البطاقة امام الكاميرا ");
//        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
//        intentIntegrator.initiateScan();
        //startActivityForResult(getPickImageChooserIntent(), PERMISSIONS_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            String contents = intentResult.getContents();
            if (contents != null) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View layout  = getLayoutInflater().inflate(R.layout.change_password, null);
                TextView title        = layout.findViewById(R.id.txt_title);
                EditText old_pass     = layout.findViewById(R.id.txt_old_pass);
                EditText new_pass     = layout.findViewById(R.id.txt_new_pass);
                EditText confirm_pass = layout.findViewById(R.id.txt_confirm_pass);
                Button  btn_confirm   = layout.findViewById(R.id.btn_confirm);

                btn_confirm.setOnClickListener(view -> {
                    String oldpass    = old_pass.getText().toString().intern();
                    String newpass    = new_pass.getText().toString().intern();
                    String confirmpass = confirm_pass.getText().toString().intern();
                    if(oldpass.isEmpty() || newpass.isEmpty() || confirmpass.isEmpty())
                        Toast.makeText(this, "الرجاء ادخال جميع الحقول", Toast.LENGTH_SHORT).show();
                    else {
                        if (!confirmpass.equals(newpass)) {
                            Toast.makeText(this, "كلمة المرور غير متطابقة", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "ready to go ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                title.setOnClickListener(view -> {
                  builder.setCancelable(true);
                    Toast.makeText(setting.this, "الغاء", Toast.LENGTH_SHORT).show();
                });

                builder.setView(layout);
                AlertDialog dialog = builder.show();
            }
        }
    }

}