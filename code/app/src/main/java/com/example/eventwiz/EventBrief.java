package com.example.eventwiz;

/**
 * Represents a brief version of an event, containing essential information such as event name, date, time, poster URL, and location.
 * @author Yesith
 */
public class EventBrief {
    private String eventName;
    private String eventDate;
    private String eventStartTime;
    private String eventEndTime;
    private String posterUrl; // URL to the image stored in Firebase
    //private String venue;

    private String location;



    /**
     * Constructs an EventBrief object with the specified details.
     * @param eventName The name of the event.
     * @param eventDate The date of the event.
     * @param eventStartTime The start time of the event.
     * @param eventEndTime The end time of the event.
     * @param posterUrl The URL to the poster image of the event.
     * @param location The location of the event.
     */
    public EventBrief(String eventName, String eventDate,String eventStartTime,String eventEndTime, String posterUrl,String location) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.posterUrl = posterUrl;
        //this.venue = venue;
        this.location = location;
    }

    /**
     * Gets the start time of the event.
     * @return The start time of the event.
     */
    public String getEventStartTime() {
        return eventStartTime;
    }

    /**
     * Sets the start time of the event.
     * @param eventStartTime The start time of the event.
     */
    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    /**
     * Gets the end time of the event.
     * @return The end time of the event.
     */
    public String getEventEndTime() {
        return eventEndTime;
    }

    /**
     * Sets the end time of the event.
     * @param eventEndTime The end time of the event.
     */
    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    // Getters and Setters

    /*
    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
    */

    /**
     * Gets the name of the event.
     * @return The name of the event.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the name of the event.
     * @param eventName The name of the event.
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the date of the event.
     * @return The date of the event.
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * Sets the date of the event.
     * @param eventDate The date of the event.
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Gets the URL to the poster image of the event.
     * @return The URL to the poster image of the event.
     */
    public String getPosterUrl() {
        return posterUrl;
    }

    /**
     * Sets the URL to the poster image of the event.
     * @param posterUrl The URL to the poster image of the event.
     */
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    /**
     * Gets the location of the event.
     * @return The location of the event.
     */

    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the event.
     * @param location The location of the event.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    // You may add a toString() method for easy logging/debugging
    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventStartTime='" + eventStartTime + '\'' +
                ", eventEndTime='" + eventEndTime + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
