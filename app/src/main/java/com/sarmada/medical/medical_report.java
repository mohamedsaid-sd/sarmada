package com.sarmada.medical;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class medical_report extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    medicalFileAdabter adapter ;
    private static final int PERMISSIONS_IMAGE = 20;
    final int PIC_CROP = 1;
    Uri picUri;
    Bitmap myBitmap;
    ImageView img ;
    EditText edtxt ;
    String id , txt , encoding , rid  ;
    SharedPreferences preferences ;
    RecyclerView rexyle_medical_file;
    ArrayList<medical_file_item> items ;
    Button btn_add , btn_edit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_report);
        preferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        id = preferences.getString("sh_id" , "0");
        Casting();

        if(getIntent().hasExtra("edit")){
            String report = getIntent().getExtras().getString("report");
            String image = getIntent().getExtras().getString("img");
            rid = getIntent().getExtras().getString("rid");

            if(!image.equals("0")){
                Picasso.get().load(config.QR_IMG_URL + image).into(img);
            }

            edtxt.setText(report);
            btn_add.setVisibility(View.GONE);
            btn_edit.setVisibility(View.VISIBLE);
        }



        // تعريف خلية بعدد عنصرين في الصف الواحد
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this , 1);
        // خلية مع تحريك افقي
        gridLayoutManager.setOrientation( GridLayoutManager.VERTICAL );
        // ربط القائمة المتحركة مع الخلية المعرفة
        rexyle_medical_file.setLayoutManager(gridLayoutManager);
        items = new ArrayList<>();
        //searchitems = new ArrayList<>();
        LoadData();
    }

    private void LoadData() {

        StringRequest request_medical_file = new StringRequest(
          Request.Method.POST,
          config.display_medical_file,
          response -> {
              JSONObject jsonObject;
              try {
                  jsonObject = new JSONObject( response );
                  JSONArray array = jsonObject.getJSONArray("data");
                  for (int i = 0; i <array.length() ; i++) {
                      JSONObject object = array.getJSONObject(i);
                      items.add (
                              new medical_file_item(
                                      object.getString("id") ,
                                      object.getString("img") ,
                                      object.getString("txt") ,
                                      object.getString("created_at"),
                                      object.getString("flag")));
                  }
                  adapter = new medicalFileAdabter( items , getApplicationContext());
                  // ربط القائمة المتحركة مع كلاس الربط
                  rexyle_medical_file.setAdapter( adapter );
              } catch (JSONException e) {
                  e.printStackTrace();
              }

          },
          error -> {}
        ){
            @NonNull
            @Override
            protected Map<String, String> getParams()  {
                HashMap<String,String> parms = new HashMap<>();
                parms.put("id" , id );
                return parms;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(request_medical_file);


    }

    private void Casting() {
        btn_add = findViewById(R.id.btn_add);
        btn_edit = findViewById(R.id.btn_edit);
        img = findViewById(R.id.img);
        edtxt = findViewById(R.id.edtxt);
        rexyle_medical_file = findViewById(R.id.recycle_medical_file);
    }

    // ارفاق صورة
    public void fun_attach_image(View view) {
        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }else{
            startActivityForResult(getPickImageChooserIntent() ,  PERMISSIONS_IMAGE );
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    ////////////UPLOAD IMAGE FUNCTIONS///////////////////////////////////////
    private Intent getPickImageChooserIntent() {
        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();
        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        @SuppressLint("QueryPermissionsNeeded") List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }
        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        @SuppressLint("QueryPermissionsNeeded") List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }
        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }

        allIntents.add(mainIntent);
        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[0]));
        return chooserIntent;
    }
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {

            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                    myBitmap = getResizedBitmap(myBitmap, 500);
                    img.setImageURI(picUri);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = (Bitmap) data.getExtras().get("data");
                myBitmap = bitmap;
                img.setImageBitmap(myBitmap);
            }

        }

        if(requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap selectedBitmap = extras.getParcelable("data");
                img.setImageBitmap(selectedBitmap);
            }
        }
    }
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // تعديل التقرير الطبي
    public void fun_edit_medical_file(View view){

        ProgressDialog dialog  = new ProgressDialog(this);
        dialog.setMessage(" جاري اضافة التقرير ");
        dialog.show();

        id = preferences.getString("sh_id" , "0");
        txt = edtxt.getText().toString();

        try {
            Bitmap Bimg = ((BitmapDrawable) img.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bimg.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
            encoding = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT); // picture

        } catch (Exception ex) {
            encoding = "null";
        }

        if (txt.isEmpty()) {
            Toast.makeText(this, " الرجاء إضافة الوصف !  ", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest request_add_medical_report = new StringRequest(Request.Method.POST,
                    config.edit_report_customer,
                    response -> {
                Log.e("dfgdgfdgf" , response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.get("data").equals(1)) {
                                Toast.makeText(this, "تمت تعديل التقرير بنجاح", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this,medical_report.class));
                                finish();
                                dialog.dismiss();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                    }) {
                @NonNull
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> parm = new HashMap<>();
                    parm.put("rid", rid);
                    parm.put("id", id);
                    parm.put("img", encoding);
                    parm.put("txt", txt);
                    return parm;
                }
            };
            RequestHandler.getInstance(this).addToRequestQueue(request_add_medical_report);
        }
    }

    // اضافة للتقرير الطبي
    public void fun_add_medical_file(View view) {

        if (preferences.getString("sh_login", "0").equals("0")) {
            Toast.makeText(this, "الرجاء الاشتراك لارفاق الملف الطبي", Toast.LENGTH_SHORT).show();
        }else {

            ProgressDialog dialog  = new ProgressDialog(this);
            dialog.setMessage(" جاري اضافة التقرير ");
            dialog.show();

            id = preferences.getString("sh_id" , "0");
            txt = edtxt.getText().toString();

            try {
                Bitmap Bimg = ((BitmapDrawable) img.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Bimg.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                encoding = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT); // picture

            } catch (Exception ex) {
                encoding = "null";
            }

            if (txt.isEmpty()) {
                Toast.makeText(this, " الرجاء إضافة الوصف !  ", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {

                StringRequest request_add_medical_report = new StringRequest(Request.Method.POST,
                        config.add_medical_file,
                        response -> {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.get("data").equals(1)) {
                                    Toast.makeText(this, "تمت الاضافة للتقرير", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this,medical_report.class));
                                    finish();
                                    dialog.dismiss();
                                    dialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                        }) {
                    @NonNull
                    @Override
                    protected Map<String, String> getParams() {
                        HashMap<String, String> parm = new HashMap<>();
                        parm.put("id", id);
                        parm.put("img", encoding);
                        parm.put("txt", txt);
                        return parm;
                    }
                };
                RequestHandler.getInstance(this).addToRequestQueue(request_add_medical_report);
            }
        }
    }

    public void fun_back(View view) {
        super.onBackPressed();
    }
}

