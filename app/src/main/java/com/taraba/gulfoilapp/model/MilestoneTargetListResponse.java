package com.taraba.gulfoilapp.model;

import java.util.List;

public class MilestoneTargetListResponse {
    private String status;
    private String error;
    private String target_title;
    private boolean popup_display;
    private List<String> data;

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

    public String getTarget_title() {
        return target_title;
    }

    public void setTarget_title(String target_title) {
        this.target_title = target_title;
    }

    public boolean isPopup_display() {
        return popup_display;
    }

    public void setPopup_display(boolean popup_display) {
        this.popup_display = popup_display;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
