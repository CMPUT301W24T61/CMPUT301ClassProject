package com.example.eventwiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

public class AttendeeAdapter extends ArrayAdapter<UserProfile> {


    public AttendeeAdapter(Context context, List<UserProfile> attendees) {
        super(context, 0, attendees);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        UserProfile attendee = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_attendee, parent, false);
        }

        // Get references to views
        ImageView ivUserProfileImage = convertView.findViewById(R.id.ivAttendeeProfilePic);
        TextView tvAttendeeName = convertView.findViewById(R.id.tvAttendeeName);
        TextView tvAttendeeEmail = convertView.findViewById(R.id.tvAttendeeEmail);
        TextView tvAttendeeMobile = convertView.findViewById(R.id.tvAttendeeMobile);

        // Set data to views
        if (attendee != null) {
            // Set user profile image
            if (attendee.getProfilePicImage() != null) {
                // Assuming getProfilePicImage() returns a URL or URI, you can use Glide to load it into the ImageView
                Glide.with(getContext()).load(attendee.getProfilePicImage()).into(ivUserProfileImage);
            } else {
                // Set a default image if user profile image is null
                ivUserProfileImage.setImageResource(R.drawable.ic_default_profile_icon);
            }

            // Set user name
            tvAttendeeName.setText(attendee.getUserName());

            // Set user email
            tvAttendeeEmail.setText(attendee.getUserEmail());

            //Set Mobile
            tvAttendeeMobile.setText(attendee.getUserMobile());
        }

        return convertView;
    }
}

