package com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_detail.model;

import java.util.List;

public class KcCategoryOne {
    private String status;
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        private List<String> category1_list = null;

        public List<String> getCategory1_list() {
            return category1_list;
        }

        public void setCategory1_list(List<String> category1_list) {
            this.category1_list = category1_list;
        }

    }

}
