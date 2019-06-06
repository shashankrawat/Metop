package com.elintminds.mac.metatopos.beans.getpost;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPostByIdData implements Parcelable {

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
    @SerializedName("zipcode")
    @Expose
    private String zipcode;

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
    @SerializedName("isFavorites")
    @Expose
    private String isFavorites;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("Attachments")
    @Expose
    private List<GetPostByIdAttachment> attachments = null;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("conditionId")
    @Expose
    private String conditionId;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("superCategoryId")
    @Expose
    private String superCategoryId;
    @SerializedName("Comments")
    @Expose
    private GetCommentByPostID comments;
    @SerializedName("TotalInterestedPeople")
    @Expose
    private String totalInterestedPeople;
    @SerializedName("maxRangeOnMap")
    @Expose
    private String maxRangeOnMap;
    @SerializedName("timeDuration")
    @Expose
    private String timeDuration;
    @SerializedName("externalLink")
    @Expose
    private String externalLink;
    @SerializedName("startDateTime")
    @Expose
    private String startDateTime;
    @SerializedName("endDateTime")
    @Expose
    private String endDateTime;
    @SerializedName("userinfo")
    @Expose
    private GetPostByIDUserInfo userinfo;
    @SerializedName("login_userinfo")
    @Expose
    private GetPostByIdLoggedUserInfo loginUserinfo;
    @SerializedName("visibilityTypeId")
    @Expose
    private String visibilityTypeId;

    protected GetPostByIdData(Parcel in) {
        userID = in.readString();
        title = in.readString();
        viewCount = in.readString();
        description = in.readString();
        address = in.readString();
        city = in.readString();
        state = in.readString();
        zipcode = in.readString();
        country = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        addedOn = in.readString();
        postAddedBy = in.readString();
        postId = in.readString();
        profileImageUrl = in.readString();
        mojiImageUrl = in.readString();
        isFavorites = in.readString();
        categoryId = in.readString();
        attachments = in.createTypedArrayList(GetPostByIdAttachment.CREATOR);
        price = in.readString();
        conditionId = in.readString();
        postType = in.readString();
        superCategoryId = in.readString();
        totalInterestedPeople = in.readString();
        maxRangeOnMap = in.readString();
        timeDuration = in.readString();
        externalLink = in.readString();
        startDateTime = in.readString();
        endDateTime = in.readString();
        loginUserinfo = in.readParcelable(GetPostByIdLoggedUserInfo.class.getClassLoader());
        visibilityTypeId = in.readString();
    }

    public static final Creator<GetPostByIdData> CREATOR = new Creator<GetPostByIdData>() {
        @Override
        public GetPostByIdData createFromParcel(Parcel in) {
            return new GetPostByIdData(in);
        }

        @Override
        public GetPostByIdData[] newArray(int size) {
            return new GetPostByIdData[size];
        }
    };

    public String getVisibilityTypeId() {
        return visibilityTypeId;
    }

    public void setVisibilityTypeId(String visibilityTypeId) {
        this.visibilityTypeId = visibilityTypeId;
    }


    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
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


    public GetPostByIdLoggedUserInfo getLoginUserinfo() {
        return loginUserinfo;
    }

    public void setLoginUserinfo(GetPostByIdLoggedUserInfo loginUserinfo) {
        this.loginUserinfo = loginUserinfo;
    }


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

    public String getIsFavorites() {
        return isFavorites;
    }

    public void setIsFavorites(String isFavorites) {
        this.isFavorites = isFavorites;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<GetPostByIdAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<GetPostByIdAttachment> attachments) {
        this.attachments = attachments;
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

    public String getSuperCategoryId() {
        return superCategoryId;
    }

    public void setSuperCategoryId(String superCategoryId) {
        this.superCategoryId = superCategoryId;
    }

    public GetCommentByPostID getComments() {
        return comments;
    }

    public void setComments(GetCommentByPostID comments) {
        this.comments = comments;
    }

    public String getTotalInterestedPeople() {
        return totalInterestedPeople;
    }

    public void setTotalInterestedPeople(String totalInterestedPeople) {
        this.totalInterestedPeople = totalInterestedPeople;
    }

    public GetPostByIDUserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(GetPostByIDUserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(title);
        dest.writeString(viewCount);
        dest.writeString(description);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zipcode);
        dest.writeString(country);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(addedOn);
        dest.writeString(postAddedBy);
        dest.writeString(postId);
        dest.writeString(profileImageUrl);
        dest.writeString(mojiImageUrl);
        dest.writeString(isFavorites);
        dest.writeString(categoryId);
        dest.writeTypedList(attachments);
        dest.writeString(price);
        dest.writeString(conditionId);
        dest.writeString(postType);
        dest.writeString(superCategoryId);
        dest.writeString(totalInterestedPeople);
        dest.writeString(maxRangeOnMap);
        dest.writeString(timeDuration);
        dest.writeString(externalLink);
        dest.writeString(startDateTime);
        dest.writeString(endDateTime);
        dest.writeParcelable(loginUserinfo, flags);
        dest.writeString(visibilityTypeId);
    }
}
