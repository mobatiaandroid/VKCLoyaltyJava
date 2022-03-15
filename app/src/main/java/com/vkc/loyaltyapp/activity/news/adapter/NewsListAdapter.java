package com.vkc.loyaltyapp.activity.news.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.news.model.NewsListModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.MyViewHolder> {
    private ArrayList<NewsListModel> newsList;
    ArrayList<String> listColors;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView textTitle;


        public MyViewHolder(View view) {
            super(view);

           /* textDate = (TextView) view.findViewById(R.id.textDate);
            textType = (TextView) view.findViewById(R.id.textType);
            textTitle = (TextView) view.findViewById(R.id.textTitle);
            textStatus = (TextView) view.findViewById(R.id.textStatus);*/
            //imageFoot = (ImageView) view.findViewById(R.id.imageFoot);
            textTitle = (TextView) view.findViewById(R.id.textTitle);
        }
    }


    public NewsListAdapter(Activity mActivity, ArrayList<NewsListModel> newsList) {
        this.newsList = newsList;
        this.activity = mActivity;


    }

    public NewsListAdapter(Activity mActivity) {

        this.activity = mActivity;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        NewsListModel listData = newsList.get(i);
        myViewHolder.textTitle.setText(listData.getMessage());
    }


    @Override
    public int getItemCount() {
        return newsList.size();
    }//return newsList.size();
}