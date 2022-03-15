package com.vkc.loyaltyapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;

import com.vkc.loyaltyapp.activity.common.SignUpActivity;
import com.vkc.loyaltyapp.activity.common.SignUpActivity;

/**
 * Created by user2 on 14/8/17.
 */
public class IncomingSms extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj .length; i++)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber ;
                    String message = currentMessage .getDisplayMessageBody();
                    try
                    {
                        if (senderNum .equals("TA-VKC"))
                        {
                            SignUpActivity Sms = new SignUpActivity();
                            Sms.recivedSms(message);
                        }
                    }
                    catch(Exception e){}

                }
            }

        } catch (Exception e)
        {

        }
    }

}