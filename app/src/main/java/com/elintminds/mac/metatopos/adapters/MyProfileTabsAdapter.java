package com.elintminds.mac.metatopos.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.elintminds.mac.metatopos.fragments.MyProfileAdvertisementTabviewClass;
import com.elintminds.mac.metatopos.fragments.MyProfileEventTabviewClass;
import com.elintminds.mac.metatopos.fragments.MyProfilePostTabviewClass;

import java.util.List;

public class MyProfileTabsAdapter extends FragmentStatePagerAdapter {

    private List<String> tabNames;


    public MyProfileTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new MyProfilePostTabviewClass();
        } else if (position == 1) {
            fragment = new MyProfileEventTabviewClass();
        } else if (position == 2) {
            fragment = new MyProfileAdvertisementTabviewClass();
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
//        if (position == 0) {
//            title = "Post()";
//        } else if (position == 1) {
//            title = "Events()";
//        } else if (position == 2) {
//            title = "Advertisement()";
//        }
//
        return title;
    }

    public void setTabNames(List<String> tabNames) {
        this.tabNames = tabNames;
    }
}
