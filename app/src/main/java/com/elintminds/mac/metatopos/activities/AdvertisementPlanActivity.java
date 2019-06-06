package com.elintminds.mac.metatopos.activities;

import android.app.ProgressDialog;
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
import com.elintminds.mac.metatopos.adapters.AdvertisementPlanAdapter;
import com.elintminds.mac.metatopos.beans.advertisementplan.AdvertisementPlanBean;
import com.elintminds.mac.metatopos.beans.advertisementplan.AdvertisementPlanResponse;
import com.elintminds.mac.metatopos.beans.advertisementplan.PlansData;
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

public class AdvertisementPlanActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView advertisement_plan_backbtn;
    RecyclerView plan_recyclerview;
    AdvertisementPlanAdapter advertisementPlanAdapter;
    ArrayList<AdvertisementPlanBean> data;
    APIService mapiService;
    ProgressDialog m_Dialog;
    HashMap<String, String> map = new HashMap<String, String>();
    SharedPreferences preferences;
    int[] colorCode = {R.drawable.plan_gradient_green, R.drawable.plan_gradient_blue, R.drawable.plan_gradient_purple, R.drawable.plan_gradient_red};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_plan);
        advertisement_plan_backbtn = findViewById(R.id.advertisementplan_backbtn);
        plan_recyclerview = findViewById(R.id.plan_recyclerview);
        advertisement_plan_backbtn.setOnClickListener(this);
        data = new ArrayList<AdvertisementPlanBean>();
        data.add(new AdvertisementPlanBean("Plan 1", R.drawable.plan_gradient_green));
        data.add(new AdvertisementPlanBean("Plan 2", R.drawable.plan_gradient_blue));
        data.add(new AdvertisementPlanBean("Plan 3", R.drawable.plan_gradient_purple));
        data.add(new AdvertisementPlanBean("Plan 4", R.drawable.plan_gradient_red));
        data.add(new AdvertisementPlanBean("Unlimited Plan", R.drawable.plan_gradient_green));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        plan_recyclerview.setLayoutManager(layoutManager);
        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        String token = preferences.getString("User_Token", null);
        getAdvertisementPlanList(token);

    }

    @Override
    public void onClick(View view) {
        if (view == advertisement_plan_backbtn) {
            finish();
        }
    }

    private void getAdvertisementPlanList(String token) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<AdvertisementPlanResponse> call = mapiService.getUserAdvertisementPlan(token);
            m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<AdvertisementPlanResponse>() {
                @Override
                public void onResponse(Call<AdvertisementPlanResponse> call, Response<AdvertisementPlanResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            List<PlansData> planDatalist = response.body().getData();
                            Log.e("PlanDataLIST", "" + response.body().getData().size());
                            advertisementPlanAdapter = new AdvertisementPlanAdapter(getApplicationContext(), planDatalist);
                            plan_recyclerview.setAdapter(advertisementPlanAdapter);
                            m_Dialog.dismiss();

                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }

                }

                @Override
                public void onFailure(Call<AdvertisementPlanResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();

                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
