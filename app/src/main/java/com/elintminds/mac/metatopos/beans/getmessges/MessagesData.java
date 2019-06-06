package com.elintminds.mac.metatopos.beans.getmessges;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessagesData {

    @SerializedName("messages")
    @Expose
    private List<Messages> messages = null;
    @SerializedName("userdata")
    @Expose
    private MessageUserData userdata;

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    public MessageUserData getUserdata() {
        return userdata;
    }

    public void setUserdata(MessageUserData userdata) {
        this.userdata = userdata;
    }
}
