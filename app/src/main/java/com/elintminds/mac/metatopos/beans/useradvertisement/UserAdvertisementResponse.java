package com.elintminds.mac.metatopos.beans.useradvertisement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAdvertisementResponse {


    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private Error error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private UserAdvertisementdata data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserAdvertisementdata getData() {
        return data;
    }

    public void setData(UserAdvertisementdata data) {
        this.data = data;
    }
}
