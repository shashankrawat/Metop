package com.elintminds.mac.metatopos.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.AdvertisementPlanActivity;
import com.elintminds.mac.metatopos.activities.ChangePasswordActivity;
import com.elintminds.mac.metatopos.activities.EditProfileActivity;
import com.elintminds.mac.metatopos.activities.FavouriteItemActivity;
import com.elintminds.mac.metatopos.activities.FollowersActivity;
import com.elintminds.mac.metatopos.activities.FollowingActivity;
import com.elintminds.mac.metatopos.activities.LoginActivity;
import com.elintminds.mac.metatopos.activities.MyPostEventAdsActivity;
import com.elintminds.mac.metatopos.activities.RatingFeedbackActivity;
import com.elintminds.mac.metatopos.activities.ReportIssueActivity;
import com.elintminds.mac.metatopos.activities.ReviewPrivacyPolicyActivity;
import com.elintminds.mac.metatopos.activities.TermContionsActivity;
import com.elintminds.mac.metatopos.beans.login.LoginData;
import com.elintminds.mac.metatopos.beans.login.LoginResponse;
import com.elintminds.mac.metatopos.beans.register.UpdateLanguageResponse;
import com.elintminds.mac.metatopos.beans.selectlanguage.SelectLanguageData;
import com.elintminds.mac.metatopos.beans.selectlanguage.SelectLanguageResponse;
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

