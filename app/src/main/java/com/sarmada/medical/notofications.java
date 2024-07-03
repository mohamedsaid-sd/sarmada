package com.sarmada.medical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class notofications extends AppCompatActivity {


    NotoficationAdabter adapter ;
    RecyclerView main_cat_recycel ;
    ArrayList<notofication_item> items ;
    String cat_id , cat_name;
    ProgressBar progressBar ;
    TextView txt_title ;
    SharedPreferences preferences ;
    String customer_id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notofications);

        preferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        if(preferences.getString("sh_id" , "0").equals("0")){
            Toast.makeText(this, "الرجاء تسجيل الدخول لعرض الاشعارات", Toast.LENGTH_SHORT).show();
        }else {
            customer_id = preferences.getString("sh_id" , "0");
        }

        Casting();
        // تعريف خلية بعدد عنصرين في الصف الواحد
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this , 1);
        // خلية مع تحريك افقي
        gridLayoutManager.setOrientation( GridLayoutManager.VERTICAL );
        // ربط القائمة المتحركة مع الخلية المعرفة
        main_cat_recycel.setLayoutManager(gridLayoutManager);

        // نهيئة قائمة الفئات
        items = new ArrayList<>();
        //searchitems = new ArrayList<>();

        LoadData();
    }


    private void LoadData() {

        //get the sp
        StringRequest request_medical_service = new StringRequest(
                Request.Method.POST,
                config.display_notifications,
                response -> {
                    Log.e("ERREREREREREEERRERE" ,response);

                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        JSONArray array = jsonObject.getJSONArray("data");
                        Log.e("TTTTT" ,""+array.length());
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject object = array.getJSONObject(i);
                            items.add (
                                    new notofication_item(
                                            object.getString("id"),
                                            object.getString("content"),
                                            object.getString("img")
                                    ));
                        }
                        adapter = new NotoficationAdabter( items , getApplicationContext());
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
                parms.put("id" , customer_id);
                return parms;
            }
        } ;
        RequestHandler.getInstance(this).addToRequestQueue(request_medical_service);

    }

    private void Casting() {
        main_cat_recycel    = findViewById(R.id.notofication_recycle);
        progressBar         = findViewById(R.id.progressBar);
        txt_title           = findViewById(R.id.txt_title);
    }

    public void fun_back(View view) {
        super.onBackPressed();
    }
}

class NotoficationAdabter extends RecyclerView.Adapter<NotoficationAdabter.ViewHolder>{

    // تعريف قائمة
    ArrayList<notofication_item> list;
    private final Context context;

    // Constructor
    public NotoficationAdabter(ArrayList<notofication_item> list, Context context) {
        // تعريف LawyerAdabter
        this.list = list;
        // تعريف المحتوى
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // كلاس الذي يحتوي علي
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notofication_row , parent , false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        // make the textview clickable
        holder.txt_name.setMovementMethod(LinkMovementMethod.getInstance());

        holder.txt_name.setText(list.get(position).getName());

        // hide image if no image pass
        if(list.get(position).getImg().isEmpty()){
            holder.img_image.setVisibility(View.GONE);
        }


        Picasso.get().load(config.QR_IMG_URL+list.get(position)
                        .getImg())
                .into(holder.img_image, new Callback() {
                    @Override
                    public void onSuccess() {}
                    @Override
                    public void onError(Exception e) {
                        String uri = "@drawable/loooo";
                        int imgresourse = context.getResources().getIdentifier(uri , null , context.getPackageName());
                        Drawable drawable = context.getResources().getDrawable(imgresourse);
                        holder.img_image.setImageDrawable(drawable);
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
        TextView txt_name ;
        ImageView img_image;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById( R.id.name ) ;
            img_image = itemView.findViewById( R.id.img ) ;
            txt_name.setMovementMethod(LinkMovementMethod.getInstance());

        }

    }

}