package com.hesham.sawar.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.hesham.sawar.R;
import com.hesham.sawar.data.response.FacultyResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.ui.home.HomeActivity;
import com.hesham.sawar.ui.login.LoginActivity;
import com.hesham.sawar.ui.signup.SignUpActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openActivity();
            }
        }, 1800);


    }

    public void openActivity() {
        if (false ){
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        else {
            Intent i=new Intent(this, HomeActivity.class);
            startActivity(i);
            finish();
        }
    }
}