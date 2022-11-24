package com.taraba.gulfoilapp.model;

public class VerifyUpdateRetailerProfileOTPRequest {
    private String fls_login_id;
    private String otp;
    private String participant_login_id;

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

    public String getOtp() {
        return this.otp;
    }

    public void setOtp(String str) {
        this.otp = str;
    }
}
