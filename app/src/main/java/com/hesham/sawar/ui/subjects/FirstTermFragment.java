package com.hesham.sawar.ui.subjects;


import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hesham.sawar.R;
import com.hesham.sawar.adapter.FacultyHomeAdapter;
import com.hesham.sawar.adapter.FacultySelectAdapter;
import com.hesham.sawar.adapter.SubjectHomeAdapter;
import com.hesham.sawar.data.model.DepartmentPojo;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.model.SubjectPojo;
import com.hesham.sawar.data.response.CustomResponse;
import com.hesham.sawar.data.response.DepartmentResponse;
import com.hesham.sawar.data.response.FacultyResponse;
import com.hesham.sawar.data.response.SubjectResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.networkmodule.NetworkUtilities;
import com.hesham.sawar.ui.home.HomeActivity;
import com.hesham.sawar.ui.home.HomeFragment;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstTermFragment extends Fragment implements SubjectHomeAdapter.EventListener {


    private List<SubjectPojo> facultyPojos;
    private List<DepartmentPojo> depPojos;
    private Integer depId;
    private RecyclerView facultyRecyclerView;
    private SubjectHomeAdapter facultySelectAdapter;

    PrefManager prefManager;
    private int years;
    LinearLayout emptyLayout;
    private FrameLayout progress_view;

    private Spinner departmentSpinner;

    public FirstTermFragment(int years) {
        this.years = years;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_term, container, false);
        prefManager = new PrefManager(getContext());
        facultyRecyclerView = view.findViewById(R.id.termRecyclerView);
        facultyPojos = new ArrayList<>();
        depPojos = new ArrayList<>();
        emptyLayout = view.findViewById(R.id.emptyLayout);
        progress_view = view.findViewById(R.id.progress_view);
        departmentSpinner = view.findViewById(R.id.departmentSpinner);
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (depPojos.size() != 0) {
                    depId = depPojos.get(i).getId();
                }
                getFilteredSubjects();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllDepartments();
            }
        });
        hideEmpty();
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new SubjectHomeAdapter(getContext(), this, facultyPojos, years, 1, depId);
        facultyRecyclerView.setAdapter(facultySelectAdapter);

        return view;
    }

    public void getFilteredSubjects() {//prefManager.getCenterId()
        SubjectPojo subjectPojo = new SubjectPojo(prefManager.getCenterId(), prefManager.getFacultyId(), years, 1, depId);
        Call<SubjectResponse> call = Apiservice.getInstance().apiRequest.
                getFilteredSubjects(subjectPojo);
        progress_view.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().cc_id != null) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        facultyPojos.clear();
                        facultyPojos.addAll(response.body().cc_id);
                        if (facultyPojos.size() == 0) {
                            showEmpty();
                        } else {
                            hideEmpty();
                        }
                        facultySelectAdapter = new SubjectHomeAdapter(getContext(), FirstTermFragment.this, facultyPojos, years, 1, depId);
                        facultyRecyclerView.setAdapter(facultySelectAdapter);
                    }
                }
                progress_view.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                showEmpty();
                progress_view.setVisibility(View.GONE);
            }
        });
    }

    public void getSubjects() {//prefManager.getCenterId()
        SubjectPojo subjectPojo = new SubjectPojo(prefManager.getCenterId(), prefManager.getFacultyId(), years, 1);
        Call<SubjectResponse> call = Apiservice.getInstance().apiRequest.
                getAllSubjects(subjectPojo);
        progress_view.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().cc_id != null) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        facultyPojos.clear();
                        facultyPojos.addAll(response.body().cc_id);
                        if (facultyPojos.size() == 0) {
                            showEmpty();
                        } else {
                            hideEmpty();
                        }
                        facultySelectAdapter = new SubjectHomeAdapter(getContext(), FirstTermFragment.this, facultyPojos, years, 1, depId);
                        facultyRecyclerView.setAdapter(facultySelectAdapter);
                    }
                }
                progress_view.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                showEmpty();
                progress_view.setVisibility(View.GONE);
            }
        });
    }

    public void getAllDepartments() {//prefManager.getCenterId()
        Call<DepartmentResponse> call = Apiservice.getInstance().apiRequest.
                getAllDepartments(prefManager.getFacultyId());
        progress_view.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<DepartmentResponse>() {
            @Override
            public void onResponse(Call<DepartmentResponse> call, Response<DepartmentResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().cc_id != null) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        depPojos.clear();
                        depPojos.addAll(response.body().cc_id);
                        if (depPojos.size() == 0) {
                            departmentSpinner.setVisibility(View.GONE);
                            getSubjects();
                        } else {
                            departmentSpinner.setVisibility(View.VISIBLE);
                            depId = depPojos.get(0).getId();
                        }
                        List<String> depList = new ArrayList<>(depPojos.size());
                        for (DepartmentPojo departmentPojo : depPojos) {
                            depList.add(departmentPojo.getName());
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, depList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        departmentSpinner.setAdapter(dataAdapter);
                    }
                }
                progress_view.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DepartmentResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                showEmpty();
                progress_view.setVisibility(View.GONE);
            }
        });
    }


    private void showDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_add_subject);
        TextView dialogButton = dialog.findViewById(R.id.addsubject);
        final EditText subname = dialog.findViewById(R.id.subjectname);


        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtilities.isOnline(getContext())) {
                    if (NetworkUtilities.isFast(getContext())) {
                        addSubjects(subname.getText().toString());
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
                }
//                Intent intent=new Intent(getContext(), MainActivity.class);
//                startActivity(intent);
            }
        });

        dialog.show();
    }

    public void addSubjects(String name) {//prefManager.getCenterId()
        SubjectPojo subjectPojo = new SubjectPojo(prefManager.getCenterId(), prefManager.getFacultyId(), years, 1, name, depId);
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                addSubjects(subjectPojo);
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body().status && response.body().data != null) {

                    if (depId == null) {
                        getSubjects();
                    } else {
                        getFilteredSubjects();
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

    @Override
    public void onEvent(Fragment data) {
        ((HomeActivity) getActivity()).loadFragment(data);

    }

    @Override
    public void onCheckForEmpty() {
        checkForEmpty();
    }

    public void checkForEmpty() {
        if (facultyPojos.size() == 0) {
            showEmpty();
        } else {
            hideEmpty();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {
                getAllDepartments();
            } else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }

    void showEmpty() {
        emptyLayout.setVisibility(View.VISIBLE);
    }

    void hideEmpty() {
        emptyLayout.setVisibility(View.GONE);
    }
}
