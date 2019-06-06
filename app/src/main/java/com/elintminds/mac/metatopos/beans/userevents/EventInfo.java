package com.elintminds.mac.metatopos.beans.userevents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventInfo
{
        @SerializedName("postcount")
        @Expose
        private Integer postcount;
        @SerializedName("links")
        @Expose
        private List<Integer> links = null;
        @SerializedName("posts")
        @Expose
        private List<EventDetails> posts = null;

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

        public List<EventDetails> getPosts() {
            return posts;
        }

        public void setPosts(List<EventDetails> posts) {
            this.posts = posts;
        }
}
