package com.example.eventwiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminDashboard extends AppCompatActivity {

    // Declare UI components
    private Button browseEventsButton;
    private Button browseUsers;

    private Button browseImages;

    private FloatingActionButton scanQRButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Organizer Dashboard");
            actionBar.setDisplayHomeAsUpEnabled(true); // Enable the Up button
        }
        FloatingActionButton scanQRButton = findViewById(R.id.fabCamera);
        scanQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this, QRCodeScannerActivity.class);
                startActivity(intent);
            }
        });

        // Initialize UI components
        browseEventsButton = findViewById(R.id.events);
        browseUsers = findViewById(R.id.users);

        browseImages = findViewById(R.id.images);

        // Set onClickListeners for each button
        QRCodeScannerActivity.isUserAdmin();

        browseEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to navigate to CreateEventActivity
                Intent intent = new Intent(AdminDashboard.this, BrowseEventsActivity.class);
                startActivity(intent);
            }
        });

        browseUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to navigate to CreateEventActivity
                Intent intent = new Intent(AdminDashboard.this, AdminBrowseUsersActivity.class);
                startActivity(intent);
            }
        });

        browseImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to navigate to CreateEventActivity
                Intent intent = new Intent(AdminDashboard.this, AdminBrowseImages.class);
                startActivity(intent);
            }
        });

//        scanQRButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Start the QRCodeScannerActivity
//                Intent intent = new Intent(AdminDashboard.this, ScanQRActivity.class);
//                startActivity(intent);
//            }
//        });

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
