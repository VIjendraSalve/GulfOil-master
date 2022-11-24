package com.taraba.gulfoilapp.view.splash_pop_up.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SplashPopUpResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("error")
    private String error;
    @SerializedName("display_popup")
    private String displayPopup;
    @SerializedName("data")
    private List<SplashPopUpDetails> splashPopUpDetailsList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDisplayPopup() {
        return displayPopup;
    }

    public void setDisplayPopup(String displayPopup) {
        this.displayPopup = displayPopup;
    }

    public List<SplashPopUpDetails> getSplashPopUpDetailsList() {
        return splashPopUpDetailsList;
    }

    public void setSplashPopUpDetailsList(List<SplashPopUpDetails> splashPopUpDetailsList) {
        this.splashPopUpDetailsList = splashPopUpDetailsList;
    }
}
