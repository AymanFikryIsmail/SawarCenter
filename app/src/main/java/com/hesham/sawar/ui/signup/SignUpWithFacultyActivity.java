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

import com.hesham.sawar.R;
import com.hesham.sawar.adapter.FacultySelectAdapter;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.response.FacultyResponse;
import com.hesham.sawar.data.response.UserResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpWithFacultyActivity extends AppCompatActivity {

    private Button nextBtn;
    private List<FacultyPojo> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private FacultySelectAdapter facultySelectAdapter;

    PrefManager prefManager;
    ArrayList<Integer> facultyID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_with_faculty);
        prefManager = new PrefManager(this);
        facultyRecyclerView = findViewById(R.id.facultyRecyclerView);
        facultyPojos = new ArrayList<>();
        getFaculties();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        facultyRecyclerView.setLayoutManager(linearLayoutManager);
        facultySelectAdapter = new FacultySelectAdapter(this, facultyPojos);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addFaculties();
            }
        });
    }


    public void getFaculties() {
        Call<FacultyResponse> call = Apiservice.getInstance().apiRequest.
                getFaculties();
        call.enqueue(new Callback<FacultyResponse>() {
            @Override
            public void onResponse(Call<FacultyResponse> call, Response<FacultyResponse> response) {
                if (response.body().status && response.body().cc_id != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.addAll(response.body().cc_id);
                    facultySelectAdapter = new FacultySelectAdapter(SignUpWithFacultyActivity.this, facultyPojos);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }
            }

            @Override
            public void onFailure(Call<FacultyResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }


    public void addFaculties() {
        facultyID.add(1);
        facultyID.add(2);
        FacultyPojo facultyPojo = new FacultyPojo(prefManager.getCenterId(), facultyID);

        Call<Object> call = Apiservice.getInstance().apiRequest.
                addFacultiesToCenter(facultyPojo);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("tag", "articles total result:: ");
                Intent i = new Intent(SignUpWithFacultyActivity.this, SignUpOwnerActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

}
