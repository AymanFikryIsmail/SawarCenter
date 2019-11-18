package com.hesham.sawar.data.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class UserPojo implements Serializable {

    private int id;
    private int cc_id;
    private String name;
    private String phone;
    private String email;
    private int type;

    private String password;
    private String passwd;

    private String address;
    private String start;
    private String end;
    private double rate;

    @SerializedName("univ_id")
    private int univ;
    private String university;
    private ArrayList<FacultyPojo> faculties;
    private ArrayList<Integer>  faculties_id;
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
    public UserPojo(int cc_id , String name, String password, String address, String start, String end, int univ,ArrayList<Integer>  faculties_id , String logo) {
        this.cc_id = cc_id;
        this.name = name;
        this.passwd = password;
        this.address = address;
        this.start = start;
        this.end = end;
        this.univ = univ;
        this.logo = logo;
        this.faculties_id=faculties_id;
    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswd() {
        return passwd;
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

    public String getUniversity() {
        return university;
    }

    public ArrayList<FacultyPojo> getFaculties() {
        return faculties;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCc_id(int cc_id) {
        this.cc_id = cc_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setUniv(int univ) {
        this.univ = univ;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setFaculties(ArrayList<FacultyPojo> faculties) {
        this.faculties = faculties;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public ArrayList<Integer> getFaculties_id() {
        return faculties_id;
    }

    public void setFaculties_id(ArrayList<Integer> faculties_id) {
        this.faculties_id = faculties_id;
    }
}
//
// "data": {
//         "employee": {
//         "id": 2,
//         "name": "center",
//         "phone": "0123",
//         "email": "a@a.c",
//         "type": 1,
//         "cc_id": 4,
//         "accept": 1
//         },
//         "cc": {
//         "id": 4,
//         "name": "ayman center",
//         "passwd": "123",
//         "address": "alex",
//         "logo": "1567823589072.4182files.jpg",
//         "start": "08:32:39",
//         "end": "03:32:50",
//         "univ_id": 1,
//         "delay_hours": 0,
//         "delay_date": 0,
//         "accepted": 1,
//         "university": "alexandria",
//         "faculties": [
//         "1science",
//         "2arts"
//         ]
//         }