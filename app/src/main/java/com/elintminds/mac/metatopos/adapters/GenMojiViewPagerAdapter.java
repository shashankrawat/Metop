package com.elintminds.mac.metatopos.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.elintminds.mac.metatopos.fragments.FeaturedGenMojifragmentviewClass;
import com.elintminds.mac.metatopos.fragments.GenMojifragmentviewClass;

public class GenMojiViewPagerAdapter extends FragmentStatePagerAdapter {

    public GenMojiViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new GenMojifragmentviewClass();
        }
        else if (position == 1)
        {
            fragment = new FeaturedGenMojifragmentviewClass();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "GenMoji";
        }
        else if (position == 1)
        {
            title = "Featured GenMoji";
        }

        return title;
    }
}
