package com.elintminds.mac.metatopos.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.adapters.FollowingAdapter;
import com.elintminds.mac.metatopos.beans.editprofile.FollowResponse;
import com.elintminds.mac.metatopos.beans.getfollowerslist.FollowersListData;
import com.elintminds.mac.metatopos.beans.getfollowerslist.FollowersResponse;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingActivity extends AppCompatActivity implements View.OnClickListener, FollowingUserInterface, OtherUserProfileInterface {
    RecyclerView following_Reccyclerview;
    ImageView back_Btn;
    FollowingAdapter followingAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Integer> data;
    HashMap<String, String> map;
    private SharedPreferences preferences;
    private String token, userId;
    List<FollowersListData> followingdata;
    APIService mapiService;
    ProgressDialog m_Dialog;
    int itemPosition;
    boolean isFollowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);


        following_Reccyclerview = findViewById(R.id.following_Reccyclerview);
        back_Btn = findViewById(R.id.following_back_btn);
        back_Btn.setOnClickListener(this);

        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
        Intent i = getIntent();
        userId = i.getStringExtra("Userid");
        map = new HashMap<>();
        map.put("userid", userId);
        getfollowingList(token, map);
    }

    @Override
    public void onClick(View view) {
        if (view == back_Btn) {
            finish();
        }
    }

    private void getfollowingList(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            Log.e("Following List", "" + map);
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<FollowersResponse> call = mapiService.getFollowingList(token, map);
            m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<FollowersResponse>() {
                @Override
                public void onResponse(Call<FollowersResponse> call, Response<FollowersResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            followingdata = response.body().getData();
                            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            following_Reccyclerview.setLayoutManager(linearLayoutManager);
                            following_Reccyclerview.scrollToPosition(0);
                            if (followingdata != null && followingdata.size() > 0) {
                                followingAdapter = new FollowingAdapter(getApplicationContext(), followingdata, FollowingActivity.this);
                                following_Reccyclerview.setAdapter(followingAdapter);
                            }
                            m_Dialog.dismiss();


                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<FollowersResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }


    private void followUser(String token, String isfollowvalue, String userID) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            Log.e("Follow", "" + isfollowvalue);
            map = new HashMap<>();
            map.put("status", isfollowvalue);
            map.put("userid", userID);
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<FollowResponse> call = mapiService.followUnfollowUser(token, map);
            call.enqueue(new Callback<FollowResponse>() {
                @Override
                public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Toast.makeText(FollowingActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            onFollowSuccess();
                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<FollowResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onFollowSuccess() {
        isFollowed = !isFollowed;

        if (isFollowed) {
            followingdata.get(itemPosition).getUserInfo().setIsAlreadyFollowed("1");
        } else {
            followingdata.get(itemPosition).getUserInfo().setIsAlreadyFollowed("0");
        }
        followingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFollowFollowingClick(int position) {

        itemPosition = position;
        if (isFollowed) {
            followUser(token, "0", followingdata.get(itemPosition).getFollowerId());

        } else {

            followUser(token, "1", followingdata.get(itemPosition).getFollowerId());

        }

    }
}
