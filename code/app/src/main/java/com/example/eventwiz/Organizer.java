package com.example.eventwiz;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Organizer extends User {
    private List<Event> organizedEvents;
    private Map<String, List<String>> eventAttendeesMap; // Maps event IDs to attendee names
    private Map<String, String> eventQRCodeMap; // Maps event IDs to QR code file names or references

    public Organizer(String name) {
        super(name);
        organizedEvents = new ArrayList<>();
        eventAttendeesMap = new HashMap<>();
        eventQRCodeMap = new HashMap<>();
    }

    @Override
    public void setGeolocationTrackingEnabled(boolean isEnabled) {

    }

    public Event createEvent(String name, String description, LocalDateTime dateTime, String location, int maxAttendees) {
        Event newEvent = new Event(name, description, dateTime, location, maxAttendees);
        organizedEvents.add(newEvent);
        eventAttendeesMap.put(newEvent.getId(), new ArrayList<>()); // Initialize attendees list for the new event
        // QR code reference can be set separately if needed
        return newEvent;
    }

    public void setEventQRCodeReference(String eventId, String qrCodeReference) {
        Event event = findEventById(eventId);
        if (event != null) {
            event.setQrCodeReference(qrCodeReference);
            eventQRCodeMap.put(eventId, qrCodeReference); // Update the map with the new QR code reference
        }
    }

    // Finds an event by its ID, corrected to remove duplicate method declaration
    private Event findEventById(String eventId) {
        for (Event event : organizedEvents) {
            if (event.getId().equals(eventId)) {
                return event;
            }
        }
        return null;
    }

    public String getExistingQRCode(Event event) {
        return eventQRCodeMap.get(event.getId());
    }

    public void uploadEventPoster(String eventId, String posterPath) {
        Event event = findEventById(eventId);
        if (event != null) {
            event.setPoster(posterPath);
        }
    }
}
