package com.hesham.sawar.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.hesham.sawar.R;
import com.hesham.sawar.data.model.PaperPojo;
import com.hesham.sawar.data.response.CustomResponse;
import com.hesham.sawar.data.response.PaperResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaperHomeAdapter extends RecyclerView.Adapter<PaperHomeAdapter.MyViewHolder> {
    private Context context;
    private List<PaperPojo> facultyPojos;
    private EventListener listener;
    Dialog dialog;

    PrefManager prefManager;

    int categoryId;

    public PaperHomeAdapter() {
        facultyPojos = new ArrayList<>();
    }

    public PaperHomeAdapter(Context context, List<PaperPojo> facultyPojos, int categoryId, EventListener listener) {
        this.context = context;
//            prefManager=new PrefManager(context);
        this.facultyPojos = facultyPojos;
        this.categoryId = categoryId;
        prefManager = new PrefManager(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public PaperHomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_paper, parent, false);
        return new PaperHomeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaperHomeAdapter.MyViewHolder holder, int position) {
        holder.bind(facultyPojos.get(position));
    }

    @Override
    public int getItemCount() {
        return facultyPojos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView facultyname, date, price, pages;
        public ImageView popupMenuTxt;

        public MyViewHolder(View itemView) {
            super(itemView);

            facultyname = itemView.findViewById(R.id.papername);
            date = itemView.findViewById(R.id.paperdate);
            pages = itemView.findViewById(R.id.paperpages);
            price = itemView.findViewById(R.id.paperprice);
            popupMenuTxt = itemView.findViewById(R.id.popupMenuTxt);

        }

        public void bind(final PaperPojo facultyPojo) {
            facultyname.setText("Name: " + facultyPojo.getName());
            date.setText("Date: " + facultyPojo.getDate());
            pages.setText("Pages: " + facultyPojo.getPage());
            price.setText("Price: " + facultyPojo.getPrice());
            popupMenuTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Context wrapper = new ContextThemeWrapper(context, R.style.PopupMenu);

                    PopupMenu popupMenu = new PopupMenu(context, popupMenuTxt);
                    popupMenu.inflate(R.menu.popup_menu);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.edit:

                                    dialog = new Dialog(context); // making dialog full screen
                                    LayoutInflater layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View Dview = layoutinflater.inflate(R.layout.dialog_add_paper, null);
                                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(Dview);
                                    TextView dialogButton = dialog.findViewById(R.id.addsubject);
                                    final EditText subname = dialog.findViewById(R.id.papername);
                                    final EditText paperpages = dialog.findViewById(R.id.paperpages);
                                    final EditText paperprice = dialog.findViewById(R.id.paperprice);
                                    subname.setText(facultyPojo.getName());
                                    paperpages.setText(facultyPojo.getPage()+"");
                                    paperprice.setText(facultyPojo.getPrice()+"");


                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (subname.getText().toString().isEmpty() || paperpages.getText().toString().isEmpty() || paperprice.getText().toString().isEmpty() ) {
                                                Toast.makeText(context, "you must enter values", Toast.LENGTH_LONG).show();
                                            } else {
                                                double price = Double.parseDouble(paperprice.getText().toString());
                                                int pages = Integer.parseInt(paperpages.getText().toString());
                                                facultyPojo.setName(subname.getText().toString());
                                                facultyPojo.setPage(pages);
                                                facultyPojo.setPrice(price);
                                                facultyPojo.setPaper_id(facultyPojo.getId());
                                                renameSubjects(facultyPojo);
                                                dialog.dismiss();
                                            }

                                        }
                                    });

                                    dialog.show();
                                    break;
                                case R.id.delete:
                                    final AlertDialog alertdialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                                    builder.setTitle("Delete confirmation");
                                    builder.setMessage("Are you sure to delete ");
                                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface _dialog, int which) {
                                            //do your work here
                                            facultyPojo.setPaper_id(facultyPojo.getId());
                                            deleteSubject(facultyPojo, _dialog);
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

                }
            });

        }
    }

    public interface EventListener {
        void onCheckForEmpty();

    }

    public void updateList(List<PaperPojo> newlist) {
        facultyPojos = newlist;
        this.notifyDataSetChanged();
    }

    public void renameSubjects(PaperPojo subjectPojo) {//prefManager.getCenterId()
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                renamePaper(subjectPojo);

        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().data != null) {
                        getPapers();
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                dialog.dismiss();

            }
        });
    }

    public void deleteSubject(final PaperPojo subjectPojo, final DialogInterface _dialog) {//prefManager.getCenterId()
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                removePaper(subjectPojo);
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {

                    if (response.body().status && response.body().data != null) {
                        facultyPojos.remove(subjectPojo);
                        notifyDataSetChanged();
                        _dialog.dismiss();
                        listener.onCheckForEmpty();
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                _dialog.dismiss();

            }
        });
    }


    public void getPapers() {//prefManager.getCenterId()
        Call<PaperResponse> call = Apiservice.getInstance().apiRequest.
                getPapers(categoryId, prefManager.getSubjectId());
        call.enqueue(new Callback<PaperResponse>() {
            @Override
            public void onResponse(Call<PaperResponse> call, Response<PaperResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().data != null && response.body().data.size() != 0) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        facultyPojos.clear();
                        facultyPojos.addAll(response.body().data);
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<PaperResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

}

