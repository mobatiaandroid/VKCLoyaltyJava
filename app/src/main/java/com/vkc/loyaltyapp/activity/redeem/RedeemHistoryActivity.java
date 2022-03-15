package com.vkc.loyaltyapp.activity.redeem;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.redeem.adapter.RedeemAdapter;
import com.vkc.loyaltyapp.activity.redeem.model.RedeemModel;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.utils.CustomToast;
import com.vkc.loyaltyapp.volleymanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RedeemHistoryActivity extends AppCompatActivity implements VKCUrlConstants {
    AppCompatActivity mContext;
    ArrayList<RedeemModel> listGifts;
    ListView listViewRedeem;
    ImageView btn_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_history);
        mContext = this;
        initUI();
    }

    private void initUI() {
        // get the reference of RecyclerView
        listGifts = new ArrayList<>();
        listViewRedeem = (ListView) findViewById(R.id.listViewRedeem);
        btn_left = (ImageView) findViewById(R.id.btn_left);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listViewRedeem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogData dialog = new DialogData(mContext, position);
                dialog.show();

            }
        });
        getHistory();
    }

    public void getHistory() {
        listGifts.clear();
        try {
            String[] name = {"cust_id"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(REDEEM_LIST_URL);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    JSONObject objResponse = rootObject.optJSONObject("response");


                                    String status = objResponse.optString("status");

                                    if (status.equalsIgnoreCase("Success")) {
                                        JSONArray dataArray = objResponse.optJSONArray("data");
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject obj = dataArray.optJSONObject(i);
                                                RedeemModel model = new RedeemModel();
                                                model.setImage(obj.optString("image"));
                                                model.setPoint(obj.optString("point"));
                                                model.setTitle(obj.optString("title"));
                                                model.setDate(obj.optString("date"));
                                                model.setQuantity(obj.optString("quantity"));
                                                model.setStatus(obj.optString("status"));
                                                model.setDealer(obj.optString("dealer_name"));
                                                model.setGift_type(obj.optString("gift_type"));
                                                listGifts.add(model);
                                            }
                                            RedeemAdapter adapter = new RedeemAdapter(mContext, listGifts);
                                            listViewRedeem.setAdapter(adapter);
                                        } else {
                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(5);
                                        }


                                    } else {
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(4);
                                        // Toast.makeText(mContext, getResources().getString(R.string.invalid_user), Toast.LENGTH_SHORT).show();
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


    public class DialogData extends Dialog {

        public AppCompatActivity mActivity;
        String type, message;
        int position;

        public DialogData(AppCompatActivity a, int position) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;
            this.position = position;

        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_history);
            init();

        }

        private void init() {

            // Button buttonSet = (Button) findViewById(R.id.buttonOk);
            TextView txt_date = (TextView) findViewById(R.id.txt_date);
            TextView txt_type = (TextView) findViewById(R.id.txt_type);
            TextView txt_name = (TextView) findViewById(R.id.txt_name);
            TextView text_point = (TextView) findViewById(R.id.text_point);
            TextView text_quantity = (TextView) findViewById(R.id.text_quantity);
            TextView text_status = (TextView) findViewById(R.id.text_status);
            TextView text_dealer = (TextView) findViewById(R.id.text_dealer);

            Button buttonCancel = (Button) findViewById(R.id.btn_ok);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            txt_date.setText(": " + listGifts.get(position).getDate());
            txt_type.setText(": " + listGifts.get(position).getGift_type());
            txt_name.setText(": " + listGifts.get(position).getTitle());
            text_point.setText(": " + listGifts.get(position).getPoint());
            text_quantity.setText(": " + listGifts.get(position).getQuantity());
            text_status.setText(": " + listGifts.get(position).getStatus());
            text_dealer.setText(": " + listGifts.get(position).getDealer());


        }


    }
}

