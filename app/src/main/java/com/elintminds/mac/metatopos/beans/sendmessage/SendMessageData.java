package com.elintminds.mac.metatopos.beans.sendmessage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendMessageData {

    @SerializedName("MessagesData")
    @Expose
    private MessageData messagesData;

    public MessageData getMessagesData() {
        return messagesData;
    }

    public void setMessagesData(MessageData messagesData) {
        this.messagesData = messagesData;
    }

}
