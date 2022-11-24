package com.taraba.gulfoilapp.model;

import com.google.gson.annotations.SerializedName;

public class AppSurveyRequest {
    @SerializedName("login_id")
    private String loginId;

    public String getLoginId() {
        return this.loginId;
    }

    public void setLoginId(String str) {
        this.loginId = str;
    }
}
