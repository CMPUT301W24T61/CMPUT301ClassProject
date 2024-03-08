package com.example.eventwiz;

/**
 * The User class represents a generic user in the system.
 * @author Junkai
 */
public abstract class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void setGeolocationTrackingEnabled(boolean isEnabled);
}
