package com.example.eventwiz;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AttendeeOptionsDialogFragment extends DialogFragment implements View.OnClickListener {

    private final String eventId;

    // Constructor to pass eventId to the fragment
    public AttendeeOptionsDialogFragment(String eventId) {
        this.eventId = eventId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_dialog_attendee_options, null);

        Button btnAttendeeList = dialogView.findViewById(R.id.btnAttendeeList);
        Button btnCheckedInList = dialogView.findViewById(R.id.btnCheckedInList);

        btnAttendeeList.setOnClickListener(this);
        btnCheckedInList.setOnClickListener(this);

        builder.setView(dialogView);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAttendeeList) {
            openAttendeeList(eventId);
        } else if (v.getId() == R.id.btnCheckedInList) {
            openCheckedInList(eventId);
        }
        dismiss(); // Dismiss the dialog after an option is selected
    }


    private void openAttendeeList(String eventId) {
        if (eventId != null && !eventId.isEmpty()) {
            Intent intent = new Intent(requireContext(), AttendeeListActivity.class);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
        } else {
            Toast.makeText(requireContext(), "Event ID is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCheckedInList(String eventId) {
        if (eventId != null && !eventId.isEmpty()) {
            Intent intent = new Intent(requireContext(), CheckedInAttendeesActivity.class);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
        } else {
            Toast.makeText(requireContext(), "Event ID is missing", Toast.LENGTH_SHORT).show();
        }
    }

}

