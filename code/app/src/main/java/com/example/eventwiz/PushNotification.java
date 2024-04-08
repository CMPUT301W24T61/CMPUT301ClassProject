package com.example.eventwiz;

import com.google.gson.annotations.SerializedName;

public class PushNotification {
    @SerializedName("data")
    private NotificationData data;

    @SerializedName("to")
    private String to;

    public PushNotification(NotificationData data, String topic) {
        this.data = data;
        this.to = "/topics/my-topic";
    }

    // Getters and setters for data and to
    // Add them as needed
}



