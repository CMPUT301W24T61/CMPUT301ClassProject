package com.example.eventwiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


/**
 * Activity to display the details of a successfully created event.
 * @author Junkai
 */
public class EventCreationSuccessActivity extends AppCompatActivity {

    private TextView tvEventName, tvEventDate, tvEventStartTime, tvEventEndTime, tvEventLocation, tvMaxAttendees, tvEventDescription;
    private ImageView ivEventPoster, ivCheckInQRCode, ivPromotionQRCode;
    private Event event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation_success);

        event = (Event) getIntent().getSerializableExtra("event");

        initializeUI();
        loadEventDetails();
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
        tvEventDescription = findViewById(R.id.tvEventDescription);

        ImageButton btnGoToDashboard = findViewById(R.id.gotodasboard);
        btnGoToDashboard.setOnClickListener(v -> goToDashboardActivity());
    }

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

    private void goToDashboardActivity() {
        Intent intent = new Intent(EventCreationSuccessActivity.this, DashboardActivity.class);
        startActivity(intent);
    }
}

