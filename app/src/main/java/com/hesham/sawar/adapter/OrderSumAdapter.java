package com.hesham.sawar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawar.R;
import com.hesham.sawar.data.model.OrderDetailsPojo;

import java.util.ArrayList;
import java.util.List;

public class OrderSumAdapter extends RecyclerView.Adapter<OrderSumAdapter.MyViewHolder> {
    private Context context;
    private List<OrderDetailsPojo> facultyPojos;
    private EventListener listener;

    public OrderSumAdapter() {
        facultyPojos = new ArrayList<>();
    }

    public OrderSumAdapter(Context context, List<OrderDetailsPojo> facultyPojos) {
        this.context = context;
//            prefManager=new PrefManager(context);
        this.facultyPojos = facultyPojos;

    }

    @NonNull
    @Override
    public OrderSumAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_sum_order, parent, false);
        return new OrderSumAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSumAdapter.MyViewHolder holder, int position) {
        holder.bind(facultyPojos.get(position));
    }

    @Override
    public int getItemCount() {
        return facultyPojos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView ordername, copy;
        public Button deleteBtn;

        public MyViewHolder(View itemView) {
            super(itemView);

            ordername = itemView.findViewById(R.id.ordername);
            copy = itemView.findViewById(R.id.orderno);

        }

        public void bind(final OrderDetailsPojo orderPojo) {
            String lectutre = orderPojo.getCategory();
            String year = orderPojo.getYear();
            String faculty = orderPojo.getFaculty();
            String department = orderPojo.getDepartment();

            ordername.setText(year + "/" + faculty + "/" + department + "/" + orderPojo.getSubject() + "/" + lectutre + "/" + orderPojo.getName());//                date.setText("date: "+orderPojo.getDate());
//                time.setText("Time: "+orderPojo.getDate());
            copy.setText("" + orderPojo.getNo());

        }
    }

    public interface EventListener {
        void onEvent(Fragment data);
    }

}

