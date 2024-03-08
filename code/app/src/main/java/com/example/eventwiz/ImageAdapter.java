package com.example.eventwiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * ImageAdapter is a RecyclerView adapter responsible for displaying images in a grid layout.
 * @author Hunaid
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<String> imageList;

    private OnItemClickListener listener;

    /**
     * Interface definition for a callback to be invoked when an item in the image list is clicked.
     */
    public interface OnItemClickListener {
        void onItemClick(String imageUrl);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Constructs an ImageAdapter with the provided image list and context.
     *
     * @param imageList The list of image URLs to display.
     * @param context   The context in which the adapter will be used.
     */
    public ImageAdapter(ArrayList<String> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    private Context context;
    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_image,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        // loading the images from the position
//        Glide.with(holder.itemView.getContext()).load(imageList.get(position)).into(holder.imageView);

        String imageUrl = imageList.get(position);
        Glide.with(holder.itemView.getContext()).load(imageUrl).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(imageUrl);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    /**
     * ViewHolder class for holding the views of a grid item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        /**
         * Constructs a ViewHolder with the given itemView.
         *
         * @param itemView The item view to hold.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item);
        }
    }
}
