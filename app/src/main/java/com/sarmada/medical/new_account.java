package com.sarmada.medical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class new_account extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int PERMISSIONS_IMAGE = 20;
    Uri picUri;
    Bitmap myBitmap;
    ImageView img ;
    EditText edname , edphone , edwhatsapp , edaddress , edsub ;
    String name , encoding , phone , whatsapp , address , sub ;
    EditText edreport1 , edreport2 , edreport3 , edreport4 , edreport5 ;
    String report ;
    EditText eduser , edpass , edpassc ;
    String  user , pass , passc , gander ;

    Spinner spinner ;
    ArrayList<String> ar_spiner ;

    RadioButton meale , female ;

    TextView erorr_alert ;
    SharedPreferences preferences ;

    CountryCodePicker ccp , ccpw;
    private String phoneVerificationId;
    final int PIC_CROP = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        preferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        casting();
        ar_spiner = new ArrayList<>();
        ar_spiner.add("Basic , Personal  ");
        ar_spiner.add("Basic , Family ");
        ar_spiner.add("VIP , Personal ");
        ar_spiner.add("VIP , Family ");
        ar_spiner.add("VIP+ , Personal   ");
        ar_spiner.add("VIP+ , Family  ");
        ArrayAdapter<String> adapter_spinner = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, ar_spiner);
        spinner.setAdapter(adapter_spinner);

        if(getIntent().getExtras().getString("flag").equals("bp")){
            sub = ar_spiner.get(0);
            spinner.setSelection(0);
            spinner.setEnabled(false);
        } else if(getIntent().getExtras().getString("flag").equals("bf")){
            sub = ar_spiner.get(1);
            spinner.setSelection(1);
            spinner.setEnabled(false);
        }else if(getIntent().getExtras().getString("flag").equals("vp")){
            sub = ar_spiner.get(2);
            spinner.setSelection(2);
            spinner.setEnabled(false);
        }else if(getIntent().getExtras().getString("flag").equals("vf")){
            sub = ar_spiner.get(3);
            spinner.setSelection(3);
            spinner.setEnabled(false);
        }else if(getIntent().getExtras().getString("flag").equals("vvp")){
            sub = ar_spiner.get(4);
            spinner.setSelection(4);
            spinner.setEnabled(false);
        }else if(getIntent().getExtras().getString("flag").equals("vvf")){
            sub = ar_spiner.get(5);
            spinner.setSelection(5);
            spinner.setEnabled(false);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //sub = ar_spiner.get(i);
                //Toast.makeText(new_account.this, "Select : "+ar_spiner.get(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(meale.isChecked()){
            gander = "meal";
        }else{
            gander = "female";
        }

        ccp.registerCarrierNumberEditText(edphone);
        ccp.setPhoneNumberValidityChangeListener(isValidNumber -> {
            if (!isValidNumber) {
                edphone.setBackground(ContextCompat.getDrawable(this, R.drawable.redroundboundbackground));
                edphone.setTextColor(getResources().getColor(R.color.red));
            } else {
                edphone.setBackground(ContextCompat.getDrawable(this, R.drawable.whiterobackground));
                edphone.setTextColor(getResources().getColor(R.color.black));
            }
        });

        ccpw.registerCarrierNumberEditText(edwhatsapp);
        ccpw.setPhoneNumberValidityChangeListener(isValidNumber -> {
            if (!isValidNumber) {
                edwhatsapp.setBackground(ContextCompat.getDrawable(this, R.drawable.redroundboundbackground));
                edwhatsapp.setTextColor(getResources().getColor(R.color.red));
            } else {
                edwhatsapp.setBackground(ContextCompat.getDrawable(this, R.drawable.whiterobackground));
                edwhatsapp.setTextColor(getResources().getColor(R.color.black));
            }
        });


    }

    private void casting() {
        img       = findViewById(R.id.img);
        edname    = findViewById(R.id.edname);
        edphone   = findViewById(R.id.txt_phone);
        edwhatsapp   = findViewById(R.id.txt_whatsapp);
        edaddress = findViewById(R.id.edaddress);
        edreport1 = findViewById(R.id.edreport1);
        edreport2 = findViewById(R.id.edreport2);
        edreport3 = findViewById(R.id.edreport3);
        edreport4 = findViewById(R.id.edreport4);
        edreport5 = findViewById(R.id.edreport5);
        eduser    = findViewById(R.id.eduser);
        edpass    = findViewById(R.id.edpass);
        edpassc   = findViewById(R.id.edpassc);
        spinner   = findViewById(R.id.spinner);
        meale     = findViewById(R.id.male);
        female    = findViewById(R.id.female);
        erorr_alert=findViewById(R.id.txt_erorr_alert);
        ccp = findViewById(R.id.cpp);
        ccpw = findViewById(R.id.cppw);
    }

    public void fun_upload_image(View view) {
        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }else{
            startActivityForResult(getPickImageChooserIntent(), PERMISSIONS_IMAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    private Intent getPickImageChooserIntent() {
        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();
        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }
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

    //deal with upload image result
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

    public void fun_new_request(View view) {

        Toast.makeText(this, "SUB"+sub, Toast.LENGTH_SHORT).show();

        name    = edname.getText().toString();
        phone   = edphone.getText().toString();
        whatsapp = edwhatsapp.getText().toString();
        address = edaddress.getText().toString();
        //sub     = edsub.getText().toString();
        user    = eduser.getText().toString();
        pass    = edpass.getText().toString();
        passc   = edpassc.getText().toString();
        report = "فصيلة دم المريض هي : "+edreport1.getText().toString() + " العمليات التي قام بها هي : " +edreport2.getText().toString() + " ياخذ المريض هذة العلاجات بصورة دائمة:  " + edreport3.getText().toString()
                + " ويعاني من الامراض :  " +edreport4.getText().toString() + " واخر تشخيص قام به هو : " + edreport5.getText().toString() ;

        //img
        try {
            Bitmap Bimg = ((BitmapDrawable) img.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bimg.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
            encoding = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT); // picture

            if (name.isEmpty()) {
                showErorr("حقل الاسم مطلوب", edname);
            } else if (name.length() < 10) {
                showErorr("الاسم قصير للغاية", edname);
            } else if (phone.isEmpty()) {
                dismissError(edname);
                showErorr("حقل الهاتف مطلوب", edphone);
            } else if (!ccp.isValidFullNumber()) {
                showErorr("رقم هاتف غير صالح", edphone);
            }else if(whatsapp.isEmpty()){
                dismissError(edphone);
                showErorr("رقم  الواتساب مطلوب",edwhatsapp);
            }else if(!ccpw.isValidFullNumber()){
                showErorr("رقم واتس غير صالح", edphone);
            }else if(address.isEmpty()){
                dismissError(edwhatsapp);
                showErorr("حقل العنوان مطلوب", edaddress);
            }else if(user.isEmpty()){
                dismissError(edaddress);
                showErorr("حقل اسم المستخدم مطلوب", eduser);
            }else if(pass.isEmpty()){
                dismissError(eduser);
                showErorr("حقل اسم المستخدم مطلوب", edpass);
            }else if(!pass.equals(passc)){
                dismissError(edpass);
                showErorr("كلمة المرور غير متطابقة", edpassc);
            }else {
                dismissError(edpassc);
                dismissError(img);
                Log.e("TTTTTTTTT","hgfghfhfhgfgh");
                // complete adding sucssfolly
                // add the requsert
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("جاري ارسال البيانات ");
                dialog.setCancelable(false);
                dialog.show();
                final StringRequest request_new_requset = new StringRequest(Request.Method.POST,
                        config.add_requset,
                        response ->
                        {
                            Log.e("TTTTTTTTT",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.get("data").equals(0)) {
                                    dialog.dismiss();
                                    Toast.makeText(this, "خطأ في عملية الاضافة الدخول", Toast.LENGTH_LONG).show();
                                } else if(jsonObject.get("data").equals(2)){
                                    dialog.dismiss();
                                    showErorr("اسم المستخدم موجود مسبقا" , eduser);
                                } else{
                                    dialog.dismiss();
                                    Toast.makeText(this, "تم طلب الاشتراك بنجاح ", Toast.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("sh_user" , user );
                                    editor.putString("sh_pass" , pass );
                                    editor.apply();
                                    startActivity(new Intent( this , Success.class ));
                                    finish();
                                }
                            } catch (JSONException e) {
                                dialog.dismiss();
                                e.printStackTrace();
                            }
                        },
                        error -> {

                        }) {
                    @NonNull
                    @Override
                    protected Map<String, String> getParams() {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", name);
                        map.put("phone", ccp.getFullNumberWithPlus());
                        map.put("whatsapp", ccpw.getFullNumberWithPlus());
                        map.put("gander", gander);
                        map.put("add", address);
                        map.put("user", user);
                        map.put("pass", pass);
                        map.put("sub", sub);
                        map.put("report", report);
                        map.put("image", encoding);
                        return map;

                    }
                };
               RequestHandler.getInstance(this).addToRequestQueue(request_new_requset);
           }
        }catch (Exception ex){
            showErorr("الرجاء اختيار الصورة " , img);
        }
    }

    private void showErorr(String msg, View view) {
        erorr_alert.setVisibility(View.VISIBLE);
        erorr_alert.setText(msg);
        view.setBackground(ContextCompat.getDrawable(this, R.drawable.redroundboundbackground));
        view.requestFocus();
    }

    private void dismissError(View view) {
        erorr_alert.setVisibility(View.GONE);
        erorr_alert.setText("");
        if (view != null)
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.whiterobackground));
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}