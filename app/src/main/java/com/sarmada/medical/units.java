package com.sarmada.medical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class units extends AppCompatActivity {

   UnitspAdabter adapter ;
    RecyclerView subject_recycle ;
    ArrayList<unit_item> items , searchitems ;
    ProgressBar progressBar ;
    String subject_id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units);
        // get the subject id
        subject_id = getIntent().getExtras().getString("id");
//        Toast.makeText(this, "Subject id = "+subject_id, Toast.LENGTH_SHORT).show();

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
                config.display_unit,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject object = array.getJSONObject(i);
                            items.add (
                                    new unit_item(object.getString("id") , object.getString("unit") ));
                        }
                        adapter = new UnitspAdabter( items , getApplicationContext());
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
                map.put("subject", subject_id);
                return map;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(request_medical_service);
    }

    private void Casting() {
        subject_recycle = findViewById(R.id.unit_recycle);
        progressBar = findViewById(R.id.progressBar);
    }
}
class UnitspAdabter extends RecyclerView.Adapter<UnitspAdabter.ViewHolder>{

    // تعريف قائمة
    ArrayList<unit_item> list;
    private final Context context;

    // Constructor
    public UnitspAdabter(ArrayList<unit_item> list, Context context) {
        // تعريف LawyerAdabter
        this.list = list;
        // تعريف المحتوى
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // كلاس الذي يحتوي علي
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.units_row , parent , false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
//        hold the values :
        String name = list.get(position).getName();
        holder.name.setText(name);

        holder.liner.setOnClickListener(v -> {
            context.startActivity(new Intent(context , lessons.class)
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