package com.pertamina.musicoolpromo.model.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RewardData {
    @SerializedName("data")
        @Expose
        private List<RewardResult> rewardResults = null;

        public List<RewardResult> getRewardResults() {
            return rewardResults;
        }

        public void setRewardResults(List<RewardResult> rewardResults) {
            this.rewardResults = rewardResults;
        }
}
