package com.example.eventwiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Activity for the user dashboard, providing options to create events, browse events, and scan QR codes.
 * @author Junkai
 */
public class DashboardActivity extends AppCompatActivity {

    // Declare UI components
    private Button createEventButton;
    private Button hostedEventsButton;
    private Button browseEventsButton;
    private Button profileButton;

    private FloatingActionButton scanQRButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // Initialize ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("User Dashboard");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Initialize UI components
        createEventButton = findViewById(R.id.createEvent);
        hostedEventsButton = findViewById(R.id.myHostedEvents);
        browseEventsButton = findViewById(R.id.browseEvents);
        profileButton = findViewById(R.id.myProfile);
        scanQRButton = findViewById(R.id.fabCamera);
        ImageButton backButton = findViewById(R.id.BackArrow);


        // Set onClickListeners for each button
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to navigate to CreateEventActivity
                Intent intent = new Intent(DashboardActivity.this, AddEventDetailActivity.class);
                startActivity(intent);
            }
        });

        scanQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the QRCodeScannerActivity
                Intent intent = new Intent(DashboardActivity.this, ScanQRActivity.class);
                startActivity(intent);
            }
        });

        browseEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashboardActivity.this, BrowseEventsActivity.class);
                startActivity(intent);
            }
        });

        //manageEventsButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {

                //Intent intent = new Intent(OrganizerDashboardActivity.this, ManageEventsActivity.class);
                //startActivity(intent);
            //}
        //});

        backButton.setOnClickListener(view -> onBackPressed());


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
