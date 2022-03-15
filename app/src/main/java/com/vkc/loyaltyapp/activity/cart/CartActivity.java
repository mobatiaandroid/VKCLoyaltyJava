package com.vkc.loyaltyapp.activity.cart;

import android.content.Intent;
import android.os.Bundle;
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
import com.vkc.loyaltyapp.activity.cart.adapter.CartListAdapter;
import com.vkc.loyaltyapp.activity.cart.model.CartModel;
import com.vkc.loyaltyapp.activity.cart.model.DealerModel;
import com.vkc.loyaltyapp.activity.redeem.RedeemHistoryActivity;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.manager.CustomToastMessage;
import com.vkc.loyaltyapp.utils.CustomToast;
import com.vkc.loyaltyapp.volleymanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by user2 on 1/11/17.
 */
public class CartActivity extends AppCompatActivity implements VKCUrlConstants, View.OnClickListener {
    AppCompatActivity mContext;
    EditText editQuantity;
    ImageView mImageBack, btn_history;
    ListView listViewCart;
    TextView textCartTotal, textBalanceCoupon, textCartQuantity;
    ArrayList<CartModel> listCart;
    Spinner spinnerDealer;
    String mDealerId, mRole;
    ArrayList<DealerModel> listDealers;
    Button buttonOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mContext = this;
        initUI();
    }

    private void initUI() {
        // get the reference of RecyclerView
        mDealerId = "";
        listDealers = new ArrayList<>();
        listCart = new ArrayList<>();
        listViewCart = (ListView) findViewById(R.id.listViewCart);
        textBalanceCoupon = (TextView) findViewById(R.id.textBalanceCoupon);
        textCartTotal = (TextView) findViewById(R.id.textTotalCart);
        textCartQuantity = (TextView) findViewById(R.id.textCartQuantity);
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
                    mRole = listDealers.get(position - 1).getRole();
                } else {
                    mDealerId = "";
                    mRole = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getDealers();
        getCartItems();
    }

    public void getCartItems() {
        listCart.clear();
        try {
            String[] name = {"cust_id"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(GET_MY_CART);
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
                                    String balance_points = objResponse.optString("balance_points");
                                    String total_points = objResponse.optString("total_points");
                                    String total_quantity = objResponse.optString("total_quantity");
                                    textBalanceCoupon.setText(balance_points);
                                    textCartTotal.setText(total_points);
                                    textCartQuantity.setText(total_quantity);
                                    if (status.equalsIgnoreCase("Success")) {

                                        JSONArray dataArray = objResponse.optJSONArray("data");
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                CartModel model = new CartModel();
                                                JSONObject obj = dataArray.optJSONObject(i);
                                                model.setId(obj.optString("id"));
                                                model.setGift_id(obj.optString("gift_id"));
                                                model.setGift_title(obj.optString("gift_title"));
                                                model.setGift_type(obj.optString("gift_type"));
                                                model.setQuantity(obj.optString("quantity"));
                                                model.setPoint(obj.optString("point"));
                                                listCart.add(model);
                                            }
                                            CartListAdapter adapter = new CartListAdapter(mContext, listCart, textCartTotal, textBalanceCoupon, textCartQuantity, listViewCart);
                                            listViewCart.setAdapter(adapter);

                                        } else {
                                            listViewCart.setAdapter(null);
                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(43);
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
            String[] values = {AppPrefenceManager.getCustomerId(mContext), "5"};

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
                                                model.setRole(obj.optString("role"));
                                                listDealers.add(model);
                                            }
                                            ArrayList<String> listDealer = new ArrayList<String>();
                                            listDealer.add("Select Dealer");
                                            for (int i = 0; i < listDealers.size(); i++) {
                                                listDealer.add(listDealers.get(i).getName());
                                            }
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CartActivity.this, android.R.layout.simple_spinner_item, listDealer);
                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            spinnerDealer.setAdapter(adapter);

                                        } else {
                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(44);
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

    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        } else if (v == buttonOrder) {
            if (mDealerId.equals("")) {
                CustomToast toast = new CustomToast(mContext);
                toast.show(45);
            } else if (listCart.size() == 0) {
                CustomToast toast = new CustomToast(mContext);
                toast.show(46);
            } else {


                ArrayList<String> listOrder = new ArrayList<String>();
                for (int i = 0; i < listCart.size(); i++) {

                    listOrder.add(listCart.get(i).getId().toString());
                }

                Gson gson = new GsonBuilder().create();
                JsonArray details = gson.toJsonTree(listOrder).getAsJsonArray();
                placeOrder(details.toString());

            }
        } else if (v == btn_history) {
            startActivity(new Intent(CartActivity.this, RedeemHistoryActivity.class));
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
            String[] name = {"cust_id", "dealer_id", "role", "ids"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), mDealerId, mRole, ids};

            final VolleyWrapper manager = new VolleyWrapper(PLACE_ORDER);
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
                                        getCartItems();

                                    } else if (status.equalsIgnoreCase("scheme_error")) {

                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(64);
                                        spinnerDealer.setSelection(0);
                                        mDealerId = "";
                                        getCartItems();

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
