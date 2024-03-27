package com.example.eventwiz;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class EventCheckIn extends AppCompatActivity {

    private TextView tvEventName, tvEventDate, tvEventStartTime, tvEventEndTime, tvEventLocation, tvMaxAttendees, tvEventDescription;
    private Button btnSignUp, btnCheckIn;
    private ImageView ivEventPoster, ivCheckInQRCode, ivPromotionQRCode;
    private FirebaseFirestore db;

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
        ImageButton btnGoToDashboard = findViewById(R.id.gotodasboard);
        btnGoToDashboard.setOnClickListener(v -> goToDashboardActivity());
    }
    private void checkInToEvent() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String eventId = getIntent().getStringExtra("eventId");

        if (user != null && eventId != null && !eventId.isEmpty()) {
            String userId = user.getUid();

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

                    // Update the document
                    eventRef.update("checkInsCount", checkIns)
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
        } else {
            // User not logged in or eventId is null/empty
            Toast.makeText(this, "Error: User not logged in or invalid event ID.", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Initializes UI elements by finding their respective views in the layout.
     */
    private void initializeUI() {
        tvEventName = findViewById(R.id.tvEventName);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventLocation = findViewById(R.id.tvEventLocation);
        tvMaxAttendees = findViewById(R.id.tvMaxAttendees);
        ivEventPoster = findViewById(R.id.ivEventPoster);
        ivCheckInQRCode = findViewById(R.id.ivCheckInQRCode);
        ivPromotionQRCode = findViewById(R.id.ivPromotionQRCode);
        tvEventStartTime = findViewById(R.id.tvEventStartTime);
        tvEventEndTime = findViewById(R.id.tvEventEndTime);
        tvEventDescription = findViewById(R.id.tvEventDescription);
    }

    /**
     * Retrieves event details from Firestore using the provided eventId.
     *
     * @param eventId The unique identifier of the event to be loaded.
     */
    private void loadEventFromFirestore(String eventId) {
        DocumentReference eventDocument = db.collection("events").document(eventId);
        eventDocument.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Event event = documentSnapshot.toObject(Event.class);
                loadEventDetails(event);
            }
        });
    }

    /**
     * Populates the UI with details of the given event.
     *
     * @param event The event object containing details to be displayed.
     */
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
    /**
     * Navigates to the DashboardActivity when the "Go to Dashboard" button is clicked.
     */
    private void goToDashboardActivity() {
        Intent intent = new Intent(EventCheckIn.this, DashboardActivity.class);
        startActivity(intent);
    }
}

