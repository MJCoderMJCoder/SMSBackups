package com.lzf.smsbackups.util;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.lzf.smsbackups.SMSBackupsApp;

/**
 * 内容观察者探测器
 * Created by MJCoder on 2018-06-05.
 */

public class ObserverUtil extends ContentObserver {
    private Context context;

    public ObserverUtil(Context context, Handler handler) {
        super(handler);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Log.v("ObserverUtil", uri + "");
        try {
            //每当有新短信到来时，使用我们获取短消息的方法
            ExcelUtil.writeExcel(context, SMSBackupsApp.TITLE, "SMSBackups.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
