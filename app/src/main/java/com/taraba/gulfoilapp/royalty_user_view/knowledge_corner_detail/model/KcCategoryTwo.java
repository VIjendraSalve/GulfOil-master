package com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_detail.model;

import java.util.List;

public class KcCategoryTwo {
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

        private List<String> category2_list = null;

        public List<String> getCategory2_list() {
            return category2_list;
        }

        public void setCategory2_list(List<String> category2_list) {
            this.category2_list = category2_list;
        }
    }

}
