package com.example.eventwiz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OrganizerService organizerService;


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

        initializeUI();


        // Retrieve the event ID passed from the previous activity
        String eventId = getIntent().getStringExtra("eventId");
        if (eventId != null) {
            fetchEventDetails(eventId);
        } else {
            Log.e("EventCreationSuccess", "Event ID is null.");
            // Handle the case when eventId is null
        }
    }

    private void fetchEventDetails(String eventId) {
        DocumentReference eventDocument = db.collection("events").document(eventId);
        eventDocument.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Event event = documentSnapshot.toObject(Event.class);
                loadEventDetails(event);
                organizerService.loadEventDetails(event, ivEventPoster, ivCheckInQRCode, ivPromotionQRCode,
                        tvEventName, tvEventDate, tvEventStartTime, tvEventEndTime, tvEventLocation, tvMaxAttendees);
            }
        });
    }
    /**
     * Initializes the UI elements by finding and assigning views to their respective variables.
     */

    private void initializeUI() {
        tvEventName = findViewById(R.id.tvEventName);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventStartTime = findViewById(R.id.tvEventStartTime);
        tvEventEndTime = findViewById(R.id.tvEventEndTime);
        tvEventLocation = findViewById(R.id.tvEventLocation);
        tvMaxAttendees = findViewById(R.id.tvMaxAttendees);
        tvEventDescription = findViewById(R.id.tvEventDescription);
        ivEventPoster = findViewById(R.id.ivEventPoster);
        ivCheckInQRCode = findViewById(R.id.ivCheckInQRCode);
        ivPromotionQRCode = findViewById(R.id.ivPromotionQRCode);

        ImageButton btnGoToDashboard = findViewById(R.id.gotodasboard);
        btnGoToDashboard.setOnClickListener(v -> goToDashboardActivity());

        //adding share functionlity
        ivCheckInQRCode.setOnClickListener(v -> shareImage(ivCheckInQRCode));
        ivPromotionQRCode.setOnClickListener(v -> shareImage(ivPromotionQRCode));
        organizerService = new OrganizerService(this);
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
        tvEventDate.setText(String.format("Date: %s", event.getDate()));
        tvEventStartTime.setText(String.format("Start Time: %s", event.getStartTime()));
        tvEventEndTime.setText(String.format("End Time: %s", event.getEndTime()));
        tvEventLocation.setText(String.format("Location: %s", event.getLocation()));
        tvMaxAttendees.setText(String.format("Max Attendees: %d", event.getMaxAttendees()));
        tvEventDescription.setText(String.format("Event Description: %s", event.getDescription()));

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
        } catch (Exception e) {
            e.printStackTrace();

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

