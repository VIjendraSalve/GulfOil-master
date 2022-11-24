package com.taraba.gulfoilapp.model;

public class LeaderBoardRequest {
    private String participant_login_id;

    public LeaderBoardRequest(String participant_login_id) {
        this.participant_login_id = participant_login_id;
    }

    public String getParticipant_login_id() {
        return participant_login_id;
    }

    public void setParticipant_login_id(String participant_login_id) {
        this.participant_login_id = participant_login_id;
    }
}
