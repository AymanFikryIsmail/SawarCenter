package com.hesham.sawar.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.hesham.sawar.R;
import com.hesham.sawar.adapter.OrderDetailsAdapter;
import com.hesham.sawar.adapter.OrderUnReadyAdapter;
import com.hesham.sawar.adapter.OrderViewPager;
import com.hesham.sawar.adapter.SubjectViewPager;
import com.hesham.sawar.data.model.OrderDetailsPojo;
import com.hesham.sawar.data.model.OrderPojo;
import com.hesham.sawar.data.response.DetailsResponse;
import com.hesham.sawar.data.response.OrderResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {


    private List<OrderDetailsPojo> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private OrderDetailsAdapter facultySelectAdapter;

    PrefManager prefManager;
    int orderid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        facultyRecyclerView=findViewById(R.id.termRecyclerView);
        prefManager = new PrefManager(this);
        orderid=getIntent().getIntExtra("orderid",0);
        facultyPojos = new ArrayList<>();
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(this);
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new OrderDetailsAdapter(this,facultyPojos);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        getOrdersDetails();
    }

    public void getOrdersDetails() {//prefManager.getCenterId()
        Call<DetailsResponse> call = Apiservice.getInstance().apiRequest.
                getOrderDetails(orderid);
        call.enqueue(new Callback<DetailsResponse>() {
            @Override
            public void onResponse(Call<DetailsResponse> call, Response<DetailsResponse> response) {
                if (response.body().status && response.body().data != null && response.body().data.size() != 0) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.clear();
                    facultyPojos.addAll(response.body().data);
                    facultySelectAdapter = new OrderDetailsAdapter(OrderActivity.this, facultyPojos);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }
            }

            @Override
            public void onFailure(Call<DetailsResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

}
