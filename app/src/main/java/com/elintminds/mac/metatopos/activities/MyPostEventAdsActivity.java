package com.elintminds.mac.metatopos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.adapters.MyProfileTabsAdapter;

public class MyPostEventAdsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView my_Backbtn;
    TabLayout my_Post_Event_ads_tab;
    ViewPager tab_View_Pager;
    MyProfileTabsAdapter myProfileTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post_event_ads);
        my_Backbtn = findViewById(R.id.my_backbtn);
        my_Post_Event_ads_tab = findViewById(R.id.my_tablayout);
        tab_View_Pager = findViewById(R.id.my_viewpager);
        my_Backbtn.setOnClickListener(this);
        Intent intent = getIntent();
        final String post_count, event_count, ads_count;
        post_count = intent.getStringExtra("Total_Post");
        event_count = intent.getStringExtra("Total_Event");
        ads_count = intent.getStringExtra("Total_Advertisement");
        myProfileTabsAdapter = new MyProfileTabsAdapter(getSupportFragmentManager());
        tab_View_Pager.setAdapter(myProfileTabsAdapter);
        my_Post_Event_ads_tab.setupWithViewPager(tab_View_Pager);

        TabLayout.Tab tab = my_Post_Event_ads_tab.getTabAt(0);
        TabLayout.Tab tab2 = my_Post_Event_ads_tab.getTabAt(1);
        TabLayout.Tab tab3 = my_Post_Event_ads_tab.getTabAt(2);
        tab.setText("Post" + "(" + post_count + ")");
        tab2.setText("Event" + "(" + event_count + ")");
        tab3.setText("Advertisement" + "(" + ads_count + ")");

//        tab_View_Pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(my_Post_Event_ads_tab));
//        my_Post_Event_ads_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//
//                int id = tab.getPosition();
//                switch (id) {
//                    case 0:
//                        tab.setText("Post" + "(" + post_count + ")");
//                        break;
//                    case 1:
//                        tab.setText("Event" + "(" + event_count + ")");
//                        break;
//                    case 2:
//                        tab.setText("Advertisement" + "(" + ads_count + ")");
//                        break;
//
//
//                }
//                tab_View_Pager.setCurrentItem(tab.getPosition());
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//                int id = tab.getPosition();
//                switch (id) {
//                    case 0:
//                        tab.setText("Post" + "(" + post_count + ")");
//                        break;
//                    case 1:
//                        tab.setText("Event" + "(" + event_count + ")");
//                        break;
//                    case 2:
//                        tab.setText("Advertisement" + "(" + ads_count + ")");
//                        break;
//
//                }
//
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        if (view == my_Backbtn) {
            finish();
        }

    }
}
