package com.example.eventwiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
        TextView tvAttendeeEmail = convertView.findViewById(R.id.tvAttendeeEmail);
        TextView tvAttendeeHomepage = convertView.findViewById(R.id.tvAttendeeHomepage);
        TextView tvAttendeeMobile = convertView.findViewById(R.id.tvAttendeeMobile);
        ImageView ivAttendeeProfilePic = convertView.findViewById(R.id.ivAttendeeProfilePic);

        tvAttendeeName.setText(attendee.getUserName());
        tvAttendeeEmail.setText(attendee.getUserEmail());
        tvAttendeeHomepage.setText(attendee.getUserHomepage());
        tvAttendeeMobile.setText(attendee.getUserMobile());
        Glide.with(getContext()).load(attendee.getProfilePicImage()).into(ivAttendeeProfilePic);

        return convertView;
    }

}
