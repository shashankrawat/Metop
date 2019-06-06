package com.elintminds.mac.metatopos.beans.genEmoji;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenEmojiData implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("mojiImageUrl")
    @Expose
    private String mojiImageUrl;
    @SerializedName("isFeatured")
    @Expose
    private String isFeatured;
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

    protected GenEmojiData(Parcel in) {
        id = in.readString();
        mojiImageUrl = in.readString();
        isFeatured = in.readString();
        addedBy = in.readString();
        addedOn = in.readString();
        modifiedBy = in.readString();
        modifiedOn = in.readString();
        status = in.readString();
    }

    public static final Creator<GenEmojiData> CREATOR = new Creator<GenEmojiData>() {
        @Override
        public GenEmojiData createFromParcel(Parcel in) {
            return new GenEmojiData(in);
        }

        @Override
        public GenEmojiData[] newArray(int size) {
            return new GenEmojiData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMojiImageUrl() {
        return mojiImageUrl;
    }

    public void setMojiImageUrl(String mojiImageUrl) {
        this.mojiImageUrl = mojiImageUrl;
    }

    public String getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(String isFeatured) {
        this.isFeatured = isFeatured;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(mojiImageUrl);
        dest.writeString(isFeatured);
        dest.writeString(addedBy);
        dest.writeString(addedOn);
        dest.writeString(modifiedBy);
        dest.writeString(modifiedOn);
        dest.writeString(status);
    }
}
