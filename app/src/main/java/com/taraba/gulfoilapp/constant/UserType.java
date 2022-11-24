package com.taraba.gulfoilapp.constant;

public enum UserType {
    UNNATI("Unnati"), ROYAL("Unnati Royale"), ELITE("Unnati Elite"), CLUB("Unnati Club")
    , SIMPLE("Unnati Club");

    private String value;

    UserType(String value) {

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
