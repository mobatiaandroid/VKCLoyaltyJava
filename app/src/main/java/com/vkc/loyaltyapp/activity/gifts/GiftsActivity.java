package com.vkc.loyaltyapp.activity.gifts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.cart.CartActivity;
import com.vkc.loyaltyapp.activity.gifts.adapter.GiftsAdapter;
import com.vkc.loyaltyapp.activity.gifts.model.GiftsModel;
import com.vkc.loyaltyapp.activity.gifts.model.VoucherModel;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.utils.CustomToast;
import com.vkc.loyaltyapp.volleymanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user2 on 2/8/17.
 */
public class GiftsActivity extends AppCompatActivity implements com.vkc.loyaltyapp.constants.VKCUrlConstants, View.OnClickListener {
    AppCompatActivity mContext;
    List<GiftsModel> listGifts;
    RecyclerView recyclerView;
    EditText editQuantity;
    ImageView mImageBack, imageCart;
    TextView textCart;
    String mVoucherId;
    LinearLayout llAddCart, llVoucher;
    Spinner spinnerVoucher;
    ArrayList<VoucherModel> listVouchers;
    TextView textCoupon;
    TextView textCartTotal, textBalanceCoupon, textCartQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);
        mContext = this;
        initUI();
    }

    private void initUI() {
        // get the reference of RecyclerView
        mVoucherId = "";
        listGifts = new ArrayList<>();
        listVouchers = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mImageBack = (ImageView) findViewById(R.id.btn_left);
        textCoupon = (TextView) findViewById(R.id.textCoupon);
        textCoupon.setVisibility(View.GONE);
        textBalanceCoupon = (TextView) findViewById(R.id.textBalanceCoupon);
        textCartTotal = (TextView) findViewById(R.id.textTotalCart);
        textCartQuantity = (TextView) findViewById(R.id.textCartQuantity);
        textCart = (TextView) findViewById(R.id.textCount);
        imageCart = (ImageView) findViewById(R.id.btn_right);
        spinnerVoucher = (Spinner) findViewById(R.id.spinnerVoucher);
        editQuantity = (EditText) findViewById(R.id.editQty);
        llAddCart = (LinearLayout) findViewById(R.id.llCart);
        llVoucher = (LinearLayout) findViewById(R.id.llVoucher);
        llVoucher.setVisibility(View.GONE);
        mImageBack.setOnClickListener(this);
        imageCart.setOnClickListener(this);
        llAddCart.setOnClickListener(this);
// set a GridLayoutManager with default vertical orientation and 3 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        //  LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        //  recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
       /* recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });*/
        //   recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        //  recyclerView.setItemAnimator(new DefaultItemAnimator());
        spinnerVoucher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setGravity(Gravity.CENTER);
                ((TextView) parent.getChildAt(0)).setTextSize(12);
                if (position > 0) {
                    mVoucherId = listVouchers.get(position - 1).getId();
                    textCoupon.setText(listVouchers.get(position - 1).getCoupon_value() + " Loyalty Points");
                    textCoupon.setVisibility(View.VISIBLE);
                } else {
                    textCoupon.setVisibility(View.GONE);
                    mVoucherId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getGifts();
        getCartItems();
    }

    public void getGifts() {
        listGifts.clear();
        listVouchers.clear();

        try {
            String[] name = {"cust_id"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(GET_GIFTS);
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
                                        JSONArray voucherArray = objResponse.optJSONArray("vouchers");
                                        if (voucherArray.length() > 0) {
                                            for (int i = 0; i < voucherArray.length(); i++) {
                                                JSONObject obj = voucherArray.optJSONObject(i);
                                                VoucherModel model = new VoucherModel();
                                                model.setId(obj.optString("id"));
                                                model.setCoupon_value(obj.optString("coupon_value"));
                                                model.setVoucher_value(obj.optString("voucher_value"));
                                                listVouchers.add(model);
                                            }
                                            ArrayList<String> listVoucher = new ArrayList<String>();
                                            listVoucher.add("Loyalty Points");
                                            for (int i = 0; i < listVouchers.size(); i++) {
                                                listVoucher.add(listVouchers.get(i).getVoucher_value() + " Voucher value" + " ( " + listVouchers.get(i).getCoupon_value() + " Loyalty Points )");
                                            }
                                            if (listVoucher.size() > 0) {

                                                llVoucher.setVisibility(View.VISIBLE);
                                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(GiftsActivity.this, android.R.layout.simple_spinner_item, listVoucher);
                                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spinnerVoucher.setAdapter(adapter);

                                            } else {
                                                llVoucher.setVisibility(View.GONE);
                                            }

                                        }
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject obj = dataArray.optJSONObject(i);
                                                GiftsModel model = new GiftsModel();
                                                model.setId(obj.optString("id"));
                                                model.setDescription(obj.optString("description"));
                                                model.setImage(obj.optString("image"));
                                                model.setPoint(obj.optString("point"));
                                                model.setTitle(obj.optString("title"));
                                                listGifts.add(model);
                                            }
                                            GiftsAdapter adapter = new GiftsAdapter(mContext, listGifts, textCart, textBalanceCoupon, textCartTotal);
                                            recyclerView.setAdapter(adapter);
                                        } else {
                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(5);
                                        }
                                        getCartCount();

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

    public void getCartCount() {

        try {
            String[] name = {"cust_id"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(GET_CART_COUNT);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    String status = rootObject.optString("response");

                                    if (status.equalsIgnoreCase("1")) {
                                        textCart.setText(rootObject.optString("cart_count"));
                                    } else if (status.equals("5")) {
                                        CustomToast toast = new CustomToast(
                                                mContext);
                                        toast.show(62);
                                    } else {
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(24);
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

    public void addToCart() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editQuantity.getWindowToken(), 0);
        try {
            String[] name = {"cust_id", "gift_id", "quantity", "gift_type"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), mVoucherId, editQuantity.getText().toString().trim(), "2"};

            final VolleyWrapper manager = new VolleyWrapper(ADD_TO_CART);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    String response = rootObject.optString("response");
                                    String cart_count = rootObject.optString("cart_count");


                                    if (response.equalsIgnoreCase("1")) {

                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(6);
                                        editQuantity.setText("");
                                        textCart.setText(cart_count);
                                        spinnerVoucher.setSelection(0);
                                        getCartCount();
                                        getCartItems();
                                    } else if (response.equalsIgnoreCase("2")) {

                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(38);
                                        editQuantity.setText("");
                                    } else if (response.equals("5")) {
                                        CustomToast toast = new CustomToast(
                                                mContext);
                                        toast.show(61);
                                    } else {
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(39);
                                        editQuantity.setText("");
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
            // Log.d("TAG", "Common error");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        } else if (v == imageCart) {
            startActivity(new Intent(GiftsActivity.this, CartActivity.class));
        } else if (v == llAddCart) {
            if (mVoucherId.equals("")) {
                CustomToast toast = new CustomToast(mContext);
                toast.show(41);
            } else if (editQuantity.getText().toString().trim().equals("")) {
                CustomToast toast = new CustomToast(mContext);
                toast.show(42);
            } else if (editQuantity.getText().toString().trim().equals("0")) {
                CustomToast toast = new CustomToast(mContext);
                toast.show(59);
            } else {
                addToCart();
            }
        }
    }

    public void getCartItems() {

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
    protected void onRestart() {
        super.onRestart();
        getCartCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCartCount();
        getCartItems();
    }
}
