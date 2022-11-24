package com.taraba.gulfoilapp.model;

import java.io.Serializable;

/**
 * Created by android7 on 6/2/16.
 */
public class Notification implements Serializable {

    String type;
    int trno;
    String data, mdate;
    String title, body, img_url;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    public Notification() {
    }

    public Notification(String title, String body, String img_url, String mdate, int trno, String status) {
        this.title = title;
        this.body = body;
        this.img_url = img_url;
        this.mdate = mdate;
        this.trno = trno;
        this.status = status;
    }

    public int getTrno() {
        return trno;
    }

    public void setTrno(int trno) {
        this.trno = trno;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }
}
