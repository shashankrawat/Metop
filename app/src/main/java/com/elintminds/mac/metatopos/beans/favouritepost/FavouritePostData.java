package com.elintminds.mac.metatopos.beans.favouritepost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavouritePostData {

    @SerializedName("postinfo")
    @Expose
    private FavouritePostInfo postinfo;

    public FavouritePostInfo getPostinfo() {
        return postinfo;
    }

    public void setPostinfo(FavouritePostInfo postinfo) {
        this.postinfo = postinfo;
    }
}
