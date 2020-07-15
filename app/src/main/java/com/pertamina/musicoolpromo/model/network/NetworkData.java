package com.pertamina.musicoolpromo.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NetworkData {
    @SerializedName("data")
    @Expose
    private List<NetworkResult> networkResults = null;

    public List<NetworkResult> getNetworkResults() {
        return networkResults;
    }

    public void setNetworkResults(List<NetworkResult> networkResults) {
        this.networkResults = networkResults;
    }
}
