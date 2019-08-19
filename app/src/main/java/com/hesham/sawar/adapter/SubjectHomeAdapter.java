package com.hesham.sawar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawar.R;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.model.SubjectPojo;
import com.hesham.sawar.ui.subjects.YearsFragment;

import java.util.ArrayList;
import java.util.List;

public class SubjectHomeAdapter extends RecyclerView.Adapter<SubjectHomeAdapter.MyViewHolder> {
        private Context context;
        private List<SubjectPojo> facultyPojos;

    public SubjectHomeAdapter() {
        facultyPojos = new ArrayList<>();
        }

    public SubjectHomeAdapter(Context context, List<SubjectPojo> facultyPojos ) {
            this.context = context;
//            prefManager=new PrefManager(context);
            this.facultyPojos = facultyPojos;
        }

        @NonNull
        @Override
        public SubjectHomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_faultyhome, parent, false);
            return new SubjectHomeAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull SubjectHomeAdapter.MyViewHolder holder, int position) {
            holder.bind(facultyPojos.get(position));
        }

        @Override
        public int getItemCount() {
            return facultyPojos.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView facultyname;

            public MyViewHolder(View itemView) {
                super(itemView);

                facultyname = itemView.findViewById(R.id.facultyname);
            }

            public void bind(final SubjectPojo facultyPojo) {
                facultyname.setText(facultyPojo.getName());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        YearsFragment yearsFragment = new YearsFragment(facultyPojo.getYear());
                    }
                });

            }
        }
    public interface EventListener {
        void onEvent(Fragment data);
    }
        public void updateList(List<SubjectPojo> newlist) {
            facultyPojos = newlist;
            this.notifyDataSetChanged();
        }
    }

