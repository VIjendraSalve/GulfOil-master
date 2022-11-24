package com.taraba.gulfoilapp.model;

import android.text.TextUtils;

import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.io.Serializable;

/**
 * Created by android7 on 6/2/16.
 */
public class NewNotification implements Serializable {

    private int trno;
    private String type;
    private String user_id;
    private String title;
    private String description;
    private String image;
    private String channel_type;
    private String mdate;
    private String status;

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getChannel_type() {
        return channel_type;
    }

    public void setChannel_type(String channel_type) {
        this.channel_type = channel_type;
    }

    public String getMdate() {
        if (TextUtils.isEmpty(mdate))
            return mdate;
        else {
            return GulfOilUtils.formatdate(mdate, GulfOilUtils.DD_MM_YYYY_HH_MM_SS, GulfOilUtils.DD_MMMM_YYYYY_HH_MM_AMPM);
        }
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
