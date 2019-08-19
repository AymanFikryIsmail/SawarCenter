package com.hesham.sawar.ui.subjects;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hesham.sawar.R;
import com.hesham.sawar.adapter.FacultySelectAdapter;
import com.hesham.sawar.data.model.FacultyPojo;

import java.util.ArrayList;
import java.util.List;

public class PaperFragment extends Fragment {
    public static PaperFragment newInstance() {
        PaperFragment fragment = new PaperFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private List<FacultyPojo> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private FacultySelectAdapter facultySelectAdapter;
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
        View view= inflater.inflate(R.layout.fragment_paper, container, false);
        facultyRecyclerView=view.findViewById(R.id.termRecyclerView);
        facultyPojos = new ArrayList<>();
        facultyPojos.add(new FacultyPojo("er.jpg"));
        facultyPojos.add(new FacultyPojo("aa.jpg"));
        facultyPojos.add(new FacultyPojo("ddd.jpg"));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext() , 2);
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new FacultySelectAdapter(getContext(),facultyPojos);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        return view;
    }

}