package com.pertamina.musicoolpromo.view.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.fragment.RetrofitFragment;
import com.pertamina.musicoolpromo.view.fragment.TubeFragment;

public class ScanPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    public ScanPagerAdapter (Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.first_tab_points,
            R.string.second_tab_points
    };

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new TubeFragment();
                break;
            case 1:
                fragment = new RetrofitFragment();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
