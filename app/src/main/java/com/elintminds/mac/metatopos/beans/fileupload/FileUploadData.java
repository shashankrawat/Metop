package com.elintminds.mac.metatopos.beans.fileupload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileUploadData {

    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("file_type")
    @Expose
    private String fileType;
    @SerializedName("file_path")
    @Expose
    private String filePath;
    @SerializedName("full_path")
    @Expose
    private String fullPath;
    @SerializedName("raw_name")
    @Expose
    private String rawName;
    @SerializedName("orig_name")
    @Expose
    private String origName;
    @SerializedName("client_name")
    @Expose
    private String clientName;
    @SerializedName("file_ext")
    @Expose
    private String fileExt;
    @SerializedName("file_size")
    @Expose
    private Double fileSize;
    @SerializedName("is_image")
    @Expose
    private Boolean isImage;
    @SerializedName("image_width")
    @Expose
    private Integer imageWidth;
    @SerializedName("image_height")
    @Expose
    private Integer imageHeight;
    @SerializedName("image_type")
    @Expose
    private String imageType;
    @SerializedName("image_size_str")
    @Expose
    private String imageSizeStr;
    @SerializedName("custom_path")
    @Expose
    private String customPath;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    public String getOrigName() {
        return origName;
    }

    public void setOrigName(String origName) {
        this.origName = origName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public Double getFileSize() {
        return fileSize;
    }

    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }

    public Boolean getIsImage() {
        return isImage;
    }

    public void setIsImage(Boolean isImage) {
        this.isImage = isImage;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageSizeStr() {
        return imageSizeStr;
    }

    public void setImageSizeStr(String imageSizeStr) {
        this.imageSizeStr = imageSizeStr;
    }

    public String getCustomPath() {
        return customPath;
    }

    public void setCustomPath(String customPath) {
        this.customPath = customPath;
    }



}
