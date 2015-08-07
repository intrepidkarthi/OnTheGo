package com.looksphere.goindia.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.looksphere.goindia.activity.SplashActivity;
import com.parse.ParsePushBroadcastReceiver;


/**
 * Created by SPARK on 12/10/14.
 */
public class ParsePushReceiver extends ParsePushBroadcastReceiver {

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.e("Push", "Clicked");
        Intent i = new Intent(context, SplashActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}