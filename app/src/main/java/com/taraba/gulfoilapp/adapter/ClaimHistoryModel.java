package com.taraba.gulfoilapp.adapter;

/**
 * Created by android3 on 1/27/16.
 */
public class ClaimHistoryModel {

    private int claim_participant_id;
    private String claim_uid;
    private String claim_validation_status;
    private String claim_point;
    private String claim_part_no;
    private String claim_record_date;
    private String claim_uid_status;

    public int getClaim_participant_id() {
        return claim_participant_id;
    }

    public void setClaim_participant_id(int claim_participant_id) {
        this.claim_participant_id = claim_participant_id;
    }

    public String getClaim_uid() {
        return claim_uid;
    }

    public void setClaim_uid(String claim_uid) {
        this.claim_uid = claim_uid;
    }

    public String getClaim_validation_status() {
        return claim_validation_status;
    }

    public void setClaim_validation_status(String claim_validation_status) {
        this.claim_validation_status = claim_validation_status;
    }

    public String getClaim_point() {
        return claim_point;
    }

    public void setClaim_point(String claim_point) {
        this.claim_point = claim_point;
    }

    public String getClaim_part_no() {
        return claim_part_no;
    }

    public void setClaim_part_no(String claim_part_no) {
        this.claim_part_no = claim_part_no;
    }

    public String getClaim_record_date() {
        return claim_record_date;
    }

    public void setClaim_record_date(String claim_record_date) {
        this.claim_record_date = claim_record_date;
    }

    public String getClaim_uid_status() {
        return claim_uid_status;
    }

    public void setClaim_uid_status(String claim_uid_status) {
        this.claim_uid_status = claim_uid_status;
    }

    @Override
    public String toString() {
        return "ClaimHistoryModel{" +
                "claim_participant_id=" + claim_participant_id +
                ", claim_uid='" + claim_uid + '\'' +
                ", claim_validation_status='" + claim_validation_status + '\'' +
                ", claim_point='" + claim_point + '\'' +
                ", claim_part_no='" + claim_part_no + '\'' +
                ", claim_record_date='" + claim_record_date + '\'' +
                ", claim_uid_status='" + claim_uid_status + '\'' +
                '}';
    }
}
