package com.taraba.gulfoilapp.model;

import com.google.gson.annotations.SerializedName;

public class QuestionAnswerModel {
    private String answer;
    private String question;
    @SerializedName("question_id")
    private String questionId;

    public String getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(String str) {
        this.questionId = str;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String str) {
        this.question = str;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String str) {
        this.answer = str;
    }
}
