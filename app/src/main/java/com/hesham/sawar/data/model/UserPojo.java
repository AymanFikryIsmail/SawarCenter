package com.hesham.sawar.data.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class UserPojo implements Serializable {

    private int cc_id;
    private String name;
    private String phone;
    private String email;
    private int type;

    private String password;
    private String address;
    private String start;
    private String end;

    @SerializedName("univ_id")
    private int univ;

    private String logo;
    public UserPojo() {
    }

    public UserPojo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserPojo(int cc_id, String name, String phone, String email, int type) {
        this.cc_id = cc_id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.type = type;
    }

    public UserPojo(String name, String password, String address, String start, String end, int univ, String logo) {
        this.name = name;
        this.password = password;
        this.address = address;
        this.start = start;
        this.end = end;
        this.univ = univ;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return cc_id;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public int getUniv() {
        return univ;
    }

    public String getLogo() {
        return logo;
    }

    public int getCc_id() {
        return cc_id;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public int getType() {
        return type;
    }
}
