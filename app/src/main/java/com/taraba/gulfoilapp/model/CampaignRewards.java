package com.taraba.gulfoilapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CampaignRewards implements Serializable {

    @SerializedName("campaign_name")
    private String campaign_name;
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


    public String getCampaign_name() {
        return campaign_name;
    }

    public void setCampaign_name(String campaign_name) {
        this.campaign_name = campaign_name;
    }

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
}
