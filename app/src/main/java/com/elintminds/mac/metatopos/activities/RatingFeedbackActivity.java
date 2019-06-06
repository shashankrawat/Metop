package com.elintminds.mac.metatopos.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.adapters.FeedbackAdapter;
import com.elintminds.mac.metatopos.beans.feedback.FeedbackData;
import com.elintminds.mac.metatopos.beans.feedback.FeedbackResponse;
import com.elintminds.mac.metatopos.beans.feedback.FeedbacksList;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingFeedbackActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView feedback_recyclrview;
    ImageView back_btn;
    APIService mapiService;
    ProgressDialog m_Dialog;
    Button submit_btn;
    EditText ed_add_feedback;
    RatingBar ratingBar;
    ImageView iv_gemMoji;
    RelativeLayout inner_lay;
    CircleImageView iv_profile_image;
    HashMap<String, String> map = new HashMap<String, String>();
    SharedPreferences preferences;
    LinearLayoutManager linearLayoutManager;
    FeedbackAdapter feedbackAdapter;
    String token, userID, otherUserId;
    String is_from;
    public static boolean IS_APP_OPEN = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_feedback);
        initviews();
        Intent i = getIntent();
        otherUserId = i.getStringExtra("USER_ID");
        is_from = i.getStringExtra("IS_FROM");
        if (is_from.equals("PROFILE_FRAGMENT")) {
            inner_lay.setVisibility(View.GONE);
            submit_btn.setVisibility(View.GONE);
        } else if (is_from.equals("Other_Profile")) {
            inner_lay.setVisibility(View.VISIBLE);
            submit_btn.setVisibility(View.VISIBLE);
        } else if (is_from.equalsIgnoreCase("Notification_Fragment")) {
            inner_lay.setVisibility(View.VISIBLE);
            submit_btn.setVisibility(View.VISIBLE);
        } else if (is_from.equalsIgnoreCase("NOTIFICATIONS")) {
            inner_lay.setVisibility(View.GONE);
            submit_btn.setVisibility(View.GONE);
        }
        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
        map.put("userid", otherUserId);
        getFeedbacks(token, map);
    }

    private void initviews() {
        back_btn = findViewById(R.id.rating_feebback_back_btn);
        submit_btn = findViewById(R.id.submit_btn);
        ratingBar = findViewById(R.id.rating_bar);
        ed_add_feedback = findViewById(R.id.ed_add_feedback);
        iv_profile_image = findViewById(R.id.iv_profile_image);
        iv_gemMoji = findViewById(R.id.iv_gemMoji);
        inner_lay = findViewById(R.id.inner_lay);
        feedback_recyclrview = findViewById(R.id.feedback_recyclerview);
        submit_btn.setOnClickListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        feedback_recyclrview.setLayoutManager(linearLayoutManager);
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == back_btn) {
            finish();
        } else if (view == submit_btn) {
            if (ratingBar.getRating() == 0) {
                Toast.makeText(this, "Please add  your rating", Toast.LENGTH_SHORT).show();
            } else if (ed_add_feedback.getText().toString().equals("")) {
                Toast.makeText(this, "Please add your feedback", Toast.LENGTH_SHORT).show();
                ed_add_feedback.requestFocus();
            } else {
                map.put("userid", userID);
                map.put("starrating", String.valueOf(ratingBar.getRating()));
                map.put("feedbackmessage", ed_add_feedback.getText().toString());
                giveFeedBack(token, map);
            }

        }
    }

    private void getFeedbacks(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<FeedbackResponse> call = mapiService.getFeedbacks(token, map);
            m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<FeedbackResponse>() {
                @Override
                public void onResponse(Call<FeedbackResponse> call, Response<FeedbackResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {

                            FeedbackData feedbackData = response.body().getData();
                            userID = feedbackData.getUserData().getUserID();
                            Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + feedbackData.getUserData().getProfileImageUrl());
                            Uri path2 = Uri.parse("https://www.metatopos.elintminds.work/" + feedbackData.getUserData().getMojiImageUrl());
                            Glide.with(getApplicationContext()).load(path).into(iv_profile_image);
                            Glide.with(getApplicationContext()).load(path2).into(iv_gemMoji);
                            List<FeedbacksList> otherUserFeedbackData = feedbackData.getFeedbacksData();
                            feedbackAdapter = new FeedbackAdapter(getApplicationContext(), otherUserFeedbackData);
                            feedback_recyclrview.setAdapter(feedbackAdapter);
                            m_Dialog.dismiss();


                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }

                }

                @Override
                public void onFailure(Call<FeedbackResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();

                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void giveFeedBack(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<FeedbackResponse> call = mapiService.giveFeedback(token, map);
            m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<FeedbackResponse>() {
                @Override
                public void onResponse(Call<FeedbackResponse> call, Response<FeedbackResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            ed_add_feedback.setText("");
                            ratingBar.setRating(0);
                            m_Dialog.dismiss();

                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }

                }

                @Override
                public void onFailure(Call<FeedbackResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();

                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();


        }
    }

}
