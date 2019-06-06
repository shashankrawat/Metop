package com.elintminds.mac.metatopos.beans.useradvertisement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdvertisementInfo {
    @SerializedName("postcount")
    @Expose
    private Integer postcount;
    @SerializedName("links")
    @Expose
    private List<Integer> links = null;
    @SerializedName("posts")
    @Expose
    private List<Advertisementdetail> posts = null;

    public Integer getPostcount() {
        return postcount;
    }

    public void setPostcount(Integer postcount) {
        this.postcount = postcount;
    }

    public List<Integer> getLinks() {
        return links;
    }

    public void setLinks(List<Integer> links) {
        this.links = links;
    }

    public List<Advertisementdetail> getPosts() {
        return posts;
    }

    public void setPosts(List<Advertisementdetail> posts) {
        this.posts = posts;
    }
}
