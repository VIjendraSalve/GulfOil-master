package com.taraba.gulfoilapp.model;

/**
 * Created by android on 3/14/16.
 */
public class Product {

    int trno;
    int product_id_pk;
    String name;
    String product_code;
    String small_description;
    String small_image_link;
    int sort_order;
    long points;
    int product_visible;
    int active_status;
    String product_type;
    String record_date;
    String update_date;
    String category_id;
    String bonus_details;
    String product_details;
    String date;
    int color;

    public int getTrno() {
        return trno;
    }

    public void setTrno(int trno) {
        this.trno = trno;
    }

    public String getBonus_details() {
        return bonus_details;
    }

    public void setBonus_details(String bonus_details) {
        this.bonus_details = bonus_details;
    }

    public String getProduct_details() {
        return product_details;
    }

    public void setProduct_details(String product_details) {
        this.product_details = product_details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getProduct_id_pk() {
        return product_id_pk;
    }

    public void setProduct_id_pk(int product_id_pk) {
        this.product_id_pk = product_id_pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getSmall_description() {
        return small_description;
    }

    public void setSmall_description(String small_description) {
        this.small_description = small_description;
    }

    public String getSmall_image_link() {
        return small_image_link;
    }

    public void setSmall_image_link(String small_image_link) {
        this.small_image_link = small_image_link;
    }

    public int getSort_order() {
        return sort_order;
    }

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public int getProduct_visible() {
        return product_visible;
    }

    public void setProduct_visible(int product_visible) {
        this.product_visible = product_visible;
    }

    public int getActive_status() {
        return active_status;
    }

    public void setActive_status(int active_status) {
        this.active_status = active_status;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}