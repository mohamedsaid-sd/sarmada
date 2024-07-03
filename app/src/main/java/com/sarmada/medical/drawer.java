package com.sarmada.medical;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;

public class drawer extends AppCompatActivity {

    private DrawerLayout drawer;
    Toolbar toolbar;
    SharedPreferences preferences ;
    String id , name ;
    TextView txt_customer_name ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Log.e("DEBAGE" , "drawer is open");

        preferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        id = preferences.getString("sh_id" , "0");
        name = preferences.getString("sh_name" , "0");



        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        txt_customer_name = findViewById(R.id.txt_customer_name);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        txt_customer_name.setText("مرحبا "+name);



    }
}