package com.elintminds.mac.metatopos.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.elintminds.mac.metatopos.fragments.OtherProfileAdvertisementTabFragmentviewClass;
import com.elintminds.mac.metatopos.fragments.OtherProfileEventTabFragmentviewClass;
import com.elintminds.mac.metatopos.fragments.OtherProfilePostFragmentviewClass;

public class OtherProfileTabsAdapter extends FragmentStatePagerAdapter {
    SharedPreferences preferences;
    Context context;
    String token, totolPost, totalEvent, totalAds;


    public OtherProfileTabsAdapter(FragmentManager fm) {
        super(fm);


    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new OtherProfilePostFragmentviewClass();
        } else if (position == 1) {
            fragment = new OtherProfileEventTabFragmentviewClass();
        } else if (position == 2) {
            fragment = new OtherProfileAdvertisementTabFragmentviewClass();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
//
//        if (position == 0) {
//
//            title = "Post";
//
//        } else if (position == 1) {
//
//            title = "Event";
//
//        } else if (position == 2) {
//
//            title = "Advertisement";
//        }

        return title;
    }
}
