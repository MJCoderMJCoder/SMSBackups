package com.lzf.smsbackups.bean;

/**
 * Created by MJCoder on 2018-06-06.
 */

public class SMSBean {
    private String id; //短信序号，如100
    private String thread_id; //对话的序号，如100，与同一个手机号互发的短信，其序号是相同的
    private String address; //发件人地址，即手机号，如+86138138000
    private String person; //发件人，如果发件人在通讯录中则为具体姓名，陌生人为null
    private String date; //日期，long型，如1346988516，可以对日期显示格式进行设置
    private String type; //短信类型1是接收到的，2是已发出
    private String body; //短信具体内容

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "SMSBean{" +
                "id='" + id + '\'' +
                ", thread_id='" + thread_id + '\'' +
                ", address='" + address + '\'' +
                ", person='" + person + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
