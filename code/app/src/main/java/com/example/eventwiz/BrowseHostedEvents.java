package com.example.eventwiz;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class BrowseHostedEvents extends AppCompatActivity {
    private ListView listView;
    private EventAdapter adapter;

    // This should be a list of Event objects now
    private List<Event> events;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_hosted_events);

        // Setup the action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("EventWiz");
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.turqoise)));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Initialize the list and adapter
        listView = findViewById(R.id.lvEvents);
        events = new ArrayList<>();
        adapter = new EventAdapter(this, events);
        listView.setAdapter(adapter);

        // Set a listener for list item clicks
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Event event = events.get(position);
            // Navigate to ViewEventDetailsActivity with the event ID
        });

        // Back button
        ImageButton backButton = findViewById(R.id.BackArrow);
        backButton.setOnClickListener(view -> onBackPressed());

        // Fetch events from Firestore
        fetchEvents();
    }

    private void fetchEvents() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String currentUserId = user.getUid();

            db.collection("events")
                    .whereEqualTo("organizerId", currentUserId) // Filter events by organizerId
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            events.clear(); // Clear existing events to avoid duplicates
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Event event = document.toObject(Event.class);
                                event.setId(document.getId()); // Ensure the ID is set
                                events.add(event);
                            }
                            adapter.notifyDataSetChanged(); // Notify the adapter of the change
                        } else {
                            Log.e("BrowseHostedEvents", "Error getting documents: ", task.getException());
                        }
                    });
        } else {
            Log.e("BrowseHostedEvents", "No user signed in");
            // Handle case where no user is signed in (optional)
        }
    }
}