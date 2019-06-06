package com.elintminds.mac.metatopos.beans.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterData {



        @SerializedName("userID")
        @Expose
        private String userID;
        @SerializedName("userName")
        @Expose
        private String userName;
        @SerializedName("fullName")
        @Expose
        private String fullName;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("isSocial")
        @Expose
        private String isSocial;
        @SerializedName("isVerified")
        @Expose
        private String isVerified;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("profileImageUrl")
        @Expose
        private String profileImageUrl;
        @SerializedName("mojiImageUrl")
        @Expose
        private String mojiImageUrl;
        @SerializedName("languageId")
        @Expose
        private Object languageId;
        @SerializedName("planId")
        @Expose
        private Object planId;
        @SerializedName("planActivationOn")
        @Expose
        private Object planActivationOn;
        @SerializedName("isNotificationEnabled")
        @Expose
        private String isNotificationEnabled;
        @SerializedName("addedOn")
        @Expose
        private String addedOn;
        @SerializedName("modifiedOn")
        @Expose
        private String modifiedOn;
        @SerializedName("totolPosts")
        @Expose
        private String totolPosts;
        @SerializedName("totolEvents")
        @Expose
        private String totolEvents;
        @SerializedName("totolAdvertisements")
        @Expose
        private String totolAdvertisements;
        @SerializedName("totolFollowers")
        @Expose
        private String totolFollowers;
        @SerializedName("totolFollowings")
        @Expose
        private String totolFollowings;
        @SerializedName("totolReviews")
        @Expose
        private String totolReviews;
        @SerializedName("averageStarRating")
        @Expose
        private String averageStarRating;
        @SerializedName("totalFavouritePosts")
        @Expose
        private String totalFavouritePosts;
        @SerializedName("DeviceTokenID")
        @Expose
        private Integer deviceTokenID;

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

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getIsSocial() {
            return isSocial;
        }

        public void setIsSocial(String isSocial) {
            this.isSocial = isSocial;
        }

        public String getIsVerified() {
            return isVerified;
        }

        public void setIsVerified(String isVerified) {
            this.isVerified = isVerified;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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

        public Object getLanguageId() {
            return languageId;
        }

        public void setLanguageId(Object languageId) {
            this.languageId = languageId;
        }

        public Object getPlanId() {
            return planId;
        }

        public void setPlanId(Object planId) {
            this.planId = planId;
        }

        public Object getPlanActivationOn() {
            return planActivationOn;
        }

        public void setPlanActivationOn(Object planActivationOn) {
            this.planActivationOn = planActivationOn;
        }

        public String getIsNotificationEnabled() {
            return isNotificationEnabled;
        }

        public void setIsNotificationEnabled(String isNotificationEnabled) {
            this.isNotificationEnabled = isNotificationEnabled;
        }

        public String getAddedOn() {
            return addedOn;
        }

        public void setAddedOn(String addedOn) {
            this.addedOn = addedOn;
        }

        public String getModifiedOn() {
            return modifiedOn;
        }

        public void setModifiedOn(String modifiedOn) {
            this.modifiedOn = modifiedOn;
        }

        public String getTotolPosts() {
            return totolPosts;
        }

        public void setTotolPosts(String totolPosts) {
            this.totolPosts = totolPosts;
        }

        public String getTotolEvents() {
            return totolEvents;
        }

        public void setTotolEvents(String totolEvents) {
            this.totolEvents = totolEvents;
        }

        public String getTotolAdvertisements() {
            return totolAdvertisements;
        }

        public void setTotolAdvertisements(String totolAdvertisements) {
            this.totolAdvertisements = totolAdvertisements;
        }

        public String getTotolFollowers() {
            return totolFollowers;
        }

        public void setTotolFollowers(String totolFollowers) {
            this.totolFollowers = totolFollowers;
        }

        public String getTotolFollowings() {
            return totolFollowings;
        }

        public void setTotolFollowings(String totolFollowings) {
            this.totolFollowings = totolFollowings;
        }

        public String getTotolReviews() {
            return totolReviews;
        }

        public void setTotolReviews(String totolReviews) {
            this.totolReviews = totolReviews;
        }

        public String getAverageStarRating() {
            return averageStarRating;
        }

        public void setAverageStarRating(String averageStarRating) {
            this.averageStarRating = averageStarRating;
        }

        public String getTotalFavouritePosts() {
            return totalFavouritePosts;
        }

        public void setTotalFavouritePosts(String totalFavouritePosts) {
            this.totalFavouritePosts = totalFavouritePosts;
        }

        public Integer getDeviceTokenID() {
            return deviceTokenID;
        }

        public void setDeviceTokenID(Integer deviceTokenID) {
            this.deviceTokenID = deviceTokenID;
        }

    }
