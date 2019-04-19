package com.lzf.smsbackups.util;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lzf.smsbackups.SMSBackupsApp;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {

    /**
     * 必须实现父类的构造方法
     */
    public MyIntentService() {
        super("MyIntentService");
    }

    /**
     * 必须重写的核心方法
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Log.v("MyIntentService", intent + "");
            startForeground(0, null);
            //            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            //            int anHour = 30 * 1000; //这里是定时的,这里设置的是每隔30秒执行一次
            //            long triggerAtTime = SystemClock.elapsedRealtime() + anHour; //SystemClock.elapsedRealtime()：获得系统开机到现在经历的毫秒数
            //            Intent alarmReceiverIntent = new Intent(this, AlarmBroadcastReceiver.class);
            //            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmReceiverIntent, 0);
            //            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent); //通过set方法设置定时任务
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            while (true) {
                try {
                    ExcelUtil.writeExcel(this, SMSBackupsApp.TITLE, "SMSBackups.xls");//每当有新短信到来时，使用我们获取短消息的方法
                    Thread.sleep(2 * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
    }

    /**
     * Service被创建时调用
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Service被启动时调用
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 必须要实现的方法
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * Service被关闭之前回调
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
