package com.example.eventwiz;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckedInAttendeesActivity extends AppCompatActivity {
    private ListView lvAttendees;
    private CheckedInAttendeeAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Pair<UserProfile, Integer>> attendeesInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_in_attendees);
        setupActionBar();

        lvAttendees = findViewById(R.id.lvAttendees);
        adapter = new CheckedInAttendeeAdapter(this, R.layout.item_checked_in_attendee, attendeesInfo);

        lvAttendees.setAdapter(adapter);

        String eventId = getIntent().getStringExtra("eventId");
        if (eventId != null) {
            loadCheckedInAttendeesForEvent(eventId);
        }

        ImageButton backButton = findViewById(R.id.back_arrow);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Checked-in Attendees");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadCheckedInAttendeesForEvent(String eventId) {
        Log.d("CheckedInAttendees", "Loading checked-in attendees for event: " + eventId);
        db.collection("events").document(eventId).get().addOnSuccessListener(documentSnapshot -> {
            Event event = documentSnapshot.toObject(Event.class);
            if (event != null && event.getCheckInsCount() != null) {
                fetchUserProfiles(new ArrayList<>(event.getCheckInsCount().keySet()), event.getCheckInsCount());
            } else {
                Log.d("CheckedInAttendees", "Event or check-ins list is null.");
            }
        }).addOnFailureListener(e -> {
            Log.e("CheckedInAttendees", "Error loading event: " + e.getMessage(), e);
        });
    }

    private void fetchUserProfiles(List<String> userIds, Map<String, Integer> checkInsCount) {
        Map<String, UserProfile> profiles = new HashMap<>();
        for (String userId : userIds) {
            db.collection("Users").document(userId).get().addOnSuccessListener(userSnapshot -> {
                UserProfile userProfile = userSnapshot.toObject(UserProfile.class);
                if (userProfile != null) {
                    profiles.put(userId, userProfile);
                    if (profiles.size() == userIds.size()) {
                        for (Map.Entry<String, Integer> entry : checkInsCount.entrySet()) {
                            UserProfile profile = profiles.get(entry.getKey());
                            Integer count = entry.getValue();
                            attendeesInfo.add(new Pair<>(profile, count));
                        }
                        adapter.notifyDataSetChanged();
                        Log.d("CheckedInAttendees", "User profiles retrieved and displayed.");
                    }
                } else {
                    Log.d("CheckedInAttendees", "User profile is null for user ID: " + userId);
                }
            }).addOnFailureListener(e -> {
                Log.e("CheckedInAttendees", "Error fetching user profile: " + e.getMessage(), e);
            });
        }
    }
}
