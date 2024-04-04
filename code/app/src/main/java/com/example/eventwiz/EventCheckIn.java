package com.example.eventwiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class EventCheckIn extends AppCompatActivity {

    private TextView tvEventName, tvEventDate, tvEventStartTime, tvEventEndTime, tvEventLocation, tvMaxAttendees, tvEventDescription;
    private Button btnSignUp, btnCheckIn;
    private ImageView ivEventPoster, ivCheckInQRCode, ivPromotionQRCode;
    private FirebaseFirestore db;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_checkin);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Event Details");
            int color = ContextCompat.getColor(this, R.color.turqoise);
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
        db = FirebaseFirestore.getInstance();
        initializeUI();
        String eventId = getIntent().getStringExtra("eventId");
        if (eventId != null && !eventId.isEmpty()) {
            loadEventFromFirestore(eventId);
        }
        btnCheckIn = findViewById(R.id.btnCheckIn);
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInToEvent();
            }
        });
        ImageButton btnGoToDashboard = findViewById(R.id.goback);
        btnGoToDashboard.setOnClickListener(v -> goToDashboardActivity());
    }

    private void checkInToEvent() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String eventId = getIntent().getStringExtra("eventId");

        if (user != null && eventId != null && !eventId.isEmpty()) {
            String userId = user.getUid();

            // Check location permissions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                // Location permissions granted, proceed with location retrieval
                getLocationAndCheckIn(eventId, userId);
            } else {
                // Request location permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            // User not logged in or eventId is null/empty
            Toast.makeText(this, "Error: User not logged in or invalid event ID.", Toast.LENGTH_LONG).show();
        }
    }

    private void getLocationAndCheckIn(String eventId, String userId) {
        // Get the FusedLocationProviderClient
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        // Location retrieved successfully, now you can proceed with check-in
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        // Store the location in the database or perform any other action
                        // Here you can update the check-ins count along with the location
                        updateCheckInsAndLocation(eventId, userId, latitude, longitude);
                    } else {
                        // Location is null
                        Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateCheckInsAndLocation(String eventId, String userId, double latitude, double longitude) {
        DocumentReference eventRef = db.collection("events").document(eventId);
        // First, get the current event document to check the existing check-ins
        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                Map<String, Object> checkIns = (Map<String, Object>) document.get("checkInsCount");
                if (checkIns == null) {
                    checkIns = new HashMap<>();
                }
                Object currentCountObject = checkIns.get(userId);
                int currentCount = 0;
                if (currentCountObject instanceof Long) {
                    currentCount = ((Long) currentCountObject).intValue();
                } else if (currentCountObject instanceof Integer) {
                    currentCount = (Integer) currentCountObject;
                }
                // Increment the count
                checkIns.put(userId, currentCount + 1);

                // Update the document with check-ins count and location
                Map<String, Object> eventData = new HashMap<>();
                eventData.put("checkInsCount", checkIns);
                eventData.put("lastCheckInLocation", new GeoPoint(latitude, longitude));

                eventRef.update(eventData)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(EventCheckIn.this, "Checked in successfully", Toast.LENGTH_SHORT).show();
                            // Optionally, navigate to another activity or update UI
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(EventCheckIn.this, "Failed to check in: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Log.d("EventCheckIn", "Failed to get event document: ", task.getException());
                Toast.makeText(EventCheckIn.this, "Failed to check in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with location retrieval
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String eventId = getIntent().getStringExtra("eventId");
                if (user != null && eventId != null && !eventId.isEmpty()) {
                    String userId = user.getUid();
                    getLocationAndCheckIn(eventId, userId);
                } else {
                    // Handle the case where user or eventId is null
                }
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializeUI() {
        tvEventName = findViewById(R.id.tv_event_name);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventLocation = findViewById(R.id.tvEventLocation);
        tvMaxAttendees = findViewById(R.id.tvMaxAttendees);
        ivEventPoster = findViewById(R.id.iv_event_poster);
        ivCheckInQRCode = findViewById(R.id.ivCheckInQRCode);
        ivPromotionQRCode = findViewById(R.id.ivPromotionQRCode);
        tvEventStartTime = findViewById(R.id.tvEventStartTime);
        tvEventEndTime = findViewById(R.id.tvEventEndTime);
        tvEventDescription = findViewById(R.id.tvEventDescription);
    }

    private void loadEventFromFirestore(String eventId) {
        DocumentReference eventDocument = db.collection("events").document(eventId);
        eventDocument.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Event event = documentSnapshot.toObject(Event.class);
                loadEventDetails(event);
            }
        });
    }

    private void loadEventDetails(Event event) {
        if (event != null) {
            tvEventName.setText(event.getName());
            tvEventDate.setText(String.format("Date: %s", event.getDate()));
            tvEventStartTime.setText(String.format("Start Time: %s", event.getStartTime()));
            tvEventEndTime.setText(String.format("End Time: %s", event.getEndTime()));
            tvEventLocation.setText(String.format("Location: %s", event.getLocation()));
            tvMaxAttendees.setText(String.format("Max Attendees: %d", event.getMaxAttendees()));
            tvEventDescription.setText(String.format("Event Description: %s", event.getDescription()));

            Glide.with(this).load(event.getPosterUrl()).placeholder(R.drawable.image_placeholder_background).into(ivEventPoster);
            Glide.with(this).load(event.getCheckInQRCode()).placeholder(R.drawable.image_placeholder_background).into(ivCheckInQRCode);
            Glide.with(this).load(event.getPromotionQRCode()).placeholder(R.drawable.image_placeholder_background).into(ivPromotionQRCode);
        }
    }

    private void goToDashboardActivity() {
        Intent intent = new Intent(EventCheckIn.this, DashboardActivity.class);
        startActivity(intent);
    }
}
