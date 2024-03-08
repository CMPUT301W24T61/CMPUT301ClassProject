package com.example.eventwiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<UserProfile> userProfiles;
    private OnItemClickListener onItemClickListener;


    public UserAdapter(List<UserProfile> userProfiles, OnItemClickListener onItemClickListener) {
        this.userProfiles = userProfiles;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each list item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind the UserProfile object to the holder
        UserProfile userProfile = userProfiles.get(position);
        holder.bind(userProfile);
    }

    @Override
    public int getItemCount() {
        // Return the size of the dataset
        return userProfiles.size();
    }

    public void setUserProfiles(List<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfilePic;
        TextView userNameTextView;
        TextView userEmailTextView;
        TextView userHomepageTextView;
        TextView userPhoneTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize view elements
            userProfilePic = itemView.findViewById(R.id.user_profile_pic);
            userNameTextView = itemView.findViewById(R.id.user_name_text_view);
            userEmailTextView = itemView.findViewById(R.id.user_email_text_view);
            userHomepageTextView = itemView.findViewById(R.id.user_homepage_text_view);
            userPhoneTextView = itemView.findViewById(R.id.user_phone_text_view);

            itemView.setOnClickListener(v -> onItemClickListener.onItemClicked(userProfiles.get(getAdapterPosition())));
        }

        void bind(UserProfile userProfile) {
            // Set the data to view elements
            userNameTextView.setText(userProfile.getUserName());
            userEmailTextView.setText(userProfile.getUserEmail());
            userHomepageTextView.setText(userProfile.getUserHomepage());
            userPhoneTextView.setText(userProfile.getUserMobile());

            // Load the profile picture
            if (userProfile.getProfilePicImage() != null && !userProfile.getProfilePicImage().isEmpty()) {
                // Use your preferred image loading library (e.g., Glide or Picasso) to load the image
                Glide.with(itemView.getContext())
                        .load(userProfile.getProfilePicImage())
                        .placeholder(R.drawable.baseline_add_a_photo_24) // Replace with your placeholder drawable
                        .into(userProfilePic);
            } else {
                // Set a default image or placeholder
                userProfilePic.setImageResource(R.drawable.baseline_add_a_photo_24); // Replace with your placeholder drawable
            }
        }
    }

    public void removeUserProfile(UserProfile userProfile) {
        int position = userProfiles.indexOf(userProfile);
        if (position > -1) {
            userProfiles.remove(position);
            notifyItemRemoved(position);
        }
    }

    interface OnItemClickListener {
        void onItemClicked(UserProfile userProfile);
    }
}

