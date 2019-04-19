package com.lzf.smsbackups.activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.lzf.smsbackups.R;
import com.lzf.smsbackups.SMSBackupsApp;
import com.lzf.smsbackups.util.ExcelUtil;
import com.lzf.smsbackups.util.ObserverUtil;
import com.lzf.smsbackups.util.SMSUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.BROADCAST_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 6003);
        }
        getContentResolver().registerContentObserver(SMSUtil.SMS, true, new ObserverUtil(this, new Handler()));
        new Thread() {
            @Override
            public void run() {
                super.run();
                writeExcel();
            }
        }.start();
    }

    /**
     * 写入Excel：递归处理。直至无异常执行完成
     */
    private void writeExcel() {
        try {
            ExcelUtil.writeExcel(this, SMSBackupsApp.TITLE, "SMSBackups.xls");
        } catch (Exception e) {
            e.printStackTrace();
            writeExcel();
        }
    }
}
