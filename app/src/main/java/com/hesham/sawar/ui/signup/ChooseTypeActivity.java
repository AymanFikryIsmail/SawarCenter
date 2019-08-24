package com.hesham.sawar.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hesham.sawar.R;
import com.hesham.sawar.ui.login.LoginActivity;
import com.hesham.sawar.utils.PrefManager;

public class ChooseTypeActivity extends AppCompatActivity implements View.OnClickListener {


    Button gotoowner, gotoassistant;

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type);
        prefManager=new PrefManager(this);
        gotoowner=findViewById(R.id.gotoowner);
        gotoowner.setOnClickListener(this);

        gotoassistant=findViewById(R.id.gotoassistant);
        gotoassistant.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.gotoowner:

                prefManager.setType(1);
                Intent i = new Intent(this, SignUpActivity.class);
                startActivity(i);
                break;
            case R.id.gotoassistant:

                prefManager.setType(0);
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }
}
