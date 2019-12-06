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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hesham.sawar.R;
import com.hesham.sawar.adapter.FacultyHomeAdapter;
import com.hesham.sawar.adapter.FacultySelectAdapter;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.response.FacultyResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.networkmodule.NetworkUtilities;
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
    private FrameLayout progress_view;

    public HomeFragment() {
        // Required empty public constructor
    }


    PrefManager prefManager;
    LinearLayout emptyLayout;
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
        progress_view=view.findViewById(R.id.progress_view);
        emptyLayout=view.findViewById(R.id.emptyLayout);

        emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        callApi();
    }

    private  void callApi() {
        hideEmpty();
        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {
                getFaculties();
            } else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }




    public void getFaculties() {
        Call<FacultyResponse> call = Apiservice.getInstance().apiRequest.
                getHomeFaculties(prefManager.getCenterId());
        progress_view.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<FacultyResponse>() {
            @Override
            public void onResponse(Call<FacultyResponse> call, Response<FacultyResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().cc_id != null) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        facultyPojos.clear();
                        facultyPojos.addAll(response.body().cc_id);
                        facultySelectAdapter = new FacultyHomeAdapter(getContext(), HomeFragment.this, facultyPojos);
                        facultyRecyclerView.setAdapter(facultySelectAdapter);

                    }
                }
                progress_view.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FacultyResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                progress_view.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();

            }
        });
    }


    void showEmpty(){
        emptyLayout.setVisibility(View.VISIBLE);
    }
    void hideEmpty(){
        emptyLayout.setVisibility(View.GONE);
    }

    @Override
    public void onEvent(Fragment data) {
        ((HomeActivity)getActivity()).loadFragment(data);
    }
}
