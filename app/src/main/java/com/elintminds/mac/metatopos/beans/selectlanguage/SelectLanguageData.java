package com.elintminds.mac.metatopos.beans.selectlanguage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectLanguageData {
    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("CODE")
    @Expose
    private String cODE;
    @SerializedName("SUBCODE")
    @Expose
    private String sUBCODE;
    @SerializedName("DESCRIPTION")
    @Expose
    private String dESCRIPTION;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;

    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getCODE() {
        return cODE;
    }

    public void setCODE(String cODE) {
        this.cODE = cODE;
    }

    public String getSUBCODE() {
        return sUBCODE;
    }

    public void setSUBCODE(String sUBCODE) {
        this.sUBCODE = sUBCODE;
    }

    public String getDESCRIPTION() {
        return dESCRIPTION;
    }

    public void setDESCRIPTION(String dESCRIPTION) {
        this.dESCRIPTION = dESCRIPTION;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

}
