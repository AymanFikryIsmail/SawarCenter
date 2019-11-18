package com.hesham.sawar.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.hesham.sawar.R;
import com.hesham.sawar.data.response.CheckEmployeeResponse;
import com.hesham.sawar.data.response.FacultyResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.ui.assistant.AssistantsActivity;
import com.hesham.sawar.ui.home.HomeActivity;
import com.hesham.sawar.ui.login.LoginActivity;
import com.hesham.sawar.ui.signup.SignUpActivity;
import com.hesham.sawar.ui.signup.SignUpWithFacultyActivity;
import com.hesham.sawar.utils.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ( prefManager.getToken().equals("") ) {
                    openActivity();
                }else {
                    checkForEmployee();
                }
            }
        }, 1800);
 prefManager=new PrefManager(this);

    }

    public void openActivity() {
//        prefManager.getToken();
        if ( prefManager.getToken().equals("") ){
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




    public void checkForEmployee() {//prefManager.getCenterId()
        Call<CheckEmployeeResponse> call = Apiservice.getInstance().apiRequest.
                checkForEmployee(prefManager.getUserPojo().getId());
        call.enqueue(new Callback<CheckEmployeeResponse>() {
            @Override
            public void onResponse(Call<CheckEmployeeResponse> call, Response<CheckEmployeeResponse> response) {
                if (response.body() !=null &&response.body().status ) {//response.body().status  &&

                    if (response.body().data){
                        openActivity();
                    }else {
                        Toast.makeText(SplashActivity.this, "You are not allowed to continue", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckEmployeeResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(SplashActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
            }
        });
    }
}