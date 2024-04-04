package com.example.eventwiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * The AdminDashboard class represents the main dashboard for administrators in the EventWiz application.
 * It provides access to various functionalities such as browsing events, users, and images.
 * Administrators can perform actions like scanning QR codes and managing the application.
 *
 * @author Hunaid
 * @version 1.0
 * @since 2024-03-08
 */
public class AdminDashboard extends AppCompatActivity {

    // Declare UI components
    private Button browseEventsButton;
    private Button browseUsers;

    private Button browseImages;

    private FloatingActionButton scanQRButton;

    /**
     * Called when the activity is first created. This method initializes the UI components,
     * sets up the ActionBar, and defines onClickListeners for each button.
     *
     * @param savedInstanceState A Bundle containing the saved state of the activity (if any).
     */
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

    }
}
