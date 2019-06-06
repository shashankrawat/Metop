package com.elintminds.mac.metatopos.beans.getpost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPostByIdRecentComment {

    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("PostUserID")
    @Expose
    private String postUserID;
    @SerializedName("CommentID")
    @Expose
    private String commentID;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("addedOn")
    @Expose
    private String addedOn;
    @SerializedName("profileImageUrl")
    @Expose
    private String profileImageUrl;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostUserID() {
        return postUserID;
    }

    public void setPostUserID(String postUserID) {
        this.postUserID = postUserID;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
