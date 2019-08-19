package com.hesham.sawar.ui.subjects;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hesham.sawar.R;
import com.hesham.sawar.adapter.FacultySelectAdapter;
import com.hesham.sawar.adapter.YearHomeAdapter;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class YearsFragment extends Fragment  implements YearHomeAdapter.EventListener{

    private List<String> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private YearHomeAdapter facultySelectAdapter;
    private int years;
    private ArrayList<String> yearsNames;
    public YearsFragment(int years) {
        this.years=years;
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yearsNames = new ArrayList<>();
        yearsNames.add("First year");
        yearsNames.add("second year");
        yearsNames.add("third year");
        yearsNames.add("forth year");
        if (years==5){
            yearsNames.add("fifth year");
        }else if (years==6){
            yearsNames.add("fifth year");
            yearsNames.add("sixth year");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_years, container, false);
        facultyRecyclerView=view.findViewById(R.id.yearRecyclerView);

//        yearsNames.
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext() );
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new YearHomeAdapter(getContext(),this,yearsNames);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        return view;
    }

    @Override
    public void onEvent(Fragment data) {
        ((HomeActivity)getActivity()).loadFragment(data);

    }
}
