package com.hesham.sawar.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawar.R;
import com.hesham.sawar.data.model.OrderPojo;
import com.hesham.sawar.data.response.AssistantResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.ui.order.OrderActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderUnReadyAdapter extends RecyclerView.Adapter<OrderUnReadyAdapter.MyViewHolder> {
        private Context context;
        private List<OrderPojo> facultyPojos;
    private EventListener listener;

    public OrderUnReadyAdapter() {
        facultyPojos = new ArrayList<>();
        }

    public OrderUnReadyAdapter(Context context, List<OrderPojo> facultyPojos ) {
            this.context = context;
//            prefManager=new PrefManager(context);
            this.facultyPojos = facultyPojos;

    }

        @NonNull
        @Override
        public OrderUnReadyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_unready_order, parent, false);
            return new OrderUnReadyAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderUnReadyAdapter.MyViewHolder holder, int position) {
            holder.bind(facultyPojos.get(position));
        }

        @Override
        public int getItemCount() {
            return facultyPojos.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView ordername , date ,price ,time ,canceledOrder ;
            public Button deleteBtn;
            public MyViewHolder(View itemView) {
                super(itemView);

                ordername = itemView.findViewById(R.id.ordername);
                date = itemView.findViewById(R.id.orderdate);
                time = itemView.findViewById(R.id.ordertime);
                price = itemView.findViewById(R.id.orderprice);
                deleteBtn = itemView.findViewById(R.id.deleteBtn);
                canceledOrder = itemView.findViewById(R.id.canceledOrder);

            }

            public void bind(final OrderPojo orderPojo) {
                ordername.setText("Order number        "+orderPojo.getId());
                date.setText("date: "+orderPojo.getDate());
                time.setText("Time: "+orderPojo.getDate());
                price.setText("price:"+orderPojo.getPrice());
                if (orderPojo.getCancel_s() == 1) {
                    canceledOrder.setVisibility(View.VISIBLE);
                } else {
                    canceledOrder.setVisibility(View.GONE);
                }

                if (isBeforeNow(orderPojo.getDate())) {
                    canceledOrder.setVisibility(View.GONE);
                }else {
                    canceledOrder.setVisibility(View.VISIBLE);
                    canceledOrder.setText("Time Out");
                }
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makeReadyOrder(orderPojo);
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent=new Intent(context, OrderActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("orderid",orderPojo.getId());
                        context.startActivity(intent);
                    }
                });

            }
        }

    boolean isBeforeNow(String dt) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        String output = sdf.format(c.getTime());
        Date strDate = null;
        try {
            strDate = sdf.parse(output);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (new Date().after(strDate)) {
            return true;
        }
        return false;
    }

    public void makeReadyOrder(final OrderPojo orderPojo) {//prefManager.getCenterId()
        Call<Object> call = Apiservice.getInstance().apiRequest.
                makeReadyOrder(orderPojo.getId());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("tag", "articles total result:: " +response);
                facultyPojos.remove(orderPojo);
                OrderUnReadyAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

    public interface EventListener {
        void onEvent(Fragment data);
    }
        public void updateList(List<OrderPojo> newlist) {
            facultyPojos = newlist;
            this.notifyDataSetChanged();
        }
    }

