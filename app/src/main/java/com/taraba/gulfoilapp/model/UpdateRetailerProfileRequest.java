package com.taraba.gulfoilapp.model;

public class UpdateRetailerProfileRequest {
    private String fls_login_id;
    private String mobile_no;
    private String participant_login_id;
    private String workshop_name;

    public String getParticipant_login_id() {
        return this.participant_login_id;
    }

    public void setParticipant_login_id(String str) {
        this.participant_login_id = str;
    }

    public String getFls_login_id() {
        return this.fls_login_id;
    }

    public void setFls_login_id(String str) {
        this.fls_login_id = str;
    }

    public String getWorkshop_name() {
        return this.workshop_name;
    }

    public void setWorkshop_name(String str) {
        this.workshop_name = str;
    }

    public String getMobile_no() {
        return this.mobile_no;
    }

    public void setMobile_no(String str) {
        this.mobile_no = str;
    }
}
