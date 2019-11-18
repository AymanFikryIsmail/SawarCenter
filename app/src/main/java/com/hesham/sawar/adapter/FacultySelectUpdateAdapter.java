package com.hesham.sawar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawar.R;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class FacultySelectUpdateAdapter extends RecyclerView.Adapter<FacultySelectUpdateAdapter.MyViewHolder> {
    private Context context;
    private List<FacultyPojo> facultyPojos;
    EventListener eventListener;

    private PrefManager prefManager;
    public FacultySelectUpdateAdapter() {
        facultyPojos = new ArrayList<>();
    }

    public FacultySelectUpdateAdapter(Context context, EventListener eventListener, List<FacultyPojo> facultyPojos) {
        this.context = context;
        this.eventListener = eventListener;
            prefManager=new PrefManager(context);
        this.facultyPojos = facultyPojos;
    }

    @NonNull
    @Override
    public FacultySelectUpdateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_faultyselect, parent, false);
        return new FacultySelectUpdateAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultySelectUpdateAdapter.MyViewHolder holder, int position) {
        holder.bind(facultyPojos.get(position));
    }

    @Override
    public int getItemCount() {
        return facultyPojos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView facultyname;
        public CheckBox facultycheck;

        public MyViewHolder(View itemView) {
            super(itemView);

            facultyname = itemView.findViewById(R.id.facultyname);
            facultycheck = itemView.findViewById(R.id.facultycheck);
        }

        public void bind(final FacultyPojo facultyPojo) {
            facultyname.setText(facultyPojo.getName());
            for (int i=0 ; i<prefManager.getCenterData().getFaculties_id().size() ; i ++){
                if (prefManager.getCenterData().getFaculties_id().get(i)==facultyPojo.getId()){
                    facultycheck.setChecked(true);
                }
            }
            facultycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    eventListener.onChange(facultyPojo.getId(), b);
                }
            });
        }
    }

    public void updateList(List<FacultyPojo> newlist) {
        facultyPojos = newlist;
        this.notifyDataSetChanged();
    }


    public interface EventListener {

        void onChange(int facultyId, boolean check);
    }
}

