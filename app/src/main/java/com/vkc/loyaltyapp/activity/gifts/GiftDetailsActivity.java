package com.vkc.loyaltyapp.activity.gifts;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.issuepoint.model.UserModel;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.utils.CustomToast;
import com.vkc.loyaltyapp.volleymanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user2 on 2/8/17.
 */
public class GiftDetailsActivity extends AppCompatActivity implements VKCUrlConstants, View.OnClickListener {
    AppCompatActivity mContext;
    ImageView imageThumb;
    TextView textDescription, textPoints, textAddCart, textCart;
    String mImageUrl, mDescription, mPoints, mGiftId, mDealerId = "";
    EditText editQty;
    ImageView mImageBack;
    ArrayList<UserModel> listUsers;
    LinearLayout llDistributor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_detail);
        mContext = this;
        Intent intent = getIntent();
        mImageUrl = intent.getExtras().getString("image");
        mDescription = intent.getExtras().getString("description");
        mPoints = intent.getExtras().getString("point");
        mGiftId = intent.getExtras().getString("id");
        initUI();
    }

    private void initUI() {
        // get the reference of RecyclerView
        listUsers = new ArrayList<>();
        imageThumb = (ImageView) findViewById(R.id.imageThumb);
        textDescription = (TextView) findViewById(R.id.textDescription);
        textPoints = (TextView) findViewById(R.id.textPoints);
        textAddCart = (TextView) findViewById(R.id.textAddCart);
        editQty = (EditText) findViewById(R.id.editQuantity);
        textCart = (TextView) findViewById(R.id.textCount);
        llDistributor = (LinearLayout) findViewById(R.id.llDistributor);

        mImageBack = (ImageView) findViewById(R.id.btn_left);
        mImageBack.setOnClickListener(this);

        textAddCart.setOnClickListener(this);
        Picasso.with(GiftDetailsActivity.this)
                .load(mImageUrl)
                .into(imageThumb, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        //do smth when picture is loaded successfully

                    }

                    @Override
                    public void onError() {
                        //do smth when there is picture loading error
                    }
                });
        textDescription.setText(mDescription);
        textPoints.setText(mPoints);
        if (AppPrefenceManager.getUserType(mContext).equals("7")) {
            textAddCart.setVisibility(View.GONE);
            llDistributor.setVisibility(View.GONE);
        } else {
            textAddCart.setVisibility(View.VISIBLE);
            llDistributor.setVisibility(View.VISIBLE);

        }

    }

    public void addToCart() {


        try {
            String[] name = {"cust_id", "gift_id", "quantity"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), mGiftId, editQty.getText().toString().trim()};

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
                                        editQty.setText("");
                                        textCart.setText(cart_count);
                                    } else if (response.equalsIgnoreCase("2")) {

                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(38);
                                    } else {
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(39);
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
        } else if (v == textAddCart) {
            if (editQty.getText().toString().trim().equals("")) {
                CustomToast toast = new CustomToast(mContext);
                toast.show(40);
            } else {
                addToCart();
            }
        }

    }


}
