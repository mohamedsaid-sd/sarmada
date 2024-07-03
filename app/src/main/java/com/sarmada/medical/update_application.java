package com.sarmada.medical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class update_application extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_application);

    }

    public void fun_update(View view) {
        Intent open_store = new Intent( Intent.ACTION_VIEW );
        open_store.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.sarmada.medical"));
        startActivity(open_store);
    }

    @Override
    public void onBackPressed() {
        //
    }
}