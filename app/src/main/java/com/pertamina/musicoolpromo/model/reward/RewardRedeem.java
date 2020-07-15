package com.pertamina.musicoolpromo.model.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RewardRedeem {

    @SerializedName("reward_id")
    @Expose
    private String reward_id;

    @SerializedName("requested_by")
    @Expose
    private String requested_by;

    public String getReward_id() {
        return reward_id;
    }

    public void setReward_id(String reward_id) {
        this.reward_id = reward_id;
    }

    public String getRequested_by() {
        return requested_by;
    }

    public void setRequested_by(String requested_by) {
        this.requested_by = requested_by;
    }
}
