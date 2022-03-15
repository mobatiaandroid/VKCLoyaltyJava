package com.vkc.loyaltyapp.activity.pointhistory.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.pointhistory.model.HistoryModel;

import java.util.List;


public class HistoryAdapter extends BaseAdapter {
	AppCompatActivity mActivity;

	LayoutInflater mLayoutInflater;
	List<HistoryModel> listHistory;

	public HistoryAdapter(AppCompatActivity mActivity, List<HistoryModel> listModel) {

		this.mActivity = mActivity;
		this.listHistory = listModel;
		// this.notifyDataSetChanged();
		// System.out.println("Length" + listModel.size());
		// mLayoutInflater = LayoutInflater.from(mActivity);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listHistory.size();
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
		TextView textType;
		TextView textPoints;
		TextView textToUser;
		TextView textDate;

	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder viewHolder = null;
		View v = view;

		if (view == null) {

			LayoutInflater inflater = (LayoutInflater) mActivity
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			v = inflater.inflate(R.layout.item_history, null);
			viewHolder = new ViewHolder();

		} else {
			viewHolder = (ViewHolder) v.getTag();

		}

		if (position % 2 == 1) {
			// view.setBackgroundColor(Color.BLUE);
			v.setBackgroundColor(mActivity.getResources().getColor(
					R.color.list_row_color_grey));
		} else {
			v.setBackgroundColor(mActivity.getResources().getColor(
					R.color.list_row_color_white));
		}

		viewHolder.textType = (TextView) v.findViewById(R.id.textType);
		viewHolder.textPoints = (TextView) v.findViewById(R.id.textPoints);
		viewHolder.textToUser = (TextView) v.findViewById(R.id.textToUser);
		viewHolder.textDate = (TextView) v.findViewById(R.id.textDate);

		v.setTag(viewHolder);

		viewHolder.textType.setText(listHistory.get(position).getType());
		viewHolder.textPoints.setText(listHistory.get(position).getPoints());
		if (listHistory.get(position).getTo_name().length() > 0) {
			viewHolder.textToUser.setText(listHistory.get(position)
					.getTo_name()
					+ " / "
					+ listHistory.get(position).getTo_role());
		} else {
			viewHolder.textToUser.setText("");
		}
		viewHolder.textDate.setText(listHistory.get(position).getDateValue());

		return v;
	}

}
