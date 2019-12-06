package com.hesham.sawar.adapter;

import android.annotation.SuppressLint;
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
import com.hesham.sawar.data.model.CategoryPojo;
import com.hesham.sawar.data.model.PaperPojo;
import com.hesham.sawar.data.response.CategoryResponse;
import com.hesham.sawar.data.response.CustomResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context context;
    private List<CategoryPojo> categoryPojoList;
    private EventListener listener;
    Dialog dialog;

    PrefManager prefManager;
    int categoryId;

    public CategoryAdapter() {
        categoryPojoList = new ArrayList<>();
    }

    public CategoryAdapter(Context context, List<CategoryPojo> categoryPojoList, EventListener listener) {
        this.context = context;
        this.categoryPojoList = categoryPojoList;
        prefManager = new PrefManager(context);
        this.listener = listener;
        if (categoryPojoList.size() > 0) {
            categoryId = categoryPojoList.get(0).getId();
        }
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category, parent, false);
        return new CategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        holder.bind(categoryPojoList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryPojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryName, date, price, pages;
        public ImageView popupMenuTxt;
        public View categoryViewId;

        public MyViewHolder(View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.catId);
            popupMenuTxt = itemView.findViewById(R.id.popupMenuTxt);
            categoryViewId = itemView.findViewById(R.id.categoryViewId);
        }

        public void bind(final CategoryPojo facultyPojo) {
            categoryName.setText(facultyPojo.getName());
            categoryViewId.setVisibility(View.INVISIBLE);
            categoryName.setTextColor(context.getResources().getColor(R.color.grey1));
            if (categoryId == facultyPojo.getId()) {
                categoryViewId.setVisibility(View.VISIBLE);
                categoryName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
//                    Context wrapper = new ContextThemeWrapper(context, R.style.PopupMenu);
            popupMenuTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, popupMenuTxt);
                    popupMenu.inflate(R.menu.popup_menu);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.edit:
                                    dialog = new Dialog(context); // making dialog full screen
                                    LayoutInflater layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View Dview = layoutinflater.inflate(R.layout.dialog_add_subject, null);
                                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(Dview);
                                    TextView dialogButton = dialog.findViewById(R.id.addsubject);
                                    final EditText subname = dialog.findViewById(R.id.subjectname);
                                    subname.setText(facultyPojo.getName());

                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (subname.getText().toString().isEmpty()) {
                                                Toast.makeText(context, "you must enter values", Toast.LENGTH_LONG).show();
                                            } else {
//                                        facultyPojo.setName(subname.getText().toString());
                                                renameCategory(subname.getText().toString(), facultyPojo.getId());
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
                                            removeCategory(facultyPojo, _dialog);
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
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    categoryId = facultyPojo.getId();
                    categoryName.setTextColor(R.color.colorPrimary);
                    categoryViewId.setVisibility(View.VISIBLE);
                    listener.onCategoryClick(facultyPojo.getId());
                    notifyDataSetChanged();
                }
            });
        }
    }


    public interface EventListener {
        void onCheckForEmpty();

        void onCategoryClick(int catId);

    }

    public void updateList(List<CategoryPojo> newlist) {
        categoryPojoList = newlist;
        this.notifyDataSetChanged();
    }

    public void renameCategory(String categoryPojo, int id) {//prefManager.getCenterId()
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                renameCategory(categoryPojo, id);

        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().data != null) {
                        getCategory();
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

    public void removeCategory(final CategoryPojo categoryPojo, final DialogInterface _dialog) {//prefManager.getCenterId()
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                removeCategory(categoryPojo.getId());
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {

                    if (response.body().status && response.body().data != null) {
                        categoryPojoList.remove(categoryPojo);
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


    public void getCategory() {//prefManager.getCenterId()
        Call<CategoryResponse> call = Apiservice.getInstance().apiRequest.
                getCategory(prefManager.getSubjectId());
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().data != null && response.body().data.size() != 0) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        categoryPojoList.clear();
                        categoryPojoList.addAll(response.body().data);
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

}

