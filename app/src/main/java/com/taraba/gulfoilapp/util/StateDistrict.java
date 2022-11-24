package com.taraba.gulfoilapp.util;

/**
 * Created by android3 on 12/24/15.
 */
public class StateDistrict {
    private int id;
    private String Statename;
    private String Districtname;

    public StateDistrict() {
    }

    public StateDistrict(int id, String Statename, String Districtname) {
        this.id = id;
        this.Statename = Statename;
        this.Districtname = Districtname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatename(String Statename) {
        this.Statename = Statename;
    }

    public void setDistrictname(String Districtname) {
        this.Districtname = Districtname;
    }

    public int getId() {
        return id;
    }

    public String getDistrictname() {
        return Districtname;
    }

    public String getStatename() {
        return Statename;
    }
}