package com.example.eventwiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class BrowseSignedUpEventsActivity extends AppCompatActivity implements EventAdapter.EventClickListener {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Event> signedUpEvents = new ArrayList<>();
    private EventAdapter adapter;
    private List<Event> events;
    private ListView eventsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_signed_up_events);

        ImageButton backButton = findViewById(R.id.BackArrow);
        backButton.setOnClickListener(view -> finish());

        eventsListView = findViewById(R.id.lvEvents);
        adapter = new EventAdapter(this, signedUpEvents, this);
        eventsListView.setAdapter(adapter);

        fetchSignedUpEvents();
    }

    private void fetchSignedUpEvents() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String currentUserId = user.getUid();
            db.collection("events")
                    .whereArrayContains("signups", currentUserId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        signedUpEvents.clear();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Event event = documentSnapshot.toObject(Event.class);
                            event.setId(documentSnapshot.getId());
                            signedUpEvents.add(event);
                        }
                        adapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> Log.e("BrowseSignedUpEvents", "Error loading signed up events", e));
        }
    }

    @Override
    public void onEventClicked(Event event) {
        Intent intent = new Intent(BrowseSignedUpEventsActivity.this, ViewEventDetailsActivity.class);
        intent.putExtra("eventId", event.getId());
        startActivity(intent);
    }
}
