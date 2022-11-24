package com.taraba.gulfoilapp.royalty_user_view.voucher_details.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VoucherDetails {
    @SerializedName("balance_amount")
    private String balance_amount;
    @SerializedName("expiry_of_the_code")
    private String expiry_of_the_code;
    @SerializedName("invoice_detail")
    private List<InvoiceDetails> invoiceDetail = null;
    @SerializedName("order_id")
    private String order_id;
    @SerializedName("redeemed_amount")
    private String redeemed_amount;
    @SerializedName("voucher_amount")
    private String voucher_amount;
    @SerializedName("voucher_no")
    private String voucher_no;

    public String getOrder_id() {
        return this.order_id;
    }

    public void setOrder_id(String str) {
        this.order_id = str;
    }

    public String getVoucher_no() {
        return this.voucher_no;
    }

    public void setVoucher_no(String str) {
        this.voucher_no = str;
    }

    public String getVoucher_amount() {
        return this.voucher_amount;
    }

    public void setVoucher_amount(String str) {
        this.voucher_amount = str;
    }

    public String getRedeemed_amount() {
        return this.redeemed_amount;
    }

    public void setRedeemed_amount(String str) {
        this.redeemed_amount = str;
    }

    public String getBalance_amount() {
        return this.balance_amount;
    }

    public void setBalance_amount(String str) {
        this.balance_amount = str;
    }

    public String getExpiry_of_the_code() {
        return this.expiry_of_the_code;
    }

    public void setExpiry_of_the_code(String str) {
        this.expiry_of_the_code = str;
    }

    public List<InvoiceDetails> getInvoiceDetail() {
        return this.invoiceDetail;
    }

    public void setInvoiceDetail(List<InvoiceDetails> list) {
        this.invoiceDetail = list;
    }
}
