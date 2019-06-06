package com.elintminds.mac.metatopos.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.adapters.MyProfileAdvertisementAdapter;
import com.elintminds.mac.metatopos.beans.useradvertisement.Advertisementdetail;
import com.elintminds.mac.metatopos.beans.useradvertisement.UserAdvertisementResponse;
import com.elintminds.mac.metatopos.beans.useradvertisement.UserAdvertisementdata;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileAdvertisementTabviewClass extends Fragment {
    APIService mapiService;
    RecyclerView tabs_container_recyclerview;
    MyProfileAdvertisementAdapter advertisementAdapter;
    SharedPreferences preferences;

    public MyProfileAdvertisementTabviewClass() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.other_profile_tab_fragment_layout, container, false);
        tabs_container_recyclerview = view.findViewById(R.id.tabs_container_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        tabs_container_recyclerview.setLayoutManager(gridLayoutManager);
        preferences = getActivity().getSharedPreferences("Prefrences", 0);
        String token = preferences.getString("User_Token", null);
        getMyAdvertisement(token);

        return view;
    }


    private void getMyAdvertisement(String token) {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<UserAdvertisementResponse> call = mapiService.getAdvertisement(token);
            final ProgressDialog m_Dialog = DialogUtils.showProgressDialog(getContext());
            m_Dialog.show();
            call.enqueue(new Callback<UserAdvertisementResponse>() {
                @Override
                public void onResponse(Call<UserAdvertisementResponse> call, Response<UserAdvertisementResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {

                            UserAdvertisementdata adsdata = response.body().getData();
                            List<Advertisementdetail> adslist = adsdata.getPostinfo().getPosts();
                            if (adslist != null && adslist.size() > 0) {
                                advertisementAdapter = new MyProfileAdvertisementAdapter(getContext(), adslist);
                                tabs_container_recyclerview.setAdapter(advertisementAdapter);
                            }
                            m_Dialog.dismiss();


                        } else {
                            Toast.makeText(getContext(), "" + response.body().getError(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserAdvertisementResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();

                }
            });
        } else {
            Toast.makeText(getContext(), "!No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }
}
