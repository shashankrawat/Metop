package com.elintminds.mac.metatopos.beans.editprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private FollowData data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FollowData getData() {
        return data;
    }

    public void setData(FollowData data) {
        this.data = data;
    }
}
