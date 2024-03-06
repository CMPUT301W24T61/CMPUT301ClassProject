package com.example.eventwiz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;

public class CreateEventActivity extends AppCompatActivity {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Organizer organizer = new Organizer();
    private Uri imageUri = null;
    private Event event;
    private ImageView posterImageView;

    // ActivityResultLauncher for the user to pick an image from the gallery
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                imageUri = result;
                posterImageView.setImageURI(imageUri); // Assuming you have an ImageView to display the selected image
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add_info); // Adjust this layout file name as necessary

        posterImageView = findViewById(R.id.poster);
        Button uploadButton = findViewById(R.id.uploadButton);
        Button createEventButton = findViewById(R.id.createEventButton);

        uploadButton.setOnClickListener(v -> mGetContent.launch("image/*"));

        createEventButton.setOnClickListener(v -> {
            if (imageUri == null) {
                Toast.makeText(CreateEventActivity.this, "Please upload an event poster", Toast.LENGTH_SHORT).show();
            } else {
                generateAndUploadData();
            }
        });
    }

    private void generateAndUploadData() {
        try {
            Bitmap checkInQRCodeBitmap = organizer.generateCheckInQRCode("Sample Check-In Data");
            Bitmap promotionQRCodeBitmap = organizer.generatePromotionQRCode("Sample Promotion Data");

            uploadBitmapAndGetUrl(checkInQRCodeBitmap, "checkInQRCode.png", checkInQRUrl -> {
                uploadBitmapAndGetUrl(promotionQRCodeBitmap, "promotionQRCode.png", promotionQRUrl -> {
                    uploadPosterAndGetUrl(imageUri, posterUrl -> {
                        event = (Event) getIntent().getSerializableExtra("event");
                        // Set QR code URLs and poster URL
                        event.setCheckInQRCode(checkInQRUrl);
                        event.setPromotionQRCode(promotionQRUrl);
                        event.setPosterUrl(posterUrl);

                        saveEventToFirestore(event);
                    });
                });
            });
        } catch (WriterException e) {
            Toast.makeText(this, "QR Code generation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

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

    private void saveEventToFirestore(Event event) {
        db.collection("events").document(event.getId()).set(event).addOnSuccessListener(documentReference -> {
            Toast.makeText(CreateEventActivity.this, "Event created successfully.", Toast.LENGTH_SHORT).show();
            navigateToSuccessActivity(event);
        }).addOnFailureListener(e -> {
            Toast.makeText(CreateEventActivity.this, "Error saving event.", Toast.LENGTH_SHORT).show();
        });
    }

    private void navigateToSuccessActivity(Event event) {
        Intent intent = new Intent(CreateEventActivity.this, EventSuccessActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }

    interface OnUploadCompleteListener {
        void onUploadComplete(String url);
    }
}
