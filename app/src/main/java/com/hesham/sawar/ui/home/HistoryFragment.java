package com.hesham.sawar.ui.home;


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
import com.hesham.sawar.adapter.OrderHistoryAdapter;
import com.hesham.sawar.adapter.OrderUnReadyAdapter;
import com.hesham.sawar.data.model.OrderPojo;
import com.hesham.sawar.data.response.OrderInfoResponse;
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
public class HistoryFragment extends Fragment {


    private List<OrderPojo> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private OrderHistoryAdapter facultySelectAdapter;

    PrefManager prefManager;
    TextView emptyLayout;

    TextView totalorders , totalservice ,totalrate ,totalincome;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        facultyRecyclerView=view.findViewById(R.id.termRecyclerView);
        prefManager = new PrefManager(getContext());
        emptyLayout=view.findViewById(R.id.emptyLayout);
        hideEmpty();

        facultyPojos = new ArrayList<>();
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext() );
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new OrderHistoryAdapter(getContext(),facultyPojos);
        facultyRecyclerView.setAdapter(facultySelectAdapter);

        totalorders=view.findViewById(R.id.totalorders);
        totalservice=view.findViewById(R.id.totalservice);
        totalrate=view.findViewById(R.id.totalrate);
        totalincome=view.findViewById(R.id.totalincome);
        if (NetworkUtilities.isOnline(getContext())) {
            getOrdersHistory();
            getOrdersInfo();
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    public void getOrdersHistory() {//prefManager.getCenterId()
        Call<OrderResponse> call = Apiservice.getInstance().apiRequest.
                getOrderHistory(prefManager.getCenterId());
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
                    facultySelectAdapter = new OrderHistoryAdapter(getContext(), facultyPojos);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();

                showEmpty();
            }
        });
    }


    public void getOrdersInfo() {//prefManager.getCenterId()
        Call<OrderInfoResponse> call = Apiservice.getInstance().apiRequest.
                getOrderInfo(prefManager.getCenterId());
        call.enqueue(new Callback<OrderInfoResponse>() {
            @Override
            public void onResponse(Call<OrderInfoResponse> call, Response<OrderInfoResponse> response) {
                if (response.body().status && response.body().data != null&& response.body().data.size()!=0 ) {
                    totalorders .setText("Total no. of orders: "+response.body().data.get(0).getOrder_no());
                    totalservice .setText("Total service:  "+response.body().data.get(0).getTotal_service()+" LE");
                    totalrate .setText("Rating: "+response.body().data.get(0).getRate());
                    totalincome .setText("Total copying income: "+response.body().data.get(0).getTotal_income()+" LE");
                }
            }

            @Override
            public void onFailure(Call<OrderInfoResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }
    void showEmpty(){
        emptyLayout.setVisibility(View.VISIBLE);
    }
    void hideEmpty(){
        emptyLayout.setVisibility(View.GONE);
    }
}
