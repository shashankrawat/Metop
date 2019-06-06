package com.elintminds.mac.metatopos.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.selectlanguage.SelectLanguageData;
import com.elintminds.mac.metatopos.beans.selectlanguage.SelectLanguageResponse;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.List;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    Context mContext;
    SharedPreferences sharedPreferences;
    ImageView applogo, app_name;
    APIService mapiService;
    Dialog dialog;
    LinearLayout languages_btn_container;
    LayoutInflater mInflater, layoutInflater;
    List<SelectLanguageData> language_list;
    private String previousUploadFile = "", user_ID, language_ID, imagePathNew, picturePath, picturePathURL;
    int selectionPosition = -1, i, childcount, previousTag = -1;
    View sbView, view;
    String[] languages = {"English", "Arabic"};
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        mContext = this;
        applogo = findViewById(R.id.applogo);
        app_name = findViewById(R.id.app_name);

        sharedPreferences = getSharedPreferences("Prefrences", MODE_PRIVATE);
        Animation logo_anim = AnimationUtils.loadAnimation(this, R.anim.splash_slide_down_anim);
        Animation text_anim = AnimationUtils.loadAnimation(this, R.anim.zoom_out_splash_screen_anim);
        applogo.startAnimation(logo_anim);
        app_name.startAnimation(text_anim);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (sharedPreferences.getBoolean("Logged", false)) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                } else {
                    showselectlangDialog();
//                    getLanguages();
                }
            }
        }, 2000);

    }

    private void showselectlangDialog() {
        ImageView closebtn;
        Button done_btn;
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_language_popup);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        closebtn = dialog.findViewById(R.id.close_btn);
        done_btn = dialog.findViewById(R.id.btn_Done);
        languages_btn_container = dialog.findViewById(R.id.languages_btn_container);
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
//        TextView textView=view.findViewById(R.id.tv_show_language);


        for (i = 0; i < languages.length; i++) {
            // Add the text layout to the parent layout
            view = layoutInflater.inflate(R.layout.language_item_view, languages_btn_container, false);
            boolean islanguageSelected = false;
            final TextView tv_language = view.findViewById(R.id.tv_show_language);
//                                tv_language.setText(language_list.get(i).getDESCRIPTION());
            tv_language.setText(languages[i]);
            languages_btn_container.addView(tv_language);
        }
        final View childView = languages_btn_container.getChildAt(childcount);
        languageSelectClickListener();
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                map.put("languageId", language_ID);
//                map.put("userId", user_ID);
//                updateLanguage(map);
                if (language_ID != null && !language_ID.equalsIgnoreCase("")) {
                    startActivity(new Intent(SplashActivity.this, AppIntroSliderctivity.class));
                    finish();
                    dialog.dismiss();
                } else {
                    Toast.makeText(mContext, getString(R.string.select_language_error), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void getLanguages() {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<SelectLanguageResponse> call = mapiService.selectLanguageResponse();
            call.enqueue(new Callback<SelectLanguageResponse>() {
                @Override
                public void onResponse(Call<SelectLanguageResponse> call, Response<SelectLanguageResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {

                            Log.e("Language Data", "" + response.body().getData().getLanguages());
                            language_list = response.body().getData().getLanguages();
                            for (i = 0; i < language_list.size(); i++) {
                                // Add the text layout to the parent layout
                                view = layoutInflater.inflate(R.layout.language_item_view, languages_btn_container, false);
                                boolean islanguageSelected = false;
                                final TextView tv_language = view.findViewById(R.id.tv_show_language);
                                tv_language.setText(language_list.get(i).getDESCRIPTION());
                                Log.e("Language List", "" + language_list);
                                languages_btn_container.addView(tv_language);

                            }
                            languageSelectClickListener();
                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getError(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<SelectLanguageResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "!No Internet Connection", Toast.LENGTH_SHORT).show();
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
                        Log.e("LanguageID", "" + previousTag);
//                            language_ID = language_list.get(previousTag).getSUBCODE();
                        language_ID = languages[previousTag];
                    }

                });
            }
        }
    }


}
