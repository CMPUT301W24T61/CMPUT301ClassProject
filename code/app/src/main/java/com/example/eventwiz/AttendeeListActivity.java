package com.example.eventwiz;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

        ImageButton backButton = findViewById(R.id.BackArrow);
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

        db.collection("events").document(eventId).get().addOnSuccessListener(documentSnapshot -> {
            Event event = documentSnapshot.toObject(Event.class);
            if (event != null && event.getSignups() != null) {
                fetchUserProfiles(event.getSignups());
            }
        }).addOnFailureListener(e -> {
            Log.e("AttendeeListActivity", "Error loading event", e);
        });
    }

    private void fetchUserProfiles(List<String> userIds) {
        // A counter to keep track of completed async tasks.
        AtomicInteger counter = new AtomicInteger(userIds.size());

        // Synchronized list to handle concurrent modifications.
        List<UserProfile> synchronizedAttendees = Collections.synchronizedList(new ArrayList<>());

        for (String userId : userIds) {
            db.collection("Users").document(userId).get().addOnSuccessListener(userSnapshot -> {
                if (userSnapshot.exists()) {
                    UserProfile userProfile = userSnapshot.toObject(UserProfile.class);
                    if (userProfile != null) {
                        Log.d("AttendeeListActivity", "User profile retrieved: " + userProfile.getUserName());
                        synchronizedAttendees.add(userProfile);
                    }
                } else {
                    Log.d("AttendeeListActivity", "User profile does not exist for user ID: " + userId);
                }

                // If all async tasks are completed, update the UI.
                if (counter.decrementAndGet() == 0) {
                    runOnUiThread(() -> {
                        attendees.clear();
                        attendees.addAll(synchronizedAttendees);
                        adapter.notifyDataSetChanged();
                    });
                }

            }).addOnFailureListener(e -> {
                Log.e("AttendeeListActivity", "Error fetching user profile", e);

                // Also decrement the counter on failure.
                if (counter.decrementAndGet() == 0) {
                    runOnUiThread(() -> {
                        attendees.clear();
                        attendees.addAll(synchronizedAttendees);
                        adapter.notifyDataSetChanged();
                    });
                }
            });
        }
    }



}
