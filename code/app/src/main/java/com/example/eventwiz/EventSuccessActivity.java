package com.example.eventwiz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EventSuccessActivity extends AppCompatActivity {

    private TextView tvEventName, tvEventDate, tvEventStartTime, tvEventEndTime, tvEventLocation;
    private ImageView ivEventPoster, ivCheckInQRCode, ivPromotionQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation_success);

        initializeUI();
        loadEventDetails();
    }

    private void initializeUI() {
        tvEventName = findViewById(R.id.tvEventName);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventStartTime = findViewById(R.id.tvEventStartTime);
        tvEventEndTime = findViewById(R.id.tvEventEndTime);
        tvEventLocation = findViewById(R.id.tvEventLocation);
        ivEventPoster = findViewById(R.id.ivEventPoster);
        ivCheckInQRCode = findViewById(R.id.ivCheckInQRCode);
        ivPromotionQRCode = findViewById(R.id.ivPromotionQRCode);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Event Details");
        }
    }

    private void loadEventDetails() {
        // Load passed data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvEventName.setText(extras.getString("eventName", "Event Name"));
            tvEventDate.setText("Date: " + extras.getString("eventDate", "TBD"));
            tvEventStartTime.setText("From: " + extras.getString("eventStartTime", "TBD"));
            tvEventEndTime.setText("To: " + extras.getString("eventEndTime", "TBD"));
            tvEventLocation.setText("Location: " + extras.getString("eventLocation", "TBD"));

            loadPosterImage(extras.getString("eventPosterUri"));
            loadQRCodeImage(ivCheckInQRCode, extras.getString("eventQrCodeFileName"));
            loadQRCodeImage(ivPromotionQRCode, extras.getString("promoQrCodeFileName"));
        }
    }

    private void loadPosterImage(String imageUri) {
        try {
            ivEventPoster.setImageURI(Uri.parse(imageUri));
        } catch (Exception e) {
            e.printStackTrace();
            ivEventPoster.setImageResource(R.drawable.image_placeholder_background);
        }
    }

    private void loadQRCodeImage(ImageView imageView, String fileName) {
        try {
            FileInputStream fis = openFileInput(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            imageView.setImageBitmap(bitmap);
            fis.close();
        } catch (FileNotFoundException e) {
            imageView.setImageResource(R.drawable.image_placeholder_background);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
