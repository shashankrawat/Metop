package com.elintminds.mac.metatopos.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.AdvertisementActivity;
import com.elintminds.mac.metatopos.activities.EventsActivity;
import com.elintminds.mac.metatopos.activities.PostActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Mapfragmentclass extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, GoogleMap.OnMarkerClickListener {
    SupportMapFragment mapFragment;
    OffersListFragment offersListFragment;
    private GoogleMap mMap;
    ImageView ic_listview, ic_search, ic_filter;
    Context context;
    private Marker customMarker1, customMarker2, customMarker3;
    private LatLng markerLatLngEvnt, markerLatLngadvertisement, markerLatLngpost;
    LocationManager locationManager;
    View marker1, marker2, marker3, mapView;
    TextView numTxt, numTxt2, numTxt3;
    LatLngBounds bounds, bounds2, bounds3;


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment_layout, container, false);
        ic_listview = view.findViewById(R.id.icon_gridview);
        ic_filter = view.findViewById(R.id.icon_filter);

        ic_listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new OffersListFragment();
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container_top, fragment, "mapview");
                fragmentTransaction.commit();

            }
        });
        ic_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(getContext(), CustomMarkerClusteringDemoActivity.class);
//                startActivity(i);
            }
        });


        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
            markerLatLngEvnt = new LatLng(48.8567, 2.3508);
            markerLatLngadvertisement = new LatLng(48.8530, 2.3450);
            markerLatLngpost = new LatLng(48.8500, 2.3550);
            mapView = getChildFragmentManager().findFragmentById(R.id.map).getView();
            marker1 = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.event_marker_layout, null);
            numTxt = marker1.findViewById(R.id.event_description);
            marker2 = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.advertisement_mrker_layout, null);
            numTxt2 = marker2.findViewById(R.id.advertisement_description);
            marker3 = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.common_post_marker_layout, null);
            numTxt3 = marker3.findViewById(R.id.post_content);
            numTxt.setText("Chainsmoker Ultra Event");
            numTxt2.setText("Best Ice Cream Corner");
            numTxt3.setText("Buy Iphone XS");
            customMarker1 = mMap.addMarker(new MarkerOptions()
                    .position(markerLatLngEvnt)
                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, marker1))));
            customMarker2 = mMap.addMarker(new MarkerOptions()
                    .position(markerLatLngadvertisement)
                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, marker2))));
            customMarker3 = mMap.addMarker(new MarkerOptions()
                    .position(markerLatLngpost)
                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, marker3))));
            mMap.setOnMarkerClickListener(this);

            if (mapView.getViewTreeObserver().isAlive()) {
                mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onGlobalLayout() {
                        bounds = new LatLngBounds.Builder().include(markerLatLngEvnt).build();
                        bounds2 = new LatLngBounds.Builder().include(markerLatLngadvertisement).build();
                        bounds3 = new LatLngBounds.Builder().include(markerLatLngpost).build();
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }

                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                        mMap.setMinZoomPreference(5);
                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(48.8567,
                                2.3508));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                        mMap.moveCamera(center);
                        mMap.animateCamera(zoom);
                    }
                });
            }
            if (!success) {
                Log.e(getTag(), "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(getTag(), "Can't find style. Error: ", e);
        }

    }

    // Convert a view to bitmap
    public Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


    @Override
    public void onMyLocationChange(Location location) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(customMarker1)) {
            Intent i = new Intent(getContext(), EventsActivity.class);
            startActivity(i);

        } else if (marker.equals(customMarker2)) {
            Intent i = new Intent(getContext(), AdvertisementActivity.class);
            startActivity(i);

        } else if (marker.equals(customMarker3)) {
            Intent i = new Intent(getContext(), PostActivity.class);
            startActivity(i);

        }
        return false;
    }


}
