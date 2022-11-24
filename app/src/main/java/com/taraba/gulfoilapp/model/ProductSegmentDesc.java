package com.taraba.gulfoilapp.model;

/**
 * Created by AND707 on 01-Jul-18.
 */
public class ProductSegmentDesc {
    private String categoryId;
    private String id;
    private String name;

    public ProductSegmentDesc(String categoryId, String id, String name) {
        this.categoryId = categoryId;
        this.id = id;
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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
