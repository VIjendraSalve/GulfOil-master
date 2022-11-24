package com.taraba.gulfoilapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dell on 02-Apr-19.
 */
public class DigitalOrderHistory implements Serializable {
    @SerializedName("orders_id_pk")
    private String ordersIdPk;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("orders_record_date")
    private String ordersRecordDate;
    @SerializedName("reward_name")
    private String rewardName;
    @SerializedName("quantity")
    private String quantity;
    @SerializedName("reward_value")
    private String rewardValue;
    @SerializedName("reward_code")
    private String rewardCode;
    @SerializedName("status")
    private String status;
    @SerializedName("reward_order_detail")
    private List<RewardOrderDetail> rewardOrderDetail = null;
    @SerializedName("order_detail_id")
    private String orderDetailId;

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrdersIdPk() {
        return ordersIdPk;
    }

    public void setOrdersIdPk(String ordersIdPk) {
        this.ordersIdPk = ordersIdPk;
    }

    public String getOrdersRecordDate() {
        return ordersRecordDate;
    }

    public void setOrdersRecordDate(String ordersRecordDate) {
        this.ordersRecordDate = ordersRecordDate;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(String rewardValue) {
        this.rewardValue = rewardValue;
    }

    public String getRewardCode() {
        return rewardCode;
    }

    public void setRewardCode(String rewardCode) {
        this.rewardCode = rewardCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RewardOrderDetail> getRewardOrderDetail() {
        return rewardOrderDetail;
    }

    public void setRewardOrderDetail(List<RewardOrderDetail> rewardOrderDetail) {
        this.rewardOrderDetail = rewardOrderDetail;
    }
}
