package com.elintminds.mac.metatopos.beans.advertisementplan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlansData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("validityPeriod")
    @Expose
    private String validityPeriod;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("totalPosts")
    @Expose
    private String totalPosts;
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
    @SerializedName("isActivated")
    @Expose
    private String isActivated;
    @SerializedName("planActivationOn")
    @Expose
    private String planActivationOn;
    @SerializedName("planDays")
    @Expose
    private String planDays;

    private int colorCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(String totalPosts) {
        this.totalPosts = totalPosts;
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

    public String getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(String isActivated) {
        this.isActivated = isActivated;
    }

    public String getPlanActivationOn() {
        return planActivationOn;
    }

    public void setPlanActivationOn(String planActivationOn) {
        this.planActivationOn = planActivationOn;
    }

    public String getPlanDays() {
        return planDays;
    }

    public void setPlanDays(String planDays) {
        this.planDays = planDays;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }
}
