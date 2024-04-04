package com.example.eventwiz;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HostedEventDashboardActivity extends AppCompatActivity {

    private TextView tvEventID, tvEventName, tvEventDate, tvEventStartTime, tvEventEndTime, tvEventLocation, tvMaxAttendees, tvCountdownTimer;
    private ImageView ivEventPoster;
    private FirebaseFirestore db;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_dashboard);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Event Details");
            int color = ContextCompat.getColor(this, R.color.turqoise);
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }

        db = FirebaseFirestore.getInstance();
        initializeUI();
        String eventId = getIntent().getStringExtra("eventId");

        ImageButton btnGoToDashboard = findViewById(R.id.goback);
        btnGoToDashboard.setOnClickListener(v -> goToBrowseActivity());
        TooltipCompat.setTooltipText(btnGoToDashboard, "Go Back");

        if (eventId != null && !eventId.isEmpty()) {
            loadEventFromFirestore(eventId);
        }

        ImageButton viewAttendance = findViewById(R.id.button_attendance);
        viewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAttendeeOptionsDialog(eventId);
            }
        });
        TooltipCompat.setTooltipText(viewAttendance, "View Attendance");

        ImageButton viewQRcodes = findViewById(R.id.button_qr_codes);
        viewQRcodes.setOnClickListener(v -> goToViewQRActivity(eventId));
        TooltipCompat.setTooltipText(viewQRcodes, "View QR Codes");

        ImageButton viewCheckInMap = findViewById(R.id.button_map);
        viewCheckInMap.setOnClickListener(v -> goToCheckinMapActivity());
        TooltipCompat.setTooltipText(viewQRcodes, "View Check-In Map");

        ImageButton createAnnouncement = findViewById(R.id.button_announce);
        createAnnouncement.setOnClickListener(v -> openAnnouncementDialog());
        TooltipCompat.setTooltipText(viewQRcodes, "Create Announcement");


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void initializeUI() {
        tvEventID = findViewById(R.id.tv_event_id);
        tvEventName = findViewById(R.id.tv_event_name);
        tvEventDate = findViewById(R.id.tv_event_date);
        tvEventLocation = findViewById(R.id.tv_location);
        tvMaxAttendees = findViewById(R.id.tv_attendance);
        ivEventPoster = findViewById(R.id.iv_event_poster);
        tvEventStartTime = findViewById(R.id.tv_start_time);
        tvEventEndTime = findViewById(R.id.tv_end_time);
        tvCountdownTimer = findViewById(R.id.tv_event_status);
    }

    private void loadEventFromFirestore(String eventId) {
        DocumentReference eventDocument = db.collection("events").document(eventId);
        eventDocument.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Event event = documentSnapshot.toObject(Event.class);
                loadEventDetails(event);
                assert event != null;
                startCountdownTimer(event);
            }
        });
    }

    private void loadEventDetails(Event event) {
        if (event != null) {
            tvEventID.setText(String.format("ID: %s", event.getId()));
            tvEventName.setText(String.format("Event: %s", event.getName()));
            tvEventDate.setText(String.format("On: %s", event.getDate()));
            tvEventStartTime.setText(String.format("From: %s", event.getStartTime()));
            tvEventEndTime.setText(String.format("To: %s", event.getEndTime()));
            tvEventLocation.setText(String.format("Location: %s", event.getLocation()));

            // Fetch real-time attendance from Firestore
            DocumentReference eventDocument = db.collection("events").document(event.getId());
            eventDocument.addSnapshotListener((documentSnapshot, e) -> {
                if (e != null) {
                    // Handle errors
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Event updatedEvent = documentSnapshot.toObject(Event.class);
                    if (updatedEvent != null) {
                        long checkins = updatedEvent.getCheckInsCount().size(); // Fetch number of check-ins
                        long signups = updatedEvent.getSignups().size(); // Fetch number of sign-ups

                        tvMaxAttendees.setText(String.format("%d check-ins out of %d sign-ups", checkins, signups));
                    }
                }
            });

            Glide.with(this).load(event.getPosterUrl()).placeholder(R.drawable.image_placeholder_background).into(ivEventPoster);
        }
    }

    private void startCountdownTimer(Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMMM-yyyy hh:mm a", Locale.getDefault());

        if (event != null && event.getDate() != null && event.getStartTime() != null) {
            LocalDateTime eventDateTime = LocalDateTime.parse(event.getDate() + " " + event.getStartTime(), formatter);
            LocalDateTime currentDateTime = LocalDateTime.now();

            long eventSeconds = eventDateTime.toEpochSecond(java.time.ZoneOffset.UTC);
            long currentSeconds = currentDateTime.toEpochSecond(java.time.ZoneOffset.UTC);

            long differenceSeconds = eventSeconds - currentSeconds;

            if (differenceSeconds > 0) {
                countDownTimer = new CountDownTimer(differenceSeconds * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long days = millisUntilFinished / (1000 * 60 * 60 * 24);
                        long hours = (millisUntilFinished % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                        long minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60);
                        long seconds = (millisUntilFinished % (1000 * 60)) / 1000;

                        String countdown = String.format(Locale.getDefault(), "         Your Event Starts in:\n%02d days, %02d hrs, %02d mins, %02d sec", days, hours, minutes, seconds);
                        tvCountdownTimer.setText(countdown);
                    }

                    @Override
                    public void onFinish() {
                        tvCountdownTimer.setText("Event Underway...");
                    }
                }.start();
            } else {
                tvCountdownTimer.setText("This Event has Ended");
            }
        } else {
            tvCountdownTimer.setText("Invalid Date-Time");
            // Handle the case where event or its date-time components are null
        }
    }


    private void goToBrowseActivity() {
        Intent intent = new Intent(HostedEventDashboardActivity.this, BrowseHostedEvents.class);
        startActivity(intent);
    }

    private void openAttendeeOptionsDialog(String eventId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AttendeeOptionsDialogFragment dialogFragment = new AttendeeOptionsDialogFragment(eventId);
        dialogFragment.show(fragmentManager, "AttendeeOptionsDialogFragment");
    }

    private void goToViewQRActivity(String eventId) {
        Intent intent = new Intent(HostedEventDashboardActivity.this, ViewShareQRCodeActivity.class);
        intent.putExtra("eventId", eventId);
        startActivity(intent);
    }

    private void goToCheckinMapActivity() {
        String eventId = getIntent().getStringExtra("eventId");
        if (eventId != null && !eventId.isEmpty()) {
            Intent intent = new Intent(HostedEventDashboardActivity.this, OrganizerMapService.class);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
        } else {
            Toast.makeText(HostedEventDashboardActivity.this, "Invalid event ID", Toast.LENGTH_SHORT).show();
        }
    }

    // Code to open announcement dialog
    // Code to open announcement dialog
    private void openAnnouncementDialog() {
        // Retrieve the event ID from the activity's intent
        String eventId = getIntent().getStringExtra("eventId");

        // Create a new instance of the AnnouncementDialogFragment
        AnnouncementDialogFragment dialogFragment = new AnnouncementDialogFragment();

        // Pass the event ID to the dialog fragment using arguments
        Bundle args = new Bundle();
        args.putString("eventId", eventId); // Pass event ID to the dialog
        dialogFragment.setArguments(args);

        // Show the dialog fragment
        dialogFragment.show(getSupportFragmentManager(), "AnnouncementDialogFragment");
    }

}
