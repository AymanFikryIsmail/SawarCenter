package com.hesham.sawar.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawar.R;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.model.SubjectPojo;
import com.hesham.sawar.data.response.AssistantResponse;
import com.hesham.sawar.data.response.SubjectResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.ui.subjects.FirstTermFragment;
import com.hesham.sawar.ui.subjects.PaperFragment;
import com.hesham.sawar.ui.subjects.YearsFragment;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectHomeAdapter extends RecyclerView.Adapter<SubjectHomeAdapter.MyViewHolder> {
    private Context context;
    private List<SubjectPojo> facultyPojos;
    private EventListener listener;

    PrefManager prefManager;

    //dialog
    Dialog dialog ;

    public SubjectHomeAdapter() {
        facultyPojos = new ArrayList<>();
    }
    int years, term;

    public SubjectHomeAdapter(Context context, EventListener listener, List<SubjectPojo> facultyPojos , int years ,int term) {
        this.context = context;
        prefManager = new PrefManager(context);
        this.facultyPojos = facultyPojos;
        this.listener = listener;
        this.years=years;
        this.term=term;

    }

    @NonNull
    @Override
    public SubjectHomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_termsubject, parent, false);
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

        public TextView facultyname , popupMenuTxt;

        public MyViewHolder(View itemView) {
            super(itemView);

            facultyname = itemView.findViewById(R.id.facultyname);
            popupMenuTxt = itemView.findViewById(R.id.popupMenuTxt);

        }

        public void bind(final SubjectPojo facultyPojo) {
            facultyname.setText(facultyPojo.getName());

            popupMenuTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Context wrapper = new ContextThemeWrapper(context, R.style.PopupMenu);

                    PopupMenu popupMenu =new PopupMenu(context,popupMenuTxt);
                    popupMenu.inflate(R.menu.popup_menu);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.edit:

                                    dialog = new Dialog(context); // making dialog full screen
                                    LayoutInflater layoutinflater  = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                                    View Dview = layoutinflater.inflate(R.layout.dialog_add_subject,null);
                                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(Dview);
                                    Button dialogButton = dialog.findViewById(R.id.addsubject);
                                    final EditText subname = dialog.findViewById(R.id.subjectname);

                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//                                            int newsId=associationNews.getId();
//                                            int associationId=associationNews.getAssociation_id();
                                            facultyPojo.setName(subname.getText().toString());
                                            facultyPojo.setSub_id(facultyPojo.getId());
                                            renameSubjects(facultyPojo);

                                        }
                                    });

                                    dialog.show();
                                    break;
                                case R.id.delete:
                                    final AlertDialog alertdialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Delete confirmation");
                                    builder.setMessage("Are you sure to delete ");
                                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface _dialog, int which) {
                                            //do your work here
                                            facultyPojo.setSub_id(facultyPojo.getId());
                                            deleteSubject(facultyPojo,_dialog);
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

                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prefManager.setSubjectId(facultyPojo.getId());
                    PaperFragment yearsFragment = new PaperFragment();
                    listener.onEvent(yearsFragment);

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
    public void renameSubjects(SubjectPojo subjectPojo) {//prefManager.getCenterId()
        Call<Object> call = Apiservice.getInstance().apiRequest.
                renameSubjects(subjectPojo);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
              getSubjects();
              dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                dialog.dismiss();

            }
        });
    }
    public void deleteSubject(final SubjectPojo subjectPojo,final DialogInterface _dialog) {//prefManager.getCenterId()
        Call<Object> call = Apiservice.getInstance().apiRequest.
                removeSubjects(subjectPojo);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                facultyPojos.remove(subjectPojo);
                SubjectHomeAdapter.this.notifyDataSetChanged();
                _dialog.dismiss();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                _dialog.dismiss();

            }
        });
    }


    public void getSubjects() {//prefManager.getCenterId()
        SubjectPojo subjectPojo = new SubjectPojo(prefManager.getCenterId(), prefManager.getFacultyId(), years, term);
        Call<SubjectResponse> call = Apiservice.getInstance().apiRequest.
                getAllSubjects(subjectPojo);
        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                if (response.body().status && response.body().cc_id != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.clear();
                    facultyPojos.addAll(response.body().cc_id);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }
}

