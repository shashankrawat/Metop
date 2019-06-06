
package com.elintminds.mac.metatopos;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerInfo implements ClusterItem {
    public String name;
    public String profilePhoto;
    public String profileEmoji;
    private LatLng mPosition;
    public String postType;
    public String postID;
    public String postviews;
    private Drawable drawable;


    public MarkerInfo(LatLng position, String name, String pictureResource, String emoji, String type, String id,String views) {
        this.name = name;
        profilePhoto = pictureResource;
        profileEmoji = emoji;
        mPosition = position;
        postType = type;
        postID = id;
        postviews=views;
    }

    public void setPosition(LatLng position)
    {
        mPosition = position;
    }
    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
