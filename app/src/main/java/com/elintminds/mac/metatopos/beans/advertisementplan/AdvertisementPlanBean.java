package com.elintminds.mac.metatopos.beans.advertisementplan;

public class AdvertisementPlanBean {


    String planName;
    int  Bgcolor;

    public AdvertisementPlanBean(String planName, int bgcolor) {
        this.planName = planName;
        Bgcolor = bgcolor;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getBgcolor() {
        return Bgcolor;
    }

    public void setBgcolor(int bgcolor) {
        Bgcolor = bgcolor;
    }
}
