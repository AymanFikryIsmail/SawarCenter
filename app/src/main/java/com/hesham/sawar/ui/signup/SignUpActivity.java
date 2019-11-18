package com.hesham.sawar.ui.signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hesham.sawar.R;
import com.hesham.sawar.adapter.CustomSpinnerAdapter;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.FacultyResponse;
import com.hesham.sawar.data.response.ImageResponse;
import com.hesham.sawar.data.response.UserResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.networkmodule.NetworkUtilities;
import com.hesham.sawar.ui.login.LoginActivity;
import com.hesham.sawar.utils.AddImageUtilities;
import com.hesham.sawar.utils.OnRequestImageIntentListener;
import com.hesham.sawar.utils.PrefManager;

import java.io.File;
import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements OnRequestImageIntentListener {

    private Spinner universitySpinner;

    private Button nextBtn;
    private List<FacultyPojo> universityPojos;
    private FrameLayout progress_view;

    PrefManager prefManager;
    private TextView fromEdit, toedit;
    private EditText nameEdit, addressEdit, passwordedit;
    private int universityId;

    private ImageView addImage, backarrowId;
    Calendar startTime, endTime;

    private File file;
    private File imageFile;
    String logo = "";
    List<String> list;

    CustomSpinnerAdapter customSpinnerAdapter;

    private boolean isPasswordVisible;
    ImageView eyeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        universityPojos = new ArrayList<>();
        list = new ArrayList<String>();
        list.add(0, "University served ");

        initView();
        nextBtn = findViewById(R.id.nextBtn);
        progress_view = findViewById(R.id.progress_view);
        eyeId = findViewById(R.id.eyeId);
        eyeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePassVisability();
            }
        });
        eyeId.setImageResource(R.drawable.icon_visibility_outlined);

        addImage = findViewById(R.id.addImage);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkGalleryOrCameraPermissions(10);

            }
        });
        backarrowId = findViewById(R.id.backarrowId);
        backarrowId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCenter();

            }
        });
        initUniversitySpinner();
        getUniversities();
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
    public void getUniversities() {
        Call<FacultyResponse> call = Apiservice.getInstance().apiRequest.
                getUniversities();
        if (NetworkUtilities.isOnline(this)) {
            if (NetworkUtilities.isFast(this)) {

            call.enqueue(new Callback<FacultyResponse>() {
                @Override
                public void onResponse(Call<FacultyResponse> call, Response<FacultyResponse> response) {
                    if (response.body() != null) {
                        if (response.body().status && response.body().cc_id != null && response.body().cc_id.size() != 0) {
                            Log.d("tag", "articles total result:: " + response.body().getMessage());
                            universityPojos.addAll(response.body().cc_id);

                            for (int i = 1; i <= universityPojos.size(); i++) {
                                list.add(universityPojos.get(i - 1).getName());
                            }
//                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SignUpActivity.this,
//                                android.R.layout.simple_spinner_item, list);
//                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            customSpinnerAdapter = new CustomSpinnerAdapter(SignUpActivity.this, list, "University Served");
                            universitySpinner.setAdapter(customSpinnerAdapter);
                            universitySpinner.setSelection(0, false);
                            universitySpinner.setAdapter(customSpinnerAdapter);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<FacultyResponse> call, Throwable t) {
                    Log.d("tag", "articles total result:: " + t.getMessage());
                    Toast.makeText(SignUpActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                }
            });
            }else {
                Toast.makeText(this, "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(SignUpActivity.this, "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }

    // add items into spinner dynamically
    public void initUniversitySpinner() {
        universitySpinner = (Spinner) findViewById(R.id.universitySpinner);
        customSpinnerAdapter = new CustomSpinnerAdapter(this, list, "University Served");
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, list);
        // Initializing an ArrayAdapter

//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(customSpinnerAdapter);
        universitySpinner.setSelection(0, false);

        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(SignUpActivity.this,
//                        "OnItemSelectedListener : " + adapterView.getItemAtPosition(i).toString(),
//                        Toast.LENGTH_SHORT).show();
                universityId = i;

                         }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    void initView() {
        prefManager = new PrefManager(this);
        nameEdit = findViewById(R.id.nameedit);
        passwordedit = findViewById(R.id.passwordedit);
        addressEdit = findViewById(R.id.addressedit);
        fromEdit = findViewById(R.id.fromedit);
        toedit = findViewById(R.id.toedit);


        fromEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(SignUpActivity.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        fromEdit.setText(selectedHour + ":" + selectedMinute);
                        startTime = Calendar.getInstance();
                        startTime.set(Calendar.MINUTE, selectedMinute);
                        startTime.set(Calendar.HOUR_OF_DAY, selectedHour);


                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        toedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SignUpActivity.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        toedit.setText(selectedHour + ":" + selectedMinute);
                        endTime = Calendar.getInstance();
                        endTime.set(Calendar.MINUTE, selectedMinute);
                        endTime.set(Calendar.HOUR_OF_DAY, selectedHour);


                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

    }

    public void addCenter() {
//        int a = Integer.parseInt(fromEdit.getText().toString());

        if (nameEdit.getText().toString().trim().isEmpty()) {
            nameEdit.setError("Please enter name");
        } else if (passwordedit.getText().toString().trim().isEmpty()) {
            passwordedit.setError("Please enter password");
        } else if (!validatePassword(passwordedit.getText().toString())) {
            passwordedit.setError("Password must be more than 8 ");
        } else if (addressEdit.getText().toString().trim().isEmpty()) {
            addressEdit.setError("Please enter address");
        } else if (fromEdit.getText().toString().trim().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please enter open time", Toast.LENGTH_LONG).show();
        } else if (toedit.getText().toString().trim().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please enter close time ", Toast.LENGTH_LONG).show();
        } else if (universityId == 0) {
            Toast.makeText(SignUpActivity.this, "please choose university ", Toast.LENGTH_LONG).show();
        } else {
            String start = DateFormat.getTimeInstance().format(startTime.getTime()).split(" ")[0];
            String end = DateFormat.getTimeInstance().format(endTime.getTime()).split(" ")[0];

            progress_view.setVisibility(View.VISIBLE);
            final UserPojo userPojo = new UserPojo(nameEdit.getText().toString(), passwordedit.getText().toString(), addressEdit.getText().toString()
                    , start, end,
                    universityPojos.get(universityId - 1).getId(), logo);
            Call<UserResponse> call = Apiservice.getInstance().apiRequest.
                    addCenter(userPojo);
            if (NetworkUtilities.isOnline(this)) {

                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.body() != null) {

                            if (response.body().status && response.body().cc_id != 0) {
                                Log.d("tag", "articles total result:: " + response.body().getMessage());
                                prefManager.setCenterId(response.body().cc_id);
                                prefManager.setCenterData(userPojo);
                                prefManager.setUniversityId(universityPojos.get(universityId - 1).getId());

                                Intent i = new Intent(SignUpActivity.this, SignUpWithFacultyActivity.class);
                                startActivity(i);
                            }
                        }
                        progress_view.setVisibility(View.GONE);
                    }
                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Log.d("tag", "articles total result:: " + t.getMessage());
                        progress_view.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(SignUpActivity.this, "Please , check your network connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void uploadImage() {
        RequestBody imageFileRb = null;
        if (imageFile != null) {
            imageFileRb = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
        }
//        MultipartBody.Part pictureFileToUpload = MultipartBody.Part.createFormData("file", file.getName(), imageFileRb);
        Call<ImageResponse> call = Apiservice.getInstance().apiRequest.
                uploadProfileImages(imageFileRb);
        if (NetworkUtilities.isOnline(this)) {
            if (NetworkUtilities.isFast(this)) {

            call.enqueue(new Callback<ImageResponse>() {
                @Override
                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                    if (response.body().status) {
                        Log.d("tag", "articles total result:: " + response);
                        logo = response.body().data;
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions = requestOptions.placeholder(R.drawable.ellipse_9)
                                .transforms(new CenterCrop(), new CircleCrop()).dontAnimate();
                        Glide.with(SignUpActivity.this).load(imageFile)
                                .apply(requestOptions)
                                .into(addImage);
                        prefManager.setImageProfile(logo);
                    } else {
                        Toast.makeText(SignUpActivity.this, "please try again", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<ImageResponse> call, Throwable t) {
                    Log.d("tag", "articles total result:: " + t.getMessage());
                    progress_view.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                }
            });
            }else {
                Toast.makeText(this, "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }  } else {
            Toast.makeText(SignUpActivity.this, "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }

    private void checkGalleryOrCameraPermissions(int galleryOrCamera) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
        } else {
            AddImageUtilities.openGalleryOrCameraIntent(10, this, this);
        }
    }

    @Override
    public void onRequestGallery(Intent gallery) {
        startActivityForResult(gallery, 10);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("result", "onactivityresult");
        if (resultCode == RESULT_OK) {
            if (requestCode == 10) {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    String imagePath = AddImageUtilities.getImagePath(selectedImage, this);
                    File file = new File(imagePath);

                    this.imageFile = file;
                    Log.e("file", this.imageFile.toString());
                    uploadImage();
//                    }
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AddImageUtilities.openGalleryOrCameraIntent(10, this, this);
            }

        }
    }


    private boolean validateEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        boolean isValidEmail = email.matches(emailPattern);
        return isValidEmail;
    }

    private boolean validatePassword(String password1) {
        return password1.length() > 7;
    }

    private boolean validatePhoneNumber(String phoneNumber) {

        String phonePattern = "^01[0-2|5]{1}[0-9]{8}";
        boolean isValidPhone = phoneNumber.matches(phonePattern);

        return isValidPhone;

    }
}
