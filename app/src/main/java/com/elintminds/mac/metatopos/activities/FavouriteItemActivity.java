package com.elintminds.mac.metatopos.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.adapters.FavouriteItemAdapter;
import com.elintminds.mac.metatopos.beans.favouritepost.FavouritePostData;
import com.elintminds.mac.metatopos.beans.favouritepost.FavouritePostResponse;
import com.elintminds.mac.metatopos.beans.favouritepost.FavouritePosts;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteItemActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView favourite_item_backbtn;
    RecyclerView favouriteItemRecyclerview;
    FavouriteItemAdapter favouriteItemAdapter;
    APIService mapiService;
    SharedPreferences preferences;
    ProgressDialog m_Dialog;
    ArrayList<Integer> itemImages = new ArrayList<>(Arrays.asList(R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man,
            R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man,
            R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man,
            R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman,
            R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man,
            R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_woman, R.drawable.ic_genmoji_man));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        favourite_item_backbtn = findViewById(R.id.favourite_item_backbtn);
        favouriteItemRecyclerview = findViewById(R.id.favourite_item_recyclerview);
        favourite_item_backbtn.setOnClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        favouriteItemRecyclerview.setLayoutManager(gridLayoutManager);
        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        String token = preferences.getString("User_Token", null);
        getfavouritePosts(token);

    }


    @Override
    public void onClick(View view) {
        if (view == favourite_item_backbtn) {
            finish();
        }
    }

    private void getfavouritePosts(String token) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<FavouritePostResponse> call = mapiService.getfavouriteposts(token);
            m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<FavouritePostResponse>() {
                @Override
                public void onResponse(Call<FavouritePostResponse> call, Response<FavouritePostResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            FavouritePostData fvrtPostData = response.body().getData();
                            List<FavouritePosts> dataList = fvrtPostData.getPostinfo().getPosts();
                            Log.e("DATA", "" + dataList.size());
                            if (dataList != null)
                                favouriteItemAdapter = new FavouriteItemAdapter(getApplicationContext(), dataList);
                            Log.e("Get Post Time", response.body().getData().getPostinfo().getPosts().get(0).getAddedOn());
                            favouriteItemRecyclerview.setAdapter(favouriteItemAdapter);
                            m_Dialog.dismiss();

                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<FavouritePostResponse> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();

                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


}