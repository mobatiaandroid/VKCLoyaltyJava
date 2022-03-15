package com.vkc.loyaltyapp.activity.dealers;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.HomeActivity;
import com.vkc.loyaltyapp.activity.dealers.adapter.DealersListAdapter;
import com.vkc.loyaltyapp.activity.dealers.model.DealerModel;
import com.vkc.loyaltyapp.appcontroller.AppController;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.manager.HeaderManager;
import com.vkc.loyaltyapp.utils.CustomToast;
import com.vkc.loyaltyapp.volleymanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by user2 on 7/8/17.
 */
public class DealersActivity extends AppCompatActivity implements VKCUrlConstants, View.OnClickListener {
    AppCompatActivity mContext;
    ArrayList<DealerModel> listDealers;
    ListView listViewDealer;
    TextView textSubmit;
    // ArrayList<String> listIds;
    HeaderManager headermanager;
    LinearLayout relativeHeader;
    ImageView mImageBack;
    EditText editSearch;
    ArrayList<DealerModel> tempDealer;
    DealersListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealers_list);
        mContext = this;
        ininUI();
        getDealers("");
    }

    private void ininUI() {
        listDealers = new ArrayList<>();
        tempDealer = new ArrayList<>();

        listViewDealer = (ListView) findViewById(R.id.listViewDealer);
        // mImageSearch = (ImageView) findViewById(R.id.imageSearch);
        textSubmit = (TextView) findViewById(R.id.textSubmit);
        editSearch = (EditText) findViewById(R.id.editSearch);
        relativeHeader = (LinearLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(DealersActivity.this, getResources().getString(R.string.dealers));
        headermanager.getHeader(relativeHeader, 1);
        mImageBack = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        mImageBack.setOnClickListener(this);
        textSubmit.setOnClickListener(this);
        // mImageSearch.setOnClickListener(this);
        editSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            /*if (editSearch.getText().toString().trim().length() > 0) {
                                getDealers(editSearch.getText().toString().trim());
                            } else {
                                getDealers("");
                            }*/
/*
                            for (int i = 0; i < listDealers.size(); i++) {
                                if (listDealers.get(i).getName().contains(editSearch.getText().toString().trim())) {
                                    listViewDealer.setSelection(i);
                                    break;
                                }
                            }*/


                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    listViewDealer.setSelection(0);
                } else {
                    for (int i = 0; i < listDealers.size(); i++) {

                        if (listDealers.get(i).getName().toLowerCase().startsWith(s.toString().toLowerCase())) {
                            listViewDealer.setSelection(i);
                            break;
                        }


                    }
                }
                /*else {

                    for (int i = 0; i < listDealers.size(); i++) {
                        if (listDealers.get(i).getName().contains(s)) {
                            listViewDealer.setSelection(i);
                            break;
                        }
                    }
                }*/
                /*if (s.length() == 0) {
                    tempDealer = listDealers;
                    adapter = new DealersListAdapter(mContext, tempDealer);
                    adapter.notifyDataSetChanged();
                    listViewDealer.setAdapter(adapter);
                } else {
                    for (int i = 0; i < listDealers.size(); i++) {
                        if (listDealers.get(i).getName().contains(editSearch.getText().toString().trim())) {
                            tempDealer.add(listDealers.get(i));
                        }
                    }
                    adapter = new DealersListAdapter(mContext, tempDealer);
                    adapter.notifyDataSetChanged();
                    listViewDealer.setAdapter(adapter);
                }*/

               /* if (s.length() == 0) {
                    *//*tempDealer = listDealers;
                    adapter = new DealersListAdapter(mContext, tempDealer);
                    adapter.notifyDataSetChanged();
                    listViewDealer.setAdapter(adapter);*//*
                    listViewDealer.setSelection(0);
                } else {
                    for (int i = 0; i < listDealers.size(); i++) {
                        if (listDealers.get(i).getName().contains(editSearch.getText().toString().trim())) {
                            listViewDealer.setSelection(i);
                            break;
                        }
                    }
                }*/
            }
        });
    }

    public void getDealers(String searchKey) {
        try {
            AppController.listDealers.clear();
            listDealers.clear();
            // listIds.clear();
            String[] name = {"cust_id", "role", "search_key"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext), searchKey};

            final VolleyWrapper manager = new VolleyWrapper(GET_DEALERS);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);

                            ArrayList<DealerModel> dealersNotAssigned = new ArrayList<DealerModel>();
                            ArrayList<DealerModel> dealersAssigned = new ArrayList<DealerModel>();
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = objResponse.optString("status");
                                    if (status.equalsIgnoreCase("Success")) {
                                        JSONArray dataArray = objResponse.optJSONArray("data");
                                        if (dataArray.length() > 0) {
                                            dealersNotAssigned.clear();
                                            dealersAssigned.clear();
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject obj = dataArray.optJSONObject(i);
                                                DealerModel model = new DealerModel();
                                                model.setId(obj.optString("id"));
                                                model.setName(obj.optString("name"));
                                                model.setRole(obj.optString("role"));

                                                if (obj.optString("is_assigned").equals("0")) {
                                                    model.setIsChecked(false);
                                                    dealersNotAssigned.add(model);
                                                } else {
                                                    model.setIsChecked(true);
                                                    dealersAssigned.add(model);
                                                }

                                            }

                                            listDealers.addAll(dealersAssigned);
                                            listDealers.addAll(dealersNotAssigned);
                                            //Sort Dealers List A - Z
                                           /* Collections.sort(listDealers, new CustomComparator() {
                                                @Override
                                                public int compare(DealerModel o1, DealerModel o2) {
                                                    return o1.getName().trim().compareToIgnoreCase(o2.getName().trim());
                                                }
                                            });*/

                                            adapter = new DealersListAdapter(mContext, listDealers);
                                            //adapter.notifyDataSetChanged();
                                            listViewDealer.setAdapter(adapter);

                                        } else {

                                        }
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

    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        }
        switch (v.getId()) {
            case R.id.textSubmit:
                //listIds.clear();
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < AppController.listDealers.size(); i++) {
                    if (AppController.listDealers.get(i).isChecked()) {


                        // listIds.add(AppController.listDealers.get(i).getId())


                        JSONObject object = new JSONObject();
                        try {

                            object.put("id", AppController.listDealers.get(i)
                                    .getId());
                            object.put("role", AppController.listDealers.get(i)
                                    .getRole());

                            // object.putOpt("grid_value",cartArrayList.get(i).getProdGridValue());
                            jsonArray.put(object);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block

                        }


                    }


                }
                try {
                    jsonObject.put("dealers", jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonArray.length() > 0) {
                    if (jsonArray.length() > 10) {
                        CustomToast toast = new CustomToast(mContext);
                        toast.show(11);
                    } else {
                        submitDealers(jsonArray);
                    }

                } else {
                    CustomToast toast = new CustomToast(mContext);
                    toast.show(10);
                }
                break;
        }
    }

    public void submitDealers(JSONArray objArray) {
        try {


            String[] name = {"cust_id", "role", "dealer_id"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext), objArray.toString()};
            final VolleyWrapper manager = new VolleyWrapper(ASSIGN_DEALERS);
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
                                        AppPrefenceManager.saveLoginStatusFlag(mContext, "yes");
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(12);
                                        startActivity(new Intent(DealersActivity.this, HomeActivity.class));
                                        finish();

                                    } else {
                                        AppPrefenceManager.saveLoginStatusFlag(mContext, "no");
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

    public class CustomComparator implements Comparator<DealerModel> {
        @Override
        public int compare(DealerModel o1, DealerModel o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
