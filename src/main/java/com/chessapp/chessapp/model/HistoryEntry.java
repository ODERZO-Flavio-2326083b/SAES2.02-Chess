package com.chessapp.chessapp.model;

public class HistoryEntry {
    private String pseudo1;
    private String pseudo2;
    private String date;

    public HistoryEntry(String pseudo1, String pseudo2, String date) {
        this.pseudo1 = pseudo1;
        this.pseudo2 = pseudo2;
        this.date = date;
    }

    public String getPseudo1() {
        return pseudo1;
    }

    public String getPseudo2() {
        return pseudo2;
    }

    public String getDate() {
        return date;
    }
}
