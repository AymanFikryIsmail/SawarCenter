package com.hesham.sawar.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.hesham.sawar.R;
import com.hesham.sawar.adapter.SubjectViewPager;

public class OrderActivity extends AppCompatActivity {


    SubjectViewPager sectionsPagerAdapter;
    ViewPager viewPager;
    TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);
        sectionsPagerAdapter = new SubjectViewPager(this, getSupportFragmentManager() ,1);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
    }


}
