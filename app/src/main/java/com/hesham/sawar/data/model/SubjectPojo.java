package com.hesham.sawar.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubjectPojo {

    private int cc_id;
    @SerializedName("f_id")
    private int faculty_id;
    private int year;
    private int term;
    private String name;




    public SubjectPojo(int cc_id, int faculty_id, int year, int term) {
        this.cc_id = cc_id;
        this.faculty_id = faculty_id;
        this.year = year;
        this.term = term;
    }

    public String getName() {
        return name;
    }

    public int getCc_id() {
        return cc_id;
    }

    public int getFaculty_id() {
        return faculty_id;
    }

    public int getYear() {
        return year;
    }

    public int getTerm() {
        return term;
    }
}
