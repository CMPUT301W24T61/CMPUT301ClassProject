package com.example.eventwiz;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;


import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * BrowseEventsActivity is responsible for displaying events in a list from the firebase database
 * @see Event
 * @author Hunaid
 */

public class BrowseEventsActivity extends AppCompatActivity {
    private ListView listView;
    private EventAdapter adapter;
    private List<Event> events; // You should populate this list with actual event data

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_events);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("EventWiz");
            int color = ContextCompat.getColor(this, R.color.turqoise);

            // Set the background color of the ActionBar
            actionBar.setBackgroundDrawable(new ColorDrawable(color));

            // Set display option
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.lvEvents);
        events = new ArrayList<>();
        adapter = new EventAdapter(this, events);
        listView.setAdapter(adapter);
        fetchEvents();

       // code to remove event if admin detected
        AdminService adminService = new AdminService();
        if(QRCodeScannerActivity.isUserAdmin()){
            listView.setOnItemClickListener((parent, view, position, id) -> {
                if (QRCodeScannerActivity.isUserAdmin()) {
                    Event selectedEvent = adapter.getItem(position);
                    new AlertDialog.Builder(BrowseEventsActivity.this)
                            .setTitle("Remove Event")
                            .setMessage("Are you sure you want to remove this event?")
                            .setPositiveButton("Yes", (dialog, which) -> removeEvent(selectedEvent))
                            .setNegativeButton("No", null)
                            .show();
                }
            });
        }
    }

    private void removeEvent(Event event) { //only remove if user is admin
        // Remove event from Firebase
        db.collection("events").document(event.getEventID()) // Ensure your Event class has an id field
                .delete()
                .addOnSuccessListener(aVoid -> {
                    events.remove(event);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(BrowseEventsActivity.this, "Event removed successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(BrowseEventsActivity.this, "Error removing event", Toast.LENGTH_SHORT).show());
    }

    private void fetchEvents() {
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String eventName = document.getString("name");
                            String eventDate = document.getString("date");
                            String eventTimeEnd = document.getString("endTime");
                            String eventTimeStart = document.contains("startTime") ? document.getString("startTime") : ""; // Assuming 'startTime' in your Firestore
                            String posterUrl = document.getString("posterUrl");
                            String venue = document.getString("location");
                            String eventID = document.getString("id");

                            Event newEvent = new Event(eventName, eventDate +"  " + eventTimeStart + " to " + eventTimeEnd, eventTimeStart, posterUrl, venue, eventID);
                            events.add(newEvent);
                        }
                        adapter.notifyDataSetChanged(); // Notify the adapter that the data set has changed
                    } else {
                        // Handle the error
                        Log.d("error", "This is firebase error");
                    }
                });
    }
}