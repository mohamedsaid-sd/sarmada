package com.sarmada.medical;

import android.app.Activity;
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
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class feedback extends AppCompatActivity {

    SharedPreferences preferences ;
    EditText edfeedback ;
    String c_id , feed , cat , encoding  ;
    LinearLayout liner_feed , liner_success ;
    private static final int PERMISSIONS_IMAGE = 20;
    final int PIC_CROP = 1;
    Spinner spinner ;
    ArrayList<String> ar_spiner ;
    Bitmap myBitmap;
    ImageView img ;
    Uri picUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        // المتغيير المحلي
        preferences = getSharedPreferences("LOGIN" , Context.MODE_PRIVATE);

        // الربط مع الواجهات
        casting();

        // ملء مصفوفة الخيارات بتاعت الاسباينو
        ar_spiner = new ArrayList<>();
        ar_spiner.add(" استفسار  ");
        ar_spiner.add(" اقتراح  ");
        ar_spiner.add(" مشكلة تقنية  ");
        ar_spiner.add("  مساعدة في كيفية استخدام التطبيق  ");
        ar_spiner.add("  مشكلة مع مؤسسة طبية  ");
        ArrayAdapter<String> adapter_spinner = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, ar_spiner);
        spinner.setAdapter(adapter_spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cat = ar_spiner.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void casting() {
        // النصوص
        edfeedback = findViewById(R.id.edfeedback);
        // القوالب
        liner_feed = findViewById(R.id.liner_feed);
        liner_success = findViewById(R.id.liner_success);
        spinner       = findViewById(R.id.spinner);
        img           = findViewById(R.id.img);
    }

    public void fun_send_feedback(View view) {

        // المتغييرات النصية
        c_id = preferences.getString("sh_id" , "0");
        feed = edfeedback.getText().toString() ;

        // طباعة كل القييم الممررة للتاكد منها
        // الفئة - الصورة - العميل - النص المكتوب


        Bitmap Bimg = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bimg.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        encoding = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT); // picture


        if(feed.isEmpty()){
            Toast.makeText(this, "الرجاء اضافة بعض المشاكل اول المقترحات", Toast.LENGTH_SHORT).show();
        }else{

            if(c_id.equals("0")){
                Toast.makeText(this, "ألرجاء الاشتراك لارسال الملاحظات والتعليقات", Toast.LENGTH_SHORT).show();
            }else{
                // ارسال المشكلات والمقترحات
                StringRequest request_send_feedback = new StringRequest(
                        Request.Method.POST,
                        config.add_feedback,
                        response -> {
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                // التاكد من عملية الاضافة
                                if (jsonObject.get("data").equals(0)) {
                                    // لم تتم الاضافة
                                    Toast.makeText(this, "خطا في عملية الاضافة", Toast.LENGTH_SHORT).show();
                                } else {
                                    // تمت الاضافة بنجاح
                                    // اخفاء الصفحة
                                    liner_feed.setVisibility(View.GONE);
                                    // اظهار رسالة الشكر
                                    liner_success.setVisibility(View.VISIBLE);
                                    // الانتقال للشاشة الملف الشخصي بعد 5 ثوان
                                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                        startActivity(new Intent(this, profile.class));
                                        finish();
                                    }, 5000);
                                }
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
                        map.put("c_id", c_id);
                        map.put("feed", feed);
                        map.put("cat", cat);
                        map.put("img", encoding);
                        return map;
                    }
                };

                //**************************************
                RequestHandler.getInstance(this).addToRequestQueue(request_send_feedback);

            }
        }
    }

    // التعامل مع الصورة
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
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }
    public void fun_back(View view) {
        super.onBackPressed();
    }
    public void fun_attach_image(View view) {
        startActivityForResult(getPickImageChooserIntent(), PERMISSIONS_IMAGE);
    }
    private Intent getPickImageChooserIntent() {
        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();
        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
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

}