package com.elintminds.mac.metatopos.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.FilterActivity;
import com.elintminds.mac.metatopos.adapters.OffersListAdapter;
import com.elintminds.mac.metatopos.beans.getallpostsbylatlong.AllPostInfo;
import com.elintminds.mac.metatopos.beans.getallpostsbylatlong.AllPostsData;
import com.elintminds.mac.metatopos.beans.getallpostsbylatlong.AllUserPostResponse;
import com.elintminds.mac.metatopos.beans.getallpostsbylatlong.PostList;
import com.elintminds.mac.metatopos.beans.getfiltersCategoryList.PostFilterData;
import com.elintminds.mac.metatopos.beans.getpostsbysearchfilters.FilterPostData;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.GetLocation;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersListFragment extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    public static final int requestCode_Location = 80;
    private static final int Filter_REQUEST = 200;
    OffersListAdapter offersListAdapter;
    ImageView icon_map, icon_filter;
    EditText ed_Search;
    APIService mapiService;
    ProgressDialog m_Dialog;
    HashMap<String, String> map = new HashMap<String, String>();
    public List<PostList> postsLists = new ArrayList<>();
    public List<FilterPostData> searchlist = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    SharedPreferences preferences;
    String token;
    private GetLocation getLocation;
    double currentLat, curentLng;
    ArrayList<String> categoryids = new ArrayList<>();
    List<Address> addresses = null;
    String city, state, country, zipcode, lat, lng;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_offers_in_grid_layout, container, false);
        recyclerView = view.findViewById(R.id.offers_recyclerview);
        icon_map = view.findViewById(R.id.icon_map);
        icon_filter = view.findViewById(R.id.icon_filter);
        ed_Search = view.findViewById(R.id.ed_search_filter);
        gridLayoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        offersListAdapter = new OffersListAdapter(getContext(), postsLists);
        recyclerView.setAdapter(offersListAdapter);
        getLocation = new GetLocation(getContext());
        icon_filter.setOnClickListener(this);
        icon_map.setOnClickListener(this);
        preferences = getContext().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
        checkForPermissionLocation(getContext());


        ed_Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchText = ed_Search.getText().toString();
                    if (!searchText.equals("") || searchText.length() > 4) {
//                            map.put("search", searchText);
//                            map.put("pagenum", "1");
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("searchtext", searchText);
                        map.put("radius", "50");  //data.getStringExtra("RangeOnMap")
                        map.put("latitude", String.valueOf(currentLat));
                        map.put("longitude", String.valueOf(curentLng));
                        map.put("categoryids", "");
                        map.put("minprice", "");
                        map.put("maxprice", "");
                        map.put("fromdate", "");
                        map.put("todate", "");
                        applyFilter(token, map);
                    } else if (searchText.equals("")) {
                        getPostByCurrentLatlong();
                    }
                    return true;
                }
                return false;
            }
        });
        ed_Search.addTextChangedListener(new TextWatcher() {
            final android.os.Handler handler = new android.os.Handler();
            Runnable runnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(runnable);
                if (s.toString().trim().length() == 0) {
                    ed_Search.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_search, 0);

                } else {

                    ed_Search.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                runnable = new Runnable() {
                    @Override
                    public void run() {

                        //do some work with s.toString()
                        String searchText = ed_Search.getText().toString();
                        if (!searchText.equals("") || searchText.length() > 4) {
//                            map.put("search", searchText);
//                            map.put("pagenum", "1");
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("searchtext", searchText);
                            map.put("radius", "50");  //data.getStringExtra("RangeOnMap")
                            map.put("latitude", String.valueOf(currentLat));
                            map.put("longitude", String.valueOf(curentLng));
                            map.put("categoryids", "");
                            map.put("minprice", "");
                            map.put("maxprice", "");
                            map.put("fromdate", "");
                            map.put("todate", "");
                            applyFilter(token, map);
                        } else if (searchText.equals("")) {
                            getPostByCurrentLatlong();
                        }
                    }
                };
                handler.postDelayed(runnable, 4000);


            }
        });


        return view;


    }


    @Override
    public void onClick(View view) {
        if (view == icon_filter) {
            Intent i = new Intent(getContext(), FilterActivity.class);
            startActivityForResult(i, Filter_REQUEST);
        } else if (view == icon_map) {
            Fragment fragment = new ClusterFrag();
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_top, fragment, "mapview");
            fragmentTransaction.commit();
        }


    }

    private void getPostByCurrentLatlong() {
        map.put("radius", "50");
        map.put("latitude", String.valueOf(currentLat));
        map.put("longitude", String.valueOf(curentLng));
        getAllUsersPosts(token, map);
    }

    private void getAllUsersPosts(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<AllUserPostResponse> call = mapiService.getAllUserPosts(token, map);
            m_Dialog = DialogUtils.showProgressDialog(getContext());
            m_Dialog.show();
            call.enqueue(new Callback<AllUserPostResponse>() {
                @Override
                public void onResponse(Call<AllUserPostResponse> call, Response<AllUserPostResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            AllPostsData getAllPostData = response.body().getData();
                            postsLists.addAll(getAllPostData.getPostinfo().getPosts());
                            Log.e("POST_ALLDATA", "" + getAllPostData);
                            Log.e("POST_list", "" + postsLists);
                            if (postsLists.size() > 0) {
                                offersListAdapter.notifyDataSetChanged();
                            }
                            m_Dialog.dismiss();

                        } else {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllUserPostResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();

        }
    }

    private void getSearchFilterResult(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<AllUserPostResponse> call = mapiService.searchFilter(token, map);
            m_Dialog = DialogUtils.showProgressDialog(getContext());
            m_Dialog.show();
            call.enqueue(new Callback<AllUserPostResponse>() {
                @Override
                public void onResponse(Call<AllUserPostResponse> call, Response<AllUserPostResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            AllPostsData data = response.body().getData();
                            if (data != null) {
                                AllPostInfo postInfo = data.getPostinfo();
                                if (postInfo != null) {
                                    if (postInfo.getPosts() != null) {
                                        postsLists.clear();
                                        postsLists.addAll(data.getPostinfo().getPosts());
                                        offersListAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(getContext(), R.string.no_data_found, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            m_Dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllUserPostResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();

        }
    }

    private void checkForPermissionLocation(Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                alertdialogMesg();
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode_Location);
            }
        } else {
            if (getLocation.enabledLocation()) {
                getLocation.getUserLocation();
                currentLat = getLocation.getUserLatitude();
                curentLng = getLocation.getUserLongitude();
                getLocation.getAddressLocation(currentLat, curentLng);
                getPostByCurrentLatlong();
            } else {
                Toast.makeText(getContext(), R.string.you_have_denied_access_location_permission, Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void alertdialogMesg() {
        AlertDialog.Builder mybulider = new AlertDialog.Builder(getContext());
        mybulider.setTitle("Alert Message");
        mybulider.setMessage(R.string.Alert_Explanation_Location);
        mybulider.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode_Location);
            }
        });
        AlertDialog ad = mybulider.create();
        ad.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case requestCode_Location: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (getLocation.enabledLocation()) {
                        getLocation.getUserLocation();
                        currentLat = getLocation.getUserLatitude();
                        curentLng = getLocation.getUserLongitude();
                        getPostByCurrentLatlong();

                    }
                } else {
                    Toast.makeText(getContext(), R.string.you_have_denied_access_location_permission, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("OFFER RESULT", "" + requestCode + ", " + resultCode + ", " + data);
        if (requestCode == Filter_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                categoryids.clear();
                offersListAdapter.notifyDataSetChanged();
                PostFilterData filterData;
                filterData = data.getParcelableExtra("CategoryValue");
                ArrayList<List<String>> catIds = new ArrayList<>();
                catIds.add(filterData.getValue());
                categoryids.addAll(filterData.getValue());
                Log.e("GETRangeValue", "" + data.getStringExtra("RangeValue"));
                HashMap<String, Object> map = new HashMap<>();
                map.put("searchtext", "");
                map.put("radius", data.getStringExtra("RangeValue"));
                map.put("latitude", String.valueOf(currentLat));
                map.put("longitude", String.valueOf(curentLng));
                map.put("categoryids", categoryids);
                map.put("minprice", data.getStringExtra("MaxPrice"));
                map.put("maxprice", data.getStringExtra("MinPrice"));
                map.put("fromdate", data.getStringExtra("StartData"));
                map.put("todate", data.getStringExtra("EndDate"));
                applyFilter(token, map);

            }
        }
    }

    private void applyFilter(String token, HashMap<String, Object> map) {
        Log.e("FIL MAP", "" + map.toString());
        if (AppUtils.isInternetIsAvailable(getContext())) {
            Log.e("Follower List", "" + map);
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<AllUserPostResponse> call = mapiService.applyFiltersbyCategoryID(token, map);
            m_Dialog = DialogUtils.showProgressDialog(getContext());
            m_Dialog.show();
            call.enqueue(new Callback<AllUserPostResponse>() {
                @Override
                public void onResponse(Call<AllUserPostResponse> call, Response<AllUserPostResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            AllPostsData data = response.body().getData();
                            if (data != null) {
                                AllPostInfo postInfo = data.getPostinfo();
                                if (postInfo != null) {
                                    if (postInfo.getPosts() != null) {
                                        postsLists.clear();
                                        postsLists.addAll(data.getPostinfo().getPosts());
                                        offersListAdapter.notifyDataSetChanged();

                                    } else {
                                        Toast.makeText(getContext(), R.string.no_data_found, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            m_Dialog.dismiss();

                        } else {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }

    }

}
