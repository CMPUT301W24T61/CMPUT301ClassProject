package com.example.eventwiz;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class EnlargeImageFragment extends DialogFragment {

    private static final String IMAGE_URL = "imageUrl";

    public EnlargeImageFragment() {
    }

    public static EnlargeImageFragment newInstance(String imageUrl) {
        EnlargeImageFragment fragment = new EnlargeImageFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enlarge_image, container, false);
        if (getArguments() != null) {
            String imageUrl = getArguments().getString(IMAGE_URL);
            Log.d("EnlargeImageFragment", "Received image URL: " + imageUrl);
            ImageView imageView = view.findViewById(R.id.image);
            Glide.with(this)
                    .load(imageUrl)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("EnlargeImageFragment", "Error loading image", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.d("EnlargeImageFragment", "Image loaded successfully");
                            return false;
                        }
                    })
                    .into(imageView);
        } else {
            Log.e("EnlargeImageFragment", "Arguments are null.");
        }
        return view;
    }
}
