package com.hesham.sawar.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hesham.sawar.R;
import com.hesham.sawar.adapter.OrderDetailsAdapter;
import com.hesham.sawar.data.model.OrderDetailsPojo;
import com.hesham.sawar.data.model.OrderPojo;
import com.hesham.sawar.data.model.PaperPojo;
import com.hesham.sawar.data.response.DetailsResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {


    private List<OrderDetailsPojo> orderDetailsPojos;
    private ArrayList<OrderPojo> orderPojos;

    private RecyclerView facultyRecyclerView;
    private OrderDetailsAdapter facultySelectAdapter;

    PrefManager prefManager;
    int orderid;
    LinearLayout calculationId;
    TextView emptyLayout;
    private TextView totalPrice, totalService, totalMonye;
    double total_service, total_money;
    double total_Price = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        facultyRecyclerView=findViewById(R.id.termRecyclerView);
        calculationId=findViewById(R.id.calculationId);
        emptyLayout=findViewById(R.id.emptyLayout);
        hideEmpty();
        prefManager = new PrefManager(this);
        orderPojos = new ArrayList<>();
        orderPojos=(ArrayList<OrderPojo>)getIntent().getSerializableExtra("orderList");
        if (orderPojos!=null){

        }else {
            orderid=getIntent().getIntExtra("orderid",0);
            orderDetailsPojos = new ArrayList<>();
            RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(this);
            facultyRecyclerView.setLayoutManager(gridLayoutManager);
            facultySelectAdapter = new OrderDetailsAdapter(this, orderDetailsPojos);
            facultyRecyclerView.setAdapter(facultySelectAdapter);
            getOrdersDetails();
        }
    }
    HashMap<Integer, OrderDetailsPojo> paperPojoHashMap;
    void initView(View view) {
        totalPrice = view.findViewById(R.id.totalPriceCart);
        totalService = view.findViewById(R.id.totalServiceCart);
        totalMonye = view.findViewById(R.id.totalCart);
        calculateTotalPrice();

    }

    void calculateTotalPrice() {
        total_Price = 0;
        for (int i = 0; i < orderPojos.size(); i++) {
            total_Price = total_Price + orderPojos.get(i).getPrice() * orderPojos.get(i).getNo();
        }
        totalPrice.setText( total_Price + "");
        double service = calculateService();

        total_service = service;
        total_money = (total_Price + service);
        totalService.setText(total_service + "");
        totalMonye.setText(total_money + "");
    }

    double calculateService() {
        ArrayList<Integer> copyNum = new ArrayList<>();
        ArrayList<Integer> pageNum = new ArrayList<>();
        ;
        for (int i = 0; i < orderPojos.size(); i++) {
            copyNum.add(orderPojos.get(i).getNo());
            pageNum.add(orderPojos.get(i).getPage());
        }
        int x = 5;
        double sum = 0;
        double serv = 0;
        int q = 0;
        while (q == 0) {
            for (int i = 0; i < copyNum.size(); i++) {
                if (copyNum.get(i) > 0) {
                    sum += pageNum.get(i);
                    int newCopies = copyNum.get(i);
                    copyNum.set(i, --newCopies);
                }
            }
            if (sum > 0) {
//                console.log(sum/100)
                double rem=sum % 100;
                if (rem==0){
                    serv += Math.floor(sum / 100) ;
                }else {
                    serv += Math.floor(sum / 100)+1 ;
                }
                sum = 0;
            } else {
                q = 1;
            }
        }
        return serv;
    }
        public void getOrdersDetails() {//prefManager.getCenterId()
        Call<DetailsResponse> call = Apiservice.getInstance().apiRequest.
                getOrderDetails(orderid);
        call.enqueue(new Callback<DetailsResponse>() {
            @Override
            public void onResponse(Call<DetailsResponse> call, Response<DetailsResponse> response) {
                if (response.body().status && response.body().data != null && response.body().data.size() != 0) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    orderDetailsPojos.clear();
                    orderDetailsPojos.addAll(response.body().data);
                    calculateTotalPrice();
                    if (orderDetailsPojos.size()==0){
                        showEmpty();
                    }else {
                        hideEmpty();
                    }
                    facultySelectAdapter = new OrderDetailsAdapter(OrderDetailsActivity.this, orderDetailsPojos);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }else {
                    showEmpty();
                }
            }

            @Override
            public void onFailure(Call<DetailsResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

    void showEmpty(){
        emptyLayout.setVisibility(View.VISIBLE);
        calculationId.setVisibility(View.GONE);


    }
    void hideEmpty(){
        emptyLayout.setVisibility(View.GONE);
        calculationId.setVisibility(View.VISIBLE);

    }


}
