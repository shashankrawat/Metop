package com.elintminds.mac.metatopos.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.addnewpost.BuyOrSellFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.FoodGroceryDeatilsFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.JobDetailFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.OtherPostdetailsFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.ServicesDetailsFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.SportsEntertainmentDetailsFormActivity;
import com.elintminds.mac.metatopos.adapters.AdvertisementAdapter;
import com.elintminds.mac.metatopos.adapters.PostImagesSliderAdapter;
import com.elintminds.mac.metatopos.beans.getpost.GetPostByIdAttachment;
import com.elintminds.mac.metatopos.beans.getpost.GetPostByIdData;
import com.elintminds.mac.metatopos.beans.getpost.GetPostByIdRecentComment;
import com.elintminds.mac.metatopos.beans.getpost.GetPostByIdResponse;
import com.elintminds.mac.metatopos.beans.removepost.RemovePostResponse;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppConstants;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.GetLocation;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoveAndEditPostActivtiy extends AppCompatActivity implements View.OnClickListener {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    public static final int requestCode_Location = 80;
    private static final Integer[] IMAGES = {R.drawable.sample, R.drawable.sample, R.drawable.sampleimg};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    Handler handler;
    RecyclerView recyclerView;
    AdvertisementAdapter advertisementAdapter;
    LinearLayoutManager linearLayoutManager;
    RelativeLayout ratingandreview;
    CircleImageView profileImage, addcomment_userdp;
    ImageView profileEmoji;
    TextView userName, postTime, totalViews, totalComment, totalReviews, postPrice, postTitle, postDescription, postAddress, tv_send_comment, tv_distance;
    RatingBar userRating;
    String postid;
    Button edit_PostBtn, remove_PostBtn;
    Dialog dialog;
    APIService mapiService;
    ProgressDialog m_Dialog;
    HashMap<String, String> map = new HashMap<String, String>();
    SharedPreferences preferences;
    ArrayList<GetPostByIdData> postdata;
    List<GetPostByIdAttachment> attachments;
    List<GetPostByIdRecentComment> comments;
    private GetLocation getLocation;
    double currentLat, curentLng, postLat, postLng;
    String token, postCategoryID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_and_edit_post_activtiy);
        getLocation = new GetLocation(getApplicationContext());
        initView();
        Intent intent = getIntent();
        postid = intent.getStringExtra("POST_ID");
        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
        map.put("postid", postid);
        getPostByPostID(token, map);
        checkForPermissionLocation(this);
    }

    private void initView() {
        profileImage = findViewById(R.id.removepost_userprofile_pic);
        profileEmoji = findViewById(R.id.remove_post_userEmoji);
        userName = findViewById(R.id.user_name);
        postTime = findViewById(R.id.posttime);
        totalViews = findViewById(R.id.tv_viewss);
        totalComment = findViewById(R.id.tv_conversation);
        totalReviews = findViewById(R.id.remove_post_review);
        userRating = findViewById(R.id.ratings);
        postPrice = findViewById(R.id.post_price);
        postTitle = findViewById(R.id.post_title);
        postDescription = findViewById(R.id.post_Description);
        postAddress = findViewById(R.id.tv_address);
        ratingandreview = findViewById(R.id.ratingandreview);
        tv_send_comment = findViewById(R.id.tv_send_comment);
        tv_distance = findViewById(R.id.tv_distance);
        edit_PostBtn = findViewById(R.id.edit_Post_btn);
        remove_PostBtn = findViewById(R.id.remove_Post_Btn);
        mPager = findViewById(R.id.pager);
        addcomment_userdp = findViewById(R.id.addcomment_userdp);
        recyclerView = findViewById(R.id.makeoffer_recyclerview);
        ratingandreview = findViewById(R.id.ratingandreview);
        ratingandreview.setOnClickListener(this);
        edit_PostBtn.setOnClickListener(this);
        remove_PostBtn.setOnClickListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.scrollToPosition(0);
//        advertisementAdapter = new AdvertisementAdapter(this, co);
//        recyclerView.setAdapter(advertisementAdapter);
        Toolbar tb = (Toolbar) findViewById(R.id.make_offer_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tb.setTitle("");
        tb.setSubtitle("");
        if (tb != null) {
            setSupportActionBar(tb);
            tb.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        setSystemBarsTransparentFully(this);
    }


    @Override
    public void onClick(View view) {
        if (view == ratingandreview) {
            Intent i = new Intent(this, RatingFeedbackActivity.class);
//            i.putParcelableArrayListExtra("Edit_Post_Data", postdata);
//            startActivity(i);
        } else if (view == edit_PostBtn) {
            if (postCategoryID.equals("1")) {
                Intent i = new Intent(this, BuyOrSellFormActivity.class);
                i.putExtra("Edit_Post_Data", postdata.get(0));
                i.putExtra("isFromRemoveEditActivity", "BuyOrSell");
                startActivity(i);
            } else if (postCategoryID.equals("2")) {
                Intent i = new Intent(this, ServicesDetailsFormActivity.class);
                i.putExtra("Edit_Post_Data", postdata.get(0));
                i.putExtra("isFromRemoveEditActivity", "Services");
                startActivity(i);
            } else if (postCategoryID.equals("3")) {
                Intent i = new Intent(this, FoodGroceryDeatilsFormActivity.class);
                i.putExtra("Edit_Post_Data", postdata.get(0));
                i.putExtra("isFromRemoveEditActivity", "Food_and_Grocery");
                startActivity(i);
            } else if (postCategoryID.equals("4")) {
                Intent i = new Intent(this, JobDetailFormActivity.class);
                i.putExtra("Edit_Post_Data", postdata.get(0));
                i.putExtra("isFromRemoveEditActivity", "Jobs");
                startActivity(i);
            } else if (postCategoryID.equals("5")) {
                Intent i = new Intent(this, SportsEntertainmentDetailsFormActivity.class);
                i.putExtra("Edit_Post_Data", postdata.get(0));
                i.putExtra("isFromRemoveEditActivity", "SportsEntertainment");
                startActivity(i);
            } else if (postCategoryID.equals("8")) {
                Intent i = new Intent(this, OtherPostdetailsFormActivity.class);
                i.putExtra("Edit_Post_Data", postdata.get(0));
                i.putExtra("isFromRemoveEditActivity", "OtherProfile");
                startActivity(i);
            }

        } else if (view == remove_PostBtn) {
            map.put("postid", postid);
            removePostByID(token, map);

        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void getPostByPostID(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<GetPostByIdResponse> call = mapiService.getPostByID(token, map);
            m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<GetPostByIdResponse>() {
                @Override
                public void onResponse(Call<GetPostByIdResponse> call, Response<GetPostByIdResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            postdata = response.body().getpostByIdData();
                            attachments = postdata.get(0).getAttachments();
                            Log.e("Attachment", "" + attachments);
                            comments = postdata.get(0).getComments().getRecentComment();
                            postCategoryID = postdata.get(0).getSuperCategoryId();
                            imageslider();
                            displayUserData();
                            linearLayoutManager.setAutoMeasureEnabled(true);
                            recyclerView.setLayoutManager(linearLayoutManager);
//                        postCommentAdapter = new PostCommentAdapter(getApplicationContext(), comments);
//                        recyclerView.setAdapter(postCommentAdapter);
                            m_Dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }

                }

                @Override
                public void onFailure(Call<GetPostByIdResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();

                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayUserData() {
        Uri profilepath = Uri.parse("https://www.metatopos.elintminds.work/" + postdata.get(0).getUserinfo().getProfileImageUrl());
        Uri emojipath = Uri.parse("https://www.metatopos.elintminds.work/" + postdata.get(0).getUserinfo().getMojiImageUrl());
        Glide.with(getApplicationContext()).load(profilepath).into(profileImage);
        Glide.with(getApplicationContext()).load(emojipath).into(profileEmoji);
        userName.setText(postdata.get(0).getUserinfo().getFullName());
        String lastActiveDate = AppConstants.convertLastActiveDate(postdata.get(0).getAddedOn());
        postTime.setText("Ad posted " + lastActiveDate);
        totalViews.setText(postdata.get(0).getViewCount());
        totalComment.setText(postdata.get(0).getComments().getTotalComment());
        totalReviews.setText("reviews(" + postdata.get(0).getUserinfo().getTotolReviews() + ")");
        postPrice.setText("$" + postdata.get(0).getPrice());
        userRating.setRating(Float.parseFloat(postdata.get(0).getUserinfo().getAverageStarRating()));
        postTitle.setText(postdata.get(0).getTitle());
        postDescription.setText(postdata.get(0).getDescription());
        postAddress.setText(postdata.get(0).getAddress());
        postLat = Double.parseDouble(postdata.get(0).getLatitude());
        postLng = Double.parseDouble(postdata.get(0).getLongitude());
        double earthRadius = 6371; //in kilo meters
        double dLat = Math.toRadians(currentLat - postLat);
        double dLng = Math.toRadians(curentLng - postLng);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(currentLat)) * Math.cos(Math.toRadians(postLat)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);
        tv_distance.setText((int) dist + " Km away");


    }

    private void imageslider() {
        mPager.setAdapter(new PostImagesSliderAdapter(RemoveAndEditPostActivtiy.this, attachments));
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.dip2px(10);
        NUM_PAGES = IMAGES.length;
        handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {
            }
        });
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
            } else {
                Toast.makeText(getApplicationContext(), "You have denied access location request", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void alertdialogMesg() {
        AlertDialog.Builder mybulider = new AlertDialog.Builder(getApplicationContext());
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

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You have denied access location request", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void removePostByID(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<RemovePostResponse> call = mapiService.removePost(token, map);
            m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<RemovePostResponse>() {
                @Override
                public void onResponse(Call<RemovePostResponse> call, Response<RemovePostResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Toast.makeText(RemoveAndEditPostActivtiy.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }

                }

                @Override
                public void onFailure(Call<RemovePostResponse> call, Throwable t) {
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
