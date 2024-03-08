package com.example.eventwiz;


import java.io.Serializable;
import java.util.UUID;

/**
 * This class represents an event in the app with relevant details.
 *
 * @see EventAdapter Used for adapting events to UI elements.
 * @author Hunaid, Junkai
 */

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


    /**
     * Default constructor required for calls to DataSnapshot.getValue(Event.class).
     * This constructor should be used for deserialization purposes.
     */
    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    }

    /**
     * Constructs a new Event object with specified details.
     *
     * @param name           The name of the event.
     * @param description    A description of the event.
     * @param date           The date of the event.
     * @param startTime      The start time of the event.
     * @param endTime        The end time of the event.
     * @param location       The location of the event.
     * @param maxAttendees   The maximum number of attendees allowed for the event.
     * @param checkInQRCode  The QR code for event check-in.
     * @param promotionQRCode The QR code for event promotions.
     * @param posterUrl      The URL of the event poster.
     */
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

    }

    /**
     *
     * @return poster URL as a string
     */
    public String getPosterUrl() {
        return posterUrl;
    }

    /**
     * Sets poster URL
     * @param posterUrl
     */
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }


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
    }


}