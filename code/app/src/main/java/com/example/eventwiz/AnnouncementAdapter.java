package com.example.eventwiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class AnnouncementAdapter extends ArrayAdapter<Announcement> {
    private List<Announcement> announcements;

    public AnnouncementAdapter(Context context, List<Announcement> announcements) {
        super(context, 0, announcements);
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Announcement announcement = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_announcement, parent, false);
        }

        // Get references to views
        TextView tvAnnouncementTitle = convertView.findViewById(R.id.tvAnnouncementTitle);
        TextView tvAnnouncementDate = convertView.findViewById(R.id.tvAnnouncementDate);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        // Set data to views
        if (announcement != null) {
            // Set title
            tvAnnouncementTitle.setText(announcement.getTitle());

            tvAnnouncementDate.setText(announcement.getDate());
            tvDescription.setText(announcement.getDescription());
        }

        return convertView;
    }

    public Announcement getAnnouncement(int position) {
        return announcements.get(position);
    }
}


