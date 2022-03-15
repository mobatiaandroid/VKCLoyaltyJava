package com.vkc.loyaltyapp.volleymanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.appcontroller.AppController;
import com.vkc.loyaltyapp.manager.CustomDialog;
import com.vkc.loyaltyapp.utils.UtilityMethods;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bibin on 18/03/16.
 */
public class VolleyWrapper {
    final String TAG_STRING_RESPONSE_POST = "REQ_STRING_RESPONSE_POST";
    final String TAG_STRING_RESPONSE_GET = "REQ_STRING_RESPONSE_GET";
    final String TAG_JSON_RESPONSE_POST = "REQ_JSON_RESPONSE_POST";
    final String TAG_JSON_RESPONSE_GET = "REQ_JSON_RESPONSE_GET";
    final String TAG_CUSTOM_JSON_RESPONSE_POST = "REQ_JSON_RESPONSE_POST";
    final String TAG_CUSTOM_JSON_RESPONSE_GET = "REQ_JSON_RESPONSE_GET";

    String mRequestUrl;
    ResponseListener mResponseListener;


    public VolleyWrapper(String requestUrl) {
        this.mRequestUrl = requestUrl;
    }


    public void getResponseGET(final Context activity, int type, String[] name,
                               String[] value,
                               ResponseListener responseListener) {
        for (int i = 0; i < value.length; i++) {
            System.out.println("name " + i + " " + name[i]);
            System.out.println("value " + i + " " + value[i]);
        }
        System.out.println("mRequestUrl::" + mRequestUrl);

        if (UtilityMethods.isNetworkConnected(activity)) {
            this.mResponseListener = responseListener;
            if (type == 0)
                getStringResponseGET((AppCompatActivity) activity);
                //getStringResponseGETCustom((Activity) activity);
            else if (type == 1)
                getJsonResponseGet((AppCompatActivity) activity, name, value);
            else if (type == 11)
                getJsonResponseGetCustom((AppCompatActivity) activity, name, value);
        } else {
            ((AppCompatActivity) activity).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CustomDialog customDialog = new CustomDialog
                            (activity, activity.getResources().getString(R.string.no_internet));
                    customDialog.show();
                }
            });
        }

    }

    public void getResponsePOST(final Context activity, int type, String[] name,
                                String[] value, ResponseListener responseListener) {
        for (int i = 0; i < value.length; i++) {
            System.out.println("name " + i + " " + name[i]);
            System.out.println("value from app " + i + " " + value[i]);
        }
        if (UtilityMethods.isNetworkConnected(activity)) {
            this.mResponseListener = responseListener;
            if (type == 0)
                getStringResponsePOST((AppCompatActivity) activity, name, value);
            else if (type == 1)
                getJsonResponsePOST((AppCompatActivity) activity, name, value);
            else if (type == 11)
                getJsonResponsePostCustom((AppCompatActivity) activity, name, value);
            else if (type == 10)
                getJsonResponsePostWithoutContentType((AppCompatActivity) activity, name, value);
        } else {
            ((AppCompatActivity) activity).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CustomDialog customDialog = new CustomDialog
                            (activity, activity.getResources().getString(R.string.no_internet));
                    customDialog.show();
                }
            });
        }
    }


    private void getJsonResponsePostWithoutContentType(final AppCompatActivity activity, final String[] name, final String[] value) {
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setTitle("Loading");
        pDialog.setMessage("Please wait...");
        pDialog.setCanceledOnTouchOutside(false);
        if (!activity.isFinishing()) {
            if (pDialog != null) {
                pDialog.show();
            }
        }
        StringRequest sr = new StringRequest(Request.Method.POST, mRequestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response " + response);
                        mResponseListener.responseSuccess(response.toString());
                        try {
                            if (!activity.isFinishing()) {
                                if (pDialog != null) {
                                    if (pDialog.isShowing()) {
                                        pDialog.dismiss();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ((Activity) activity).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CustomDialog customDialog = new CustomDialog
                                            (activity, activity.getResources().getString(R.string.common_error));
                                    customDialog.show();
                                }
                            });
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error " + error.toString());
                mResponseListener.responseFailure(error.getMessage());
                try {
                    if (!activity.isFinishing()) {
                        if (pDialog != null) {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) activity).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CustomDialog customDialog = new CustomDialog
                                    (activity, activity.getResources().getString(R.string.common_error));
                            customDialog.show();
                        }
                    });
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                for (int i = 0; i < name.length; i++) {
                    params.put(name[i], value[i]);
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Accept", "aplication/json; charset=utf-8");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(sr, TAG_STRING_RESPONSE_POST);
    }


    public void getJsonResponsePostCustom(final AppCompatActivity activity, final String[] name,
                                          final String[] value) {
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setTitle("Loading");
        pDialog.setMessage("Please wait...");
        pDialog.setCanceledOnTouchOutside(false);
        if (!activity.isFinishing()) {
            if (pDialog != null) {
                pDialog.show();
            }
        }
        Map<String, String> params = new HashMap<String, String>();
        for (int i = 0; i < name.length; i++) {
            params.put(name[i], value[i]);
        }
        System.out.println("mRequestUrl" + mRequestUrl);

        CustomVolleyRequest customrequest = new CustomVolleyRequest(Request.Method.POST, mRequestUrl, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mResponseListener.responseSuccess(response.toString());
                        System.out.println("Response " + response);
                        System.out.println("mRequestUrl::" + mRequestUrl);

                        try {
                            if (!activity.isFinishing()) {
                                if (pDialog != null) {
                                    if (pDialog.isShowing()) {
                                        pDialog.dismiss();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ((Activity) activity).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CustomDialog customDialog = new CustomDialog
                                            (activity, activity.getResources().getString(R.string.common_error));
                                    customDialog.show();
                                }
                            });
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error " + error.toString());
                        System.out.println("C::" + mRequestUrl);

                        mResponseListener.responseFailure(error.getMessage());
                        try {
                            if (!activity.isFinishing()) {
                                if (pDialog != null) {
                                    if (pDialog.isShowing()) {
                                        pDialog.dismiss();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ((Activity) activity).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CustomDialog customDialog = new CustomDialog
                                            (activity, activity.getResources().getString(R.string.common_error));
                                    customDialog.show();
                                }
                            });
                        }
                    }
                });


        customrequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customrequest, TAG_CUSTOM_JSON_RESPONSE_POST);


    }

    public void getJsonResponseGetCustom(final AppCompatActivity activity, final String[] name,
                                         final String[] value) {
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setTitle("Loading");
        pDialog.setMessage("Please wait...");
        pDialog.setCanceledOnTouchOutside(false);
        if (!activity.isFinishing()) {
            if (pDialog != null) {
                pDialog.show();
            }
        }
        StringBuffer modifiedurlWithParams = new StringBuffer(mRequestUrl);

        for (int index = 0; index < name.length; index++) {
            if (index == 0) {
                modifiedurlWithParams.append("?");
            } else {
                modifiedurlWithParams.append("&");
            }
            modifiedurlWithParams.append(name[index]);
            modifiedurlWithParams.append("=");
            modifiedurlWithParams.append(value[index]);
        }

        CustomVolleyRequest customrequest = new CustomVolleyRequest(modifiedurlWithParams.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response " + response);
                        mResponseListener.responseSuccess(response.toString());
                        try {
                            if (!activity.isFinishing()) {
                                if (pDialog != null) {
                                    if (pDialog.isShowing()) {
                                        pDialog.dismiss();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ((Activity) activity).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CustomDialog customDialog = new CustomDialog
                                            (activity, activity.getResources().getString(R.string.common_error));
                                    customDialog.show();
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error " + error.toString());
                mResponseListener.responseFailure(error.getMessage());
                try {
                    if (!activity.isFinishing()) {
                        if (pDialog != null) {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) activity).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CustomDialog customDialog = new CustomDialog
                                    (activity, activity.getResources().getString(R.string.common_error));
                            customDialog.show();
                        }
                    });
                }
            }
        }
        );
        customrequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customrequest, TAG_CUSTOM_JSON_RESPONSE_GET);
    }


    public void getJsonResponsePOST(final AppCompatActivity activity, final String[] name,
                                    final String[] value) {


        final ProgressDialog pDialog = new ProgressDialog(activity);

        if (!activity.isFinishing()) {
            if (pDialog != null) {
                pDialog.show();
            }
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, mRequestUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response " + response);
                        mResponseListener.responseSuccess(response.toString());
                        try {
                            if (!activity.isFinishing()) {
                                if (pDialog != null) {
                                    if (pDialog.isShowing()) {
                                        pDialog.dismiss();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ((Activity) activity).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CustomDialog customDialog = new CustomDialog
                                            (activity, activity.getResources().getString(R.string.common_error));
                                    customDialog.show();
                                }
                            });
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error " + error.toString());
                mResponseListener.responseFailure(error.getMessage());
                try {
                    if (!activity.isFinishing()) {
                        if (pDialog != null) {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) activity).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CustomDialog customDialog = new CustomDialog
                                    (activity, activity.getResources().getString(R.string.common_error));
                            customDialog.show();
                        }
                    });
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.e("TAG HEAD PARAMTER ", "HEADER PARAMS IS CALLED ");
                Map<String, String> params = new HashMap<String, String>();
                for (int i = 0; i < name.length; i++) {
                    System.out.println("NAME " + name[i] + "VALUE " + value[i]);
                    params.put(name[i], value[i]);
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.e("TAG HEAD PARAMTER ", "HEADER IS CALLED ");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "aplication/json; charset=utf-8");

                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, TAG_JSON_RESPONSE_POST);

    }

    public void getJsonResponseGet(final AppCompatActivity activity, final String[] name, final String[] value) {
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setTitle("Loading");
        pDialog.setMessage("Please wait...");
        if (!activity.isFinishing()) {
            if (pDialog != null) {
                pDialog.show();
            }
        }
        StringBuffer modifiedurlWithParams = new StringBuffer(mRequestUrl);

        for (int index = 0; index < name.length; index++) {
            if (index == 0)
                modifiedurlWithParams.append("?");
            modifiedurlWithParams.append(name[index]);
            modifiedurlWithParams.append(value[index]);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, modifiedurlWithParams.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response " + response);
                        mResponseListener.responseSuccess(response.toString());
                        try {
                            if (!activity.isFinishing()) {
                                if (pDialog != null) {
                                    if (pDialog.isShowing()) {
                                        pDialog.dismiss();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ((Activity) activity).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CustomDialog customDialog = new CustomDialog
                                            (activity, activity.getResources().getString(R.string.common_error));
                                    customDialog.show();
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error " + error.toString());
                mResponseListener.responseFailure(error.getMessage());
                try {
                    if (!activity.isFinishing()) {
                        if (pDialog != null) {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) activity).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CustomDialog customDialog = new CustomDialog
                                    (activity, activity.getResources().getString(R.string.common_error));
                            customDialog.show();
                        }
                    });
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.e("TAG HEAD PARAMTER ", "HEADER IS CALLED ");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "aplication/json; charset=utf-8");

                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, TAG_JSON_RESPONSE_GET);

    }


    public String getResponseCache() {
        Cache cache = AppController.getInstance().getRequestQueue()
                .getCache();
        Cache.Entry entry = cache.get(mRequestUrl);
        String data = "";
        if (entry != null) {
            try {
                data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // Cached response doesn't exists. Make network call here
        }
        return data;
    }


    public void getStringResponsePOST(final AppCompatActivity activity, final String[] name,
                                      final String[] value) {
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setTitle("Loading");
        pDialog.setMessage("Please wait...");
        pDialog.setCanceledOnTouchOutside(false);
        if (!activity.isFinishing()) {
            if (pDialog != null) {
                pDialog.show();
            }
        }

        StringRequest sr = new StringRequest(Request.Method.POST, mRequestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mResponseListener.responseSuccess(response.toString());
                        System.out.println("Response " + response);
                        try {
                            if (!activity.isFinishing()) {
                                if (pDialog != null) {
                                    if (pDialog.isShowing()) {
                                        pDialog.dismiss();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ((Activity) activity).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CustomDialog customDialog = new CustomDialog
                                            (activity, activity.getResources().getString(R.string.common_error));
                                    customDialog.show();
                                }
                            });
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error " + error.toString());
                mResponseListener.responseFailure(error.getMessage());
                try {
                    if (!activity.isFinishing()) {
                        if (pDialog != null) {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) activity).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CustomDialog customDialog = new CustomDialog
                                    (activity, activity.getResources().getString(R.string.common_error));
                            customDialog.show();
                        }
                    });
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                for (int i = 0; i < name.length; i++) {
                    params.put(name[i], value[i]);
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(sr, TAG_STRING_RESPONSE_POST);
    }

    public void getStringResponseGET(final AppCompatActivity activity) {
        final ProgressDialog pDialog = new ProgressDialog(activity);
        if (!activity.isFinishing()) {
            if (pDialog != null) {
                pDialog.show();
            }
        }

        StringRequest strReq = new StringRequest(Request.Method.GET, mRequestUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response " + response);
                        mResponseListener.responseSuccess(response.toString());
                        try {
                            if (!activity.isFinishing()) {
                                if (pDialog != null) {
                                    if (pDialog.isShowing()) {
                                        pDialog.dismiss();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ((Activity) activity).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CustomDialog customDialog = new CustomDialog
                                            (activity, activity.getResources().getString(R.string.common_error));
                                    customDialog.show();
                                }
                            });
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error " + error.toString());
                mResponseListener.responseFailure(error.getMessage());
                try {
                    if (!activity.isFinishing()) {
                        if (pDialog != null) {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) activity).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CustomDialog customDialog = new CustomDialog
                                    (activity, activity.getResources().getString(R.string.common_error));
                            customDialog.show();
                        }
                    });
                }
            }
        });
        // set Volley cache enable true
        strReq.setShouldCache(true);
        strReq.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq,
                TAG_STRING_RESPONSE_GET);
    }

    public void getStringResponseGETCustom(final AppCompatActivity activity) {
        final ProgressDialog pDialog = new ProgressDialog(activity);
        if (!activity.isFinishing()) {
            if (pDialog != null) {
                pDialog.show();
            }
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(mRequestUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response " + response);
                        mResponseListener.responseSuccess(response.toString());

                        try {
                            if (!activity.isFinishing()) {
                                System.out.println("Response " + response);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ((Activity) activity).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CustomDialog customDialog = new CustomDialog
                                            (activity, activity.getResources().getString(R.string.common_error));
                                    customDialog.show();
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error " + error.toString());
                mResponseListener.responseFailure(error.getMessage());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.e("TAG HEAD PARAMTER ", "HEADER IS CALLED ");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "aplication/json; charset=utf-8");

                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, TAG_JSON_RESPONSE_GET);
//        adsas
//        JsonObjectRequest strReq = new JsonObjectRequest(mRequestUrl,new Response.Listener<>)asd


//        JsonObjectRequest strReq = new JsonObjectRequest(mRequestUrl,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println("Response " + response);
//                        mResponseListener.responseSuccess(response.toString());
//                        try {
//                            if (!activity.isFinishing()) {
//                                if (pDialog != null) {
//                                    if (pDialog.isShowing()) {
//                                        pDialog.dismiss();
//                                    }
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            ((Activity) activity).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    CustomDialog customDialog = new CustomDialog
//                                            (activity, activity.getResources().getString(R.string.error_heading),activity.getResources().getString(R.string.common_error));
//                                    customDialog.show();
//                                }
//                            });
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println("Error " + error.toString());
//                mResponseListener.responseFailure(error.getMessage());
//                try {
//                    if (!activity.isFinishing()) {
//                        if (pDialog != null) {
//                            if (pDialog.isShowing()) {
//                                pDialog.dismiss();
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    ((Activity) activity).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            CustomDialog customDialog = new CustomDialog
//                                    (activity, activity.getResources().getString(R.string.error_heading),activity.getResources().getString(R.string.common_error));
//                            customDialog.show();
//                        }
//                    });
//                }
//            }
//        });
        // set Volley cache enable true
//        strReq.setShouldCache(true);
//        strReq.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(strReq,
//                TAG_STRING_RESPONSE_GET);
    }


    public interface ResponseListener {
        public void responseSuccess(String successResponse);

        public void responseFailure(String failureResponse);

    }
}

