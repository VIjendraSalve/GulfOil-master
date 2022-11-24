package com.taraba.gulfoilapp.model;

public class SubmitYDROTPRequest {
    private String participant_login_id;
    private String order_detail_id;
    private String otp;

    public String getParticipant_login_id() {
        return participant_login_id;
    }

    public void setParticipant_login_id(String participant_login_id) {
        this.participant_login_id = participant_login_id;
    }

    public String getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(String order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
