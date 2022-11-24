package com.taraba.gulfoilapp.model;

public class VerifyUpdateProfileOTPRequest {
    private String participant_login_id;
    private String type;
    private String otp;

    public String getParticipant_login_id() {
        return participant_login_id;
    }

    public void setParticipant_login_id(String participant_login_id) {
        this.participant_login_id = participant_login_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
