package com.vkc.loyaltyapp.activity.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.VideoView;

import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.HomeActivity;
import com.vkc.loyaltyapp.activity.dealers.DealersActivity;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;

public class SplashActivity extends AppCompatActivity {

    //VideoView videoView;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
       /* videoView = (VideoView) findViewById(R.id.videoView);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);
        videoView.setVideoURI(video);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                startNextActivity();
            }
        });

        videoView.start();*/
        loadSplash();
    }

    private void startNextActivity() {
        if (isFinishing())
            return;
        if (AppPrefenceManager.getLoginStatusFlag(mContext).equals("yes")) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else if (AppPrefenceManager.getIsVerifiedOTP(mContext).equals("yes")) {
            if (AppPrefenceManager.getDealerCount(mContext) > 0) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                startActivity(new Intent(this, DealersActivity.class));
                finish();
            }
        } else {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        }
    }

    private void loadSplash() {
        CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                if (AppPrefenceManager.getAgreeTerms(mContext)) {
                    startNextActivity();
                } else {
                    startActivity(new Intent(SplashActivity.this, TermsandConditionActivity.class));
                    finish();
                }

                //
            }
        };
        countDownTimer.start();
    }

}
