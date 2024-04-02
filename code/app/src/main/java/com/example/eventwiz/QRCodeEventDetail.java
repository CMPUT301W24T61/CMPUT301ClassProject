package com.example.eventwiz;

public class QRCodeEventDetail {
    private String checkInQRCodeUrl;
    private String promotionQRCodeUrl;
    private String eventName;
    private String eventPosterUrl;
    private String hashCode; // Add a field to store the hash code of the QR code

    // Updated constructor
    public QRCodeEventDetail(String checkInQRCodeUrl, String promotionQRCodeUrl, String eventName, String eventPosterUrl, String hashCode) {
        this.checkInQRCodeUrl = checkInQRCodeUrl;
        this.promotionQRCodeUrl = promotionQRCodeUrl;
        this.eventName = eventName;
        this.eventPosterUrl = eventPosterUrl;
        this.hashCode = hashCode; // Initialize the hash code
    }

    // Getters
    public String getCheckInQRCodeUrl() { return checkInQRCodeUrl; }
    public String getPromotionQRCodeUrl() { return promotionQRCodeUrl; }
    public String getEventName() { return eventName; }
    public String getEventPosterUrl() { return eventPosterUrl; }
    public String getHashCode() { return hashCode; } // Getter for the hash code
}

