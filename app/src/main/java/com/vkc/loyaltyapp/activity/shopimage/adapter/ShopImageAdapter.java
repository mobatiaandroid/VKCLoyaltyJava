package com.vkc.loyaltyapp.activity.shopimage.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.gifts.model.GiftsModel;
import com.vkc.loyaltyapp.activity.shopimage.ShopImageActivity;
import com.vkc.loyaltyapp.activity.shopimage.model.ImageListModel;
import com.vkc.loyaltyapp.appcontroller.AppController;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.utils.CustomToast;
import com.vkc.loyaltyapp.volleymanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by user2 on 2/8/17.
 */
public class ShopImageAdapter extends RecyclerView.Adapter<ShopImageAdapter.MyViewHolder> implements VKCUrlConstants {
    List<ImageListModel> listImages;
    ArrayList personImages;
    AppCompatActivity mContext;
    ShopImageAdapter shopAdapter;
    RecyclerView recyclerView;

    public ShopImageAdapter(AppCompatActivity context, List<ImageListModel> images, RecyclerView recyclerImages) {
        this.mContext = context;
        this.listImages = images;
        this.recyclerView = recyclerImages;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_image, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        String imsge_name = listImages.get(position).getImage().replaceAll(" ", "%20");
        Picasso.with(mContext).load(imsge_name).resize(200, 200).centerInside().into(holder.image);
        // implement setOnClickListener event on item view.
        holder.image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  if (holder.editQty.getText().toString().trim().equals("")) {
                    CustomToast toast = new CustomToast(mContext);
                    toast.show(40);
                } else if (holder.editQty.getText().toString().trim().equals("0")) {
                    CustomToast toast = new CustomToast(mContext);
                    toast.show(59);
                } else {


                    addToCart(holder.editQty, listGifts.get(position).getId());
                }*/

                DialogConfirm dialog = new DialogConfirm(mContext, listImages.get(position).getId());
                dialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's

        ImageView image, image_close;


        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            image = (ImageView) itemView.findViewById(R.id.image);
            image_close = (ImageView) itemView.findViewById(R.id.image);

        }
    }

    public class DialogConfirm extends Dialog implements
            android.view.View.OnClickListener {

        public AppCompatActivity mActivity;
        String type, message, id;

        public DialogConfirm(AppCompatActivity a, String id) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;
            this.id = id;

        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_delete_image);
            init();

        }

        private void init() {

            // Button buttonSet = (Button) findViewById(R.id.buttonOk);
            TextView textYes = (TextView) findViewById(R.id.textYes);
            TextView textNo = (TextView) findViewById(R.id.textNo);


            textYes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    deleteImage(id);
                    dismiss();
                }
            });


            textNo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    // mActivity.finish();


                }
            });

        }

        @Override
        public void onClick(View v) {

            dismiss();
        }

    }

    public void deleteImage(String id) {

        try {


            String[] name = {"id"};
            String[] values = {id};

            final VolleyWrapper manager = new VolleyWrapper(DELETE_IMAGE);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    //JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = rootObject.optString("status");
                                    if (status.equals("Success")) {
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(52);
                                        getImageHistory();
                                    } else if (status.equals("Error")) {
                                        getImageHistory();
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


    public void getImageHistory() {
        AppController.imageList.clear();
        try {
            String[] name = {"cust_id", "role"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(GET_IMAGE_HISTORY);
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
                                    if (status.equals("Success")) {
                                        JSONArray objData = objResponse.optJSONArray("data");
                                        if (objData.length() > 0) {
                                            for (int i = 0; i < objData.length(); i++) {
                                                JSONObject obj = objData.optJSONObject(i);
                                                ImageListModel model = new ImageListModel();
                                                model.setImage(obj.getString("image"));
                                                model.setId(obj.getString("id"));
                                                AppController.imageList.add(model);
                                            }
                                      /*      if (imageList.size() > 1) {
                                                if (!imageList.get(0).getImage().equals("")) {

                                                    relative1.setVisibility(View.VISIBLE);
                                                    image1Delete.setVisibility(View.VISIBLE);
                                                    Picasso.with(mContext).load(imageList.get(0).getImage()).resize(200, 200).centerInside().into(image1);
                                                } else {
                                                    relative1.setVisibility(View.GONE);
                                                    image1Delete.setVisibility(View.GONE);

                                                }

                                                if (!imageList.get(1).getImage().equals("")) {
                                                    relative2.setVisibility(View.VISIBLE);
                                                    image2Delete.setVisibility(View.VISIBLE);
                                                    Picasso.with(mContext).load(imageList.get(1).getImage()).resize(200, 200).centerInside().into(image2);
                                                } else {
                                                    relative2.setVisibility(View.GONE);
                                                    image2Delete.setVisibility(View.GONE);
                                                }
                                            } else {

                                                relative2.setVisibility(View.GONE);
                                                image2Delete.setVisibility(View.GONE);
                                                if (!imageList.get(0).getImage().equals("")) {

                                                    relative1.setVisibility(View.VISIBLE);
                                                    image1Delete.setVisibility(View.VISIBLE);
                                                    Picasso.with(mContext).load(imageList.get(0).getImage()).resize(200, 200).centerInside().into(image1);
                                                } else {
                                                    relative1.setVisibility(View.GONE);
                                                    image1Delete.setVisibility(View.GONE);

                                                }
                                            }*/


                                            ShopImageAdapter adapter = new ShopImageAdapter(mContext, AppController.imageList,recyclerView);
                                            recyclerView.setAdapter(adapter);

                                        } else {


                                            // initUI();
                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(51);
                                            //ShopImageAdapter adapter = new ShopImageAdapter(mContext, AppController.imageList,recyclerView);
                                            recyclerView.setAdapter(null);

                                        }
                                       /* String imageUrl = objData.optString("image");
                                        Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.shop_image).into(imageShop);*/
                                    }

                                    //    } else {
                                    //  CustomToast toast = new CustomToast(mContext);
                                    //    toast.show(4);
                                    //   }

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
}