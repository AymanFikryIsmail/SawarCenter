package com.hesham.sawar.ui.assistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.hesham.sawar.R;
import com.hesham.sawar.adapter.AssistantViewPager;
import com.hesham.sawar.adapter.SubjectViewPager;

public class AssistantsActivity extends AppCompatActivity {

    AssistantViewPager sectionsPagerAdapter;
    ViewPager viewPager;
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistants);

        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);
        sectionsPagerAdapter = new AssistantViewPager(this, getSupportFragmentManager() );
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
    }

}