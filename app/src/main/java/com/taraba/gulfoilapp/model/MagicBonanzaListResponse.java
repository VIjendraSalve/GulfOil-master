package com.taraba.gulfoilapp.model;

import java.io.Serializable;
import java.util.List;

public class MagicBonanzaListResponse implements Serializable {
    private String status;
    private List<Data> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable {
        private String header;
        private String name;
        private String campaign_name;
        private String target_meter_header_id_pk;

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCampaign_name() {
            return campaign_name;
        }

        public void setCampaign_name(String campaign_name) {
            this.campaign_name = campaign_name;
        }

        public String getTarget_meter_header_id_pk() {
            return target_meter_header_id_pk;
        }

        public void setTarget_meter_header_id_pk(String target_meter_header_id_pk) {
            this.target_meter_header_id_pk = target_meter_header_id_pk;
        }
    }
}
