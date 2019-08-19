package com.hesham.sawar.ui.home;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hesham.sawar.R;
import com.hesham.sawar.adapter.FacultyHomeAdapter;
import com.hesham.sawar.adapter.FacultySelectAdapter;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.response.FacultyResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.ui.signup.SignUpActivity;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment  implements FacultyHomeAdapter.EventListener{

    private List<FacultyPojo> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private FacultyHomeAdapter facultySelectAdapter;
    public HomeFragment() {
        // Required empty public constructor
    }


    PrefManager prefManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        facultyRecyclerView=view.findViewById(R.id.facultyRecyclerView);
        facultyPojos = new ArrayList<>();

        prefManager=new PrefManager(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext() , 2);
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new FacultyHomeAdapter(getContext(),this,facultyPojos);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        getFaculties();
        return view;
    }




    public void getFaculties() {
        Call<FacultyResponse> call = Apiservice.getInstance().apiRequest.
                getHomeFaculties(prefManager.getCenterId());
        call.enqueue(new Callback<FacultyResponse>() {
            @Override
            public void onResponse(Call<FacultyResponse> call, Response<FacultyResponse> response) {
                if (response.body().status  && response.body().cc_id != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.addAll(response.body().cc_id);
                    facultySelectAdapter = new FacultyHomeAdapter(getContext(),HomeFragment.this,facultyPojos);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }
            }

            @Override
            public void onFailure(Call<FacultyResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

    @Override
    public void onEvent(Fragment data) {
        ((HomeActivity)getActivity()).loadFragment(data);
    }
}