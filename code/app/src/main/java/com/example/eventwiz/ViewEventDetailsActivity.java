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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * ViewEventDetailsActivity displays the details of a specific event.
 * It retrieves event details from Firestore and populates the UI accordingly.
 */
public class ViewEventDetailsActivity extends AppCompatActivity {

    private TextView tvEventName, tvEventDate, tvEventStartTime, tvEventEndTime, tvEventLocation, tvMaxAttendees, tvEventDescription;
    private Button btnSignUp, btnCheckIn;
    private ImageView ivEventPoster, ivCheckInQRCode, ivPromotionQRCode;
    private FirebaseFirestore db;
    private Event event;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle).
     *                           Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_event_details);
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
        ivCheckInQRCode = findViewById(R.id.ivCheckInQRCode);
        ivPromotionQRCode = findViewById(R.id.ivPromotionQRCode);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpForEvent();
            }
        });
        ImageButton btnGoToDashboard = findViewById(R.id.gotodasboard);
        btnGoToDashboard.setOnClickListener(v -> goToDashboardActivity());
        ivEventPoster.setOnClickListener(view -> {
            if (event != null && event.getPosterUrl() != null && !event.getPosterUrl().isEmpty()) {
                String imageUrl = event.getPosterUrl();
                Log.d("EventCreationSuccess", "Displaying Event Poster: " + imageUrl);
                EnlargeImageFragment enlargeImageFragment = EnlargeImageFragment.newInstance(imageUrl);
                enlargeImageFragment.show(getSupportFragmentManager(), "enlarge_event_poster");
            } else {
                Log.e("EventCreationSuccess", "Event Poster URL is null or empty.");
            }
        });
        ivCheckInQRCode.setOnClickListener(view -> {
            if (event != null && event.getCheckInQRCode() != null && !event.getCheckInQRCode().isEmpty()) {
                String imageUrl = event.getCheckInQRCode();
                EnlargeImageFragment enlargeImageFragment = EnlargeImageFragment.newInstance(imageUrl);
                enlargeImageFragment.show(getSupportFragmentManager(), "enlarge_check_in_qr");
            }
        });

        ivPromotionQRCode.setOnClickListener(view -> {
            if (event != null && event.getPromotionQRCode() != null && !event.getPromotionQRCode().isEmpty()) {
                String imageUrl = event.getPromotionQRCode();
                EnlargeImageFragment enlargeImageFragment = EnlargeImageFragment.newInstance(imageUrl);
                enlargeImageFragment.show(getSupportFragmentManager(), "enlarge_promotion_qr");
            }
        });
    }
    private void signUpForEvent() {
        String eventId = getIntent().getStringExtra("eventId");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null || eventId == null || eventId.trim().isEmpty()) {
            Toast.makeText(ViewEventDetailsActivity.this, "Error: Invalid event ID or user not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        DocumentReference eventRef = db.collection("events").document(eventId);


        eventRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Event event = documentSnapshot.toObject(Event.class);
                if (event != null) {
                    List<String> currentSignUps = event.getSignups();
                    Integer maxAttendees = event.getMaxAttendees(); // Use Integer to allow for null

                    // Proceed if no maxAttendees set (null) or current sign-ups are less than max
                    if (maxAttendees == null || (currentSignUps == null || currentSignUps.size() < maxAttendees)) {
                        // Check if the user is already signed up
                        if (currentSignUps != null && currentSignUps.contains(userId)) {
                            Toast.makeText(ViewEventDetailsActivity.this, "You are already signed up.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Add user to sign-ups
                        eventRef.update("signups", FieldValue.arrayUnion(userId))
                                .addOnSuccessListener(aVoid -> Toast.makeText(ViewEventDetailsActivity.this, "Signed up successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(ViewEventDetailsActivity.this, "Sign up failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        // Max attendees limit is set and reached/exceeded
                        Toast.makeText(ViewEventDetailsActivity.this, "Event is full.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ViewEventDetailsActivity.this, "Event data could not be loaded.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ViewEventDetailsActivity.this, "Event not found.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(ViewEventDetailsActivity.this, "Failed to fetch event details: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
                event = documentSnapshot.toObject(Event.class);
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
            tvEventDescription.setText(String.format("Event Description: %s", event.getDescription()));
            if (event.getMaxAttendees() != null) {
                tvMaxAttendees.setText(String.format("Max Attendees: %d", event.getMaxAttendees()));
                tvMaxAttendees.setVisibility(View.VISIBLE);
            } else {
                tvMaxAttendees.setVisibility(View.GONE);
            }
            Glide.with(this).load(event.getPosterUrl()).placeholder(R.drawable.image_placeholder_background).into(ivEventPoster);
            Glide.with(this).load(event.getCheckInQRCode()).placeholder(R.drawable.image_placeholder_background).into(ivCheckInQRCode);
            Glide.with(this).load(event.getPromotionQRCode()).placeholder(R.drawable.image_placeholder_background).into(ivPromotionQRCode);
        }
    }
    /**
     * Navigates to the DashboardActivity when the "Go to Dashboard" button is clicked.
     */
    private void goToDashboardActivity() {
        Intent intent = new Intent(ViewEventDetailsActivity.this, DashboardActivity.class);
        startActivity(intent);
    }
}
