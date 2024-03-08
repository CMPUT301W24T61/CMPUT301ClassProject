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
 * BrowseEventsActivity is responsible for displaying events in a list from the firebase database
 * @see Event
 * @author Hunaid
 */

public class BrowseEventsActivity extends AppCompatActivity {
    private ListView listView;
    private EventAdapter adapter;

    private List<EventBrief> events;



    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_events);


        ImageButton backButton = findViewById(R.id.BackArrow);
        backButton.setOnClickListener(view -> onBackPressed());


        listView = findViewById(R.id.lvEvents);
        events = new ArrayList<>();

        // Example: Fetch events asynchronously from Firebase or any other source
        // Async data fetching code goes here...

        // After fetching data, update the events list and notify the adapter
        // events.addAll(fetchedEvents);
        // adapter.notifyDataSetChanged();

        // For now, add sample data
        EventBrief newEventBrief = new EventBrief("IDK", "DS", "SDSD", "URL_TO_IMAGE", "SDS","LOC");
        events.add(newEventBrief);

        adapter = new EventAdapter(this, events);
        listView.setAdapter(adapter);

        // Add click listener to handle item clicks
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle item click, e.g., navigate to detail view
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("EventWiz");
            int color = ContextCompat.getColor(this, R.color.turqoise);

            // Set the background color of the ActionBar
            actionBar.setBackgroundDrawable(new ColorDrawable(color));

            // Set display option
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.lvEvents);
        events = new ArrayList<>();
        adapter = new EventAdapter(this, events);
        listView.setAdapter(adapter);
        fetchEvents();
    }

    private void fetchEvents() {
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String eventName = document.getString("name");
                            String eventDate = document.getString("date");
                            String eventTimeEnd = document.getString("endTime");
                            String eventTimeStart = document.contains("startTime") ? document.getString("startTime") : ""; // Assuming 'startTime' in your Firestore
                            String posterUrl = document.getString("posterUrl");
                            String venue = document.getString("location");

                            EventBrief newEventBrief = new EventBrief(eventName, eventDate,eventTimeStart, eventTimeEnd,posterUrl,venue);
                            events.add(newEventBrief);
                        }
                        adapter.notifyDataSetChanged(); // Notify the adapter that the data set has changed
                    } else {
                        // Handle the error
                        Log.d("error", "This is firebase error");
                    }
                });

    }


}