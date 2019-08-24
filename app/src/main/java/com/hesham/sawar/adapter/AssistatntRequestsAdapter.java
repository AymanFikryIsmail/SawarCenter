package com.hesham.sawar.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawar.R;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.AssistantResponse;
import com.hesham.sawar.data.response.UserResponse;
import com.hesham.sawar.networkmodule.Apiservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssistatntRequestsAdapter extends RecyclerView.Adapter<AssistatntRequestsAdapter.MyViewHolder> {
    private Context context;
    private List<UserPojo> facultyPojos;

    public AssistatntRequestsAdapter() {
        facultyPojos = new ArrayList<>();
    }

    public AssistatntRequestsAdapter(Context context, List<UserPojo> facultyPojos) {
        this.context = context;
//            prefManager=new PrefManager(context);
        this.facultyPojos = facultyPojos;
    }

    @NonNull
    @Override
    public AssistatntRequestsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_assistant_request, parent, false);
        return new AssistatntRequestsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssistatntRequestsAdapter.MyViewHolder holder, int position) {
        holder.bind(facultyPojos.get(position));
    }

    @Override
    public int getItemCount() {
        return facultyPojos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView assistantName, assistantPhone, assistantEmail;

        public Button acceptBtn, refuseBtn;

        public MyViewHolder(View itemView) {
            super(itemView);

            assistantName = itemView.findViewById(R.id.assistantName);
            assistantPhone = itemView.findViewById(R.id.assistantphone);
            assistantEmail = itemView.findViewById(R.id.assistantEmail);
            acceptBtn = itemView.findViewById(R.id.acceptBtn);
            refuseBtn = itemView.findViewById(R.id.refuseBtn);

        }

        public void bind(final UserPojo facultyPojo) {
            assistantName.setText(facultyPojo.getName());
            assistantPhone.setText(facultyPojo.getPhone());
            assistantEmail.setText(facultyPojo.getEmail());

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptAssistants(facultyPojo);

                }
            });

            refuseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rejectAssistants(facultyPojo);

                }
            });
        }
    }

    public void updateList(List<UserPojo> newlist) {
        facultyPojos = newlist;
        this.notifyDataSetChanged();
    }


    public void acceptAssistants(final UserPojo userPojo) {//prefManager.getCenterId()
        Call<Object> call = Apiservice.getInstance().apiRequest.
                acceptAssistants(userPojo.getId());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                facultyPojos.remove(userPojo);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }


    public void rejectAssistants(final UserPojo userPojo) {//prefManager.getCenterId()
        Call<Object> call = Apiservice.getInstance().apiRequest.
                rejectAssistants(userPojo.getId());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
               facultyPojos.remove(userPojo);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }
}

