package com.hesham.sawar.ui.center_data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
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
import com.hesham.sawar.data.response.CustomResponse;
import com.hesham.sawar.data.response.FacultyResponse;
import com.hesham.sawar.data.response.ImageResponse;
import com.hesham.sawar.data.response.UserResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.networkmodule.NetworkUtilities;
import com.hesham.sawar.ui.home.HomeActivity;
import com.hesham.sawar.ui.signup.SignUpActivity;
import com.hesham.sawar.ui.signup.SignUpWithFacultyActivity;
import com.hesham.sawar.utils.AddImageUtilities;
import com.hesham.sawar.utils.OnRequestImageIntentListener;
import com.hesham.sawar.utils.PrefManager;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hesham.sawar.networkmodule.NetworkManager.BASE_URL;

public class CenterDataActivity extends AppCompatActivity implements OnRequestImageIntentListener {

    PrefManager prefManager;
    private EditText nameEdit,passwordedit, addressEdit ;
    private int universityId;
    private TextView fromEdit, toedit;

    private ImageView addImage;
    private List<FacultyPojo> universityPojos;
    private Spinner universitySpinner, facultySpinner;
    CustomSpinnerAdapter customSpinnerAdapter;
    List<String> list;
    private File file;
    private File imageFile;
    String logo = "";
    private Button nextBtn;
    Calendar startTime, endTime;

    private FrameLayout progress_view;

    private boolean isPasswordVisible;
    ImageView eyeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_data);
        universityPojos = new ArrayList<>();
        list = new ArrayList<String>();
        list.add(0, "University served ");
        progress_view = findViewById(R.id.progress_view);
        eyeId = findViewById(R.id.eyeId);
        eyeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePassVisability();
            }
        });
        eyeId.setImageResource(R.drawable.icon_visibility_outlined);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCenter();

            }
        });
        initView();
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkGalleryOrCameraPermissions(10);

            }
        });
        addItemsOnSpinner2();
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
    void initView() {
        prefManager = new PrefManager(this);
        nameEdit = findViewById(R.id.nameedit);
        passwordedit = findViewById(R.id.passwordedit);
        addressEdit = findViewById(R.id.addressedit);
        fromEdit = findViewById(R.id.fromedit);
        toedit = findViewById(R.id.toedit);
        addImage = findViewById(R.id.addImage);

        nameEdit.setText(prefManager.getCenterData().getName());
        passwordedit.setText(prefManager.getCenterData().getPassword());
        addressEdit.setText(prefManager.getCenterData().getAddress());
        fromEdit.setText(prefManager.getCenterData().getStart());
        toedit.setText(prefManager.getCenterData().getEnd());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.placeholder(R.drawable.ellipse_9)
                .transforms(new CenterCrop()).dontAnimate();
        Glide.with(this).load( BASE_URL+prefManager.getCenterData().getLogo())
                .apply(requestOptions)
                .into(addImage);



        fromEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(CenterDataActivity.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
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
                mTimePicker = new TimePickerDialog(CenterDataActivity.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
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

    public void addItemsOnSpinner2() {
        universitySpinner = (Spinner) findViewById(R.id.universitySpinner);

        customSpinnerAdapter = new CustomSpinnerAdapter(CenterDataActivity.this, list, "University Served");
        universitySpinner.setAdapter(customSpinnerAdapter);
        universitySpinner.setSelection(0, false);
        universitySpinner.setAdapter(customSpinnerAdapter);


        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                universityId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void getUniversities() {
        Call<FacultyResponse> call = Apiservice.getInstance().apiRequest.
                getUniversities();
        call.enqueue(new Callback<FacultyResponse>() {
            @Override
            public void onResponse(Call<FacultyResponse> call, Response<FacultyResponse> response) {
                if (response.body().status && response.body().cc_id != null) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    universityPojos.addAll(response.body().cc_id);
                    int position=0;
                    int selectedUnivId=prefManager.getCenterData().getUniv();

                    for (int i = 1; i <= universityPojos.size(); i++) {
                        list.add(universityPojos.get(i - 1).getName());
                        if (universityPojos.get(i - 1).getId()==selectedUnivId){
                            position=i;
                        }
                    }
                    customSpinnerAdapter = new CustomSpinnerAdapter(CenterDataActivity.this, list, "University Served");
                    universitySpinner.setAdapter(customSpinnerAdapter);
                    universitySpinner.setSelection(0, false);

                    universitySpinner.setAdapter(customSpinnerAdapter);

                    universitySpinner.setSelection(position,true);
                }
            }

            @Override
            public void onFailure(Call<FacultyResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

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
            Toast.makeText(CenterDataActivity.this, "Please enter open time", Toast.LENGTH_LONG).show();
        } else if (toedit.getText().toString().trim().isEmpty()) {
            Toast.makeText(CenterDataActivity.this, "Please enter close time ", Toast.LENGTH_LONG).show();
        } else if (universityId == 0) {
            Toast.makeText(CenterDataActivity.this, "please choose university ", Toast.LENGTH_LONG).show();
        } else {
            String start = DateFormat.getTimeInstance().format(startTime.getTime()).split(" ")[0];
            String end = DateFormat.getTimeInstance().format(endTime.getTime()).split(" ")[0];

            progress_view.setVisibility(View.VISIBLE);
            final UserPojo userPojo = new UserPojo(prefManager.getCenterId()  ,nameEdit.getText().toString(), passwordedit.getText().toString(), addressEdit.getText().toString()
                    , start, end,
                    universityPojos.get(universityId - 1).getId(), logo);
            Call<CustomResponse> call = Apiservice.getInstance().apiRequest.
                    updateCC(userPojo);
            if (NetworkUtilities.isOnline(this)) {

                call.enqueue(new Callback<CustomResponse>() {
                    @Override
                    public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                        if (response.body().status ) {
                            Log.d("tag", "articles total result:: " + response.body().getMessage());
                            prefManager.setCenterData(userPojo);
//                            prefManager.setUniversityId( universityPojos.get(universityId - 1).getId());
                            progress_view.setVisibility(View.GONE);

                            Intent i = new Intent(CenterDataActivity.this, HomeActivity.class);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onFailure(Call<CustomResponse> call, Throwable t) {
                        Log.d("tag", "articles total result:: " + t.getMessage());
                        progress_view.setVisibility(View.GONE);
                        Toast.makeText(CenterDataActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(CenterDataActivity.this, "Please , check your network connection", Toast.LENGTH_LONG).show();
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
            call.enqueue(new Callback<ImageResponse>() {
                @Override
                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                    if (response.body().status) {
                        Log.d("tag", "articles total result:: " + response);
                        logo = response.body().data;
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions = requestOptions.placeholder(R.drawable.ellipse_9)
                                .transforms(new CenterCrop(), new CircleCrop()).dontAnimate();
                        Glide.with(CenterDataActivity.this).load(imageFile)
                                .apply(requestOptions)
                                .into(addImage);
                        prefManager.setImageProfile(logo);
                    } else {
                        Toast.makeText(CenterDataActivity.this, "please try again", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<ImageResponse> call, Throwable t) {
                    Log.d("tag", "articles total result:: " + t.getMessage());
                    progress_view.setVisibility(View.GONE);
                    Toast.makeText(CenterDataActivity.this, "Something went wrong , please try again", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(CenterDataActivity.this, "Please , check your network connection", Toast.LENGTH_LONG).show();
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
