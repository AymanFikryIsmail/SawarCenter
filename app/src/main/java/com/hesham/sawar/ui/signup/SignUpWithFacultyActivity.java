package com.hesham.sawar.ui.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.hesham.sawar.R;
import com.hesham.sawar.adapter.FacultySelectAdapter;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.response.CustomResponse;
import com.hesham.sawar.data.response.FacultyResponse;
import com.hesham.sawar.data.response.UserResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.networkmodule.NetworkUtilities;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpWithFacultyActivity extends AppCompatActivity implements FacultySelectAdapter.EventListener {

    private Button nextBtn;
    private List<FacultyPojo> facultyPojos;
    private List<FacultyPojo> newFacultyPojos;

    private RecyclerView facultyRecyclerView;
    private FacultySelectAdapter facultySelectAdapter;

    PrefManager prefManager;
    ArrayList<Integer> facultyID = new ArrayList<>();
    private FrameLayout progress_view;
    private ImageView backarrowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_with_faculty);
        prefManager = new PrefManager(this);
        facultyRecyclerView = findViewById(R.id.facultyRecyclerView);
        facultyPojos = new ArrayList<>();
        newFacultyPojos = new ArrayList<>();
        progress_view = findViewById(R.id.progress_view);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        facultyRecyclerView.setLayoutManager(linearLayoutManager);
        facultySelectAdapter = new FacultySelectAdapter(this, this, facultyPojos);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtilities.isOnline(SignUpWithFacultyActivity.this)) {
                    if (NetworkUtilities.isFast(SignUpWithFacultyActivity.this)) {

                        if (facultyID.size() != 0) {
                            addFaculties();
                        } else {
                            Toast.makeText(SignUpWithFacultyActivity.this, "You must choose faculty ", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SignUpWithFacultyActivity.this, "Poor network connection , please try again", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SignUpWithFacultyActivity.this, "Please , check your network connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        backarrowId = findViewById(R.id.backarrowId);
        backarrowId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (NetworkUtilities.isOnline(SignUpWithFacultyActivity.this)) {
            if (NetworkUtilities.isFast(SignUpWithFacultyActivity.this)) {
                getFaculties();
            }else {
                Toast.makeText(SignUpWithFacultyActivity.this, "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(SignUpWithFacultyActivity.this, "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }


    public void getFaculties() {
        Call<FacultyResponse> call = Apiservice.getInstance().apiRequest.
                getFaculties();
        progress_view.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<FacultyResponse>() {
            @Override
            public void onResponse(Call<FacultyResponse> call, Response<FacultyResponse> response) {
                if (response.body() != null) {
                    if (response.body().status && response.body().cc_id != null) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        for (int i = 0; i < response.body().cc_id.size(); i++) {
                            if (response.body().cc_id.get(i).getUniv_id() == prefManager.getUniversityId()) {
                                facultyPojos.add(response.body().cc_id.get(i));
                            }
                        }
                        facultySelectAdapter = new FacultySelectAdapter(SignUpWithFacultyActivity.this, SignUpWithFacultyActivity.this, facultyPojos);
                        facultyRecyclerView.setAdapter(facultySelectAdapter);
                    } else {
                        Toast.makeText(SignUpWithFacultyActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                    }
                }
                progress_view.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FacultyResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(SignUpWithFacultyActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                progress_view.setVisibility(View.GONE);

            }
        });
    }


    public void addFaculties() {
        FacultyPojo facultyPojo = new FacultyPojo(prefManager.getCenterId(), facultyID);
        progress_view.setVisibility(View.VISIBLE);

        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                addFacultiesToCenter(facultyPojo);
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                progress_view.setVisibility(View.GONE);
                if (response.body() != null) {
                    if (response.body().status) {
                        Log.d("tag", "articles total result:: ");
                        progress_view.setVisibility(View.GONE);
                        prefManager.getCenterData().setFaculties_id(facultyID);
                        Intent i = new Intent(SignUpWithFacultyActivity.this, SignUpOwnerActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(SignUpWithFacultyActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                progress_view.setVisibility(View.GONE);
                Toast.makeText(SignUpWithFacultyActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                Log.d("tag", "articles total result:: " + t.getMessage());
            }
        });
    }

    @Override
    public void onChange(int facultyId, boolean check) {
        if (check) {
            facultyID.add(facultyId);
        } else {
            facultyID.remove((Integer) facultyId);
        }
    }
}
