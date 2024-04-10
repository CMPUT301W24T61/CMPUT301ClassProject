package com.example.eventwiz;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckedInAttendeesActivity extends AppCompatActivity {

    private int totalCount = 0;
    private static final String TAG = "Check-In Activity";
    private ListView lvAttendees;
    private CheckedInAttendeeAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Pair<UserProfile, Integer>> attendeesInfo = new ArrayList<>();
    private TextView tvTotalCheckedInCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_in_attendees);
        setupActionBar();
        tvTotalCheckedInCount = findViewById(R.id.tvTotalCheckedInCount);
        lvAttendees = findViewById(R.id.lvAttendees);
        adapter = new CheckedInAttendeeAdapter(this, R.layout.item_checked_in_attendee, attendeesInfo);

        lvAttendees.setAdapter(adapter);

        String eventId = getIntent().getStringExtra("eventId");
        if (eventId != null) {
            loadCheckedInAttendeesForEvent(eventId);
            checkAttendanceAndSendAlert(eventId);
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

    private void updateTotalCheckedInCount(int totalCount) {
        this.totalCount=totalCount;
        String totalCheckedInText = "Total Checked In: " + totalCount;
        tvTotalCheckedInCount.setText(totalCheckedInText);
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
                        attendeesInfo.clear();
                        for (String id : profiles.keySet()) {
                            UserProfile profile = profiles.get(id);
                            Integer count = checkInsCount.getOrDefault(id, 0);
                            attendeesInfo.add(new Pair<>(profile, count));
                        }
                        adapter.notifyDataSetChanged();
                        updateTotalCheckedInCount(profiles.size());
                    }
                } else {
                    Log.d("CheckedInAttendees", "User profile is null for user ID: " + userId);
                }
            }).addOnFailureListener(e -> {
                Log.e("CheckedInAttendees", "Error fetching user profile: " + e.getMessage(), e);
            });
        }
    }

    private void sendStaticAlert(String message) {
        // Create a unique notification channel ID
        String channelId = "static_alert_channel";

        // Get the system notification manager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel for devices running Android Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Static Alert Channel";
            String channelDescription = "Channel for sending static alerts";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel);
        }

        // Create a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentTitle("Milestone Alert")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        notificationManager.notify(0, builder.build());
    }

    private void checkAttendanceAndSendAlert(String eventId) {
        db.collection("events").document(eventId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot eventDocument = task.getResult();
                        if (eventDocument.exists()) {
                            Event event = eventDocument.toObject(Event.class);
                            if (event != null) {
                                List<String> signups = event.getSignups();
                                int totalSignups = signups != null ? signups.size() : 0;

                                double attendancePercentage = (double) totalCount/ totalSignups * 100;

                                // Check if current user is the organizer and attendance is at 50%
                                FirebaseAuth mAuth;
                                mAuth = FirebaseAuth.getInstance();
                                if (event.getOrganizerId().equals(mAuth.getCurrentUser().getUid()) && attendancePercentage == 50) {
                                    String alertMessage = "Attendance has reached 50%";
                                    sendStaticAlert(alertMessage);
                                }

                                // Check if current user is the organizer and attendance is at 100%
                                if (event.getOrganizerId().equals(mAuth.getCurrentUser().getUid()) && attendancePercentage == 100) {
                                    String alertMessage = "Attendance has reached 100%";
                                    sendStaticAlert(alertMessage);
                                }
                            } else {
                                Log.d(TAG, "Event is null");
                            }
                        } else {
                            Log.d(TAG, "Event document doesn't exist");
                        }
                    } else {
                        Log.e(TAG, "Error getting event document: ", task.getException());
                    }
                });


    }
}

