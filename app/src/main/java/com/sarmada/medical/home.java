package com.sarmada.medical;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences preferences ;
    String id , name , sub ;
    RecyclerView recycle_cat ;
    TextView customer_name ;
    // شريط الاداء
    ProgressBar progressBar ;
    CatAdapter adapter ;
    ArrayList<cat_item> items ;
    private DrawerLayout drawer;
    Toolbar toolbar;
    TextView txt_customer_name , notify  ;
    ImageView slide_image ;

    int cimi ;
    int img[] = {R.drawable.banar1 , R.drawable.banar2 , R.drawable.banar3 , R.drawable.banar4 , R.drawable.banner5 , R.drawable.banar6 , R.drawable.banar7};

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // التحقق من الاصدار
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            method_check_version();
        }
        // الربط
        Casting();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                @SuppressLint("ResourceType") Animation animationfadein = AnimationUtils.loadAnimation(getApplicationContext() , R.animator.fade_in);
                @SuppressLint("ResourceType") Animation animationltr = AnimationUtils.loadAnimation(getApplicationContext() , R.animator.ltr);
                @SuppressLint("ResourceType") Animation animationrtl = AnimationUtils.loadAnimation(getApplicationContext() , R.animator.rtl);
//                AnimationSet s = new AnimationSet(false);
//                s.addAnimation(animationrtl);
////                s.addAnimation(animationfadein);
////                s.addAnimation(animationltr);


                slide_image.startAnimation(animationfadein);
                slide_image.postDelayed(this, 8000);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    slide_image.setImageDrawable(getResources().getDrawable(img[cimi],getApplicationContext().getTheme()));
                }else{
                    slide_image.setImageDrawable(getResources().getDrawable(img[cimi]));
                }
                cimi ++ ;
                cimi = cimi%img.length;


            }
        };
        slide_image.postDelayed(runnable , 8000);



        // تعريف خلية بعدد عنصرين في الصف الواحد
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this , 2);
        // خلية مع تحريك افقي
        gridLayoutManager.setOrientation( GridLayoutManager.VERTICAL );
        // ربط القائمة المتحركة مع الخلية المعرفة
        recycle_cat.setLayoutManager(gridLayoutManager);
        // نهيئة قائمة الفئات
        items = new ArrayList<>();

        //local variable
        preferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        id = preferences.getString("sh_id" , "0");
        name = preferences.getString("sh_name" , "0");
        sub = preferences.getString("sh_sub" , "0");

        if(preferences.getString("sh_id", "0").equals("0")){
            notify.setVisibility(View.GONE);
        }else {
            StringRequest request_medical_service = new StringRequest(
                    Request.Method.POST,
                    config.display_notifications,
                    response -> {
                        try {
                            JSONObject jsonObject = new JSONObject( response );
                            JSONArray array = jsonObject.getJSONArray("data");
                            notify.setText(""+array.length());
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
                    parms.put("id" , id);
                    return parms;
                }
            } ;
            RequestHandler.getInstance(this).addToRequestQueue(request_medical_service);
        }


        loadData();


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        txt_customer_name = findViewById(R.id.txt_customer_name);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        MenuItem target = menu.findItem(R.id.nav_add);
        MenuItem target1 = menu.findItem(R.id.nav_setting);
        MenuItem target2 = menu.findItem(R.id.nav_medical);

        if(sub.equals("0")){target1.setVisible(false);target2.setVisible(false);};

        if(sub.equals("0") || !sub.contains("Fam"))
        {target.setVisible(false);}

        if(id.equals("0"))
            customer_name.setText(" مرحبا عميل سرمدا ");
        else
            customer_name.setText( "مرحبا " + name);


   }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void method_check_version() {
        StringRequest request_display_versions = new StringRequest(Request.Method.POST,
                config.display_versions,
                response -> {
                    Log.e("VERSIONS" , response+"  ,   C"+BuildConfig.VERSION_CODE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i <jsonArray.length() ; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            if(object.getString("name").equals("sarmada")){
                                String code = object.getString("code");
                                String date = object.getString("date");
                                checkVersion(Integer.parseInt(code) , date);
                                Log.e("VERSIONS" , object.getString("code")+"|"+object.getString("date"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {}
        );
        RequestHandler.getInstance(this).addToRequestQueue(request_display_versions);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkVersion(int newVersionCode , String date) {
        Log.e("VERSIONS" , "DATA:"+newVersionCode+"|"+date);
        // calender now
        Calendar now = Calendar.getInstance();
        // calender date
        Calendar finish = Calendar.getInstance();

        // 4 > 6
        if(newVersionCode > BuildConfig.VERSION_CODE ){

            Log.e("VERSIONSDATE" , newVersionCode+"/"+BuildConfig.VERSION_CODE);
            // في حالة كان اصدار قواعد البيانات اصغر من اصدار  التطبيق
            // هذا يعني انه يتوفر اصدار جديد
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date d = format.parse(date);
                finish.setTime(d);
                Log.e("VERSIONSDATE" , ""+d.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // بعد التاكد من توفر اصدار جديد التحقق من التاريخ
            Long x  = ChronoUnit.DAYS.between(now.toInstant(),finish.toInstant());

            Log.e("VERSIONSDATE" , ""+x);

            if(x > 0  && x < 8){

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(" اصدار جديد  ");
                alertDialogBuilder
                        .setMessage(" يتوفر تحديث جديد لتطبيق Sarmada بإضافات جديدة وحلول لمشكلات سابقة , هل تود تحديثه من المتجر ؟")
                        .setPositiveButton("تعم", (dialog, which) -> {
                            Intent open_store = new Intent( Intent.ACTION_VIEW );
                            open_store.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.sarmada.medical"));
                            startActivity(open_store);
                        }).setNegativeButton("لا", (dialog, which) -> {
                                    dialog.cancel();
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();


            }else if(x < 0) {
                // اغلاق التطبيق نهائيا
                startActivity(new Intent( this , update_application.class ));
                finish();
            }
            Log.e("VERSIONSF" , "N:"+now.getTime()+"|F:"+finish.getTime()+":::"+x );
        }else {



        }
    }
    private void loadData() {
        // عرض الفئات
        StringRequest request_display_cat = new StringRequest(
                Request.Method.POST,
                config.display_cat,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject object = array.getJSONObject(i);
                            items.add (
                                    new cat_item(
                                            object.getString("id") ,
                                            object.getString("img") ,
                                            object.getString("cat") ,
                                            object.getString("active")
                                    ));

                        }
                        adapter = new CatAdapter( items , getApplicationContext());
                        // ربط القائمة المتحركة مع كلاس الربط
                        recycle_cat.setAdapter( adapter );
                        // اخفاء الشريط
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }
        );
        RequestHandler.getInstance(this).addToRequestQueue(request_display_cat);
    }
    private void Casting() {
        customer_name = findViewById( R.id.txt_customer_name);
        recycle_cat   = findViewById(R.id.recycle_cat) ;
        progressBar   = findViewById(R.id.progressBar);
        notify        = findViewById(R.id.notify);
        slide_image   = findViewById(R.id.slide_image);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // الحصول علي id العنصر المحدد
        int id = item.getItemId();


        if (id == R.id.nav_exit) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(home.this);
            alertDialogBuilder.setTitle(" تطبيق سرمدا ");
            alertDialogBuilder
                    .setMessage("تاكيد الخروج من البرنامج")
                    .setCancelable(false)
                    .setPositiveButton("نعم", (dialogInterface, i) -> {
                        // الضغط علي تسجيل الخروج من القائمة
                        startActivity(new Intent(this , login.class));
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("sh_login" , "0");
                        editor.putString("sh_id" , null);
                        editor.putString("sh_name" , null);
                        editor.putString("sh_phone" , null);
                        editor.putString("sh_sub" , null);
                        editor.putString("sh_level" , null);
                        editor.putString("sh_school" , null);
                        editor.apply();
                        Toast.makeText(this, "تم تسجيل الخروج", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("لا" , ((dialogInterface, i) ->
                            dialogInterface.cancel()) );
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        } else if(id == R.id.nav_profile){
            if(name.equals("0")){
                // لم يتم تسجيل الدخول
                // تغيير اسم الملف الشخصي الي الداتا المرضية
                item.setTitle(" الداتا المرضية ");
                startActivity(new Intent( this , ads.class ));
            }else{
                // تم تسجيل الدخول
                // الذهاب الي الملف الشخصي
                startActivity(new Intent( this , profile.class ));
            }

        } else if(id == R.id.nav_home){
          //  startActivity(new Intent( this , home.class ));
        } else if(id == R.id.nav_feedback){
            // الذهاب الي صفحة المشكلات والمقترحات
            startActivity( new Intent( this , feedback.class ) );
        }else if(id == R.id.nav_add){
            startActivity( new Intent( this , ads.class )
                    .putExtra("flag" , "F"));
        }else if(id == R.id.nav_policy){
            Intent privicy_page = new Intent( Intent.ACTION_VIEW , Uri.parse("https://sarmadamedical.com/sarmada_privacy.php") );
            startActivity( privicy_page );
        }else if(id == R.id.nav_medical) {
            startActivity( new Intent( this , medical_report.class ) );
        }else if(id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT ,
                    "قم بتحميل تطبيق سرمدا الطبي من المتجر https://play.google.com/store/apps/details?id=com.sarmada.medical واستمتع بخصومات تصل الي 70% علي اغلب المعاملات الطبية");
            intent.setType("text/plain");
            startActivity(intent);
        }else if(id == R.id.nav_support){
                startActivity(new Intent(
                        this , support.class
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }else{
            startActivity( new Intent( this , setting.class ) );
        }
        return false;
    }
    public void fun_notification(View view) {
        startActivity(new Intent( this , notofications.class ));
    }
}

class CatAdapter extends RecyclerView.Adapter<CatAdapter.ViewHolder>{

    // تعريف قائمة
    ArrayList<cat_item> list;
     final Context context;

    // Constructor
    public CatAdapter(ArrayList<cat_item> list, Context context) {
        // تعريف LawyerAdabter
        this.list = list;
        // تعريف المحتوى
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // كلاس الذي يحتوي علي
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_row , parent , false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        // ربط قائمة الفئات مع واجهه غنصر القائمة المتحركة
        holder.txt_cat.setText(" "+list.get(position).getCat());

        // عرض صورة المجموعة
        Picasso.get().load(config.QR_IMG_URL+list.get(position).getImg())
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

        holder.liner.setOnClickListener(view -> {

            // التاكد من اختيار تصنيف الاطباء
            if(list.get(position).getId().equals("1")){
                // اختيار الاطباء
                // الذهاب الي قائمة الاخصاء
                context.startActivity(new Intent( context , doctor_sp.class )
                        .putExtra("cat_id" , list.get(position).getId())
                        .putExtra("cat_name" , list.get(position).getCat())
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }else{
                // اختيار قسم اخر
                context.startActivity(new Intent( context , main_cat.class )
                        .putExtra("cat_id" , list.get(position).getId())
                        .putExtra("cat_name" , list.get(position).getCat())
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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
        TextView txt_cat ;
        LinearLayout liner;
        ImageView img ;

        // الربط مع واجهه العنصر
        public ViewHolder(View itemView) {
            super(itemView);
            txt_cat = itemView.findViewById(R.id.txt_cat);
            img = itemView.findViewById(R.id.img);
            liner = itemView.findViewById(R.id.liner);
        }

    }

}