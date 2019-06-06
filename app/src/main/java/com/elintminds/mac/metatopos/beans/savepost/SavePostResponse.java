package com.elintminds.mac.metatopos.beans.savepost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SavePostResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private Object error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private SavePostData data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SavePostData getData() {
        return data;
    }

    public void setData(SavePostData data) {
        this.data = data;
    }
}
