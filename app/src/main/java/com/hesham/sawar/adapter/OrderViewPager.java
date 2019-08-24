package com.hesham.sawar.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hesham.sawar.ui.order.ReadyOrderFragment;
import com.hesham.sawar.ui.order.UnReadyOrderFragment;
import com.hesham.sawar.ui.subjects.FirstTermFragment;
import com.hesham.sawar.ui.subjects.SecondTermFragment;

public class OrderViewPager extends FragmentPagerAdapter {

    private static final String[] TAB_TITLES = new String[]{"UnReady Orders" ,"Ready Orders"};
    private final Context mContext;

    private int years;
    public OrderViewPager(Context context, FragmentManager fm, int years) {
        super(fm);
        mContext = context;
        this.years=years;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new UnReadyOrderFragment();
                break;
            case 1:
                fragment = new ReadyOrderFragment();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}