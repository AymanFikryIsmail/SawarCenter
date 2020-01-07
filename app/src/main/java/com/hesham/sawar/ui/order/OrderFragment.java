package com.hesham.sawar.ui.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TabWidget;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.hesham.sawar.R;
import com.hesham.sawar.adapter.OrderViewPager;
import com.hesham.sawar.utils.PrefManager;

import java.util.List;

public class OrderFragment extends Fragment {
    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }


    OrderViewPager sectionsPagerAdapter;
    ViewPager viewPager;
    TabLayout tabs;
    TextView centername, refresh;
    PrefManager prefManager;
    UnReadyOrderFragment unReadyOrderFragment;
    Button sum ,filter;

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
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        prefManager = new PrefManager(getContext());

        viewPager = view.findViewById(R.id.view_pager);
        tabs = view.findViewById(R.id.tabs);

        centername = view.findViewById(R.id.centernameId);
        centername.setText(prefManager.getCenterData().getName());

        sum = view.findViewById(R.id.sum);
        sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Fragment> fragments = getChildFragmentManager().getFragments();
                if (fragments != null) {
                    for (Fragment fragment : fragments) {
                        if (fragment instanceof UnReadyOrderFragment) {
                            ((UnReadyOrderFragment) fragment).sum();
                        } else {
//                            ((ReadyOrderFragment) fragment).getOrders();
                        }
                    }
                }
            }
        });


        filter = view.findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Fragment> fragments = getChildFragmentManager().getFragments();
                if (fragments != null) {
                    for (Fragment fragment : fragments) {
                        if (fragment instanceof UnReadyOrderFragment) {
                            ((UnReadyOrderFragment) fragment).getFilteringDependencyOrders();
                        } else {
//                            ((ReadyOrderFragment) fragment).getOrders();
                        }
                    }
                }
            }
        });

        refresh = view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Fragment> fragments = getChildFragmentManager().getFragments();
                if (fragments != null) {
                    for (Fragment fragment : fragments) {
                        if (fragment instanceof UnReadyOrderFragment) {
                            ((UnReadyOrderFragment) fragment).getOrders();
                        } else {
                            ((ReadyOrderFragment) fragment).getOrders();
                        }
                    }
                }
//                sectionsPagerAdapter = new OrderViewPager(getContext(), getChildFragmentManager() );
//                viewPager.setAdapter(sectionsPagerAdapter);
//                tabs.setupWithViewPager(viewPager);
            }
        });


        TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layout, null);
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.analytics, 0, 0);
        // tabs.getTabAt(0).setCustomView(tabOne);
        sectionsPagerAdapter = new OrderViewPager(getContext(), getChildFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
        return view;
    }

    public void setNumOfUnReadyoRders(int num) {
        tabs.getTabAt(0).setText("UnReady Orders " + num);
    }

}