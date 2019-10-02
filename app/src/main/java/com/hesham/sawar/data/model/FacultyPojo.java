package com.hesham.sawar.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FacultyPojo {

    @SerializedName("id")
    private int id;
    private String name;
    private int years;
    private boolean isSelected;
    @SerializedName("univ_id")
    private int univ_id;
    @SerializedName("university")
    private int university;

    private int cc_id;
    private ArrayList<Integer> faculties_id;

    public FacultyPojo() {
    }

    public FacultyPojo(int id, String name, int years, boolean isSelected, int univ_id, int cc_id, ArrayList<Integer> faculties_id) {
        this.id = id;
        this.name = name;
        this.years = years;
        this.isSelected = isSelected;
        this.univ_id = univ_id;
        this.cc_id = cc_id;
        this.faculties_id = faculties_id;
    }

    public FacultyPojo(int cc_id, ArrayList<Integer>  faculties_id) {
        this.cc_id = cc_id;
        this.faculties_id = faculties_id;
    }

    public FacultyPojo(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public int getYears() {
        return years;
    }

    public int getUniv_id() {
        return univ_id;
    }

    public int getUniversity() {
        return university;
    }
}
