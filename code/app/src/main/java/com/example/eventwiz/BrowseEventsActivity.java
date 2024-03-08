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

import java.util.ArrayList;
import java.util.List;

/**
 * The BrowseEventsActivity class is responsible for displaying events in a list
 * retrieved from the Firebase database. It extends the AppCompatActivity and
 * utilizes a custom EventAdapter for displaying events in a ListView.
 *
 * @see Event
 * @author Hunaid
 */

public class BrowseEventsActivity extends AppCompatActivity {
    private ListView listView;
    private EventAdapter adapter;

    // This should be a list of Event objects now
    private List<Event> events;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Called when the activity is first created. This method sets up the user interface,
     * initializes the ActionBar, configures the ListView and adapter, sets up item click
     * listeners, and fetches events from the Firestore database.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_events);

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

    /**
     * Fetches events from the Firestore database and populates the events list.
     * Notifies the adapter when the data set changes.
     */
    private void fetchEvents() {
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Use toObject to convert the document to an Event object directly
                            Event event = document.toObject(Event.class);
                            // Ensure that the ID is set from the document
                            event.setId(document.getId());
                            events.add(event);
                        }
                        adapter.notifyDataSetChanged(); // Notify the adapter that the data set has changed
                    } else {
                        Log.e("BrowseEventsActivity", "Error getting documents: ", task.getException());
                    }
                });
    }
}