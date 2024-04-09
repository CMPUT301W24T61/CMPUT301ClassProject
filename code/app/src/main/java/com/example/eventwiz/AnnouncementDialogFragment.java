package com.example.eventwiz;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AnnouncementDialogFragment extends DialogFragment {

    private EditText editTextTitle, editTextDescription;
    private TextView textViewDate;
    private Button buttonSave;

    private FirebaseFirestore db;
    private String eventId;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        // Set the title
        builder.setTitle("Create Announcement");
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_announcement_dialog, null);

        // Initialize views
        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        textViewDate = view.findViewById(R.id.textViewDate);
        buttonSave = view.findViewById(R.id.buttonSave);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Get event ID from arguments
        Bundle args = getArguments();
        if (args != null) {
            eventId = args.getString("eventId");
        }

        // Set current date as default in textViewDate
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        textViewDate.setText(currentDate);

        // Handle save button click
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnnouncement();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    private void saveAnnouncement() {
        // Get user inputs
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String date = textViewDate.getText().toString().trim();

        // Validate inputs
        if (title.isEmpty() || description.isEmpty() || date.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Announcement object
        Announcement announcement = new Announcement(title, description, date);

        // Get the event document reference
        DocumentReference eventRef = db.collection("events").document(eventId);

        // Update the event document with the new announcement
        eventRef.update("announcements", FieldValue.arrayUnion(announcement))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "Announcement saved successfully", Toast.LENGTH_SHORT).show();
                    dismiss();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to save announcement", Toast.LENGTH_SHORT).show();
                });
    }

}
