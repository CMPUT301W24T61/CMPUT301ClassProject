package com.example.eventwiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;

public class AdminBrowseUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private AdminService adminService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_browse_users);

        recyclerView = findViewById(R.id.users_recycler_view); // Ensure this ID matches your layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adminService = new AdminService();
        userAdapter = new UserAdapter(new ArrayList<>(), userProfile -> showConfirmationDialog(userProfile));
        recyclerView.setAdapter(userAdapter);

        adminService.fetchUserProfiles(userProfiles -> {
            userAdapter.setUserProfiles(userProfiles);
            userAdapter.notifyDataSetChanged();
        });
    }

    private void showConfirmationDialog(UserProfile userProfile) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this user?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteUserProfile(userProfile);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void deleteUserProfile(UserProfile userProfile) {
        adminService.removeUserByDocId(userProfile.getUserDocID(), new AdminService.OnDeletionCompleteListener() {
            @Override
            public void onDeletionComplete() {
                // User deleted successfully, update UI accordingly
                userAdapter.removeUserProfile(userProfile);
            }

            @Override
            public void onDeletionFailed(Exception e) {
                // Handle the deletion failure
            }
        });
    }
}
