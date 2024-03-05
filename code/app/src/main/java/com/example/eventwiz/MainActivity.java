package com.example.eventwiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This class will handle button presses from the main screen and will call other classes
 * and activities as necessary
 * @author Hunaid
 * Will need to update this with @see for classes as they are created.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("EventWiz");
        }

        Button buttonBrowseEvents = findViewById(R.id.button_browse_events);
//        buttonBrowseEvents.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, BrowseEventsActivity.class);
//                startActivity(intent);
//
//            }
//        });

        Button buttonRegister = findViewById(R.id.button_register);
//        buttonRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(intent);
//
//            }
//        });

        Button buttonScanQR = findViewById(R.id.button_scan_qr);
        buttonScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QRCodeScannerActivity.class);
                startActivity(intent);
            }
        });





    }
}