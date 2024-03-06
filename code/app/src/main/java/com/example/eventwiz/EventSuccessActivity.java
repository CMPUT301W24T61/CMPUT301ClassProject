package com.example.eventwiz;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;


public class EventSuccessActivity extends AppCompatActivity {

    private TextView tvEventName, tvEventDate, tvEventStartTime, tvEventEndTime, tvEventLocation, tvMaxAttendees;
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

