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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.adapters.PostSelectForFilterAdapter;
import com.elintminds.mac.metatopos.beans.getfiltersCategoryList.FiltersData;
import com.elintminds.mac.metatopos.beans.getfiltersCategoryList.PostFilterData;
import com.elintminds.mac.metatopos.beans.getfiltersCategoryList.SearchFilterResponse;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectPostForFilterActivity extends AppCompatActivity implements View.OnClickListener, SelectedFilterCategoryListener {
    RecyclerView filter_Recyclerview;
    LinearLayoutManager linearLayoutManager;
    ImageView back_Btn;
    Button apply_btn;
    PostSelectForFilterAdapter postSelectForFilterAdapter;
    HashMap<String, String> map;
    APIService mapiService;
    ProgressDialog m_Dialog;
    private SharedPreferences preferences;
    private String token, categoryID;
    List<PostFilterData> postFilterData;
    PostFilterData filterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_post_for_filter);

        filter_Recyclerview = findViewById(R.id.filter_Recyclerview);
        back_Btn = findViewById(R.id.filterpost_back_btn);
        apply_btn = findViewById(R.id.apply_btn);
        apply_btn.setOnClickListener(this);
        back_Btn.setOnClickListener(this);

        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
        getFilterCategory(token);
    }

    @Override
    public void onClick(View view) {
        if (view == back_Btn) {
            finish();
        } else if (view == apply_btn) {
            Log.e("APPLY", "" + filterData.getValue());
            Intent intent = new Intent();
            intent.putExtra("FILTER_DATA", filterData);
            setResult(RESULT_OK, intent);
            finish();
        }


    }

    @Override
    public void onPostSubCategoryClick(int position) {
        filterData = postFilterData.get(position);
        Log.e("Filter Data", "" + filterData.getValue());

    }


    private void getFilterCategory(String token) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            Log.e("Follower List", "" + map);
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<SearchFilterResponse> call = mapiService.searchFilterCategoryList(token);
            m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<SearchFilterResponse>() {
                @Override
                public void onResponse(Call<SearchFilterResponse> call, Response<SearchFilterResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            m_Dialog.dismiss();
                            FiltersData filtersData = response.body().getData();
                            postFilterData = filtersData.getPosts();
                            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            filter_Recyclerview.setLayoutManager(linearLayoutManager);
                            filter_Recyclerview.scrollToPosition(0);
                            if (postFilterData != null) {
                                postSelectForFilterAdapter = new PostSelectForFilterAdapter(getApplicationContext(), postFilterData, SelectPostForFilterActivity.this);
                                filter_Recyclerview.setAdapter(postSelectForFilterAdapter);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SearchFilterResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
