package com.example.eventwiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class AttendeeAdapter extends ArrayAdapter<UserProfile> {

    public AttendeeAdapter(Context context, List<UserProfile> attendees) {
        super(context, 0, attendees);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserProfile attendee = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_attendee, parent, false);
        }
        TextView tvAttendeeName = convertView.findViewById(R.id.tvAttendeeName);
        tvAttendeeName.setText(attendee.getUserName());
        return convertView;
    }
}
