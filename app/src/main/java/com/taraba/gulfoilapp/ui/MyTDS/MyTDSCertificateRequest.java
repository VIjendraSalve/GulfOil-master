package com.taraba.gulfoilapp.ui.MyTDS;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MyTDSCertificateRequest {

    private String participant_login_id;
    private String period_of;

    public String getParticipant_login_id() {
        return participant_login_id;
    }

    public void setParticipant_login_id(String participant_login_id) {
        this.participant_login_id = participant_login_id;
    }

    public String getPeriod_of() {
        return period_of;
    }

    public void setPeriod_of(String period_of) {
        this.period_of = period_of;
    }
}
