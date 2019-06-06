package com.elintminds.mac.metatopos.beans.useradvertisement;

import com.elintminds.mac.metatopos.beans.userposts.AttachmentImages;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Advertisementdetail {
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
    @SerializedName("maxRangeOnMap")
    @Expose
    private String maxRangeOnMap;
    @SerializedName("timeDuration")
    @Expose
    private String timeDuration;
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

    public String getMaxRangeOnMap() {
        return maxRangeOnMap;
    }

    public void setMaxRangeOnMap(String maxRangeOnMap) {
        this.maxRangeOnMap = maxRangeOnMap;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        this.timeDuration = timeDuration;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }


}
