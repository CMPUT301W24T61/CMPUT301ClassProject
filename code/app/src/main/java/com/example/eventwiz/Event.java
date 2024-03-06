package com.example.eventwiz;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Event implements Serializable {
    private final String id;
    private String name;
    private String description;
    private String date; // Changed from dateTime to date
    private String startTime; // Added
    private String endTime; // Added
    private String location;
    private int maxAttendees;
    private String poster;
    private String checkInQRCode;
    private String promotionQRCode;
    private Set<String> attendeeIds;

    public Event(String name, String description, String date, String startTime, String endTime, String location, int maxAttendees) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.maxAttendees = maxAttendees;
        this.attendeeIds = new HashSet<>();
    }


    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getMaxAttendees() { return maxAttendees; }
    public void setMaxAttendees(int maxAttendees) { this.maxAttendees = maxAttendees; }
    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }
    public String getCheckInQRCode() {
        return this.checkInQRCode;
    }
    public void setCheckInQRCode(String checkInQRCodePath) {
        this.checkInQRCode = checkInQRCodePath;
    }



    public void setPromotionQRCode(String promotionQRCodePath) {
        this.promotionQRCode = promotionQRCodePath;
    }

    public String getPromotionQRCode() {
        return this.promotionQRCode;
    }


    public Set<String> getAttendeeIds() { return attendeeIds; }
    public void setAttendeeIds(Set<String> attendeeIds) { this.attendeeIds = attendeeIds; }
}
