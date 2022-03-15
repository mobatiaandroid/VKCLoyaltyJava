package com.vkc.loyaltyapp.activity.redeem_subdealer.adapter;

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
public class SubdealerRedeemAdapter extends BaseAdapter {
    AppCompatActivity mActivity;

    LayoutInflater mLayoutInflater;
    ArrayList<RedeemModel> listModel;
    private ArrayList<DealerModel> mOriginalValues; // Original Values
    private ArrayList<DealerModel> mDisplayedValues;

    public SubdealerRedeemAdapter(AppCompatActivity mActivity,
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

        TextView textRetailer;
        TextView textGift;
        TextView textCoupon;
        TextView textQty;


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
                    .inflate(R.layout.item_subdealer_redeem_list, null);
            viewHolder = new ViewHolder();
            viewHolder.textRetailer = (TextView) view.findViewById(R.id.textRetailer);
            viewHolder.textGift = (TextView) view.findViewById(R.id.textType);
            viewHolder.textCoupon = (TextView) view.findViewById(R.id.textTitle);
            viewHolder.textQty = (TextView) view.findViewById(R.id.textStatus);


            //   viewHolder.checkBox.setOnCheckedChangeListener(null);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textRetailer.setText(listModel.get(position).getRetailer_name());
        viewHolder.textGift.setText(listModel.get(position).getTitle());
        viewHolder.textCoupon.setText(listModel.get(position).getPoint());
        viewHolder.textQty.setText(listModel.get(position).getQuantity());

        return view;
    }

}

