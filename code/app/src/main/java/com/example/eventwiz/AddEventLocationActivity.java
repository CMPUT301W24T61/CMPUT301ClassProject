package com.example.eventwiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Activity for adding event location details.
 * @author Junkai
 */
public class AddEventLocationActivity extends AppCompatActivity {

    private EditText etAddressLine1, etAddressLine2, etAddressLine3, etCity, etAreaCodePostalCode, etStateProvince, etCountry;
    private Button btnNext;
    private Event event;
    public Organizer organizer;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_location_detail);
        organizer = (Organizer) getIntent().getSerializableExtra("organizer");
        db = FirebaseFirestore.getInstance(); // Initialize Firestore instance

        if (getIntent().hasExtra("event")) {
            event = (Event) getIntent().getSerializableExtra("event");
        }

        initializeUI();
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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Location Details");
        }

        btnNext.setOnClickListener(v -> processEventLocation());
        backButton.setOnClickListener(view -> onBackPressed());
    }

    private void processEventLocation() {
        StringBuilder locationBuilder = new StringBuilder();
        appendLocationPart(etAddressLine1, locationBuilder);
        appendLocationPart(etAddressLine2, locationBuilder);
        appendLocationPart(etAddressLine3, locationBuilder);
        appendLocationPart(etCity, locationBuilder);
        appendLocationPart(etAreaCodePostalCode, locationBuilder);
        appendLocationPart(etStateProvince, locationBuilder);
        appendLocationPart(etCountry, locationBuilder);

        // Remove trailing comma and space
        String location = locationBuilder.toString().replaceAll(", $", "");

        event.setLocation(location);
        saveEventToFirestore(event);

        Intent intent = new Intent(AddEventLocationActivity.this, CreateEventActivity.class);
        intent.putExtra("event", event);
        intent.putExtra("organizer", organizer);
        startActivity(intent);
    }

    private void appendLocationPart(EditText editText, StringBuilder builder) {
        String text = editText.getText().toString().trim();
        if (!text.isEmpty()) {
            builder.append(text).append(", ");
        }
    }

    private void saveEventToFirestore(Event event) {
        db.collection("events").document(event.getId()).set(event);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
