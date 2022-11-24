package com.taraba.gulfoilapp.model;

import java.util.ArrayList;

public class ParticipantProfileResponse {
    private String status;
    private String error;
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private String profile_status;
        private String uin;
        private String retailer_code;
        private String shop_name;
        private String full_name;
        private String mobile_no;
        private String email_id;
        private PanDetail pan_detail;
        private String view_profile;
        private BankDetails bank_details;

        public String getProfile_status() {
            return profile_status;
        }

        public void setProfile_status(String profile_status) {
            this.profile_status = profile_status;
        }

        public String getUin() {
            return uin;
        }

        public void setUin(String uin) {
            this.uin = uin;
        }

        public String getRetailer_code() {
            return retailer_code;
        }

        public void setRetailer_code(String retailer_code) {
            this.retailer_code = retailer_code;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
        }

        public String getEmail_id() {
            return email_id;
        }

        public void setEmail_id(String email_id) {
            this.email_id = email_id;
        }

        public PanDetail getPan_detail() {
            return pan_detail;
        }

        public void setPan_detail(PanDetail pan_detail) {
            this.pan_detail = pan_detail;
        }

        public String getView_profile() {
            return view_profile;
        }

        public void setView_profile(String view_profile) {
            this.view_profile = view_profile;
        }

        public BankDetails getBank_details() {
            return bank_details;
        }

        public void setBank_details(BankDetails bank_details) {
            this.bank_details = bank_details;
        }
    }

    public class PanDetail{
        private String pan;
        private String pan_name;
        private String pan_status;
        private String view_pan;
        private boolean is_readonly;
        private ArrayList<String> pan_reason = new ArrayList<>();

        public String getPan() {
            return pan;
        }

        public void setPan(String pan) {
            this.pan = pan;
        }

        public String getView_pan() {
            return view_pan;
        }

        public void setView_pan(String view_pan) {
            this.view_pan = view_pan;
        }

        public boolean isIs_readonly() {
            return is_readonly;
        }

        public void setIs_readonly(boolean is_readonly) {
            this.is_readonly = is_readonly;
        }

        public String getPan_name() {
            return pan_name;
        }

        public void setPan_name(String pan_name) {
            this.pan_name = pan_name;
        }

        public String getPan_status() {
            return pan_status;
        }

        public void setPan_status(String pan_status) {
            this.pan_status = pan_status;
        }

        public ArrayList<String> getPan_reason() {
            return pan_reason;
        }

        public void setPan_reason(ArrayList<String> pan_reason) {
            this.pan_reason = pan_reason;
        }
    }

    public class BankDetails{
        private String bank_name;
        private String account_holder_name;
        private String account_number;
        private String account_type;
        private String branch_name;
        private String ifsc_code;
        private String view_cheque_image;
        private String bank_status;
        private boolean is_readonly;
        private ArrayList<String> bank_reason = new ArrayList<>();

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getAccount_holder_name() {
            return account_holder_name;
        }

        public void setAccount_holder_name(String account_holder_name) {
            this.account_holder_name = account_holder_name;
        }

        public String getAccount_number() {
            return account_number;
        }

        public void setAccount_number(String account_number) {
            this.account_number = account_number;
        }

        public String getAccount_type() {
            return account_type;
        }

        public void setAccount_type(String account_type) {
            this.account_type = account_type;
        }

        public String getBranch_name() {
            return branch_name;
        }

        public void setBranch_name(String branch_name) {
            this.branch_name = branch_name;
        }

        public String getIfsc_code() {
            return ifsc_code;
        }

        public void setIfsc_code(String ifsc_code) {
            this.ifsc_code = ifsc_code;
        }

        public String getView_cheque_image() {
            return view_cheque_image;
        }

        public void setView_cheque_image(String view_cheque_image) {
            this.view_cheque_image = view_cheque_image;
        }

        public boolean isIs_readonly() {
            return is_readonly;
        }

        public void setIs_readonly(boolean is_readonly) {
            this.is_readonly = is_readonly;
        }

        public String getBank_status() {
            return bank_status;
        }

        public void setBank_status(String bank_status) {
            this.bank_status = bank_status;
        }

        public ArrayList<String> getBank_reason() {
            return bank_reason;
        }

        public void setBank_reason(ArrayList<String> bank_reason) {
            this.bank_reason = bank_reason;
        }
    }
}
