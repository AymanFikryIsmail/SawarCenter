package com.hesham.sawar.ui.subjects;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.hesham.sawar.R;
import com.hesham.sawar.adapter.SubjectViewPager;

public class SubjectFragment extends Fragment {

    SubjectViewPager sectionsPagerAdapter;
    ViewPager viewPager;
    TabLayout tabs;

    private int years;

    public SubjectFragment(int years) {
        this.years=years;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_subject, container, false);

        viewPager = view.findViewById(R.id.view_pager);
        tabs = view.findViewById(R.id.tabs);
        sectionsPagerAdapter = new SubjectViewPager(getContext(), getChildFragmentManager() , years);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
        return view;
    }

}
