package com.vkc.loyaltyapp.activity.cart.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.cart.model.CartModel;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.utils.CustomToast;
import com.vkc.loyaltyapp.volleymanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by user2 on 1/11/17.
 */
public class CartListAdapter extends BaseAdapter implements VKCUrlConstants {
    AppCompatActivity mContext;
    String qty = "";
    LayoutInflater mLayoutInflater;
    ArrayList<CartModel> listModel;
    TextView textCartCoupon, textBalance, textCart;
    ListView listViewCart;

    public CartListAdapter(AppCompatActivity mActivity,
                           ArrayList<CartModel> listModel, TextView textCartCoupon, TextView textBalance, TextView textCart, ListView listViewCart) {

        this.mContext = mActivity;
        this.listModel = listModel;
        this.textCartCoupon = textCartCoupon;
        this.textBalance = textBalance;
        this.textCart = textCart;
        this.listViewCart = listViewCart;
        // AppController.listDealers.clear();

        // this.notifyDataSetChanged();
        //System.out.println("Length" + listModel.size());
        // mLayoutInflater = LayoutInflater.from(mActivity);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listModel.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    static class ViewHolder {

        TextView textType;
        TextView textTitle;
        TextView textCoupon;
        EditText editQuantity;
        ImageView imageRemove;


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder viewHolder = null;

        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = mInflater
                    .inflate(R.layout.item_cart_list, null);
            viewHolder = new ViewHolder();
            viewHolder.textType = (TextView) view.findViewById(R.id.textType);
            viewHolder.textTitle = (TextView) view.findViewById(R.id.textName);
            viewHolder.textCoupon = (TextView) view.findViewById(R.id.textCoupons);
            viewHolder.editQuantity = (EditText) view.findViewById(R.id.editQuantity);
            viewHolder.imageRemove = (ImageView) view.findViewById(R.id.imageDelete);
            viewHolder.editQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.imageRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> listDel = new ArrayList<String>();

                listDel.add(listModel.get(position).getId().toString());
                Gson gson = new GsonBuilder().create();
                JsonArray details = gson.toJsonTree(listDel).getAsJsonArray();
               // System.out.println("JsonArray:" + details);
                deleteCart(details.toString());
            }
        });

        viewHolder.textType.setText(listModel.get(position).getGift_type());
        viewHolder.textTitle.setText(listModel.get(position).getGift_title());
        viewHolder.textCoupon.setText(listModel.get(position).getPoint());
        viewHolder.editQuantity.setText(listModel.get(position).getQuantity());


        viewHolder.editQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count >= 1) {

                    if (listModel.get(position).getQuantity().equals(s.toString())) {

                    } else {
                        if (s.toString().equals("0")) {
                            CustomToast toast = new CustomToast(mContext);
                            toast.show(59);
                            getCartItems();
                        } else {
                            editCart(listModel.get(position).getId(), s.toString());
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        viewHolder.editQuantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                  /* Write your logic here that will be executed when user taps next button */
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    editCart(listModel.get(position).getId(), v.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });


        return view;
    }

    public void editCart(String id, String qty) {

        try {
            String[] name = {"cust_id", "id", "quantity"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), id, qty};

            final VolleyWrapper manager = new VolleyWrapper(EDIT_MY_CART);
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
                                        textCartCoupon.setText(objResponse.optString("total_points"));
                                        textBalance.setText(objResponse.optString("balance_points"));
                                        textCart.setText(objResponse.optString("total_quantity"));

                                        getCartItems();
                                    }

                                    else if (status.equalsIgnoreCase("scheme_error")) {
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(63);
                                    }
                                    else {
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(38);
                                        getCartItems();
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

    public void deleteCart(String listDel) {

        try {
            String[] name = {"cust_id", "ids"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), listDel};

            final VolleyWrapper manager = new VolleyWrapper(DELETE_MY_CART);
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
                                        listViewCart.setAdapter(null);
                                        textCartCoupon.setText(objResponse.optString("total_points"));
                                        textBalance.setText(objResponse.optString("balance_points"));
                                        textCart.setText(objResponse.optString("total_quantity"));
                                        getCartItems();
                                    } else {
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(49);
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

    public void getCartItems() {
        listModel.clear();
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
                                    textBalance.setText(balance_points);
                                    textCartCoupon.setText(total_points);
                                    textCart.setText(total_quantity);
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
                                                listModel.add(model);
                                            }

                                            listViewCart.setAdapter(null);
                                            CartListAdapter adapter = new CartListAdapter(mContext, listModel, textCartCoupon, textBalance, textCart, listViewCart);

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
}