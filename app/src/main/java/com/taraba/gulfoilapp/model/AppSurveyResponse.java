package com.taraba.gulfoilapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppSurveyResponse {
    private List<QuestionAnswerModel> data = null;
    @SerializedName("start_page")
    private String startPage;
    private String status;
    @SerializedName("thank_you_page")
    private String thankYouPage;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public List<QuestionAnswerModel> getData() {
        return this.data;
    }

    public void setData(List<QuestionAnswerModel> list) {
        this.data = list;
    }

    public String getThankYouPage() {
        return this.thankYouPage;
    }

    public void setThankYouPage(String str) {
        this.thankYouPage = str;
    }

    public String getStartPage() {
        return this.startPage;
    }

    public void setStartPage(String str) {
        this.startPage = str;
    }
}
