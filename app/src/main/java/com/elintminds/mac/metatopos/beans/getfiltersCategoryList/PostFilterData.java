package com.elintminds.mac.metatopos.beans.getfiltersCategoryList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class PostFilterData implements Parcelable {
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("value")
    @Expose
    private List<String> value;

    public PostFilterData() {

    }

    protected PostFilterData(Parcel in) {
        label = in.readString();
        value = in.createStringArrayList();
    }

    public static final Creator<PostFilterData> CREATOR = new Creator<PostFilterData>() {
        @Override
        public PostFilterData createFromParcel(Parcel in) {
            return new PostFilterData(in);
        }

        @Override
        public PostFilterData[] newArray(int size) {
            return new PostFilterData[size];
        }
    };

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = Collections.singletonList(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeStringList(value);
    }
}
