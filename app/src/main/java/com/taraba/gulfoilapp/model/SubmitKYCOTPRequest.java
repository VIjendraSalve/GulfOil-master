package com.taraba.gulfoilapp.model;

public class SubmitKYCOTPRequest {
    private String login_id;
    private String otp;

    public String login_id() {
        return login_id;
    }

    public void setParticipant_login_id(String login_id) {
        this.login_id = login_id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
