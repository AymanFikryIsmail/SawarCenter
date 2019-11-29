package com.hesham.sawar.data.model;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PaperPojo {

    private int id;
    private int paper_id;
    private String name;
    private int page;
    private long date;
    private int sub_id;
    private double price;
    private int  category_id;



    public PaperPojo(String name, int page, int sub_id, double price, int category_id) {
        this.name = name;
        this.page = page;
        this.sub_id = sub_id;
        this.price = price;
        this.category_id = category_id;
    }

    public int getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(int paper_id) {
        this.paper_id = paper_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPage() {
        return page;
    }

    public String getDate() {
        long val = this.date;
        Date date=new Date(val);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        String dateText = df2.format(date);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        Date newDate = null;
//        try {
//            newDate = format.parse(strCurrentDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        format = new SimpleDateFormat("yyyy-MM-dd");
//        Date currentDate = new Date(newDate.getTime());
//        String d = format.format(currentDate);
        return dateText;
    }

    public int getSub_id() {
        return sub_id;
    }

    public double getPrice() {
        return price;
    }

    public int getCategoryId() {
        return category_id;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
