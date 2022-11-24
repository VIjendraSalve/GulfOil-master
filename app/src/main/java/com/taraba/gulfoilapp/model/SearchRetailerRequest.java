package com.taraba.gulfoilapp.model;

public class SearchRetailerRequest {
    private String login_user_type;
    private String mobile_number;
    private String retailer_code;
    private String retailer_tally_id;
    private String search_by;
    private String user_login_id;

    public String getSearch_by() {
        return this.search_by;
    }

    public void setSearch_by(String str) {
        this.search_by = str;
    }

    public String getMobile_number() {
        return this.mobile_number;
    }

    public void setMobile_number(String str) {
        this.mobile_number = str;
    }

    public String getLogin_user_type() {
        return this.login_user_type;
    }

    public void setLogin_user_type(String str) {
        this.login_user_type = str;
    }

    public String getUser_login_id() {
        return this.user_login_id;
    }

    public void setUser_login_id(String str) {
        this.user_login_id = str;
    }

    public String getRetailer_code() {
        return this.retailer_code;
    }

    public void setRetailer_code(String str) {
        this.retailer_code = str;
    }

    public String getRetailer_tally_id() {
        return this.retailer_tally_id;
    }

    public void setRetailer_tally_id(String str) {
        this.retailer_tally_id = str;
    }
}
