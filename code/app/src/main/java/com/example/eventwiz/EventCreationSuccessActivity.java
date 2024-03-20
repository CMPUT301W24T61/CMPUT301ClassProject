package com.example.eventwiz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Activity to display the details of a successfully created event.
 * This activity includes information about the event such as name, date, time, location,
 * maximum attendees, event poster, check-in QR code, and promotion QR code.
 *
 * This class extends AppCompatActivity and is used to handle the UI and navigation logic
 * for the event creation success screen.
 *
 * The event details are passed to this activity through an Intent with the key "event".
 *
 * @author Junkai
 */
public class EventCreationSuccessActivity extends AppCompatActivity {

    private TextView tvEventName, tvEventDate, tvEventStartTime, tvEventEndTime, tvEventLocation, tvMaxAttendees, tvEventDescription;
    private ImageView ivEventPoster, ivCheckInQRCode, ivPromotionQRCode;
    private Event event;

    /**
     * Called when the activity is starting. Responsible for initializing the UI,
     * retrieving the event details from the Intent, and loading the event details.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, this Bundle contains
     *                           the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation_success);

        event = (Event) getIntent().getSerializableExtra("event");

        initializeUI();
        loadEventDetails();

    }

    /**
     * Initializes the UI elements by finding and assigning views to their respective variables.
     */

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
        tvEventDescription = findViewById(R.id.tvEventDescription);

        ImageButton btnGoToDashboard = findViewById(R.id.gotodasboard);
        btnGoToDashboard.setOnClickListener(v -> goToDashboardActivity());
    }

    /**
     * Loads and displays the event details on the UI elements.
     * If the event has associated images (poster, QR codes), they are loaded using Glide.
     */
    private void loadEventDetails() {
        if (event != null) {
            tvEventName.setText(event.getName());
            tvEventDate.setText("Date: " + event.getDate());
            tvEventStartTime.setText(event.getStartTime());
            tvEventEndTime.setText(event.getEndTime());
            tvEventLocation.setText("Location: " + event.getLocation());
            tvMaxAttendees.setText("Max Attendees: " + event.getMaxAttendees());
            tvEventDescription.setText(("Event Description: " + event.getDescription()));

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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Navigates to the DashboardActivity when the "Go to Dashboard" button is clicked.
     */
    private void goToDashboardActivity() {
        Intent intent = new Intent(EventCreationSuccessActivity.this, DashboardActivity.class);
        startActivity(intent);
    }


}

