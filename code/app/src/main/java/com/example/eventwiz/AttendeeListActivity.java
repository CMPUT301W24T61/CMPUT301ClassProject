package com.example.eventwiz;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AttendeeListActivity extends AppCompatActivity {
    private ListView lvAttendees;
    private AttendeeAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<UserProfile> attendees = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_signed_up_list);
        setupActionBar();

        lvAttendees = findViewById(R.id.lvAttendees);
        adapter = new AttendeeAdapter(this, attendees);
        lvAttendees.setAdapter(adapter);

        String eventId = getIntent().getStringExtra("eventId");
        if (eventId != null) {
            loadAttendeesForEvent(eventId);
        }

        ImageButton backButton = findViewById(R.id.back_arrow);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Signed Up Attendees");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadAttendeesForEvent(String eventId) {
        Log.d("AttendeeListActivity", "Loading attendees for event: " + eventId);
        db.collection("events").document(eventId).get().addOnSuccessListener(documentSnapshot -> {
            Event event = documentSnapshot.toObject(Event.class);
            if (event != null && event.getSignups() != null) {
                Log.d("AttendeeListActivity", "Event retrieved successfully. Fetching user profiles...");
                fetchUserProfiles(event.getSignups());
            } else {
                Log.d("AttendeeListActivity", "Event or signups list is null.");
            }
        }).addOnFailureListener(e -> {
            Log.e("AttendeeListActivity", "Error loading event: " + e.getMessage(), e);
        });
    }

    private void fetchUserProfiles(List<String> userIds) {
        attendees.clear();
        for (String userId : userIds) {
            db.collection("Users").document(userId).get().addOnSuccessListener(userSnapshot -> {
                UserProfile userProfile = userSnapshot.toObject(UserProfile.class);
                if (userProfile != null) {
                    attendees.add(userProfile);
                    adapter.notifyDataSetChanged();
                    Log.d("AttendeeListActivity", "User profile retrieved: " + userProfile.getUserName());
                } else {
                    Log.d("AttendeeListActivity", "User profile is null for user ID: " + userId);
                }
            }).addOnFailureListener(e -> {
                Log.e("AttendeeListActivity", "Error fetching user profile: " + e.getMessage(), e);
            });
        }
    }


}
