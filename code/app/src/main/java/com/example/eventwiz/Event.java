package com.example.eventwiz;

<<<<<<< HEAD
import java.io.Serializable;
import java.util.UUID;

public class Event implements Serializable {
    private String id;
    private String name;
    private String description;
    private String date;
    private String startTime;
    private String endTime;
    private String location;
    private int maxAttendees;
    private String posterUrl;
    private String checkInQRCode;
    private String promotionQRCode;
    private String organizerId;

    public Event(String name, String description, String date, String startTime, String endTime, String location, int maxAttendees, String checkInQRCode, String promotionQRCode, String posterUrl) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.maxAttendees = maxAttendees;
        this.checkInQRCode = checkInQRCode;
        this.promotionQRCode = promotionQRCode;
        this.posterUrl = posterUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMaxAttendees() {
        return maxAttendees;
    }

    public void setMaxAttendees(int maxAttendees) {
        this.maxAttendees = maxAttendees;
=======
public class Event {
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String posterUrl; // URL to the image stored in Firebase
    private String venue;



    public Event(String eventName, String eventDate, String eventTime, String posterUrl, String venue) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.posterUrl = posterUrl;
        this.venue = venue;
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
>>>>>>> origin/hunaid
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

<<<<<<< HEAD
    public String getCheckInQRCode() {
        return checkInQRCode;
    }

    public void setCheckInQRCode(String checkInQRCode) {
        this.checkInQRCode = checkInQRCode;
    }

    public String getPromotionQRCode() {
        return promotionQRCode;
    }

    public void setPromotionQRCode(String promotionQRCode) {
        this.promotionQRCode = promotionQRCode;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
=======
    // You may add a toString() method for easy logging/debugging
    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                '}';
>>>>>>> origin/hunaid
    }
}
