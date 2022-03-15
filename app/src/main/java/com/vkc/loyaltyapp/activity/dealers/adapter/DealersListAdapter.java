package com.vkc.loyaltyapp.activity.dealers.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.dealers.model.DealerModel;
import com.vkc.loyaltyapp.appcontroller.AppController;

import java.util.ArrayList;

/**
 * Created by user2 on 8/8/17.
 */
public class DealersListAdapter extends BaseAdapter {
    AppCompatActivity mActivity;

    LayoutInflater mLayoutInflater;
    ArrayList<DealerModel> listModel;
    private ArrayList<DealerModel> mOriginalValues; // Original Values
    private ArrayList<DealerModel> mDisplayedValues;

    public DealersListAdapter(AppCompatActivity mActivity,
                              ArrayList<DealerModel> listModel) {

        this.mActivity = mActivity;
        AppController.listDealers = listModel;


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return AppController.listDealers.size();
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

        TextView textName;
        CheckBox checkBox;

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
                    .inflate(R.layout.item_dealer_list, null);
            viewHolder = new ViewHolder();
            viewHolder.textName = (TextView) view.findViewById(R.id.textViewName);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox_dealer);

            viewHolder.checkBox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {

                         /*   String idValue = AppController.dealersModels.get(
                                    position).getId();*/

                            int getPosition = (Integer) buttonView.getTag();
                            AppController.listDealers.get(getPosition)
                                    .setIsChecked(isChecked);

                        }
                    });
            //   viewHolder.checkBox.setOnCheckedChangeListener(null);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.checkBox.setTag(position);
        viewHolder.textName.setText(AppController.listDealers.get(position)
                .getName());


        viewHolder.checkBox.setChecked(AppController.listDealers.get(
                position).isChecked());

        return view;
    }

}
