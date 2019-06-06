package com.elintminds.mac.metatopos.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.adapters.OtherProfileAdapter;
import com.elintminds.mac.metatopos.beans.userevents.EventDetails;
import com.elintminds.mac.metatopos.beans.userevents.UserEventdata;
import com.elintminds.mac.metatopos.beans.userevents.UserEventsResponse;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherProfileEventTabFragmentviewClass extends Fragment {

    APIService mapiService;
    OtherProfileAdapter adapter;
    SharedPreferences preferences;
    RecyclerView tabs_container_recyclerview;
    String token;
    HashMap<String, String> map = new HashMap<String, String>();

    public OtherProfileEventTabFragmentviewClass() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.other_profile_tab_fragment_layout, container, false);
        ArrayList<Integer> personImages = new ArrayList<>(Arrays.asList(R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man,
                R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man,
                R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man,
                R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman,
                R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man,
                R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man));
        tabs_container_recyclerview = view.findViewById(R.id.tabs_container_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        tabs_container_recyclerview.setLayoutManager(gridLayoutManager);
        preferences = getActivity().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
        Intent intent = getActivity().getIntent();
        String userID = intent.getStringExtra("USER_ID");
        map.put("userid", userID);
        getotherUserEvent(token, map);
        return view;
    }


    private void getotherUserEvent(String token, HashMap<String, String> userid) {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<UserEventsResponse> call = mapiService.getotherUserEvents(token, userid);
            final ProgressDialog m_Dialog = DialogUtils.showProgressDialog(getContext());
            m_Dialog.show();
            call.enqueue(new Callback<UserEventsResponse>() {
                @Override
                public void onResponse(Call<UserEventsResponse> call, Response<UserEventsResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            UserEventdata evnets_list = response.body().getEventData();
                            List<EventDetails> postlist = evnets_list.getPostinfo().getPosts();
                            Log.e("EVENT_DETAILS", "" + postlist);
                            Log.e("EVENT_DETAILS", "" + evnets_list);
                            adapter = new OtherProfileAdapter(getContext(), postlist);
                            tabs_container_recyclerview.setAdapter(adapter);
                            m_Dialog.dismiss();

                        } else {
                            Toast.makeText(getContext(), "" + response.body().getError(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserEventsResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();

                }
            });
        } else {
            Toast.makeText(getContext(), "!No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }
}
