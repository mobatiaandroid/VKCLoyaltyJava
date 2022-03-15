package com.vkc.loyaltyapp.activity.pointhistory.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.pointhistory.model.HistoryModel;
import com.vkc.loyaltyapp.activity.pointhistory.model.TransactionModel;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryAdapter extends BaseExpandableListAdapter
        implements VKCUrlConstants

{
    List<TransactionModel> listTransaction;
    ArrayList<HistoryModel> productList;
    AppCompatActivity mContext;
    int positionValue;

    public TransactionHistoryAdapter(AppCompatActivity mContext,
                                     List<TransactionModel> listTransaction) {
        this.listTransaction = listTransaction;
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return listTransaction.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        productList = listTransaction.get(groupPosition).getListHistory();
        //return listTransaction.get(positionValue).getListHistory().size();
        return productList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listTransaction.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return productList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_history_parent,
                    null);
        }

        TextView textUser = (TextView) convertView.findViewById(R.id.textUser);
        TextView textPoint = (TextView) convertView.findViewById(R.id.textPoint);

        TextView textIcon = (TextView) convertView.findViewById(R.id.textIcon);
        textUser.setText(listTransaction.get(groupPosition).getUserName());
        textPoint.setText(listTransaction.get(groupPosition).getTotPoints()+" Coupons");
        positionValue = groupPosition;
        if (isExpanded) {
            textIcon.setText("-");
        } else {
            textIcon.setText("+");
        }
        return convertView;
    }

    static class ViewHolder {
        TextView textType;
        TextView textPoints;
        TextView textToUser;
        TextView textDate;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        /*System.out.println("Group Position:"+groupPosition+"Child Psoitiomn:"+childPosition);
        System.out.println("Count:"+listTransaction.get(groupPosition).getListHistory().size());*/


        ViewHolder viewHolder = null;
        View v = convertView;

        if (v == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(R.layout.item_history, null);
            viewHolder = new ViewHolder();


            v.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) v.getTag();
        viewHolder.textType = (TextView) v.findViewById(R.id.textType);
        viewHolder.textPoints = (TextView) v.findViewById(R.id.textPoints);
        viewHolder.textToUser = (TextView) v.findViewById(R.id.textToUser);
        viewHolder.textDate = (TextView) v.findViewById(R.id.textDate);


        //childPosition=childPosition-1;
        if (childPosition % 2 == 1) {
            // view.setBackgroundColor(Color.BLUE);
            v.setBackgroundColor(mContext.getResources().getColor(
                    R.color.list_row_color_grey));
        } else {
            v.setBackgroundColor(mContext.getResources().getColor(
                    R.color.list_row_color_white));
        }
        //	String date=productList.get(childPosition).getDateValue();

        viewHolder.textType.setText(productList.get(childPosition).getType());
        viewHolder.textPoints.setText(productList.get(childPosition).getPoints());
        if (productList.get(childPosition).getTo_name().length() > 0) {
            viewHolder.textToUser.setText(productList.get(childPosition)
                    .getTo_name()
                    + " / "
                    + productList.get(childPosition).getTo_role());
        } else {
            viewHolder.textToUser.setText("");
        }
        //System.out.println("Position Value:"+childPosition);
        viewHolder.textDate.setText(productList.get(childPosition).getDateValue());

        System.out.println("Date " + productList.get(childPosition).getDateValue());
        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}