package com.example.eventwiz;

public class Announcement {
    private String title;
    private String description;
    private String date;

    public Announcement(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }
    // Default constructor (no-argument constructor)
    public Announcement() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


