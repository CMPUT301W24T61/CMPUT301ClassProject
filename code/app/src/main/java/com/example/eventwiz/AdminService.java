package com.example.eventwiz;

import android.media.tv.TvContract;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import java.util.List;
import java.util.ArrayList;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageException;

/**
 * AdminService class provides methods to interact with Firebase Storage for admin-related tasks.
 * @author Hunaid
 */
public class AdminService {

    private StorageReference storageRef;
    private FirebaseFirestore firestoreDb;

    /**
     * Constructs an instance of AdminService and initializes the storage reference.
     */
    public AdminService() {
        storageRef = FirebaseStorage .getInstance().getReference().child("event_posters");
        firestoreDb = FirebaseFirestore.getInstance();
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


    public void removeEvent(String eventId, OnDeletionCompleteListener listener) {
        // Delete the event data from Firestore
        firestoreDb.collection("events").document(eventId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Optionally, delete associated poster from Firebase Storage
                    // You can use the deletePoster method if you have the URL of the poster
                    listener.onDeletionComplete();
                })
                .addOnFailureListener(e -> listener.onDeletionFailed(e));
    }

    public void fetchUserProfiles(OnUsersFetchedListener listener) {
        firestoreDb.collection("Users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<UserProfile> userProfiles = queryDocumentSnapshots.toObjects(UserProfile.class);
                    listener.onUsersFetched(userProfiles);
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }

    public void removeUserByDocId(String userDocID, OnDeletionCompleteListener listener) {
        firestoreDb.collection("Users").document(userDocID)
                .delete()
                .addOnSuccessListener(aVoid -> listener.onDeletionComplete())
                .addOnFailureListener(listener::onDeletionFailed);
    }

    public interface OnUsersFetchedListener {
        void onUsersFetched(List<UserProfile> userProfiles);
    }


    /**
     * Interface to handle completion or failure of image deletion.
     */
    public interface OnDeletionCompleteListener {
        void onDeletionComplete();
        void onDeletionFailed(Exception e);
    }

}
