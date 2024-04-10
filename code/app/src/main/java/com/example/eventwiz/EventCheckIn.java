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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EventCheckIn extends AppCompatActivity {

    private TextView tvEventName, tvEventDate, tvEventStartTime, tvEventEndTime, tvEventLocation, tvMaxAttendees, tvEventDescription;
    private Button btnSignUp, btnCheckIn;
    private ImageView ivEventPoster, ivCheckInQRCode, ivPromotionQRCode;
    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        initializeUI();
        String eventId = getIntent().getStringExtra("eventId");
        if (eventId != null && !eventId.isEmpty()) {
            loadEventFromFirestore(eventId);
        }
        btnCheckIn = findViewById(R.id.btnCheckIn);
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check location permission
                if (ContextCompat.checkSelfPermission(EventCheckIn.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with location-based check-in
                    getLastLocation();
                } else {
                    // Permission not granted, request it from the user
                    ActivityCompat.requestPermissions(EventCheckIn.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                }
            }
        });

        ImageButton btnGoToDashboard = findViewById(R.id.gotodasboard);
        btnGoToDashboard.setOnClickListener(v -> goToDashboardActivity());
    }

    // Method to get last known location
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Location location = task.getResult();
                        GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        performCheckIn(geoPoint);
                    } else {
                        Toast.makeText(EventCheckIn.this, "Unable to retrieve location. Proceeding with basic check-in.", Toast.LENGTH_SHORT).show();
                        checkInWithoutLocation();
                    }
                });
    }


    private void checkInWithoutLocation() {
        performCheckIn(null);
    }

    private void performCheckIn(GeoPoint location) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String eventId = getIntent().getStringExtra("eventId");

        if (user != null && eventId != null && !eventId.isEmpty()) {
            String userId = user.getUid();

            // Update event document
            DocumentReference eventRef = db.collection("events").document(eventId);
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
                    checkIns.put(userId, currentCount + 1);

                    eventRef.update("checkInsCount", checkIns)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(EventCheckIn.this, "Checked in successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("EventCheckIn", "Failed to check in: " + e.getMessage());
                                Toast.makeText(EventCheckIn.this, "Failed to check in", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Log.e("EventCheckIn", "Failed to get event document: ", task.getException());
                    Toast.makeText(EventCheckIn.this, "Failed to check in", Toast.LENGTH_SHORT).show();
                }
            });

            // Update user document with the last check-in location
            DocumentReference userRef = db.collection("Users").document(userId);
            if (location != null) {
                userRef.update("lastCheckInLocation", location)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("EventCheckIn", "User location updated successfully");
                        })
                        .addOnFailureListener(e -> {
                            Log.e("EventCheckIn", "Failed to update user location: " + e.getMessage());
                        });
            }
        } else {
            Toast.makeText(this, "Error: User not logged in or invalid event ID.", Toast.LENGTH_LONG).show();
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
            // Check if max attendees is set to maximum value
            if (event.getMaxAttendees() == Integer.MAX_VALUE) {
                tvMaxAttendees.setVisibility(View.GONE);
            } else {
                tvMaxAttendees.setText(String.format("Max Attendees: %d", event.getMaxAttendees()));
                tvMaxAttendees.setVisibility(View.VISIBLE); // Ensure visibility is set to VISIBLE
            }
            tvEventDescription.setText(String.format("Event Description: %s", event.getDescription()));

            // Load images only if the URLs are not null
            if (event.getPosterUrl() != null) {
                Picasso.get().load(event.getPosterUrl()).placeholder(R.drawable.image_placeholder_background).into(ivEventPoster);
            } else {
                ivEventPoster.setImageResource(R.drawable.image_placeholder_background); // Set a placeholder image
            }
            if (event.getCheckInQRCode() != null) {
                Picasso.get().load(event.getCheckInQRCode()).placeholder(R.drawable.image_placeholder_background).into(ivCheckInQRCode);
            } else {
                ivCheckInQRCode.setImageResource(R.drawable.image_placeholder_background); // Set a placeholder image
            }
            if (event.getPromotionQRCode() != null) {
                Picasso.get().load(event.getPromotionQRCode()).placeholder(R.drawable.image_placeholder_background).into(ivPromotionQRCode);
            } else {
                ivPromotionQRCode.setImageResource(R.drawable.image_placeholder_background); // Set a placeholder image
            }
        }
    }




    private void goToDashboardActivity() {
        Intent intent = new Intent(EventCheckIn.this, DashboardActivity.class);
        startActivity(intent);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission is required for check-in.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
