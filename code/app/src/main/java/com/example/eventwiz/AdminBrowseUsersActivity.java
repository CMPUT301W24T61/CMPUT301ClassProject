package com.example.eventwiz;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
/**
 * AdminBrowseUsersActivity is responsible for displaying and managing user profiles
 * in the admin panel.
 *
 * This activity allows administrators to browse user profiles, confirm deletions,
 * and interact with the AdminService to perform user-related actions.
 * @author Hunaid
 */
public class AdminBrowseUsersActivity extends AppCompatActivity {

    /**
     * RecyclerView to display the list of user profiles.
     */
    private RecyclerView recyclerView;

    /**
     * Adapter for managing and displaying user profiles in the RecyclerView.
     */
    private UserAdapter userAdapter;
    /**
     * Service for handling administrative actions related to user profiles.
     */
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

    /**
     * Displays a confirmation dialog for user profile deletion.
     *
     * @param userProfile The UserProfile object representing the user to be deleted.
     */
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

    /**
     * Deletes the specified user profile using the AdminService.
     *
     * @param userProfile The UserProfile object representing the user to be deleted.
     */
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
