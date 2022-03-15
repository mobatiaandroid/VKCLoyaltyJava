package com.vkc.loyaltyapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.common.SplashActivity;
import com.vkc.loyaltyapp.activity.gifts.GiftsActivity;
import com.vkc.loyaltyapp.activity.inbox.InboxActivity;
import com.vkc.loyaltyapp.activity.issuepoint.IssuePointActivity;
import com.vkc.loyaltyapp.activity.news.NewsListActivity;
import com.vkc.loyaltyapp.activity.pdf.PDFViewActivity;
import com.vkc.loyaltyapp.activity.pointhistory.PointHistoryActivity;
import com.vkc.loyaltyapp.activity.profile.ProfileActivity;
import com.vkc.loyaltyapp.activity.shopimage.ShopImageActivity;
import com.vkc.loyaltyapp.activity.subdealerredeem.SubdealerRedeemActivity;
import com.vkc.loyaltyapp.appcontroller.AppController;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.utils.ConnectivityReceiver;
import com.vkc.loyaltyapp.utils.CustomToast;
import com.vkc.loyaltyapp.utils.UtilityMethods;
import com.vkc.loyaltyapp.volleymanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import devlight.io.library.ArcProgressStackView;

/**
 * Created by user2 on 26/7/17.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener, VKCUrlConstants, ConnectivityReceiver.ConnectivityReceiverListener {
    public final static int MODEL_COUNT = 2;
    TextView textPoints, textVersion, textNoPoint, txtNews;
    RelativeLayout rlHide;
    LinearLayout llPoints, llGifts, llProfile, llShop, llDescription, llInbox;
    AppCompatActivity mContext;
    int myPoint;
    ArcProgress arcProgress;
    Button btnIssue;
    ArcProgressStackView mArcProgressStackView;
    String gift_status;
    private int[] mStartColors = new int[MODEL_COUNT];
    private int[] mEndColors = new int[MODEL_COUNT];
    String serverVersion;
    String imei_no;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;

        initUI();
        getMyPoints();


    }

    private void initUI() {
        llShop = (LinearLayout) findViewById(R.id.llShop);
        llGifts = (LinearLayout) findViewById(R.id.llGifts);
        llPoints = (LinearLayout) findViewById(R.id.llPoints);
        llProfile = (LinearLayout) findViewById(R.id.llProfile);
        llInbox = (LinearLayout) findViewById(R.id.llInbox);
        textPoints = (TextView) findViewById(R.id.textPoint);
        textNoPoint = (TextView) findViewById(R.id.textNoPoint);
        textVersion = (TextView) findViewById(R.id.textVersion);
        txtNews = (TextView) findViewById(R.id.txtNews);
        rlHide = (RelativeLayout) findViewById(R.id.rlHide);
        rlHide.setVisibility(View.GONE);
        textVersion.setText("Ver. " + getVersion());
        llDescription = (LinearLayout) findViewById(R.id.llDescription);
        arcProgress = (ArcProgress) findViewById(R.id.arc_progress);
        btnIssue = (Button) findViewById(R.id.buttonIssue);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mArcProgressStackView = (ArcProgressStackView) findViewById(R.id.arcProgressStackView);
    arcProgress.setSuffixText("");
        arcProgress.setStrokeWidth(15);
        arcProgress.setMax(10000000);
        arcProgress.setBottomTextSize(80);
        arcProgress.setUnfinishedStrokeColor(getResources().getColor(R.color.white));
        arcProgress.setTextColor(getResources().getColor(R.color.white));
        arcProgress.setBackgroundColor(getResources().getColor(R.color.transparent));

        llProfile.setOnClickListener(this);
        llPoints.setOnClickListener(this);
        llGifts.setOnClickListener(this);
        llShop.setOnClickListener(this);
        btnIssue.setOnClickListener(this);
        llInbox.setOnClickListener(this);
        txtNews.setOnClickListener(this);
        if (AppPrefenceManager.getUserType(mContext).equals("7")) {
            btnIssue.setVisibility(View.VISIBLE);
            mArcProgressStackView.setVisibility(View.GONE);
            arcProgress.setVisibility(View.VISIBLE);
            llDescription.setVisibility(View.GONE);
            // textPoints.setVisibility(View.GONE);
        } else {
            btnIssue.setVisibility(View.GONE);
            mArcProgressStackView.setVisibility(View.VISIBLE);
            arcProgress.setVisibility(View.GONE);
            // textPoints.setVisibility(View.VISIBLE);
            llDescription.setVisibility(View.VISIBLE);
        }

        final String[] startColors = getResources().getStringArray(R.array.devlight);
        //  final String[] endColors = getResources().getStringArray(R.array.default_preview);
        final String[] bgColors = getResources().getStringArray(R.array.bg);

        // Parse colors
        for (int i = 0; i < MODEL_COUNT; i++) {
            mStartColors[i] = Color.parseColor(startColors[i]);
            mEndColors[i] = Color.parseColor(bgColors[i]);
        }
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */
                if (report.areAllPermissionsGranted()) {


                }

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                token.continuePermissionRequest();


            }
        }).check();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 100: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startActivity(new Intent(HomeActivity.this, ShopImageActivity.class));
                } else {


                    ActivityCompat.requestPermissions(HomeActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            101);
                    //  Toast.makeText(mContext, "Unable to continue without granting permission for writing data to external storage", Toast.LENGTH_LONG).show();
                }
                return;
            }

            case 101: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startActivity(new Intent(HomeActivity.this, ShopImageActivity.class));

                    //

                } else {
                    Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                    myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                    myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myAppSettings);
                    Toast.makeText(mContext, "Unable to continue without granting permission for writing data to external storage and camera access", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llGifts:

                if (AppPrefenceManager.getUserType(mContext).equals("7")) {
                    /*CustomToast toast = new CustomToast(mContext);
                    toast.show(23);*/
                    startActivity(new Intent(HomeActivity.this, SubdealerRedeemActivity.class));
                    this.overridePendingTransition(0, 0);

                } else {
/*
                    CustomToast toast = new CustomToast(mContext);
                    toast.show(31);*/
                    startActivity(new Intent(HomeActivity.this, GiftsActivity.class));
                    this.overridePendingTransition(0, 0);
                }







               /* if (gift_status.equals("0")) {

                    CustomToast toast = new CustomToast(mContext);
                    toast.show(31);
                } else {*/

                // }
                break;
            case R.id.llPoints:
                startActivity(new Intent(HomeActivity.this, PointHistoryActivity.class));
                this.overridePendingTransition(0, 0);
                break;

            case R.id.llInbox:
                startActivity(new Intent(HomeActivity.this, NewsListActivity.class));
                this.overridePendingTransition(0, 0);
                break;

            case R.id.txtNews:
                startActivity(new Intent(HomeActivity.this, InboxActivity.class));
                this.overridePendingTransition(0, 0);
                break;
            case R.id.llShop:
                if (AppPrefenceManager.getUserType(mContext).equals("7")) {
                    CustomToast toast = new CustomToast(mContext);
                    toast.show(23);
                } else {
                    if ((int) Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext,
                                    Manifest.permission.CAMERA)) {

                                // Show an explanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.
                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        101);
                            } else {

                                // No explanation needed, we can request the permission.

                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        101);

                                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                // app-defined int constant. The callback method gets the
                                // result of the request.
                            }


                        } else {
                            startActivity(new Intent(HomeActivity.this, ShopImageActivity.class));
                            this.overridePendingTransition(0, 0);
                        }

                    } else {
                        startActivity(new Intent(HomeActivity.this, ShopImageActivity.class));
                        this.overridePendingTransition(0, 0);
                    }
                }

                break;
            case R.id.llProfile:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                this.overridePendingTransition(0, 0);
                break;
            case R.id.buttonIssue:
                startActivity(new Intent(HomeActivity.this, IssuePointActivity.class));
                this.overridePendingTransition(0, 0);
                break;
        }
    }

    public void getMyPoints() {
        try {
            String[] name = {"cust_id", "role"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(GET_LOYALTY_POINTS);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //  System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {
                                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);


                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);


                                    JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = objResponse.optString("status");
                                    String imei_number = objResponse.optString("imei_no");


                                    if (status.equals("Success")) {

                                        String points = objResponse.optString("loyality_point");
                                        gift_status = objResponse.optString("gift_status");
                                        myPoint = Integer.parseInt(points);
                                        if (points.equals("0")) {
                                            textNoPoint.setVisibility(View.VISIBLE);
                                            textPoints.setVisibility(View.GONE);
                                        } else {
                                            textPoints.setText(points + " Coupons");
                                            textNoPoint.setVisibility(View.GONE);
                                        }

                                        int mul_val = myPoint * 100;
                                        int percent_value = mul_val / 3000;
                                        //    Log.i("percent ", "" + percent_value);
                                        mArcProgressStackView.setTextColor(Color.parseColor("#000000"));
                                        mArcProgressStackView.setDrawWidthDimension(150);

                                        if (AppPrefenceManager.getUserType(mContext).equals("7")) {
                                            btnIssue.setVisibility(View.VISIBLE);
                                            mArcProgressStackView.setVisibility(View.GONE);
                                            arcProgress.setVisibility(View.VISIBLE);
                                            llDescription.setVisibility(View.GONE);
                                            arcProgress.setProgress(myPoint);

                                        } else {
                                            btnIssue.setVisibility(View.GONE);
                                            mArcProgressStackView.setVisibility(View.VISIBLE);
                                            arcProgress.setVisibility(View.GONE);
                                            llDescription.setVisibility(View.VISIBLE);
                                            final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
                                            //   models.add(new ArcProgressStackView.Model("Coupon Target : 1600", 110, mEndColors[0], mStartColors[0]));
                                            models.add(new ArcProgressStackView.Model("", 110, mEndColors[0], mStartColors[0]));
                                            models.add(new ArcProgressStackView.Model("", percent_value, mEndColors[1], mStartColors[1]));
                                            mArcProgressStackView.setModels(models);
                                        }
                                        //  System.out.println("imei_number condition" + imei_number);
                                        // System.out.println("imei_number condition imei_no" + imei_no);


                                        if (Build.VERSION.SDK_INT >= 29) {

                                            FirebaseInstanceId.getInstance().getInstanceId()
                                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                            if (!task.isSuccessful()) {
                                                                return;
                                                            }

                                                            // Get new Instance ID token

                                                            imei_no = task.getResult().getId();
                                                        }
                                                    });

                                            if (!imei_no.equals(imei_number)) {
                                                // 352711095056053

                                                if (imei_no.equals("web")) {

                                                } else {
                                                    alertSignout(mContext);
                                                }
                                            }

                                        } else if (Build.VERSION.SDK_INT >= 26) {
                                            int phoneCount = tm.getPhoneCount();
                                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                                // TODO: Consider calling
                                                //    ActivityCompat#requestPermissions
                                                // here to request the missing permissions, and then overriding
                                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                //                                          int[] grantResults)
                                                // to handle the case where the user grants the permission. See the documentation
                                                // for ActivityCompat#requestPermissions for more details.
                                                return;
                                            }

                                            //only api 21 above
                                            if (phoneCount > 1) {

                                                imei_no = tm.getImei(0);
                                            } else {
                                                imei_no = tm.getImei();
                                            }
                                        } else {
                                            //only api 21 down


                                            imei_no = tm.getDeviceId();
                                        }
                                    }
                                    if (!imei_no.equals(imei_number)) {
                                        // 352711095056053

                                        if (imei_no.equals("web")) {

                                        } else {
                                            alertSignout(mContext);
                                        }
                                    } /*else {

                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(0);
                                    }*/
                                    getAppVesion();


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
            // Log.d("TAG", "Common error");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMyPoints();
    }

    private String getVersion() {
        PackageInfo packageinfo = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return packageinfo.versionName.toString();
    }

    public static void updateApp(final AppCompatActivity act) {
        final String appPackageName = act.getPackageName();
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("New Update Available !")// act.getString(R.string.dialog_title_update_app)
                .setMessage("Please update the app to avail new features")//
                /*.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    act.startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("market://details?id="
                                                    + appPackageName)));
                                } catch (ActivityNotFoundException anfe) {
                                    act.startActivity(new Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://play.google.com/store/apps/details?id="
                                                    + appPackageName)));
                                }
                            }
                        }
                )*/

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            act.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id="
                                            + appPackageName)));
                        } catch (ActivityNotFoundException anfe) {
                            act.startActivity(new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id="
                                            + appPackageName)));
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void getAppVesion() {
        try {
            String[] name = {};
            String[] values = {};

            final VolleyWrapper manager = new VolleyWrapper(GET_APP_VERSION_URL);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);


                                    JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = objResponse.optString("status");

                                    if (status.equals("Success")) {
                                        serverVersion = objResponse.optString("appversion");
                                        if (serverVersion.equals(getVersion())) {
                                            deviceRegister();
                                            rlHide.setVisibility(View.GONE);
                                        } else {
                                            rlHide.setVisibility(View.VISIBLE);
                                            updateApp(mContext);
                                        }
                                    } else {

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
            //Log.d("TAG", "Common error");
        }
    }

    public void deviceRegister() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();


                        AppPrefenceManager.saveToken(mContext, token);

                    }
                });
        try {
            String[] name = {"cust_id", "role", "device_id"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext), AppPrefenceManager.getToken(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(DEVICE_REGISTRATION_API);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);


                                    JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = objResponse.optString("status");

                                    if (status.equals("Success")) {

                                    } else if (status.equals("Empty")) {

                                        deviceRegister();
                                    } else {
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
            //Log.d("TAG", "Common error");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(HomeActivity.this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if (isConnected) {

        } else {
            CustomToast toast = new CustomToast(mContext);
            toast.show(58);
        }
    }

    public void alertSignout(final Activity act) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(act);
        builder.setTitle("Alert !")// act.getString(R.string.dialog_title_update_app)
                .setMessage("You are not allowed to use the VKC Loyalty on multiple device . The system will logout from this device now.")//
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    dialog.cancel();
                                    AppPrefenceManager.saveLoginStatusFlag(mContext, "no");
                                    AppPrefenceManager.setIsVerifiedOTP(mContext, "no");
                                    //AppPrefenceManager.saveUserType(mContext, "");
                                    AppPrefenceManager.saveDealerCount(mContext, 0);
                                    Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                } catch (ActivityNotFoundException anfe) {

                                }
                            }
                        }
                );


        android.app.AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
