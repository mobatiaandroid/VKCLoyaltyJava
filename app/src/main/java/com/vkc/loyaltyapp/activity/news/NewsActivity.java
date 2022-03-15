package com.vkc.loyaltyapp.activity.news;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.HomeActivity;
import com.vkc.loyaltyapp.manager.RecyclerViewTouchListener;
import com.vkc.loyaltyapp.activity.news.adapter.NewsAdapter;
import com.vkc.loyaltyapp.activity.news.model.ColorModel;
import com.vkc.loyaltyapp.activity.news.model.ProductModel;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
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

public class NewsActivity extends AppCompatActivity implements VKCUrlConstants {
    Activity mContext;
    ArrayList<ProductModel> listProducts;
    RecyclerView listViewNews;
    ImageView btn_left;
    String newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setContentView(R.layout.activity_news_list);
        mContext = this;


        initUI();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            // listProducts = (ArrayList<ProductModel>) bundle.getSerializable("news_data");
            newsId = bundle.getString("news_id");
            //  listProducts = ((ArrayList<ProductModel>) getIntent().getExtras().getSerializable("news_data"));
            if (newsId.equals("")) {
                listProducts = (ArrayList<ProductModel>) getIntent().getSerializableExtra("news_data");

                NewsAdapter adapter = new NewsAdapter(mContext, listProducts);
                listViewNews.setAdapter(adapter);
            } else {
                getNews(newsId);
            }
        }
    }

    private void initUI() {
        // get the reference of RecyclerView
        listProducts = new ArrayList<>();
        listViewNews = (RecyclerView) findViewById(R.id.recyclerNews);
        btn_left = (ImageView) findViewById(R.id.btn_left);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newsId.equals("")) {
                    finish();
                } else {
                    Intent intent = new Intent(NewsActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NewsActivity.this);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(NewsActivity.this, DividerItemDecoration.HORIZONTAL);
        // itemDecorator.setDrawable(ContextCompat.getDrawable(NewsActivity.this, R.drawable.divider));
        listViewNews.setLayoutManager(mLayoutManager);
        listViewNews.setItemAnimator(new DefaultItemAnimator());
        listViewNews.addItemDecoration(itemDecorator);
        listViewNews.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), listViewNews, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {


                Intent intent = new Intent(getApplicationContext(),
                        NewsDetailActivity.class);
                intent.putExtra("product_data", (Serializable) listProducts.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        //getHistory();

        NewsAdapter adapter = new NewsAdapter(mContext, listProducts);
        listViewNews.setAdapter(adapter);
    }

    public void getNews(String news_id) {
        listProducts.clear();
        try {
            String[] name = {"push_id"};
            String[] values = {news_id};

            final VolleyWrapper manager = new VolleyWrapper(GET_NEWS_DETAIL);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    String response = rootObject.optString("response");
                                    String status = rootObject.optString("responsecode");


                                    if (response.equalsIgnoreCase("success")) {
                                        JSONArray dataArray = rootObject.optJSONArray("data");
                                        JSONObject objProducts = dataArray.getJSONObject(0);
                                        JSONArray arrayProduct = objProducts.optJSONArray("product_details");
                                        if (dataArray.length() > 0) {

                                            //  ArrayList<ProductModel> listProducts = new ArrayList<>();
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
                                                    ColorModel model = new ColorModel();
                                                    model.setCode(objColor.optString("code"));
                                                    model.setName(objColor.optString("name"));

                                                    listColors.add(model);
                                                }
                                                productModel.setColor(listColors);
                                                productModel.setSize(objProduct.optString("size"));
                                                productModel.setTitle(objProduct.optString("title"));
                                                listProducts.add(productModel);
                                            }


                                            NewsAdapter adapter = new NewsAdapter(mContext, listProducts);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (newsId.equals("")) {
            finish();
        } else {
            Intent intent = new Intent(NewsActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}

