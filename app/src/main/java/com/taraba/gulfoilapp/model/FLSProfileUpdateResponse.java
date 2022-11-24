package com.taraba.gulfoilapp.model;

public class FLSProfileUpdateResponse {
    private String error;
    private String message;
    private String profile_image;
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String str) {
        this.error = str;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public String getProfile_image() {
        return this.profile_image;
    }

    public void setProfile_image(String str) {
        this.profile_image = str;
    }
}
