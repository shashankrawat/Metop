package com.elintminds.mac.metatopos.beans.getallpostsbylatlong;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllPostsData {
    @SerializedName("postinfo")
    @Expose
    private AllPostInfo postinfo;

    public AllPostInfo getPostinfo() {
        return postinfo;
    }

    public void setPostinfo(AllPostInfo postinfo) {
        this.postinfo = postinfo;
    }
}
