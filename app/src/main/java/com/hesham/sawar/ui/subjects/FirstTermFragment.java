package com.hesham.sawar.ui.subjects;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hesham.sawar.R;
import com.hesham.sawar.adapter.FacultyHomeAdapter;
import com.hesham.sawar.adapter.FacultySelectAdapter;
import com.hesham.sawar.adapter.SubjectHomeAdapter;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.model.SubjectPojo;
import com.hesham.sawar.data.response.FacultyResponse;
import com.hesham.sawar.data.response.SubjectResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.ui.home.HomeFragment;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstTermFragment extends Fragment {

    private List<SubjectPojo> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private SubjectHomeAdapter facultySelectAdapter;

    PrefManager prefManager;
    private int years;

    public FirstTermFragment(int years) {
        this.years=years;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_first_term, container, false);
        prefManager=new PrefManager(getContext());
        facultyRecyclerView=view.findViewById(R.id.termRecyclerView);
        facultyPojos = new ArrayList<>();


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext() , 2);
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new SubjectHomeAdapter(getContext(),facultyPojos);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        getSubjects();
        return view;
    }



    public void getSubjects() {//prefManager.getCenterId()
        SubjectPojo subjectPojo=new SubjectPojo(1 ,prefManager.getFacultyId(), years,1);
        Call<SubjectResponse> call = Apiservice.getInstance().apiRequest.
                getAllSubjects(subjectPojo);
        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                if (response.body().status  && response.body().cc_id != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.addAll(response.body().cc_id);
                    facultySelectAdapter = new SubjectHomeAdapter(getContext(),facultyPojos);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }
            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

}
