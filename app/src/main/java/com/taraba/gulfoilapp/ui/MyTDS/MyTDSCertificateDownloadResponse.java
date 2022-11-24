package com.taraba.gulfoilapp.ui.MyTDS;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MyTDSCertificateDownloadResponse {

    private String status;
    private String error;
    private String total_tds_amount;
    private Data data;

    public String getTotal_tds_amount() {
        return total_tds_amount;
    }

    public void setTotal_tds_amount(String total_tds_amount) {
        this.total_tds_amount = total_tds_amount;
    }

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

    public class Data  implements Serializable {

        private Q1 q1;
        private Q2 q2;
        private Q3 q3;
        private Q4 q4;
        private String error;

        public Q1 getQ1() {
            return q1;
        }

        public void setQ1(Q1 q1) {
            this.q1 = q1;
        }

        public Q2 getQ2() {
            return q2;
        }

        public void setQ2(Q2 q2) {
            this.q2 = q2;
        }

        public Q3 getQ3() {
            return q3;
        }

        public void setQ3(Q3 q3) {
            this.q3 = q3;
        }

        public Q4 getQ4() {
            return q4;
        }

        public void setQ4(Q4 q4) {
            this.q4 = q4;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public class Q1{
            private String quarter_status;
            private String quarter;
            private String tds_certificate;
            private String tds_amount;
            private String upload_date;

            public String getQuarter_status() {
                return quarter_status;
            }

            public void setQuarter_status(String quarter_status) {
                this.quarter_status = quarter_status;
            }

            public String getQuarter() {
                return quarter;
            }

            public void setQuarter(String quarter) {
                this.quarter = quarter;
            }

            public String getTds_certificate() {
                return tds_certificate;
            }

            public void setTds_certificate(String tds_certificate) {
                this.tds_certificate = tds_certificate;
            }

            public String getTds_amount() {
                return tds_amount;
            }

            public void setTds_amount(String tds_amount) {
                this.tds_amount = tds_amount;
            }

            public String getUpload_date() {
                return upload_date;
            }

            public void setUpload_date(String upload_date) {
                this.upload_date = upload_date;
            }
        }

        public class Q2{
            private String quarter_status;
            private String quarter;
            private String tds_certificate;
            private String tds_amount;
            private String upload_date;

            public String getQuarter_status() {
                return quarter_status;
            }

            public void setQuarter_status(String quarter_status) {
                this.quarter_status = quarter_status;
            }

            public String getQuarter() {
                return quarter;
            }

            public void setQuarter(String quarter) {
                this.quarter = quarter;
            }

            public String getTds_certificate() {
                return tds_certificate;
            }

            public void setTds_certificate(String tds_certificate) {
                this.tds_certificate = tds_certificate;
            }

            public String getTds_amount() {
                return tds_amount;
            }

            public void setTds_amount(String tds_amount) {
                this.tds_amount = tds_amount;
            }

            public String getUpload_date() {
                return upload_date;
            }

            public void setUpload_date(String upload_date) {
                this.upload_date = upload_date;
            }
        }

        public class Q3{
            private String quarter_status;
            private String quarter;
            private String tds_certificate;
            private String tds_amount;
            private String upload_date;

            public String getQuarter_status() {
                return quarter_status;
            }

            public void setQuarter_status(String quarter_status) {
                this.quarter_status = quarter_status;
            }

            public String getQuarter() {
                return quarter;
            }

            public void setQuarter(String quarter) {
                this.quarter = quarter;
            }

            public String getTds_certificate() {
                return tds_certificate;
            }

            public void setTds_certificate(String tds_certificate) {
                this.tds_certificate = tds_certificate;
            }

            public String getTds_amount() {
                return tds_amount;
            }

            public void setTds_amount(String tds_amount) {
                this.tds_amount = tds_amount;
            }

            public String getUpload_date() {
                return upload_date;
            }

            public void setUpload_date(String upload_date) {
                this.upload_date = upload_date;
            }
        }

        public class Q4{
            private String quarter_status;
            private String quarter;
            private String tds_certificate;
            private String tds_amount;
            private String upload_date;

            public String getQuarter_status() {
                return quarter_status;
            }

            public void setQuarter_status(String quarter_status) {
                this.quarter_status = quarter_status;
            }

            public String getQuarter() {
                return quarter;
            }

            public void setQuarter(String quarter) {
                this.quarter = quarter;
            }

            public String getTds_certificate() {
                return tds_certificate;
            }

            public void setTds_certificate(String tds_certificate) {
                this.tds_certificate = tds_certificate;
            }

            public String getTds_amount() {
                return tds_amount;
            }

            public void setTds_amount(String tds_amount) {
                this.tds_amount = tds_amount;
            }

            public String getUpload_date() {
                return upload_date;
            }

            public void setUpload_date(String upload_date) {
                this.upload_date = upload_date;
            }
        }


    }





}
