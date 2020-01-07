package com.hesham.sawar.data.model;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDetailsPojo {

    private int id;
    @SerializedName("paper_name")
    private String name;
    @SerializedName("subject")
    private String subject;
    @SerializedName("no")
    private int no;
    @SerializedName("paper_num")
    private int page;
    private int sub_id;
    private int p_id;
    private int o_id;
    private double price;
    private String category;
    @SerializedName("paper_category")
    private String paper_category;

    private String year;
    private String faculty;
    private String department;

    private int orderSum;


    public OrderDetailsPojo(String name, String subject, int page, int sub_id, int p_id, int o_id, double price, String type) {
        this.name = name;
        this.subject = subject;
        this.page = page;
        this.sub_id = sub_id;
        this.p_id = p_id;
        this.o_id = o_id;
        this.price = price;
        this.paper_category = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public int getPage() {
        return page;
    }

    public int getSub_id() {
        return sub_id;
    }

    public int getP_id() {
        return p_id;
    }

    public int getO_id() {
        return o_id;
    }

    public double getPrice() {
        return price;
    }

    public String getPaperCategory() {
        return paper_category;
    }

    public int getNo() {
        return no;
    }


    public String getCategory() {
        return category;
    }

    public String getYear() {
        return year;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getDepartment() {
        return department;
    }

    public int getOrderSum() {
        return orderSum;
    }
}