public class ProfileFragmentClass extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener
{
    public static final String TAG = "ProfileFragmentClass";
    View view;
    Context context;
    TextView tv_post_event_advs, tv_rating_feedback, tv_edit_profile, tv_change_password, tv_terms_conditions, tv_change_lang, tv_advs_plan, tv_privacy_policies, tv_report_issue, tv_logout,
            username, emailurl, total_followers, total_following, total_review, total_post, total_ads, total_event, tv_favourite_items;
    LinearLayout favourite_item, followers_Lay, following_lay;
    RelativeLayout Share_with_friend_lay;
    Switch push_notifications_switch;
    ImageView iv_Emoji;
    RatingBar user_rating;
    CircleImageView iv_user_dp;
    Dialog dialog;
    Button edit_btn;
    APIService mapiService;
    ProgressDialog m_Dialog;
    SharedPreferences preferences;
    String token, device_token;
    LoginData profiledata;
    int i, childcount;
    int selectionPosition = -1;
    LayoutInflater layoutInflater;
    LinearLayout languages_btn_container;
    String userid, language_ID, imagePathNew;
    List<SelectLanguageData> language_list;
    int previousTag = -1;
    HashMap<String, String> map = new HashMap<String, String>();
    boolean isnotificationenabled = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_profile, container, false);
        initviews();
        preferences = getActivity().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
        device_token = preferences.getString("LOGGED_DEVICE_Token", null);
        Log.e("FCM_TOKEN", "" + device_token);
        userProfileinfo(token);
        getLanguages();
        return view;
    }

    private void initviews() {
        tv_post_event_advs = view.findViewById(R.id.post_event_advertisement);
        favourite_item = view.findViewById(R.id.favourite_item);
        tv_rating_feedback = view.findViewById(R.id.rating_feedback);
        tv_edit_profile = view.findViewById(R.id.edit_profile);
        tv_change_password = view.findViewById(R.id.change_password);
        tv_terms_conditions = view.findViewById(R.id.term_conditions);
        tv_change_lang = view.findViewById(R.id.change_language);
        tv_advs_plan = view.findViewById(R.id.advertisement_plan);
        tv_privacy_policies = view.findViewById(R.id.privacy_policies);
        tv_report_issue = view.findViewById(R.id.report_issue);
        tv_logout = view.findViewById(R.id.logout);
        Share_with_friend_lay = view.findViewById(R.id.Share_with_friend_lay);
        push_notifications_switch = view.findViewById(R.id.push_notifications_switch);
        iv_user_dp = view.findViewById(R.id.iv_user_dp);
        iv_Emoji = view.findViewById(R.id.iv_emoji);
        username = view.findViewById(R.id.tv_username);
        emailurl = view.findViewById(R.id.tv_weburl);
        total_followers = view.findViewById(R.id.tv_follow);
        total_following = view.findViewById(R.id.tv_folloers);
        total_review = view.findViewById(R.id.tv_total_reviews);
        total_post = view.findViewById(R.id.tv_post);
        total_ads = view.findViewById(R.id.tv_advertisement);
        total_event = view.findViewById(R.id.tv_event);
        user_rating = view.findViewById(R.id.ratings);
        tv_favourite_items = view.findViewById(R.id.tv_favourite_items);
        followers_Lay = view.findViewById(R.id.followers_lay);
        following_lay = view.findViewById(R.id.following_lay);
        edit_btn = view.findViewById(R.id.edit_btn);
        followers_Lay.setOnClickListener(this);
        following_lay.setOnClickListener(this);
        tv_post_event_advs.setOnClickListener(this);
        tv_change_password.setOnClickListener(this);
        favourite_item.setOnClickListener(this);
        tv_rating_feedback.setOnClickListener(this);
        tv_edit_profile.setOnClickListener(this);
        tv_change_lang.setOnClickListener(this);
        tv_advs_plan.setOnClickListener(this);
        tv_report_issue.setOnClickListener(this);
        tv_privacy_policies.setOnClickListener(this);
        tv_terms_conditions.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        push_notifications_switch.setOnCheckedChangeListener(this);
        Share_with_friend_lay.setOnClickListener(this);


    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked == true) {
            map.put("isNotificationEnabled", "1");
            ChangeUserSettings(token, map);
            isnotificationenabled = true;
        } else if (isChecked == false) {
            map.put("isNotificationEnabled", "0");
            ChangeUserSettings(token, map);
            isnotificationenabled = false;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == followers_Lay) {
            Intent followersIntent = new Intent(getContext(), FollowersActivity.class);
            followersIntent.putExtra("userid", userid);
            startActivity(followersIntent);
        } else if (view == following_lay) {
            Intent followingIntent = new Intent(getContext(), FollowingActivity.class);
            followingIntent.putExtra("userid", userid);
            startActivity(followingIntent);
        } else if (view == Share_with_friend_lay) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String referlink = profiledata.getAppShareLink();
                i.putExtra(Intent.EXTRA_TEXT, referlink);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
                e.toString();
            }
        } else if (view == tv_post_event_advs) {
            Intent myIntent = new Intent(getContext(), MyPostEventAdsActivity.class);
            myIntent.putExtra("Total_Post", profiledata.getTotolPosts());
            myIntent.putExtra("Total_Event", profiledata.getTotolEvents());
            myIntent.putExtra("Total_Advertisement", profiledata.getTotolAdvertisements());
            startActivity(myIntent);
        } else if (view == favourite_item) {
            Intent favitem_intent = new Intent(getContext(), FavouriteItemActivity.class);
            startActivity(favitem_intent);
        } else if (view == tv_rating_feedback) {
            Intent ratingfeedback = new Intent(getContext(), RatingFeedbackActivity.class);
            ratingfeedback.putExtra("User_ID", userid);
            ratingfeedback.putExtra("IS_FROM", "PROFILE_FRAGMENT");
            startActivity(ratingfeedback);
        } else if (view == tv_edit_profile) {
            Intent editprofile = new Intent(getContext(), EditProfileActivity.class);
            editprofile.putExtra("USER_PROFILE_DATA", profiledata);
            startActivity(editprofile);
        } else if (view == tv_change_password) {
            Intent changepassword = new Intent(getContext(), ChangePasswordActivity.class);
            startActivity(changepassword);
        } else if (view == tv_report_issue) {
            Intent reportissue = new Intent(getContext(), ReportIssueActivity.class);
            startActivity(reportissue);
        } else if (view == tv_privacy_policies) {
            Intent privacypolicies = new Intent(getContext(), ReviewPrivacyPolicyActivity.class);
            startActivity(privacypolicies);
        } else if (view == tv_terms_conditions) {
            Intent termcondition = new Intent(getContext(), TermContionsActivity.class);
            startActivity(termcondition);
        } else if (view == tv_change_lang) {
            showselectlangDialog();
        } else if (view == tv_advs_plan) {
            Intent ads_plan = new Intent(getContext(), AdvertisementPlanActivity.class);
            startActivity(ads_plan);
        } else if (view == tv_logout) {
            map.put("devicetoken", device_token);
            logout(token, map);
        }
    }

    private void showselectlangDialog() {
        ImageView closebtn;
        Button done_btn;

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.select_language_popup);
        dialog.show();

        closebtn = dialog.findViewById(R.id.close_btn);
        done_btn = dialog.findViewById(R.id.btn_Done);
        languages_btn_container = dialog.findViewById(R.id.languages_btn_container);
        layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
