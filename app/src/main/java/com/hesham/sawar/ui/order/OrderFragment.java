package com.hesham.sawar.ui.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.hesham.sawar.R;
import com.hesham.sawar.adapter.OrderViewPager;

public class OrderFragment extends Fragment {
    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }


    OrderViewPager sectionsPagerAdapter;
    ViewPager viewPager;
    TabLayout tabs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_order, container, false);

        viewPager =view. findViewById(R.id.view_pager);
        tabs = view.findViewById(R.id.tabs);
        TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layout, null);
        tabOne.setText("ONE");
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.analytics, 0, 0);
       // tabs.getTabAt(0).setCustomView(tabOne);
        sectionsPagerAdapter = new OrderViewPager(getContext(), getChildFragmentManager() ,1);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
        return view;
    }

}