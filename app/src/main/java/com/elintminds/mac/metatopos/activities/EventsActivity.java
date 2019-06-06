package com.elintminds.mac.metatopos.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.adapters.EventAdapter;
import com.elintminds.mac.metatopos.adapters.EventImagesSliderAdapter;
import com.elintminds.mac.metatopos.beans.addcomment.AddCommentResponse;
import com.elintminds.mac.metatopos.beans.addremocefavoritepost.AddRemoveFvrtPostResponse;
import com.elintminds.mac.metatopos.beans.getevents.EventAttachments;
import com.elintminds.mac.metatopos.beans.getevents.GetEventByIdData;
import com.elintminds.mac.metatopos.beans.getevents.GetEventByIdResponse;
import com.elintminds.mac.metatopos.beans.getpost.GetPostByIdRecentComment;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppConstants;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.GetLocation;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsActivity extends AppCompatActivity implements View.OnClickListener, EventActivityInterface {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    public static final int requestCode_Location = 80;
    private static final Integer[] IMAGES = {R.drawable.sample, R.drawable.sample, R.drawable.sampleimg};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    Handler handler;
    RecyclerView recyclerView;
    EventAdapter eventAdapter;
    LinearLayoutManager linearLayoutManager;
    RelativeLayout ratingandreview;
    ArrayList<Integer> data = new ArrayList<>(Arrays.asList(R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman));
    CircleImageView profileImage;
    ImageView profileEmoji;
    private TextView userName, postTime, totalViews, totalComment, totalIntrestedCount, totalReviews, postTitle, postDescription, postAddress, tv_distance;
    private RatingBar userRating;
    TextView send_comment;
    EditText ed_add_comment;
    CircleImageView add_comment_profile_pic;
    APIService mapiService;
    ProgressDialog m_Dialog;
    HashMap<String, String> map, map2;
    SharedPreferences preferences;
    List<GetEventByIdData> eventdata;
    boolean isLike = false;
    String token, postID, logged_UserID, currentPostUserID;
    Button btn_Intrested, btn_NotIntrested;
    List<EventAttachments> attachments;
    List<GetPostByIdRecentComment> comments;
    private GetLocation getLocation;
    double currentLat, curentLng, postLat, postLng;
    private MenuItem menuitem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);


        initViews();
        setSystemBarsTransparentFully(this);
        getLocation = new GetLocation(getApplicationContext());
        Intent getPostIDIntent = getIntent();
        postID = getPostIDIntent.getStringExtra("PostID");
        Log.e("POSTID", "" + postID);
        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
        logged_UserID = preferences.getString("LOGGED_UDERID", null);
        map2 = new HashMap<String, String>();
        map = new HashMap<String, String>();
        map2.put("postid", postID);
        getEventData(token, map2);
        checkForPermissionLocation(this);


    }

    private void initViews() {
        profileImage = findViewById(R.id.userprofile_pic);
        profileEmoji = findViewById(R.id.profile_Emoji);
        userName = findViewById(R.id.user_name);
        postTime = findViewById(R.id.posttime);
        totalViews = findViewById(R.id.tv_event_viewss);
        totalComment = findViewById(R.id.tv_conversation);
        totalIntrestedCount = findViewById(R.id.tv_intrested_people);
        totalReviews = findViewById(R.id.eventsrcReview);
        userRating = findViewById(R.id.eventratings);
        postTitle = findViewById(R.id.event_Title);
        postDescription = findViewById(R.id.event_Des);
        postAddress = findViewById(R.id.tv_event_Address);
        recyclerView = findViewById(R.id.makeoffer_recyclerview);
        ratingandreview = findViewById(R.id.ratingandreview);
        btn_Intrested = findViewById(R.id.btn_Intrested);
        btn_NotIntrested = findViewById(R.id.btn_NotGoing);
        send_comment = findViewById(R.id.tv_send_comment);
        ed_add_comment = findViewById(R.id.ed_add_comment);
        add_comment_profile_pic = findViewById(R.id.addcomment_userdp);
        tv_distance = findViewById(R.id.tv_distance);

        mPager = (ViewPager) findViewById(R.id.pager);

        btn_NotIntrested.setOnClickListener(this);
        btn_Intrested.setOnClickListener(this);
        ratingandreview.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        send_comment.setOnClickListener(this);


        Toolbar tb = findViewById(R.id.make_offer_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
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

        ed_add_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (ed_add_comment.getText().toString().length() > 1) {
                    send_comment.setVisibility(View.VISIBLE);
                } else if (ed_add_comment.getText().toString().length() == 0) {
                    send_comment.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar tb = (Toolbar) findViewById(R.id.make_offer_toolbar);
        tb.inflateMenu(R.menu.makeoffer_menu);
        menuitem = menu.findItem(R.id.like);
        menuitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item == menuitem)
                    if (currentPostUserID.equals(logged_UserID)) {

                        Toast.makeText(EventsActivity.this, "You can't add this post to favorite list because you are the owner of this post", Toast.LENGTH_SHORT).show();

                    } else {
                        if (isLike) {

                            addToFvrtPost(token, "0", postID);

                        } else {

                            addToFvrtPost(token, "1", postID);

                        }
                    }
                return onOptionsItemSelected(item);
            }

        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.like:
                return true;
            case R.id.make_offer_toolbar:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    @Override
    public void onClick(View view) {
        String userid = eventdata.get(0).getUserID();
        if (view == ratingandreview) {
            Intent i = new Intent(this, RatingFeedbackActivity.class);
            i.putExtra("USER_ID", userid);
            i.putExtra("IS_FROM", "Other_Profile");
            startActivity(i);
        } else if (view == profileImage) {

            Intent otherProfileintent = new Intent(this, OthersProfileActivity.class);
            otherProfileintent.putExtra("USER_ID", userid);
            startActivity(otherProfileintent);
        } else if (view == btn_Intrested) {
            map.put("status", "1");
            map.put("postid", eventdata.get(0).getPostId());
            intrestedForEvent(token, map);
        } else if (view == btn_NotIntrested) {
            map.put("status", "0");
            map.put("postid", eventdata.get(0).getPostId());
            intrestedForEvent(token, map);

        } else if (view == send_comment) {
            map.put("postid", postID);
            map.put("comment", ed_add_comment.getText().toString());
            aDDComment(token, map);
        }

    }


    private void getEventData(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<GetEventByIdResponse> call = mapiService.getEventByID(token, map);
            m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<GetEventByIdResponse>() {
                @Override
                public void onResponse(Call<GetEventByIdResponse> call, Response<GetEventByIdResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            eventdata = response.body().getData();
                            Log.e("GETPOSTBYID", "" + response.body().getData());
                            attachments = eventdata.get(0).getAttachments();
                            comments = eventdata.get(0).getComments().getRecentComment();
                            currentPostUserID = eventdata.get(0).getUserID();
                            if (eventdata.get(0).getIsFavorites().equals("Y")) {
                                isLike = true;
                                menuitem.setIcon(getResources().getDrawable(R.drawable.ic_like));
                            } else {
                                isLike = false;
                                menuitem.setIcon(getResources().getDrawable(R.drawable.ic_remove_from_favorite_post));
                            }
                            if (currentPostUserID.equals(logged_UserID)) {
                                ratingandreview.setVisibility(View.GONE);
                            } else {
                                ratingandreview.setVisibility(View.VISIBLE);
                            }
                            imageslider();
                            displayUserData();
                            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.scrollToPosition(0);
                            eventAdapter = new EventAdapter(getApplicationContext(), comments);
                            recyclerView.setAdapter(eventAdapter);
                            m_Dialog.dismiss();

                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }

                }

                @Override
                public void onFailure(Call<GetEventByIdResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayUserData() {
        Uri profilepath = Uri.parse("https://www.metatopos.elintminds.work/" + eventdata.get(0).getUserinfo().getProfileImageUrl());
        Uri emojipath = Uri.parse("https://www.metatopos.elintminds.work/" + eventdata.get(0).getUserinfo().getMojiImageUrl());
        Glide.with(getApplicationContext()).load(profilepath).into(profileImage);
        Glide.with(getApplicationContext()).load(emojipath).into(profileEmoji);
        Uri addcommentuserpath = Uri.parse("https://www.metatopos.elintminds.work/" + eventdata.get(0).getLogin_userinfo().getProfileImageUrl());
        Glide.with(getApplicationContext()).load(addcommentuserpath).into(add_comment_profile_pic);
        userName.setText(eventdata.get(0).getUserinfo().getFullName());
        String lastActiveDate = AppConstants.convertLastActiveDate(eventdata.get(0).getAddedOn());
        postTime.setText("Ad posted " + lastActiveDate);
        totalViews.setText(eventdata.get(0).getViewCount());
        totalComment.setText(eventdata.get(0).getComments().getTotalComment());
        totalIntrestedCount.setText(eventdata.get(0).getTotalInterestedPeople());
        totalReviews.setText("reviews(" + eventdata.get(0).getUserinfo().getTotolReviews() + ")");
        userRating.setRating(Float.parseFloat(eventdata.get(0).getUserinfo().getAverageStarRating()));
        postTitle.setText(eventdata.get(0).getTitle());
        postDescription.setText(eventdata.get(0).getDescription());
        postAddress.setText(eventdata.get(0).getAddress());

        postLat = Double.parseDouble(eventdata.get(0).getLatitude());
        postLng = Double.parseDouble(eventdata.get(0).getLongitude());

        double earthRadius = 6371; //in kilo meters
        double dLat = Math.toRadians(currentLat - postLat);
        double dLng = Math.toRadians(curentLng - postLng);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(currentLat)) * Math.cos(Math.toRadians(postLat)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);
        tv_distance.setText((int) dist + " Km away");
    }

    private void imageslider() {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);


        mPager.setAdapter(new EventImagesSliderAdapter(EventsActivity.this, attachments));


        CircleIndicator indicator =
                findViewById(R.id.indicator);
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
        }, 3000, 3000);

        // Pager listener over indicator
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

    private void addToFvrtPost(String token, String likeVaue, String postID) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            map.put("status", likeVaue);
            map.put("postid", postID);
            Call<AddRemoveFvrtPostResponse> call = mapiService.addRemoveFvrtPost(token, map);
            call.enqueue(new Callback<AddRemoveFvrtPostResponse>() {
                @Override
                public void onResponse(Call<AddRemoveFvrtPostResponse> call, Response<AddRemoveFvrtPostResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Toast.makeText(EventsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            onLikeSuccess();

                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddRemoveFvrtPostResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();


                }
            });

        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void intrestedForEvent(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            Call<AddRemoveFvrtPostResponse> call = mapiService.intrestedForEventorNot(token, map);
//        m_Dialog = DialogUtils.showProgressDialog(this, "Wait while loading");
//        m_Dialog.show();
            call.enqueue(new Callback<AddRemoveFvrtPostResponse>() {
                @Override
                public void onResponse(Call<AddRemoveFvrtPostResponse> call, Response<AddRemoveFvrtPostResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Toast.makeText(EventsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

//                        m_Dialog.dismiss();

                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                        m_Dialog.dismiss();
                        }
                    }

                }

                @Override
                public void onFailure(Call<AddRemoveFvrtPostResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                m_Dialog.dismiss();

                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void aDDComment(final String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            Call<AddCommentResponse> call = mapiService.addComment(token, map);
            m_Dialog = DialogUtils.showProgressDialog(this);
            m_Dialog.show();
            call.enqueue(new Callback<AddCommentResponse>() {
                @Override
                public void onResponse(Call<AddCommentResponse> call, Response<AddCommentResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            m_Dialog.dismiss();
                            ed_add_comment.setText("");
                            getEventData(token, map2);
                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddCommentResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();

                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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


    @Override
    public void onLikeSuccess() {
        isLike = !isLike;
        if (isLike) {
            menuitem.setIcon(getResources().getDrawable(R.drawable.ic_like));
        } else {
            menuitem.setIcon(getResources().getDrawable(R.drawable.ic_remove_from_favorite_post));
        }
    }

}
