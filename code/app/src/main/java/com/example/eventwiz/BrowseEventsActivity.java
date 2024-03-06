package com.example.eventwiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
public class BrowseEventsActivity extends AppCompatActivity {
    private ListView listView;
    private EventAdapter adapter;
    private List<Event> events; // You should populate this list with actual event data

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_events);

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

                            Event newEvent = new Event(eventName, eventDate +"  " + eventTimeStart + " to " + eventTimeEnd, eventTimeStart, posterUrl, venue);
                            events.add(newEvent);
                        }
                        adapter.notifyDataSetChanged(); // Notify the adapter that the data set has changed
                    } else {
                        // Handle the error
                        Log.d("error", "This is firebase error");
                    }
                });
    }
}