//        TextView textView=view.findViewById(R.id.tv_show_language);
        for (i = 0; i < language_list.size(); i++) {
            // Add the text layout to the parent layout
            view = layoutInflater.inflate(R.layout.language_item_view, languages_btn_container, false);
            boolean islanguageSelected = false;
            final TextView tv_language = view.findViewById(R.id.tv_show_language);
            tv_language.setText(language_list.get(i).getDESCRIPTION());
            Log.e("hghjssf", "" + language_list);
            languages_btn_container.addView(tv_language);
            languageSelectClickListener();
        }

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                map.put("languageId", language_ID);
                map.put("userId", userid);
                updateLanguage(map);

            }
        });
    }

    private void userProfileinfo(String token) {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<LoginResponse> call = mapiService.getUserProfile(token);
            m_Dialog = DialogUtils.showProgressDialog(getContext());
            m_Dialog.show();
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            profiledata = response.body().getData();
                            Log.e("User_Profile_Data", "" + response.body().getData());
                            tv_favourite_items.setText("(" + profiledata.getTotalFavouritePosts() + ")");
                            userid = profiledata.getUserID();
                            m_Dialog.dismiss();
                            showprofileData();

                        } else {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();

        }
    }

    private void showprofileData() {
        String getprofile, getemoji;
        Uri profilepicpath, emojipath;
        username.setText(profiledata.getFullName());
        emailurl.setText(profiledata.getEmail());
        total_followers.setText(profiledata.getTotolFollowers());
        total_following.setText(profiledata.getTotolFollowings());
        total_review.setText("reviews(" + profiledata.getTotolReviews() + ")");
        total_post.setText(profiledata.getTotolPosts());
        total_ads.setText(profiledata.getTotolAdvertisements());
        total_event.setText(profiledata.getTotolEvents());
        user_rating.setRating(Float.parseFloat(profiledata.getAverageStarRating()));
        getprofile = profiledata.getProfileImageUrl();
        getemoji = profiledata.getMojiImageUrl();
        profilepicpath = Uri.parse(APIService.BASE_URL + getprofile);
        emojipath = Uri.parse(APIService.BASE_URL + getemoji);
        Glide.with(getContext()).load(profilepicpath).into(iv_user_dp);
        Glide.with(getContext()).load(emojipath).into(iv_Emoji);
        if (profiledata.getIsNotificationEnabled().equals("1")) {
            push_notifications_switch.setChecked(true);
            isnotificationenabled = true;
        } else {
            push_notifications_switch.setChecked(false);
            isnotificationenabled = false;

        }

    }

    private void getLanguages() {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            Call<SelectLanguageResponse> call = mapiService.selectLanguageResponse();
            call.enqueue(new Callback<SelectLanguageResponse>() {
                @Override
                public void onResponse(Call<SelectLanguageResponse> call, Response<SelectLanguageResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Log.e("Language Data", "" + response.body().getData().getLanguages());
                            language_list = response.body().getData().getLanguages();
                        } else {
                            Toast.makeText(getContext(), "" + response.body().getError(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }

                }

                @Override
                public void onFailure(Call<SelectLanguageResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();


                }
            });
        } else {
            Toast.makeText(getContext(), "!No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

    private void languageSelectClickListener() {
        int count = languages_btn_container.getChildCount();
        if (count > 0) {
            for (childcount = 0; childcount < count; childcount++) {
                final View childView = languages_btn_container.getChildAt(childcount);
                childView.setTag(childcount);
                childView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (previousTag != -1) {
                            languages_btn_container.getChildAt(previousTag).setSelected(false);
                        }
                        previousTag = (int) view.getTag();
                        childView.setSelected(true);
                        Log.e("LanguageID", "" + language_list.get(previousTag).getSUBCODE());
                        language_ID = language_list.get(previousTag).getSUBCODE();

                    }

                });
            }
        }
    }

    private void updateLanguage(HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<UpdateLanguageResponse> call = mapiService.updateLanguage(map);
            m_Dialog = DialogUtils.showProgressDialog(getContext());
            m_Dialog.show();
            call.enqueue(new Callback<UpdateLanguageResponse>() {
                @Override
                public void onResponse(Call<UpdateLanguageResponse> call, Response<UpdateLanguageResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Log.e("Update_Language", "" + response.body().getMessage());
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();

                        } else {
                            Log.e("UPdate_Language_Error", "" + response.body().getMessage());
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpdateLanguageResponse> call, Throwable t) {
                    Log.e("OnFailed", "" + t.getMessage());
                    Toast.makeText(getContext(), "Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
            dialog.dismiss();
        } else {
            Toast.makeText(getContext(), "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void logout(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<LoginResponse> call = mapiService.logout(token, map);
            m_Dialog = DialogUtils.showProgressDialog(getContext());
            m_Dialog.show();
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.commit();
                            Log.d("Logout", "Now log out and start the activity login");
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            m_Dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();

        }
    }

    private void ChangeUserSettings(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<LoginResponse> call = mapiService.changeUserSettings(token, map);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {


                        } else {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();

        }
    }


}
