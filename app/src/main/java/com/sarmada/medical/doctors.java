package com.sarmada.medical;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.sarmada.medical.items.doctors_Items;

public class doctors extends AppCompatActivity {

    EditText edsearch ;
    DoctorsAdabter adapter ;
    RecyclerView doctor_recycle ;
    ArrayList<doctors_Items> items , searchitems ;
    //EditText edsearch ;


    String sp_id , cat_id  ;

    SharedPreferences preferences ;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        sp_id = getIntent().getExtras().getString("sp_id");
        cat_id = getIntent().getExtras().getString("cat_id");

        Casting();
        // تعريف خلية بعدد عنصرين في الصف الواحد
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this , 1);
        // خلية مع تحريك افقي
        gridLayoutManager.setOrientation( GridLayoutManager.VERTICAL );
        // ربط القائمة المتحركة مع الخلية المعرفة
        doctor_recycle.setLayoutManager(gridLayoutManager);

        // نهيئة قائمة الفئات
        items = new ArrayList<>();
        searchitems = new ArrayList<>();

        LoadData();

        edsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter = null ;
                searchitems.clear();
                String stsearch = edsearch.getText().toString();
                for (int j = 0; j < items.size(); j++) {
                    if(items.get(j).getName().contains(stsearch) || items.get(j).getLocation().contains(stsearch) || items.get(j).getSp_name().contains(stsearch) ){
                        searchitems.add( new doctors_Items(
                                items.get(j).getImg(),
                                items.get(j).getId(),
                                items.get(j).getName() ,
                                items.get(j).getPhone() ,
                                items.get(j).getSp_name() ,
                                items.get(j).getLocation(),
                                items.get(j).getWork_time(),
                                items.get(j).getMaxbasic(),
                                items.get(j).getMaxvip(),
                                items.get(j).getLink(),
                                items.get(j).getCv() ,
                                cat_id));
                    }
                }
                adapter = new DoctorsAdabter( searchitems , getApplicationContext());
                // ربط القائمة المتحركة مع كلاس الربط
                doctor_recycle.setAdapter( adapter );
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void LoadData() {

        //get the sp
        StringRequest request_medical_service = new StringRequest(
                Request.Method.POST,
                config.display_doctos,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject object = array.getJSONObject(i);
                            items.add (
                                    new doctors_Items(
                                            object.getString("img"),
                                            object.getString("id"),
                                            object.getString("name") ,
                                            object.getString("phone") ,
                                            object.getString("sp_name") ,
                                            object.getString("address") ,
                                            object.getString("work_time"),
                                            object.getString("maxbasic") ,
                                            object.getString("maxvip"),
                                            object.getString("link"),
                                            object.getString("cv"),
                                            cat_id
                                            ));
                        }
                        adapter = new DoctorsAdabter( items , getApplicationContext());
                        // ربط القائمة المتحركة مع كلاس الربط
                        doctor_recycle.setAdapter( adapter );
                        progressBar.setVisibility(View.GONE);
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
                parms.put("id" , sp_id);
                return parms;
            }
        } ;
        RequestHandler.getInstance(this).addToRequestQueue(request_medical_service);

    }


    private void Casting() {
        doctor_recycle = findViewById(R.id.doctor_recycle);
        progressBar    = findViewById(R.id.progressBar);
        edsearch       = findViewById(R.id.edsearch);
    }
}

class DoctorsAdabter extends RecyclerView.Adapter<DoctorsAdabter.ViewHolder>{

    // متغيير محلي
    SharedPreferences preferences ;

    // تعريف قائمة
    ArrayList<doctors_Items> list;
    Context context ;

