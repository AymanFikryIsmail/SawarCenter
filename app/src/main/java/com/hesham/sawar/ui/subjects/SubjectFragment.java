package com.hesham.sawar.ui.subjects;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.hesham.sawar.R;
import com.hesham.sawar.adapter.SubjectViewPager;
import com.hesham.sawar.ui.home.HomeActivity;

public class SubjectFragment extends Fragment {

    SubjectViewPager sectionsPagerAdapter;
    ViewPager viewPager;
    TabLayout tabs;

    private int years;
    private ImageView backarrowId;

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
        viewPager.setCurrentItem(1);

        tabs.setupWithViewPager(viewPager);
        backarrowId=view.findViewById(R.id.backarrowId);
        backarrowId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).onBackPressed();
            }
        });
        return view;
    }



}
