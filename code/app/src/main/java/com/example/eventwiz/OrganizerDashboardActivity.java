package com.example.eventwiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.eventwiz.R;  

public class OrganizerDashboardActivity extends AppCompatActivity {

    // Declare UI components
    private Button createEventButton;
    private Button viewEventsButton;
    private Button manageEventsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_dashboard);

        // Initialize ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Organizer Dashboard");
            actionBar.setDisplayHomeAsUpEnabled(true); // Enable the Up button
        }

        // Initialize UI components
        createEventButton = findViewById(R.id.createEvent);
        viewEventsButton = findViewById(R.id.myEvents);
        manageEventsButton = findViewById(R.id.eventDashboard);

        // Set onClickListeners for each button
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to navigate to CreateEventActivity
                Intent intent = new Intent(OrganizerDashboardActivity.this, EventDetailActivity.class);
                startActivity(intent);
            }
        });

        //viewEventsButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {

               // Intent intent = new Intent(OrganizerDashboardActivity.this, ViewEventsActivity.class);
                //startActivity(intent);
            //}
        //});

        //manageEventsButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {

                //Intent intent = new Intent(OrganizerDashboardActivity.this, ManageEventsActivity.class);
                //startActivity(intent);
            //}
        //});
    }
}
