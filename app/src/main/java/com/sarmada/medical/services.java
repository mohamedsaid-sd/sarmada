package com.sarmada.medical;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.icu.text.ListFormatter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import java.util.Map;

public class services extends AppCompatActivity {

    ServicepAdabter adapter ;
    RecyclerView recycle_servies;
    String org_id , org_name ;
    ArrayList<service_item> items ;
    TextView medical_name ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        org_id = getIntent().getExtras().getString("org_id");
        org_name = getIntent().getExtras().getString("org_name");

        casting();
        // تعريف خلية بعدد عنصرين في الصف الواحد
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this , 1);
        // خلية مع تحريك افقي
        gridLayoutManager.setOrientation( GridLayoutManager.VERTICAL );
        // ربط القائمة المتحركة مع الخلية المعرفة
        recycle_servies.setLayoutManager(gridLayoutManager);
        // نهيئة قائمة الفئات
        items = new ArrayList<>();
        //searchitems = new ArrayList<>();
        LoadData();
        medical_name.setText(org_name);

    }

    private void LoadData() {

        //get the sp
        StringRequest request_medical_service = new StringRequest(
                Request.Method.POST,
                config.display_services,
                response -> {
                    Log.e("TTTTTTTT" , ""+response);
                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject object = array.getJSONObject(i);
                            items.add (
                                    new service_item(object.getString("id") ,
                                            object.getString("service") ,
                                            object.getString("price") ,
                                            object.getString("present") ,
                                            object.getString("service_price")));

                        }
                        adapter = new ServicepAdabter( items , getApplicationContext());
                        // ربط القائمة المتحركة مع كلاس الربط
                        recycle_servies.setAdapter( adapter );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }
        ){
            @NonNull
            @Override
            protected Map<String, String> getParams()  {
                HashMap<String,String> parms = new HashMap<>();
                parms.put("id" , org_id);
                return parms;
            }
        }  ;
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

    private void casting() {
        recycle_servies = findViewById(R.id.recycle_servies);
        medical_name    = findViewById(R.id.medical_name);
    }
}

class ServicepAdabter extends RecyclerView.Adapter<ServicepAdabter.ViewHolder>{

    // تعريف قائمة
    ArrayList<service_item> list;
    private final Context context;

    // Constructor
    public ServicepAdabter(ArrayList<service_item> list, Context context) {
        // تعريف LawyerAdabter
        this.list = list;
        // تعريف المحتوى
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // كلاس الذي يحتوي علي
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_row , parent , false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        // ربط قائمة الفئات مع واجهه غنصر القائمة المتحركة
        holder.txtname.setText(" "+list.get(position).getName());

        holder.txtpersent.setText("%" +list.get(position).getPresent());


        // التحقق من السعر اذا كان 0 قم باخفاء عرض السعر قبل وبعد التخفيض
        if(list.get(position).getPrice().equals("0")){
            //اخفاء العرض قبل السعر وبعده
            holder.txtprice.setVisibility(View.INVISIBLE);
            holder.txtpprice.setVisibility(View.GONE);
        }else {
            //اظهار العرض قبل السعر وبعده
            holder.txtprice.setText(" "+list.get(position).getPrice() + " ج ");
            holder.txtpprice.setText(" "+list.get(position).getPprice() + " ج ");
            holder.txtprice.setPaintFlags(holder.txtpersent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

    }

    // دالة تحديد عدد وعناصر ومواقع القائمة المتحركة
    @Override
    public int getItemCount() {
        return  (null != list ? list.size(): 0 );
    }

    // الربط مع واجهه عنصر القائمة المتحركة
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // تعريف محتويات واجهه العنصر
        TextView txtname , txtprice , txtpprice , txtpersent ;
        LinearLayout liner;

        // الربط مع واجهه العنصر
        public ViewHolder(View itemView) {
            super(itemView);
            txtname = itemView.findViewById( R.id.txtname ) ;
            txtprice = itemView.findViewById( R.id.txtprice ) ;
            txtpprice = itemView.findViewById( R.id.txtpprice ) ;
            txtpersent = itemView.findViewById( R.id.txtpersent ) ;
            liner = itemView.findViewById(R.id.liner) ;
        }

    }

}