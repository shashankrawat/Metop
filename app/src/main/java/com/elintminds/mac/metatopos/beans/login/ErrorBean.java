package com.elintminds.mac.metatopos.beans.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorBean {

    @SerializedName("oldpassword")
    @Expose
    private String oldpassword;
    @SerializedName("newpassword")
    @Expose
    private String newpassword;
    @SerializedName("confirmnewpassword")
    @Expose
    private String confirmnewpassword;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("profile_image_url")
    @Expose
    private String profileImageUrl;
    @SerializedName("gen_moji_id")
    @Expose
    private String genMojiId;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getGenMojiId() {
        return genMojiId;
    }

    public void setGenMojiId(String genMojiId) {
        this.genMojiId = genMojiId;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getConfirmnewpassword() {
        return confirmnewpassword;
    }

    public void setConfirmnewpassword(String confirmnewpassword) {
        this.confirmnewpassword = confirmnewpassword;
    }
}
