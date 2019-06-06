package com.elintminds.mac.metatopos.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.mappath.MyItems;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

public class ClusteringDemo extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    GoogleMap mMap;
    ClusterManager mClusterManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.map_fragment_layout);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap != null) {
            return;
        }
        mMap = googleMap;
        setUpClusterer();
    }

    protected GoogleMap getMap() {
        return mMap;
    }


    private void setUpClusterer() {
        // Declare a variable for the cluster manager.
        ClusterManager<MyItems> mClusterManager;

        // Position the map.
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItems>(this, getMap());

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        getMap().setOnCameraChangeListener((GoogleMap.OnCameraChangeListener) mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MyItems offsetItem = new MyItems(lat, lng);
            mClusterManager.addItem(offsetItem);
        }
    }

//    void startDemo() {
//        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 9.5f));
//
//        mClusterManager = new ClusterManager<MarkerInfo>(this, getMap());
//        mClusterManager.setRenderer(new CustomMarkerClusteringDemoActivity.PersonRenderer());
//        getMap().setOnCameraIdleListener(mClusterManager);
//        getMap().setOnMarkerClickListener(mClusterManager);
//        getMap().setOnInfoWindowClickListener(mClusterManager);
//        mClusterManager.setOnClusterClickListener(this);
//        mClusterManager.setOnClusterInfoWindowClickListener(this);
//        mClusterManager.setOnClusterItemClickListener(this);
//        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
//
//        addItems();
//        mClusterManager.cluster();
//    }

//    private void addItems() {
//        // http://www.flickr.com/photos/sdasmarchives/5036248203/
//        mClusterManager.addItem(new MarkerInfo(position(), "Walter", R.drawable.ic_tracking));
//
//        // http://www.flickr.com/photos/usnationalarchives/4726917149/
//        mClusterManager.addItem(new MarkerInfo(position(), "Gran", R.drawable.ic_tracking));
//
//        // http://www.flickr.com/photos/nypl/3111525394/
//        mClusterManager.addItem(new MarkerInfo(position(), "Ruth", R.drawable.ic_tracking));
//
//        // http://www.flickr.com/photos/smithsonian/2887433330/
//        mClusterManager.addItem(new MarkerInfo(position(), "Stefan", R.drawable.ic_tracking));
//
//        // http://www.flickr.com/photos/library_of_congress/2179915182/
//        mClusterManager.addItem(new MarkerInfo(position(), "Mechanic", R.drawable.ic_tracking));
//
//        // http://www.flickr.com/photos/nationalmediamuseum/7893552556/
//        mClusterManager.addItem(new MarkerInfo(position(), "Yeats", R.drawable.ic_tracking));
//
//        // http://www.flickr.com/photos/sdasmarchives/5036231225/
//        mClusterManager.addItem(new MarkerInfo(position(), "John", R.drawable.ic_tracking));
//
//        // http://www.flickr.com/photos/anmm_thecommons/7694202096/
//        mClusterManager.addItem(new MarkerInfo(position(), "Trevor the Turtle", R.drawable.ic_tracking));
//
//        // http://www.flickr.com/photos/usnationalarchives/4726892651/
//        mClusterManager.addItem(new MarkerInfo(position(), "Teach", R.drawable.ic_tracking));
//    }

//    private LatLng position() {
//        return new LatLng(random(51.6723432, 51.38494009999999), random(0.148271, -0.3514683));
//    }
//
//    private double random(double min, double max) {
//        return mRandom.nextDouble() * (max - min) + min;
//    }
}
