package com.elintminds.mac.metatopos.beans.userevents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserEventdata {


    @SerializedName("postinfo")
    @Expose
    private EventInfo postinfo;

    public EventInfo getPostinfo() {
        return postinfo;
    }

    public void setPostinfo(EventInfo postinfo) {

        this.postinfo = postinfo;
    }
}
