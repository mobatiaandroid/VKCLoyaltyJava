package com.vkc.loyaltyapp.activity.pdf;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StrictMode;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.utils.UtilityMethods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PDFViewActivity extends AppCompatActivity {
    LinearLayout llDownload;
    PDFView pdf;
    String url, name;
    Bundle extras;
    Context mContext;
    ProgressDialog mProgressDialog;
    ImageView mImageBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_pdfview);
        mContext = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //  LocaleHelper.setLocale(getApplicationContext(), PrefUtils.getLanguageString(mContext));

        extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("pdf");
            url=url.replaceAll(" ","%20");
            //  title = extras.getString("title");
            name = extras.getString("name");

        }


        llDownload = findViewById(R.id.llDownload);
        pdf = findViewById(R.id.pdfView);
        mImageBack = (ImageView) findViewById(R.id.btn_left);

        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilityMethods.isNetworkConnected(mContext)) {
                    mProgressDialog = new ProgressDialog(PDFViewActivity.this);
                    mProgressDialog.setMessage("Downloading..");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setCancelable(true);
                    new DownloadPDF().execute();
                    mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            new DownloadPDF().cancel(true); //cancel the task
                        }
                    });
                } else {
                    Toast.makeText(mContext, "Network error", Toast.LENGTH_SHORT).show();
                }
            }
        });


        PermissionListener permissionListenerGallery = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (UtilityMethods.isNetworkConnected(mContext)) {
                    new loadPDF().execute();
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
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

        //
    }

    public class loadPDF extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PDFViewActivity.this);
            dialog.setMessage("Please wait...");//Please wait...
            dialog.show();
        }

        protected Void doInBackground(String... urls) {
            URL u = null;
            try {
                String fileName = "document.pdf";
                u = new URL(url);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);


                String auth = "SGHCXFTPUser" + ":" + "cXFTPu$3r";
                String encodedAuth = new String(Base64.encodeToString(auth.getBytes(), Base64.DEFAULT));
                encodedAuth = encodedAuth.replace("\n", "");

                c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                c.addRequestProperty("Authorization", "Basic " + encodedAuth);
                //c.setRequestProperty("Accept", "application/json");
                // c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                c.connect();

                int response = c.getResponseCode();
                String PATH = Environment.getExternalStorageDirectory()
                        + "/download/";
                // Log.d("Abhan", "PATH: " + PATH);
                File file = new File(PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outputFile = new File(file, fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "document.pdf");
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "document.pdf");
            // System.out.println("file.exists() = " + file.exists());
            // pdf.fromUri(uri);

            pdf.fromFile(file).defaultPage(0).enableSwipe(true).load();

            //web.loadUrl(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "test.pdf");
            // Toast.makeText(MainActivity.this, "Completed", Toast.LENGTH_LONG).show();
        }

    }

    public class DownloadPDF extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private ProgressDialog dialog;
        String filename = name.replace(" ", "_");
        String fileName = filename + ".pdf";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PDFViewActivity.this);
            dialog.setMessage("Downloading please wait...");//Please wait...
            dialog.show();
        }

        protected Void doInBackground(String... urls) {
            URL u = null;
            try {

                u = new URL(url);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);


               /* String auth = "SGHCXFTPUser" + ":" + "cXFTPu$3r";
                String encodedAuth = new String(Base64.encodeToString(auth.getBytes(), Base64.DEFAULT));
                encodedAuth = encodedAuth.replace("\n", "");
*/
                c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //c.addRequestProperty("Authorization", "Basic " + encodedAuth);
                //c.setRequestProperty("Accept", "application/json");
                // c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                c.connect();

                int response = c.getResponseCode();
                String PATH = Environment.getExternalStorageDirectory()
                        + "/download/";
                // Log.d("Abhan", "PATH: " + PATH);
                File file = new File(PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outputFile = new File(file, fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + fileName);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + fileName);
            System.out.println("file.exists() = " + file.exists());
            if (file.exists()) {
                Toast.makeText(mContext, "File Downloaded to Downloads folder", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Something Went Wrong. Download failed", Toast.LENGTH_SHORT).show();
            }
            // pdf.fromUri(uri);

            // pdf.fromFile(file).defaultPage(1).enableSwipe(true).load();

            //web.loadUrl(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "test.pdf");
            // Toast.makeText(MainActivity.this, "Completed", Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
//        stopDisconnectTimer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    private String getFilepath(String filename) {

        return new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Download/" + filename).getPath();

    }

}
