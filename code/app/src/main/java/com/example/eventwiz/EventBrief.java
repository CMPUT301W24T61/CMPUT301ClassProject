package com.example.eventwiz;

public class EventBrief {
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String posterUrl; // URL to the image stored in Firebase
    private String venue;

    private String location;



    public EventBrief(String eventName, String eventDate, String eventTime, String posterUrl, String venue,String location
    ) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.posterUrl = posterUrl;
        this.venue = venue;
        this.location = location;
    }

    // Getters and Setters
    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // You may add a toString() method for easy logging/debugging
    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
