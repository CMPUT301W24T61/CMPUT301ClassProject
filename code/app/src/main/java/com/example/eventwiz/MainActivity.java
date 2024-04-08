package com.example.eventwiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth userAuth;
    private SharedPreferences sp;
    private String uid;

    private TextView gpsStatus;
    private LocationManager locationManager;

    // Flag to track if profile creation dialog has been shown
    private boolean profileCreationDialogShown = false;

    private Button buttonRegister;

    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userAuth = FirebaseAuth.getInstance();
        sp = getSharedPreferences("MyPrefs", MODE_PRIVATE);


        gpsStatus = findViewById(R.id.gps_status);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        loadingProgressBar = findViewById(R.id.loading_progress_bar);

        Button buttonGetStarted = findViewById(R.id.button_get_started);
        buttonGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = userAuth.getCurrentUser();
                if (currentUser != null) {
                    uid = currentUser.getUid();
                    checkUserProfileExists(uid);
                } else {
                    Toast.makeText(MainActivity.this, "User is not signed in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateFCMTokenInFirestore();

        buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SaveUserProfileActivity.class);
                startActivity(intent);
            }
        });

        Button buttonScanQR = findViewById(R.id.button_scan_qr);
        buttonScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = userAuth.getCurrentUser();
                if (currentUser != null) {
                    uid = currentUser.getUid();
                    checkUserProfileExistsForQRScan(uid);
                } else {
                    Toast.makeText(MainActivity.this, "User is not signed in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Check GPS status asynchronously
        new CheckGpsStatusTask().execute();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check user authentication status asynchronously
        new CheckUserAuthStatusTask().execute();

        // Check if anonymous userID is saved
        checkIfAnonymousUserIdSaved();

        FirebaseUser currentUser = userAuth.getCurrentUser();
        if (currentUser != null) {
            uid = currentUser.getUid();
            onStartProfileButtonVisibility(uid);
        }
    }

    private void checkIfAnonymousUserIdSaved() {
        // Retrieve the anonymous userID from SharedPreferences
        String savedUserId = sp.getString("anonymousUserId", null);

        // Check if the saved userID is not null
        if (savedUserId != null) {
            // Anonymous UserID is saved, you can log it or display it
            Log.d("SharedPreferences", "Anonymous UserID found: " + savedUserId);
        } else {
            // Anonymous UserID is not saved
            Log.d("SharedPreferences", "No Anonymous UserID found");
        }
    }

    private void checkUserProfileExists(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(uid); // Change "users" to your collection where user profiles are stored
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // User document exists, now check if userName and userEmail exist
                        String userName = document.getString("userName");
                        String userEmail = document.getString("userEmail");
                        if (userName != null && userEmail != null) {
                            // Both userName and userEmail exist
                            // User profile is complete
                            if (!profileCreationDialogShown) {
                                // Show dialog only if it hasn't been shown before
                                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                                startActivity(intent);

                            }
                        } else {
                            // Either userName or userEmail is missing
                            // Show dialog to create profile
                            if (!profileCreationDialogShown) {
                                showCreateProfileDialog();
                                profileCreationDialogShown = true;

                            }
                        }
                    } else {
                        // User document does not exist
                        // Show dialog to create profile
                        if (!profileCreationDialogShown) {
                            showCreateProfileDialog();
                            profileCreationDialogShown = true;

                        }
                    }
                } else {
                    // Handle error
                    Log.e("Firestore", "Error checking user profile existence: ", task.getException());
                }
            }
        });
    }

    private void checkUserProfileExistsForQRScan(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(uid); // Change "users" to your collection where user profiles are stored
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // User document exists
                        Intent intent = new Intent(MainActivity.this, QRCodeScannerActivity.class);
                        startActivity(intent);
                    } else {
                        // User document does not exist
                        showCreateProfileDialogForQRScan();
                    }
                } else {
                    // Handle error
                    Log.e("Firestore", "Error checking user profile existence: ", task.getException());
                }
            }
        });
    }

    private void onStartProfileButtonVisibility(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(uid); // Change "users" to your collection where user profiles are stored
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // User document exists, hide the "Create Profile" button
                        buttonRegister.setVisibility(View.GONE);
                    } else {
                        // User document does not exist, show the "Create Profile" button
                        buttonRegister.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Handle error
                    Log.e("Firestore", "Error checking user profile existence: ", task.getException());
                }
            }
        });
    }

    private void showCreateProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No User Profile Found!");
        builder.setMessage("Create a Profile to Get Started.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // prompt to main UI to create profile
                        dialogInterface.dismiss();
                        profileCreationDialogShown = false;


                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showCreateProfileDialogForQRScan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No User Profile Found!");
        builder.setMessage("Create a Profile to Access this Feature.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing or navigate to profile creation activity
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void attemptAnonymousAuthentication() {
        // Show loading ProgressBar
        loadingProgressBar.setVisibility(View.VISIBLE);
        userAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loadingProgressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    FirebaseUser user = userAuth.getCurrentUser();
                    if (user != null) {
                        uid = user.getUid();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("anonymousUserId", uid);
                        editor.apply();
                        Log.d("Authentication", "Anonymous authentication successful. UID: " + uid);
                        updateUI(user);
                        Log.d("SharedPreferences", "Saved Anonymous User ID: " + uid);

                        // Debug: Check if UID is successfully retrieved from SharedPreferences
                        String retrievedUid = sp.getString("anonymousUserId", null);
                        if (retrievedUid != null) {
                            Log.d("SharedPreferences", "Retrieved Anonymous User ID: " + retrievedUid);
                        } else {
                            Log.e("SharedPreferences", "Failed to retrieve Anonymous User ID from SharedPreferences");
                        }
                    }
                } else {
                    Log.e("Authentication", "Anonymous authentication failed: " + task.getException());
                    updateUI(null);
                    Toast.makeText(MainActivity.this, "Anonymous authentication failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.d("Authentication", "User is signed in. UID: " + user.getUid());
            Toast.makeText(MainActivity.this, "Signed in anonymously", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("Authentication", "User is still not signed in.");
            Toast.makeText(MainActivity.this, "User is not signed in", Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonSwitchGPS(View view) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            turnOffGPS();
        } else {
            turnOnGPS();
        }
    }

    private void turnOnGPS() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Location is disabled. Would you like to enable it?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        updateGPSStatus(true); // Update GPS status text when GPS is turned on
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void turnOffGPS() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Location is enabled. Would you like to disable it?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        updateGPSStatus(false); // Update GPS status text when GPS is turned off
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void updateGPSStatus(boolean isGpsEnabled) {
        gpsStatus.setText(isGpsEnabled ? "Location ON" : "Location OFF");
    }

    // AsyncTask to check GPS status
    private class CheckGpsStatusTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        @Override
        protected void onPostExecute(Boolean isGpsEnabled) {
            updateGPSStatus(isGpsEnabled);
        }
    }

    // AsyncTask to check user authentication status
    private class CheckUserAuthStatusTask extends AsyncTask<Void, Void, FirebaseUser> {

        @Override
        protected FirebaseUser doInBackground(Void... voids) {
            return userAuth.getCurrentUser();
        }

        @Override
        protected void onPostExecute(FirebaseUser currentUser) {
            if (currentUser == null) {
                Log.d("Authentication", "User is not signed in. Attempting anonymous authentication.");
                attemptAnonymousAuthentication();
            } else {
                Log.d("Authentication", "User is already signed in. UID: " + currentUser.getUid());
                updateUI(currentUser);
            }
        }

    }

    private void updateFCMTokenInFirestore() {
        // Retrieve FCM token
        FirebaseInstallations.getInstance().getToken(/* forceRefresh = */ true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        InstallationTokenResult tokenResult = task.getResult();
                        String fcmToken = tokenResult.getToken();
                        // Update the FCM token in Firestore
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserId);
                            userRef.update("fcmToken", fcmToken)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "FCM token updated successfully"))
                                    .addOnFailureListener(e -> Log.e(TAG, "Error updating FCM token: " + e.getMessage(), e));
                        }
                    } else {
                        Log.e(TAG, "Failed to get Firebase Cloud Messaging token: " + task.getException().getMessage());
                    }
                });
    }
}


