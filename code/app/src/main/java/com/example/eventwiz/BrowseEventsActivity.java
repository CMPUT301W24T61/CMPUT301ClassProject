package com.example.eventwiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BrowseEventsActivity extends AppCompatActivity {
    private ListView listView;
    private EventAdapter adapter;
    private List<Event> events; // You should populate this list with actual event data
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_events);


        listView = findViewById(R.id.lvEvents);

        // Initialize your events list here
        events = new ArrayList<>();
        //fetch event list from firebase
        Event newEvent = new Event("IDK", "DS", "SDSD", "https://www.google.com/url?sa=i&url=https%3A%2F%2Fsamderlust.com%2Fdev-blog%2Fjava%2Fwrite-read-arraylist-object-file-java&psig=AOvVaw3WnZBF_93h7cCM7S14ij7e&ust=1709789902768000&source=images&cd=vfe&opi=89978449&ved=0CBMQjRxqFwoTCMidrPD13oQDFQAAAAAdAAAAABAJ", "SDS");
        events.add(newEvent);
        events.add(newEvent);
        events.add(newEvent);
        events.add(newEvent);
        adapter = new EventAdapter(this, events);
        listView.setAdapter(adapter);
    }
}