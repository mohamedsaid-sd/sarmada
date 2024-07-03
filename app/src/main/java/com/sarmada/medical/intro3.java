package com.sarmada.medical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class intro3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro3);
    }

    public void fun_next(View view) {
        startActivity(new Intent( this , intro4.class ));
        finish();
    }

    public void fun_pref(View view) {
        startActivity(new Intent( this , intro2.class ));
        finish();
    }


}