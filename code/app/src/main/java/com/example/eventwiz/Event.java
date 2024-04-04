package com.example.eventwiz;


import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.Map;

/**
 * This class stores all the data for Events made in the app
 * @see EventAdapter
 * @author Hunaid,Junkai
 */

public class Event implements Serializable {
    private String id;
    private String name;
    private String description;
    private String date;
    private String startTime;
    private String endTime;
    private String location;
    private Integer maxAttendees;
    private String posterUrl;
    private String checkInQRCode;
    private String promotionQRCode;
    private String organizerId;
    private List<String> signups;

    private Map<String, Integer> checkInsCount;

    private String hashCode;
    private String promotionHashCode;

    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    }
    public Event(String name, String description, String date, String startTime, String endTime, String location, Integer maxAttendees, String checkInQRCode, String promotionQRCode, String posterUrl, String hashCode, String promotionHashCode, List<String> signups, Map<String, Integer> checkInsCount) {
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
        this.signups = signups;
        this.checkInsCount = checkInsCount;
        this.posterUrl = posterUrl;
        this.hashCode= hashCode;
        this.promotionHashCode = promotionHashCode;
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

    public Integer getMaxAttendees() {
        return maxAttendees;
    }

    public void setMaxAttendees(Integer maxAttendees) {
        this.maxAttendees = maxAttendees;

    }

    public List<String> getSignups() {return signups;}

    public void setSignups(List<String> signups) {this.signups = signups;}

    public Map<String, Integer> getCheckInsCount() {return checkInsCount;}

    public void setCheckInsCount(Map<String, Integer> checkInsCount) {this.checkInsCount = checkInsCount;}


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

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getPromotionHashCode() {
        return promotionHashCode;
    }

    public void setPromotionHashCode(String promotionHashCode) {
        this.promotionHashCode = promotionHashCode;
    }
}