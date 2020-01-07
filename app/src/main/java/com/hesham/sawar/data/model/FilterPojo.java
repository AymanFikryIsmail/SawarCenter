package com.hesham.sawar.data.model;

import java.util.List;

public class FilterPojo {
    private int id;
    private String name;
    private int years;
    private List<DepartmentPojo> department;

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public int getYears() {
        return years;
    }

    public List<DepartmentPojo> getDepartment() {
        return department;
    }
}
