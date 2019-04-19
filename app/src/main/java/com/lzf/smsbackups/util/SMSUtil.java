package com.lzf.smsbackups.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.lzf.smsbackups.bean.SMSBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * sms主要结构：
 * 　　_id：          短信序号，如100
 * 　　thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的
 * 　　address：  发件人地址，即手机号，如+86138138000
 * 　　person：   发件人，如果发件人在通讯录中则为具体姓名，陌生人为null
 * 　　date：       日期，long型，如1346988516，可以对日期显示格式进行设置
 * 　　protocol： 协议0SMS_RPOTO短信，1MMS_PROTO彩信
 * 　　read：      是否阅读0未读，1已读
 * 　　status：    短信状态-1接收，0complete,64pending,128failed
 * 　　type：       短信类型1是接收到的，2是已发出
 * 　　body：      短信具体内容
 * 　　service_center：短信服务中心号码编号，如+8613800755500
 * Created by MJCoder on 2018-06-06.
 */

public class SMSUtil {
    public static final Uri SMS_INBOX = Uri.parse("content://sms/inbox"); //收件箱
    public static final Uri SMS_SENT = Uri.parse("content://sms/sent"); //已发送
    public static final Uri SMS = Uri.parse("content://sms/"); //全部短信
    public static final Uri SMS_DRAFT = Uri.parse("content://sms/draft"); //草稿
    public static final Uri SMS_OUTBOX = Uri.parse("content://sms/outbox"); //发件箱
    public static final Uri SMS_FAILED = Uri.parse("content://sms/failed"); //发件失败
    public static final Uri SMS_QUEUED = Uri.parse("content://sms/queued"); //待发送列表
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取最新的短消息的方法
     */
    public static List<SMSBean> getSmsFromPhone(Context context) {
        List<SMSBean> smsBeans = new ArrayList<SMSBean>();
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[]{"_id", "thread_id", "address", "person", "date", "type", "body"};
        String where = " date >  " + (System.currentTimeMillis() - 5 * 1000); //10 * 60 * 1000=600,000：10分钟
        Cursor cur = cr.query(SMS, projection, where, null, "date desc");
        if (null == cur)
            return smsBeans;
        while (cur.moveToNext()) {
            SMSBean smsBean = new SMSBean();
            smsBean.setId(cur.getString(cur.getColumnIndex("_id")));
            smsBean.setThread_id(cur.getString(cur.getColumnIndex("thread_id")));
            smsBean.setAddress(cur.getString(cur.getColumnIndex("address")));//手机号
            smsBean.setPerson(cur.getString(cur.getColumnIndex("person")));
            smsBean.setDate(SIMPLE_DATE_FORMAT.format(cur.getLong(cur.getColumnIndex("date"))));
            smsBean.setType(cur.getString(cur.getColumnIndex("type"))); //"1".equals(type.trim())收到了短信；"2".equals(type.trim())发送出的短信
            smsBean.setBody(cur.getString(cur.getColumnIndex("body")));
            if (!smsBeans.contains(smsBean)) {
                smsBeans.add(smsBean);
            }
        }
        Log.v("SMSUtil", smsBeans.toString());
        return smsBeans;
    }

    /**
     * 获取现存的所有短信
     *
     * @param context
     * @return
     */
    public static List<SMSBean> getAllSmsFromPhone(Context context) {
        List<SMSBean> smsBeans = new ArrayList<SMSBean>();
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[]{"_id", "thread_id", "address", "person", "date", "type", "body"};
        Cursor cur = cr.query(SMS, projection, null, null, "date desc");
        if (null == cur)
            return smsBeans;
        while (cur.moveToNext()) {
            SMSBean smsBean = new SMSBean();
            smsBean.setId(cur.getString(cur.getColumnIndex("_id")));
            smsBean.setThread_id(cur.getString(cur.getColumnIndex("thread_id")));
            smsBean.setAddress(cur.getString(cur.getColumnIndex("address")));//手机号
            smsBean.setPerson(cur.getString(cur.getColumnIndex("person")));
            smsBean.setDate(SIMPLE_DATE_FORMAT.format(cur.getLong(cur.getColumnIndex("date"))));
            smsBean.setType(cur.getString(cur.getColumnIndex("type"))); //"1".equals(type.trim())收到了短信；"2".equals(type.trim())发送出的短信
            smsBean.setBody(cur.getString(cur.getColumnIndex("body")));
            if (!smsBeans.contains(smsBean)) {
                smsBeans.add(smsBean);
            }
        }
        Log.v("SMSUtil", smsBeans.toString());
        return smsBeans;
    }
}
