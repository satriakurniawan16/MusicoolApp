package com.pertamina.musicoolpromo.model.Feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsFeedData {
    @SerializedName("data")
    @Expose
    private List<NewsFeedResult> movieResults = null;

    public List<NewsFeedResult> getNewsResults() {
        return movieResults;
    }

    public void setNewsFeedResults(List<NewsFeedResult> movieResults) {
        this.movieResults = movieResults;
    }
}
