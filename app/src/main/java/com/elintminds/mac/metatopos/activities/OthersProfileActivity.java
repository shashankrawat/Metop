package com.elintminds.mac.metatopos.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.adapters.OtherProfileTabsAdapter;
import com.elintminds.mac.metatopos.beans.editprofile.FollowResponse;
import com.elintminds.mac.metatopos.beans.login.LoginData;
import com.elintminds.mac.metatopos.beans.login.LoginResponse;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OthersProfileActivity extends AppCompatActivity implements View.OnClickListener, OtherUserProfileInterface {

    OtherProfileTabsAdapter otherProfileTabsAdapter;
    TabLayout otherProfileTablayout;
    ViewPager tabViewPager;
    ImageView iv_otherprofile_backbtn, iv_genEmoji;
    TextView tv_username, tv_useremail, tv_totalfollowers, tv_totalfollowing, tv_totalreview;
    Button btn_chat, btn_follow, btn_Unfollow;
    LinearLayout follow_lay, following_lay;
    RatingBar user_ratings;
    CircleImageView iv_user_dp;
    APIService mapiService;
    SharedPreferences preferences;
    ProgressDialog m_Dialog;
    LoginData profiledata;
    boolean isFollowed = false;
    String token, userID, total_post_count, total_event_count, total_ads_count, logged_UserID, CurrentPostUserID;
    HashMap<String, String> map = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);

        initViews();
        Intent intent = getIntent();
        String userID = intent.getStringExtra("USER_ID");
        otherProfileTabsAdapter = new OtherProfileTabsAdapter(getSupportFragmentManager());
        tabViewPager.setAdapter(otherProfileTabsAdapter);
        otherProfileTablayout.setupWithViewPager(tabViewPager);
        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
        logged_UserID = preferences.getString("LOGGED_UDERID", null);
        otherUserProfileinfo(token, userID);

        setSystemBarsTransparentFully(this);
    }

    private void initViews() {
        otherProfileTablayout = findViewById(R.id.otherprofile_tablayout);
        tabViewPager = findViewById(R.id.otherprofile_viewpager);
        iv_otherprofile_backbtn = findViewById(R.id.other_profile_backbtn);
        iv_user_dp = findViewById(R.id.iv_user_dp);
        iv_genEmoji = findViewById(R.id.iv_emoji);
        tv_username = findViewById(R.id.tv_username);
        tv_useremail = findViewById(R.id.tv_weburl);
        tv_totalfollowers = findViewById(R.id.tv_follow);
        user_ratings = findViewById(R.id.userratings);
        tv_totalfollowing = findViewById(R.id.tv_following);
        tv_totalreview = findViewById(R.id.total_reviews);
        btn_chat = findViewById(R.id.chat_btn);
        btn_follow = findViewById(R.id.follow_btn);
        btn_Unfollow = findViewById(R.id.unfollow_btn);
        follow_lay = findViewById(R.id.followers_layout);
        following_lay = findViewById(R.id.following_layout);
        following_lay.setOnClickListener(this);
        follow_lay.setOnClickListener(this);
        iv_otherprofile_backbtn.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        btn_follow.setOnClickListener(this);
        btn_Unfollow.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        if (view == iv_otherprofile_backbtn) {
            finish();
        } else if (view == btn_chat) {
            startActivity(new Intent(this, ChatActivity.class));
        } else if (view == btn_follow) {
//            map.put("status", "1");
//            map.put("userid", userID);
            if (CurrentPostUserID.equals(logged_UserID)) {

                Toast.makeText(OthersProfileActivity.this, "You can't follow yourself", Toast.LENGTH_SHORT).show();

            } else {
                if (isFollowed) {
                    followUser(token, "0", userID);

                } else {

                    followUser(token, "1", userID);

                }
            }
//            if (isFollowed) {
//                map.put("status", "0");
//            } else {
//                map.put("status", "1");
//            }
//            map.put("userid", userID);
//            followUser(token, map);
        } else if (view == follow_lay) {
            Intent followIntent = new Intent(this, FollowersActivity.class);
            followIntent.putExtra("Userid", userID);
            startActivity(followIntent);
        } else if (view == following_lay) {
            Intent followingIntent = new Intent(this, FollowingActivity.class);
            followingIntent.putExtra("Userid", userID);
            startActivity(followingIntent);
        }
//        else if (view == btn_Unfollow) {
//            map.put("status", "0");
//            map.put("userid", userID);
//            followUser(token, map);
//            btn_Unfollow.setVisibility(View.GONE);
//            btn_follow.setVisibility(View.VISIBLE);
//        }

    }

    @Override
    public void onFollowSuccess() {

        isFollowed = !isFollowed;

        if (isFollowed) {
            btn_follow.setText("Unfollow");
        } else {
            btn_follow.setText("Follow");
        }

    }

    private void otherUserProfileinfo(String token, String userid) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<LoginResponse> call = mapiService.getotherUerProfile(token, userid);
            m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            profiledata = response.body().getData();
                            Log.e("Other_User_Profile_Data", "" + profiledata);
                            userID = profiledata.getUserID();
                            total_post_count = profiledata.getTotolPosts();
                            total_event_count = profiledata.getTotolEvents();
                            total_ads_count = profiledata.getTotolAdvertisements();
                            CurrentPostUserID = profiledata.getUserID();
                            TabLayout.Tab tab = otherProfileTablayout.getTabAt(0);
                            TabLayout.Tab tab2 = otherProfileTablayout.getTabAt(1);
                            TabLayout.Tab tab3 = otherProfileTablayout.getTabAt(2);
                            tab.setText("Post" + "(" + total_post_count + ")");
                            tab2.setText("Event" + "(" + total_event_count + ")");
                            tab3.setText("Advertisement" + "(" + total_ads_count + ")");
                            if (profiledata.getIsAlreadyFollowed().equals("1")) {
//                            btn_Unfollow.setVisibility(View.VISIBLE);
//                            btn_follow.setVisibility(View.GONE);
                                isFollowed = true;
                                btn_follow.setText("Unfollow");

                            } else {
//                            btn_Unfollow.setVisibility(View.GONE);
//                            btn_follow.setVisibility(View.VISIBLE);
                                isFollowed = false;
                                btn_follow.setText("Follow");

                            }

                            showprofileData();
                            m_Dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void showprofileData() {
        String getprofile, getemoji;
        Uri profilepicpath, emojipath;
        tv_username.setText(profiledata.getFullName());
        tv_useremail.setText(profiledata.getEmail());
        tv_totalfollowers.setText(profiledata.getTotolFollowers());
        tv_totalfollowing.setText(profiledata.getTotolFollowings());
        tv_totalreview.setText("reviews(" + profiledata.getTotolReviews() + ")");
        user_ratings.setRating(Float.parseFloat(profiledata.getAverageStarRating()));
        getprofile = profiledata.getProfileImageUrl();
        getemoji = profiledata.getMojiImageUrl();
        profilepicpath = Uri.parse(APIService.BASE_URL + getprofile);
        emojipath = Uri.parse(APIService.BASE_URL + getemoji);
        Glide.with(getApplicationContext()).load(profilepicpath).into(iv_user_dp);
        Glide.with(getApplicationContext()).load(emojipath).into(iv_genEmoji);

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
                            Toast.makeText(OthersProfileActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

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


    public static void setSystemBarsTransparentFully(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
                }
            }
        }
    }


}
