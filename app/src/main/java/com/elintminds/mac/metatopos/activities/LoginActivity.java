package com.elintminds.mac.metatopos.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.selectlanguage.SelectLanguageData;
import com.elintminds.mac.metatopos.beans.sociallogin.SocialLoginResponse;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    Button btn_login;
    Button btn_create_account;
    ImageView app_logo;
    TextView welcome_text;
    TextView tv_privacy_policy, tv_term_condition;
    ImageView facebook_social_login, twitter_social_login, google_social_login;
    private LoginManager fbLoginManager;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int FACEBOOK_LOGIN_REQ = 101;
    private static final int GOOGLE_LOGIN_REQ = 102;
    private static final int TWITTER_LOGIN_REQ = 103;
    private TwitterAuthClient mTwitterAuthClient;

    private TwitterSession twitterSession;

    HashMap<String, String> map = new HashMap<String, String>();
    SharedPreferences preferences;

    APIService mapiService;
    Dialog dialog;
    LinearLayout languages_btn_container;
    LayoutInflater mInflater, layoutInflater;
    List<SelectLanguageData> language_list;
    private String previousUploadFile = "", user_ID, language_ID, imagePathNew, picturePath, picturePathURL;
    int selectionPosition = -1, i, childcount, previousTag = -1;
    View sbView, view;
    String device_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_login);

        initviews();
        viewsAnimation();
        updateFCMToken();
        callbackManager = CallbackManager.Factory.create();
        fbLoginManager = LoginManager.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }


    private void initviews() {
        btn_login = findViewById(R.id.btn_login);
        app_logo = findViewById(R.id.app_logo);
        welcome_text = findViewById(R.id.welcome_text);
        btn_create_account = findViewById(R.id.btn_create_account);
        tv_privacy_policy = findViewById(R.id.tv_privacy_policy);
        tv_term_condition = findViewById(R.id.tv_term_condition);
        facebook_social_login = findViewById(R.id.icon_facebook);
        twitter_social_login = findViewById(R.id.icon_twitter);
        google_social_login = findViewById(R.id.icon_google);
        btn_login.setOnClickListener(this);
        btn_create_account.setOnClickListener(this);
        facebook_social_login.setOnClickListener(this);
        twitter_social_login.setOnClickListener(this);
        google_social_login.setOnClickListener(this);
        tv_privacy_policy.setOnClickListener(this);
        tv_term_condition.setOnClickListener(this);

    }

    public void viewsAnimation() {
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down_anim);
        Animation faded = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.faded_anim);
        Animation slide_left_to_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_to_right_anim);
        Animation slide_right_to_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_to_left_anim);
        Animation zoom_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_anim);
        app_logo.startAnimation(slide_down);
        google_social_login.startAnimation(faded);
        facebook_social_login.startAnimation(slide_left_to_right);
        twitter_social_login.startAnimation(slide_right_to_left);
        btn_create_account.setAnimation(slide_left_to_right);
        btn_login.setAnimation(slide_right_to_left);
        welcome_text.setAnimation(zoom_out);
    }


    private void updateFCMToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("FIRE BASE", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        device_token = task.getResult().getToken();
                        Log.e("FIRE_BASE_TOKEN", device_token);
                        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("LOGGED_DEVICE_Token", device_token);
                        editor.commit();
                    }
                });
    }


    @Override
    public void onClick(View view) {

        if (view == btn_login) {
            Intent signin = new Intent(this, SigninActivity.class);
            startActivity(signin);
        } else if (view == btn_create_account) {
            Intent registerintent = new Intent(this, RegisterActivity.class);
            startActivity(registerintent);
        } else if (view == tv_privacy_policy) {
            Intent privacy_policy = new Intent(this, ReviewPrivacyPolicyActivity.class);
            startActivity(privacy_policy);
        } else if (view == tv_term_condition) {
            Intent term_condition = new Intent(this, TermContionsActivity.class);
            startActivity(term_condition);
        } else if (view == facebook_social_login) {
            if (AppUtils.isInternetIsAvailable(this)) {
                fbLoginManager.logInWithReadPermissions(this,
                        Arrays.asList("public_profile", "email"));
                doFacebookLogin();
            } else {
                Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            }
        } else if (view == google_social_login) {
            if (AppUtils.isInternetIsAvailable(this)) {
                doGoogleLogIn();
            } else {
                Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            }
        } else if (view == twitter_social_login) {
            if (AppUtils.isInternetIsAvailable(this)) {
                doTwitterLogin();
            } else {
                Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void doTwitterLogin() {
        mTwitterAuthClient = new TwitterAuthClient();
        mTwitterAuthClient.authorize(LoginActivity.this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterSession = result.data;
                Log.e("UserID", "" + twitterSession.getUserId() + "   UserName" + twitterSession.getUserName());
                Toast.makeText(LoginActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                fetchTwitterEmail();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    void fetchTwitterEmail() {

        mTwitterAuthClient.requestEmail(twitterSession, new com.twitter.sdk.android.core.Callback<String>() {
            @Override
            public void success(Result<String> result) {
                // Do something with the result, which provides the email address
                String twitterEmailD = result.data;
                Log.e("Email ID", "" + twitterEmailD);
                map.put("email", twitterEmailD);
                map.put("devicetoken", device_token);
                map.put("fullname", twitterSession.getUserName());
                doSocialLogin(map);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(LoginActivity.this, "Authentication Failured", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void doFacebookLogin() {
        fbLoginManager.registerCallback(callbackManager,

                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        if (AccessToken.getCurrentAccessToken() != null) {
                            RequestFacebookData();
                        }
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(LoginActivity.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void doGoogleLogIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_LOGIN_REQ);
    }

    private void getGoogleAccountInfo(GoogleSignInResult result) {
        GoogleSignInAccount acccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acccount != null) {
            String userName = acccount.getDisplayName();
            String userEmail = acccount.getEmail();
            HashMap<String, String> map1 = new HashMap<String, String>();
            Log.e("Username", "" + userName);
            Log.e("UserMail", "" + userEmail);
            map1.put("email", userEmail);
            map1.put("devicetoken", device_token);
            map.put("fullname", userName);
            doSocialLogin(map1);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == GOOGLE_LOGIN_REQ) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            getGoogleAccountInfo(result);
        }
        if (mTwitterAuthClient != null) {
            mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void RequestFacebookData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e("ProviderResponse", response.toString());
                        Log.e("ACCES_TOKEN", "" + AccessToken.getCurrentAccessToken());
                        JSONObject json = response.getJSONObject();
                        Log.e("FB JSON", "" + json);
                        try {
                            if (json != null) {
                                String socialEmail = json.getString("email");
                                String socialName = json.getString("first_name");
                                String fbID = json.getString("id");
                                Log.e("FB ID", "" + fbID);
                                Log.e("email", "" + socialEmail);
                                Log.e("name", "" + socialName);
                                if (fbID != null) {
                                    map.put("email", socialEmail);
                                    map.put("devicetoken", device_token);
                                    map.put("fullname", socialName);
                                    doSocialLogin(map);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "" + connectionResult.toString(), Toast.LENGTH_SHORT).show();
    }


    private void doSocialLogin(HashMap<String, String> map) {
        mapiService = RetrofitClient.getClient().create(APIService.class);
        Call<SocialLoginResponse> call = mapiService.Sociallogin(map);
        final ProgressDialog m_Dialog = DialogUtils.showProgressDialog(this);
        m_Dialog.show();
        call.enqueue(new Callback<SocialLoginResponse>() {
            @Override
            public void onResponse(Call<SocialLoginResponse> call, Response<SocialLoginResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == true) {
                        Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        String deviceToken = response.body().getData().getDeviceTokenID();
                        String token = response.body().getData().getToken();
                        String userID = response.body().getData().getUserID();
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("User_Token", token);
                        editor.putString("LOGGED_UDERID", userID);
                        editor.putString("LOGGED_DEVICE_Token", deviceToken);
                        editor.putBoolean("Logged", true);
                        editor.commit();
                        gotoHome();
                        m_Dialog.dismiss();
                    } else {
                        Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        m_Dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<SocialLoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, R.string.no_internet_connection + t.getMessage(), Toast.LENGTH_SHORT).show();
                m_Dialog.dismiss();
            }
        });
    }

    private void gotoHome() {
        Intent i = new Intent(LoginActivity.this, SocialLoginImageAndEmojiActivity.class);
        startActivity(i);
        finish();
    }


}