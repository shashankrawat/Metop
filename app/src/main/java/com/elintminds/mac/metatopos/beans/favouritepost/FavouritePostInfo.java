package com.elintminds.mac.metatopos.beans.favouritepost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavouritePostInfo {
    @SerializedName("postcount")
    @Expose
    private Integer postcount;
    @SerializedName("links")
    @Expose
    private List<Integer> links = null;
    @SerializedName("posts")
    @Expose
    private List<FavouritePosts> posts = null;

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

    public List<FavouritePosts> getPosts() {
        return posts;
    }

    public void setPosts(List<FavouritePosts> posts) {
        this.posts = posts;
    }

}
