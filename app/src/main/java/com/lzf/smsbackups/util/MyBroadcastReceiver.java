package com.lzf.smsbackups.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by MJCoder on 2018-06-05.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("MyBroadcastReceiver", intent.getAction());
        context.startService(new Intent(context, MyIntentService.class));
    }
}
