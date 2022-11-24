package com.taraba.gulfoilapp.model;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by android3 on 3/16/16.
 */
public class OrderHistory implements Serializable {

    String order_id;
    String name;
    String gp;
    String status;
    String date;
    String order_request_no;
    String order_details;
    String product_image;
    String qty;


    public OrderHistory(String s, String abc, String s1, String active, String s2, String order_request_no, String orders) {


        Log.e("", "------------------------------------" + s + ", " + abc + ", " + s1 + ", " + active + ", " + s2);
        this.order_id = s;
        this.name = abc;
        this.gp = s1;
        this.status = active;
        this.date = s2;
        this.order_request_no = order_request_no;
        this.order_details = orders;
    }


    public OrderHistory(String s, String abc, String s1, String active, String s2, String order_request_no, String orders, String product_image, String qty) {


        Log.e("", "------------------------------------" + s + ", " + abc + ", " + s1 + ", " + active + ", " + s2);
        this.order_id = s;
        this.name = abc;
        this.gp = s1;
        this.status = active;
        this.date = s2;
        this.order_request_no = order_request_no;
        this.order_details = orders;
        this.product_image = product_image;
        this.qty = qty;
    }


    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGp() {
        return gp;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrder_request_no() {
        return order_request_no;
    }

    public String getOrder_details() {
        return order_details;
    }

}
