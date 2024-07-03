package com.sarmada.medical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class subjects extends AppCompatActivity {

    SubjectSpAdabter adapter ;
    RecyclerView subject_recycle ;
    ArrayList<subject_item> items , searchitems ;
//    EditText edsearch ;
    ProgressBar progressBar ;

    String school , level ;
    SharedPreferences preferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        //local variable
        preferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        // the post keys :
        school = preferences.getString("sh_school" , "Null");
        level = preferences.getString("sh_level" , "Null");

        Toast.makeText(this, "SR:"+school+level, Toast.LENGTH_SHORT).show();

        Casting();
        // تعريف خلية بعدد عنصرين في الصف الواحد
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this , 1);
        // خلية مع تحريك افقي
        gridLayoutManager.setOrientation( GridLayoutManager.VERTICAL );
        // ربط القائمة المتحركة مع الخلية المعرفة
        subject_recycle.setLayoutManager(gridLayoutManager);

        // نهيئة قائمة الفئات
        items = new ArrayList<>();
//        searchitems = new ArrayList<>();

            // load the date
        LoadData();

    }

    // load the data
    private void LoadData() {

        //get the sp
        StringRequest request_medical_service = new StringRequest(
                Request.Method.POST,
                config.display_subject,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject object = array.getJSONObject(i);
                            items.add (
                                    new subject_item(object.getString("id") , object.getString("name") ));

                        }
                        adapter = new SubjectSpAdabter( items , getApplicationContext());
                        // ربط القائمة المتحركة مع كلاس الربط
                        subject_recycle.setAdapter( adapter );
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }
        ) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("school", school);
                map.put("level", level);
                return map;

            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(request_medical_service);
    }

    private void Casting() {
        subject_recycle = findViewById(R.id.subject_recycle);
//        edsearch = findViewById(R.id.edsearch);
        progressBar = findViewById(R.id.progressBar);
    }

    public void fun_go_to_notofication(View view) {
            startActivity(new Intent( this , notofications.class ));
    }
}

class SubjectSpAdabter extends RecyclerView.Adapter<SubjectSpAdabter.ViewHolder>{

    // تعريف قائمة
    ArrayList<subject_item> list;
    private final Context context;

    // Constructor
    public SubjectSpAdabter(ArrayList<subject_item> list, Context context) {
        // تعريف LawyerAdabter
        this.list = list;
        // تعريف المحتوى
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // كلاس الذي يحتوي علي
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_row , parent , false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
//        hold the values :
          String name = list.get(position).getName();
          holder.name.setText(name);

          holder.liner.setOnClickListener(v -> {
              context.startActivity(new Intent(context , units.class)
                      .putExtra("id" , list.get(position).getId())
                      .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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
        ConstraintLayout holder ;

        // الربط مع واجهه العنصر
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById( R.id.name ) ;
            liner = itemView.findViewById(R.id.liner) ;
//            holder = itemView.findViewById(R.id.holder);
        }

    }

}