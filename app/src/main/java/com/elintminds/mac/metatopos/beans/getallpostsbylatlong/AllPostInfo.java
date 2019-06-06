package com.elintminds.mac.metatopos.beans.getallpostsbylatlong;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllPostInfo {


    @SerializedName("posts")
    @Expose
    private List<PostList> posts = null;

    public List<PostList> getPosts() {
        return posts;
    }

    public void setPosts(List<PostList> posts) {
        this.posts = posts;
    }
}
