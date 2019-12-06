package com.hesham.sawar.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hesham.sawar.R;
import com.hesham.sawar.adapter.OrderSumAdapter;
import com.hesham.sawar.data.model.OrderDetailsPojo;
import com.hesham.sawar.data.response.DetailsResponse;
import com.hesham.sawar.databinding.ActivityOrderSumBinding;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.networkmodule.NetworkUtilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSumActivity extends AppCompatActivity {

    private ActivityOrderSumBinding binding;
    private List<OrderDetailsPojo> orderDetailsPojos;
    private OrderSumAdapter orderSumAdapter;

    private  List<Integer> orderIdList;
    String orderListString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order_sum);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_sum);
        binding.setLifecycleOwner(this);
        orderDetailsPojos = new ArrayList<>();
        orderIdList= new ArrayList<>();
        if (getIntent().getExtras()!=null){
            orderIdList =getIntent().getIntegerArrayListExtra("orderList");
             orderListString = String.valueOf(orderIdList);
        }
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(this);
        binding.termRecyclerView.setLayoutManager(gridLayoutManager);
        binding.emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        callApi();
    }

    private  void callApi() {
        hideEmpty();
        if (NetworkUtilities.isOnline(this)) {
            if (NetworkUtilities.isFast(this)) {
                getOrderSum();
            } else {
                Toast.makeText(this, "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }

    public void getOrderSum() {//prefManager.getCenterId()
        Call<DetailsResponse> call = Apiservice.getInstance().apiRequest.
                getOrderSum(orderListString);
        binding.progressView.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<DetailsResponse>() {
            @Override
            public void onResponse(Call<DetailsResponse> call, Response<DetailsResponse> response) {
                if (response.body() != null) {
                    if (response.body() != null && response.body().status && response.body().data != null && response.body().data.size() != 0) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        orderDetailsPojos.clear();
                        orderDetailsPojos.addAll(response.body().data);
                        if (orderDetailsPojos.size() == 0) {
                            showEmpty();
                        } else {
                            hideEmpty();
                        }
                        orderSumAdapter = new OrderSumAdapter(OrderSumActivity.this, orderDetailsPojos);
                        binding.termRecyclerView.setAdapter(orderSumAdapter);
                    } else {
                        showEmpty();
                    }
                }
                binding.progressView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DetailsResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(OrderSumActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                binding.progressView.setVisibility(View.GONE);
                showEmpty();
            }
        });
    }

    void showEmpty(){
        binding.emptyLayout.setVisibility(View.VISIBLE);
    }
    void hideEmpty(){
        binding.emptyLayout.setVisibility(View.GONE);
    }

}
