package com.taraba.gulfoilapp.ui.MyTDS;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MyTDSCertificate {

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

    public class Data  implements Serializable {

        @SerializedName("period_of")
        ArrayList<String> period_of = new ArrayList<>();

        public ArrayList<String> getPeriodOfArraylist() {
            return period_of;
        }

        public void setPeriodOfArraylist(ArrayList<String> periodOfArraylist) {
            this.period_of = periodOfArraylist;
        }
    }


    @Override
    public String toString() {
        return data.getPeriodOfArraylist().get(0);
    }


}
