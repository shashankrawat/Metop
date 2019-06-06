package com.elintminds.mac.metatopos.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.elintminds.mac.metatopos.MarkerInfo;
import com.elintminds.mac.metatopos.MultiDrawable;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.AdvertisementActivity;
import com.elintminds.mac.metatopos.activities.AdvertisementMarkerGenerator;
import com.elintminds.mac.metatopos.activities.EventIconMaker;
import com.elintminds.mac.metatopos.activities.EventMarkerGenerator;
import com.elintminds.mac.metatopos.activities.EventsActivity;
import com.elintminds.mac.metatopos.activities.PostActivity;
import com.elintminds.mac.metatopos.activities.PostMarkerGenerator;
import com.elintminds.mac.metatopos.beans.getallpostsbylatlong.PostList;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ClusterFrag extends MapFragment
{
    public static final String TAG = "ClusterFrag";
    public List<PostList> postsLists = new ArrayList<>();
    private ArrayList<MarkerInfo> markersList;
    private static final double DEFAULT_RADIUS = 0.0003;
    private int maxClusterSize = 3;
    private boolean isFirstTime = false;

    public ClusterFrag()
    {
    }

    private class CommonRenderer extends DefaultClusterRenderer<MarkerInfo>
    {
        private EventMarkerGenerator eventIconGenerator;
        private AdvertisementMarkerGenerator advertisementIconGenerator;
        private PostMarkerGenerator postIconGenerator;
        private final EventIconMaker mClusterIconGenerator = new EventIconMaker(context);
        private final ImageView mImageView;
        private final int mDimension;
        private Bitmap icon = null;
        private View multiProfile = parentActivity.getLayoutInflater().inflate(R.layout.post_clusterrender_layout, null);

        public CommonRenderer() {
            super(context, getMap(), mClusterManager);
            mImageView = new ImageView(context);
            mDimension = (int) context.getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) context.getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mClusterIconGenerator.setContentView(multiProfile);
        }

        @Override
        protected void onBeforeClusterItemRendered(MarkerInfo person, MarkerOptions markerOptions)
        {
            if (person.postType.equalsIgnoreCase("post"))
            {
                postIconGenerator = new PostMarkerGenerator(context, getView());
                icon = postIconGenerator.makeIcon();
            }
            else if (person.postType.equalsIgnoreCase("advertisement"))
            {
                advertisementIconGenerator = new AdvertisementMarkerGenerator(context);
                icon = advertisementIconGenerator.makeIcon();
            }
            else if (person.postType.equalsIgnoreCase("event"))
            {
                eventIconGenerator = new EventMarkerGenerator(context);
                icon = eventIconGenerator.makeIcon();
            }
            if (icon != null) {
                markerOptions.position(person.getPosition()).icon(BitmapDescriptorFactory.fromBitmap(icon));  //.title(person.name
            }
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MarkerInfo> cluster, MarkerOptions markerOptions)
        {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (MarkerInfo p : cluster.getItems())
            {
                // Draw 4 at most.
                Drawable drawable = context.getResources().getDrawable(R.drawable.profilepicsample);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > maxClusterSize;
        }

        @Override
        protected void onClusterItemRendered(final MarkerInfo clusterItem, final Marker marker)
        {
            super.onClusterItemRendered(clusterItem, marker);

            if (clusterItem.postType.equalsIgnoreCase("post"))
            {
                Log.e("POST_ICON",""+APIService.BASE_URL + clusterItem.profileEmoji);
                Glide.with(parentActivity)
                        .load(APIService.BASE_URL + clusterItem.profileEmoji)
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                        .thumbnail(0.1f)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable drawable, Transition<? super Drawable> transition) {
                                ImageView emojiView = (ImageView) postIconGenerator.setMarkerDetails(clusterItem, 10, 0);
                                if(emojiView != null){
                                    emojiView.setImageDrawable(drawable);
                                }

                                icon = postIconGenerator.makeIcon();
                                marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
                            }
                        });

                Timer timer = new Timer();
                TimerTask updateProfile = new CustomTimerTask(context, marker, clusterItem);
                timer.scheduleAtFixedRate(updateProfile, 100,1000);
            }
            else if (clusterItem.postType.equalsIgnoreCase("advertisement"))
            {
                Glide.with(parentActivity)
                        .load(APIService.BASE_URL + clusterItem.profilePhoto)
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                        .thumbnail(0.1f)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable drawable, Transition<? super Drawable> transition) {
                                ImageView iconView = (ImageView) advertisementIconGenerator.setAdsMarkerDetails(clusterItem);
                                if(iconView != null){
                                    iconView.setImageDrawable(drawable);
                                }
                                icon = advertisementIconGenerator.makeIcon();
                                marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
                            }
                        });
            }
            else if (clusterItem.postType.equalsIgnoreCase("event"))
            {
                Glide.with(parentActivity)
                        .load(APIService.BASE_URL + clusterItem.profilePhoto)
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                        .thumbnail(0.1f)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable drawable, Transition<? super Drawable> transition)
                            {
                                ImageView iconView = (ImageView) eventIconGenerator.setEventMarkerDetails(clusterItem);
                                if(iconView != null){
                                    iconView.setImageDrawable(drawable);
                                }
                                icon = eventIconGenerator.makeIcon();
                                marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
                            }
                        });
            }


        }

    }



    @Override
    protected void startCluster(List<PostList> mapData, LatLng location)
    {
        if(markersList != null){
            mClusterManager.removeItems(markersList);
            getMap().clear();
            isFirstTime = false;
        }else {
            isFirstTime = true;
        }
        postsLists = mapData;
        if (postsLists.size() > 0)
        {
            if(context != null) {
                mClusterManager = new CustomClusterManager<>(context, getMap());
                mClusterManager.setRenderer(new ClusterFrag.CommonRenderer());
                mClusterManager.setOnClusterClickListener(eventsClusterClickListener());
                mClusterManager.setOnClusterItemClickListener(eventsClusterItemClickListener());
                getMap().setOnCameraIdleListener(mClusterManager);
                getMap().setOnMarkerClickListener(mClusterManager);

                LatLngBounds.Builder builder = LatLngBounds.builder();

                markersList = new ArrayList<>();

                for (int i = 0; i < postsLists.size(); i++) {
                    PostList item = postsLists.get(i);
                    double lat = Double.parseDouble(item.getLatitude());
                    double lng = Double.parseDouble(item.getLongitude());
                    String postType = item.getPostType();
                    String title = item.getTitle();
                    String imagepath = item.getAttachments().get(0).getImageUrl();
                    String emojipath = item.getMojiImageUrl();
                    String postID = item.getPostId();
                    String views_count = item.getViewCount();
                    markersList.add(new MarkerInfo(position(lat, lng), title, imagepath, emojipath, postType, postID, views_count));
                    builder.include(position(lat, lng));
                }

                if (!markersList.isEmpty()) {
                    mClusterManager.addItems(markersList);
                    mClusterManager.cluster();
                }

                if (isFirstTime) {
                    // Get the LatLngBounds
                    final LatLngBounds bounds = builder.build();
                    try {
                        getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }



    private LatLng position(double lat, double lng)
    {
        return new LatLng(lat, lng);
    }




    private ClusterManager.OnClusterClickListener<MarkerInfo> eventsClusterClickListener()
    {
        return new ClusterManager.OnClusterClickListener<MarkerInfo>()
        {
            @Override
            public boolean onClusterClick(Cluster<MarkerInfo> cluster)
            {
                float maxZoomLevel = getMap().getMaxZoomLevel();
                float currentZoomLevel = getMap().getCameraPosition().zoom;
                if(currentZoomLevel < maxZoomLevel-1){
                    maxClusterSize = 1;
                }else {
                    maxClusterSize = 4;
                }
                LatLngBounds.Builder builder = LatLngBounds.builder();

                // relocate the markers around the current markers position
                int counter = 0;
                float rotateFactor = (360f / cluster.getItems().size());

                if (mClusterManager.itemsInSameLocation(cluster))
                {
                    for (MarkerInfo item : cluster.getItems())
                    {
                        double lat = item.getPosition().latitude + (DEFAULT_RADIUS * Math.cos(++counter * rotateFactor));
                        double lng = item.getPosition().longitude + (DEFAULT_RADIUS * Math.sin(counter * rotateFactor));
                        MarkerInfo copy = new MarkerInfo(position(lat, lng), item.name, item.profilePhoto, item.profileEmoji, item.postType, item.postID, item.postviews);

                        builder.include(position(lat, lng));
                        mClusterManager.removeItem(item);
                        mClusterManager.addItem(copy);
                        if(distance > 100) {
                            mClusterManager.cluster();
                        }

                    }
                }else {
                    for (MarkerInfo item : cluster.getItems()) {
                        builder.include(item.getPosition());
                    }
                }

                // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
                // inside of bounds, then animate to center of the bounds.
                // Create the builder to collect all essential cluster items for the bounds.




                // Get the LatLngBounds
                final LatLngBounds bounds = builder.build();
                try {
                    getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }
        };
    }

    private ClusterManager.OnClusterItemClickListener<MarkerInfo> eventsClusterItemClickListener()
    {
        return new ClusterManager.OnClusterItemClickListener<MarkerInfo>()
        {
            @Override
            public boolean onClusterItemClick(MarkerInfo markerInfo)
            {
                switch (markerInfo.postType)
                {
                    case "post": {
                        Intent i = new Intent(context, PostActivity.class);
                        i.putExtra("PostID", markerInfo.postID);
                        startActivity(i);
                        break;
                    }
                    case "event": {
                        Intent i = new Intent(context, EventsActivity.class);
                        i.putExtra("PostID", markerInfo.postID);
                        startActivity(i);
                        break;
                    }
                    case "advertisement": {
                        Intent i = new Intent(context, AdvertisementActivity.class);
                        i.putExtra("PostID", markerInfo.postID);
                        startActivity(i);
                        break;
                    }
                }
                return true;
            }
        };
    }



    class CustomTimerTask extends TimerTask
    {
        private Context context;
        private Handler mHandler = new Handler();
        private Marker mMarker;
        private MarkerInfo mInfo;

        // Write Custom Constructor to pass Context
        CustomTimerTask(Context con, Marker marker, MarkerInfo info)
        {
            this.context = con;
            this.mMarker = marker;
            this.mInfo = info;
        }

        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            final Handler handler = new Handler();
                            final long start = SystemClock.uptimeMillis();
                            final long duration = 1000;

                            final Interpolator interpolator = new BounceInterpolator();

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    long elapsed = SystemClock.uptimeMillis() - start;
                                    float t = Math.max(1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                                    mMarker.setAnchor(0.1f, 0.1f * t);

                                    if (t > 0.0) {
                                        // Post again 16ms later.
                                        handler.postDelayed(this, 100);
                                    }
                                }
                            });
                        }
                    });
                }
            }).start();

        }

    }


}
