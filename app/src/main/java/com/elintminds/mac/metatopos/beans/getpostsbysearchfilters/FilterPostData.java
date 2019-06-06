package com.elintminds.mac.metatopos.beans.getpostsbysearchfilters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterPostData {

    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("viewCount")
    @Expose
    private String viewCount;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("addedOn")
    @Expose
    private String addedOn;
    @SerializedName("postAddedBy")
    @Expose
    private String postAddedBy;
    @SerializedName("postId")
    @Expose
    private String postId;
    @SerializedName("profileImageUrl")
    @Expose
    private String profileImageUrl;
    @SerializedName("mojiImageUrl")
    @Expose
    private String mojiImageUrl;
    @SerializedName("Attachments")
    @Expose
    private List<FilterPostAttachment> attachments = null;
    @SerializedName("TotalComment")
    @Expose
    private String totalComment;
    @SerializedName("TotalInterestedPeople")
    @Expose
    private String totalInterestedPeople;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("conditionId")
    @Expose
    private String conditionId;
    @SerializedName("post_type")
    @Expose
    private String postType;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getMojiImageUrl() {
        return mojiImageUrl;
    }

    public void setMojiImageUrl(String mojiImageUrl) {
        this.mojiImageUrl = mojiImageUrl;
    }

    public List<FilterPostAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<FilterPostAttachment> attachments) {
        this.attachments = attachments;
    }

    public String getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(String totalComment) {
        this.totalComment = totalComment;
    }

    public String getTotalInterestedPeople() {
        return totalInterestedPeople;
    }

    public void setTotalInterestedPeople(String totalInterestedPeople) {
        this.totalInterestedPeople = totalInterestedPeople;
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
