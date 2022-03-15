package com.vkc.loyaltyapp.activity.news;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.news.adapter.ColorAdapter;
import com.vkc.loyaltyapp.activity.news.adapter.CustomPagerAdapter;
import com.vkc.loyaltyapp.activity.news.model.ColorModel;
import com.vkc.loyaltyapp.activity.news.model.NewsListModel;
import com.vkc.loyaltyapp.activity.news.model.ProductModel;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

import java.util.ArrayList;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class NewsDetailActivity extends Activity implements VKCUrlConstants {
    Activity mContext;
    ArrayList<NewsListModel> listGifts;
    RecyclerView recyclerColor;
    ImageView btn_left;
    ViewPager mViewPager;
    ProductModel mProduct;
    TextView txtModelName;
    TextView txtSize;
    TextView txtCategory, txtMrp, txtDescription;
    WebView webDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setContentView(R.layout.activity_news_details);
        mContext = this;

        recyclerColor = (RecyclerView) findViewById(R.id.recyclerColor);
        // DividerItemDecoration itemDecorator = new DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL);
        recyclerColor.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, true));

        // itemDecorator.setDrawable(ContextCompat.getDrawable(NewsActivity.this, R.drawable.divider));
        recyclerColor.setItemAnimator(new DefaultItemAnimator());
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            // listProducts = (ArrayList<ProductModel>) bundle.getSerializable("news_data");

            //  listProducts = ((ArrayList<ProductModel>) getIntent().getExtras().getSerializable("news_data"));

            mProduct = (ProductModel) getIntent().getSerializableExtra("product_data");

            /*NewsAdapter adapter = new NewsAdapter(mContext, listProducts);
            listViewNews.setAdapter(adapter);*/
        }

        initUI();
    }

    private void initUI() {
        // get the reference of RecyclerView

        btn_left = (ImageView) findViewById(R.id.btn_left);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.pager);

        txtModelName = (TextView) findViewById(R.id.txtModelName);
        txtSize = (TextView) findViewById(R.id.txtSize);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        txtMrp = (TextView) findViewById(R.id.txtMrp);
        txtDescription = (TextView) findViewById(R.id.txtDesc);

        webDescription = (WebView) findViewById(R.id.webDescription);
        webDescription.getSettings().setBuiltInZoomControls(false);
        webDescription.getSettings().setDisplayZoomControls(false);
        webDescription.setBackgroundColor(Color.TRANSPARENT);
        webDescription.setScrollbarFadingEnabled(true);
        if (mProduct.getDescription().trim().length() > 0) {
            txtDescription.setVisibility(View.VISIBLE);
            String htmlData = "<!DOCTYPE html><html> <head><meta name=viewport content=target-densitydpi=medium-dpi, width=device-width/></head><body style=\"margin: 0px; padding: 0px;width:100%;height:auto;\">" +
                    "<p style=\"text-align:justify;font-size:12px;\">" + mProduct.getDescription() + "</p></body></html>";
            webDescription.loadData(htmlData, "text/html", "UTF-8");
        } else {
            txtDescription.setVisibility(View.GONE);
        }


        // webView.loadUrl(mProduct.getFiles().get(position));
        txtSize.setText(mProduct.getSize());
        txtModelName.setText("Model : " + mProduct.getModelNo());
        //webDescription.setText(mProduct.getDescription());
        txtCategory.setText("CATEGORY : " + mProduct.getGender());
        txtMrp.setText("MRP : " + mProduct.getMrp());
        CustomPagerAdapter
                mCustomPagerAdapter = new CustomPagerAdapter(this, mProduct);
        ArrayList<ColorModel> listColors = (ArrayList<ColorModel>) mProduct.getColor();
        ColorAdapter adapter = new ColorAdapter(mContext, listColors);
        recyclerColor.setAdapter(adapter);
        BookFlipPageTransformer bookFlipPageTransformer = new BookFlipPageTransformer();

// Enable / Disable scaling while flipping. If true, then next page will scale in (zoom in). By default, its true.
        bookFlipPageTransformer.setEnableScale(true);

// The amount of scale the page will zoom. By default, its 5 percent.
        bookFlipPageTransformer.setScaleAmountPercent(10f);

// Assign the page transformer to the ViewPager.
        mViewPager.setPageTransformer(true, bookFlipPageTransformer);
        DotsIndicator indicator = (DotsIndicator) findViewById(R.id.dots_indicator);

        mViewPager.setAdapter(mCustomPagerAdapter);
        indicator.setViewPager(mViewPager);

    }


}
