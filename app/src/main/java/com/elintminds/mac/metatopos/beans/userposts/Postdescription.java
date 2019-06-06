package com.elintminds.mac.metatopos.beans.userposts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Postdescription {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("addedOn")
    @Expose
    private String addedOn;
    @SerializedName("postAddedBy")
    @Expose
    private String postAddedBy;
    @SerializedName("postId")
    @Expose
    private String postId;
    @SerializedName("viewCount")
    @Expose
    private String viewCount;
    @SerializedName("Attachments")
    @Expose
    private List<AttachmentImages> attachments = null;
    @SerializedName("TotalComment")
    @Expose
    private String totalComment;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("conditionId")
    @Expose
    private String conditionId;
    @SerializedName("post_type")
    @Expose
    private String postType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getPostAddedBy() {
        return postAddedBy;
    }

    public void setPostAddedBy(String postAddedBy) {
        this.postAddedBy = postAddedBy;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public List<AttachmentImages> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentImages> attachments) {
        this.attachments = attachments;
    }

    public String getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(String totalComment) {
        this.totalComment = totalComment;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }
}
