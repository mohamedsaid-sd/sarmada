package com.sarmada.medical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class main_cat extends AppCompatActivity {


    EditText edsearch;
    MainCatAdabter adapter ;
    RecyclerView main_cat_recycel ;
    ArrayList<maincat_item> items , searchitems ;
    String cat_id , cat_name;
    ProgressBar progressBar ;
    TextView txt_title ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cat);

        cat_id = getIntent().getExtras().getString("cat_id");
        cat_name = getIntent().getExtras().getString("cat_name");

        Casting();
        // تعريف خلية بعدد عنصرين في الصف الواحد
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this , 1);
        // خلية مع تحريك افقي
        gridLayoutManager.setOrientation( GridLayoutManager.VERTICAL );
        // ربط القائمة المتحركة مع الخلية المعرفة
        main_cat_recycel.setLayoutManager(gridLayoutManager);

        // نهيئة قائمة الفئات
        items = new ArrayList<>();
        searchitems = new ArrayList<>();
        //searchitems = new ArrayList<>();

        LoadData();

        txt_title.setText(cat_name);

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
                    if(items.get(j).getMaincat().contains(stsearch)){
                        searchitems.add( new maincat_item(
                                items.get(j).getId(),
                                items.get(j).getImg(),
                                items.get(j).getMaincat(),
                                items.get(j).getHot_line(),
                                items.get(j).getDescripe(),
                                items.get(j).getStatus(),
                                cat_id // تمرير رقم الفئة مع بيانات المجموعة
                        ));
                    }
                }
                adapter = new MainCatAdabter( searchitems , getApplicationContext());
                // ربط القائمة المتحركة مع كلاس الربط
                main_cat_recycel.setAdapter( adapter );
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
                config.display_main_cat,
        response -> {
                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject object = array.getJSONObject(i);
                            items.add (
                                    new maincat_item(
                                            object.getString("id"),
                                            object.getString("img"),
                                            object.getString("maincat"),
                                            object.getString("hot_line"),
                                            object.getString("descripe"),
                                            object.getString("active"),
                                            cat_id // تمرير رقم الفئة مع بيانات المجموعة
                                    ));
                        }
                        adapter = new MainCatAdabter( items , getApplicationContext());
                        // ربط القائمة المتحركة مع كلاس الربط
                        main_cat_recycel.setAdapter( adapter );
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
                parms.put("cat_id" , cat_id);
                return parms;
            }
        } ;
        RequestHandler.getInstance(this).addToRequestQueue(request_medical_service);

    }

    private void Casting() {
        main_cat_recycel    = findViewById(R.id.main_cat_recycle);
        progressBar         = findViewById(R.id.progressBar);
        txt_title           = findViewById(R.id.txt_title);
        edsearch = findViewById(R.id.edsearch);
    }
}

class MainCatAdabter extends RecyclerView.Adapter<MainCatAdabter.ViewHolder>{

    // تعريف قائمة
    ArrayList<maincat_item> list;
    private final Context context;

    // Constructor
    public MainCatAdabter(ArrayList<maincat_item> list, Context context) {
        // تعريف LawyerAdabter
        this.list = list;
        // تعريف المحتوى
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // كلاس الذي يحتوي علي
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_cat_row , parent , false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        // عرض نص المجموعة
        holder.txt_mcat.setText(list.get(position).getMaincat());

        // التحقق من الخط الساخن
        if (list.get(position).getHot_line().equals("0") || list.get(position).getHot_line().equals("")){
            // اخفاء الخط الساخن
            holder.txt_holine.setVisibility(View.GONE);
        }else{
            // عرض الخط الساخن
            holder.txt_holine.setText(" الخط الساخن : "+list.get(position).getHot_line());
        }

        // التحقق من وصف المجموعة
        if(list.get(position).getDescripe().isEmpty() || list.get(position).getDescripe().equals("")
        || list.get(position).getDescripe().equals(" ")){
            // اخفاء الوصف
            holder.txt_descripe.setVisibility(View.GONE);
        }else{
            // عرض وصف المجموعة
            holder.txt_descripe.setText(list.get(position).getDescripe());
        }

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

       //  ******************************** احداث الضغط علي الازرار*****************************

        // حددث الاتصال بالخط الساخن
        holder.txt_holine.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+list.get(position).hot_line));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        // حدث الضغط علي كارد المجموعة
        holder.liner.setOnClickListener(view ->
                context.startActivity(new Intent( context , medical_cat.class )
                        // تمرير رقم الفئة
                .putExtra("cat_id" , list.get(position).getCat_id())
                        // تمرير اسم الفئة
                .putExtra("cat_name" , list.get(position).getMaincat())
                        // رقم المجموعة
                .putExtra("maincat" , list.get(position).getId())

                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        );


    }

    // دالة تحديد عدد وعناصر ومواقع القائمة المتحركة
    @Override
    public int getItemCount() {
        return  (null != list ? list.size(): 0 );
    }

    // الربط مع واجهه عنصر القائمة المتحركة
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // تعريف محتويات واجهه العنصر
        TextView txt_mcat , txt_holine , txt_descripe;
        ImageView img ;
        LinearLayout liner , nofify_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_mcat = itemView.findViewById( R.id.txt_mcat ) ;
            txt_holine = itemView.findViewById( R.id.hotline ) ;
            txt_descripe = itemView.findViewById( R.id.descripe ) ;
            img = itemView.findViewById( R.id.img ) ;
            liner = itemView.findViewById(R.id.liner) ;
            nofify_layout = itemView.findViewById(R.id.nofify_layout) ;
        }

    }

}