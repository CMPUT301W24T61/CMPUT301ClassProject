package com.example.eventwiz;

import android.content.Intent;
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

public class BrowseEventsActivity extends AppCompatActivity implements EventAdapter.EventClickListener {
    private ListView listView;
    private EventAdapter adapter;

    // This should be a list of Event objects now
    private List<Event> events;


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
        adapter = new EventAdapter(this, events, this);
        listView.setAdapter(adapter);

        // Back button
        ImageButton backButton = findViewById(R.id.BackArrow);
        backButton.setOnClickListener(view -> onBackPressed());

        // Fetch events from Firestore
        fetchEvents();

        // Set a listener for list item clicks
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Event event = events.get(position);
            Intent intent = new Intent(BrowseEventsActivity.this, ViewEventDetailsActivity.class);
            intent.putExtra("eventId", event.getId());
            startActivity(intent);
        });


    }
    @Override
    public void onEventClicked(Event event) {
        Intent intent = new Intent(BrowseEventsActivity.this, ViewEventDetailsActivity.class);
        intent.putExtra("eventId", event.getId());
        startActivity(intent);
    }


    private void fetchEvents() {
        AttendeeService.fetchEvents(new AttendeeService.EventsFetchListener() {
            @Override
            public void onEventsFetched(List<Event> fetchedEvents) {
                events.addAll(fetchedEvents);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFetchError(Exception e) {
                Log.e("BrowseEventsActivity", "Error fetching events: ", e);
            }
        });
    }
}