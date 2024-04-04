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
 * This activity allows the user to input location information for an event,
 * such as address lines, city, postal code, state, and country.
 *
 * The entered location details are then processed and associated with the event,
 * and the event information is saved to Firestore.
 *
 * @author Junkai
 */
public class AddEventLocationActivity extends AppCompatActivity {

    private EditText etAddressLine1, etAddressLine2, etAddressLine3, etCity, etAreaCodePostalCode, etStateProvince, etCountry;
    private Button btnNext;
    private Event event;
    public Organizer organizer;
    private FirebaseFirestore db;


    /**
     * Called when the activity is first created.
     * Initializes the UI components, retrieves the organizer information from the intent,
     * and sets up Firestore for data storage.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
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
    /**
     * Initializes the user interface components and sets up event listeners for buttons.
     */
    private void initializeUI() {
        etAddressLine1 = findViewById(R.id.etAddressLine1);
        etAddressLine2 = findViewById(R.id.etAddressLine2);
        etAddressLine3 = findViewById(R.id.etAddressLine3);
        etCity = findViewById(R.id.etCity);
        etAreaCodePostalCode = findViewById(R.id.etAreaCodePostalCode);
        etStateProvince = findViewById(R.id.etStateProvince);
        etCountry = findViewById(R.id.etCountry);
        btnNext = findViewById(R.id.btnNext);
        ImageButton backButton = findViewById(R.id.back_arrow);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Location Details");
        }

        btnNext.setOnClickListener(v -> processEventLocation());
        backButton.setOnClickListener(view -> onBackPressed());
    }

    /**
     * Processes the entered event location details, constructs a location string,
     * associates it with the event, and saves the event information to Firestore.
     * Finally, navigates to the next activity for additional event details.
     */
    private void processEventLocation() {
         if (isFieldEmpty(etAddressLine1) || isFieldEmpty(etCity) || isFieldEmpty(etStateProvince)
                 || isFieldEmpty(etCountry) || isFieldEmpty(etAreaCodePostalCode)) {
                 etAddressLine1.setError("This field is required");
                 etCity.setError("This field is required");
                 etStateProvince.setError("This field is required");
                 etCountry.setError("This field is required");
                 etAreaCodePostalCode.setError("This field is required");
                 return;
                 }    
        StringBuilder locationBuilder = new StringBuilder();
        appendLocationPart(etAddressLine1, locationBuilder);
        appendLocationPart(etAddressLine2, locationBuilder);
        appendLocationPart(etAddressLine3, locationBuilder);
        appendLocationPart(etCity, locationBuilder);
        appendLocationPart(etStateProvince, locationBuilder);
        appendLocationPart(etCountry, locationBuilder);
        appendLocationPart(etAreaCodePostalCode, locationBuilder); 

        String location = locationBuilder.toString().replaceAll(", $", "");

        event.setLocation(location);
        saveEventToFirestore(event);

        Intent intent = new Intent(AddEventLocationActivity.this, CreateEventActivity.class);
        intent.putExtra("event", event);
        intent.putExtra("organizer", organizer);
        startActivity(intent);
    }

    /**
     * Appends the text value of the EditText to the StringBuilder if it is not empty.
     *
     * @param editText The EditText containing the text to be appended.
     * @param builder  The StringBuilder to append the text to.
     */
    private void appendLocationPart(EditText editText, StringBuilder builder) {
        String text = editText.getText().toString().trim();
        if (!text.isEmpty()) {
            builder.append(text).append(", ");
        }
    }

    /**
     * Saves the event information to Firestore.
     *
     * @param event The Event object to be saved to Firestore.
     */
    private void saveEventToFirestore(Event event) {
        db.collection("events").document(event.getId()).set(event);
    }

    /**
     * Handles the "Up" button in the ActionBar, navigating back when pressed.
     *
     * @return true if the navigation was handled successfully.
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

     private boolean isFieldEmpty(EditText editText) {

             return editText.getText().toString().trim().isEmpty();

     }



}