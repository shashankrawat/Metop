package com.elintminds.mac.metatopos.beans.getpost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCommentByPostID {


    @SerializedName("TotalComment")
    @Expose
    private String totalComment;
    @SerializedName("RecentComment")
    @Expose
    private List<GetPostByIdRecentComment> recentComment = null;

    public String getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(String totalComment) {
        this.totalComment = totalComment;
    }

    public List<GetPostByIdRecentComment> getRecentComment() {
        return recentComment;
    }

    public void setRecentComment(List<GetPostByIdRecentComment> recentComment) {
        this.recentComment = recentComment;
    }

}
