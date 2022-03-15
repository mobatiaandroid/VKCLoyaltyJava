package com.vkc.loyaltyapp.activity.common;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.HomeActivity;
import com.vkc.loyaltyapp.activity.common.model.DistrictModel;
import com.vkc.loyaltyapp.activity.common.model.StateModel;
import com.vkc.loyaltyapp.activity.dealers.DealersActivity;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.utils.AppSignatureHashHelper;
import com.vkc.loyaltyapp.utils.CustomToast;
import com.vkc.loyaltyapp.utils.OtpReceivedInterface;
import com.vkc.loyaltyapp.utils.SMSReceiver;
import com.vkc.loyaltyapp.utils.UtilityMethods;
import com.vkc.loyaltyapp.volleymanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


/**
 * Created by user2 on 28/7/17.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, VKCUrlConstants, OtpReceivedInterface, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Button buttonRegister;
    EditText editMobile, editOwner, editShop, editPlace, editPin, editCustomer, editDoor, editAddress, editLandMark;
    AppCompatActivity mContext;
    EditText editOtp1, editOtp2, editOtp3, editOtp4;
    String otpValue = "";
    boolean isNewReg;
    LinearLayout llCustID, llAddress, llUserType;
    ImageView imageSearch, imageGetData;
    String imei_no = "";
    String mobileNo, selectedState, selectedDistrict, stateId, district;
    Spinner spinnerState, spinnerDist, spinnerUserType;
    ArrayList<String> listState, listDistrict, userTypeList;
    ArrayList<StateModel> stateList;
    ArrayList<DistrictModel> distList;
    private String userType;
    String isNewMobile = "0";
    private SMSReceiver smsReceiver;
    private GoogleApiClient mGoogleApiClient;
    String smsKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mContext = this;
        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);
        smsKey=appSignatureHashHelper.getAppSignatures().get(0);
       // Log.i("Hash Key", "HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));
        smsautodetection();
        startSMSListener();
        initUI();
        if ((int) Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 123);
            } else {
                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

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
                } else if (Build.VERSION.SDK_INT >= 26) {
                    //only api 21 above
                    int phoneCount = tm.getPhoneCount();
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
        } else {

            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            imei_no = tm.getDeviceId();

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

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
                } else if (Build.VERSION.SDK_INT >= 26) {
                    //only api 21 above
                    int phoneCount = tm.getPhoneCount();
                    if (phoneCount > 1) {
                        imei_no = tm.getImei(0);
                    } else {
                        imei_no = tm.getImei();
                    }
                } else {
                    //only api 21 down

                    imei_no = tm.getDeviceId();
                }

            } else {

            }
        }
    }

    private void initUI() {
        listState = new ArrayList<>();
        listDistrict = new ArrayList<>();
        stateList = new ArrayList<>();
        distList = new ArrayList<>();
        userTypeList = new ArrayList<>();
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editMobile = (EditText) findViewById(R.id.editMobile);
        editOwner = (EditText) findViewById(R.id.editOwner);
        editShop = (EditText) findViewById(R.id.editShop);
        editDoor = (EditText) findViewById(R.id.editDoor);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editLandMark = (EditText) findViewById(R.id.editLandMark);
        spinnerState = (Spinner) findViewById(R.id.spinnerState);
        spinnerUserType = (Spinner) findViewById(R.id.spinnerUserType);
        spinnerDist = (Spinner) findViewById(R.id.spinnerDistrict);
        editPlace = (EditText) findViewById(R.id.editPlace);
        editPin = (EditText) findViewById(R.id.editPin);
        editCustomer = (EditText) findViewById(R.id.editCustId);
        imageSearch = (ImageView) findViewById(R.id.imageSearch);
        imageGetData = (ImageView) findViewById(R.id.imageFetchData);
        llCustID = (LinearLayout) findViewById(R.id.llCustId);
        llAddress = (LinearLayout) findViewById(R.id.llAddress);
        llUserType = (LinearLayout) findViewById(R.id.llUserType);
        llCustID.setVisibility(View.GONE);
        llAddress.setVisibility(View.GONE);
        llUserType.setVisibility(View.GONE);
        editOwner.setEnabled(false);
        editShop.setEnabled(false);
        editPlace.setEnabled(false);
        editPin.setEnabled(false);
        editOwner.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editPlace.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editShop.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editDoor.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editAddress.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editLandMark.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        imageSearch.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        editCustomer.setText("");
        userType = "";
        selectedState = "";
        userTypeList.clear();
        editMobile.setText("");
        userTypeList.add("User Type");
        userTypeList.add("Retailer");
        userTypeList.add("Subdealer");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpActivity.this, android.R.layout.simple_spinner_item, userTypeList);
        spinnerUserType.setAdapter(adapter);
        getState();
        /*new Handler().postDelayed(new Runnable() {
            public void run() {
                // play.setText("Play");
                CustomToast toast = new CustomToast(mContext);
                toast.show(0);
            }
        }, 5000);*/
        editCustomer.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            getData();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position > 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

                    ((TextView) parent.getChildAt(0)).setTextSize(14);

                    ((TextView) parent.getChildAt(0)).setAllCaps(true);
                    stateId = stateList.get(position - 1).getStateId();
                    selectedState = stateList.get(position - 1).getStateName();
                    getDistrict();
                } else {
                    stateId = "";
                    selectedState = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedState = "";
                stateId = "";
            }
        });
        imageGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getData();
            }
        });
        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position > 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

                    ((TextView) parent.getChildAt(0)).setTextSize(14);

                    ((TextView) parent.getChildAt(0)).setAllCaps(true);

                    if (userTypeList.get(position).equals("Retailer")) {
                        userType = "5";
                    } else if (userTypeList.get(position).equals("Subdealer")) {
                        userType = "7";
                    }


                } else {
                    userType = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                userType = "";
            }
        });
        spinnerDist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {

                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) parent.getChildAt(0)).setAllCaps(true);
                    ((TextView) parent.getChildAt(0)).setTextSize(14);
                    selectedDistrict = distList.get(position - 1).getDistrictName();
                } else {
                    selectedDistrict = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedDistrict = "";
            }
        });
        editMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if (s.length() == 10) {
                    getData();
                } else {
                    editOwner.setText("");
                    editShop.setText("");
                    spinnerState.setSelection(0);
                    // listDistrict.clear();
                    // distList.clear();
                    spinnerDist.setSelection(0);
                    editPlace.setText("");
                    editPin.setText("");
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegister:
                if (editMobile.getText().toString().trim().equals("")) {
                    UtilityMethods.setErrorForEditText(editMobile, "Mandatory Field");
                } else if (editOwner.getText().toString().trim().equals("")) {
                    UtilityMethods.setErrorForEditText(editOwner, "Mandatory Field");
                } else if (editShop.getText().toString().trim().equals("")) {
                    UtilityMethods.setErrorForEditText(editShop, "Mandatory Field");
                } else if (userType.equals("") && isNewReg) {
                    CustomToast toast = new CustomToast(mContext);
                    toast.show(57);
                } else if (editDoor.getText().toString().trim().equals("") && isNewReg) {
                    UtilityMethods.setErrorForEditText(editDoor, "Mandatory Field");
                } else if (editAddress.getText().toString().trim().equals("") && isNewReg) {
                    UtilityMethods.setErrorForEditText(editAddress, "Mandatory Field");
                } else if (selectedState.equals("")) {
                    CustomToast toast = new CustomToast(mContext);
                    toast.show(32);
                } else if (selectedDistrict.equals("")) {
                    CustomToast toast = new CustomToast(mContext);
                    toast.show(33);
                } else if (editPlace.getText().toString().trim().equals("")) {
                    UtilityMethods.setErrorForEditText(editPlace, "Mandatory Field");
                } else if (editPin.getText().toString().trim().equals("")) {
                    UtilityMethods.setErrorForEditText(editPin, "Mandatory Field");
                } else {
                    if (isNewReg) {
                        newRegisterAPI();

                    } else {
                        registerAPI();
                    }
                }
                break;
            case R.id.imageSearch:


                getData();


                break;
        }

    }

    public void getData() {
        try {
            String[] name = {"mobile", "customer_id", "imei_no"};
            String[] values = {editMobile.getText().toString().trim(), editCustomer.getText().toString().trim(), imei_no};

            final VolleyWrapper manager = new VolleyWrapper(GET_USER_DATA);
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
                                    if (status.equalsIgnoreCase("Success")) {

                                        JSONObject objData = objResponse.optJSONObject("data");
                                        String owner = objData.optString("contact_person").toUpperCase();
                                        String shop_name = objData.optString("name").toUpperCase();
                                        district = objData.optString("district");
                                        String state = objData.optString("state_name");
                                        String pin = objData.getString("pincode");
                                        String role = objData.optString("role");
                                        String cust_id = objData.optString("cust_id");
                                        String city = objData.optString("city").toUpperCase();
                                        String stateCode = objData.optString("state");

                                        //   editCustomer.setText("");

                                        if (editCustomer.getText().toString().trim().length() > 0) {
                                            /*DialogConfirm dialogCon = new DialogConfirm(mContext, "2", "To register with this new mobile number please proceed with submit");
                                            dialogCon.show();*/
                                        }
                                        for (int i = 0; i < stateList.size(); i++) {
                                            if (stateList.get(i).getStateId().equals(stateCode)) {
                                                spinnerState.setSelection(i + 1);
                                            }
                                        }


                                        String isLoggedIn = objData.optString("is_logged_in");
                                        mobileNo = objData.optString("phone");
                                        AppPrefenceManager.setMobile(mContext, mobileNo);
                                        if (isLoggedIn.equals("0")) {


                                            int dealerCount = Integer.parseInt(objData.optString("dealers_count"));
                                            if (dealerCount > 0) {
                                                //  buttonRegister.setVisibility(View.GONE);
                                                AppPrefenceManager.saveDealerCount(mContext, dealerCount);
                                                CustomToast toast = new CustomToast(mContext);
                                                toast.show(35);

                                            } else {
                                                AppPrefenceManager.saveDealerCount(mContext, 0);
                                                //   buttonRegister.setVisibility(View.VISIBLE);
                                            }

                                            AppPrefenceManager.saveCustomerId(mContext, cust_id);
                                            AppPrefenceManager.saveUserType(mContext, role);
                                            editOwner.setText(owner);
                                            editShop.setText(shop_name);
                                            editPlace.setText(city);
                                            editPin.setText(pin);
                                            editOwner.setEnabled(true);
                                            editShop.setEnabled(false);
                                            spinnerDist.setEnabled(false);
                                            spinnerState.setEnabled(false);

                                            editPlace.setEnabled(true);
                                            editPin.setEnabled(false);
                                            llCustID.setVisibility(View.GONE);
                                            llAddress.setVisibility(View.GONE);
                                            llUserType.setVisibility(View.GONE);
                                            isNewReg = false;
                                        } else {

                                           /*
                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(29);*/

                                            AppPrefenceManager.saveCustomerId(mContext, cust_id);
                                            AppPrefenceManager.saveUserType(mContext, role);
                                            AppPrefenceManager.saveUserId(mContext, role);// For Dealer
                                            alertDeregister(mContext);
                                        }


                                    } else if (status.equals("Empty")) {
                                        isNewReg = true;
                                        editOwner.setEnabled(true);
                                        editShop.setEnabled(true);
                                        spinnerState.setEnabled(true);
                                        spinnerDist.setEnabled(true);
                                        editPlace.setEnabled(true);
                                        editPin.setEnabled(true);
                                        if (editCustomer.getText().toString().trim().length() > 0) {
                                            DialogConfirm dialogCon = new DialogConfirm(mContext, "0", "You are not a registered user proceed with new registration");
                                            dialogCon.show();
                                        } else {
                                            DialogConfirm dialogCon = new DialogConfirm(mContext, "1", "You are not registered with this mobile no, kindly search with CUST ID.If you don't know CUST ID please fill up the data for new registration");
                                            dialogCon.show();
                                        }


                                        llCustID.setVisibility(View.VISIBLE);
                                        llAddress.setVisibility(View.VISIBLE);
                                        llUserType.setVisibility(View.VISIBLE);

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

    public void getState() {
        stateList.clear();
        listState.clear();
        try {
            String[] name = {};
            String[] values = {};

            final VolleyWrapper manager = new VolleyWrapper(GET_STATE);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    JSONArray objStateArray = rootObject.optJSONArray("states");

                                    if (objStateArray.length() > 0) {

                                        for (int i = 0; i < objStateArray.length(); i++) {
                                            JSONObject obj = objStateArray.getJSONObject(i);
                                            StateModel model = new StateModel();
                                            model.setStateId(obj.getString("state"));
                                            model.setStateName(obj.getString("state_name"));
                                            stateList.add(model);
                                        }

                                        listState.add("Select State");
                                        for (int i = 0; i < stateList.size(); i++) {
                                            listState.add(stateList.get(i).getStateName());
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpActivity.this, android.R.layout.simple_spinner_item, listState);
                                        spinnerState.setAdapter(adapter);
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

    public void getDistrict() {
        listDistrict.clear();
        distList.clear();
        try {
            String[] name = {"state"};
            String[] values = {stateId};

            final VolleyWrapper manager = new VolleyWrapper(GET_DISTRICT);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    JSONArray objDistArray = rootObject.optJSONArray("response");

                                    if (objDistArray.length() > 0) {

                                        for (int i = 0; i < objDistArray.length(); i++) {
                                            JSONObject obj = objDistArray.getJSONObject(i);
                                            DistrictModel model = new DistrictModel();

                                            model.setDistrictName(obj.getString("district"));
                                            distList.add(model);
                                        }

                                        listDistrict.add("Select District");
                                        for (int i = 0; i < distList.size(); i++) {
                                            listDistrict.add(distList.get(i).getDistrictName());
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpActivity.this, android.R.layout.simple_spinner_item, listDistrict);
                                        spinnerDist.setAdapter(adapter);
                                        for (int j = 0; j < distList.size(); j++) {
                                            if (distList.get(j).getDistrictName().toUpperCase().equals(district.toUpperCase())) {
                                                spinnerDist.setSelection(j + 1);
                                            }
                                        }
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

    private void OTPAlert() {
        OTPDialog appDeleteDialog = new OTPDialog(mContext);

        appDeleteDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        // appDeleteDialog.setCancelable(true);
        appDeleteDialog.setCanceledOnTouchOutside(false);
        appDeleteDialog.show();

    }

    public void registerAPI() {
        try {
            String[] name = {"phone", "role", "cust_id", "contact_person", "city"};
            String[] values = {editMobile.getText().toString(), AppPrefenceManager.getUserType(mContext), AppPrefenceManager.getCustomerId(mContext), editOwner.getText().toString(), editPlace.getText().toString()};

            final VolleyWrapper manager = new VolleyWrapper(REGISTER_URL);
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
                                        OTPAlert();
                                        // OtpReader.bind(SignUpActivity.this, "VM-VKCGRP");
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
                        }

                    });
        } catch (Exception e) {
            // CustomStatusDialog(RESPONSE_FAILURE);
            e.printStackTrace();
            Log.d("TAG", "Common error");
        }
    }

    public void newRegisterAPI() {
        try {


            String[] name = {"customer_id", "shop_name", "state_name", "district", "city", "pincode", "contact_person", "phone", "door_no", "address_line1", "landmark", "user_type"};
            String[] values = {editCustomer.getText().toString(), editShop.getText().toString(), selectedState, selectedDistrict, editPlace.getText().toString(), editPin.getText().toString(), editOwner.getText().toString(), editMobile.getText().toString(), editDoor.getText().toString(), editAddress.getText().toString(), editLandMark.getText().toString(), userType};

            final VolleyWrapper manager = new VolleyWrapper(NEW_REGISTER);
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
                                    if (status.equalsIgnoreCase("Success")) {

                                       /* DialogConfirm dlg = new DialogConfirm(mContext);
                                        dlg.show();*/
                                        DialogConfirm dialogCon = new DialogConfirm(mContext, "4", "Registration request submitted successfully. Please login to loyalty app after the confirmation from Walkaroo Group.");
                                        dialogCon.show();
                                    } else if (status.equalsIgnoreCase("Exists")) {

                                       /* DialogConfirm dlg = new DialogConfirm(mContext);
                                        dlg.show();*/
                                        DialogConfirm dialogCon = new DialogConfirm(mContext, "5", "Registration request already submitted. Please contact Walkaroo Group.");
                                        dialogCon.show();
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
            Log.d("TAG", "Common error");
        }
    }

    public void verifyOTP(String otp, String mobile, String isNewMobile) {
        try {
            String[] name = {"otp", "role", "cust_id", "phone", "isnewMobile", "imei_no"};
            String[] values = {otp, AppPrefenceManager.getUserType(mContext), AppPrefenceManager.getCustomerId(mContext), mobile, isNewMobile, imei_no};

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


                                        AppPrefenceManager.setIsVerifiedOTP(mContext, "yes");
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(8);

                                        if (AppPrefenceManager.getLoginStatusFlag(mContext).equals("yes")) {

                                            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                            finish();
                                        } else {
                                            if (editCustomer.getText().toString().trim().equals("")) {
                                                Intent intent= new Intent(SignUpActivity.this, DealersActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                DialogUpdate dialogUpdate = new DialogUpdate(mContext);
                                                dialogUpdate.show();

                                            }


                                        }

                                    } else {
                                        int attempts = Integer.parseInt(objResponse.optString("otp_attempt"));
                                        if (attempts == 3) {
                                            DialogConfirm dialogCon = new DialogConfirm(mContext, "2", "Your OTP attemts exceeded. Kindly fill up the data for new registration");
                                            dialogCon.show();
                                            AppPrefenceManager.setIsVerifiedOTP(mContext, "no");
                                        } else {
                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(9);
                                        }

                                        /**/
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

    public void updatePhone(String otp, String mobile) {
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
                                        DialogConfirm dialogCon = new DialogConfirm(mContext, "3", "Mobile number updated successfully. Please login using new mobile number");
                                        dialogCon.show();
                                      /*  CustomToast toast = new CustomToast(mContext);
                                        toast.show(30)*/


                                        startActivity(new Intent(SignUpActivity.this, DealersActivity.class));
                                        finish();


                                    } else {
                                        AppPrefenceManager.setIsVerifiedOTP(mContext, "no");
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(9);
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


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onOtpReceived(String otp) {

        String otpValue = otp.substring(45, 49);
        editOtp1.setText(String.valueOf(otpValue.charAt(0)));
        editOtp1.setTextColor(Color.parseColor("#ffffff"));
        editOtp2.setText(String.valueOf(otpValue.charAt(1)));
        editOtp2.setTextColor(Color.parseColor("#ffffff"));
        editOtp3.setText(String.valueOf(otpValue.charAt(2)));
        editOtp3.setTextColor(Color.parseColor("#ffffff"));
        editOtp4.setText(String.valueOf(otpValue.charAt(3)));
        editOtp4.setTextColor(Color.parseColor("#ffffff"));


    }

    @Override
    public void onOtpTimeout() {

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

            setContentView(R.layout.dialog_otp_alert);
            init();

        }

        private void init() {

            editOtp1 = (EditText) findViewById(R.id.editOtp1);
            editOtp2 = (EditText) findViewById(R.id.editOtp2);
            editOtp3 = (EditText) findViewById(R.id.editOtp3);
            editOtp4 = (EditText) findViewById(R.id.editOtp4);
            TextView textResend = (TextView) findViewById(R.id.textResend);
            TextView textOtp = (TextView) findViewById(R.id.textOtp);
            TextView textCancel = (TextView) findViewById(R.id.textCancel);
            String mob = mobileNo.substring(6, 10);
            textOtp.setText("OTP has been sent to  XXXXXX" + mob);
            editOtp1.setCursorVisible(false);
            textCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editCustomer.setText("");
                    llCustID.setVisibility(View.GONE);
                    dismiss();
                    Intent intent = new Intent(mActivity, SignUpActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    mActivity.finish();
                }
            });
            textResend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resendOTP();
                    editOtp1.setText("");
                    editOtp2.setText("");
                    editOtp3.setText("");
                    editOtp4.setText("");
                }
            });
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

                        verifyOTP(otpValue, editMobile.getText().toString().trim(), isNewMobile);
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

    public void recivedSms(String message) {
        try {
            //.setText(message);
        } catch (Exception e) {
        }
    }


    public void resendOTP() {
        mGoogleApiClient.stopAutoManage(SignUpActivity.this);
        mGoogleApiClient.disconnect();

        try {
            String[] name = {"role", "cust_id","sms_key"};
            String[] values = {AppPrefenceManager.getUserType(mContext), AppPrefenceManager.getCustomerId(mContext),smsKey};

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
                                        smsautodetection();
                                        startSMSListener();
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


    public class DialogConfirm extends Dialog implements
            android.view.View.OnClickListener {

        public AppCompatActivity mActivity;
        String type, message;

        public DialogConfirm(AppCompatActivity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;


        }

        public DialogConfirm(AppCompatActivity a, String type, String message) {
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
            setContentView(R.layout.dialog_confirm);
            init();

        }

        private void init() {

            // Button buttonSet = (Button) findViewById(R.id.buttonOk);
            TextView textOtp = (TextView) findViewById(R.id.textOtp);
            Button buttonCancel = (Button) findViewById(R.id.buttonOk);
            textOtp.setText(message);
            if (type.equals("1")) {
                editMobile.clearFocus();
                editCustomer.requestFocus();
            }


            buttonCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    // mActivity.finish();

                    if (type.equals("1")) {
                        editCustomer.setText("");
                    } else if (type.equals("2")) {
                        editCustomer.setText("");
                        //   dismiss();
                        startActivity(new Intent(mContext, SignUpActivity.class));
                        mContext.finish();
                    } else if (type.equals("3")) {
                        //  dismiss();
                        startActivity(new Intent(mContext, DealersActivity
                                .class));
                        mContext.finish();
                    } else if (type.equals("4")) {
                        // dismiss();
                        mContext.finish();
                    } else if (type.equals("5")) {
                        //  dismiss();
                        mContext.finish();
                    } else {
                        llUserType.setVisibility(View.VISIBLE);
                        // dismiss();
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {

            dismiss();
        }

    }


    public class DialogUpdate extends Dialog implements
            android.view.View.OnClickListener {

        public AppCompatActivity mActivity;
        String type, message;

        public DialogUpdate(AppCompatActivity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;


        }

        public DialogUpdate(AppCompatActivity a, String type, String message) {
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
            setContentView(R.layout.dialog_yes_no);
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

                    updatePhone(otpValue, editMobile.getText().toString().trim());

                }
            });
            textNo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    startActivity(new Intent(SignUpActivity.this, DealersActivity.class));
                    finish();
                    // mActivity.finish();
                    //updatePhone(otpValue, editMobile.getText().toString().trim());

                }
            });

        }

        @Override
        public void onClick(View v) {

            dismiss();
        }


    }

    public void alertDeregister(final Activity act) {
        final String appPackageName = act.getPackageName();
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Alert !")// act.getString(R.string.dialog_title_update_app)
                .setMessage("This account is already registered with a device. You have to deregister the previous device to continue login. Please click ok to proceed.")//
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    dialog.cancel();
                                } catch (ActivityNotFoundException anfe) {

                                }
                            }
                        }
                )

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        try {
                            isNewMobile = "2"; // For updating IMEI
                            dialog.cancel();
                            resendOTP();
                            OTPDialog appDeleteDialog = new OTPDialog(mContext);

                            appDeleteDialog.getWindow().setBackgroundDrawable(
                                    new ColorDrawable(Color.TRANSPARENT));
                            appDeleteDialog.setCanceledOnTouchOutside(false);
                            appDeleteDialog.show();
                        } catch (ActivityNotFoundException anfe) {

                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void startSMSListener() {
        try {
            smsReceiver = new SMSReceiver();
            smsReceiver.setOnOtpListeners(SignUpActivity.this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            this.registerReceiver(smsReceiver, intentFilter);

            SmsRetrieverClient client = SmsRetriever.getClient(this);

            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // API successfully started
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Fail to start API
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void smsautodetection() {
        smsReceiver = new SMSReceiver();
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
        smsReceiver.setOnOtpListeners(SignUpActivity.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(smsReceiver, intentFilter);
    }
}
