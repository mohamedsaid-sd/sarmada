package com.sarmada.medical;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class select extends AppCompatActivity {

    SharedPreferences preferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        preferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

    }

    public void fun_medical(View view) {
        startActivity(new Intent(this , home.class));
    }
    public void fun_education(View view) {
        // stop link at this time
        //startActivity(new Intent(this , subjects.class));
        // adding new link
        startActivity( new Intent( this , welcome_school.class ) );
    }

    public void fun_log_out(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(select.this);
        alertDialogBuilder.setTitle(" تطبيق سرمدا ");
        alertDialogBuilder
                .setMessage("تاكيد الخروج من البرنامج")
                .setCancelable(false)
                .setPositiveButton("نعم", (dialogInterface, i) -> {
                    // الضغط علي تسجيل الخروج من القائمة
                    startActivity(new Intent(this , login.class));
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("sh_login" , "0");
                    editor.putString("sh_id" , null);
                    editor.putString("sh_name" , null);
                    editor.putString("sh_phone" , null);
                    editor.putString("sh_sub" , null);
                    editor.putString("sh_level" , null);
                    editor.putString("sh_school" , null);
                    editor.apply();
                    Toast.makeText(this, "تم تسجيل الخروج", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("لا" , ((dialogInterface, i) ->
                        dialogInterface.cancel()) );
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
}