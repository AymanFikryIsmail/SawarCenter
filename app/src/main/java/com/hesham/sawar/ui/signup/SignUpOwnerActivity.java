package com.hesham.sawar.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.hesham.sawar.R;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.CenterResponse;
import com.hesham.sawar.data.response.CustomResponse;
import com.hesham.sawar.data.response.UserResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.networkmodule.NetworkUtilities;
import com.hesham.sawar.ui.home.HomeActivity;
import com.hesham.sawar.ui.login.LoginActivity;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpOwnerActivity extends AppCompatActivity {

    private Button nextBtn;

    private PrefManager prefManager;
    private EditText nameEdit, phoneEdit, emailEdit;
    private FrameLayout progress_view;
    private Spinner centerSpinner;
    int centerId;
    private List<UserPojo> centerPojos;
    int centerDataId;
    List<String> list;
    private ImageView backarrowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_owner);
        centerPojos = new ArrayList<>();
        list = new ArrayList<String>();
        list.add(0, "Choose copy center ");
        progress_view = findViewById(R.id.progress_view);

        initView();
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtilities.isOnline(SignUpOwnerActivity.this)) {
                    signup();
                } else {
                    Toast.makeText(SignUpOwnerActivity.this, "Please , check your network connection", Toast.LENGTH_LONG).show();
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
    }

    public void getCenters() {
        Call<CenterResponse> call = Apiservice.getInstance().apiRequest.
                getAllCenter();
        call.enqueue(new Callback<CenterResponse>() {
            @Override
            public void onResponse(Call<CenterResponse> call, Response<CenterResponse> response) {
                if (response.body().status && response.body().data != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    centerPojos.addAll(response.body().data);

                    for (int i = 1; i <= centerPojos.size(); i++) {
                        list.add(centerPojos.get(i - 1).getName());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SignUpOwnerActivity.this,
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    centerSpinner.setAdapter(dataAdapter);
                }
            }

            @Override
            public void onFailure(Call<CenterResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }

    void initView() {
        prefManager = new PrefManager(this);
        nameEdit = findViewById(R.id.editname);
        phoneEdit = findViewById(R.id.editphone);
        emailEdit = findViewById(R.id.editemail);
        centerSpinner = (Spinner) findViewById(R.id.centersSpinner);

        if (prefManager.getType() == 0) {
            if (NetworkUtilities.isOnline(SignUpOwnerActivity.this)) {
                getCenters();
            } else {
                Toast.makeText(SignUpOwnerActivity.this, "Please , check your network connection", Toast.LENGTH_LONG).show();
            }
            setCenterSpinner();
            centerSpinner.setVisibility(View.VISIBLE);
        } else {
            centerSpinner.setVisibility(View.GONE);
            centerDataId = prefManager.getCenterId();
        }
    }

    public void setCenterSpinner() {
        List<String> list = new ArrayList<String>();
        // list.add("select university");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        centerSpinner.setAdapter(dataAdapter);
        centerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                centerId = i;
                if (centerId != 0)
                    centerDataId = centerPojos.get(centerId-1).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void signup() {
        if (nameEdit.getText().toString().trim().isEmpty()) {
            nameEdit.setError("Please enter name");
        } else if (emailEdit.getText().toString().trim().isEmpty() || !validateEmail(emailEdit.getText().toString())) {
            emailEdit.setError("Please enter vailid email");
        } else if (phoneEdit.getText().toString().trim().isEmpty()) {
            phoneEdit.setError("Please enter phone number");
        } else if (!validatePhoneNumber(phoneEdit.getText().toString().trim())) {
            phoneEdit.setError("Please enter valid phone number");
        }   else if (centerDataId == 0) {
            Toast.makeText(this, "please choose copy center ", Toast.LENGTH_LONG).show();
        }
        else {
            final UserPojo userPojo = new UserPojo(centerDataId, nameEdit.getText().toString(),
                    phoneEdit.getText().toString(), emailEdit.getText().toString(),
                    prefManager.getType());
            progress_view.setVisibility(View.VISIBLE);

            Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                    signup(userPojo);
            call.enqueue(new Callback<CustomResponse>() {
                @Override
                public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                    progress_view.setVisibility(View.GONE);
                    if (response.body().status) {
                        prefManager.setUserPojo(userPojo);
//                prefManager.setToken("registered");
                        Intent i = new Intent(SignUpOwnerActivity.this, LoginActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(SignUpOwnerActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<CustomResponse> call, Throwable t) {
                    Log.d("tag", "articles total result:: " + t.getMessage());
                    progress_view.setVisibility(View.GONE);
                    Toast.makeText(SignUpOwnerActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                }
            });
        }

    }


    private boolean validateEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        boolean isValidEmail = email.matches(emailPattern);
        return isValidEmail;
    }



    private boolean validatePhoneNumber(String phoneNumber) {

        String phonePattern = "^01[0-2|5]{1}[0-9]{8}";
        boolean isValidPhone = phoneNumber.matches(phonePattern);

        return isValidPhone;

    }

}
