package com.lzf.smsbackups;

import android.app.Application;

/**
 * Created by MJCoder on 2018-06-06.
 */

public class SMSBackupsApp extends Application {
    public static final String[] TITLE = new String[]{"短信序号", "会话ID：与同一个手机号互发的短信标识", "手机号", "发件人：如果发件人在通讯录中则为具体姓名，陌生人为null", "日期", "短信类型：1是接收到的，2是已发出的", " 短信具体内容"};
}
