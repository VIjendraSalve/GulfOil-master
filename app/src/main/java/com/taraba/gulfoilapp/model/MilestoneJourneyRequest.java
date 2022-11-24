package com.taraba.gulfoilapp.model;

public class MilestoneJourneyRequest {
    private String participant_login_id;
    private String target_volume;

    public String getParticipant_login_id() {
        return participant_login_id;
    }

    public void setParticipant_login_id(String participant_login_id) {
        this.participant_login_id = participant_login_id;
    }

    public String getTarget_volume() {
        return target_volume;
    }

    public void setTarget_volume(String target_volume) {
        this.target_volume = target_volume;
    }
}
