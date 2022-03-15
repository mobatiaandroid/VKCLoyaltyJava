package com.vkc.loyaltyapp.activity.issuepoint;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.issuepoint.model.UserModel;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.manager.HeaderManager;
import com.vkc.loyaltyapp.utils.CustomToast;
import com.vkc.loyaltyapp.volleymanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user2 on 10/8/17.
 */
public class    IssuePointActivity extends AppCompatActivity implements View.OnClickListener, VKCUrlConstants {

    Button btnLogin;
    TextView textSignup;
    AppCompatActivity mContext;
    EditText mEditPoint;
    String imei_no;
    ArcProgress arcProgress;
    ImageView btnIssue;
    int myPoint;
    HeaderManager headermanager;
    LinearLayout relativeHeader;
    ImageView mImageBack;
    String selectedId;
    ArrayList<UserModel> listUsers;
    AutoCompleteTextView edtSearch;
    TextView textId, textName, textAddress, textPhone;
    LinearLayout llData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_points);
        mContext = this;


        initUI();
        getMyPoints();
    }

    private void initUI() {
        listUsers = new ArrayList<>();
        arcProgress = (ArcProgress) findViewById(R.id.arc_progress);
        btnIssue = (ImageView) findViewById(R.id.buttonIssue);
        edtSearch = (AutoCompleteTextView) findViewById(R.id.autoSearch);
        relativeHeader = (LinearLayout) findViewById(R.id.relativeHeader);
        selectedId = "";
        headermanager = new HeaderManager(IssuePointActivity.this, getResources().getString(R.string.issue_point));
        headermanager.getHeader(relativeHeader, 1);
        mImageBack = headermanager.getLeftButton();
        llData = (LinearLayout) findViewById(R.id.llData);
        llData.setVisibility(View.GONE);
        textId = (TextView) findViewById(R.id.textViewId);
        textName = (TextView) findViewById(R.id.textViewName);
        textAddress = (TextView) findViewById(R.id.textViewAddress);
        textPhone = (TextView) findViewById(R.id.textViewPhone);
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        mEditPoint = (EditText) findViewById(R.id.editPoints);
        mImageBack.setOnClickListener(this);
        btnIssue.setOnClickListener(this);
        arcProgress.setSuffixText("");
        arcProgress.setStrokeWidth(15);
        arcProgress.setBottomTextSize(50);
        arcProgress.setMax(10000000);
        arcProgress.setTextColor(getResources().getColor(R.color.white));
        arcProgress.setBackgroundColor(getResources().getColor(R.color.transparent));
        arcProgress.setUnfinishedStrokeColor(getResources().getColor(R.color.white));
        edtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                String selectedData = edtSearch.getText().toString();
                for (int i = 0; i < listUsers.size(); i++) {
                    if (listUsers.get(i).getUserName().equals(selectedData)) {
                        selectedId = listUsers.get(i).getUserId();
                        getUserData();
                        //   System.out.println("Selected Id : " + selectedId);
                        break;
                    } else {
                        selectedId = "";
                    }
                }
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (selectedId.length() > 0) {
                        llData.setVisibility(View.VISIBLE);
                    }
                } else {
                    selectedId = "";
                    llData.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edtSearch.showDropDown();
                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        } else if (v == btnIssue) {


            if (edtSearch.getText().toString().trim().equals("")) {
                CustomToast toast = new CustomToast(mContext);
                toast.show(14);
            } else if (mEditPoint.getText().toString().trim().equals("")) {
                //VKCUtils.textWatcherForEditText(mEditPoint, "Mandatory field");
                CustomToast toast = new CustomToast(mContext);
                toast.show(17);

            } else if (Integer.parseInt(mEditPoint.getText().toString().trim()) > myPoint) {
                // FeedbackSubmitApi();
                CustomToast toast = new CustomToast(mContext);
                toast.show(16);
            } else {
                submitPoints();
            }

        }
    }


    public void getUserData() {
        try {
            String[] name = {"cust_id", "role"};
            String[] values = {selectedId, "5"};

            final VolleyWrapper manager = new VolleyWrapper(GET_DATA);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            // System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);


                                    JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = objResponse.optString("status");

                                    if (status.equals("Success")) {

                                        JSONObject objData = objResponse.optJSONObject("data");
                                        String cust_id = objData.optString("customer_id");
                                        String address = objData.optString("address");
                                        String name = objData.optString("name");
                                        String phone = objData.optString("phone");
                                        textId.setText(": " + cust_id);
                                        textName.setText(": " + name);
                                        textAddress.setText(": " + address);
                                        textPhone.setText(": " + phone);
                                        llData.setVisibility(View.VISIBLE);

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

    public void getMyPoints() {
        try {
            String[] name = {"cust_id", "role"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(GET_LOYALTY_POINTS);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);


                                    JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = objResponse.optString("status");

                                    if (status.equals("Success")) {

                                        String points = objResponse.optString("loyality_point");
                                        myPoint = Integer.parseInt(points);
                                        //      arcProgress.setBottomText("Points");
                                        arcProgress.setProgress(myPoint);
                                        //  arcProgress.setSuffixText(points);
                                        //mTxtPoint.setText(points);
                                        getUsers();
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

    public void submitPoints() {
        String name[] = {"userid", "to_user_id", "to_role", "points", "role"};
        String values[] = {AppPrefenceManager.getCustomerId(mContext),
                selectedId, "5", mEditPoint.getText().toString(), "7"};

        final VolleyWrapper manager = new VolleyWrapper(SUBMIT_POINTS);
        manager.getResponsePOST(mContext, 11, name, values,
                new VolleyWrapper.ResponseListener() {
                    @Override
                    public void responseSuccess(String successResponse) {
                        // TODO Auto-generated method stub
                        Log.v("LOG", "18022015 success" + successResponse);
                        try {
                            JSONObject objResponse = new JSONObject(
                                    successResponse);
                            String status = objResponse.optString("response");
                            if (status.equals("1")) {

                                CustomToast toast = new CustomToast(
                                        mContext);
                                toast.show(18);
                                edtSearch.setText("");
                                mEditPoint.setText("");


                                getMyPoints();
                            }

                            else if(status.equals("5"))
                            {
                                CustomToast toast = new CustomToast(
                                        mContext);
                                toast.show(61);
                            }
                            else {
                                CustomToast toast = new CustomToast(
                                        mContext);
                                toast.show(13);

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        // parseResponse(successResponse);
                    }

                    @Override
                    public void responseFailure(String failureResponse) {
                        // TODO Auto-generated method stub
                        Log.v("LOG", "18022015 Errror" + failureResponse);
                    }
                });
    }

    private void getUsers() {

        String name[] = {"cust_id"};
        String value[] = {AppPrefenceManager.getCustomerId(mContext)};

        final VolleyWrapper manager = new VolleyWrapper(GET_RETAILERS);
        manager.getResponsePOST(mContext, 11, name, value,
                new VolleyWrapper.ResponseListener() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void responseSuccess(String successResponse) {

                        try {
                            JSONObject responseObj = new JSONObject(
                                    successResponse);
                            JSONObject response = responseObj
                                    .getJSONObject("response");
                            String status = response.getString("status");
                            if (status.equals("Success")) {
                                JSONArray dataArray = response
                                        .optJSONArray("data");
                                if (dataArray.length() > 0) {
                                    for (int i = 0; i < dataArray.length(); i++) {
                                        // listArticle[i]=articleArray.getString(i);
                                        JSONObject obj = dataArray
                                                .getJSONObject(i);
                                        UserModel model = new UserModel();
                                        model.setUserId(obj.getString("id"));
                                        model.setUserName(obj.getString("name"));
                                        // model.setCity(obj.getString("city"));
                                        listUsers.add(model);
                                    }
                                    ArrayList<String> listUser = new ArrayList<>();
                                    for (int i = 0; i < listUsers.size(); i++) {
                                        listUser.add(listUsers.get(i)
                                                .getUserName());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                            mContext,
                                            android.R.layout.simple_list_item_1,
                                            listUser);
                                    edtSearch.setThreshold(1);
                                    edtSearch.setAdapter(adapter);

                                } else {
                                    CustomToast toast = new CustomToast(
                                            mContext);
                                    toast.show(5);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void responseFailure(String failureResponse) { // TODO
                        // Auto-generated method stub

                    }
                });

    }
}