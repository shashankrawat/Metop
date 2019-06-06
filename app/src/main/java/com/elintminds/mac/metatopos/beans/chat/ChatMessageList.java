package com.elintminds.mac.metatopos.beans.chat;

public class ChatMessageList {

    public final static String MSG_TYPE_SENT = "MSG_TYPE_SENT";

    public final static String MSG_TYPE_RECEIVED = "MSG_TYPE_RECEIVED";
    public final static String MSG_Time_RECEIVED = "MSG_TIME_RECEIVED";
    public final static String MSG_Time_SEND = "MSG_TYPE_SEND";

    // Message content.
    private String msgContent;
    //image uri
//    private Bitmap bitmapImage;
    // Message type.
    private String msgType;
    private String msgTime;

//    public Bitmap getBitmapImage() {
//        return bitmapImage;
//    }
//
//    public void setBitmapImage(Bitmap bitmapImage) {
//        this.bitmapImage = bitmapImage;
//    }


    public ChatMessageList(String msgType, String msgContent, String msgTime) {
        this.msgType = msgType;
        this.msgContent = msgContent;
        this.msgTime = msgTime;
//        this.bitmapImage = bitmapImage;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }
}
