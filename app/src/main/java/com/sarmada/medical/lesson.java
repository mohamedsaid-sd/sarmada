package com.sarmada.medical;

import static com.sarmada.medical.config.QR_IMG_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class lesson extends AppCompatActivity {

    String  string_title , string_pref , string_vedio ;
    TextView title , pref ;
    VideoView vedio ;
    Uri uri ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);


        string_title = getIntent().getExtras().getString("title");
        string_pref  = getIntent().getExtras().getString("pref");
        string_vedio = getIntent().getExtras().getString("vedio");

        Casting();

        title.setText(string_title);
        pref.setText(string_pref);
        String urls = QR_IMG_URL +string_vedio;
        uri = Uri.parse(urls);
        vedio.setMediaController(new MediaController(this));
        vedio.setVideoURI(uri);
        vedio.start();

    }

    private void Casting() {
        title = findViewById(R.id.title);
        pref = findViewById(R.id.perf);
        vedio = findViewById(R.id.videoView);
    }

    public void fun_back(View view) {
        super.onBackPressed();
    }
}