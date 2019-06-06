package com.elintminds.mac.metatopos.beans.userposts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostInfo
{
        @SerializedName("postcount")
        @Expose
        private Integer postcount;
        @SerializedName("links")
        @Expose
        private List<Integer> links = null;
        @SerializedName("posts")
        @Expose
        private List<Postdescription> posts = null;

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

        public List<Postdescription> getPosts() {
            return posts;
        }

        public void setPosts(List<Postdescription> posts) {
            this.posts = posts;
        }
}
