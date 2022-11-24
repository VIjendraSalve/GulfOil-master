package com.taraba.gulfoilapp.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tarabasoftwareiinc on 06/05/17.
 */
public class WhatsNew {
    private String id;
    private String name;
    private String imageLink;

    public WhatsNew(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getString("whats_new_id_pk");
            this.name = jsonObject.getString("name");
            this.imageLink = jsonObject.getString("image_link");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return "WhatsNew{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageLink='" + imageLink + '\'' +
                '}';
    }
}
