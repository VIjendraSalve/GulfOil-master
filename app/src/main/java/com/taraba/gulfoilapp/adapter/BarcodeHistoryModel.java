package com.taraba.gulfoilapp.adapter;

/**
 * Created by android3 on 1/1/16.
 */
public class BarcodeHistoryModel {

    private int id;
    private String text;
    private String format;
    private String display;
    private String timestamp;
    private String details;


    private String histryPartcipanID;
    private String histryPublishType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getHistryPartcipanID() {
        return histryPartcipanID;
    }

    public void setHistryPartcipanID(String histryPartcipanID) {
        this.histryPartcipanID = histryPartcipanID;
    }

    public String getHistryPublishType() {
        return histryPublishType;
    }

    public void setHistryPublishType(String histryPublishType) {
        this.histryPublishType = histryPublishType;
    }
}