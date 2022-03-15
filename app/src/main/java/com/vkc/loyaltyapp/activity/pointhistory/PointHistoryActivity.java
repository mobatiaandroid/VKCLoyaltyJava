package com.vkc.loyaltyapp.activity.pointhistory;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.pointhistory.adapter.TransactionHistoryAdapter;
import com.vkc.loyaltyapp.activity.pointhistory.model.HistoryModel;
import com.vkc.loyaltyapp.activity.pointhistory.model.TransactionModel;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.manager.HeaderManager;
import com.vkc.loyaltyapp.utils.CustomToast;
import com.vkc.loyaltyapp.volleymanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user2 on 9/8/17.
 */
public class PointHistoryActivity extends AppCompatActivity implements View.OnClickListener, VKCUrlConstants {

    Button btnLogin;
    TextView textEarned, textDealerCount, textCredit, textDebit, textEarnedPoint, textTransferred, textBalance;
    AppCompatActivity mContext;

    EditText mEditUserName, mEditPassword;
    String imei_no, historyType = "";
    List<TransactionModel> listHistory;
    ExpandableListView listViewHistory;
    HeaderManager headermanager;
    LinearLayout relativeHeader, llTransaction, llRetailer, llSubDealer;
    ImageView mImageBack;

    private int lastExpandedPosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        mContext = this;
        initUI();

    }


    private void initUI() {
        listHistory = new ArrayList<>();
        listViewHistory = (ExpandableListView) findViewById(R.id.listViewHistory);
        relativeHeader = (LinearLayout) findViewById(R.id.relativeHeader);
        llTransaction = (LinearLayout) findViewById(R.id.llTransactionType);
        llRetailer = (LinearLayout) findViewById(R.id.llRetailer);
        llSubDealer = (LinearLayout) findViewById(R.id.llSubDealer);
        textEarned = (TextView) findViewById(R.id.textEarned);
        textEarnedPoint = (TextView) findViewById(R.id.textEarnedPoints);
        textTransferred = (TextView) findViewById(R.id.textTransferred);
        textBalance = (TextView) findViewById(R.id.textBalance);
        textDealerCount = (TextView) findViewById(R.id.textDealerCount);
        textCredit = (TextView) findViewById(R.id.textCredit);
        textDebit = (TextView) findViewById(R.id.textDebit);
        headermanager = new HeaderManager(PointHistoryActivity.this, getResources().getString(R.string.point_history));
        headermanager.getHeader(relativeHeader, 1);
        mImageBack = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        mImageBack.setOnClickListener(this);
        textDebit.setOnClickListener(this);
        textCredit.setOnClickListener(this);
        listViewHistory.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    listViewHistory.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        if (AppPrefenceManager.getUserType(mContext).equals("7")) {
            llTransaction.setVisibility(View.VISIBLE);
            llRetailer.setVisibility(View.GONE);
            llSubDealer.setVisibility(View.VISIBLE);
            historyType = "CREDIT";
            getHistory();
        } else {
            llTransaction.setVisibility(View.GONE);
            llRetailer.setVisibility(View.VISIBLE);
            llSubDealer.setVisibility(View.GONE);
            historyType = "";
            getHistory();
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        } else if (v == textCredit) {
            historyType = "CREDIT";
            textCredit.setBackgroundResource(R.drawable.rounded_rect_green);
            textDebit.setBackgroundResource(R.drawable.rounded_rect_redline);
            TransactionHistoryAdapter adapter = new TransactionHistoryAdapter(
                    mContext, listHistory);
            listViewHistory.setAdapter(adapter);
            getHistory();
        } else if (v == textDebit) {
            historyType = "DEBIT";

            TransactionHistoryAdapter adapter = new TransactionHistoryAdapter(
                    mContext, listHistory);
            listViewHistory.setAdapter(adapter);
            textCredit.setBackgroundResource(R.drawable.rounded_rect_redline);
            textDebit.setBackgroundResource(R.drawable.rounded_rect_green);
            getHistory();

        }
    }

    public void getHistory() {
        listHistory.clear();
        try {
            String[] name = {"userid", "role", "type"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext),
                    AppPrefenceManager.getUserType(mContext), historyType};

            final VolleyWrapper manager = new VolleyWrapper(GET_POINTS_HISTORY);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject jsonObject = new JSONObject(successResponse);
                                    JSONObject objResponse = jsonObject.optJSONObject("response");
                                    String status = objResponse.optString("status");
                                    String credits = objResponse.optString("total_credits");
                                    textEarned.setText(credits);
                                    textEarnedPoint.setText(objResponse.optString("total_credits"));
                                    textTransferred.setText(objResponse.optString("total_debits"));
                                    textBalance.setText(objResponse.optString("balance_point"));
                                    if (status.equals("Success")) {

                                        JSONArray arrayData = objResponse.optJSONArray("data");
                                        if (arrayData.length() > 0) {

                                            for (int i = 0; i < arrayData.length(); i++) {

                                                TransactionModel model = new TransactionModel();
                                                JSONObject obj = arrayData.optJSONObject(i);
                                                JSONArray arrayDetail = obj.optJSONArray("details");
                        /*JSONArray arrayDetail = new JSONArray(
                                obj.getString("details"));*/
                                                System.out.println("Detail Array " + arrayDetail);
                                                model.setUserName(obj.getString("to_name"));
                                                model.setTotPoints(obj.getString("tot_points"));
                                                ArrayList<HistoryModel> listHist = new ArrayList<>();
                                                for (int j = 0; j < arrayDetail.length(); j++) {
                                                    JSONObject obj1 = arrayDetail.optJSONObject(j);
                                                    HistoryModel model1 = new HistoryModel();
                                                    model1.setPoints(obj1.getString("points"));
                                                    model1.setType(obj1.getString("type"));
                                                    model1.setTo_name(obj1.getString("to_name"));
                                                    model1.setTo_role(obj1.getString("to_role"));
                                                    model1.setDateValue(obj1.getString("date"));

                                                    System.out.println("Date Value " + model1.getDateValue());
                                                    listHist.add(model1);
                                                }

                                                //	System.out.println("Parsed " + listHist.get(i).getDateValue());
                                                model.setListHistory(listHist);


                                                listHistory.add(model);
                                                //     System.out.println("List History " + listHistory.get(0).getListHistory().get(1).getDateValue());
                                            }
                    /*
                     * HistoryAdapter adapter = new
					 * HistoryAdapter(getActivity(), listHistory);
					 * listViewHistory.setAdapter(adapter);
					 */
                                            textDealerCount.setText(String.valueOf(listHistory.size()));
                                            TransactionHistoryAdapter adapter = new TransactionHistoryAdapter(
                                                    mContext, listHistory);
                                            listViewHistory.setAdapter(adapter);

                                        } else {
                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(13);
                                        }

                                    } else {

                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(24);
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
}