package com.example.eventwiz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity for creating an event by uploading event details and poster.
 * @author Junkai
 * @see Event
 */
public class CreateEventActivity extends AppCompatActivity {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Organizer organizer = new Organizer();
    private Uri imageUri = null;
    private Event event;
    private ImageView posterImageView;
    private CheckBox checkboxGenerateCheckInQR, checkboxGeneratePromotionQR, checkboxReuseQRCode;
    private Spinner qrCodeSpinner;
    private ArrayAdapter<String> qrCodeAdapter;
    private List<String> qrCodeUrls;

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                imageUri = result;
                posterImageView.setImageURI(imageUri);
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add_info);


        checkboxGenerateCheckInQR = findViewById(R.id.generateNewQRCode);
        checkboxGeneratePromotionQR = findViewById(R.id.generatePromotionQRCode);
        checkboxReuseQRCode = findViewById(R.id.reuseQRCode);
        qrCodeSpinner = findViewById(R.id.qrCodeSpinner);
        qrCodeUrls = new ArrayList<>();
        qrCodeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, qrCodeUrls);
        qrCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qrCodeSpinner.setAdapter(qrCodeAdapter);

        posterImageView = findViewById(R.id.poster);
        Button uploadButton = findViewById(R.id.uploadButton);
        Button createEventButton = findViewById(R.id.createEventButton);
        ImageButton backButton = findViewById(R.id.BackArrow);
        backButton.setOnClickListener(view -> onBackPressed());

        uploadButton.setOnClickListener(v -> mGetContent.launch("image/*"));

        createEventButton.setOnClickListener(v -> {
            if (imageUri == null) {
                Toast.makeText(CreateEventActivity.this, "Please upload an event poster", Toast.LENGTH_SHORT).show();
            } else {
                generateAndUploadData();
            }
        });

        checkboxReuseQRCode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            qrCodeSpinner.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            if (isChecked) {
                // Fetch and populate QR codes here
                fetchAndPopulateQRCodes();
            }
        });
    }
    private void fetchAndPopulateQRCodes() {
        FirebaseFirestore.getInstance().collection("events").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<QRCodeEventDetail> qrCodeDetails = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String checkInQRCodeUrl = documentSnapshot.getString("checkInQRCode");
                        String promotionQRCodeUrl = documentSnapshot.getString("promotionQRCode");
                        String eventName = documentSnapshot.getString("name");
                        String eventPosterUrl = documentSnapshot.getString("posterUrl");

                        // Create a new QRCodeEventDetail object with both QR code URLs
                        QRCodeEventDetail qrCodeEventDetail = new QRCodeEventDetail(checkInQRCodeUrl, promotionQRCodeUrl, eventName, eventPosterUrl);
                        qrCodeDetails.add(qrCodeEventDetail);
                    }

                    // Update the adapter with new list of QRCodeEventDetail objects
                    QRCodeEventAdapter adapter = new QRCodeEventAdapter(CreateEventActivity.this, qrCodeDetails);
                    qrCodeSpinner.setAdapter(adapter);
                })
                .addOnFailureListener(e -> Log.e("CreateEventActivity", "Error fetching QR codes", e));
    }

    /**
     * Generates QR codes and uploads event data to Firestore.
     */
    private void generateAndUploadData() {
        // Ensure at least one QR code generation option is selected
        if (!checkboxGenerateCheckInQR.isChecked() && !checkboxGeneratePromotionQR.isChecked() && !checkboxReuseQRCode.isChecked()) {
            Toast.makeText(CreateEventActivity.this, "At least one QR code option is required.", Toast.LENGTH_LONG).show();
            return; // Exit if no option is selected
        }

        event = (Event) getIntent().getSerializableExtra("event");
        if (checkboxGenerateCheckInQR.isChecked()) {
            try {
                String uniqueString = organizer.generateUniqueString();
                String hashedString = organizer.hashString(uniqueString);
                Bitmap checkInQRCodeBitmap = organizer.generateCheckInQRCode(hashedString);
                String checkInQRFileName = "checkInQRCode_" + event.getId() + ".png";
                uploadBitmapAndGetUrl(checkInQRCodeBitmap, checkInQRFileName, checkInQRUrl -> {
                    event.setCheckInQRCode(checkInQRUrl);
                    event.setHashCode(hashedString);
                    completeEventCreation();
                });
            } catch (WriterException e) {
                Toast.makeText(this, "Check-In QR Code generation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (checkboxReuseQRCode.isChecked()) {
            QRCodeEventDetail selectedDetail = (QRCodeEventDetail) qrCodeSpinner.getSelectedItem();
            if (selectedDetail != null) {
                event.setCheckInQRCode(selectedDetail.getCheckInQRCodeUrl());
                completeEventCreation();
            } else {
                Toast.makeText(this, "No QR code selected.", Toast.LENGTH_SHORT).show();
            }
        }

        if (checkboxGeneratePromotionQR.isChecked()) {
            try {
                String uniqueString = organizer.generateUniqueString();
                String hashedString = organizer.hashString(uniqueString);
                Bitmap promotionQRCodeBitmap = organizer.generateCheckInQRCode(hashedString);
                String promotionQRFileName = "promotionQRCode_" + event.getId() + ".png";
                uploadBitmapAndGetUrl(promotionQRCodeBitmap, promotionQRFileName, promotionQRUrl -> {
                    event.setPromotionQRCode(promotionQRUrl);
                    event.setPromotionHashCode(hashedString);
                    completeEventCreation();
                });
            } catch (WriterException e) {
                Toast.makeText(this, "Promotion QR Code generation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void completeEventCreation() {
        // Ensure at least one QR code URL is set if required
        if ((checkboxGenerateCheckInQR.isChecked() || checkboxReuseQRCode.isChecked() || checkboxGeneratePromotionQR.isChecked()) &&
                (event.getCheckInQRCode() != null || event.getPromotionQRCode() != null)) {
            uploadPosterAndGetUrl(imageUri, posterUrl -> {
                event.setPosterUrl(posterUrl);
                saveEventToFirestore(event);
            });
        } else if (!checkboxGenerateCheckInQR.isChecked() && !checkboxGeneratePromotionQR.isChecked() && !checkboxReuseQRCode.isChecked()) {
            // Handle the case where no QR code options are selected but event creation is triggered
            Toast.makeText(this, "No QR code option selected.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Uploads a bitmap image to Firebase Storage and retrieves its download URL.
     *
     * @param bitmap   The bitmap image to upload.
     * @param fileName The file name for the uploaded image.
     * @param listener Listener to handle the upload completion and retrieve the URL.
     */
    private void uploadBitmapAndGetUrl(Bitmap bitmap, String fileName, OnUploadCompleteListener listener) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference ref = storage.getReference().child("qr_codes/" + fileName);
        UploadTask uploadTask = ref.putBytes(data);

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return ref.getDownloadUrl();
        }).addOnSuccessListener(uri -> {
            listener.onUploadComplete(uri.toString());
        }).addOnFailureListener(e -> {
            Toast.makeText(CreateEventActivity.this, "Upload failed for " + fileName, Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Uploads the event poster image to Firebase Storage and retrieves its download URL.
     *
     * @param posterUri The URI of the poster image.
     * @param listener  Listener to handle the upload completion and retrieve the URL.
     */
    private void uploadPosterAndGetUrl(Uri posterUri, OnUploadCompleteListener listener) {
        final StorageReference posterRef = storage.getReference().child("event_posters/" + System.currentTimeMillis() + "_poster.jpg");
        posterRef.putFile(posterUri).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return posterRef.getDownloadUrl();
        }).addOnSuccessListener(uri -> {
            listener.onUploadComplete(uri.toString());
        }).addOnFailureListener(e -> {
            Toast.makeText(CreateEventActivity.this, "Poster upload failed.", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Saves the event to Firestore.
     *
     * @param event The event to save.
     */
    private void saveEventToFirestore(Event event) {
        db.collection("events").document(event.getId()).set(event).addOnSuccessListener(documentReference -> {
            Toast.makeText(CreateEventActivity.this, "Event created successfully.", Toast.LENGTH_SHORT).show();
            navigateToSuccessActivity(event);
        }).addOnFailureListener(e -> {
            Toast.makeText(CreateEventActivity.this, "Error saving event.", Toast.LENGTH_SHORT).show();
        });
    }

    interface OnUploadCompleteListener {
        void onUploadComplete(String url);
    }
    /**
     * Navigates to selected event
     * @param event
     */
    private void navigateToSuccessActivity(Event event) {
        Log.d("CreateEventActivity", "Navigating to Success Activity with event: " + event);
        Intent intent = new Intent(CreateEventActivity.this, EventCreationSuccessActivity.class);
        intent.putExtra("eventId", event.getId());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}