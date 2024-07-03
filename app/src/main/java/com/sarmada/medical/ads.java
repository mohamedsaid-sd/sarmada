package com.sarmada.medical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sarmada.medical.R;

public class ads extends AppCompatActivity {

    ImageView imgbasic , imgvip , imgvipplus ;
    TextView txt_basic_personal , txt_vip_personal , txt_vipplus_personal ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        Casting();

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().getString("flag").equals("F")) {
                txt_basic_personal.setVisibility(View.GONE);
                txt_vip_personal.setVisibility(View.GONE);
                txt_vipplus_personal.setVisibility(View.GONE);
            }

        }

    }

    private void Casting() {
        imgbasic = findViewById(R.id.imgBasic);
        imgvip = findViewById(R.id.imgVip);
        imgvipplus = findViewById(R.id.imgVipPlus);
        txt_basic_personal = findViewById(R.id.txt_basic_personal);
        txt_vip_personal = findViewById(R.id.txt_vip_personal);
        txt_vipplus_personal = findViewById(R.id.txt_vipplus_personal);
    }

    public void fun_basic_personal(View view) {
        startActivity(new Intent( getApplicationContext() ,new_account.class )
                .putExtra("flag" , "bp"));
    }

    public void fun_basic_family(View view) {
        startActivity(new Intent( getApplicationContext() ,new_account.class )
                .putExtra("flag" , "bf"));
    }

    public void fun_vip_presonal(View view) {
        startActivity(new Intent( getApplicationContext() ,new_account.class )
                .putExtra("flag" , "vp"));
    }

    public void fun_vip_family(View view) {
        startActivity(new Intent( getApplicationContext() ,new_account.class )
                .putExtra("flag" , "vf"));
    }

    public void fun_vipplus_personal(View view) {
        startActivity(new Intent( getApplicationContext() ,new_account.class )
                .putExtra("flag" , "vvp"));
    }

    public void fun_vipplus_family(View view) {
        startActivity(new Intent( getApplicationContext() ,new_account.class )
                .putExtra("flag" , "vvf"));
    }
}