package com.vkc.loyaltyapp.activity.news.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.news.model.ProductModel;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;


public class CustomPagerAdapter extends PagerAdapter {

    Activity mContext;
    LayoutInflater mLayoutInflater;
    int[] mResources;
    ProductModel mProduct;
    int list_Size;

    public CustomPagerAdapter(Activity context, ProductModel mProduct) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mProduct = mProduct;
        this.list_Size = mProduct.getFiles().size();
        /*mResources = new int[]{
                R.drawable.footware,
                R.drawable.footware,
                R.drawable.footware
        };*/
    }

    @Override
    public int getCount() {
        return list_Size;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ConstraintLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_pager, container, false);
        String url = mProduct.getFiles().get(position);
        url = url.replaceAll(" ", "%20");
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        Glide.with(mContext).load(url).into(imageView);
        // imageView.setImageResource(mResources[position]);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = mContext.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.layout_image_zoom, null);
                dialogBuilder.setView(dialogView);

                WebView webView = (WebView) dialogView.findViewById(R.id.imageLarge);
                ImageView imageClose = (ImageView) dialogView.findViewById(R.id.imageClose);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setDisplayZoomControls(false);
                webView.setInitialScale(80);
                webView.setBackgroundColor(Color.TRANSPARENT);

                webView.setScrollbarFadingEnabled(true);
                webView.loadUrl(mProduct.getFiles().get(position));
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                imageClose.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        alertDialog.dismiss();
                    }
                });
            }
        });
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}