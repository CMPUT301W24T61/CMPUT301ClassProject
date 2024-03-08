package com.example.eventwiz;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for handling the Browse Events screen
 * @see Event
 * @author Hunaid
 */
public class BrowseEventsActivity extends AppCompatActivity {
    private ListView listView;
    private EventAdapter adapter;
    private List<Event> events; // You should populate this list with actual event data
    private Button deleteEventButton;
    private Event selectedEvent;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_events);

        deleteEventButton = findViewById(R.id.button_delete);
        listView = findViewById(R.id.lvEvents);
        db = FirebaseFirestore.getInstance();

        // Initialize your events list here
        events = new ArrayList<>();
        //fetch event list from firebase
        Event newEvent = new Event("IDK", "DS", "SDSD", "https://www.google.com/url?sa=i&url=https%3A%2F%2Fsamderlust.com%2Fdev-blog%2Fjava%2Fwrite-read-arraylist-object-file-java&psig=AOvVaw3WnZBF_93h7cCM7S14ij7e&ust=1709789902768000&source=images&cd=vfe&opi=89978449&ved=0CBMQjRxqFwoTCMidrPD13oQDFQAAAAAdAAAAABAJ", "SDS");
        events.add(newEvent);
        events.add(newEvent);
        adapter = new EventAdapter(this, events);
        listView.setAdapter(adapter);

        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent(selectedEvent);
                deleteEventButton.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void toggleDeleteButton(View view, int position){
        deleteEventButton = findViewById(R.id.button_delete);
        deleteEventButton.setVisibility(View.VISIBLE);
        selectedEvent = getSelectedEvent(position);
    }

    private Event getSelectedEvent(int position){
        return (Event) events.get(position);
    }

    public void deleteEvent(Event event){
        events.remove(event);
        adapter.remove(event);
        db.collection("events").document(event.getEventName())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firestore", "Document Snapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error deleting document", e);
                    }
                });
        adapter.notifyDataSetChanged();
    }

}