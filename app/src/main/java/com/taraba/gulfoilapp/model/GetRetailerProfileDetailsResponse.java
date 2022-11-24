package com.taraba.gulfoilapp.model;

import java.util.List;

public class GetRetailerProfileDetailsResponse {
    private List<Data> data;
    private String error;
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String str) {
        this.error = str;
    }

    public List<Data> getData() {
        return this.data;
    }

    public void setData(List<Data> list) {
        this.data = list;
    }

    public class Data {
        private String edit;
        private String mobile_no;
        private String msg;
        private String retailer_id;
        private String retailer_name;
        private String workshop_name;

        public Data() {
        }

        public String getRetailer_id() {
            return this.retailer_id;
        }

        public void setRetailer_id(String str) {
            this.retailer_id = str;
        }

        public String getRetailer_name() {
            return this.retailer_name;
        }

        public void setRetailer_name(String str) {
            this.retailer_name = str;
        }

        public String getWorkshop_name() {
            return this.workshop_name;
        }

        public void setWorkshop_name(String str) {
            this.workshop_name = str;
        }

        public String getMobile_no() {
            return this.mobile_no;
        }

        public void setMobile_no(String str) {
            this.mobile_no = str;
        }

        public String getEdit() {
            return this.edit;
        }

        public void setEdit(String str) {
            this.edit = str;
        }

        public String getMsg() {
            return this.msg;
        }

        public void setMsg(String str) {
            this.msg = str;
        }
    }
}
