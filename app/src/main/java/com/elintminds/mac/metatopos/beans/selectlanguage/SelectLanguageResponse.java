package com.elintminds.mac.metatopos.beans.selectlanguage;

import com.elintminds.mac.metatopos.beans.login.ErrorBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectLanguageResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private ErrorBean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private LanguageBean data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LanguageBean getData() {
        return data;
    }

    public void setData(LanguageBean data) {
        this.data = data;
    }

}
