package com.taraba.gulfoilapp.model;

/**
 * Created by AND707 on 02-Jul-18.
 */
public class PointsCalculatorMaster {
    private String category;
    private String product_segment_description;
    private String brand_description;
    private String pack_size_description;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct_segment_description() {
        return product_segment_description;
    }

    public void setProduct_segment_description(String product_segment_description) {
        this.product_segment_description = product_segment_description;
    }

    public String getBrand_description() {
        return brand_description;
    }

    public void setBrand_description(String brand_description) {
        this.brand_description = brand_description;
    }

    public String getPack_size_description() {
        return pack_size_description;
    }

    public void setPack_size_description(String pack_size_description) {
        this.pack_size_description = pack_size_description;
    }
}
