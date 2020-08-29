package com.hesham.sawar.ui.splash;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.hesham.sawar.R;
import com.hesham.sawar.data.response.CheckEmployeeResponse;
import com.hesham.sawar.data.response.CustomResponse;
import com.hesham.sawar.data.response.FacultyResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.networkmodule.NetworkUtilities;
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
                if (prefManager.getToken().equals("")) {
                    openActivity();
                } else {
                    if (NetworkUtilities.isOnline(SplashActivity.this)) {
                        if (NetworkUtilities.isFast(SplashActivity.this)) {
//                            checkForEmployee();
                            checkAppUpdates();
                        } else {
                            Toast.makeText(SplashActivity.this, "Poor network connection , please try again, then reopen the app", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SplashActivity.this, "Please , check your network connection , then reopen the app", Toast.LENGTH_LONG).show();
                    }
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

    public void checkAppUpdates() {//prefManager.getCenterId()
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                checkAppUpdates();
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() !=null &&response.body().status ) {//response.body().status  &&
                  double val= (double) response.body().data;
                    if (val==0){
                        checkForEmployee();

                    }else {
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse("https://play.google.com/store/apps/details?id=com.hesham.sawar"));
                        startActivity(viewIntent);
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                Toast.makeText(SplashActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
            }
        });
    }
}