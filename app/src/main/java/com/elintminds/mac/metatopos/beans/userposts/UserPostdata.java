package com.elintminds.mac.metatopos.beans.userposts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPostdata {


    @SerializedName("postinfo")
    @Expose
    private PostInfo postinfo;

    public PostInfo getPostinfo() {
        return postinfo;
    }

    public void setPostinfo(PostInfo postinfo) {

        this.postinfo = postinfo;
    }
}
