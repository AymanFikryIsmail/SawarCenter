package com.hesham.sawar.ui.subjects;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hesham.sawar.R;
import com.hesham.sawar.adapter.FacultySelectAdapter;
import com.hesham.sawar.adapter.PaperHomeAdapter;
import com.hesham.sawar.adapter.SubjectHomeAdapter;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.model.PaperPojo;
import com.hesham.sawar.data.model.SubjectPojo;
import com.hesham.sawar.data.response.CustomResponse;
import com.hesham.sawar.data.response.PaperResponse;
import com.hesham.sawar.data.response.SubjectResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.networkmodule.NetworkUtilities;
import com.hesham.sawar.ui.home.HomeActivity;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaperFragment extends Fragment implements View.OnClickListener, PaperHomeAdapter.EventListener {
    public static PaperFragment newInstance() {
        PaperFragment fragment = new PaperFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private List<PaperPojo> facultyPojos;
    TextView emptyLayout;
    private FrameLayout progress_view;

    private RecyclerView facultyRecyclerView;
    private PaperHomeAdapter facultySelectAdapter;
    PrefManager prefManager;
    private String paperType;
    private ImageView backarrowId;

    TextView lectureId, handoutId, sectionId, courseId, revisionId;
    View lectureViewId, handoutViewId, sectionViewId, courseViewId, revisionViewId;

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
        View view = inflater.inflate(R.layout.fragment_paper, container, false);
        prefManager = new PrefManager(getContext());

        facultyRecyclerView = view.findViewById(R.id.termRecyclerView);
        facultyPojos = new ArrayList<>();
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        emptyLayout = view.findViewById(R.id.emptyLayout);
        progress_view = view.findViewById(R.id.progress_view);

        hideEmpty();
        initView(view);
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new PaperHomeAdapter(getContext(), facultyPojos, paperType, PaperFragment.this);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {
                getPapers();
            } else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }


        backarrowId = view.findViewById(R.id.backarrowId);
        backarrowId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) getActivity()).onBackPressed();
            }
        });
        return view;
    }

    void initView(View view) {

        paperType = "l";
        lectureId = view.findViewById(R.id.lectureId);
        handoutId = view.findViewById(R.id.handoutId);
        sectionId = view.findViewById(R.id.sectionId);
        courseId = view.findViewById(R.id.courseId);
        revisionId = view.findViewById(R.id.revisionId);


        lectureViewId = view.findViewById(R.id.lectureViewId);
        handoutViewId = view.findViewById(R.id.handoutViewId);
        sectionViewId = view.findViewById(R.id.sectionViewId);
        courseViewId = view.findViewById(R.id.courseViewId);
        revisionViewId = view.findViewById(R.id.revisionViewId);

        lectureId.setOnClickListener(this);
        handoutId.setOnClickListener(this);
        sectionId.setOnClickListener(this);
        courseId.setOnClickListener(this);
        revisionId.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.lectureId:
                paperType = "l";
                lectureId.setTextColor(getResources().getColor(R.color.colorPrimary));
                handoutId.setTextColor(getResources().getColor(R.color.grey1));
                sectionId.setTextColor(getResources().getColor(R.color.grey1));
                courseId.setTextColor(getResources().getColor(R.color.grey1));
                revisionId.setTextColor(getResources().getColor(R.color.grey1));

                lectureViewId.setVisibility(View.VISIBLE);
                handoutViewId.setVisibility(View.GONE);
                sectionViewId.setVisibility(View.GONE);
                courseViewId.setVisibility(View.GONE);
                revisionViewId.setVisibility(View.GONE);

                getPapers();
                break;
            case R.id.handoutId:
                paperType = "h";
                lectureId.setTextColor(getResources().getColor(R.color.grey1));
                handoutId.setTextColor(getResources().getColor(R.color.colorPrimary));
                sectionId.setTextColor(getResources().getColor(R.color.grey1));
                courseId.setTextColor(getResources().getColor(R.color.grey1));
                revisionId.setTextColor(getResources().getColor(R.color.grey1));

                lectureViewId.setVisibility(View.GONE);
                handoutViewId.setVisibility(View.VISIBLE);
                sectionViewId.setVisibility(View.GONE);
                courseViewId.setVisibility(View.GONE);
                revisionViewId.setVisibility(View.GONE);
                getPapers();
                break;
            case R.id.sectionId:
                paperType = "s";
                lectureId.setTextColor(getResources().getColor(R.color.grey1));
                handoutId.setTextColor(getResources().getColor(R.color.grey1));
                sectionId.setTextColor(getResources().getColor(R.color.colorPrimary));
                courseId.setTextColor(getResources().getColor(R.color.grey1));
                revisionId.setTextColor(getResources().getColor(R.color.grey1));

                lectureViewId.setVisibility(View.GONE);
                handoutViewId.setVisibility(View.GONE);
                sectionViewId.setVisibility(View.VISIBLE);
                courseViewId.setVisibility(View.GONE);
                revisionViewId.setVisibility(View.GONE);

                getPapers();
                break;
            case R.id.courseId:
                paperType = "c";
                lectureId.setTextColor(getResources().getColor(R.color.grey1));
                handoutId.setTextColor(getResources().getColor(R.color.grey1));
                sectionId.setTextColor(getResources().getColor(R.color.grey1));
                courseId.setTextColor(getResources().getColor(R.color.colorPrimary));
                revisionId.setTextColor(getResources().getColor(R.color.grey1));

                lectureViewId.setVisibility(View.GONE);
                handoutViewId.setVisibility(View.GONE);
                sectionViewId.setVisibility(View.GONE);
                courseViewId.setVisibility(View.VISIBLE);
                revisionViewId.setVisibility(View.GONE);

                getPapers();
                break;
            case R.id.revisionId:
                paperType = "r";
                lectureId.setTextColor(getResources().getColor(R.color.grey1));
                handoutId.setTextColor(getResources().getColor(R.color.grey1));
                sectionId.setTextColor(getResources().getColor(R.color.grey1));
                courseId.setTextColor(getResources().getColor(R.color.grey1));
                revisionId.setTextColor(getResources().getColor(R.color.colorPrimary));

                lectureViewId.setVisibility(View.GONE);
                handoutViewId.setVisibility(View.GONE);
                sectionViewId.setVisibility(View.GONE);
                courseViewId.setVisibility(View.GONE);
                revisionViewId.setVisibility(View.VISIBLE);
                getPapers();
                break;
        }

    }


    public void getPapers() {//prefManager.getCenterId()
        Call<PaperResponse> call = Apiservice.getInstance().apiRequest.
                getPapers(paperType, prefManager.getSubjectId());
        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {

                progress_view.setVisibility(View.VISIBLE);

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
                                facultySelectAdapter = new PaperHomeAdapter(getContext(), facultyPojos, paperType, PaperFragment.this);
                                facultyRecyclerView.setAdapter(facultySelectAdapter);
                            } else {
                                facultyPojos.clear();
                                facultySelectAdapter = new PaperHomeAdapter(getContext(), facultyPojos, paperType, PaperFragment.this);
                                facultyRecyclerView.setAdapter(facultySelectAdapter);
                                showEmpty();
                            }
                        }
                        progress_view.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<PaperResponse> call, Throwable t) {
                        Log.d("tag", "articles total result:: " + t.getMessage());
                        showEmpty();
                        progress_view.setVisibility(View.GONE);

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

    public void addSubjects(String name, int pages, double price) {//prefManager.getCenterId()
        PaperPojo subjectPojo = new PaperPojo(name, pages, prefManager.getSubjectId(), price, paperType);
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
        emptyLayout.setVisibility(View.VISIBLE);
    }

    void hideEmpty() {
        emptyLayout.setVisibility(View.GONE);
    }

    @Override
    public void onCheckForEmpty() {
        checkForEmpty();
    }
}