package com.hesham.sawar.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hesham.sawar.R;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.LoginResponse;
import com.hesham.sawar.data.response.UserResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.ui.home.HomeActivity;
import com.hesham.sawar.ui.signup.ChooseTypeActivity;
import com.hesham.sawar.ui.signup.SignUpActivity;
import com.hesham.sawar.ui.signup.SignUpWithFacultyActivity;
import com.hesham.sawar.utils.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button  signin, gotoRegister;
    PrefManager prefManager;
    private EditText emailEdit ,passwordedit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        gotoRegister=findViewById(R.id.gotoRegister);
        signin=findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ChooseTypeActivity.class);
                startActivity(i);
                finish();
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

        Call<LoginResponse> call = Apiservice.getInstance().apiRequest.
                SignIn(userPojo);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body().status  && response.body().data != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    prefManager.setCenterId(response.body().data.getEmployee().getCc_id());

                    prefManager.setCenterData(response.body().data.getCc());
                    prefManager.setUserPojo(response.body().data.getCc());
                    prefManager.setToken(response.body().getToken());

                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse > call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });

    }
}
