package com.elintminds.mac.metatopos.beans.favouritepost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavouritePosts {

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
    private List<FavouritePostAttactments> attachments = null;
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
    @SerializedName("maxRangeOnMap")
    @Expose
    private String maxRangeOnMap;
    @SerializedName("timeDuration")
    @Expose
    private String timeDuration;
    @SerializedName("externalLink")
    @Expose
    private String externalLink;

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

    public List<FavouritePostAttactments> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<FavouritePostAttactments> attachments) {
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

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }
}
