package com.hesham.sawar.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hesham.sawar.ui.assistant.AssistantsFragment;
import com.hesham.sawar.ui.assistant.AssistantsRequestsFragment;
import com.hesham.sawar.ui.subjects.FirstTermFragment;
import com.hesham.sawar.ui.subjects.SecondTermFragment;

public class AssistantViewPager extends FragmentPagerAdapter {

    private static final String[] TAB_TITLES = new String[]{"Assistants" ,"Requests"};
    private final Context mContext;

    private int years;
    public AssistantViewPager(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new AssistantsFragment();
                break;
            case 1:
                fragment = new AssistantsRequestsFragment();
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