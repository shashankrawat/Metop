package com.elintminds.mac.metatopos.beans.useradvertisement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAdvertisementdata {


    @SerializedName("postinfo")
    @Expose
    private AdvertisementInfo postinfo;

    public AdvertisementInfo getPostinfo() {
        return postinfo;
    }

    public void setPostinfo(AdvertisementInfo postinfo) {
        this.postinfo = postinfo;
    }
}

