package com.hesham.sawar.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hesham.sawar.R;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.UserResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.ui.signup.SignUpActivity;
import com.hesham.sawar.ui.signup.SignUpOwnerActivity;
import com.hesham.sawar.ui.signup.SignUpWithFacultyActivity;
import com.hesham.sawar.utils.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView gotoRegister;
    PrefManager prefManager;
    private EditText emailEdit ,passwordedit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        gotoRegister=findViewById(R.id.gotoRegister);
        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    void initView(){
        prefManager=new PrefManager(this);
        emailEdit=findViewById(R.id.emailedit);
        passwordedit=findViewById(R.id.passwordedit);


    }


    public void login(){
        UserPojo userPojo=new UserPojo(emailEdit.getText().toString(), passwordedit.getText().toString());

        Call<UserResponse> call = Apiservice.getInstance().apiRequest.
                SignIn(userPojo);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body().status  && response.body().cc_id != 0) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    prefManager.setCenterId(response.body().cc_id);
                    Intent i = new Intent(LoginActivity.this, SignUpWithFacultyActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });

    }
}
