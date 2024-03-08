package com.example.eventwiz;

/**
 * This class stores all the data for Events made in the app
 * @see EventAdapter
 * @author Hunaid
 */
public class Event {
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String posterUrl; // URL to the image stored in Firebase
    private String venue;
    private String eventID;


    /**
     * This method initializes the events information.
     * @param eventName
     * @param eventDate
     * @param eventTime
     * @param posterUrl
     * @param venue
     */
    public Event(String eventName, String eventDate, String eventTime, String posterUrl, String venue, String eventID) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.posterUrl = posterUrl;
        this.venue = venue;
        this.eventID = eventID;
    }

    // Getters and Setters

    /**
     * @return venue name as a string
     */
    public String getVenue() {
        return venue;
    }

    /**
     * Sets venue name
     * @param venue
     */
    public void setVenue(String venue) {
        this.venue = venue;
    }

    /**
     *
     * @return event name as a string
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the event name
     * @param eventName
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     *
     * @return event date as a string
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * Sets event date
     * @param eventDate
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /**
     *
     * @return event time as a string
     */
    public String getEventTime() {
        return eventTime;
    }

    /**
     * Sets event time
     * @param eventTime
     */
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
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

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     *
     * @return all the details of an event in string format
     */
    // You may add a toString() method for easy logging/debugging
    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                '}';
    }
}