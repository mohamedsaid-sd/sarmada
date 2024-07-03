package com.sarmada.medical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class support extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
    }

    public void openFacebook(View view) {
        String url_page = "https://www.facebook.com/Sarmada.for.medical.care?mibextid=ZbWKwl";
        Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(url_page));
        startActivity(intent);
    }

    public void openLinkedin(View view) {
        String url_page = "https://www.linkedin.com/company/sarmada-for-medical-care/";
        Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(url_page));
        startActivity(intent);
    }


    public void call1(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+201004869800"));
        startActivity(intent);
    }

    public void call2(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+201009375800"));
        startActivity(intent);
    }

    public void call3(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+201006234800"));
        startActivity(intent);
    }
}