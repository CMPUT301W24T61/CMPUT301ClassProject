package com.example.eventwiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import android.widget.Button;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewEventDetailsActivity extends AppCompatActivity {

    private TextView tvEventName, tvEventDate, tvEventStartTime, tvEventEndTime, tvEventLocation, tvMaxAttendees;
    private ImageView ivEventPoster, ivCheckInQRCode, ivPromotionQRCode;
    private Event event;

    private FirebaseFirestore db;
    private DocumentReference eventDocument;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_event_details);

        // Insitantiate top support action bar
        ActionBar actionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Event Details");
            int color = ContextCompat.getColor(this, R.color.turqoise);

            // Set the background color of the ActionBar
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }

        ImageButton btnGoToDashboard = findViewById(R.id.gotodasboard);
        btnGoToDashboard.setOnClickListener(v -> goToDashboardActivity());


        event = (Event) getIntent().getSerializableExtra("event");

        String eventId = getIntent().getStringExtra("eventId");
        if (eventId != null) {
            // Get a reference to the event document
            eventDocument = db.collection("events").document(eventId);

            // Attach a listener to the document
            eventDocument.addSnapshotListener((documentSnapshot, e) -> {
                if (e != null) {
                    // Handle errors
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Convert document to Event object
                    event = documentSnapshot.toObject(Event.class);
                    initializeUI();
                    loadEventDetails();
                }
            });



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
        tvEventStartTime = findViewById((R.id.tvEventStartTime));
        tvEventEndTime = findViewById((R.id.tvEventEndTime));
    }

    private void loadEventDetails() {
        if (event != null) {
            tvEventName.setText(event.getName());
            tvEventDate.setText("Date: " + event.getDate());
            tvEventStartTime.setText(event.getStartTime());
            tvEventEndTime.setText(event.getEndTime());
            tvEventLocation.setText("Location: " + event.getLocation());
            tvMaxAttendees.setText("Max Attendees: " + event.getMaxAttendees());

            if (event.getPosterUrl() != null && !event.getPosterUrl().isEmpty()) {
                Glide.with(this).load(event.getPosterUrl()).into(ivEventPoster);
            } else {
                ivEventPoster.setImageResource(R.drawable.image_placeholder_background);
            }
            if (event.getCheckInQRCode() != null && !event.getCheckInQRCode().isEmpty()) {
                Glide.with(this).load(event.getCheckInQRCode()).into(ivCheckInQRCode);
            } else {
                ivCheckInQRCode.setVisibility(View.GONE);
            }

            if (event.getPromotionQRCode() != null && !event.getPromotionQRCode().isEmpty()) {
                Glide.with(this).load(event.getPromotionQRCode()).into(ivPromotionQRCode);
            } else {
                ivPromotionQRCode.setVisibility(View.GONE);
            }
        }
    }

    private void goToDashboardActivity() {
        Intent intent = new Intent(ViewEventDetailsActivity.this, DashboardActivity.class);
        // You may need to adjust the class (DashboardActivity) based on your actual dashboard activity class
        startActivity(intent);
    }

}
