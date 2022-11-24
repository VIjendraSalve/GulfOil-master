package com.taraba.gulfoilapp.model;

public class TargetMeterReqest {
    private String login_id;
    private String target_meter_header_id_pk;
    private String fls_login_id;


    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getTarget_meter_header_id_pk() {
        return target_meter_header_id_pk;
    }

    public void setTarget_meter_header_id_pk(String target_meter_header_id_pk) {
        this.target_meter_header_id_pk = target_meter_header_id_pk;
    }

    public String getFls_login_id() {
        return fls_login_id;
    }

    public void setFls_login_id(String fls_login_id) {
        this.fls_login_id = fls_login_id;
    }
}
