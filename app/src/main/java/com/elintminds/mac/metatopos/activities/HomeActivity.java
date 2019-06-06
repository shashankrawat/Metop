package com.elintminds.mac.metatopos.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.fragments.AddPostFragmentclass;
import com.elintminds.mac.metatopos.fragments.ClusterFrag;
import com.elintminds.mac.metatopos.fragments.MessagesFragmentclass;
import com.elintminds.mac.metatopos.fragments.NotificationsFragmentclass;
import com.elintminds.mac.metatopos.fragments.ProfileFragmentClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener
{
    FrameLayout container_top;
    ImageView ic_home, ic_message, ic_add, ic_notifications, ic_profile;
    Fragment fragment;
    FragmentManager fragmentManager;
    private ArrayList<String> tags = new ArrayList<>();
    public static final int requestCode_Location = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initviews();
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    private void initviews()
    {
        fragmentManager = getSupportFragmentManager();
        container_top = findViewById(R.id.container_top);
        ic_home = findViewById(R.id.ic_home);
        ic_message = findViewById(R.id.ic_msg);
        ic_add = findViewById(R.id.ic_add);
        ic_notifications = findViewById(R.id.ic_notifications);
        ic_profile = findViewById(R.id.ic_profile);
        ic_home.setOnClickListener(this);
        ic_message.setOnClickListener(this);
        ic_add.setOnClickListener(this);
        ic_notifications.setOnClickListener(this);
        ic_profile.setOnClickListener(this);
        ic_home.performClick();
    }



    @Override
    public void onClick(View view) {
        if (view == ic_home) {
//            AppUtils.startAnimation(this, ic_home);
            if(!ic_home.isSelected()) {
                tabSelectedChange(true, false, false, false, false);
                checkForPermissionLocation(this);
            }
        } else if (view == ic_add) {
            if(!ic_add.isSelected()) {
                tabSelectedChange(false, true, false, false, false);
                addPostFragView();
            }

        } else if (view == ic_message) {
            if(!ic_message.isSelected()) {
                msgFragView();
                tabSelectedChange(false, false, true, false, false);
            }

        } else if (view == ic_profile) {
            if(!ic_profile.isSelected()) {
                tabSelectedChange(false, false, false, true, false);
                profileFragView();
            }
        } else if (view == ic_notifications) {
            if(!ic_notifications.isSelected()) {
                tabSelectedChange(false, false, false, false, true);
                notificationFragView();
            }
        }
    }

    private void tabSelectedChange(boolean isHome, boolean isAddPost, boolean isMessage, boolean isProfile, boolean isNotification) {
        ic_home.setSelected(isHome);
        ic_add.setSelected(isAddPost);
        ic_message.setSelected(isMessage);
        ic_profile.setSelected(isProfile);
        ic_notifications.setSelected(isNotification);
    }

    void mapFragmentView()
    {
        fragment = new ClusterFrag();
//        fragmentManager.beginTransaction().replace(R.id.container_top, fragment, "mapview").commit();
        showFragment(fragment, ClusterFrag.TAG);
    }

    void profileFragView()
    {
        fragment = new ProfileFragmentClass();
//        fragmentManager.beginTransaction().replace(R.id.container_top, fragment, "profileview").commit();
        showFragment(fragment, ProfileFragmentClass.TAG);
    }

    void notificationFragView()
    {
        fragment = new NotificationsFragmentclass();
//        fragmentManager.beginTransaction().replace(R.id.container_top, fragment, "notification").commit();
        showFragment(fragment, NotificationsFragmentclass.TAG);
    }

    void addPostFragView()
    {
        fragment = new AddPostFragmentclass();
//        fragmentManager.beginTransaction().replace(R.id.container_top, fragment, "AddPostview").commit();
        showFragment(fragment, AddPostFragmentclass.TAG);
    }

    void msgFragView()
    {
        fragment = new MessagesFragmentclass();
//        fragmentManager.beginTransaction().replace(R.id.container_top, fragment, "Msgview").commit();
        showFragment(fragment, MessagesFragmentclass.TAG);
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


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            this.moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showFragment(Fragment frag, String tag)
    {
        if (tags.contains(tag)) {
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(tag)).commit();
        } else {

            tags.add(tag);
            fragmentManager.beginTransaction().add(R.id.container_top, frag, tag).commit();
        }
        for (String tagStr : tags) {
            Log.e("Tags", tagStr);
            if (!tagStr.equals(tag)) {
                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(tagStr)).commit();
            }
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
            mapFragmentView();
        }
    }

    private void alertdialogMesg() {
        AlertDialog.Builder mybulider = new AlertDialog.Builder(this);
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
                    mapFragmentView();
                } else {
                    alertdialogMesg();
                }
            }
        }
    }
}
