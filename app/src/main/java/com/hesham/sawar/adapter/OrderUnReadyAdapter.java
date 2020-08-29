package com.hesham.sawar.adapter;

import android.annotation.SuppressLint;
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
import com.hesham.sawar.data.response.CustomResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.ui.order.OrderDetailsActivity;
import com.hesham.sawar.ui.order.OrderFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderUnReadyAdapter extends RecyclerView.Adapter<OrderUnReadyAdapter.MyViewHolder> {
    private Context context;
    private List<OrderPojo> facultyPojos;
    private EventListener listener;
    private List<OrderPojo> originList;

    public OrderUnReadyAdapter() {
        facultyPojos = new ArrayList<>();
    }

    public OrderUnReadyAdapter(Context context, List<OrderPojo> facultyPojos, EventListener listener) {
        this.context = context;
//            prefManager=new PrefManager(context);
        this.facultyPojos = facultyPojos;
        this.originList = new ArrayList<>();

        this.originList.addAll(facultyPojos);
        this.listener = listener;
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

        public TextView ordername, date, price, time, receivededOrder, canceledOrder, outDatedOrder;
        public Button deleteBtn;

        public MyViewHolder(View itemView) {
            super(itemView);

            ordername = itemView.findViewById(R.id.ordername);
            date = itemView.findViewById(R.id.orderdate);
            time = itemView.findViewById(R.id.ordertime);
            price = itemView.findViewById(R.id.orderprice);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            canceledOrder = itemView.findViewById(R.id.canceledOrder);
            outDatedOrder = itemView.findViewById(R.id.outDatedOrder);
            receivededOrder = itemView.findViewById(R.id.receivededOrder);

        }

        public void bind(final OrderPojo orderPojo) {
            ordername.setText("Order number :               " + orderPojo.getNum() + "" + orderPojo.getDay() + orderPojo.getVar());
//                ordername.setText("Order number        "+orderPojo.getId());
            date.setText("date: " + orderPojo.getDate());
//                time.setText("Time: "+orderPojo.getDate());
            price.setText("" + orderPojo.getTotal_price());
            Date date = new Date(orderPojo.getLongDate());
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String dateText = df2.format(date);
//                time.setText("Time: " + dateText);

            String pm = convertTime(orderPojo.getFormattedDate());
            time.setText(pm);
            deleteBtn.setText("Ready");
            canceledOrder.setVisibility(View.GONE);
            receivededOrder.setVisibility(View.GONE);
            outDatedOrder.setVisibility(View.GONE);

            if (orderPojo.getCancel_s() == 1) {
                canceledOrder.setVisibility(View.VISIBLE);
                deleteBtn.setText("Remove");
            } else {
                canceledOrder.setVisibility(View.GONE);
            }
            if (orderPojo.getRecieve() == 1) {
                receivededOrder.setVisibility(View.VISIBLE);
                deleteBtn.setText("Remove");
            }
            if (isBeforeNow(orderPojo.getLongDate())) {
                outDatedOrder.setVisibility(View.GONE);
            } else {
                outDatedOrder.setVisibility(View.VISIBLE);
                outDatedOrder.setText("Time out");
                deleteBtn.setText("Remove");
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (orderPojo.getCancel_s() == 1) {
                        removeOrder(orderPojo);
                    } else if (orderPojo.getRecieve() == 1) {
                        removeOrder(orderPojo);
                    } else if (!isBeforeNow(orderPojo.getLongDate())) {
                        removeOrder(orderPojo);
                    } else {
                        makeReadyOrder(orderPojo);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, OrderDetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("orderid", orderPojo.getId());
                    intent.putExtra("ordertotal", orderPojo.getTotal_price());
                    intent.putExtra("orderservice", orderPojo.getService());
                    context.startActivity(intent);
                }
            });

        }
    }

    @SuppressLint("SimpleDateFormat")
    private String convertTime(String time) {
        String processingTime = "";

        try {
            String _24HourTime = time;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            processingTime = _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
        }
        return processingTime;
    }

    boolean isBeforeNow(long dt) {

        long after = dt + 86400000;//86400000;
        long today = 0;
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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

    boolean isBeforeNow(String dt) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
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
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                makeReadyOrder(orderPojo.getId());
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        Log.d("tag", "articles total result:: " + response);
                        facultyPojos.remove(orderPojo);
                        OrderUnReadyAdapter.this.notifyDataSetChanged();
                        listener.onRemove(facultyPojos.size());
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

    public void removeOrder(final OrderPojo orderPojo) {//prefManager.getCenterId()
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                removeReadyOrder(orderPojo.getId());
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        Log.d("tag", "articles total result:: " + response);
                        facultyPojos.remove(orderPojo);
                        OrderUnReadyAdapter.this.notifyDataSetChanged();
                        listener.onRemove(facultyPojos.size());
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

    public void filter(String searchWord) {

        searchWord = searchWord.toLowerCase();
        facultyPojos.clear();

        if (searchWord.isEmpty()) {
            facultyPojos.addAll(originList);
            this.notifyDataSetChanged();
        } else {

            List<OrderPojo> filteredList = new ArrayList<>();
            for (OrderPojo cityPackage : originList) {

                String name=cityPackage.getNum()+""+cityPackage.getDay()+cityPackage.getVar();
                if ((name.toLowerCase().contains(searchWord))) {
                    filteredList.add(cityPackage);
                }
            }
            facultyPojos = filteredList;
            this.notifyDataSetChanged();
        }
    }

    public interface EventListener {
        void onRemove(int data);

    }

    public void updateList(List<OrderPojo> newlist) {
        facultyPojos = newlist;
        this.notifyDataSetChanged();
    }
}

