package com.hesham.sawar.adapter;

import android.content.Context;
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
import com.hesham.sawar.networkmodule.Apiservice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderReadyAdapter extends RecyclerView.Adapter<OrderReadyAdapter.MyViewHolder> {
    private Context context;
    private List<OrderPojo> facultyPojos;
    private EventListener listener;

    public OrderReadyAdapter() {
        facultyPojos = new ArrayList<>();
    }

    public OrderReadyAdapter(Context context, List<OrderPojo> facultyPojos) {
        this.context = context;
//            prefManager=new PrefManager(context);
        this.facultyPojos = facultyPojos;

    }

    @NonNull
    @Override
    public OrderReadyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_unready_order, parent, false);
        return new OrderReadyAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderReadyAdapter.MyViewHolder holder, int position) {
        holder.bind(facultyPojos.get(position));
    }

    @Override
    public int getItemCount() {
        return facultyPojos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView ordername, date, price, time, canceledOrder;
        public Button deleteBtn;

        public MyViewHolder(View itemView) {
            super(itemView);

            ordername = itemView.findViewById(R.id.ordername);
            date = itemView.findViewById(R.id.orderdate);
            time = itemView.findViewById(R.id.ordertime);
            price = itemView.findViewById(R.id.orderprice);
            canceledOrder = itemView.findViewById(R.id.canceledOrder);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

        }

        public void bind(final OrderPojo orderPojo) {
            ordername.setText("Order number        " + orderPojo.getId());
            date.setText("date: " + orderPojo.getDate());
            time.setText("Time: " + orderPojo.getDate());
            price.setText("price:" + orderPojo.getTotal_price());

            if (orderPojo.getCancel_s() == 1) {
                canceledOrder.setVisibility(View.VISIBLE);
            } else {
                canceledOrder.setVisibility(View.GONE);
            }

            if (isBeforeNow(orderPojo.getLongDate())) {
                canceledOrder.setVisibility(View.GONE);
            } else {
                canceledOrder.setVisibility(View.VISIBLE);
                canceledOrder.setText("Time Out");
            }

            if (orderPojo.getRecieve() == 1) {
                deleteBtn.setText("Done");
            } else {
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeOrder(orderPojo);
                    }
                });
            }

        }
    }

    boolean isBeforeNow(long dt) {

        long after = dt + 86400000;
        long today = 0;
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String output = sdf.format(c.getTime());
            Date date = sdf.parse(output);

            today = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (after > today) {
            return true;
        }
        return false;
    }

    public void removeOrder(final OrderPojo orderPojo) {//prefManager.getCenterId()
        Call<Object> call = Apiservice.getInstance().apiRequest.
                removeReadyOrder(orderPojo.getId());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("tag", "articles total result:: " + response);
                facultyPojos.remove(orderPojo);
                OrderReadyAdapter.this.notifyDataSetChanged();
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

