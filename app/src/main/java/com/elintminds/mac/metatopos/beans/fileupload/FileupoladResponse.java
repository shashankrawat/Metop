package com.elintminds.mac.metatopos.beans.fileupload;

import com.elintminds.mac.metatopos.beans.login.ErrorBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileupoladResponse {

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
    private FileUploadData fileUpload;

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

    public FileUploadData getfileData() {
        return fileUpload;
    }

    public void setfileData(FileUploadData fileUpload) {
        this.fileUpload = fileUpload;
    }

}
