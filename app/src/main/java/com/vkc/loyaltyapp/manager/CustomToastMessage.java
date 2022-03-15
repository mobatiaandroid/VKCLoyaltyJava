package com.vkc.loyaltyapp.manager;

import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vkc.loyaltyapp.R;

/**
 * Created by user2 on 21/2/18.
 */
public class CustomToastMessage {

    AppCompatActivity mActivity;
    TextView mTextView;
    Toast mToast;
    String message;

    public CustomToastMessage(AppCompatActivity mActivity, String message) {
        this.mActivity = mActivity;
        this.message = message;
        init();

    }

    public void init() {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View layouttoast = inflater.inflate(R.layout.custom_toast, null);
        mTextView = (TextView) layouttoast.findViewById(R.id.texttoast);

        mToast = new Toast(mActivity);
        mToast.setView(layouttoast);

        mTextView.setText(message);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
}