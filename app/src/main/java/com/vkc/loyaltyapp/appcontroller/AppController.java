package com.vkc.loyaltyapp.appcontroller;

import androidx.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.vkc.loyaltyapp.activity.dealers.model.DealerModel;
import com.vkc.loyaltyapp.activity.inbox.model.InboxModel;
import com.vkc.loyaltyapp.activity.shopimage.model.ImageListModel;
import com.vkc.loyaltyapp.activity.subdealerredeem.model.RetailerModel;
import com.vkc.loyaltyapp.utils.ConnectivityReceiver;
import com.vkc.loyaltyapp.utils.LruBitmapCache;

import java.util.ArrayList;

/**
 * Created by Bibin Johnson
 */
public class AppController extends MultiDexApplication {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    public static ArrayList<InboxModel> listNotification=new ArrayList<>();
    private static AppController mInstance;
    public static ArrayList<DealerModel> listDealers = new ArrayList<>();
    public static ArrayList<RetailerModel> listRetailers = new ArrayList<>();
    public static ArrayList<ImageListModel> imageList= new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }




}

