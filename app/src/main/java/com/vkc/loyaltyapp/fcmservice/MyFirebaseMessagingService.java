package com.vkc.loyaltyapp.fcmservice;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.HomeActivity;
import com.vkc.loyaltyapp.activity.news.NewsActivity;
import com.vkc.loyaltyapp.manager.AppPrefenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import androidx.core.app.NotificationCompat;

/**
 * Created by Bibin Johnson
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Intent intent;
    Bitmap bitmap;
    String mType;
    public static final String FCM_PARAM = "picture";
    private static final String CHANNEL_NAME = "FCM";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //   Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            // Log.d(TAG, "Message data payload: " + remoteMessage.getData().toString());
//            String questionTitle = data.get("questionTitle").toString();
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString().replaceAll("=", ":"));

                handleDataMessage(json);


            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        }

        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
////            sendNotification(remoteMessage.getNotification().getBody());
//
//            sendNotification(remoteMessage.getNotification().getBody());
//            // Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }

    }

    @TargetApi(26)
    private void sendNotification(String messageBody,String news_id) {

        Random Number = new Random();
        int Rnumber = Number.nextInt(100);
        // if (mType.equals("Text")) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        if (news_id.equals("")) {
            intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        } else {
            intent = new Intent(this, NewsActivity.class);
            intent.putExtra("news_id", news_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notifi_vkc)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap))
                // .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.notifi_vkc);
        if (Build.VERSION.SDK_INT >= 23) {
//            notificationBuilder.setSmallIcon(R.drawable.notifyicons);
            notificationBuilder.setColor(getResources().getColor(R.color.split_bg));
            //    notificationBuilder.setSmallIcon(R.drawable.not_large);
            notificationBuilder.setLargeIcon(largeIcon);

        } else {
            notificationBuilder.setSmallIcon(R.drawable.notifi_vkc);
//            notificationBuilder.setSmallIcon(R.drawable.notifyicons);
//            notificationBuilder.setColor(getResources().getColor(R.color.tictapHeader));

        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = getString(R.string.app_name) + "_01";// The id of the channel.
            CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationBuilder.setChannelId(mChannel.getId());
            mChannel.setShowBadge(true);
            mChannel.canShowBadge();
            mChannel.enableLights(true);
            mChannel.setLightColor(getResources().getColor(R.color.colorPrimaryDark));
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);

        }
        notificationManager.notify(Rnumber, notificationBuilder.build());

    }

    private void handleDataMessage(JSONObject json) {
        //   Log.e(TAG, "push json: " + json.toString());
        String news_id = "";
        try {
            JSONObject data = json.getJSONObject("body");
            String message = data.optString("message");
            news_id = data.optString("newsid");
            String title = data.optString("title");
            String image = data.optString("image");
            if (image.length() > 0) {
                bitmap = getBitmapfromUrl(image);
            }
            sendNotification(message, news_id);
           // sendNotification(message);


        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        AppPrefenceManager.saveToken(getApplicationContext(),s);
    }
/*  public void getNotificationType(JSONObject json) {
        try {
            mType = json.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }*/


}

/*image push
{"data":{"message":"test","type":"Image","URL":"http:\/\/localhost\/testmobi\/media\/uploads\/file_59636679c2d33.jpeg"}}
{"data":{"message":"jkdsjk","type":"Video","URL":"https:\/\/www.youtube.com\/embed\/dUgra62yOBw"}}
*/