package com.example.eventwiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class EventLocationActivity extends AppCompatActivity {

    private EditText etAddressLine1, etAddressLine2, etAddressLine3, etCity, etAreaCodePostalCode;
    private Spinner spinnerStateProvince, spinnerCountry;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_location_detail);

        initializeUI();
        setupActionBar();
        setupNextButton();
    }

    private void initializeUI() {
        etAddressLine1 = findViewById(R.id.etAddressLine1);
        etAddressLine2 = findViewById(R.id.etAddressLine2); // Ensure ID matches XML
        etAddressLine3 = findViewById(R.id.etAddressLine3); // Ensure ID matches XML
        etCity = findViewById(R.id.etCity);
        etAreaCodePostalCode = findViewById(R.id.etAreaCodePostalCode);
        spinnerStateProvince = findViewById(R.id.spinnerStateProvince);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        btnNext = findViewById(R.id.btnNext);

        ImageButton backButton = findViewById(R.id.BackArrow);
        backButton.setOnClickListener(view -> onBackPressed());
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Location");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupNextButton() {
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(EventLocationActivity.this, CreateEventActivity.class);
            // Collect and pass all location data
            intent.putExtra("addressLine1", etAddressLine1.getText().toString());
            intent.putExtra("addressLine2", etAddressLine2.getText().toString());
            intent.putExtra("addressLine3", etAddressLine3.getText().toString());
            intent.putExtra("city", etCity.getText().toString());
            intent.putExtra("stateProvince", spinnerStateProvince.getSelectedItem().toString());
            intent.putExtra("country", spinnerCountry.getSelectedItem().toString());
            intent.putExtra("areaCodePostalCode", etAreaCodePostalCode.getText().toString());

            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
