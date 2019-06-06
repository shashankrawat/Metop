package com.elintminds.mac.metatopos.beans.sendmessage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageData {

    @SerializedName("ReceiverImage")
    @Expose
    private String receiverImage;
    @SerializedName("MessageInfo")
    @Expose
    private List<SendMessageInfo> messageInfo = null;

    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }

    public List<SendMessageInfo> getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(List<SendMessageInfo> messageInfo) {
        this.messageInfo = messageInfo;
    }
}
