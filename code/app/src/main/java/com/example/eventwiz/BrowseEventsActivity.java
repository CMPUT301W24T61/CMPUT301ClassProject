package com.example.eventwiz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BrowseEventsActivity extends AppCompatActivity {
    private ListView listView;
    private EventAdapter adapter;
    private List<EventBrief> events;

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
    }


}