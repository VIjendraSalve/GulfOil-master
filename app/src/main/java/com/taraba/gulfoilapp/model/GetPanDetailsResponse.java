package com.taraba.gulfoilapp.model;

public class GetPanDetailsResponse {

    private String error;
    private String status;
    private String message;
    private String pan_number;
    private String pan_name;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPan_number() {
        return pan_number;
    }

    public void setPan_number(String pan_number) {
        this.pan_number = pan_number;
    }

    public String getPan_name() {
        return pan_name;
    }

    public void setPan_name(String pan_name) {
        this.pan_name = pan_name;
    }
}
