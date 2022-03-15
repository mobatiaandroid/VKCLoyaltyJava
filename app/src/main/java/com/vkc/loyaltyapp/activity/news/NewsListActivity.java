package com.vkc.loyaltyapp.activity.news;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;


import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.manager.RecyclerViewTouchListener;
import com.vkc.loyaltyapp.activity.news.adapter.NewsListAdapter;
import com.vkc.loyaltyapp.activity.news.model.ColorModel;
import com.vkc.loyaltyapp.activity.news.model.NewsListModel;
import com.vkc.loyaltyapp.activity.news.model.ProductModel;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.utils.CustomToast;
import com.vkc.loyaltyapp.volleymanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListActivity extends AppCompatActivity implements VKCUrlConstants {
    Activity mContext;
    ArrayList<NewsListModel> listNews;
    RecyclerView listViewNews;
    ImageView btn_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setContentView(R.layout.activity_news);
        mContext = this;
        initUI();
    }

    private void initUI() {
        // get the reference of RecyclerView
        listNews = new ArrayList<>();
        listViewNews = (RecyclerView) findViewById(R.id.recyclerListNews);
        btn_left = (ImageView) findViewById(R.id.btn_left);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NewsListActivity.this);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(NewsListActivity.this, DividerItemDecoration.HORIZONTAL);
        // itemDecorator.setDrawable(ContextCompat.getDrawable(NewsActivity.this, R.drawable.divider));
        listViewNews.setLayoutManager(mLayoutManager);
        listViewNews.setItemAnimator(new DefaultItemAnimator());
        listViewNews.addItemDecoration(itemDecorator);
        listViewNews.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), listViewNews, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {


                Intent intent = new Intent(getApplicationContext(),
                        NewsActivity.class);
                //AppController.listProducts = listNews.get(position).getProductDetails();
                intent.putExtra("news_data", (Serializable) listNews.get(position).getProductDetails());
                intent.putExtra("news_id", "");

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        getNews();

    }

    public void getNews() {
        listNews.clear();
        try {
            String[] name = {"cust_id", "role"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext)};//AppPrefenceManager.getCustomerId(mContext),AppPrefenceManager.getUserType(mContext)

            final VolleyWrapper manager = new VolleyWrapper(GET_NEWS);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    String response = rootObject.optString("response");
                                    String status = rootObject.optString("statuscode");


                                    if (response.equalsIgnoreCase("success")) {
                                        JSONArray dataArray = rootObject.optJSONArray("data");
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject obj = dataArray.optJSONObject(i);
                                                NewsListModel model = new NewsListModel();
                                                model.setId(obj.optString("id"));
                                                model.setMessage(obj.optString("message"));
                                                JSONArray arrayProduct = obj.optJSONArray("product_details");
                                                ArrayList<ProductModel> listProducts = new ArrayList<>();
                                                for (int j = 0; j < arrayProduct.length(); j++) {
                                                    JSONObject objProduct = arrayProduct.optJSONObject(j);
                                                    ProductModel productModel = new ProductModel();
                                                    productModel.setBrand(objProduct.optString("brand"));
                                                    productModel.setThumbImage(objProduct.optString("thumb_image"));
                                                    productModel.setDescription(objProduct.optString("description"));
                                                    productModel.setModelNo(objProduct.optString("model_no"));
                                                    productModel.setGender(objProduct.optString("gender"));
                                                    productModel.setMrp(objProduct.optString("mrp"));
                                                    JSONArray arrayImages = objProduct.optJSONArray("files");
                                                    ArrayList listImages = new ArrayList();
                                                    for (int k = 0; k < arrayImages.length(); k++) {


                                                        listImages.add(arrayImages.get(k));
                                                    }
                                                    productModel.setFiles(listImages);
                                                    JSONArray arrayColors = objProduct.optJSONArray("color");
                                                    ArrayList<ColorModel> listColors = new ArrayList();
                                                    for (int k = 0; k < arrayColors.length(); k++) {
                                                        JSONObject objColor = arrayColors.getJSONObject(k);
                                                        ColorModel Color_model = new ColorModel();
                                                        Color_model.setCode(objColor.optString("code"));
                                                        Color_model.setName(objColor.optString("name"));

                                                        listColors.add(Color_model);
                                                    }
                                                    productModel.setColor(listColors);
                                                    productModel.setSize(objProduct.optString("size"));
                                                    productModel.setTitle(objProduct.optString("title"));
                                                    listProducts.add(productModel);
                                                }
                                                model.setProductDetails(listProducts);
                                                listNews.add(model);
                                            }


                                            NewsListAdapter adapter = new NewsListAdapter(mContext, listNews);
                                            listViewNews.setAdapter(adapter);
                                        } else {
                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(5);
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
            Log.d("TAG", "Common error");
        }
    }


    public class DialogData extends Dialog {

        public Activity mActivity;
        String type, message;
        int position;

        public DialogData(Activity a, int position) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;
            this.position = position;

        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_history);
            init();

        }

        private void init() {

            // Button buttonSet = (Button) findViewById(R.id.buttonOk);
           /* TextView txt_date = (TextView) findViewById(R.id.txt_date);
            TextView txt_type = (TextView) findViewById(R.id.txt_type);
            TextView txt_name = (TextView) findViewById(R.id.txt_name);
            TextView text_point = (TextView) findViewById(R.id.text_point);
            TextView text_quantity = (TextView) findViewById(R.id.text_quantity);
            TextView text_status = (TextView) findViewById(R.id.text_status);
            TextView text_dealer = (TextView) findViewById(R.id.text_dealer);

            Button buttonCancel = (Button) findViewById(R.id.btn_ok);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            txt_date.setText(": " + listGifts.get(position).getDate());
            txt_type.setText(": " + listGifts.get(position).getGift_type());
            txt_name.setText(": " + listGifts.get(position).getTitle());
            text_point.setText(": " + listGifts.get(position).getPoint());
            text_quantity.setText(": " + listGifts.get(position).getQuantity());
            text_status.setText(": " + listGifts.get(position).getStatus());
            text_dealer.setText(": " + listGifts.get(position).getDealer());*/


        }


    }
}

