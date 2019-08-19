package com.hesham.sawar.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hesham.sawar.R;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.UserResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.ui.home.HomeActivity;
import com.hesham.sawar.ui.login.LoginActivity;
import com.hesham.sawar.utils.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpOwnerActivity extends AppCompatActivity {

    private Button nextBtn;

    private PrefManager prefManager;
    private EditText nameEdit, phoneEdit, emailEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_owner);

        initView();
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }

    void initView() {
        prefManager = new PrefManager(this);
        nameEdit = findViewById(R.id.editname);
        phoneEdit = findViewById(R.id.editphone);
        emailEdit = findViewById(R.id.editemail);
    }

    public void signup() {
        final UserPojo userPojo = new UserPojo(prefManager.getCenterId(), nameEdit.getText().toString(),
                phoneEdit.getText().toString(), emailEdit.getText().toString(),
                prefManager.getType());

        Call<Object> call = Apiservice.getInstance().apiRequest.
                signup(userPojo);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                prefManager.setUserPojo(userPojo);

                Intent i = new Intent(SignUpOwnerActivity.this, HomeActivity.class);
                    startActivity(i);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });

    }
}
