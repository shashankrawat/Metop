package com.elintminds.mac.metatopos.beans.getfollowerslist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowersListData {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("followerId")
    @Expose
    private String followerId;
    @SerializedName("addedBy")
    @Expose
    private String addedBy;
    @SerializedName("addedOn")
    @Expose
    private String addedOn;
    @SerializedName("modifiedby")
    @Expose
    private String modifiedby;
    @SerializedName("modifiedOn")
    @Expose
    private String modifiedOn;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("userInfo")
    @Expose
    private FollowersUserInfo userInfo;

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

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
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

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
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

    public FollowersUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(FollowersUserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
