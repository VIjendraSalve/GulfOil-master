package com.taraba.gulfoilapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class YDRResponse implements Serializable {
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

    public class Data  implements Serializable{
        @SerializedName("unnati")
        private List<Unnati> unnatiList;
        @SerializedName("campaign")
        private List<Campagin> campaignList;
        @SerializedName("milestone")
        private List<Milestone> milestoneList;

        public List<Unnati> getUnnatiList() {
            return unnatiList;
        }

        public void setUnnatiList(List<Unnati> unnatiList) {
            this.unnatiList = unnatiList;
        }

        public List<Campagin> getCampaignList() {
            return campaignList;
        }

        public void setCampaignList(List<Campagin> campaignList) {
            this.campaignList = campaignList;
        }

        public List<Milestone> getMilestoneList() {
            return milestoneList;
        }

        public void setMilestoneList(List<Milestone> milestoneList) {
            this.milestoneList = milestoneList;
        }

        public class Unnati implements Serializable{
            private String order_id;
            private String orders_id_pk;
            private String order_detail_id;
            private String orders_record_date;
            private String reward_name;
            private String status;
            private String reward_code;
            private String reward_value;
            private String quantity;
            private String product_image;
            private String view_codes;

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

            public String getProduct_image() {
                return product_image;
            }

            public void setProduct_image(String product_image) {
                this.product_image = product_image;
            }

            public String getView_codes() {
                return view_codes;
            }

            public void setView_codes(String view_codes) {
                this.view_codes = view_codes;
            }
        }

        public class Campagin implements Serializable{
            private String order_id;
            private String orders_id_pk;
            private String order_detail_id;
            private String orders_record_date;
            private String reward_name;
            private String status;
            private String reward_code;
            private String reward_value;
            private String quantity;
            private String product_image;
            private String campaign_name;
            private String view_codes;

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

            public String getProduct_image() {
                return product_image;
            }

            public void setProduct_image(String product_image) {
                this.product_image = product_image;
            }

            public String getCampaign_name() {
                return campaign_name;
            }

            public void setCampaign_name(String campaign_name) {
                this.campaign_name = campaign_name;
            }

            public String getView_codes() {
                return view_codes;
            }

            public void setView_codes(String view_codes) {
                this.view_codes = view_codes;
            }
        }

        public class Milestone implements Serializable {
            private String order_id;
            private String orders_id_pk;
            private String order_detail_id;
            private String orders_record_date;
            private String reward_name;
            private String status;
            private String reward_code;
            private String reward_value;
            private String quantity;
            private String product_image;
            private String view_codes;

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

            public String getProduct_image() {
                return product_image;
            }

            public void setProduct_image(String product_image) {
                this.product_image = product_image;
            }

            public String getView_codes() {
                return view_codes;
            }

            public void setView_codes(String view_codes) {
                this.view_codes = view_codes;
            }
        }
    }
}
