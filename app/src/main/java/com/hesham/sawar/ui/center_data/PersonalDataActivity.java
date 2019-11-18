package com.hesham.sawar.ui.center_data;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hesham.sawar.R;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.SignUpResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.ui.home.HomeActivity;
import com.hesham.sawar.utils.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDataActivity extends AppCompatActivity {


    PrefManager prefManager;
    private TextView nameEdit, phoneedit, emailEdit;
    private int universityId;
    private FrameLayout progress_view;

    private Button update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        initView();
    }


    void initView() {
        prefManager = new PrefManager(this);
        nameEdit = findViewById(R.id.nameedit);
        phoneedit = findViewById(R.id.phoneedit);
        emailEdit = findViewById(R.id.emailedit);
        progress_view = findViewById(R.id.progress_view);
        update = findViewById(R.id.update);

        nameEdit.setText(prefManager.getUserPojo().getName());
        phoneedit.setText(prefManager.getUserPojo().getPhone());
        emailEdit.setText(prefManager.getUserPojo().getEmail());


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               update();
            }
        });
    }


    public void update() {

        progress_view.setVisibility(View.VISIBLE);
        final UserPojo userPojo = prefManager.getUserPojo();
        Call<SignUpResponse> call = Apiservice.getInstance().apiRequest.
                updateProfile(prefManager.getUserPojo().getId(), nameEdit.getText().toString(), emailEdit.getText().toString(),
                        phoneedit.getText().toString());
        progress_view.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        userPojo.setName(nameEdit.getText().toString());
                        userPojo.setEmail(emailEdit.getText().toString());
                        userPojo.setPhone(phoneedit.getText().toString());
                        prefManager.setUserPojo(userPojo);
                        prefManager.setToken("registered");
                        Intent i = new Intent(PersonalDataActivity.this, HomeActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(PersonalDataActivity.this, response.message(), Toast.LENGTH_LONG).show();
                    }
                }
                progress_view.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                progress_view.setVisibility(View.GONE);

            }
        });

    }


}
