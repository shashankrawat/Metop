package com.elintminds.mac.metatopos.beans.getpostsbysearchfilters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FiltersData {

    @SerializedName("postinfo")
    @Expose
    private FilterPostsInfo postinfo;

    public FilterPostsInfo getPostinfo() {
        return postinfo;
    }

    public void setPostinfo(FilterPostsInfo postinfo) {
        this.postinfo = postinfo;
    }

}
