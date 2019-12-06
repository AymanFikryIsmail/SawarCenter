package com.hesham.sawar.ui.paper;

import android.app.Dialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hesham.sawar.R;
import com.hesham.sawar.adapter.CategoryAdapter;
import com.hesham.sawar.adapter.PaperHomeAdapter;
import com.hesham.sawar.data.model.CategoryPojo;
import com.hesham.sawar.data.model.PaperPojo;
import com.hesham.sawar.data.response.CategoryResponse;
import com.hesham.sawar.data.response.CustomResponse;
import com.hesham.sawar.data.response.PaperResponse;
import com.hesham.sawar.databinding.FragmentPaperBinding;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.networkmodule.NetworkUtilities;
import com.hesham.sawar.ui.home.HomeActivity;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaperFragment extends Fragment implements PaperHomeAdapter.EventListener ,  CategoryAdapter.EventListener {
    public static PaperFragment newInstance() {
        PaperFragment fragment = new PaperFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private List<PaperPojo> facultyPojos;
    private List<CategoryPojo> categoryPojos;

    private CategoryAdapter categoryAdapter ;
    private PaperHomeAdapter facultySelectAdapter;
    PrefManager prefManager;
    private int paperCategory;

    private FragmentPaperBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_paper, container, false);
         binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_paper, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);

        prefManager = new PrefManager(getContext());
        categoryPojos= new ArrayList<>();
        facultyPojos = new ArrayList<>();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        binding.addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryDialog();
            }
        });
        hideEmpty();
        initView(binding.getRoot());

        binding.backarrowId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).onBackPressed();
            }
        });
        binding.emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        callApi();
    }

    private  void callApi() {
        hideEmpty();
        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {
             getCategory();
            } else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }

    void initView(View view) {
        RecyclerView.LayoutManager categoryLayoutManager =   new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.categoryRecyclerView.setLayoutManager(categoryLayoutManager);

        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        binding.termRecyclerView.setLayoutManager(gridLayoutManager);
    }

    public void getCategory() {//prefManager.getCenterId()
        Call<CategoryResponse> call = Apiservice.getInstance().apiRequest.
                getCategory( prefManager.getSubjectId());
        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {

                binding.progressView.setVisibility(View.VISIBLE);

                call.enqueue(new Callback<CategoryResponse>() {
                    @Override
                    public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                        if (response.body() != null) {
                            if (response.body().status && response.body().data != null && response.body().data.size() != 0) {
                                Log.d("tag", "articles total result:: " + response.body().getMessage());
                                categoryPojos.clear();
                                categoryPojos.addAll(response.body().data);
                                if (categoryPojos.size() == 0) {
                                    showEmpty();
                                } else {
                                    hideEmpty();
                                    paperCategory=categoryPojos.get(0).getId();
                                }
                                categoryAdapter = new CategoryAdapter(getContext(), categoryPojos, PaperFragment.this);
                                binding.categoryRecyclerView.setAdapter(categoryAdapter);
                                getPapers();
                            } else {
                                categoryPojos.clear();
                                categoryAdapter = new CategoryAdapter(getContext(), categoryPojos, PaperFragment.this);
                                binding.categoryRecyclerView.setAdapter(categoryAdapter);
                                showEmpty();
                            }
                        }
                        binding.progressView.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<CategoryResponse> call, Throwable t) {
                        Log.d("tag", "articles total result:: " + t.getMessage());
                        showEmpty();
                        binding.progressView.setVisibility(View.GONE);

                        Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }


    public void getPapers() {//prefManager.getCenterId()
        Call<PaperResponse> call = Apiservice.getInstance().apiRequest.
                getPapers(paperCategory, prefManager.getSubjectId());
        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {

                binding.progressView.setVisibility(View.VISIBLE);

                call.enqueue(new Callback<PaperResponse>() {
                    @Override
                    public void onResponse(Call<PaperResponse> call, Response<PaperResponse> response) {
                        if (response.body() != null) {
                            if (response.body().status && response.body().data != null && response.body().data.size() != 0) {
                                Log.d("tag", "articles total result:: " + response.body().getMessage());
                                facultyPojos.clear();
                                facultyPojos.addAll(response.body().data);
                                if (facultyPojos.size() == 0) {
                                    showEmpty();
                                } else {
                                    hideEmpty();
                                }
                                facultySelectAdapter = new PaperHomeAdapter(getContext(), facultyPojos, paperCategory, PaperFragment.this);
                                binding.termRecyclerView.setAdapter(facultySelectAdapter);
                            } else {
                                facultyPojos.clear();
                                facultySelectAdapter = new PaperHomeAdapter(getContext(), facultyPojos, paperCategory, PaperFragment.this);
                                binding.termRecyclerView.setAdapter(facultySelectAdapter);
                                showEmpty();
                            }
                        }
                        binding.progressView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<PaperResponse> call, Throwable t) {
                        Log.d("tag", "articles total result:: " + t.getMessage());
                        showEmpty();
                        binding.progressView.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }


    private void showDialog() {
        final Dialog dialog = new Dialog(getContext());
//        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_add_paper);
        TextView dialogButton = dialog.findViewById(R.id.addsubject);
        final EditText subname = dialog.findViewById(R.id.papername);
        final EditText paperpages = dialog.findViewById(R.id.paperpages);
        final EditText paperprice = dialog.findViewById(R.id.paperprice);


        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // need validation here
                if (subname.getText().toString().isEmpty() || paperpages.getText().toString().isEmpty() || paperprice.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "you must enter values", Toast.LENGTH_LONG).show();
                } else {
                    double price = Double.parseDouble(paperprice.getText().toString());
                    int pages = Integer.parseInt(paperpages.getText().toString());
                    if (NetworkUtilities.isOnline(getContext())) {
                        if (NetworkUtilities.isFast(getContext())) {

                            addSubjects(subname.getText().toString(), pages, price);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
                    }
                }

//                Intent intent=new Intent(getContext(), MainActivity.class);
//                startActivity(intent);
            }
        });

        dialog.show();
    }

    private void showCategoryDialog() {
        final Dialog dialog = new Dialog(getContext());
//        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_add_subject);
        TextView dialogButton = dialog.findViewById(R.id.addsubject);
        final EditText subname = dialog.findViewById(R.id.subjectname);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // need validation here
                if (subname.getText().toString().isEmpty() ) {
                    Toast.makeText(getContext(), "you must enter values", Toast.LENGTH_LONG).show();
                } else {
                    if (NetworkUtilities.isOnline(getContext())) {
                        if (NetworkUtilities.isFast(getContext())) {

                            addCategory(subname.getText().toString());
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        dialog.show();
    }
    public void addCategory(String categoryPojo) {//prefManager.getCenterId()
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                addCategory(categoryPojo, prefManager.getSubjectId());

        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().data != null) {
                        getCategory();
                    }
                }
            }
            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addSubjects(String name, int pages, double price) {//prefManager.getCenterId()
        PaperPojo subjectPojo = new PaperPojo(name, prefManager.getCenterId(),pages, prefManager.getSubjectId(), price, paperCategory);
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                addPaper(subjectPojo);
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body().status && response.body().data != null) {
                    getPapers();
                }
            }
            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void checkForEmpty() {
        if (facultyPojos.size() == 0) {
            showEmpty();
        } else {
            hideEmpty();
        }
    }

    void showEmpty() {
        binding.emptyLayout.setVisibility(View.VISIBLE);
    }

    void hideEmpty() {
        binding.emptyLayout.setVisibility(View.GONE);
    }

    @Override
    public void onCheckForEmpty() {
        checkForEmpty();
    }

    @Override
    public void onCategoryClick(int catId) {
        paperCategory=catId;
        getPapers();
    }


}