package com.vkc.loyaltyapp.activity.profile;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.common.SignUpActivity;
import com.vkc.loyaltyapp.activity.dealers.DealersActivity;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.manager.HeaderManager;
import com.vkc.loyaltyapp.utils.AndroidMultiPartEntity;
import com.vkc.loyaltyapp.utils.CustomToast;
import com.vkc.loyaltyapp.utils.UtilityMethods;
import com.vkc.loyaltyapp.volleymanager.VolleyWrapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

/**
 * Created by user2 on 9/8/17.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, VKCUrlConstants {


    AppCompatActivity mContext;

    HeaderManager headermanager;
    LinearLayout relativeHeader;
    ImageView mImageBack, imageProfile;
    Button buttonUpdate;
    EditText editMobile, editOwner, editShop, editState, editDist, editPlace, editPin, editAddress, editMobile2, editEmail;
    TextView textCustId, textMydealers, textUpdate;
    String filePath = "";
    int ACTIVITY_REQUEST_CODE = 700;
    int ACTIVITY_FINISH_RESULT_CODE = 701;
    private Uri mImageCaptureUri;
    String otpValue = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mContext = this;
        initUI();
        getProfile();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 100: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCameraGalleryChoice();

                } else {


                    Toast.makeText(mContext, "Unable to continue without granting permission for camera and writing data to external storage", Toast.LENGTH_LONG).show();

                    Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                    myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                    myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myAppSettings);
                }
                return;
            }

            case 101: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    showCameraGalleryChoice();

                    //

                } else {

                    Toast.makeText(mContext, "Unable to continue without granting permission for writing data to external storage and camera access", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    private void initUI() {
        relativeHeader = (LinearLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(ProfileActivity.this, getResources().getString(R.string.profile));
        headermanager.getHeader(relativeHeader, 1);
        mImageBack = headermanager.getLeftButton();

        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        textCustId = (TextView) findViewById(R.id.textCustId);
        textUpdate = (TextView) findViewById(R.id.textUpdate);
        textMydealers = (TextView) findViewById(R.id.textMydealers);
        mImageBack.setOnClickListener(this);
        imageProfile = (ImageView) findViewById(R.id.imageProfile);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        editMobile = (EditText) findViewById(R.id.editMobile);
        editOwner = (EditText) findViewById(R.id.editOwner);
        editShop = (EditText) findViewById(R.id.editShop);
        editState = (EditText) findViewById(R.id.editState);
        editDist = (EditText) findViewById(R.id.editDistrict);
        editPlace = (EditText) findViewById(R.id.editPlace);
        editPin = (EditText) findViewById(R.id.editPin);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editMobile2 = (EditText) findViewById(R.id.editMobile2);
        editEmail = (EditText) findViewById(R.id.editEmail);
        // editMobile.setEnabled(false);
        editOwner.setEnabled(true);
        editShop.setEnabled(false);
        editState.setEnabled(false);
        editDist.setEnabled(false);
        editPlace.setEnabled(true);
        editPin.setEnabled(false);
        editAddress.setEnabled(false);
        buttonUpdate.setOnClickListener(this);
        textMydealers.setOnClickListener(this);
        imageProfile.setOnClickListener(this);
        textUpdate.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        } else if (v == buttonUpdate) {

        /*    if (filePath.equals("")) {
                CustomToast toast = new CustomToast(
                        mContext);
                toast.show(21);
            } else {
                UploadFileToServer upload = new UploadFileToServer();
                upload.execute();
            }*/

            if (filePath.equals("")) {
                CustomToast toast = new CustomToast(
                        mContext);
                toast.show(21);
            } else {
                UpdateProfile upload = new UpdateProfile();
                upload.execute();
            }


        } else if (v == imageProfile) {

            if ((int) Build.VERSION.SDK_INT >= 23) {
                PermissionListener permissionListenerGallery = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (UtilityMethods.isNetworkConnected(mContext)) {
                            // new PDFViewActivity.loadPDF().execute();
                            showCameraGalleryChoice();

                        } else {
                            Toast.makeText(mContext, "Network error", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(mContext, "Permission Denied\n", Toast.LENGTH_SHORT).show();

                    }


                };
                TedPermission.with(mContext)
                        .setPermissionListener(permissionListenerGallery)
                        .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
                        .check();

            } else {
                showCameraGalleryChoice();
            }

        } else if (v == textMydealers) {
            startActivity(new Intent(ProfileActivity.this, DealersActivity.class));
        } else if (v == textUpdate) {
            if (editMobile.getText().toString().trim().length() > 0) {
                if (editMobile.getText().toString().trim().length() == 10) {

                    //Update mobile dialog

                    if (AppPrefenceManager.getMobile(mContext).equals(editMobile.getText().toString().trim())) {

                    } else {
                        DialogUpdateMobile dialog = new DialogUpdateMobile(mContext);
                        dialog.show();
                    }

                } else {
                    CustomToast toast = new CustomToast(mContext);
                    toast.show(54);
                }

            } else {

                CustomToast toast = new CustomToast(mContext);
                toast.show(54);

            }
        }

    }

    public void getProfile() {
        try {

            String[] name = {"cust_id", "role"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext)};
            final VolleyWrapper manager = new VolleyWrapper(GET_PROFILE);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = objResponse.optString("status");
                                    if (status.equals("Success")) {
                                        JSONObject objData = objResponse.optJSONObject("data");
                                        String name = objData.optString("name");
                                        String contact_person = objData.optString("contact_person");
                                        String district = objData.optString("district");
                                        String city = objData.optString("city");
                                        String state_name = objData.optString("state_name");
                                        String pincode = objData.optString("pincode");
                                        String phone = objData.optString("phone");
                                        String url = objData.optString("image");
                                        String mobile2 = objData.optString("phone2");
                                        String email = objData.optString("email");
                                        editMobile2.setText(mobile2);
                                        editEmail.setText(email);
                                        editShop.setText(name);
                                        editOwner.setText(contact_person);
                                        editDist.setText(district);
                                        editMobile.setText(phone);
                                        editPlace.setText(city);
                                        editState.setText(state_name);
                                        editPin.setText(pincode);
                                        editAddress.setText(objData.optString("address"));
                                        textCustId.setText("CUST_ID: - " + objData.optString("customer_id"));

                                        Picasso.with(mContext).load(url).placeholder(R.drawable.profile_image).into(imageProfile);//.transform(new CircleTransform())

                                    }


                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                CustomToast toast = new CustomToast(mContext);
                                toast.show(0);
                            }
                        }

                        @Override
                        public void responseFailure(String failureResponse) {
                            //CustomStatusDialog(RESPONSE_FAILURE);
                        }

                    });
        } catch (Exception e) {
            // CustomStatusDialog(RESPONSE_FAILURE);
            e.printStackTrace();
            //Log.d("TAG", "Common error");
        }
    }

    private class UpdateProfile extends AsyncTask<Void, Integer, String> {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        private JSONObject obj;
        private String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(UPDATE_PROFILE);
                File file = new File(filePath);
                FileBody bin1 = new FileBody(file.getAbsoluteFile());
                AndroidMultiPartEntity entity;
                entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {

                            }
                        });
                entity.addPart("cust_id", new StringBody(AppPrefenceManager.getCustomerId(mContext)));
                entity.addPart("role", new StringBody(AppPrefenceManager.getUserType(mContext)));
                entity.addPart("phone", new StringBody(editMobile.getText().toString().trim()));
                entity.addPart("contact_person", new StringBody(editOwner.getText().toString().trim()));
                entity.addPart("city", new StringBody(editPlace.getText().toString().trim()));
                entity.addPart("phone2", new StringBody(editMobile2.getText().toString().trim()));
                entity.addPart("email", new StringBody(editEmail.getText().toString().trim()));
                if (filePath.equals("")) {

                } else {
                    entity.addPart("image", bin1);
                }

                httppost.setEntity(entity);


                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {

                    responseString = EntityUtils.toString(r_entity);


                } else {

                    responseString = EntityUtils.toString(r_entity);
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
                Log.e("UploadApp", "exception: " + responseString);
            } catch (IOException e) {
                responseString = e.toString();
                Log.e("UploadApp", "exception: " + responseString);
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            System.out.print("Result " + result);
            try {
                obj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject responseObj = obj.optJSONObject("response");
                responseString = responseObj.optString("status");

                if (responseString.equals("Success")) {
                    CustomToast toast = new CustomToast(mContext);
                    toast.show(26);
                    getProfile();
                } else {
                    CustomToast toast = new CustomToast(mContext);
                    toast.show(27);
                }

            } catch (Exception e) {
                String error = e.toString();
                System.out.println("Error " + e);

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST_CODE) {
            if (resultCode == ACTIVITY_FINISH_RESULT_CODE) {
                finish();
            }
        } else {
            Bitmap bitmap = null;
            Uri selectedImage = null;
            if (resultCode != Activity.RESULT_OK)
                return;
            switch (requestCode) {
                case 0:
                    selectedImage = data.getData();
                    String root = Environment.getExternalStorageDirectory().getAbsolutePath();

                    bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    File myDir = new File(root + "/" + getResources().getString(R.string.app_name));
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    File file = new File(myDir, "tmp_avatar_"
                            + String.valueOf(System.currentTimeMillis()) + ".JPEG");
                    filePath = file.getAbsolutePath();


                    //  imgPath = file.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= 27) {
                        selectedImage = FileProvider.getUriForFile(
                                ProfileActivity.this,
                                "com.vkc.loyaltyapp.provider", //(use your app signature + ".provider" )
                                file);
                    } else {
                        selectedImage = Uri.fromFile(file);

                    }
                    // filePath=file.getAbsolutePath();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), selectedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = mContext.getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
//                        File tempFile = new File(imageUri.getPath());
                        File tempFile = new File(picturePath);
                        String root1 = Environment.getExternalStorageDirectory().getAbsolutePath();

                        File myDir1 = new File(root1 + "/" + getResources().getString(R.string.app_name));
                        if (!myDir1.exists()) {
                            myDir1.mkdirs();
                        }
                        File file1 = new File(myDir1, "tmp_avatar_"
                                + String.valueOf(System.currentTimeMillis()) + ".JPEG");
                        filePath = file1.getAbsolutePath();

                        bitmap = BitmapFactory.decodeFile(picturePath);

                        if (Build.VERSION.SDK_INT >= 27) {
                            selectedImage = FileProvider.getUriForFile(
                                    ProfileActivity.this,
                                    "com.vkc.loyaltyapp.provider", //(use your app signature + ".provider" )
                                    tempFile);
                        } else {
                            selectedImage = Uri.parse(picturePath);

                        }

                        // filePath = selectedImage.getPath();

                        cursor.close();
                    }
                    break;
            }
            if (bitmap != null) {
                try {
                    File tempFile = new File(filePath);
                    long size = tempFile.length();
                    ByteArrayOutputStream byteArrayOutputStream;
                    Log.e("Size image:", "" + size);
                    int minSize = 600;
                    int widthOfImage = bitmap.getWidth();
                    int heightOfImage = bitmap.getHeight();
                    Log.e("Width", "" + widthOfImage);
                    Log.e("Height", "" + heightOfImage);
                    int newHeight = 600;
                    int newWidht = 600;
                    if (widthOfImage < minSize && heightOfImage < minSize) {
                        newWidht = widthOfImage;
                        newHeight = heightOfImage;
                    } else {
                        if (widthOfImage >= heightOfImage) {
                            //int newheght = heightOfImage/600;
                            float ratio = (float) minSize / widthOfImage;
                            Log.e("Multi width greater", "" + minSize + "/" + widthOfImage + "=" + ratio);
                            newHeight = (int) (heightOfImage * ratio);
                            newWidht = minSize;
                        } else if (heightOfImage > widthOfImage) {
                            float ratio = (float) minSize / heightOfImage;
                            newWidht = (int) (widthOfImage * ratio);
                            newHeight = minSize;
                        }

                    }
                    if (size > 1024 * 1024) {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        Bitmap b;
                        b = Bitmap.createScaledBitmap(bitmap, newWidht, newHeight, true);
                        b.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

                    } else {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        Bitmap b;
                        b = Bitmap.createScaledBitmap(bitmap, newWidht, newHeight, true);
                        b.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                    }


                    if (size > (4 * 1024 * 1024)) {
                        //CustomStatusDialog(RESPONSE_LARGE_IMAGE);
                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    } else {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        int bounding = dpToPx(mContext.getResources().getDisplayMetrics().density);
                        float xScale = (10 * (float) bounding) / width;
                        float yScale = (100 * (float) bounding) / height;
                        float scale = (xScale <= yScale) ? xScale : yScale;
                        Matrix matrix = new Matrix();
                        matrix.postScale(scale, scale);
                        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
                        imageProfile.setImageDrawable(result);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        File f = new File(filePath);
                        try {
                            f.createNewFile();
                            FileOutputStream fos = null;

                            fos = new FileOutputStream(f);
                            fos.write(byteArray);
                            fos.close();
                            filePath = f.getAbsolutePath();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    }
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }

                    // CustomStatusDialog(RESPONSE_OUT_OF_MEMORY);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                }
            }
        }
    }

    private int dpToPx(float dp) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void showCameraGalleryChoice() {
        // imageProfile = imgView;
        final androidx.appcompat.app.AlertDialog.Builder getImageFrom = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        getImageFrom.setTitle(getResources().getString(R.string.select_item));
        final CharSequence[] opsChars = {mContext.getResources().getString(R.string.take_picture),
                mContext.getResources().getString(R.string.open_gallery)};
        getImageFrom.setItems(opsChars, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                   /* if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
*/
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File myDir = new File(root + "/" + getResources().getString(R.string.app_name));
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    if (Build.VERSION.SDK_INT >= 23) {
                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    }

                    // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    //   mImageCaptureUri);
                    try {
                        cameraIntent.putExtra("return-data", true);
                        startActivityForResult(cameraIntent, 0);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (which == 1) {
                    /*if (alertDialog != null) {
                        alertDialog.dismiss();
                    }*/
                    if (Build.VERSION.SDK_INT < 19) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(
                                intent,
                                getResources().getString(
                                        R.string.select_item)), 1);
                    } else {

                        if (Build.VERSION.SDK_INT >= 23) {
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            );
                            intent.setType("image/*");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            startActivityForResult(Intent.createChooser(
                                    intent,
                                    getResources().getString(
                                            R.string.select_item)), 1);
                        } else {
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");

                            startActivityForResult(Intent.createChooser(
                                    intent,
                                    getResources().getString(
                                            R.string.select_item)), 1);

                        }


                    }
                }
            }
        });
        getImageFrom.show();
    }

    public void updateMobile() {
        try {

            String[] name = {"cust_id", "role", "phone"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext), editMobile.getText().toString().trim()};
            final VolleyWrapper manager = new VolleyWrapper(UPDATE_MOBILE);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = objResponse.optString("status");
                                    if (status.equals("Success")) {


                                        OTPDialog dialog = new OTPDialog(mContext);
                                        dialog.show();
                                    } else {
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(56);
                                    }


                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                CustomToast toast = new CustomToast(mContext);
                                toast.show(0);
                            }
                        }

                        @Override
                        public void responseFailure(String failureResponse) {
                            //CustomStatusDialog(RESPONSE_FAILURE);
                        }

                    });
        } catch (Exception e) {
            // CustomStatusDialog(RESPONSE_FAILURE);
            e.printStackTrace();
            //  Log.d("TAG", "Common error");
        }
    }

    public class DialogUpdateMobile extends Dialog implements
            android.view.View.OnClickListener {

        public AppCompatActivity mActivity;
        String type, message;

        public DialogUpdateMobile(AppCompatActivity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;


        }

        public DialogUpdateMobile(AppCompatActivity a, String type, String message) {
            super(a);
            this.type = type;
            this.message = message;
            // TODO Auto-generated constructor stub


        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_update_mobile);
            init();

        }

        private void init() {

            // Button buttonSet = (Button) findViewById(R.id.buttonOk);
            TextView textYes = (TextView) findViewById(R.id.textYes);
            TextView textNo = (TextView) findViewById(R.id.textNo);

            textYes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    // mActivity.finish();

                    updateMobile();

                }
            });
            textNo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();


                }
            });

        }

        @Override
        public void onClick(View v) {

            dismiss();
        }

    }

    public class OTPDialog extends Dialog implements
            android.view.View.OnClickListener {

        public AppCompatActivity mActivity;


        public OTPDialog(AppCompatActivity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;


        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            setContentView(R.layout.dialog_otp_mobile);
            init();

        }

        private void init() {

            final EditText editOtp1 = (EditText) findViewById(R.id.editOtp1);
            final EditText editOtp2 = (EditText) findViewById(R.id.editOtp2);
            final EditText editOtp3 = (EditText) findViewById(R.id.editOtp3);
            final EditText editOtp4 = (EditText) findViewById(R.id.editOtp4);

            TextView textOtp = (TextView) findViewById(R.id.textOtp);
            TextView textCancel = (TextView) findViewById(R.id.textCancel);
            String mob = AppPrefenceManager.getMobile(mContext).substring(6, 10);
            textOtp.setText("OTP has been sent to  XXXXXX" + mob);
            editOtp1.setCursorVisible(false);

            sendOTP();

            textCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dismiss();

                }
            });
            /*textResend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editOtp1.setText("");
                    editOtp2.setText("");
                    editOtp3.setText("");
                    editOtp4.setText("");
                }
            });*/
            editOtp1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editOtp1.setBackgroundResource(R.drawable.rounded_rect_full_white);
                    if (s.length() == 1) {
                        editOtp1.clearFocus();
                        editOtp2.requestFocus();
                    }
                    otpValue = editOtp1.getText().toString().trim() + editOtp2.getText().toString().trim() + editOtp3.getText().toString().trim() + editOtp4.getText().toString().trim();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        editOtp1.setBackgroundResource(R.drawable.rounded_rect_line);
                    }

                }
            });
            editOtp2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editOtp2.setBackgroundResource(R.drawable.rounded_rect_full_white);
                    if (s.length() == 1) {
                        editOtp2.clearFocus();
                        editOtp3.requestFocus();
                    }

                    otpValue = editOtp1.getText().toString().trim() + editOtp2.getText().toString().trim() + editOtp3.getText().toString().trim() + editOtp4.getText().toString().trim();
                    if (otpValue.length() == 1) {
                        editOtp2.clearFocus();
                        editOtp1.requestFocus();

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        editOtp2.setBackgroundResource(R.drawable.rounded_rect_line);
                    }

                }
            });
            editOtp3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editOtp3.setBackgroundResource(R.drawable.rounded_rect_full_white);
                    if (s.length() == 1) {
                        editOtp3.clearFocus();
                        editOtp4.requestFocus();
                    }

                    otpValue = editOtp1.getText().toString().trim() + editOtp2.getText().toString().trim() + editOtp3.getText().toString().trim() + editOtp4.getText().toString().trim();
                    if (otpValue.length() == 2) {
                        editOtp3.clearFocus();
                        editOtp2.requestFocus();
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        editOtp3.setBackgroundResource(R.drawable.rounded_rect_line);
                    }

                }
            });
            editOtp4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editOtp4.setBackgroundResource(R.drawable.rounded_rect_full_white);
                    otpValue = editOtp1.getText().toString().trim() + editOtp2.getText().toString().trim() + editOtp3.getText().toString().trim() + editOtp4.getText().toString().trim();

                    if (otpValue.length() == 3) {
                        editOtp3.requestFocus();
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        editOtp4.setBackgroundResource(R.drawable.rounded_rect_line);
                    } else {

                        verifyOTP(otpValue, editMobile.getText().toString().trim());
                    }


                }
            });


            Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
            buttonCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }

        @Override
        public void onClick(View v) {

            dismiss();
        }

    }

    public void verifyOTP(String otp, String mobile) {
        try {
            String[] name = {"otp", "role", "cust_id", "phone", "isnewMobile"};
            String[] values = {otp, AppPrefenceManager.getUserType(mContext), AppPrefenceManager.getCustomerId(mContext), mobile, "1"};

            final VolleyWrapper manager = new VolleyWrapper(OTP_VERIFY_URL);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = objResponse.optString("status");

                                    if (status.equalsIgnoreCase("Success")) {


                                        //    AppPrefenceManager.setIsVerifiedOTP(mContext, "yes");
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(55);
                                        Intent intent = new Intent(ProfileActivity.this, SignUpActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);


                                    } else {

                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                CustomToast toast = new CustomToast(mContext);
                                toast.show(0);
                            }
                        }

                        @Override
                        public void responseFailure(String failureResponse) {
                            //CustomStatusDialog(RESPONSE_FAILURE);
                        }

                    });
        } catch (Exception e) {
            // CustomStatusDialog(RESPONSE_FAILURE);
            e.printStackTrace();
            Log.d("TAG", "Common error");
        }


    }


    public void sendOTP() {

        try {
            String[] name = {"role", "cust_id", "sms_key"};
            String[] values = {AppPrefenceManager.getUserType(mContext), AppPrefenceManager.getCustomerId(mContext), ""};

            final VolleyWrapper manager = new VolleyWrapper(OTP_RESEND_URL);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = objResponse.optString("status");
                                    if (status.equalsIgnoreCase("Success")) {

                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(34);
                                    } else {
                                        // AppPrefenceManager.setIsVerifiedOTP(mContext, "no");
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(0);
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                CustomToast toast = new CustomToast(mContext);
                                toast.show(0);
                            }
                        }

                        @Override
                        public void responseFailure(String failureResponse) {
                            //CustomStatusDialog(RESPONSE_FAILURE);
                        }

                    });
        } catch (Exception e) {
            // CustomStatusDialog(RESPONSE_FAILURE);
            e.printStackTrace();
            Log.d("TAG", "Common error");
        }


    }
}