class medicalFileAdabter extends RecyclerView.Adapter<medicalFileAdabter.ViewHolder>{

    // تعريف قائمة
    ArrayList<medical_file_item> list;
    private final Context context;
    SharedPreferences preferences ;

    // Constructor
    public medicalFileAdabter(ArrayList<medical_file_item> list, Context context) {
        // تعريف LawyerAdabter
        this.list = list;
        // تعريف المحتوى
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // كلاس الذي يحتوي علي
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.medical_file_row , parent , false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {



        preferences = context.getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        if(list.get(position).getImg().equals("0")){
            holder.img.setVisibility(View.GONE);
        }else
        {
            Picasso.get().load(config.QR_IMG_URL + list.get(position).getImg()).into(holder.img);
        }

        // التحقق من ان ان تقرير هو تقرير المستخدم
        if(!list.get(position).getFlag().equals("C")){
            holder.img_del.setVisibility(View.GONE);
            holder.img_edit.setVisibility(View.GONE);
        }



        holder.txt.setText(list.get(position).getTxt());
        holder.date.setText(list.get(position).getDate());

        holder.img_edit.setOnClickListener(v -> {
                context.startActivity(new Intent( context , medical_report.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra("edit" , "ok")
                        .putExtra("report" , list.get(position).getTxt())
                        .putExtra("img" , list.get(position).getImg())
                        .putExtra("rid" , list.get(position).getId()));
        });

        holder.img_del.setOnClickListener(view -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(holder.img_del.getContext());
            alertDialogBuilder.setTitle(" تاكيد الحذف ");
            alertDialogBuilder
                    .setMessage(" هل انت متاكد من حذف العنصر ؟ ")
                    .setCancelable(true)
                    .setPositiveButton("نعم", (dialogInterface, i) -> {

                        @SuppressLint("NotifyDataSetChanged") StringRequest request_delete_medical_report = new StringRequest(Request.Method.POST,
                                config.delete_report_customer,
                                response -> {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.e("rRRRRRRRRRrr",response+"/////"+list.get(position).getId());
                                        if (jsonObject.get("data").equals(1)) {
                                            Toast.makeText(context, "تم حذف  العنصر", Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged();
                                            context.startActivity(new Intent(context,medical_report.class)
                                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                },
                                error -> {
                                }) {
                            @NonNull
                            @Override
                            protected Map<String, String> getParams() {
                                HashMap<String, String> parm = new HashMap<>();
                                parm.put("id", list.get(position).getId().intern());
                                return parm;
                            }
                        };
                        RequestHandler.getInstance(context).addToRequestQueue(request_delete_medical_report);
                    })
                    .setNegativeButton("لا" , ((dialogInterface, i) ->
                            dialogInterface.cancel()) );
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();

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
        TextView txt , date ;
        ImageView img , img_del , img_edit;
        LinearLayout liner;

        // الربط مع واجهه العنصر
        public ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date) ;
            txt = itemView.findViewById(R.id.name) ;
            img = itemView.findViewById(R.id.img) ;
            img_del = itemView.findViewById(R.id.img_del) ;
            img_edit = itemView.findViewById(R.id.img_edit) ;
            liner = itemView.findViewById(R.id.liner) ;
        }

    }

}