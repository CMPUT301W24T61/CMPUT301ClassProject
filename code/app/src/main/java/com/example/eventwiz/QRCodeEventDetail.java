package com.example.eventwiz;

public class QRCodeEventDetail {
    private String checkInQRCodeUrl;
    private String promotionQRCodeUrl;
    private String eventName;
    private String eventPosterUrl;

    // Constructor
    public QRCodeEventDetail(String checkInQRCodeUrl, String promotionQRCodeUrl, String eventName, String eventPosterUrl) {
        this.checkInQRCodeUrl = checkInQRCodeUrl;
        this.promotionQRCodeUrl = promotionQRCodeUrl;
        this.eventName = eventName;
        this.eventPosterUrl = eventPosterUrl;
    }

    // Getters
    public String getCheckInQRCodeUrl() { return checkInQRCodeUrl; }
    public String getPromotionQRCodeUrl() { return promotionQRCodeUrl; }
    public String getEventName() { return eventName; }
    public String getEventPosterUrl() { return eventPosterUrl; }
}

