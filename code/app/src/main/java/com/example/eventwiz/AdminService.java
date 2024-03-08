package com.example.eventwiz;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * AdminService class provides methods to interact with Firebase Storage for admin-related tasks.
 * @author Hunaid
 */
public class AdminService {

    private StorageReference storageRef;

    /**
     * Constructs an instance of AdminService and initializes the storage reference.
     */
    public AdminService() {
        storageRef = FirebaseStorage.getInstance().getReference().child("event_posters");
    }

    /**
     * Fetches event posters from Firebase Storage and populates the provided image list, RecyclerView,
     * and ProgressBar with the fetched data.
     * @param imagelist    ArrayList to store URLs of fetched images.
     * @param recyclerView RecyclerView to display the fetched images.
     * @param adapter      Adapter for the RecyclerView.
     * @param progressBar  ProgressBar to show loading progress.
     */
    public void fetchEventPosters(ArrayList<String> imagelist, RecyclerView recyclerView, ImageAdapter adapter, ProgressBar progressBar){
        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for(StorageReference file:listResult.getItems()){
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imagelist.add(uri.toString());
                            Log.e("Itemvalue",uri.toString());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    /**
     * Deletes a poster image from Firebase Storage.
     *
     * @param imageUrl URL of the image to be deleted.
     * @param listener  Listener to handle deletion completion or failure.
     */
    public void deletePoster(String imageUrl, OnDeletionCompleteListener listener) {
        StorageReference fileRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        fileRef.delete()
                .addOnSuccessListener(aVoid -> listener.onDeletionComplete())
                .addOnFailureListener(e -> listener.onDeletionFailed(e));
    }

    /**
     * Interface to handle completion or failure of image deletion.
     */
    public interface OnDeletionCompleteListener {
        void onDeletionComplete();
        void onDeletionFailed(Exception e);
    }

}
