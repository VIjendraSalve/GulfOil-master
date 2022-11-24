package com.taraba.gulfoilapp.royalty_user_view.voucher_details.model;

import com.google.gson.annotations.SerializedName;

public class InvoiceDetails {
    @SerializedName("invoice_date")
    private String invoice_date;
    @SerializedName("invoice_no")
    private String invoice_no;
    @SerializedName("voucher_redeemed_amount")
    private String voucher_redeemed_amount;

    public String getInvoice_no() {
        return this.invoice_no;
    }

    public void setInvoice_no(String str) {
        this.invoice_no = str;
    }

    public String getInvoice_date() {
        return this.invoice_date;
    }

    public void setInvoice_date(String str) {
        this.invoice_date = str;
    }

    public String getVoucher_redeemed_amount() {
        return this.voucher_redeemed_amount;
    }

    public void setVoucher_redeemed_amount(String str) {
        this.voucher_redeemed_amount = str;
    }
}
