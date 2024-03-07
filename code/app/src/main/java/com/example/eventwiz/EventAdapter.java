package com.example.eventwiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * This class is responsible for handling the adapter for events.
 * @see Event
 * @author Hunaid
 */
public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(Context context, List<Event> events) {
        super(context, 0, events);
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
        //TextView tvEventTimeRange = convertView.findViewById(R.id.tvEventTimeRange);
        //extView tvEventVenue = convertView.findViewById(R.id.tvEventVenue);

        // Populate the data into the template view using the data object
        tvEventName.setText(event.getEventName());
        tvEventDateTime.setText("Date: " + event.getEventDate());
//        tvEventTimeRange.setText("Time: " + event.getEventTime());
//        tvEventVenue.setText("Venue: " + event.getVenue());
        Toast.makeText(this.getContext(), "Scanned: " + event.getPosterUrl(), Toast.LENGTH_LONG).show();
        setImageFromFirebaseUrl(imgEventPoster, event.getPosterUrl());
//        Glide.with(getContext())
//                .load(event.getPosterUrl())
//                .into(imgEventPoster);

        // Return the completed view to render on screen
        return convertView;
    }

    private void setImageFromFirebaseUrl(ImageView imgEventPoster, String imageUrl) {
        // Using Glide to load the image into the ImageView
        Glide.with(this.getContext())
                .load(imageUrl)
                .into(imgEventPoster);
    }
}