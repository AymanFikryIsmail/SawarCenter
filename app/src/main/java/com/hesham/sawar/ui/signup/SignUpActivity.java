package com.hesham.sawar.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hesham.sawar.R;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.FacultyResponse;
import com.hesham.sawar.data.response.UserResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.ui.login.LoginActivity;
import com.hesham.sawar.utils.PrefManager;

import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private Spinner universitySpinner;

    private Button nextBtn;
    private List<FacultyPojo> universityPojos;

    PrefManager prefManager;
    private EditText nameEdit ,addressEdit ,fromEdit ,toedit , repasswordedit,passwordedit ;
    private int universityId;

    Calendar startTime , endTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        universityPojos=new ArrayList<>();
        initView();
        nextBtn=findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCenter();

            }
        });
        addItemsOnSpinner2();
         getUniversities();
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
                    List<String> list = new ArrayList<String>();

                    for (int i=0;i<universityPojos.size();i++ ){
                        list.add(universityPojos.get(i).getName());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SignUpActivity.this,
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    universitySpinner.setAdapter(dataAdapter);
                }
            }

            @Override
            public void onFailure(Call<FacultyResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }
        // add items into spinner dynamically
    public void addItemsOnSpinner2() {
        universitySpinner = (Spinner) findViewById(R.id.universitySpinner);
        List<String> list = new ArrayList<String>();
       // list.add("select university");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(dataAdapter);
        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SignUpActivity.this,
                        "OnItemSelectedListener : " + adapterView.getItemAtPosition(i).toString(),
                        Toast.LENGTH_SHORT).show();
                universityId=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    void initView(){
        prefManager=new PrefManager(this);
        nameEdit=findViewById(R.id.nameedit);
        passwordedit=findViewById(R.id.passwordedit);
        addressEdit=findViewById(R.id.addressedit);
        fromEdit=findViewById(R.id.fromedit);
        toedit=findViewById(R.id.toedit);



        fromEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SignUpActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        fromEdit.setText( selectedHour + ":" + selectedMinute);
                        startTime=Calendar.getInstance();
                        startTime.set(Calendar.MINUTE,selectedMinute);
                        startTime.set(Calendar.HOUR_OF_DAY,selectedHour);


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
                mTimePicker = new TimePickerDialog(SignUpActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        toedit.setText( selectedHour + ":" + selectedMinute);
                        endTime=Calendar.getInstance();
                        endTime.set(Calendar.MINUTE,selectedMinute);
                        endTime.set(Calendar.HOUR_OF_DAY,selectedHour);


                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

    }

    public void addCenter(){
//        int a = Integer.parseInt(fromEdit.getText().toString());
       String start= DateFormat.getTimeInstance().format(startTime.getTime()).split(" ")[0];
        String end= DateFormat.getTimeInstance().format(endTime.getTime()).split(" ")[0];

        final UserPojo userPojo=new UserPojo(nameEdit.getText().toString(),passwordedit.getText().toString(),addressEdit.getText().toString()
               , start ,end,
                universityPojos.get(universityId).getId(),"");

        Call<UserResponse> call = Apiservice.getInstance().apiRequest.
                addCenter(userPojo);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body().status  && response.body().cc_id != 0) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    prefManager.setCenterId(response.body().cc_id);
                    prefManager.setCenterData(userPojo);
                    Intent i = new Intent(SignUpActivity.this, SignUpWithFacultyActivity.class);
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
