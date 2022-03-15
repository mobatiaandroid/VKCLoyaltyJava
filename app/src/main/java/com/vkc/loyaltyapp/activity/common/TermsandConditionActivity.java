package com.vkc.loyaltyapp.activity.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.HomeActivity;
import com.vkc.loyaltyapp.activity.dealers.DealersActivity;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;
import com.vkc.loyaltyapp.utils.CustomToast;

/**
 * Created by user2 on 12/10/17.
 */
public class TermsandConditionActivity extends AppCompatActivity {

    WebView webView;
    Context mContext;
    CheckBox checkTerms;
    Button buttonNext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        mContext = this;

        webView = (WebView) findViewById(R.id.webTerms);
        buttonNext=(Button)findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppPrefenceManager.getAgreeTerms(TermsandConditionActivity.this)) {
                    if (AppPrefenceManager.getLoginStatusFlag(mContext).equals("yes")) {
                        startActivity(new Intent(TermsandConditionActivity.this, HomeActivity.class));
                        finish();
                    } else if (AppPrefenceManager.getIsVerifiedOTP(mContext).equals("yes")) {
                        if (AppPrefenceManager.getDealerCount(mContext) > 0) {
                            startActivity(new Intent(TermsandConditionActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(TermsandConditionActivity.this, DealersActivity.class));
                            finish();
                        }
                    } else {
                        startActivity(new Intent(TermsandConditionActivity.this, SignUpActivity.class));
                        finish();
                    }
                } else {

                    CustomToast toast = new CustomToast(TermsandConditionActivity.this);
                    toast.show(37);

                }
            }
        });
        checkTerms = (CheckBox) findViewById(R.id.checkboxTerms);
        checkTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //  Log.i("Checked ", " Yes");
                    AppPrefenceManager.saveAgreeTerms(mContext, true);
                } else {
                    AppPrefenceManager.saveAgreeTerms(mContext, false);
                    //   Log.i("Checked ", " No");
                }
            }
        });
        webView.loadUrl("file:///android_asset/terms.html");
       /* videoView = (VideoView) findViewById(R.id.videoView);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);
        videoView.setVideoURI(video);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                startNextActivity();
            }
        });

        videoView.start();*/

    }


}
