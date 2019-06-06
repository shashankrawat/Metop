package com.elintminds.mac.metatopos.beans.getfiltersCategoryList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FiltersData {

    @SerializedName("posts")
    @Expose
    private List<PostFilterData> posts = null;
    @SerializedName("event")
    @Expose
    private EventFilterData event;
    @SerializedName("advertisements")
    @Expose
    private AdvertisementFilterData advertisements;

    public List<PostFilterData> getPosts() {
        return posts;
    }

    public void setPosts(List<PostFilterData> posts) {
        this.posts = posts;
    }

    public EventFilterData getEvent() {
        return event;
    }

    public void setEvent(EventFilterData event) {
        this.event = event;
    }

    public AdvertisementFilterData getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(AdvertisementFilterData advertisements) {
        this.advertisements = advertisements;
    }

}
