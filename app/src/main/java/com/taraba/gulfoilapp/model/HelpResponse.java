package com.taraba.gulfoilapp.model;

public class HelpResponse {
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

    public class Data {
        private String faq;
        private String contact_us;

        public String getFaq() {
            return faq;
        }

        public void setFaq(String faq) {
            this.faq = faq;
        }

        public String getContact_us() {
            return contact_us;
        }

        public void setContact_us(String contact_us) {
            this.contact_us = contact_us;
        }
    }
}
