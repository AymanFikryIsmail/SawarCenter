package com.hesham.sawar.ui.order;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hesham.sawar.R;
import com.hesham.sawar.adapter.OrderUnReadyAdapter;
import com.hesham.sawar.data.model.OrderPojo;
import com.hesham.sawar.data.response.OrderResponse;
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
public class UnReadyOrderFragment extends Fragment  implements OrderUnReadyAdapter.EventListener {


    private ArrayList<OrderPojo> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private OrderUnReadyAdapter facultySelectAdapter;
    public UnReadyOrderFragment() {
        // Required empty public constructor
    }
    TextView emptyLayout;

    PrefManager prefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_un_ready_order, container, false);
        facultyRecyclerView=view.findViewById(R.id.termRecyclerView);
        prefManager = new PrefManager(getContext());
        emptyLayout=view.findViewById(R.id.emptyLayout);
        hideEmpty();
        facultyPojos = new ArrayList<>();
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext() );
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new OrderUnReadyAdapter(getContext(),facultyPojos ,this);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        getOrders();

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
                getOrders();
            }
        }
    }
    public void getOrders() {//prefManager.getCenterId()
        Call<OrderResponse> call = Apiservice.getInstance().apiRequest.
                getUnreadyOrders(prefManager.getCenterId());
        if (NetworkUtilities.isOnline(getContext())) {

            call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.body().status && response.body().data != null && response.body().data.size() != 0) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.clear();
                    facultyPojos.addAll(response.body().data);
                    if (facultyPojos.size()==0){
                        showEmpty();
                    }else {
                        hideEmpty();
                    }
                    facultySelectAdapter = new OrderUnReadyAdapter(getContext(), facultyPojos , UnReadyOrderFragment.this);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                    ((OrderFragment) getParentFragment()).setNumOfUnReadyoRders(facultyPojos.size());
                }else {
                    showEmpty();
                    ((OrderFragment) getParentFragment()).setNumOfUnReadyoRders(0);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                showEmpty();
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
            }
        });
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }

    public void sum(){

        Intent intent=new Intent(getContext(),OrderDetailsActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("orderList", facultyPojos);
        intent.putExtra("orderList", facultyPojos);
        startActivity(intent);

    }

    @Override
    public void onRemove(int data) {
        ((OrderFragment) getParentFragment()).setNumOfUnReadyoRders(data);
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
