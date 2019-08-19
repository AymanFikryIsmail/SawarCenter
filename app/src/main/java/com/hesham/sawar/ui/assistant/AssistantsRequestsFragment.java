package com.hesham.sawar.ui.assistant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hesham.sawar.R;
import com.hesham.sawar.adapter.AssistatntAdapter;
import com.hesham.sawar.adapter.AssistatntRequestsAdapter;
import com.hesham.sawar.adapter.SubjectHomeAdapter;
import com.hesham.sawar.data.model.SubjectPojo;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.AssistantResponse;
import com.hesham.sawar.data.response.SubjectResponse;
import com.hesham.sawar.data.response.UserResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssistantsRequestsFragment extends Fragment {


    public AssistantsRequestsFragment() {
        // Required empty public constructor
    }

    public static AssistantsRequestsFragment newInstance(String param1, String param2) {
        AssistantsRequestsFragment fragment = new AssistantsRequestsFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    private List<UserPojo> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private AssistatntRequestsAdapter facultySelectAdapter;

    PrefManager prefManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_assistants_requests, container, false);
        prefManager=new PrefManager(getContext());
        facultyRecyclerView=view.findViewById(R.id.termRecyclerView);
        facultyPojos = new ArrayList<>();


        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext() );
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new AssistatntRequestsAdapter(getContext(),facultyPojos);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        getAllAssistantsRequests();
        return view;
    }



    public void getAllAssistantsRequests() {//prefManager.getCenterId()
        Call<AssistantResponse> call = Apiservice.getInstance().apiRequest.
                getAllAssistantsRequests(prefManager.getCenterId());
        call.enqueue(new Callback<AssistantResponse>() {
            @Override
            public void onResponse(Call<AssistantResponse> call, Response<AssistantResponse> response) {
                if (response.body().status  && response.body().cc_id != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.addAll(response.body().cc_id);
                    facultySelectAdapter = new AssistatntRequestsAdapter(getContext(),facultyPojos);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }
            }

            @Override
            public void onFailure(Call<AssistantResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

}