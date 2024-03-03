package com.example.eventwiz;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateEventActivity extends AppCompatActivity {

    private ImageView eventPosterImageView;
    private Button uploadButton, createEventButton;
    private CheckBox generateNewQRCodeCheckBox, reuseQRCodeCheckBox, generatePromotionQRCodeCheckBox;
    private Uri imageUri;

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
            try {
                handleQRCodeGeneration();
            } catch (IOException | WriterException e) {
                Toast.makeText(CreateEventActivity.this, "Error handling QR codes: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleQRCodeGeneration() throws WriterException, IOException {
        String eventQrCodeFileName = "event_qr_code.png";
        String promoQrCodeFileName = "promo_qr_code.png";

        Bitmap qrCodeBitmap = null;
        if (generateNewQRCodeCheckBox.isChecked()) {
            qrCodeBitmap = generateQRCode("Event-specific QR Code Data");
            saveQRCodeToFile(qrCodeBitmap, eventQrCodeFileName);
        } else if (reuseQRCodeCheckBox.isChecked() && !checkQRCodeFileExists(eventQrCodeFileName)) {
            Toast.makeText(this, "No existing QR code to reuse.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (generatePromotionQRCodeCheckBox.isChecked()) {
            Bitmap promoQrCodeBitmap = generateQRCode("Promotion-specific QR Code Data");
            saveQRCodeToFile(promoQrCodeBitmap, promoQrCodeFileName);
        }

        // Prepare data for next activity
        Intent intent = new Intent(CreateEventActivity.this, EventSuccessActivity.class);
        intent.putExtra("eventPosterUri", imageUri.toString());
        startActivity(intent);
    }

    private Bitmap generateQRCode(String data) throws WriterException {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        return barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 400, 400);
    }

    private void saveQRCodeToFile(Bitmap qrCodeBitmap, String fileName) throws IOException {
        FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
        qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.close();
    }

    private boolean checkQRCodeFileExists(String fileName) {
        File file = getFileStreamPath(fileName);
        return file.exists();
    }
}
