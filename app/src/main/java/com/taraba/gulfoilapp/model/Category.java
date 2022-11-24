package com.taraba.gulfoilapp.model;

/**
 * Created by android on 3/14/16.
 */
public class Category {

    int category_id_pk;
    String category_name;
    int category_sort_order;
    int category_active;
    String category_record_date;
    String category_last_updated;

    public int getCategory_id_pk() {
        return category_id_pk;
    }

    public void setCategory_id_pk(int category_id_pk) {
        this.category_id_pk = category_id_pk;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getCategory_sort_order() {
        return category_sort_order;
    }

    public void setCategory_sort_order(int category_sort_order) {
        this.category_sort_order = category_sort_order;
    }

    public int getCategory_active() {
        return category_active;
    }

    public void setCategory_active(int category_active) {
        this.category_active = category_active;
    }

    public String getCategory_record_date() {
        return category_record_date;
    }

    public void setCategory_record_date(String category_record_date) {
        this.category_record_date = category_record_date;
    }

    public String getCategory_last_updated() {
        return category_last_updated;
    }

    public void setCategory_last_updated(String category_last_updated) {
        this.category_last_updated = category_last_updated;
    }
}
