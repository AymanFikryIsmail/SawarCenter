package com.hesham.sawar.ui.order;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hesham.sawar.R;
import com.hesham.sawar.adapter.OrderUnReadyAdapter;
import com.hesham.sawar.data.model.OrderPojo;
import com.hesham.sawar.data.response.OrderResponse;
import com.hesham.sawar.databinding.FragmentUnReadyOrderBinding;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.networkmodule.NetworkUtilities;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnReadyOrderFragment extends Fragment implements OrderUnReadyAdapter.EventListener {


    private ArrayList<OrderPojo> facultyPojos;

    private OrderUnReadyAdapter facultySelectAdapter;

    public UnReadyOrderFragment() {
        // Required empty public constructor
    }

    PrefManager prefManager;
    private FragmentUnReadyOrderBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_un_ready_order, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);

        prefManager = new PrefManager(getContext());
        hideEmpty();

        facultyPojos = new ArrayList<>();
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        binding.termRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new OrderUnReadyAdapter(getContext(), facultyPojos, this);
        binding.termRecyclerView.setAdapter(facultySelectAdapter);

        binding.emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        callApi();
    }

    private void callApi() {
        hideEmpty();
        if (NetworkUtilities.isOnline(getContext())) {
            if (NetworkUtilities.isFast(getContext())) {
                getOrders();
            } else {
                Toast.makeText(getContext(), "Poor network connection , please try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Please , check your network connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
//            getAssistants();
            // If we are becoming invisible, then...
            if (!isVisibleToUser) {
            } else {
                getOrders();
            }
        }
    }

    public void getOrders() {//prefManager.getCenterId()
        Call<OrderResponse> call = Apiservice.getInstance().apiRequest.
                getUnreadyOrders(prefManager.getCenterId());
        binding.progressView.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.body() != null) {

                    if (response.body() != null && response.body() != null && response.body().status && response.body().data != null && response.body().data.size() != 0) {
                        Log.d("tag", "articles total result:: " + response.body().getMessage());
                        facultyPojos.clear();
                        facultyPojos.addAll(response.body().data);
                        if (facultyPojos.size() == 0) {
                            showEmpty();
                        } else {
                            hideEmpty();
                        }
                        facultySelectAdapter = new OrderUnReadyAdapter(getContext(), facultyPojos, UnReadyOrderFragment.this);
                        binding.termRecyclerView.setAdapter(facultySelectAdapter);
                        ((OrderFragment) getParentFragment()).setNumOfUnReadyoRders(facultyPojos.size());
                    } else {
                        showEmpty();
                        ((OrderFragment) getParentFragment()).setNumOfUnReadyoRders(0);
                    }
                }
                binding.progressView.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());
                showEmpty();
                binding.progressView.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Something went wrong , please try again", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void sum() {
        showDialog();
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_add_subject);
        TextView dialogButton = dialog.findViewById(R.id.addsubject);
        final EditText subname = dialog.findViewById(R.id.subjectname);
        subname.setHint("Enter number of orders");
        subname.setInputType(InputType.TYPE_CLASS_NUMBER);

        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!subname.getText().toString().isEmpty()) {
                    int num = Integer.parseInt(subname.getText().toString());
                    ArrayList<Integer> orderIdList = new ArrayList<>();
                    for (int i = 0; i < num; i++) {
                        orderIdList.add(facultyPojos.get(i).getId());
                    }
                    Intent intent = new Intent(getContext(), OrderSumActivity.class);
                    intent.putIntegerArrayListExtra("orderList", orderIdList);
                    startActivity(intent);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "you must enter values", Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }


    @Override
    public void onRemove(int data) {
        ((OrderFragment) getParentFragment()).setNumOfUnReadyoRders(data);
        if (facultyPojos.size() == 0) {
            showEmpty();
        } else {
            hideEmpty();
        }
    }

    void showEmpty() {
        binding.emptyLayout.setVisibility(View.VISIBLE);
    }

    void hideEmpty() {
        binding.emptyLayout.setVisibility(View.GONE);
    }

}
