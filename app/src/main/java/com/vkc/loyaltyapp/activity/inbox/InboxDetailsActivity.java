package com.vkc.loyaltyapp.activity.inbox;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.appcontroller.AppController;
import com.vkc.loyaltyapp.constants.VKCUrlConstants;
import com.vkc.loyaltyapp.manager.HeaderManager;

/**
 * Created by user2 on 4/12/17.
 */
public class InboxDetailsActivity extends AppCompatActivity implements View.OnClickListener, VKCUrlConstants {

    AppCompatActivity mContext;
    HeaderManager headermanager;
    LinearLayout relativeHeader;
    ImageView mImageBack, imageNotification;
    String title, message, created_on, image, date_from, date_to;
    TextView textTitle,textPinch;
    WebView webViewMessage,webViewImage;
int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_details);
        mContext = this;
        Intent intent = getIntent();
        position= intent.getExtras().getInt("position");
        initUI();


    }


    private void initUI() {
        relativeHeader = (LinearLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(InboxDetailsActivity.this, getResources().getString(R.string.inbox));
        headermanager.getHeader(relativeHeader, 1);
        mImageBack = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.left,
                R.drawable.back);
        textTitle = (TextView) findViewById(R.id.textTitle);
        textPinch=(TextView) findViewById(R.id.textPinch);
        imageNotification = (ImageView) findViewById(R.id.imageNotification);
        webViewMessage = (WebView) findViewById(R.id.webMessage);
        webViewImage = (WebView) findViewById(R.id.webImage);
        message= AppController.listNotification.get(position).getMessage();
        image=AppController.listNotification.get(position).getImage();
        webViewImage.getSettings().setBuiltInZoomControls(true);
        webViewImage.getSettings().setDisplayZoomControls(false);
        title=AppController.listNotification.get(position).getTitle();
        if (image.trim().length() > 0) {
           webViewImage.setVisibility(View.VISIBLE);
            textPinch.setVisibility(View.VISIBLE);
           // imageNotification.setVisibility(View.VISIBLE);
            //image = image.replaceAll(" ", "%20");

          //  Picasso.with(mContext).load(image).into(imageNotification);

            String summary = "<html><body style=\"color:white;\">" + "<center><img  src='" + image + "'width='100%', height='auto'\"></center>" + "</body></html>";
            webViewImage.setBackgroundColor(Color.TRANSPARENT);
            webViewImage.loadData(summary, "text/html", null);
        } else {
           // imageNotification.setVisibility(View.GONE);
            webViewImage.setVisibility(View.GONE);
            textPinch.setVisibility(View.GONE);
        }
        mImageBack.setOnClickListener(this);
        textTitle.setText(title);
        String summary = "<html><body style=\"color:white;\">" + message + "</body></html>";
        webViewMessage.setBackgroundColor(Color.TRANSPARENT);
        webViewMessage.loadData(summary, "text/html", null);

    }
    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        }
    }

}