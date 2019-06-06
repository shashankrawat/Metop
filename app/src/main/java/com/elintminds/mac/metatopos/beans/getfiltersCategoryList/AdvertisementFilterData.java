package com.elintminds.mac.metatopos.beans.getfiltersCategoryList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdvertisementFilterData {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("value")
    @Expose
    private List<String> value = null;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

}
