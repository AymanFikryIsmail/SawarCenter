package com.hesham.sawar.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawar.R;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.AssistantResponse;
import com.hesham.sawar.data.response.CustomResponse;
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
                    new AlertDialog.Builder(context,  R.style.AlertDialogCustom)
                            .setMessage("Do you want to reject this assistant ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    rejectAssistants(facultyPojo);
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .show();


                }
            });
        }
    }

    public void updateList(List<UserPojo> newlist) {
        facultyPojos = newlist;
        this.notifyDataSetChanged();
    }


    public void acceptAssistants(final UserPojo userPojo) {//prefManager.getCenterId()
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                acceptAssistants(userPojo.getId());
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        facultyPojos.remove(userPojo);
                        notifyDataSetChanged();
                    }
                }}

            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }


    public void rejectAssistants(final UserPojo userPojo) {//prefManager.getCenterId()
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                rejectAssistants(userPojo.getId());
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        facultyPojos.remove(userPojo);
                        notifyDataSetChanged();
                    }
                }}
            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }
}

