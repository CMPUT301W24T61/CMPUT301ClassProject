package com.example.eventwiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;

import java.io.IOException;

public class CreateEventActivity extends AppCompatActivity {

    private ImageView eventPosterImageView;
    private Button uploadButton, createEventButton;
    private CheckBox generateNewQRCodeCheckBox, reuseQRCodeCheckBox, generatePromotionQRCodeCheckBox;
    private Uri imageUri;
    private Event event;
    private Organizer organizer; // Assume Organizer is passed or instantiated elsewhere

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        eventPosterImageView.setImageURI(uri);
                        imageUri = uri;
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add_info);

        organizer = new Organizer("Default Organizer", this);
        event = (Event) getIntent().getSerializableExtra("event");

        initializeUI();
        setupActionBar();
        setupButtonListeners();
    }

    private void initializeUI() {
        eventPosterImageView = findViewById(R.id.poster);
        uploadButton = findViewById(R.id.uploadButton);
        createEventButton = findViewById(R.id.createEventButton);
        generateNewQRCodeCheckBox = findViewById(R.id.generateNewQRCode);
        reuseQRCodeCheckBox = findViewById(R.id.reuseQRCode);
        generatePromotionQRCodeCheckBox = findViewById(R.id.generatePromotionQRCode);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Create Event");
        }
    }

    private void setupButtonListeners() {
        uploadButton.setOnClickListener(v -> mGetContent.launch("image/*"));

        createEventButton.setOnClickListener(v -> {
            if (imageUri == null) {
                Toast.makeText(this, "Please upload an event poster", Toast.LENGTH_SHORT).show();
                return;
            }
            event.setPoster(imageUri.toString());

            // Adjust based on Organizer's capability to generate QR codes
            if (generateNewQRCodeCheckBox.isChecked()) {
                // Assuming generateCheckInQRCode now directly updates the event or returns a path/URL
                String checkInQRCodePath = null; // This method needs to be implemented accordingly
                try {
                    checkInQRCodePath = organizer.generateCheckInQRCode(event);
                } catch (WriterException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                event.setCheckInQRCode(checkInQRCodePath); // This assumes setCheckInQRCode accepts a String path or URL
            }

            if (generatePromotionQRCodeCheckBox.isChecked()) {
                // Similarly for promotion QR codes
                String promotionQRCodePath = null; // Implement this method to return a path/URL
                try {
                    promotionQRCodePath = organizer.generatePromotionQRCode(event);
                } catch (WriterException | IOException e) {
                    throw new RuntimeException(e);
                }
                event.setPromotionQRCode(promotionQRCodePath); // Assumes setPromotionQRCode accepts a String path or URL
            }

            Intent intent = new Intent(CreateEventActivity.this, EventSuccessActivity.class);
            intent.putExtra("event", event);
            startActivity(intent);
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
