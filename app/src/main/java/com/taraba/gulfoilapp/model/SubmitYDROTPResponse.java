package com.taraba.gulfoilapp.model;

import java.io.Serializable;
import java.util.List;

public class SubmitYDROTPResponse implements Serializable {
    public String status;
    public String error;
    public List<Data> data;

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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable{
        public String order_id;
        public String orders_id_pk;
        public String order_detail_id;
        public String orders_record_date;
        public String reward_name;
        public String status;
        public String reward_code;
        public String reward_value;
        public String quantity;
        public String product_name;
        public String product_description;
        public String product_image;
        public List<RewardOrderDetail> reward_order_detail;

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

        public List<RewardOrderDetail> getReward_order_detail() {
            return reward_order_detail;
        }

        public void setReward_order_detail(List<RewardOrderDetail> reward_order_detail) {
            this.reward_order_detail = reward_order_detail;
        }

        public class RewardOrderDetail implements Serializable {
            public String ev_code;
            public String ev_pin;
            public String expiry_date;
            public String gulf_voucher;
            public String voucher_id;

            public String getEv_code() {
                return ev_code;
            }

            public void setEv_code(String ev_code) {
                this.ev_code = ev_code;
            }

            public String getEv_pin() {
                return ev_pin;
            }

            public void setEv_pin(String ev_pin) {
                this.ev_pin = ev_pin;
            }

            public String getExpiry_date() {
                return expiry_date;
            }

            public void setExpiry_date(String expiry_date) {
                this.expiry_date = expiry_date;
            }

            public String getGulf_voucher() {
                return gulf_voucher;
            }

            public void setGulf_voucher(String gulf_voucher) {
                this.gulf_voucher = gulf_voucher;
            }

            public String getVoucher_id() {
                return voucher_id;
            }

            public void setVoucher_id(String voucher_id) {
                this.voucher_id = voucher_id;
            }
        }
    }
}
