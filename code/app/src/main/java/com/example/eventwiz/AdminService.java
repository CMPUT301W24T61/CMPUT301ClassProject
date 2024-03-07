package com.example.eventwiz;

import android.media.tv.TvContract;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.List;
import java.util.ArrayList;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageException;

public class AdminService {

    private StorageReference storageRef;

    public AdminService() {
        storageRef = FirebaseStorage.getInstance().getReference().child("event_posters");
    }

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

    public void deletePoster(String imageUrl, OnDeletionCompleteListener listener) {
        StorageReference fileRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        fileRef.delete()
                .addOnSuccessListener(aVoid -> listener.onDeletionComplete())
                .addOnFailureListener(e -> listener.onDeletionFailed(e));
    }

    public interface OnDeletionCompleteListener {
        void onDeletionComplete();
        void onDeletionFailed(Exception e);
    }

}
