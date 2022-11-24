package com.taraba.gulfoilapp.model;

import java.util.List;

public class BankMasterResponse {
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
        private List<String> bank_list;
        private List<String> account_type;

        public List<String> getBank_list() {
            return bank_list;
        }

        public void setBank_list(List<String> bank_list) {
            this.bank_list = bank_list;
        }

        public List<String> getAccount_type() {
            return account_type;
        }

        public void setAccount_type(List<String> account_type) {
            this.account_type = account_type;
        }
    }
}
