package com.taraba.gulfoilapp.model;

/**
 * Created by AND707 on 01-Jul-18.
 */
public class BrandDesc {
    private String categroyId;
    private String segDescId;
    private String id;
    private String name;

    public BrandDesc(String categroyId, String segDescId, String id, String name) {
        this.categroyId = categroyId;
        this.segDescId = segDescId;
        this.id = id;
        this.name = name;
    }

    public String getCategroyId() {
        return categroyId;
    }

    public void setCategroyId(String categroyId) {
        this.categroyId = categroyId;
    }

    public String getSegDescId() {
        return segDescId;
    }

    public void setSegDescId(String segDescId) {
        this.segDescId = segDescId;
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

    @Override
    public String toString() {
        return name;
    }
}
