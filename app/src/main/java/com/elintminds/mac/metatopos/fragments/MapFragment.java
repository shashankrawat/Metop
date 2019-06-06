package com.elintminds.mac.metatopos.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.elintminds.mac.metatopos.MarkerInfo;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.FilterActivity;
import com.elintminds.mac.metatopos.beans.getallpostsbylatlong.AllPostInfo;
import com.elintminds.mac.metatopos.beans.getallpostsbylatlong.AllPostsData;
import com.elintminds.mac.metatopos.beans.getallpostsbylatlong.AllUserPostResponse;
import com.elintminds.mac.metatopos.beans.getallpostsbylatlong.PostList;
import com.elintminds.mac.metatopos.beans.getfiltersCategoryList.PostFilterData;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.GetLocation;
import com.elintminds.mac.metatopos.common.RetrofitClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class MapFragment extends Fragment implements OnMapReadyCallback
{
    private GoogleMap mMap;
    Context context;
    Activity parentActivity;
    ImageView ic_listview, ic_search, ic_filter;
    APIService mapiService;
    ProgressDialog m_Dialog;
    public List<PostList> postsLists = new ArrayList<>();
    SharedPreferences preferences;
    String token;
    HashMap<String, String> map;
    private GetLocation getLocation;
    double currentLat, curentLng;
    public static final int requestCode_Location = 80;
    public static final int requestCode_Filter_Data = 001;
    ArrayList<String> categoryids = new ArrayList<>();
    int distance = 50;
    String isFrom;
    static boolean isApiDataInprogress = false;
    CustomClusterManager<MarkerInfo> mClusterManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.map_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        parentActivity = getActivity();

        getLocation = new GetLocation(context);
        map = new HashMap<String, String>();
        ic_listview = view.findViewById(R.id.icon_gridview);
        ic_filter = view.findViewById(R.id.icon_filter);
        ic_search = view.findViewById(R.id.icon_search);
        preferences = context.getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
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
                Intent i = new Intent(context, FilterActivity.class);
                startActivityForResult(i, requestCode_Filter_Data);
            }
        });
        ic_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new OffersListFragment();
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container_top, fragment, "mapview");
                fragmentTransaction.commit();
            }
        });

        setUpMap();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected abstract void startCluster(List<PostList> mapData, LatLng location);

    protected GoogleMap getMap() {
        return mMap;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
        mMap = googleMap;
        if (mMap != null)
        {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style));

            LatLng latLng = getCurrentLocation();
            if(latLng != null) {
                getDataFromServer(distance, latLng.latitude, latLng.longitude);
            }


            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener()
            {
                @Override
                public void onCameraMove()
                {

                    VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();

                    float[] diagonalDistance = new float[1];

                    LatLng farLeft = visibleRegion.farLeft;
                    LatLng nearRight = visibleRegion.nearRight;

                    Location.distanceBetween(
                            farLeft.latitude,
                            farLeft.longitude,
                            nearRight.latitude,
                            nearRight.longitude,
                            diagonalDistance
                    );
                    distance = (int) (diagonalDistance[0] / 2);
                }
            });


            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition)
                {
//                    Log.e("ON_CAM_CHNGE",""+currentLat+", "+curentLng+", "+distance+", "+isApiDataInprogress+", "+cameraPosition.zoom);
                    if(!isApiDataInprogress) {
                        getDataFromServer(distance, cameraPosition.target.latitude, cameraPosition.target.longitude);
                    }else {
                        isApiDataInprogress = false;
                    }
                }
            });
        }
    }



    private void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }


    private LatLng getCurrentLocation()
    {
        if (getLocation.enabledLocation()) {
            getLocation.getUserLocation();
            currentLat = getLocation.getUserLatitude();
            curentLng = getLocation.getUserLongitude();

            return new LatLng(currentLat, curentLng);
        } else {
            getLocation.showSettingsAlert(context);
            return null;
        }
    }



    private void getDataFromServer(int radius, double lat, double lng)
    {
        map.put("radius", String.valueOf(radius));
        map.put("latitude", String.valueOf(lat));
        map.put("longitude", String.valueOf(lng));
        getAllUsersPosts(token, map);
    }

    private void getAllUsersPosts(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(context)) {
            isApiDataInprogress = true;
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<AllUserPostResponse> call = mapiService.getAllUserPosts(token, map);
//            m_Dialog = DialogUtils.showProgressDialog(context);
//            m_Dialog.show();
            call.enqueue(new Callback<AllUserPostResponse>() {
                @Override
                public void onResponse(Call<AllUserPostResponse> call, Response<AllUserPostResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getStatus())
                            {
                                AllPostsData getAllPostData = response.body().getData();
                                postsLists.clear();
                                postsLists.addAll(getAllPostData.getPostinfo().getPosts());
//                                m_Dialog.dismiss();
                                if (postsLists != null && postsLists.size() > 0) {
                                    mMap.clear();
                                    startCluster(postsLists, new LatLng(currentLat, curentLng));
                                }
                            } else {
                                Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                                m_Dialog.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllUserPostResponse> call, Throwable t) {
                    Log.e("MAP_SERVICE_FAIL",""+t.getMessage());
                    Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(context, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Filter Result", "" + requestCode + ", " + resultCode + ", " + data);
        if (requestCode == requestCode_Filter_Data) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                categoryids.clear();
                PostFilterData filterData;
                filterData = data.getParcelableExtra("CategoryValue");
//                ArrayList<PostFilterData> catIds = new ArrayList<>();
//                catIds.add(filterData.getValue());
                isFrom = data.getStringExtra("IsFrom");
                categoryids.addAll(filterData.getValue());

                HashMap<String, Object> map = new HashMap<>();
                map.put("searchtext", "");
                map.put("radius", data.getStringExtra("RangeValue"));  //data.getStringExtra("RangeOnMap")
                map.put("latitude", String.valueOf(currentLat));
                map.put("longitude", String.valueOf(curentLng));
                map.put("categoryids", categoryids);
                map.put("minprice", data.getStringExtra("MaxPrice"));
                map.put("maxprice", data.getStringExtra("MinPrice"));
                mMap.clear();
                applyFilter(token, map);
//                if (isFrom.equalsIgnoreCase("ApplyFiltter")) {
//
////                } else if (isFrom.equalsIgnoreCase("ClearFiltter")) {
////                    mMap.clear();
////                    getDataFromServer();
////                }
            }
        }
    }

    private void applyFilter(String token, HashMap<String, Object> map) {
        Log.e("FIL MAP", "" + map.toString());
        if (AppUtils.isInternetIsAvailable(context)) {
            Log.e("Filter Result", "" + map);
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<AllUserPostResponse> call = mapiService.applyFiltersbyCategoryID(token, map);
            m_Dialog = DialogUtils.showProgressDialog(context);
            m_Dialog.show();
            call.enqueue(new Callback<AllUserPostResponse>() {
                @Override
                public void onResponse(Call<AllUserPostResponse> call, Response<AllUserPostResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            AllPostsData data = response.body().getData();
                            if (data != null) {
                                AllPostInfo postInfo = data.getPostinfo();
                                if (postInfo != null) {
                                    if (postInfo.getPosts() != null) {
                                        postsLists.clear();
                                        postsLists.addAll(data.getPostinfo().getPosts());
                                        if (postsLists != null && postsLists.size() > 0) {
                                            startCluster(postsLists, new LatLng(currentLat, curentLng));
                                        }
                                    } else {
                                        Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            m_Dialog.dismiss();

                        } else {
                            Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllUserPostResponse> call, Throwable t) {
                    Log.e("FIL LOG", "" + t.getMessage());
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(context, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }


    class CustomClusterManager<T extends ClusterItem> extends ClusterManager<T>
    {
        CustomClusterManager(Context context, GoogleMap map) {
            super(context, map);
        }

        boolean itemsInSameLocation(Cluster<T> cluster) {
            LinkedList<T> items = new LinkedList<>(cluster.getItems());
            T item = items.remove(0);

            double longitude = item.getPosition().longitude;
            double latitude = item.getPosition().latitude;

            for (T t : items) {
                if (Double.compare(longitude, t.getPosition().longitude) != 0 && Double.compare(latitude, t.getPosition().latitude) != 0) {
                    return false;
                }
            }

            return true;
        }

        void removeItems(List<T> items) {

            for (T item : items) {
                removeItem(item);
            }
        }
    }


}
