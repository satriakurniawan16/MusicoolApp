package com.pertamina.musicoolpromo.view.adapter.history;

import androidx.annotation.NonNull;

import com.pertamina.musicoolpromo.model.HistoryPoints;

public class HistoryItem extends ListItem {

    @NonNull
    private HistoryPoints historyPoints;

    public HistoryItem(@NonNull HistoryPoints historyPoints) {
        this.historyPoints= historyPoints;
    }


    @NonNull
    public HistoryPoints getHistoryPoints() {
        return historyPoints;
    }


    // here getters and setters
    // for title and so on, built
    // using event

    @Override
    public int getType() {
        return TYPE_EVENT;
    }

}