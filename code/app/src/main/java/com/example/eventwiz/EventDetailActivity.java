package com.example.eventwiz;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventDetailActivity extends AppCompatActivity {

    private EditText etEventName, etEventDescription;
    private Spinner spinnerDay, spinnerMonth, spinnerYear;
    private Spinner spinnerFromHour, spinnerFromMinute, fromAmPM;
    private Spinner spinnerToHour, spinnerToMinute, toAmPM;
    private Button btnNext;
    private ImageButton backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        initializeUI();
        setupActionBar();
        setupListeners();
        populateSpinners();
    }

    private void initializeUI() {
        etEventName = findViewById(R.id.etEventName);
        etEventDescription = findViewById(R.id.etEventDescription);
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
        backArrow = findViewById(R.id.BackArrow);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Event Details");
        }
    }

    private void setupListeners() {
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
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                months.add(LocalDate.of(0, i, 1).getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
            }
        }
        return months;
    }
    private List<String> getDays(int maxDays) {
        List<String> days = new ArrayList<>();
        for (int i = 1; i <= maxDays; i++) {
            days.add(String.valueOf(i));
        }
        return days;
    }

    private List<String> getYears() {
        List<String> years = new ArrayList<>();
        int currentYear = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentYear = LocalDate.now().getYear();
        }
        for (int i = 0; i <= 10; i++) {
            years.add(String.valueOf(currentYear + i));
        }
        return years;
    }

    private List<String> getHours() {
        List<String> hours = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            hours.add(String.format(Locale.getDefault(), "%02d", i));
        }
        return hours;
    }

    private List<String> getMinutes() {
        List<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minutes.add(String.format(Locale.getDefault(), "%02d", i));
        }
        return minutes;
    }

    private void goToNextActivity() {
        Intent intent = new Intent(EventDetailActivity.this, EventLocationActivity.class);
        intent.putExtra("eventName", etEventName.getText().toString());
        intent.putExtra("eventDescription", etEventDescription.getText().toString());

        // Combine date and time parts to create full date-time strings
        String eventDate = spinnerDay.getSelectedItem().toString() + "-" +
                spinnerMonth.getSelectedItem().toString() + "-" +
                spinnerYear.getSelectedItem().toString();

        String eventStartTime = spinnerFromHour.getSelectedItem().toString() + ":" +
                spinnerFromMinute.getSelectedItem().toString() + " " +
                fromAmPM.getSelectedItem().toString();

        String eventEndTime = spinnerToHour.getSelectedItem().toString() + ":" +
                spinnerToMinute.getSelectedItem().toString() + " " +
                toAmPM.getSelectedItem().toString();

        // Add date and time information to intent
        intent.putExtra("eventDate", eventDate);
        intent.putExtra("eventStartTime", eventStartTime);
        intent.putExtra("eventEndTime", eventEndTime);

        startActivity(intent);
    }
}
