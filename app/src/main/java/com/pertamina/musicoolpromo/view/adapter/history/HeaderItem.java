package com.pertamina.musicoolpromo.view.adapter.history;

import androidx.annotation.NonNull;

import java.util.Date;

public class HeaderItem extends ListItem implements Comparable<HeaderItem>{

    @NonNull
    private Date date;

    public HeaderItem(@NonNull Date date) {
        this.date = date;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }

    @Override
    public int compareTo(HeaderItem o) {
        return getDate().compareTo(o.getDate());
    }
}