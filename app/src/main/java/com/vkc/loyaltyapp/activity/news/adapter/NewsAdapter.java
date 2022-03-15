package com.vkc.loyaltyapp.activity.news.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.news.model.ColorModel;
import com.vkc.loyaltyapp.activity.news.model.ProductModel;


import java.util.ArrayList;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private ArrayList<ProductModel> newsList;
    ArrayList<String> listColors;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageFoot;
        TextView txtModelName;
        TextView txtSize;
        TextView txtBrand;
        RecyclerView recyclerColor;

        public MyViewHolder(View view) {
            super(view);

            txtModelName = (TextView) view.findViewById(R.id.txtModelName);
            txtSize = (TextView) view.findViewById(R.id.txtSize);
            txtBrand = (TextView) view.findViewById(R.id.txtBrand);
            imageFoot = (ImageView) view.findViewById(R.id.imageFoot);
            recyclerColor = (RecyclerView) view.findViewById(R.id.recyclerColor);
            // DividerItemDecoration itemDecorator = new DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL);
            recyclerColor.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true));

            // itemDecorator.setDrawable(ContextCompat.getDrawable(NewsActivity.this, R.drawable.divider));
            recyclerColor.setItemAnimator(new DefaultItemAnimator());
            //  recyclerColor.addItemDecoration(itemDecorator);
        }
    }


    public NewsAdapter(Activity mActivity, ArrayList<ProductModel> newsList) {
        this.newsList = newsList;
        this.activity = mActivity;


    }

    public NewsAdapter(Activity mActivity) {

        this.activity = mActivity;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        //Profile pro = profiles.get(position);
        ProductModel list_item = newsList.get(position);
        ArrayList<ColorModel> listColors = (ArrayList<ColorModel>) newsList.get(position).getColor();
        ColorAdapter adapter = new ColorAdapter(activity, listColors);
        viewHolder.recyclerColor.setAdapter(adapter);
        viewHolder.imageFoot.bringToFront();
        viewHolder.txtBrand.setText(list_item.getBrand());
        viewHolder.txtModelName.setText(list_item.getModelNo());
        viewHolder.txtSize.setText(list_item.getSize());
        String image_url = list_item.getThumbImage();
        image_url = image_url.replaceAll(" ", "%20");
        Glide.with(activity).load(image_url).into(viewHolder.imageFoot);


    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }//return newsList.size();
}