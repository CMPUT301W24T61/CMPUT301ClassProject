package com.example.eventwiz;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewHostedEventDetailsActivity extends AppCompatActivity {
    private TextView tvEventName, tvEventDate, tvEventStartTime, tvEventEndTime, tvEventLocation, tvMaxAttendees, tvEventDescription;
    private Button btnSignedUpList, btnCheckedInList;
    private ImageView ivEventPoster, ivCheckInQRCode, ivPromotionQRCode;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_hosted_event_details);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Event Details");
            int color = ContextCompat.getColor(this, R.color.turqoise);
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
        db = FirebaseFirestore.getInstance();
        initializeUI();
        String eventId = getIntent().getStringExtra("eventId");

        btnSignedUpList.setOnClickListener(v -> {
            if (eventId != null && !eventId.isEmpty()) {
                Intent intent = new Intent(ViewHostedEventDetailsActivity.this, AttendeeListActivity.class);
                intent.putExtra("eventId", eventId);
                startActivity(intent);
            } else {
                Toast.makeText(ViewHostedEventDetailsActivity.this, "Event ID is missing", Toast.LENGTH_SHORT).show();
            }
        });

        btnCheckedInList.setOnClickListener(v -> {
            if (eventId != null && !eventId.isEmpty()) {
                Intent intent = new Intent(ViewHostedEventDetailsActivity.this, CheckedInAttendeesActivity.class);
                intent.putExtra("eventId", eventId);
                startActivity(intent);
            } else {
                Toast.makeText(ViewHostedEventDetailsActivity.this, "Event ID is missing", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton btnGoToDashboard = findViewById(R.id.gotodasboard);
        btnGoToDashboard.setOnClickListener(v -> goToDashboardActivity());

        if (eventId != null && !eventId.isEmpty()) {
            loadEventFromFirestore(eventId);
        }
    }

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
        btnSignedUpList = findViewById(R.id.btnSignUpList);
        btnCheckedInList = findViewById(R.id.btnCheckInList);
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
        Intent intent = new Intent(ViewHostedEventDetailsActivity.this, DashboardActivity.class);
        startActivity(intent);
    }
}

