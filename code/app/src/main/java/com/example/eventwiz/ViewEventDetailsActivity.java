package com.example.eventwiz;

import static com.example.eventwiz.AttendeeService.db;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.List;

/**
 * ViewEventDetailsActivity displays the details of a specific event.
 * It retrieves event details from Firestore and populates the UI accordingly.
 */
public class ViewEventDetailsActivity extends AppCompatActivity {

    private TextView tvEventName, tvEventDate, tvEventStartTime, tvEventEndTime, tvEventLocation, tvMaxAttendees, tvEventDescription;
    private ImageButton btnSignUp, btnCheckIn;
    private ImageView ivEventPoster, ivCheckInQRCode, ivPromotionQRCode;


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

        initializeUI();
        String eventId = getIntent().getStringExtra("eventId");
        if (eventId != null && !eventId.isEmpty()) {
            loadEventFromFirestore(eventId);
        }
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpForEvent();
            }
        });
        ImageButton btnGoBack = findViewById(R.id.goback);
        btnGoBack.setOnClickListener(v -> finish());



        ImageButton btnMap = findViewById(R.id.btnMapService);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAttendeeMapService();
            }
        });

        ImageButton btnAnnounce = findViewById(R.id.btnAnnouncements);
        btnAnnounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAnnouncementList();
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
        // Fetch the event document to check current sign-ups and max attendees
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
        tvEventName = findViewById(R.id.tv_event_name);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventLocation = findViewById(R.id.tvEventLocation);
        tvMaxAttendees = findViewById(R.id.tvMaxAttendees);
        ivCheckInQRCode = findViewById(R.id.ivCheckInQRCode);
        ivEventPoster =findViewById(R.id.iv_event_poster);
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
        AttendeeService.loadEventDetails(eventId, new AttendeeService.EventDetailsListener() {
            @Override
            public void onEventDetailsLoaded(Event event) {
                loadEventDetails(event);
            }

            @Override
            public void onEventDetailsError() {
                // Handle the error case, perhaps show a toast or log
                Toast.makeText(ViewEventDetailsActivity.this, "Error loading event details", Toast.LENGTH_SHORT).show();
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
            tvEventDate.setText(String.format("On: %s", event.getDate()));
            tvEventStartTime.setText(String.format("From: %s", event.getStartTime()));
            tvEventEndTime.setText(String.format("To: %s", event.getEndTime()));
            tvEventLocation.setText(String.format("Venue: %s", event.getLocation()));

            // Check if description is not null before setting it
            if (event.getDescription() != null) {
                tvEventDescription.setText(String.format("%s", event.getDescription()));
                tvEventDescription.setVisibility(View.VISIBLE); // Make TextView visible
            } else {
                tvEventDescription.setVisibility(View.GONE); // Hide TextView if description is null
            }

            // Check if max attendees is not equal to maximum value before setting it
            if (event != null) {
                Integer maxAttendees = event.getMaxAttendees();
                if (maxAttendees != null && maxAttendees != Integer.MAX_VALUE) {
                    tvMaxAttendees.setText(String.format("Attendee Limit: %d", maxAttendees));
                    tvMaxAttendees.setVisibility(View.VISIBLE); // Make TextView visible
                } else {
                    tvMaxAttendees.setText("Unlimited Attendance"); // Hide TextView if max attendees is equal to maximum value
                }
            } else {
                // Handle the case where 'event' is null, for example, display an error message or log a warning.
                Log.e("ViewEventDetailsActivity","Event is null");
            }


            Glide.with(this).load(event.getPosterUrl()).placeholder(R.drawable.image_placeholder_background).into(ivEventPoster);
            Glide.with(this).load(event.getCheckInQRCode()).placeholder(R.drawable.image_placeholder_background).into(ivCheckInQRCode);
            Glide.with(this).load(event.getPromotionQRCode()).placeholder(R.drawable.image_placeholder_background).into(ivPromotionQRCode);
        }
    }

    // Method to navigate to MapService activity
    private void goToAttendeeMapService() {
        String eventId = getIntent().getStringExtra("eventId");
        if (eventId != null && !eventId.isEmpty()) {
            Intent intent = new Intent(ViewEventDetailsActivity.this, AttendeeMapService.class);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
        } else {
            Toast.makeText(ViewEventDetailsActivity.this, "Invalid event ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToAnnouncementList() {
        String eventId = getIntent().getStringExtra("eventId");
        if (eventId != null && !eventId.isEmpty()) {
            Intent intent = new Intent(ViewEventDetailsActivity.this, AnnouncementsListActivity.class);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
        } else {
            Toast.makeText(ViewEventDetailsActivity.this, "Invalid event ID", Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment announcementDialogFragment = fragmentManager.findFragmentByTag("AnnouncementDialogFragment");
        if (announcementDialogFragment != null && announcementDialogFragment instanceof AnnouncementDialogFragment) {
            ((AnnouncementDialogFragment) announcementDialogFragment).dismiss();
        } else {
            super.onBackPressed();
        }
    }


}
