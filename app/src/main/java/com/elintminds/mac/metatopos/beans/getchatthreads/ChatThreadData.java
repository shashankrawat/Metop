package com.elintminds.mac.metatopos.beans.getchatthreads;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatThreadData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("postId")
    @Expose
    private String postId;
    @SerializedName("receiverUserId")
    @Expose
    private String receiverUserId;
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
    @SerializedName("is_deleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("postImageUrl")
    @Expose
    private List<ChatThreadPostImageUrl> postImageUrl = null;
    @SerializedName("messageId")
    @Expose
    private String messageId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("message_addedOn")
    @Expose
    private String messageAddedOn;
    @SerializedName("unread_message_count")
    @Expose
    private String unreadMessageCount;
    @SerializedName("userdata")
    @Expose
    private ChatThreadUserData userdata;

    private boolean isSwipedLeft;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<ChatThreadPostImageUrl> getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(List<ChatThreadPostImageUrl> postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageAddedOn() {
        return messageAddedOn;
    }

    public void setMessageAddedOn(String messageAddedOn) {
        this.messageAddedOn = messageAddedOn;
    }

    public String getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(String unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public ChatThreadUserData getUserdata() {
        return userdata;
    }

    public void setUserdata(ChatThreadUserData userdata) {
        this.userdata = userdata;
    }

    public boolean isSwipedLeft() {
        return isSwipedLeft;
    }

    public void setSwipedLeft(boolean swipedLeft) {
        isSwipedLeft = swipedLeft;
    }
}
