package com.hesham.sawar.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.hesham.sawar.R;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.LoginResponse;
import com.hesham.sawar.data.response.UserResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.networkmodule.NetworkUtilities;
import com.hesham.sawar.ui.home.HomeActivity;
import com.hesham.sawar.ui.signup.ChooseTypeActivity;
import com.hesham.sawar.ui.signup.SignUpActivity;
import com.hesham.sawar.ui.signup.SignUpWithFacultyActivity;
import com.hesham.sawar.utils.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button signin, gotoRegister;
    PrefManager prefManager;
    private EditText emailEdit, passwordedit;
    private FrameLayout progress_view;

    private boolean isPasswordVisible;
    ImageView eyeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        gotoRegister = findViewById(R.id.gotoRegister);
        signin = findViewById(R.id.signin);
        eyeId = findViewById(R.id.eyeId);
        eyeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              togglePassVisability();
            }
        });
        eyeId.setImageResource(R.drawable.icon_visibility_outlined);

        progress_view = findViewById(R.id.progress_view);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtilities.isOnline(LoginActivity.this)) {
                    login();
                } else {
                    Toast.makeText(LoginActivity.this, "Please , check your network connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ChooseTypeActivity.class);
                startActivity(i);
//                finish();
            }
        });
    }

    void initView() {
        prefManager = new PrefManager(this);
        emailEdit = findViewById(R.id.emailedit);
        passwordedit = findViewById(R.id.passwordedit);



    }
    private void togglePassVisability() {
        if (isPasswordVisible) {
            String pass = passwordedit.getText().toString();
            passwordedit.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordedit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordedit.setText(pass);
            passwordedit.setSelection(pass.length());
            eyeId.setImageResource(R.drawable.icon_visibility_outlined);
        } else {
            String pass = passwordedit.getText().toString();
            passwordedit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordedit.setInputType(InputType.TYPE_CLASS_TEXT);
            passwordedit.setText(pass);
            passwordedit.setSelection(pass.length());
            eyeId.setImageResource(R.drawable.icon_visibility_outlined_blue);

        }
        isPasswordVisible= !isPasswordVisible;
    }

    public void login() {
        if (emailEdit.getText().toString().trim().isEmpty()) {
            emailEdit.setError("Please enter email");
        } else if (passwordedit.getText().toString().trim().isEmpty()) {
            passwordedit.setError("Please enter password");
        } else {

            UserPojo userPojo = new UserPojo(emailEdit.getText().toString(), passwordedit.getText().toString());
            progress_view.setVisibility(View.VISIBLE);
            Call<LoginResponse> call = Apiservice.getInstance().apiRequest.
                    SignIn(userPojo);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.body().status && response.body().data != null) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        prefManager.setCenterId(response.body().data.getEmployee().getCc_id());

                        prefManager.setCenterData(response.body().data.getCc());
                        prefManager.setUserPojo(response.body().data.getEmployee());
                        prefManager.setToken(response.body().getToken());

                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    progress_view.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d("tag", "articles total result:: " + t.getMessage());
                    progress_view.setVisibility(View.GONE);

                }
            });
        }

    }
}
