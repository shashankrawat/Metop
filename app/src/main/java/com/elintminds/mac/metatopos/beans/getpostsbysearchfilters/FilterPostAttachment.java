package com.elintminds.mac.metatopos.beans.getpostsbysearchfilters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterPostAttachment {

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
