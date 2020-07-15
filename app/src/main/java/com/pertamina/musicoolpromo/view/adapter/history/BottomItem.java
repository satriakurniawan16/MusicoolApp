package com.pertamina.musicoolpromo.view.adapter.history;

import androidx.annotation.NonNull;

public class BottomItem extends ListItem {
    @NonNull
    private String lol;

    public BottomItem(@NonNull String lol) {
        this.lol = lol;
    }

    @NonNull
    public String getLol() {
        return lol;
    }

    // here getters and setters
    // for title and so on, built
    // using date

    @Override
    public int getType() {
        return TYPE_BOTTOM;
    }

}
