package com.hesham.sawar.ui.assistant;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hesham.sawar.R;
import com.hesham.sawar.adapter.AssistatntAdapter;
import com.hesham.sawar.adapter.SubjectHomeAdapter;
import com.hesham.sawar.data.model.SubjectPojo;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.AssistantResponse;
import com.hesham.sawar.data.response.SubjectResponse;
import com.hesham.sawar.data.response.UserResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.networkmodule.NetworkUtilities;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssistantsFragment extends Fragment {


    public AssistantsFragment() {
        // Required empty public constructor
    }

    private List<UserPojo> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private AssistatntAdapter facultySelectAdapter;

    PrefManager prefManager;
    TextView emptyLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_assistants, container, false);
        prefManager=new PrefManager(getContext());
        facultyRecyclerView=view.findViewById(R.id.termRecyclerView);
        facultyPojos = new ArrayList<>();
        emptyLayout=view.findViewById(R.id.emptyLayout);
        hideEmpty();

        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext() );
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new AssistatntAdapter(getContext(),facultyPojos);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        if (NetworkUtilities.isOnline(getContext())) {
            getAssistants();
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }

        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
//            getAssistants();
            // If we are becoming invisible, then...
            if (!isVisibleToUser) {
            }else {
                getAssistants();
            }
        }
    }


    public void getAssistants() {//prefManager.getCenterId()
        Call<AssistantResponse> call = Apiservice.getInstance().apiRequest.
                getAllAssistants(prefManager.getCenterId());
        call.enqueue(new Callback<AssistantResponse>() {
            @Override
            public void onResponse(Call<AssistantResponse> call, Response<AssistantResponse> response) {
                if (response.body().status  && response.body().cc_id != null) {//response.body().status  &&
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.clear();
                    facultyPojos.addAll(response.body().cc_id);
                    checkForEmpty();
                    facultySelectAdapter = new AssistatntAdapter(getContext(),facultyPojos);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }
            }

            @Override
            public void onFailure(Call<AssistantResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                showEmpty();
            }
        });
    }

    public  void checkForEmpty(){
        if (facultyPojos.size()==0){
            showEmpty();
        }else {
            hideEmpty();
        }
    }
    void showEmpty(){
        emptyLayout.setVisibility(View.VISIBLE);
    }
    void hideEmpty(){
        emptyLayout.setVisibility(View.GONE);
    }
}
