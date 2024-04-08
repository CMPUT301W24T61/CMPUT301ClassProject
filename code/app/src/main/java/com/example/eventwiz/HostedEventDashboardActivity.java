package com.example.eventwiz;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
    private static final String CHANNEL_ID = "attendance_notification_channel";
    private int notificationIdCounter = 0;

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
        viewAttendance.setOnClickListener(v -> openAttendeeOptionsDialog(eventId));
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

        ImageButton createNotification = findViewById(R.id.button_notifications);
        createNotification.setOnClickListener(v -> openNotificationActivity());
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

                        // Calculate attendance percentage
                        double attendancePercentage = (checkins * 100.0) / signups;

                        // Check if attendance percentage reaches 50% or 100% and show notification
                        if (attendancePercentage == 50.0 || attendancePercentage == 100.0) {
                            showAttendanceNotification(event.getId(), attendancePercentage);
                        }
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

    private void openAnnouncementDialog() {
        String eventId = getIntent().getStringExtra("eventId");
        AnnouncementDialogFragment dialogFragment = new AnnouncementDialogFragment();
        Bundle args = new Bundle();
        args.putString("eventId", eventId);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "AnnouncementDialogFragment");
    }

    private void openNotificationActivity() {
        String eventId = getIntent().getStringExtra("eventId");
        Intent intent = new Intent(HostedEventDashboardActivity.this, NotificationCreationActivity.class);
        intent.putExtra("eventId", eventId);
        startActivity(intent);
    }

    private void showAttendanceNotification(String eventId, double attendancePercentage) {
        String notificationTitle;
        String notificationMessage;

        if (attendancePercentage == 50.0) {
            notificationTitle = "50% Attendance Reached";
            notificationMessage = "Half of the attendees have checked in!";
        } else if (attendancePercentage == 100.0) {
            notificationTitle = "Full Attendance Reached";
            notificationMessage = "All attendees have checked in!";
        } else {
            // No need to show notification
            return;
        }

        createNotificationChannel();

        Intent intent = new Intent(this, HostedEventDashboardActivity.class);
        intent.putExtra("eventId", eventId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_new_notification)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(getNotificationId(), builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private int getNotificationId() {
        return notificationIdCounter++;
    }
}
