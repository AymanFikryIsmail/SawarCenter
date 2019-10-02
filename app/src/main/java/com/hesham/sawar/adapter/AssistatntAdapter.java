package com.hesham.sawar.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawar.R;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.AssistantResponse;
import com.hesham.sawar.data.response.UserResponse;
import com.hesham.sawar.networkmodule.Apiservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssistatntAdapter extends RecyclerView.Adapter<AssistatntAdapter.MyViewHolder> {
        private Context context;
        private List<UserPojo> facultyPojos;

    public AssistatntAdapter() {
        facultyPojos = new ArrayList<>();
        }

    public AssistatntAdapter(Context context, List<UserPojo> facultyPojos) {
            this.context = context;
//            prefManager=new PrefManager(context);
            this.facultyPojos = facultyPojos;
        }

        @NonNull
        @Override
        public AssistatntAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_assistant, parent, false);
            return new AssistatntAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull AssistatntAdapter.MyViewHolder holder, int position) {
            holder.bind(facultyPojos.get(position));
        }

        @Override
        public int getItemCount() {
            return facultyPojos.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView assistantName  , assistantPhone ,   assistantEmail;

            public Button deleteAssistant;
            public MyViewHolder(View itemView) {
                super(itemView);

                assistantName = itemView.findViewById(R.id.assistantName);
                assistantPhone = itemView.findViewById(R.id.assistantphone);
                assistantEmail = itemView.findViewById(R.id.assistantEmail);
                deleteAssistant = itemView.findViewById(R.id.deleteBtn);

            }

            public void bind(final UserPojo facultyPojo) {
                assistantName.setText(facultyPojo.getName());
                assistantPhone.setText(facultyPojo.getPhone());
                assistantEmail.setText(facultyPojo.getEmail());

                deleteAssistant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog alertdialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                        builder.setTitle("Delete confirmation");
                        builder.setMessage("Are you sure to delete ");
                        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface _dialog, int which) {
                                //do your work here
                                deleteAssistants(facultyPojo);
                            }
                        });
                        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface _dialog, int which) {
                                _dialog.dismiss();
                            }
                        });

                        alertdialog = builder.create();
                        alertdialog.show();
                    }
                });
            }
        }

        public void updateList(List<UserPojo> newlist) {
            facultyPojos = newlist;
            this.notifyDataSetChanged();
        }



    public void deleteAssistants(final UserPojo userPojo) {//prefManager.getCenterId()
        Call<Object> call = Apiservice.getInstance().apiRequest.
                deleteAssistants(userPojo.getId());
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

