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

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {
        private Context context;
        private List<OrderPojo> facultyPojos;
    private EventListener listener;

    public OrderHistoryAdapter() {
        facultyPojos = new ArrayList<>();
        }

    public OrderHistoryAdapter(Context context, List<OrderPojo> facultyPojos ) {
            this.context = context;
//            prefManager=new PrefManager(context);
            this.facultyPojos = facultyPojos;

    }

        @NonNull
        @Override
        public OrderHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_unready_order, parent, false);
            return new OrderHistoryAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderHistoryAdapter.MyViewHolder holder, int position) {
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

                deleteBtn.setVisibility(View.GONE);
                canceledOrder.setVisibility(View.GONE);
            }
        }

    public interface EventListener {
        void onEvent(Fragment data);
    }
        public void updateList(List<OrderPojo> newlist) {
            facultyPojos = newlist;
            this.notifyDataSetChanged();
        }
    }

