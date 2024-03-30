package com.example.eventwiz;

import android.content.Context;
import android.util.Pair;
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

public class CheckedInAttendeeAdapter extends ArrayAdapter<Pair<UserProfile, Integer>> {
    private int resourceLayout;
    private Context mContext;

    public CheckedInAttendeeAdapter(Context context, int resource, List<Pair<UserProfile, Integer>> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Pair<UserProfile, Integer> userProfilePair = getItem(position);
        UserProfile userProfile = userProfilePair.first;
        Integer checkIns = userProfilePair.second;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(this.resourceLayout, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tvAttendeeName);
        TextView tvEmail = convertView.findViewById(R.id.tvAttendeeEmail);
        TextView tvCheckInCount = convertView.findViewById(R.id.tvCheckInCount);
        ImageView ivProfilePic = convertView.findViewById(R.id.ivAttendeeProfilePic);

        tvName.setText(userProfile.getUserName());
        tvEmail.setText(userProfile.getUserEmail());
        tvCheckInCount.setText("Check-ins: " + checkIns);
        Glide.with(mContext).load(userProfile.getProfilePicImage()).into(ivProfilePic);

        return convertView;
    }
}

