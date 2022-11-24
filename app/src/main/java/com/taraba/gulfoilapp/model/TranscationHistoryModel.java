package com.taraba.gulfoilapp.model;

import java.util.List;

public class TranscationHistoryModel {
    private String month;
    private List<KeyValue> keyValueList;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<KeyValue> getKeyValueList() {
        return keyValueList;
    }

    public void setKeyValueList(List<KeyValue> keyValueList) {
        this.keyValueList = keyValueList;
    }
}
