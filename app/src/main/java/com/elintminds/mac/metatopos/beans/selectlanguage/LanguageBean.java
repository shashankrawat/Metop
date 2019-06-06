package com.elintminds.mac.metatopos.beans.selectlanguage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LanguageBean {
    @SerializedName("languages")
    @Expose
    private List<SelectLanguageData> languages = null;

    public List<SelectLanguageData> getLanguages() {
        return languages;
    }

    public void setLanguages(List<SelectLanguageData> languages) {
        this.languages = languages;
    }
}
