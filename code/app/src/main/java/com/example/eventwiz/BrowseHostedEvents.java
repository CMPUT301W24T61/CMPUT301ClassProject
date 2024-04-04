package com.example.eventwiz;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class BrowseHostedEvents extends AppCompatActivity implements EventAdapter.EventClickListener {
    private ListView listView;
    private EventAdapter adapter;

    private List<Event> events;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ImageButton backButton; // Corrected declaration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_hosted_events);

        // Correct initialization of backButton
        backButton = findViewById(R.id.back_arrow);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrowseHostedEvents.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("EventWiz");
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.turqoise)));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.lvEvents);
        events = new ArrayList<>();
        adapter = new EventAdapter(this, events, this);
        listView.setAdapter(adapter);

        fetchEvents();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Event event = events.get(position);
            Intent intent = new Intent(BrowseHostedEvents.this, HostedEventDashboardActivity.class);
            intent.putExtra("eventId", event.getId());
            startActivity(intent);
        });
    }

    @Override
    public void onEventClicked(Event event) {
        Intent intent = new Intent(BrowseHostedEvents.this, HostedEventDashboardActivity.class);
        intent.putExtra("eventId", event.getId());
        startActivity(intent);
    }

    private void fetchEvents() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String currentUserId = user.getUid();

            db.collection("events")
                    .whereEqualTo("organizerId", currentUserId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            events.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Event event = document.toObject(Event.class);
                                event.setId(document.getId());
                                events.add(event);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("BrowseHostedEvents", "Error getting documents: ", task.getException());
                        }
                    });
        } else {
            Log.e("BrowseHostedEvents", "No user signed in");
        }
    }
}
