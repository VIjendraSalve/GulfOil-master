package com.taraba.gulfoilapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RewardOrderDetail implements Serializable {

    @SerializedName("ev_code")
    private String evCode;
    @SerializedName("ev_pin")
    private String evPin;
    @SerializedName("expiry_date")
    private String expiryDate;
    @SerializedName("gulf_voucher")
    private String gulfVoucher;
    @SerializedName("voucher_id")
    private String voucherId;

    public String getEvCode() {
        return evCode;
    }

    public void setEvCode(String evCode) {
        this.evCode = evCode;
    }

    public String getEvPin() {
        return evPin;
    }

    public void setEvPin(String evPin) {
        this.evPin = evPin;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getGulfVoucher() {
        return gulfVoucher;
    }

    public void setGulfVoucher(String gulfVoucher) {
        this.gulfVoucher = gulfVoucher;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}