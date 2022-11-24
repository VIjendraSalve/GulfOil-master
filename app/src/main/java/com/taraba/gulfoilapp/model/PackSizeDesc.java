package com.taraba.gulfoilapp.model;

/**
 * Created by AND707 on 01-Jul-18.
 */
public class PackSizeDesc {
    private String brandDesId;
    private String id;
    private String name;

    public PackSizeDesc(String brandDesId, String id, String name) {
        this.brandDesId = brandDesId;
        this.id = id;
        this.name = name;
    }

    public String getBrandDesId() {
        return brandDesId;
    }

    public void setBrandDesId(String brandDesId) {
        this.brandDesId = brandDesId;
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
