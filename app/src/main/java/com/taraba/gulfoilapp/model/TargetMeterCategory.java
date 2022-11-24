package com.taraba.gulfoilapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by AND707 on 06-Jul-18.
 */
public class TargetMeterCategory implements Serializable {
    private String header;
    private String name;
    private String campaign_name;

    public TargetMeterCategory() {
    }

    public TargetMeterCategory(JSONObject jsonObject) {
        try {
            this.header = jsonObject.getString("header");
            this.name = jsonObject.getString("name");
            this.campaign_name = jsonObject.getString("campaign_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCampaign_name() {
        return campaign_name;
    }

    public void setCampaign_name(String campaign_name) {
        this.campaign_name = campaign_name;
    }
}
