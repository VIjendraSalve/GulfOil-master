package com.taraba.gulfoilapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CampaignRewardsDetails {
    @SerializedName("order_id")
    private String order_id;
    @SerializedName("orders_id_pk")
    private String orders_id_pk;
    @SerializedName("order_detail_id")
    private String order_detail_id;
    @SerializedName("orders_record_date")
    private String orders_record_date;
    @SerializedName("reward_name")
    private String reward_name;
    @SerializedName("status")
    private String status;
    @SerializedName("reward_code")
    private String reward_code;
    @SerializedName("reward_value")
    private String reward_value;
    @SerializedName("quantity")
    private String quantity;
    @SerializedName("product_name")
    private String product_name;
    @SerializedName("product_description")
    private String product_description;
    @SerializedName("product_image")
    private String product_image;
    @SerializedName("reward_order_detail")
    private List<RewardOrderDetail> rewardOrderDetail = null;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrders_id_pk() {
        return orders_id_pk;
    }

    public void setOrders_id_pk(String orders_id_pk) {
        this.orders_id_pk = orders_id_pk;
    }

    public String getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(String order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public String getOrders_record_date() {
        return orders_record_date;
    }

    public void setOrders_record_date(String orders_record_date) {
        this.orders_record_date = orders_record_date;
    }

    public String getReward_name() {
        return reward_name;
    }

    public void setReward_name(String reward_name) {
        this.reward_name = reward_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReward_code() {
        return reward_code;
    }

    public void setReward_code(String reward_code) {
        this.reward_code = reward_code;
    }

    public String getReward_value() {
        return reward_value;
    }

    public void setReward_value(String reward_value) {
        this.reward_value = reward_value;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public List<RewardOrderDetail> getRewardOrderDetail() {
        return rewardOrderDetail;
    }

    public void setRewardOrderDetail(List<RewardOrderDetail> rewardOrderDetail) {
        this.rewardOrderDetail = rewardOrderDetail;
    }
}
