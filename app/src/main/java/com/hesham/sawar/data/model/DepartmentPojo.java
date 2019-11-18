package com.hesham.sawar.data.model;

import com.google.gson.annotations.SerializedName;

public class DepartmentPojo {
    private int id;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
