package com.example.eventwiz;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

// Assuming Event is a class that holds event details
public class OrganizerService {

    private Context context;

    public OrganizerService(Context context) {
        this.context = context;
    }

    /**
     * Loads and displays the event details on the UI elements.
     * If the event has associated images (poster, QR codes), they are loaded using Glide.
     */
    public void loadEventDetails(Event event, ImageView ivEventPoster, ImageView ivCheckInQRCode, ImageView ivPromotionQRCode, TextView... textViews) {
        if (event != null) {
            textViews[0].setText(event.getName()); // Assuming textViews[0] is tvEventName
            textViews[1].setText("Date: " + event.getDate()); // Assuming textViews[1] is tvEventDate
            // and so on for other text views...

            if (event.getPosterUrl() != null && !event.getPosterUrl().isEmpty()) {
                Glide.with(context).load(event.getPosterUrl()).into(ivEventPoster);
            } else {
                ivEventPoster.setImageResource(R.drawable.image_placeholder_background);
            }
            if (event.getCheckInQRCode() != null && !event.getCheckInQRCode().isEmpty()) {
                Glide.with(context).load(event.getCheckInQRCode()).into(ivCheckInQRCode);
            } else {
                ivCheckInQRCode.setVisibility(View.GONE);
            }

            if (event.getPromotionQRCode() != null && !event.getPromotionQRCode().isEmpty()) {
                Glide.with(context).load(event.getPromotionQRCode()).into(ivPromotionQRCode);
            } else {
                ivPromotionQRCode.setVisibility(View.GONE);
            }
        }
    }
}
