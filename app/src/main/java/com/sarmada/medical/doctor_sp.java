package com.sarmada.medical;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class doctor_sp extends AppCompatActivity {


    DoctorsSpAdabter adapter ;
    RecyclerView doctor_sp_recycle ;
    ArrayList<doctor_sp_items> items , searchitems ;
    EditText edsearch ;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_sp);

        String cat_id = getIntent().getExtras().getString("cat_id");

        Casting();
        // تعريف خلية بعدد عنصرين في الصف الواحد
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this , 1);
        // خلية مع تحريك افقي
        gridLayoutManager.setOrientation( GridLayoutManager.VERTICAL );
        // ربط القائمة المتحركة مع الخلية المعرفة
        doctor_sp_recycle.setLayoutManager(gridLayoutManager);

        // نهيئة قائمة الفئات
        items = new ArrayList<>();
        searchitems = new ArrayList<>();

        LoadData();

        edsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter = null ;
                searchitems.clear();
                String stsearch = edsearch.getText().toString();
                for (int j = 0; j < items.size(); j++) {
                    if(items.get(j).getSp().contains(stsearch)){
                        searchitems.add(new doctor_sp_items(
                                items.get(j).getId() ,
                                items.get(j).getSp(),
                                cat_id) );
                    }
                }
                adapter = new DoctorsSpAdabter( searchitems , getApplicationContext());
                // ربط القائمة المتحركة مع كلاس الربط
                doctor_sp_recycle.setAdapter( adapter );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void Casting() {
        doctor_sp_recycle = findViewById(R.id.doctor_sp_recycle);
        edsearch = findViewById(R.id.edsearch);
        progressBar = findViewById(R.id.progressBar);
    }

    private void LoadData() {

        String cat_id = getIntent().getExtras().getString("cat_id");

        //get the sp
        StringRequest request_medical_service = new StringRequest(
                Request.Method.POST,
                config.display_doctor_sp,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject object = array.getJSONObject(i);
                            items.add (
                                    new doctor_sp_items(object.getString("id") ,
                                            object.getString("doctor_sp"),
                                            cat_id));

                        }
                        adapter = new DoctorsSpAdabter( items , getApplicationContext());
                        // ربط القائمة المتحركة مع كلاس الربط
                        doctor_sp_recycle.setAdapter( adapter );
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }
        ) ;
        RequestHandler.getInstance(this).addToRequestQueue(request_medical_service);

//        items.add( new doctor_sp_items("" , "جلدية"));
//        items.add( new doctor_sp_items("2" , "اسنان" ) ) ;
//        items.add( new doctor_sp_items("3" ,"نفسي") ) ;
//        items.add( new doctor_sp_items( "4","اطفال وحديثي ولادة") ) ;
//        items.add( new doctor_sp_items("5", "مخ واعصاب" ) ) ;
//        items.add( new doctor_sp_items( "7","عظام" ) ) ;
//        items.add( new doctor_sp_items("8" , "نساء وتوليد" ) ) ;
//        items.add( new doctor_sp_items( "9","انف واذن وحنجرة" ) ) ;
//        items.add( new doctor_sp_items( "10","قلب واوعية دموية" ) ) ;
//        items.add( new doctor_sp_items( "11","باطنية" ) ) ;
//        items.add( new doctor_sp_items("10", "امراض الدم") ) ;
//        items.add( new doctor_sp_items("10", "اورام" ) ) ;
//        items.add( new doctor_sp_items("10", "تخسيس وتغذية" ) ) ;
//        items.add( new doctor_sp_items("10", "جراحة اورام" ) ) ;
//        items.add( new doctor_sp_items("10", "جراحة اوعية دموية" ) ) ;
//        items.add( new doctor_sp_items("10", "جراحة تجميل" ) ) ;
//        items.add( new doctor_sp_items("10", "جراحة عامة" ) ) ;
//        items.add( new doctor_sp_items("10", "جراحة قلب وصدر" ) ) ;
//        items.add( new doctor_sp_items("10", "جهاز هضمي ومناظير" ) ) ;
//        items.add( new doctor_sp_items("10", "حساسية ومناعة" ) ) ;
//        items.add( new doctor_sp_items("10", "حقن مجهري واطفال انابيب" ) ) ;
//        items.add( new doctor_sp_items("10", "ذكورة وعقم" ) ) ;
//        items.add( new doctor_sp_items("10", "روماتيزم" ) ) ;
//        items.add( new doctor_sp_items("10", "سكر وغدد صماء" ) ) ;
//        items.add( new doctor_sp_items("10", "سمعيات" ) ) ;
//        items.add( new doctor_sp_items("10", "صدر وجهاز تنفسي" ) ) ;
//        items.add( new doctor_sp_items("10", "طب الأسرة" ) ) ;
//        items.add( new doctor_sp_items("10", "طب المسنين" ) ) ;
//        items.add( new doctor_sp_items("10",  "طب تقويمي" ) ) ;
//        items.add( new doctor_sp_items("10",  "علاج الآلام" ) ) ;
//        items.add( new doctor_sp_items("10",  "علاج طبيعي واصابات ملاعب" ) ) ;
//        items.add( new doctor_sp_items("10",  "عيون" ) ) ;
//        items.add( new doctor_sp_items("10",  "كبد" ) ) ;
//        items.add( new doctor_sp_items("10",  "كلى" ) ) ;
//        items.add( new doctor_sp_items("10",  "مسالك بولية" ) ) ;



    }


}

class DoctorsSpAdabter extends RecyclerView.Adapter<DoctorsSpAdabter.ViewHolder>{

    // تعريف قائمة
    ArrayList<doctor_sp_items> list;
    private final Context context;

    // Constructor
    public DoctorsSpAdabter(ArrayList<doctor_sp_items> list, Context context) {
        // تعريف LawyerAdabter
        this.list = list;
        // تعريف المحتوى
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // كلاس الذي يحتوي علي
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_sp_row , parent , false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        // ربط قائمة الفئات مع واجهه غنصر القائمة المتحركة
        holder.name.setText(" "+list.get(position).getSp());

        holder.liner.setOnClickListener(view ->
                context.startActivity(new Intent( context , doctors.class)
                .putExtra("sp_id",list.get(position).getId())
                .putExtra("cat_id",list.get(position).getCat_id())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));

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
            //   area = itemView.findViewById( R.id.area ) ;
            liner = itemView.findViewById(R.id.liner) ;
        }

    }

}