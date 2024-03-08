package com.example.eventwiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * AdminBrowseEventPosters is an activity that allows administrators to browse event posters stored in Firebase Storage.
 * @author Hunaid
 */
public class AdminBrowseImages extends AppCompatActivity {
    private ArrayList<String> imagelist;
    private RecyclerView recyclerView;
    private StorageReference root;
    private ProgressBar progressBar;
    private ImageAdapter adapter;
    private AdminService adminService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_browse_images);
        imagelist=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerview);
        adapter=new ImageAdapter(imagelist,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));
        progressBar=findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);


        adminService= new AdminService();
        adminService.fetchEventPosters(imagelist, recyclerView, adapter, progressBar);
//
        adapter.setOnItemClickListener(imageUrl -> {
            // Confirm before deleting
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this poster?")
                    .setPositiveButton("Delete", (dialog, which) -> deletePoster(imageUrl))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    /**
     * Deletes the specified poster image from Firebase Storage.
     *
     * @param imageUrl URL of the image to be deleted.
     */
    private void deletePoster(String imageUrl) {
        adminService.deletePoster(imageUrl, new AdminService.OnDeletionCompleteListener() {
            @Override
            public void onDeletionComplete() {
                // Remove the image from the list and notify the adapter
                imagelist.remove(imageUrl);
                adapter.notifyDataSetChanged();
                Toast.makeText(AdminBrowseImages.this, "Poster deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeletionFailed(Exception e) {
                Toast.makeText(AdminBrowseImages.this, "Failed to delete poster", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
