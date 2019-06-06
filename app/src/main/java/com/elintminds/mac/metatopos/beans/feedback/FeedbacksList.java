package com.elintminds.mac.metatopos.beans.feedback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbacksList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("ratingStars")
    @Expose
    private float ratingStars;
    @SerializedName("feedbackMessage")
    @Expose
    private String feedbackMessage;
    @SerializedName("addedBy")
    @Expose
    private String addedBy;
    @SerializedName("addedOn")
    @Expose
    private String addedOn;
    @SerializedName("modifiedBy")
    @Expose
    private String modifiedBy;
    @SerializedName("modifiedOn")
    @Expose
    private String modifiedOn;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("userInfo")
    @Expose
    private CurrentUserFeedbackInfo userInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getRatingStars() {
        return ratingStars;
    }

    public void setRatingStars(float ratingStars) {
        this.ratingStars = ratingStars;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    public void setFeedbackMessage(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CurrentUserFeedbackInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(CurrentUserFeedbackInfo userInfo) {
        this.userInfo = userInfo;
    }
}
