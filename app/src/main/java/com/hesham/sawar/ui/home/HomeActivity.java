package com.hesham.sawar.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.hesham.sawar.R;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.ui.assistant.AssistantsActivity;
import com.hesham.sawar.ui.center_data.CenterDataActivity;
import com.hesham.sawar.ui.center_data.PersonalDataActivity;
import com.hesham.sawar.ui.login.LoginActivity;
import com.hesham.sawar.ui.order.OrderFragment;
import com.hesham.sawar.ui.signup.SignUpActivity;
import com.hesham.sawar.utils.PrefManager;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hesham.sawar.networkmodule.NetworkManager.BASE_URL;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView navigation;

    PrefManager prefManager;
    NavigationView navigationView;

    TextView nametextView , centerName;
    ImageView centerImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefManager = new PrefManager(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigation = findViewById(R.id.navtabs);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        nametextView = navigationView.getHeaderView(0).findViewById(R.id.userNameId);
        centerName = navigationView.getHeaderView(0).findViewById(R.id.centernameId);
        centerImage = navigationView.getHeaderView(0).findViewById(R.id.imageView);

        nametextView.setText(prefManager.getUserPojo().getName());
        centerName.setText(prefManager.getCenterData().getName());


        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.placeholder(R.drawable.ellipse_9)
                .transforms(new CenterCrop(), new CircleCrop()).dontAnimate();
        Glide.with(this).load(BASE_URL+prefManager.getImageProfile())
                .apply(requestOptions)
                .into(centerImage);

        HomeFragment newsFragment = new HomeFragment();
        loadFragment(newsFragment);
    }

    private void hideItem() {
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.assistant).setVisible(false);
        nav_Menu.findItem(R.id.centerdata).setVisible(false);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_tab:
                    HomeFragment newsFragment = new HomeFragment();
                    loadFragment(newsFragment);
                    break;
                case R.id.orders_tab:
                        OrderFragment s = new OrderFragment();
                        loadFragment(s);

                    break;
            }
            return true;
        }
    };

    public void loadFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.centerdata) {
            if (prefManager.getUserPojo().getType() == 1) {
                Intent intent = new Intent(this, CenterDataActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "you are not allowed to view this", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.personaldata) {
            Intent intent = new Intent(this, PersonalDataActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.history) {
            if (prefManager.getUserPojo().getType() == 1) {
                HistoryFragment s = new HistoryFragment();
                loadFragment(s);
            } else {
                Toast.makeText(HomeActivity.this, "available for owner only ", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.assistant) {
            if (prefManager.getUserPojo().getType() == 1) {

                Intent intent = new Intent(this, AssistantsActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "you are not allowed to view this", Toast.LENGTH_LONG).show();
            }
        } else if (id ==R.id.sendproblem) {
            showDialog();
        } else if (id == R.id.sighnout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            prefManager.setToken("");
            prefManager.removeAll();

        }
        else if (id == R.id.help) {
            HelpFragment s = new HelpFragment();
            loadFragment(s);
        }

        else if (id == R.id.about) {
            AboutUsFragment s = new AboutUsFragment();
            loadFragment(s);
        }

        else if (id == R.id.privacy) {
            PrivacyFragment s = new PrivacyFragment();
            loadFragment(s);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void postProblem(int hours) {
        Call<Object> call = Apiservice.getInstance().apiRequest.
                postProblem(hours, prefManager.getCenterId());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(HomeActivity.this, "problem sent succesfully", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });

    }


    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_add_hours);
        Button dialogButton = dialog.findViewById(R.id.addHour);
        final EditText subname = dialog.findViewById(R.id.hours);


        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ho = Integer.parseInt(subname.getText().toString());
                postProblem(ho);
                dialog.dismiss();
//                Intent intent=new Intent(getContext(), MainActivity.class);
//                startActivity(intent);
            }
        });

        dialog.show();
    }


}
