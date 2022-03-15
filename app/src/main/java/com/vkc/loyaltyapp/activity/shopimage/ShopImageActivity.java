
package com.vkc.loyaltyapp.activity.shopimage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mindorks.paracamera.Camera;
import com.squareup.picasso.Picasso;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.shopimage.adapter.ShopImageAdapter;
import com.vkc.loyaltyapp.activity.shopimage.model.ImageListModel;
import com.vkc.loyaltyapp.appcontroller.AppController;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.manager.HeaderManager;
import com.vkc.loyaltyapp.utils.AndroidMultiPartEntity;
import com.vkc.loyaltyapp.utils.CameraUtils;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by user2 on 11/8/17.
 */

public class ShopImageActivity extends AppCompatActivity implements VKCUrlConstants, View.OnClickListener {

    ImageView imageShop;
    AppCompatActivity mContext;
    HeaderManager headermanager;
    LinearLayout relativeHeader;
    ImageView mImageBack;
    private Uri mImageCaptureUri;
    int ACTIVITY_REQUEST_CODE = 700;
    int ACTIVITY_FINISH_RESULT_CODE = 701;
    String filePath = "";
    Button btnCapture, btnUpload;
    private File destination = null;

    ImageView  image1Delete, image2Delete;
    RelativeLayout relative1, relative2;
    public static String limit_value;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_image);
        mContext = this;
        initUI();
    }

    private void initUI() {
        relativeHeader = (LinearLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(ShopImageActivity.this, getResources().getString(R.string.shop_image));
        headermanager.getHeader(relativeHeader, 1);
        mImageBack = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        imageShop = (ImageView) findViewById(R.id.imageShop);
       /* image1 = (ImageView) findViewById(R.id.imageOne);
        image2 = (ImageView) findViewById(R.id.imageTwo);
        image1Delete = (ImageView) findViewById(R.id.deleteImage1);
        image2Delete = (ImageView) findViewById(R.id.deleteImage2);
        image1Delete.setVisibility(View.GONE);
        image2Delete.setVisibility(View.GONE);
        relative1 = (RelativeLayout) findViewById(R.id.relative1);*/
        relative2 = (RelativeLayout) findViewById(R.id.relative2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        //  LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        btnCapture = (Button) findViewById(R.id.buttonCapture);
        btnUpload = (Button) findViewById(R.id.buttonUpload);
        getImage();
        mImageBack.setOnClickListener(this);
        btnCapture.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        // image1.setOnClickListener(this);
        // image2.setOnClickListener(this);
        // image1Delete.setOnClickListener(this);
        // image2Delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        } else if (v == btnCapture) {

            if ((int) Build.VERSION.SDK_INT >= 23) {

                if (CameraUtils.checkPermissions(getApplicationContext())) {
                    captureImage();
                } else {
                    requestCameraPermission(1);
                }

            }
            else
            {
                captureImage();
            }

        } else if (v == btnUpload) {
            if (filePath.equals("")) {
                CustomToast toast = new CustomToast(
                        mContext);
                toast.show(21);
            } else {

               /* if(AppController.imageList.size()==Integer.parseInt(limit_value))
                {
                    CustomToast toast = new CustomToast(
                            mContext);
                    toast.show(20);
                }
                else {*/
                    try {
                           UploadFileToServer upload = new UploadFileToServer();
                        upload.execute();
                    } catch (Exception e) {

                    }
                //}
            }
        } /*else if (v == image1Delete) {

            DialogConfirm dialog = new DialogConfirm(mContext, imageList.get(0).getId());
            dialog.show();
            //  deleteImage(imageList.get(0).getId());
        } else if (v == image2Delete) {

            DialogConfirm dialog = new DialogConfirm(mContext, imageList.get(1).getId());
            dialog.show();

        }*/
    }


    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        private JSONObject obj;
        private String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Uploading...");
            pDialog.setCanceledOnTouchOutside(false);
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


                HttpPost httppost = new HttpPost(UPLOAD_IMAGE);

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
                entity.addPart("image", bin1);
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

            JSONObject responseObj = obj.optJSONObject("response");
            responseString = responseObj.optString("status");

            if (responseString.equalsIgnoreCase("Success")) {

                CustomToast toast = new CustomToast(
                        mContext);
                toast.show(19);
                getImageHistory();
            } else if (responseString.equalsIgnoreCase("Exceeded")) {

                CustomToast toast = new CustomToast(
                        mContext);
                toast.show(20);
                getImage();
            } else {
                CustomToast toast = new CustomToast(
                        mContext);
                toast.show(0);
            }
        }

    }

 /*   public void showCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/" + getResources().getString(R.string.app_name));
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        File file = new File(myDir, "tmp_avatar_"
                + String.valueOf(System.currentTimeMillis()) + ".JPEG");
        if (Build.VERSION.SDK_INT >= 23) {

            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        }
            if (Build.VERSION.SDK_INT >= 27) {
            mImageCaptureUri = FileProvider.getUriForFile(
                    ShopImageActivity.this,
                    "com.vkc.loyaltyapp.provider", //(use your app signature + ".provider" )
                    file);
        } else {
            mImageCaptureUri = Uri.fromFile(file);

        }

        // cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        filePath = file.getAbsolutePath();


        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        try {
            cameraIntent.putExtra("return-data", true);
            startActivityForResult(cameraIntent, 0);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }*/
 public void showCamera() {
     Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
     String root = Environment.getExternalStorageDirectory().getAbsolutePath();
     File myDir = new File(root + "/" + getResources().getString(R.string.app_name));
     myDir.mkdirs();
     File file = new File(myDir, "tmp_avatar_"
             + String.valueOf(System.currentTimeMillis()) + ".JPEG");
     mImageCaptureUri = Uri.fromFile(file);
     cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
             mImageCaptureUri);
     try {
         cameraIntent.putExtra("return-data", true);
         startActivityForResult(cameraIntent, 0);
     } catch (ActivityNotFoundException e) {
         e.printStackTrace();
     }
 }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            try {
                Uri selectedImage = data.getData();
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                //Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                destination.mkdirs();
                FileOutputStream fo;
                try {
                    if (destination.exists ()) destination.delete ();

                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.flush();
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                filePath = destination.getAbsolutePath();
                imageShop.setImageBitmap(bitmap);

                //    CallForAPI(true);



            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private int dpToPx(float dp) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void getImage() {
        try {


            String[] name = {"cust_id", "role"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(UPLOADED_IMAGE);
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
                                        String imageUrl = objData.optString("image");
                                        Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.shop_image).into(imageShop);

                                        getImageHistory();
                                    }

                                    //    } else {
                                    //  CustomToast toast = new CustomToast(mContext);
                                    //    toast.show(4);
                                    //   }

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

    public void getImageHistory() {
        AppController.imageList.clear();
        try {


            String[] name = {"cust_id", "role"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(GET_IMAGE_HISTORY);
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
                                    limit_value=objResponse.optString("image_limit");
                                    if (status.equals("Success")) {
                                        JSONArray objData = objResponse.optJSONArray("data");
                                        if (objData.length() > 0) {
                                            for (int i = 0; i < objData.length(); i++) {
                                                JSONObject obj = objData.optJSONObject(i);
                                                ImageListModel model = new ImageListModel();
                                                model.setImage(obj.getString("image"));
                                                model.setId(obj.getString("id"));
                                                AppController.imageList.add(model);
                                            }
                                      /*      if (imageList.size() > 1) {
                                                if (!imageList.get(0).getImage().equals("")) {

                                                    relative1.setVisibility(View.VISIBLE);
                                                    image1Delete.setVisibility(View.VISIBLE);
                                                    Picasso.with(mContext).load(imageList.get(0).getImage()).resize(200, 200).centerInside().into(image1);
                                                } else {
                                                    relative1.setVisibility(View.GONE);
                                                    image1Delete.setVisibility(View.GONE);

                                                }

                                                if (!imageList.get(1).getImage().equals("")) {
                                                    relative2.setVisibility(View.VISIBLE);
                                                    image2Delete.setVisibility(View.VISIBLE);
                                                    Picasso.with(mContext).load(imageList.get(1).getImage()).resize(200, 200).centerInside().into(image2);
                                                } else {
                                                    relative2.setVisibility(View.GONE);
                                                    image2Delete.setVisibility(View.GONE);
                                                }
                                            } else {

                                                relative2.setVisibility(View.GONE);
                                                image2Delete.setVisibility(View.GONE);
                                                if (!imageList.get(0).getImage().equals("")) {

                                                    relative1.setVisibility(View.VISIBLE);
                                                    image1Delete.setVisibility(View.VISIBLE);
                                                    Picasso.with(mContext).load(imageList.get(0).getImage()).resize(200, 200).centerInside().into(image1);
                                                } else {
                                                    relative1.setVisibility(View.GONE);
                                                    image1Delete.setVisibility(View.GONE);

                                                }
                                            }*/


                                            ShopImageAdapter adapter = new ShopImageAdapter(mContext, AppController.imageList, recyclerView);
                                            recyclerView.setAdapter(adapter);

                                        } else {

                                            relative1.setVisibility(View.GONE);
                                            image1Delete.setVisibility(View.GONE);
                                            relative2.setVisibility(View.GONE);
                                            image2Delete.setVisibility(View.GONE);
                                            // initUI();
                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(51);

                                        }
                                       /* String imageUrl = objData.optString("image");
                                        Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.shop_image).into(imageShop);*/
                                    }

                                    //    } else {
                                    //  CustomToast toast = new CustomToast(mContext);
                                    //    toast.show(4);
                                    //   }

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


    public void deleteImage(String id) {

        try {


            String[] name = {"id"};
            String[] values = {id};

            final VolleyWrapper manager = new VolleyWrapper(DELETE_IMAGE);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    //JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = rootObject.optString("status");
                                    if (status.equals("Success")) {
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(52);
                                        getImageHistory();
                                    } else if (status.equals("Error")) {
                                        getImageHistory();
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

    public class DialogConfirm extends Dialog implements
            android.view.View.OnClickListener {

        public AppCompatActivity mActivity;
        String type, message, id;

        public DialogConfirm(AppCompatActivity a, String id) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;
            this.id = id;

        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_delete_image);
            init();

        }

        private void init() {

            // Button buttonSet = (Button) findViewById(R.id.buttonOk);
            TextView textYes = (TextView) findViewById(R.id.textYes);
            TextView textNo = (TextView) findViewById(R.id.textNo);


            textYes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    deleteImage(id);
                    dismiss();
                }
            });


            textNo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    // mActivity.finish();


                }
            });

        }

        @Override
        public void onClick(View v) {

            dismiss();
        }

    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == 100) {
                                // capture picture
                                captureImage();
                            } else {
                               // captureVideo();
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(ShopImageActivity.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void previewCapturedImage() {
        try {
            // hide video preview




        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

