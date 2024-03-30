package com.example.eventwiz;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;


/**
 * Activity for adding event details.
 *
 * This activity allows the user to input various details for a new event, such as event name,
 * description, date, time, and maximum attendees. The entered information is used to create an
 * Event object, which is then stored in Firestore. After entering the details, the user can proceed
 * to the next step in adding event information.
 *
 * The class also initializes and handles UI components, populates spinners with relevant data, and
 * provides navigation to the next activity in the event creation process.
 *
 * @author Junkai,Yesith
 * @version 1.0
 * @since 2024-03-08
 */
public class AddEventDetailActivity extends AppCompatActivity {


    private EditText etEventName, etEventDescription, etMaxAttendees;
    private Spinner spinnerDay, spinnerMonth, spinnerYear;
    private Spinner spinnerFromHour, spinnerFromMinute, fromAmPM;
    private Spinner spinnerToHour, spinnerToMinute, toAmPM;
    private Button btnNext;
    private ImageButton backArrow;
    public Organizer organizer;
    public FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        db = FirebaseFirestore.getInstance();
        initializeUI();
        populateSpinners();
    }

    /**
     * Initializes the user interface components, sets up the action bar, and assigns listeners
     * to buttons and spinners.
     */
    private void initializeUI() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.hide();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        etEventName = findViewById(R.id.etEventName);
        etEventDescription = findViewById(R.id.etEventDescription);
        etMaxAttendees = findViewById(R.id.etMaxAttendees);


        spinnerDay = findViewById(R.id.spinnerDay);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);
        spinnerFromHour = findViewById(R.id.spinnerFromHour);
        spinnerFromMinute = findViewById(R.id.spinnerFromMinute);
        fromAmPM = findViewById(R.id.fromAmPM);
        spinnerToHour = findViewById(R.id.spinnerToHour);
        spinnerToMinute = findViewById(R.id.spinnerToMinute);
        toAmPM = findViewById(R.id.toAmPM);


        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> goToNextActivity());


        backArrow = findViewById(R.id.BackArrow);
        backArrow.setOnClickListener(v -> finish());
    }

    /**
     * Populates various spinners with data such as months, days, years, hours, and minutes.
     */
    private void populateSpinners() {
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getMonths());
        spinnerMonth.setAdapter(monthAdapter);
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getDays(31));
        spinnerDay.setAdapter(dayAdapter);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getYears());
        spinnerYear.setAdapter(yearAdapter);
        ArrayAdapter<String> hourAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getHours());
        spinnerFromHour.setAdapter(hourAdapter);
        spinnerToHour.setAdapter(hourAdapter);
        ArrayAdapter<String> minuteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getMinutes());
        spinnerFromMinute.setAdapter(minuteAdapter);
        spinnerToMinute.setAdapter(minuteAdapter);
        ArrayAdapter<String> amPmAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"AM", "PM"});
        fromAmPM.setAdapter(amPmAdapter);
        toAmPM.setAdapter(amPmAdapter);
    }

    /**
     * Retrieves a list of months.
     *
     * @return List of month names.
     */
    private List<String> getMonths() {
        return Arrays.asList("January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December");
    }

    /**
     * Retrieves a list of days based on the provided number of days.
     *
     * @param days The number of days in the month.
     * @return List of day values as strings.
     */
    private List<String> getDays(int days) {
        List<String> dayList = new ArrayList<>();
        for (int i = 1; i <= days; i++) {
            dayList.add(String.valueOf(i));
        }
        return dayList;
    }

    /**
     * Retrieves a list of years
     *
     *
     * @return List of years as strings.
     */

    private List<String> getYears() {
        List<String> yearList = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i <= currentYear + 10; i++) { // Example range
            yearList.add(String.valueOf(i));
        }
        return yearList;
    }


    private List<String> getHours() {
        List<String> hours = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            hours.add(String.format("%02d", i));
        }
        return hours;
    }


    private List<String> getMinutes() {
        List<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minutes.add(String.format("%02d", i));
        }
        return minutes;
    }





    /**
     * Navigates to the next activity after gathering event details and saving them to Firestore.
     */
    private void goToNextActivity() {
        String eventName = etEventName.getText().toString();
        String eventDescription = etEventDescription.getText().toString();
        String maxAttendeesStr = etMaxAttendees.getText().toString();
        Integer maxAttendees = maxAttendeesStr.isEmpty() ? null : Integer.parseInt(maxAttendeesStr);

        String date = spinnerDay.getSelectedItem().toString() + "-" +
                spinnerMonth.getSelectedItem().toString() + "-" +
                spinnerYear.getSelectedItem().toString();
        String startTime = spinnerFromHour.getSelectedItem().toString() + ":" +
                spinnerFromMinute.getSelectedItem().toString() + " " +
                fromAmPM.getSelectedItem().toString();
        String endTime = spinnerToHour.getSelectedItem().toString() + ":" +
                spinnerToMinute.getSelectedItem().toString() + " " +
                toAmPM.getSelectedItem().toString();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String organizerId = currentUser != null ? currentUser.getUid() : null; // Ensure currentUser is not null
        Map<String, Integer> checkInsCount = new HashMap<>();
        Event event = new Event(eventName, eventDescription, date, startTime, endTime, "", maxAttendees, "", "", "", "", "", new ArrayList<>(), checkInsCount);
        event.setOrganizerId(organizerId);
        saveEventToFirestore(event);
        Intent intent = new Intent(AddEventDetailActivity.this, AddEventLocationActivity.class);
        intent.putExtra("event", event);
        intent.putExtra("organizer", organizer);
        startActivity(intent);
    }


    /**
     * Saves the provided event details to Firestore.
     *
     * @param event The {@code Event} object to be stored in Firestore.
     */
    private void saveEventToFirestore(Event event) {
        db.collection("events").document(event.getId()).set(event);
    }

    /**
     * Handles the "Up" button press in the action bar.
     *
     * @return {@code true} if the navigation was handled successfully.
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

