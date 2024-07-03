package com.sarmada.medical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class intro4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro4);
    }

    public void fun_next(View view) {
        startActivity(new Intent( this , intro5.class ));
        finish();
    }

    public void fun_pref(View view) {
        startActivity(new Intent( this , intro3.class ));
        finish();
    }
}