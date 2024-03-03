package com.example.eventwiz;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Event {
    private String id;
    private String name;
    private String description;
    private LocalDateTime dateTime; // Use LocalDateTime for dateTime
    private String location;
    private int maxAttendees;
    private List<String> attendeeIds;
    private String poster;
    private String qrCodeReference; // Add field for QR code reference

    // Constructor
    public Event(String name, String description, LocalDateTime dateTime, String location, int maxAttendees) {
        this.id = UUID.randomUUID().toString(); // Generate unique ID internally
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.location = location;
        this.maxAttendees = maxAttendees;
        this.attendeeIds = new ArrayList<>();
        this.poster = null;
        this.qrCodeReference = null; // Initialize QR code reference as null
    }

    // Add a setter for QR code reference
    public void setQrCodeReference(String qrCodeReference) {
        this.qrCodeReference = qrCodeReference;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getPoster() {return poster;}

    public void setPoster(String poster) {this.poster = poster;}


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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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
    }

    public List<String> getAttendeeIds() {
        return attendeeIds;
    }

    public boolean addAttendee(String attendeeId) {
        if (attendeeIds.size() < maxAttendees) {
            return attendeeIds.add(attendeeId);
        }
        System.out.println("Event is at full capacity.");
        return false;
    }

    public boolean removeAttendee(String attendeeId) {
        return attendeeIds.remove(attendeeId);
    }

    public boolean isAttendeeRegistered(String attendeeId) {
        return attendeeIds.contains(attendeeId);
    }

    public boolean isFull() {
        return attendeeIds.size() >= maxAttendees;
    }

}
