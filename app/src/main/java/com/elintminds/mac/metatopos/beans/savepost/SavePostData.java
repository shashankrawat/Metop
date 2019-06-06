package com.elintminds.mac.metatopos.beans.savepost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class SavePostData {
    @SerializedName("postId")
    @Expose
    private String postId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("PostAddedOn")
    @Expose
    private String postAddedOn;
    @SerializedName("modifiedOn")
    @Expose
    private String modifiedOn;
    @SerializedName("Attachments")
    @Expose
    private List<SavePostAttachment> attachments = null;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostAddedOn() {
        return postAddedOn;
    }

    public void setPostAddedOn(String postAddedOn) {
        this.postAddedOn = postAddedOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public List<SavePostAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<SavePostAttachment> attachments) {
        this.attachments = attachments;
    }

}
