package com.example.eventwiz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
 * The event details are passed to this activity through an Intent with the key "eventId".
 *
 * @author Junkai
 */
public class EventCreationSuccessActivity extends AppCompatActivity {

    private TextView tvEventName, tvEventDate, tvEventStartTime, tvEventEndTime, tvEventLocation, tvMaxAttendees, tvEventDescription;
    private ImageView ivEventPoster, ivCheckInQRCode, ivPromotionQRCode;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        ivCheckInQRCode = findViewById(R.id.ivCheckInQRCode);
        ivPromotionQRCode = findViewById(R.id.ivPromotionQRCode);
        initializeUI();

        // Retrieve the event ID passed from the previous activity
        String eventId = getIntent().getStringExtra("eventId");
        if (eventId != null) {
            fetchEventDetails(eventId);
        } else {
            Log.e("EventCreationSuccess", "Event ID is null.");

        }
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
                Log.d("EventCreationSuccess", "Displaying Check-In QR Code: " + imageUrl);
                EnlargeImageFragment enlargeImageFragment = EnlargeImageFragment.newInstance(imageUrl);
                enlargeImageFragment.show(getSupportFragmentManager(), "enlarge_check_in_qr");
            } else {
                Log.e("EventCreationSuccess", "Check-In QR Code URL is null or empty.");
            }
        });

        ivPromotionQRCode.setOnClickListener(view -> {
            if (event != null && event.getPromotionQRCode() != null && !event.getPromotionQRCode().isEmpty()) {
                String imageUrl = event.getPromotionQRCode();
                Log.d("EventCreationSuccess", "Displaying Promotion QR Code: " + imageUrl);
                EnlargeImageFragment enlargeImageFragment = EnlargeImageFragment.newInstance(imageUrl);
                enlargeImageFragment.show(getSupportFragmentManager(), "enlarge_promotion_qr");
            } else {
                Log.e("EventCreationSuccess", "Promotion QR Code URL is null or empty.");
            }
        });
    }

    private void fetchEventDetails(String eventId) {
        DocumentReference eventDocument = db.collection("events").document(eventId);
        eventDocument.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {

                Event event = documentSnapshot.toObject(Event.class);
                if (event != null) {
                    loadEventDetails(event);
                } else {
                    Log.e("EventCreationSuccess", "Event object is null.");
                }
            } else {
                Log.e("EventCreationSuccess", "Event document does not exist.");

                event = documentSnapshot.toObject(Event.class);
                loadEventDetails(event);

            }
        }).addOnFailureListener(e -> {
            Log.e("EventCreationSuccess", "Error fetching event details", e);
        });
    }


    /**
     * Initializes the UI elements by finding and assigning views to their respective variables.
     */
    private void initializeUI() {
        tvEventName = findViewById(R.id.tv_event_name);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventStartTime = findViewById(R.id.tvEventStartTime);
        tvEventEndTime = findViewById(R.id.tvEventEndTime);
        tvEventLocation = findViewById(R.id.tvEventLocation);
        tvMaxAttendees = findViewById(R.id.tvMaxAttendees);
        tvEventDescription = findViewById(R.id.tvEventDescription);
        ivEventPoster = findViewById(R.id.iv_event_poster);
        ivCheckInQRCode = findViewById(R.id.ivCheckInQRCode);
        ivPromotionQRCode = findViewById(R.id.ivPromotionQRCode);

        ImageButton btnGoToDashboard = findViewById(R.id.goback);
        btnGoToDashboard.setOnClickListener(v -> goToDashboardActivity());

        //adding share functionality
        ivCheckInQRCode.setOnClickListener(v -> shareImage(ivCheckInQRCode));
        ivPromotionQRCode.setOnClickListener(v -> shareImage(ivPromotionQRCode));
    }

    /**
     * Loads and displays the event details on the UI elements.
     * If the event has associated images (poster, QR codes), they are loaded using Glide.
     */
    private void loadEventDetails(Event event) {
        if (event == null) {
            Log.e("EventCreationSuccess", "Event object is null.");
            return;
        }
        tvEventName.setText(event.getName());
        tvEventDate.setText(String.format("On: %s", event.getDate()));
        tvEventStartTime.setText(String.format("From: %s", event.getStartTime()));
        tvEventEndTime.setText(String.format("To: %s", event.getEndTime()));
        tvEventLocation.setText(String.format("Location: %s", event.getLocation()));


        // Display description if not null
        if (event.getDescription() != null && !event.getDescription().isEmpty()) {
            tvEventDescription.setVisibility(View.VISIBLE);
            tvEventDescription.setText(String.format("Description: %s", event.getDescription()));
        } else {
            tvEventDescription.setVisibility(View.GONE);
        }

        // Display max attendees if not equal to Integer.MAX_VALUE
        if (event.getMaxAttendees() != null && event.getMaxAttendees() != Integer.MAX_VALUE) {
            tvMaxAttendees.setVisibility(View.VISIBLE);
            tvMaxAttendees.setText(String.format("Attendee Limit: %d", event.getMaxAttendees()));
        } else {
            tvMaxAttendees.setText(String.format("Unlimited Attendance"));
        }


        if (event.getPosterUrl() != null && !event.getPosterUrl().isEmpty()) {
            Glide.with(this).load(event.getPosterUrl()).into(ivEventPoster);
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

    //shares qr codes to other apps.
    private void shareImage(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageView.getDrawingCache();
        try {
            File file = new File(getExternalCacheDir(), "shared_image.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            imageView.setDrawingCacheEnabled(false); // Disable drawing cache after usage

            Uri contentUri = FileProvider.getUriForFile(this, "com.example.eventwiz.fileprovider", file);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share image via"));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error sharing image", Toast.LENGTH_SHORT).show();
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