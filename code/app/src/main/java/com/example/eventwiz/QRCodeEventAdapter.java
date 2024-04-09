package com.example.eventwiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class QRCodeEventAdapter extends ArrayAdapter<QRCodeEventDetail> {
    private Context context;
    private List<QRCodeEventDetail> details;

    public QRCodeEventAdapter(@NonNull Context context, @NonNull List<QRCodeEventDetail> objects) {
        super(context, 0, objects);
        this.context = context;
        this.details = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.qr_code_event_item, parent, false);
        }

        QRCodeEventDetail detail = details.get(position);

        TextView eventName = convertView.findViewById(R.id.eventName);
        ImageView eventPoster = convertView.findViewById(R.id.eventPoster);
        ImageView checkInQRCodeImage = convertView.findViewById(R.id.checkInQRCodeImage); // Existing Check-in QR Code
        ImageView promotionQRCodeImage = convertView.findViewById(R.id.promotionQRCodeImage); // New Promotion QR Code

        eventName.setText(detail.getEventName());
        Glide.with(context).load(detail.getEventPosterUrl()).into(eventPoster);
        Glide.with(context).load(detail.getCheckInQRCodeUrl()).into(checkInQRCodeImage); // Load Check-in QR Code
        Glide.with(context).load(detail.getPromotionQRCodeUrl()).into(promotionQRCodeImage); // Load Promotion QR Code

        return convertView;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }


}
