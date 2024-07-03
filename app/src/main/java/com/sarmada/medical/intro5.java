package com.sarmada.medical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class intro5 extends AppCompatActivity {

    SharedPreferences preferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro5);
        // المتغيير المحلي
        preferences = getSharedPreferences( "LOGIN" , Context.MODE_PRIVATE);
    }

    public void fun_last(View view) {
        // حفظ متغير محلي للتاكد من فتح جميع الصفحات
        SharedPreferences.Editor editor = preferences.edit();
        //  showallintro تسمية المتغيير
        // عطاءه القيمة true
        editor.putString("showallintro" , "true");
        editor.apply();
        startActivity( new Intent( this , login.class ));
        finish();

    }
}