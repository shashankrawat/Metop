package com.elintminds.mac.metatopos.beans.getpost;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPostByIdAttachment implements Parcelable {
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    protected GetPostByIdAttachment(Parcel in) {
        imageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetPostByIdAttachment> CREATOR = new Creator<GetPostByIdAttachment>() {
        @Override
        public GetPostByIdAttachment createFromParcel(Parcel in) {
            return new GetPostByIdAttachment(in);
        }

        @Override
        public GetPostByIdAttachment[] newArray(int size) {
            return new GetPostByIdAttachment[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
