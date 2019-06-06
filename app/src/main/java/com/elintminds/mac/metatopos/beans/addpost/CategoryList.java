package com.elintminds.mac.metatopos.beans.addpost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryList {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("postSuperCategoryId")
    @Expose
    private String postSuperCategoryId;
    @SerializedName("title")
    @Expose
    private String title;
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
    @SerializedName("sub_category_list")
    @Expose
    private List<SubCategoryList> subCategoryList = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostSuperCategoryId() {
        return postSuperCategoryId;
    }

    public void setPostSuperCategoryId(String postSuperCategoryId) {
        this.postSuperCategoryId = postSuperCategoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<SubCategoryList> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<SubCategoryList> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }
}
