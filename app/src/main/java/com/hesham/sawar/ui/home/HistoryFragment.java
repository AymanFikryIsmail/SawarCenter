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

import com.hesham.sawar.R;
import com.hesham.sawar.adapter.OrderHistoryAdapter;
import com.hesham.sawar.adapter.OrderUnReadyAdapter;
import com.hesham.sawar.data.model.OrderPojo;
import com.hesham.sawar.data.response.OrderInfoResponse;
import com.hesham.sawar.data.response.OrderResponse;
import com.hesham.sawar.networkmodule.Apiservice;
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

    TextView totalorders , totalservice ,totalrate ,totalincome;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        facultyRecyclerView=view.findViewById(R.id.termRecyclerView);
        prefManager = new PrefManager(getContext());

        facultyPojos = new ArrayList<>();
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext() );
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new OrderHistoryAdapter(getContext(),facultyPojos);
        facultyRecyclerView.setAdapter(facultySelectAdapter);

        totalorders=view.findViewById(R.id.totalorders);
        totalservice=view.findViewById(R.id.totalservice);
        totalrate=view.findViewById(R.id.totalrate);
        totalincome=view.findViewById(R.id.totalincome);

        getOrdersHistory();
        getOrdersInfo();
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
                    facultySelectAdapter = new OrderHistoryAdapter(getContext(), facultyPojos);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

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
                    totalorders .setText("total orders "+response.body().data.get(0).getOrder_no());
                    totalservice .setText("total service "+response.body().data.get(0).getTotal_service()+"");
                    totalrate .setText("total rate "+response.body().data.get(0).getRate());
                    totalincome .setText("total copy income "+response.body().data.get(0).getTotal_income()+"");
                }
            }

            @Override
            public void onFailure(Call<OrderInfoResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }
}
