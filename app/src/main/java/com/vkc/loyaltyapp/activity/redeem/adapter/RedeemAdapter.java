package com.vkc.loyaltyapp.activity.redeem.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.dealers.model.DealerModel;
import com.vkc.loyaltyapp.activity.redeem.model.RedeemModel;

import java.util.ArrayList;

/**
 * Created by user2 on 3/8/17.
 */
public class RedeemAdapter extends BaseAdapter {
    AppCompatActivity mActivity;

    LayoutInflater mLayoutInflater;
    ArrayList<RedeemModel> listModel;
    private ArrayList<DealerModel> mOriginalValues; // Original Values
    private ArrayList<DealerModel> mDisplayedValues;

    public RedeemAdapter(AppCompatActivity mActivity,
                         ArrayList<RedeemModel> listModels) {

        this.mActivity = mActivity;
        listModel = listModels;
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

        TextView textDate;
        TextView textType;
        TextView textTitle;
        TextView textStatus;


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder viewHolder = null;
        View v = view;

        LayoutInflater mInflater = (LayoutInflater) mActivity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = mInflater
                    .inflate(R.layout.item_redeem_list, null);
            viewHolder = new ViewHolder();
            viewHolder.textDate = (TextView) view.findViewById(R.id.textDate);
            viewHolder.textType = (TextView) view.findViewById(R.id.textType);
            viewHolder.textTitle = (TextView) view.findViewById(R.id.textTitle);
            viewHolder.textStatus = (TextView) view.findViewById(R.id.textStatus);


            //   viewHolder.checkBox.setOnCheckedChangeListener(null);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textDate.setText(listModel.get(position).getDate());
        viewHolder.textType.setText(listModel.get(position).getGift_type());
        viewHolder.textTitle.setText(listModel.get(position).getTitle());
        viewHolder.textStatus.setText(listModel.get(position).getQuantity());

        return view;
    }

}

