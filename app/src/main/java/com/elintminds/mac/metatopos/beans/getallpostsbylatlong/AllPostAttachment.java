package com.elintminds.mac.metatopos.beans.getallpostsbylatlong;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllPostAttachment {

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
