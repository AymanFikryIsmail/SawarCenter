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
                    acceptAssistants(facultyPojo.getId());

                }
            });

            refuseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rejectAssistants(facultyPojo.getId());

                }
            });
        }
    }

    public void updateList(List<UserPojo> newlist) {
        facultyPojos = newlist;
        this.notifyDataSetChanged();
    }


    public void acceptAssistants(int assistantid) {//prefManager.getCenterId()
        Call<AssistantResponse> call = Apiservice.getInstance().apiRequest.
                acceptAssistants(assistantid);
        call.enqueue(new Callback<AssistantResponse>() {
            @Override
            public void onResponse(Call<AssistantResponse> call, Response<AssistantResponse> response) {
                if (response.body().status && response.body().cc_id != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    updateList(response.body().cc_id);
                }
            }

            @Override
            public void onFailure(Call<AssistantResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }


    public void rejectAssistants(int assistantid) {//prefManager.getCenterId()
        Call<AssistantResponse> call = Apiservice.getInstance().apiRequest.
                rejectAssistants(assistantid);
        call.enqueue(new Callback<AssistantResponse>() {
            @Override
            public void onResponse(Call<AssistantResponse> call, Response<AssistantResponse> response) {
                if (response.body().status && response.body().cc_id != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    updateList(response.body().cc_id);

                }
            }

            @Override
            public void onFailure(Call<AssistantResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }
}

