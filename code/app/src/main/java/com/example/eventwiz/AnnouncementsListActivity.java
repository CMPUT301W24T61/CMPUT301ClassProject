package com.example.eventwiz;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementsListActivity extends AppCompatActivity {

    private ListView announcementsListView;
    private AnnouncementAdapter adapter;
    private List<Announcement> announcements;

    private FirebaseFirestore db;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcements_list);

        // Get the event ID from the intent
        eventId = getIntent().getStringExtra("eventId");

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize announcements list
        announcements = new ArrayList<>();

        // Initialize ListView and Adapter
        announcementsListView = findViewById(R.id.lvAnnouncements);
        adapter = new AnnouncementAdapter(this, announcements);
        announcementsListView.setAdapter(adapter);

        // Fetch announcements from Firestore
        fetchAnnouncements();
        ImageButton btnGoBack = findViewById(R.id.back_arrow);
        btnGoBack.setOnClickListener(v -> finish());


    }

    private void fetchAnnouncements() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null || eventId == null || eventId.trim().isEmpty()) {
            Toast.makeText(AnnouncementsListActivity.this, "Error: Invalid event ID or user not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch announcements for the specified event
        DocumentReference eventRef = db.collection("events").document(eventId);
        eventRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<Announcement> fetchedAnnouncements = documentSnapshot.toObject(Event.class).getAnnouncements();
                if (fetchedAnnouncements != null) {
                    announcements.addAll(fetchedAnnouncements);
                    adapter.notifyDataSetChanged();
                }
            } else {
                Log.d("AnnouncementsListActivity", "No such document");
            }
        }).addOnFailureListener(e -> {
            Log.e("AnnouncementsListActivity", "Error fetching announcements", e);
        });
    }


}
