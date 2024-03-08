package com.example.eventwiz;

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
 * @see EventBrief
 * @author Hunaid
 */
public class EventAdapter extends ArrayAdapter<EventBrief> {
    public EventAdapter(Context context, List<EventBrief> events) {

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
        EventBrief eventBrief = getItem(position);

        // Lookup view for data population
        TextView tvEventName = convertView.findViewById(R.id.tvEventName);
        TextView tvEventDateTime = convertView.findViewById(R.id.tvEventDateTime);
        ImageView imgEventPoster = convertView.findViewById(R.id.ivEventPoster);
        //TextView tvEventTimeRange = convertView.findViewById(R.id.tvEventTimeRange);

        //TextView tvEventVenue = convertView.findViewById(R.id.tvEventVenue);

        // Populate the data into the template view using the data object
        tvEventName.setText(eventBrief.getEventName());
        tvEventDateTime.setText("Date: " + eventBrief.getEventDate());
        //tvEventTimeRange.setText("Time: " + eventBrief.getEventTime());
        //tvEventVenue.setText("Venue: " + eventBrief.getVenue());
        Toast.makeText(this.getContext(), "Scanned: " + eventBrief.getPosterUrl(), Toast.LENGTH_LONG).show();
        Glide.with(getContext())
                .load(eventBrief.getPosterUrl())
                .into(imgEventPoster);

        //Button to go to event details

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle item click, e.g., navigate to another activity
                EventBrief clickedEvent = getItem(position);
                Intent intent = new Intent(EventAdapter.this.getContext(), ViewEventDetailsActivity.class);
                EventAdapter.this.getContext().startActivity(intent);
            }
        });

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