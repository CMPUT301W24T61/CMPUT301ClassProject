package com.example.eventwiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class EventLocationActivity extends AppCompatActivity {

    private EditText etAddressLine1, etAddressLine2, etAddressLine3, etCity, etAreaCodePostalCode, etStateProvince, etCountry;
    private Button btnNext;
    private Event event; // Added to hold the Event object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_location_detail);

        if (getIntent().hasExtra("event")) {
            event = (Event) getIntent().getSerializableExtra("event");
        }

        initializeUI();
        setupActionBar();
        setupNextButton();
    }

    private void initializeUI() {
        etAddressLine1 = findViewById(R.id.etAddressLine1);
        etAddressLine2 = findViewById(R.id.etAddressLine2);
        etAddressLine3 = findViewById(R.id.etAddressLine3);
        etCity = findViewById(R.id.etCity);
        etAreaCodePostalCode = findViewById(R.id.etAreaCodePostalCode);
        etStateProvince = findViewById(R.id.etStateProvince);
        etCountry = findViewById(R.id.etCountry);
        btnNext = findViewById(R.id.btnNext);

        ImageButton backButton = findViewById(R.id.BackArrow);
        backButton.setOnClickListener(view -> onBackPressed());
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Location Details");
        }
    }

    private void setupNextButton() {
        btnNext.setOnClickListener(v -> {
            StringBuilder locationBuilder = new StringBuilder();

            // Append AddressLine1
            String addressLine1 = etAddressLine1.getText().toString().trim();
            if (!addressLine1.isEmpty()) {
                locationBuilder.append(addressLine1).append(", ");
            }

            // Append AddressLine2 if it holds user input
            String addressLine2 = etAddressLine2.getText().toString().trim();
            if (!addressLine2.isEmpty()) {
                locationBuilder.append(addressLine2).append(", ");
            }

            // Append AddressLine3 if it holds user input
            String addressLine3 = etAddressLine3.getText().toString().trim();
            if (!addressLine3.isEmpty()) {
                locationBuilder.append(addressLine3).append(", ");
            }

            // Append City
            String city = etCity.getText().toString().trim();
            if (!city.isEmpty()) {
                locationBuilder.append(city).append(", ");
            }

            // Append AreaCodePostalCode
            String areaCodePostalCode = etAreaCodePostalCode.getText().toString().trim();
            if (!areaCodePostalCode.isEmpty()) {
                locationBuilder.append(areaCodePostalCode).append(", ");
            }

            // Append StateProvince
            String stateProvince = etStateProvince.getText().toString().trim();
            if (!stateProvince.isEmpty()) {
                locationBuilder.append(stateProvince).append(", ");
            }

            // Append Country
            String country = etCountry.getText().toString().trim();
            if (!country.isEmpty()) {
                locationBuilder.append(country);
            }

            // Remove trailing comma and space if present
            if (locationBuilder.length() > 0 && locationBuilder.charAt(locationBuilder.length() - 1) == ' ') {
                locationBuilder.deleteCharAt(locationBuilder.length() - 1); // Remove space
                locationBuilder.deleteCharAt(locationBuilder.length() - 1); // Remove comma
            }

            String location = locationBuilder.toString();

            event.setLocation(location);

            Intent intent = new Intent(EventLocationActivity.this, CreateEventActivity.class);
            intent.putExtra("event", event);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
