package com.taraba.gulfoilapp.model;

public class KYCOTPRequest {
    private String login_id;
    private String type;

    public String getParticipant_login_id() {
        return login_id;
    }

    public void setParticipant_login_id(String login_id) {
        this.login_id = login_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
