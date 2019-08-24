package com.hesham.sawar.ui.subjects;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hesham.sawar.R;
import com.hesham.sawar.adapter.FacultySelectAdapter;
import com.hesham.sawar.adapter.PaperHomeAdapter;
import com.hesham.sawar.adapter.SubjectHomeAdapter;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.model.PaperPojo;
import com.hesham.sawar.data.model.SubjectPojo;
import com.hesham.sawar.data.response.PaperResponse;
import com.hesham.sawar.data.response.SubjectResponse;
import com.hesham.sawar.networkmodule.Apiservice;
import com.hesham.sawar.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaperFragment extends Fragment implements View.OnClickListener {
    public static PaperFragment newInstance() {
        PaperFragment fragment = new PaperFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private List<PaperPojo> facultyPojos;

    private RecyclerView facultyRecyclerView;
    private PaperHomeAdapter facultySelectAdapter;
    PrefManager prefManager;
    private String paperType;

    TextView lectureId, handoutId ,sectionId  ,courseId ,revisionId   ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_paper, container, false);
        prefManager = new PrefManager(getContext());

        facultyRecyclerView=view.findViewById(R.id.termRecyclerView);
        facultyPojos = new ArrayList<>();
        FloatingActionButton fab=view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        initView(view);
        RecyclerView.LayoutManager gridLayoutManager = new LinearLayoutManager(getContext() );
        facultyRecyclerView.setLayoutManager(gridLayoutManager);
        facultySelectAdapter = new PaperHomeAdapter(getContext(),facultyPojos,paperType);
        facultyRecyclerView.setAdapter(facultySelectAdapter);
        getPapers();
        return view;
    }

    void initView(View view){

        paperType="l";
        lectureId=view.findViewById(R.id.lectureId);
        handoutId=view.findViewById(R.id.handoutId);
        sectionId=view.findViewById(R.id.sectionId);
        courseId=view.findViewById(R.id.courseId);
        revisionId=view.findViewById(R.id.revisionId);

        lectureId.setOnClickListener(this);
        handoutId.setOnClickListener(this);
        sectionId.setOnClickListener(this);
        courseId.setOnClickListener(this);
        revisionId.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.lectureId:
                paperType="l";
                getPapers();
                break;
            case R.id.handoutId:
                paperType="h";
                getPapers();
                break;
            case R.id.sectionId:
                paperType="s";

                getPapers();
                break;
            case R.id.courseId:
                paperType="c";

                getPapers();
                break;
            case R.id.revisionId:
                paperType="r";

                getPapers();
                break;
        }

    }




    public void getPapers() {//prefManager.getCenterId()
        Call<PaperResponse> call = Apiservice.getInstance().apiRequest.
                getPapers(paperType ,prefManager.getSubjectId());
        call.enqueue(new Callback<PaperResponse>() {
            @Override
            public void onResponse(Call<PaperResponse> call, Response<PaperResponse> response) {
                if (response.body().status && response.body().data != null && response.body().data.size() != 0) {
                    Log.d("tag", "articles total result:: " + response.body().getMessage());
                    facultyPojos.clear();
                    facultyPojos.addAll(response.body().data);
                    facultySelectAdapter = new PaperHomeAdapter(getContext(), facultyPojos , paperType);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }else {
                    facultyPojos.clear();
                    facultySelectAdapter = new PaperHomeAdapter(getContext(), facultyPojos , paperType);
                    facultyRecyclerView.setAdapter(facultySelectAdapter);
                }
            }

            @Override
            public void onFailure(Call<PaperResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }




    private  void showDialog(){
        final Dialog dialog = new Dialog(getContext());
//        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_add_paper);
        Button dialogButton = dialog.findViewById(R.id.addsubject);
        final EditText subname = dialog.findViewById(R.id.papername);
        final EditText paperpages = dialog.findViewById(R.id.paperpages);
        final EditText paperprice = dialog.findViewById(R.id.paperprice);


        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // need validation here
                if (subname.getText().toString()==null ||paperpages.getText().toString()==null ||paperprice.getText().toString()==null){
                    Toast.makeText(getContext(),"you must enter values" , Toast.LENGTH_LONG).show();
                }else {
                    double price = Double.parseDouble(paperprice.getText().toString());
                    int pages = Integer.parseInt(paperpages.getText().toString());
                    addSubjects(subname.getText().toString() , pages ,price );
                    dialog.dismiss();
                }

//                Intent intent=new Intent(getContext(), MainActivity.class);
//                startActivity(intent);
            }
        });

        dialog.show();
    }

    public void addSubjects(String name , int pages , double price ) {//prefManager.getCenterId()
        PaperPojo subjectPojo = new PaperPojo(name ,pages ,prefManager.getSubjectId() ,price , paperType );
        Call<Object> call = Apiservice.getInstance().apiRequest.
                addPaper(subjectPojo);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                getPapers();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });
    }
}