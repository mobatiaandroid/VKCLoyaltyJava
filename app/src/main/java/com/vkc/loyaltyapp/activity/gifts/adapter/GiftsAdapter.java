package com.vkc.loyaltyapp.activity.gifts.adapter;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.gifts.model.GiftsModel;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.utils.CustomToast;
import com.vkc.loyaltyapp.volleymanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by user2 on 2/8/17.
 */
public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.MyViewHolder> implements VKCUrlConstants {
    List<GiftsModel> listGifts;
    ArrayList personImages;
    AppCompatActivity mContext;
    TextView textCart, textBalance, textTotal;

    public GiftsAdapter(AppCompatActivity context, List<GiftsModel> gifts, TextView textCart, TextView textBalance, TextView textTotal) {
        this.mContext = context;
        this.listGifts = gifts;
        this.textCart = textCart;
        this.textBalance = textBalance;
        this.textTotal = textTotal;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(listGifts.get(position).getTitle());
        holder.points.setText("Coupons :" + listGifts.get(position).getPoint());
        String gift_name = listGifts.get(position).getImage().replaceAll(" ", "%20");
        Picasso.with(mContext).load(gift_name).resize(300, 300).centerInside().into(holder.image);
        // implement setOnClickListener event on item view.
        holder.llCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.editQty.getText().toString().trim().equals("")) {
                    CustomToast toast = new CustomToast(mContext);
                    toast.show(40);
                } else if (holder.editQty.getText().toString().trim().equals("0")) {
                    CustomToast toast = new CustomToast(mContext);
                    toast.show(59);
                } else {


                    addToCart(holder.editQty, listGifts.get(position).getId());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return listGifts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;
        TextView points;
        ImageView image;
        LinearLayout llCart;
        EditText editQty;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.image);
            points = (TextView) itemView.findViewById(R.id.point);
            llCart = (LinearLayout) itemView.findViewById(R.id.llCart);
            editQty = (EditText) itemView.findViewById(R.id.editQty);
        }
    }

    public void addToCart(final EditText editQty, String mGiftId) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editQty.getWindowToken(), 0);

        try {
            String[] name = {"cust_id", "gift_id", "quantity", "gift_type"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), mGiftId, editQty.getText().toString().trim(), "1"};

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
                                        getCartItems();
                                        editQty.setText("");
                                        textCart.setText(cart_count);
                                    } else if (response.equalsIgnoreCase("2")) {

                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(38);
                                        editQty.setText("");
                                    } else if (response.equals("5")) {
                                        CustomToast toast = new CustomToast(
                                                mContext);
                                        toast.show(61);
                                        editQty.setText("");
                                    } else {

                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(39);
                                        editQty.setText("");
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
                                    textBalance.setText(balance_points);
                                    textTotal.setText(total_points);


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