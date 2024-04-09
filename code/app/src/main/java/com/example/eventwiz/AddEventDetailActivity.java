package com.example.eventwiz;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEventDetailActivity extends AppCompatActivity {

    private EditText etEventName, etEventDescription, etMaxAttendees;
    private Spinner spinnerDay, spinnerMonth, spinnerYear;
    private Spinner spinnerFromHour, spinnerFromMinute, fromAmPM;
    private Spinner spinnerToHour, spinnerToMinute, toAmPM;
    private Button btnNext;
    private ImageButton backArrow;
    private FirebaseFirestore db;

    private Integer maxAttendees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        db = FirebaseFirestore.getInstance();
        initializeUI();
        populateSpinners();
    }

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
        backArrow = findViewById(R.id.back_arrow);

        btnNext.setOnClickListener(v -> goToNextActivity());
        backArrow.setOnClickListener(v -> finish());
    }

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

    private List<String> getMonths() {
        return Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    }

    private List<String> getDays(int days) {
        List<String> dayList = new ArrayList<>();
        for (int i = 1; i <= days; i++) {
            dayList.add(String.valueOf(i));
        }
        return dayList;
    }

    private List<String> getYears() {
        List<String> yearList = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i <= currentYear + 10; i++) {
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

    private void goToNextActivity() {
        String eventName = etEventName.getText().toString();
        String eventDescription = etEventDescription.getText().toString().trim();
        String maxAttendeesStr = etMaxAttendees.getText().toString();


        if (!TextUtils.isEmpty(maxAttendeesStr)) {
            try {
                maxAttendees = Integer.parseInt(maxAttendeesStr);
            } catch (NumberFormatException e) {
                etMaxAttendees.setError("Please enter a valid number for max attendees");
                return;
            }
        } else {
            // Set default value to be limitless if max attendees is not provided
            maxAttendees = Integer.MAX_VALUE;
        }

        if (TextUtils.isEmpty(eventName)) {
            etEventName.setError("Event name is required");
            return;
        }

        if (eventDescription.length() > 50) {
            Toast.makeText(this, "Description cannot exceed 50 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if end time is after start time
        String startTime = spinnerFromHour.getSelectedItem().toString() + ":" +
                spinnerFromMinute.getSelectedItem().toString() + " " +
                fromAmPM.getSelectedItem().toString();
        String endTime = spinnerToHour.getSelectedItem().toString() + ":" +
                spinnerToMinute.getSelectedItem().toString() + " " +
                toAmPM.getSelectedItem().toString();

        if (!isTimeValid(startTime, endTime)) {
            Toast.makeText(this, "End time must be after start time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proceed to the next activity
        String date = spinnerDay.getSelectedItem().toString() + "-" +
                spinnerMonth.getSelectedItem().toString() + "-" +
                spinnerYear.getSelectedItem().toString();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String organizerId = currentUser != null ? currentUser.getUid() : null;
        Map<String, Integer> checkInsCount = new HashMap<>();
        Event event = new Event(eventName, eventDescription, date, startTime, endTime, "", maxAttendees, "", "", "", "", "", new ArrayList<>(), checkInsCount, new ArrayList<>());
        event.setOrganizerId(organizerId);
        saveEventToFirestore(event);
        Intent intent = new Intent(AddEventDetailActivity.this, AddEventLocationActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }

    private void saveEventToFirestore(Event event) {
        if (event != null && event.getId() != null) {
            // Save the event to Firestore with the event ID as the document ID
            db.collection("events").document(event.getId()).set(event);
        } else {
            // Handle the case where the event object or its ID is null
            Toast.makeText(this, "Event details are invalid", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isTimeValid(String startTime, String endTime) {
        // Convert start and end time to minutes since midnight for comparison
        int startMinutes = convertToMinutesSinceMidnight(startTime);
        int endMinutes = convertToMinutesSinceMidnight(endTime);

        // Check if end time is after start time
        return endMinutes > startMinutes;
    }

    private int convertToMinutesSinceMidnight(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1].split(" ")[0]);
        String amPm = parts[1].split(" ")[1];

        // Convert hour to 24-hour format if it's PM
        if (amPm.equals("PM") && hour != 12) {
            hour += 12;
        }

        return hour * 60 + minutes;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
