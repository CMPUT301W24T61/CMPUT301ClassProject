package com.example.eventwiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * This class is responsible for handling the adapter for events.
 * @see Event
 * @author Hunaid,Yesith
 */

public class EventAdapter extends ArrayAdapter<Event> {
    private EventClickListener eventClickListener;
    public EventAdapter(Context context, List<Event> events,EventClickListener eventClickListener) {
        super(context, 0, events);
        this.eventClickListener = eventClickListener;
    }

    /**
     * This class is used to get and return the view for the events.
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }

        // Get the data item for this position
        Event event = getItem(position);

        // Lookup view for data population
        TextView tvEventName = convertView.findViewById(R.id.tvEventName);
        TextView tvEventDateTime = convertView.findViewById(R.id.tvEventDateTime);
        ImageView imgEventPoster = convertView.findViewById(R.id.ivEventPoster);


        // Populate the data into the template view using the data object
        tvEventName.setText(event.getName());
        tvEventDateTime.setText(String.format("Date: %s Time: %s - %s", event.getDate(), event.getStartTime(), event.getEndTime()));
        Glide.with(getContext()).load(event.getPosterUrl()).into(imgEventPoster);

        // Click listener to navigate to event details

        convertView.setOnClickListener(view -> {
            if(eventClickListener != null) {
                eventClickListener.onEventClicked(event);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
    public interface EventClickListener {
        void onEventClicked(Event event);
    }

    private void showConfirmationDialog(Event event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Delete", null) // Set to null. We override the onClick later.
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                AttendeeService.removeEvent(event.getId(), success -> {
                    if (success) {
                        remove(event);
                        notifyDataSetChanged();
                        Toast.makeText(getContext(), "Event deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error deleting event", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss(); // Dismiss the dialog after the operation
                });
            });
        });

        dialog.show();
    }


    private void setImageFromFirebaseUrl(ImageView imgEventPoster, String imageUrl) {
        // Using Glide to load the image into the ImageView
        Glide.with(this.getContext())
                .load(imageUrl)
                .into(imgEventPoster);
    }
}