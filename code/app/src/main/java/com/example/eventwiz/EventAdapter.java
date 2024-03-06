package com.example.eventwiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(Context context, List<Event> events) {
        super(context, 0, events);
    }

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
//        TextView tvEventTimeRange = convertView.findViewById(R.id.tvEventTimeRange);
//        TextView tvEventVenue = convertView.findViewById(R.id.tvEventVenue);

        // Populate the data into the template view using the data object
        tvEventName.setText(event.getEventName());
        tvEventDateTime.setText("Date: " + event.getEventDate());
//        tvEventTimeRange.setText("Time: " + event.getEventTime());
//        tvEventVenue.setText("Venue: " + event.getVenue());
        Toast.makeText(this.getContext(), "Scanned: " + event.getPosterUrl(), Toast.LENGTH_LONG).show();
//        Glide.with(getContext())
//                .load(event.getPosterUrl())
//                .into(imgEventPoster);


        // Return the completed view to render on screen
        return convertView;
    }
}
