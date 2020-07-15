package com.pertamina.musicoolpromo.model;

import java.util.Date;

public class HistoryPoints {

    private String title;
    private int total;
    private Date date;
    private String status;

    public HistoryPoints(String title, int total, Date date, String status) {
        this.title = title;
        this.total = total;
        this.date = date;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public int getTotal() {
        return total;
    }
}
