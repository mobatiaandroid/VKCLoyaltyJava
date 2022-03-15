package com.vkc.loyaltyapp.activity.subdealerredeem;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.cart.model.DealerModel;
import com.vkc.loyaltyapp.activity.redeem_subdealer.RedeemHistorySubdealerActivity;
import com.vkc.loyaltyapp.activity.subdealerredeem.adapter.RetailersListAdapter;
import com.vkc.loyaltyapp.activity.subdealerredeem.model.RetailerModel;
import com.vkc.loyaltyapp.appcontroller.AppController;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.manager.CustomToastMessage;
import com.vkc.loyaltyapp.utils.CustomToast;
import com.vkc.loyaltyapp.volleymanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user2 on 14/3/18.
 */
public class SubdealerRedeemActivity extends AppCompatActivity implements VKCUrlConstants, View.OnClickListener {
    AppCompatActivity mContext;
    EditText editQuantity;
    ImageView mImageBack, btn_history;
    ListView listViewRetailer;
    TextView textCartTotal, textBalanceCoupon, textCartQuantity;
    ArrayList<RetailerModel> listRetailers;
    Spinner spinnerDealer;
    String mDealerId;
    ArrayList<DealerModel> listDealers;
    Button buttonOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subdealer_redeem);
        mContext = this;
        initUI();
    }

    private void initUI() {
        // get the reference of RecyclerView
        mDealerId = "";
        listDealers = new ArrayList<>();
        listRetailers = new ArrayList<>();
        listViewRetailer = (ListView) findViewById(R.id.listViewRetailers);

        buttonOrder = (Button) findViewById(R.id.buttonOrder);
        mImageBack = (ImageView) findViewById(R.id.btn_left);
        btn_history = (ImageView) findViewById(R.id.btn_right);
        spinnerDealer = (Spinner) findViewById(R.id.spinnerDealer);
        mImageBack.setOnClickListener(this);
        buttonOrder.setOnClickListener(this);
        btn_history.setOnClickListener(this);
        spinnerDealer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setGravity(Gravity.CENTER);
                ((TextView) parent.getChildAt(0)).setTextSize(12);
                if (position > 0) {
                    mDealerId = listDealers.get(position - 1).getId();
                } else {
                    mDealerId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getDealers();

    }

    public void getRetailerList() {
        listRetailers.clear();
        try {
            String[] name = {"cust_id"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(GET_SUBDEALER_RETAILERS);
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

                                        JSONArray dataArray = objResponse.optJSONArray("data");
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                RetailerModel model = new RetailerModel();
                                                JSONObject obj = dataArray.optJSONObject(i);
                                                model.setRetailer_id(obj.optString("retailer_id"));
                                                model.setUser_name(obj.optString("user_name"));
                                                model.setUserID(obj.optString("userID"));

                                                listRetailers.add(model);
                                            }
                                            RetailersListAdapter adapter = new RetailersListAdapter(mContext, listRetailers);

                                            listViewRetailer.setAdapter(adapter);

                                        } else {
                                            listViewRetailer.setAdapter(null);
                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(66);
                                        }


                                    } else {
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(4);
                                        // Toast.makeText(mContext, getResources().getString(R.string.invalid_user), Toast.LENGTH_SHORT).show();
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

        }
    }

    public void getDealers() {
        listDealers.clear();
        try {
            String[] name = {"cust_id", "role"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), "7"};

            final VolleyWrapper manager = new VolleyWrapper(GET_DEALERS_SUBDEALER);
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

                                        JSONArray dataArray = objResponse.optJSONArray("data");
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                DealerModel model = new DealerModel();
                                                JSONObject obj = dataArray.optJSONObject(i);
                                                model.setId(obj.optString("id"));
                                                model.setName(obj.optString("name"));

                                                listDealers.add(model);
                                            }
                                            ArrayList<String> listDealer = new ArrayList<String>();
                                            listDealer.add("Select Dealer");
                                            for (int i = 0; i < listDealers.size(); i++) {
                                                listDealer.add(listDealers.get(i).getName());
                                            }
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SubdealerRedeemActivity.this, android.R.layout.simple_spinner_item, listDealer);
                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            spinnerDealer.setAdapter(adapter);

                                        } else {
                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(44);
                                        }
                                        getRetailerList();

                                    } else {
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(4);
                                        // Toast.makeText(mContext, getResources().getString(R.string.invalid_user), Toast.LENGTH_SHORT).show();
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

        }
    }

    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        } else if (v == buttonOrder) {
            if (mDealerId.equals("")) {
                CustomToast toast = new CustomToast(mContext);
                toast.show(45);
            } else if (listRetailers.size() == 0) {
                CustomToast toast = new CustomToast(mContext);
                toast.show(46);
            }

            //  else {


            ArrayList<String> listRetailer = new ArrayList<String>();
            for (int i = 0; i < listRetailers.size(); i++) {
                if (AppController.listRetailers.get(i).isChecked()) {
                    listRetailer.add(AppController.listRetailers.get(i).getRetailer_id());
                }
            }
            if (listRetailer.size() == 0) {
                CustomToast toast = new CustomToast(mContext);
                toast.show(65);
            } else if (listRetailer.size() > 0 && !mDealerId.equals("")) {

                Gson gson = new GsonBuilder().create();
                JsonArray details = gson.toJsonTree(listRetailer).getAsJsonArray();
                placeOrder(details.toString());
            }
            //  }
        } else if (v == btn_history) {
            startActivity(new Intent(SubdealerRedeemActivity.this, RedeemHistorySubdealerActivity.class));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //getCartItems();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void placeOrder(String ids) {

        try {
            String[] name = {"retailer_ids", "cust_id", "dealer_id"};
            String[] values = {ids, AppPrefenceManager.getCustomerId(mContext), mDealerId};

            final VolleyWrapper manager = new VolleyWrapper(PLACE_ORDER_SUBDEALER);
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
                                    String message = objResponse.optString("message");
                                    if (status.equalsIgnoreCase("Success")) {


                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(47);
                                        spinnerDealer.setSelection(0);
                                        mDealerId = "";
                                        getRetailerList();

                                    } else if (status.equalsIgnoreCase("scheme_error")) {


                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(64);
                                        spinnerDealer.setSelection(0);
                                        mDealerId = "";
                                        getRetailerList();

                                    } else {


                                        CustomToastMessage toast = new CustomToastMessage(mContext, message);
                                        //  toast.show(48);
                                        // Toast.makeText(mContext, getResources().getString(R.string.invalid_user), Toast.LENGTH_SHORT).show();
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

        }
    }
}
