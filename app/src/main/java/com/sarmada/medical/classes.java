package com.sarmada.medical;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class classes extends AppCompatActivity {

    TextView txt_alert ;
    ClasspAdabter adapter ;
    RecyclerView class_recycle ;
    ArrayList<class_item> items ;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        items = new ArrayList<>();

        // casting with the view
        casting();

        // تعريف خلية بعدد عنصرين في الصف الواحد
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this , 1);
        // خلية مع تحريك افقي
        gridLayoutManager.setOrientation( GridLayoutManager.VERTICAL );
        // ربط القائمة المتحركة مع الخلية المعرفة
        class_recycle.setLayoutManager(gridLayoutManager);

        // Load the data
        LoadData();

    }

    private void casting() {
        class_recycle = findViewById(R.id.class_recycle);
        progressBar = findViewById(R.id.progressBar);
        txt_alert   = findViewById(R.id.txt_alert);
    }

    private void LoadData() {

        //get the sp
        StringRequest request_medical_service = new StringRequest(
                Request.Method.POST,
                config.classes,
                response -> {
                    Log.e("LESSONLOG" , response);
                    try {
                        int Counter = 0 ;
                        JSONObject jsonObject = new JSONObject( response );
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject object = array.getJSONObject(i);
                            items.add (
                                    new class_item(
                                            object.getString("id") ,
                                            object.getString("class") ,
                                            object.getString("zoom_link"))
                            );
                            Counter ++;
                        }

                        if(Counter == 0){
                            txt_alert.setVisibility(View.VISIBLE);
                        }

                        adapter = new ClasspAdabter( items , getApplicationContext());
                        class_recycle.setAdapter( adapter );
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {}
        );
        RequestHandler.getInstance(this).addToRequestQueue(request_medical_service);
    }

}

class ClasspAdabter extends RecyclerView.Adapter<ClasspAdabter.ViewHolder>{

    // تعريف قائمة
    ArrayList<class_item> list;
    private  Context context;

    // Constructor
    public ClasspAdabter(ArrayList<class_item> list, Context context) {
        // تعريف LawyerAdabter
        this.list = list;
        // تعريف المحتوى
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // كلاس الذي يحتوي علي
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_row , parent , false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
//        hold the values :
        String name = list.get(position).getName();
        holder.name.setText(name);
        holder.name.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getZoom()));
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        });
    }

    // دالة تحديد عدد وعناصر ومواقع القائمة المتحركة
    @Override
    public int getItemCount() {
        return  (null != list ? list.size(): 0 );
    }

    // الربط مع واجهه عنصر القائمة المتحركة
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // تعريف محتويات واجهه العنصر
        TextView name ;
        LinearLayout liner;
        // الربط مع واجهه العنصر
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById( R.id.name ) ;
            liner = itemView.findViewById(R.id.liner) ;
        }

    }

}