    // Constructor
    public DoctorsAdabter(ArrayList<doctors_Items> list, Context context) {
        // تعريف LawyerAdabter
        this.list = list;
        this.context = context ;
        // تعريف المحتوى
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // كلاس الذي يحتوي علي
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_row , parent , false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        // المتغيير المحلي
        preferences = context.getSharedPreferences("LOGIN" , Context.MODE_PRIVATE);
        // اشتراك العميل
        String sub = preferences.getString("sh_sub" , "Null");
        // التاكد من ان السعر غير خالي
        if(sub.isEmpty() || sub.isEmpty() || sub.isEmpty()) {

        }else {

            Log.e("TRACK " , list.get(position).getMaxvip()+list.get(position).getMaxbasic());
            // التاكد من الطبيب
            // وضع النسبة حسب نوع الاشتراك
            if(sub.contains("VIP")){
                if(list.get(position).getMaxvip() != "null"){
                    holder.present.setVisibility(View.VISIBLE);
                    holder.name.setText(list.get(position).getVip()+"%");
                    holder.present.setText(" تصل الي "+list.get(position).getMaxvip()+"%");
                }
            }else {
                if (list.get(position).getMaxbasic() != "null") {
                    holder.present.setVisibility(View.VISIBLE);
                    holder.name.setText(list.get(position).getVip() + "%");
                    holder.present.setText(" تصل الي " + list.get(position).getMaxbasic() + "%");
                }
            }
        }

        if(list.get(position).getId().equals("645")){
            Log.e("PPPPPPPPPPPPPPPPP" ,":"+list.get(position).getMaxvip()+list.get(position).getMaxbasic()  );
        }

        boolean flag_is_day = false ;
        boolean flag_is_time = false ;

        //Time variables To Ckeck
        Integer DAY = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        String curruntday = "";

        switch (DAY){
            case Calendar.SATURDAY:
                curruntday = "sat";
                break;
            case Calendar.SUNDAY:
                curruntday = "sun";
                break;
            case Calendar.MONDAY:
                curruntday = "mon";
                break;
            case Calendar.WEDNESDAY:
                curruntday = "wen";
                break;
            case Calendar.TUESDAY:
                curruntday = "tus";
                break;
            case Calendar.THURSDAY:
                curruntday = "thru";
                break;
            case Calendar.FRIDAY:
                curruntday = "fri";
                break;
        }

        Integer currenthour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        Integer currruntsecond = Calendar.getInstance().get(Calendar.SECOND);
        // تعريف المتغيرات النصية للتعامل معها
        // اسم المؤسسة
        String name      = list.get(position).getName();
        // رقم المؤسسة
        String phone     = list.get(position).getPhone();

        String link      = list.get(position).getLink();
        // اسم التخصص
        String sp_name   = list.get(position).getSp_name();
        // عنوان المؤسسة
        String location  = list.get(position).getLocation();

        String work_time = list.get(position).getWork_time();

        // متغير الدخول
        String login = preferences.getString("sh_login" , "0");

        holder.price.setVisibility(View.GONE);
        holder.pprice.setVisibility(View.GONE);

        String work = null;
        Calendar nowcalender , startcalender , endcalender ;
        nowcalender = Calendar.getInstance();
        startcalender = Calendar.getInstance();
        endcalender = Calendar.getInstance();

        // ************ get work time
        // and check it
        try {
            
            JSONArray jsonarray = new JSONArray(work_time);
            JSONArray array = jsonarray.getJSONArray(0);
            String day = null ;
            String daytxt = null ;
            String start = null ;
            String end = null ;
            
            String OPEN_HOUR_TXT = null, CURRENT_HOUR_TXT , CLOSE_HORE_TXT = null;
            Integer OPEN_HOUR , CURRENT_HOUR , CLOSE_HORE ;
            String OPEN_MENUTE_TXT = null , CURRENT_MENUTE_TXT , CLOSE_MENUTE_TXT = null;
            Integer OPEN_MENUTE , CURRENT_MENUTE , CLOSE_MENUTE ;

            work = "\n" ;
            for (int i = 0; i < array.length(); i++) {
                if (i % 3 == 0) {
                    day = array.getString(i);
                    if (Objects.equals(day, "sat")) {
                        daytxt = "يوم السبت : ";
                    } else if (Objects.equals(day, "sun")) {
                        daytxt = "يوم  الاحد : ";
                    } else if (Objects.equals(day, "mon")) {
                        daytxt = "يوم  الاثنين : ";
                    } else if (Objects.equals(day, "tus")) {
                        daytxt = "يوم  الثلاثاء : ";
                    } else if (Objects.equals(day, "wen")) {
                        daytxt = "يوم  الاربعاء  : ";
                    } else if (Objects.equals(day, "thru")) {
                        daytxt = "يوم  الخميس : ";
                    } else if (Objects.equals(day, "fri")) {
                        daytxt = "يوم  الجمعة : ";
                    }
                    work += daytxt ;
                } else if (i % 3 != 2) {
                    start = array.getString(i);
                    String time[] = start.split(":");
                    OPEN_HOUR_TXT = time[0];
                    OPEN_MENUTE_TXT = time[1];
                    startcalender.set(Calendar.HOUR_OF_DAY , Integer.parseInt(OPEN_HOUR_TXT));
                    startcalender.set(Calendar.MINUTE , Integer.parseInt(OPEN_MENUTE_TXT));
                    work += start  ;
                    work += " - ";
                }  else if (i % 3 != 1) {
                    end = array.getString(i);
                    String time[] = end.split(":");
                    CLOSE_HORE_TXT = time[0];
                    CLOSE_MENUTE_TXT = time[1];
                    endcalender.set(Calendar.HOUR_OF_DAY , Integer.parseInt(CLOSE_HORE_TXT));
                    endcalender.set(Calendar.MINUTE , Integer.parseInt(CLOSE_MENUTE_TXT));
                    work += end ;
                    work += "\n";
                }
                if(curruntday.equals(day)){
                    if(i % 3 != 1){
                        flag_is_day = true ;
                        if(nowcalender.after(startcalender) && nowcalender.before(endcalender)){
                            flag_is_time = true ;
                        }
                    }
                }
            }

            if(flag_is_time){
                holder.open.setText(" مفتوح ");
                holder.open.setTextColor(Color.DKGRAY);
            }else {
                holder.open.setText(" مغلق ");
                holder.open.setTextColor(Color.RED);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String finalWork = work;
        holder.liner.setOnClickListener(view -> {
            // التاكد من الاشتراك
//            if(login.equals("1")){
                // تم تسجيل الدخول
                // الذهاب الي شاشة بيانات المؤسسة
                context.startActivity(new Intent( context , detailes.class )
                        .putExtra("id" , list.get(position).getId())
                        .putExtra("img" , list.get(position).getImg())
                        .putExtra("name" , name)
                        .putExtra("work_time" , finalWork)
                        .putExtra("address" , location)
                        .putExtra("phone" , phone)
                        .putExtra("link" , link)
                        .putExtra("cv" , list.get(position).getCv())
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            }else{
//                // الاشتراك المجاني
//                // الذهاب الي شاشة الاعلانات
//                context.startActivity(new Intent( context , ads.class )
//                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            }
        });


        if(list.get(position).getName().length() > 25)
        holder.name.setText(list.get(position).getName().substring(0,25)+"...");
        else holder.name.setText(list.get(position).getName());

        if(list.get(position).getCat_id().equals("6")){
            if(list.get(position).getCv().length() > 30)
                holder.sp.setText(list.get(position).getCv().substring(0,30)+"...");
            else holder.sp.setText(list.get(position).getCv());
        }else {
            if(list.get(position).getSp_name().length() > 30)
                holder.sp.setText(list.get(position).getSp_name().substring(0,30)+"...");
            else holder.sp.setText(list.get(position).getSp_name());
        }



        if(list.get(position).getLocation().length() > 25)
        holder.location.setText(list.get(position).getLocation().substring(0,25)+"...");
        else holder.sp.setText(list.get(position).getLocation());

        Picasso.get().load(config.QR_IMG_URL+list.get(position)
                        .getImg())
                .into(holder.img, new Callback() {
                    @Override
                    public void onSuccess() {}
                    @Override
                    public void onError(Exception e) {
                        String uri = "@drawable/loooo";
                        int imgresourse = context.getResources().getIdentifier(uri , null , context.getPackageName());
                        Drawable drawable = context.getResources().getDrawable(imgresourse);
                        holder.img.setImageDrawable(drawable);
                    }
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
        TextView name , sp , location , present , price , pprice , open  ;
        LinearLayout liner;
        ImageView img ;

        // الربط مع واجهه العنصر
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById( R.id.doctor_name ) ;
            sp = itemView.findViewById( R.id.doctor_sp ) ;
            location = itemView.findViewById( R.id.doctor_loc ) ;
            present = itemView.findViewById( R.id.present ) ;
            price = itemView.findViewById( R.id.txtprice ) ;
            pprice = itemView.findViewById( R.id.txtpprice ) ;
            open = itemView.findViewById( R.id.txtopen) ;
            //   area = itemView.findViewById( R.id.area ) ;
            liner = itemView.findViewById(R.id.liner) ;
            img = itemView.findViewById(R.id.img) ;

        }

    }

}