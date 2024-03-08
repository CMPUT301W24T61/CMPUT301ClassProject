package com.example.eventwiz;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;


import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



/**
 * MainActivity class handles button presses from the main screen and calls other classes and activities.
 * It also manages user authentication and GPS status.
 * @author yesith
 */
public class MainActivity extends AppCompatActivity {
    private FirebaseAuth userAuth;
    SharedPreferences sp;
    String uid;

    private TextView gpsStatus;
    private LocationManager locationManager;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState Bundle that contains data most recently supplied if the activity is being re-initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        userAuth = FirebaseAuth.getInstance();
        uid = userAuth.getUid();
        sp = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("anonymousUserId", uid);
        editor.apply();
        Log.d("SharedPreferences", "Saved Anonymous User ID: " + uid);

        gpsStatus = findViewById(R.id.gps_status);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);



        Button buttonGetStarted = findViewById(R.id.button_get_started);
        buttonGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);

            }
        });

        Button create_profile_btn = findViewById(R.id.create_profile_btn);

        create_profile_btn.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(MainActivity.this, QRCodeScannerActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Called when the activity is about to become visible to the user.
     */
    @Override
    public void onStart() {
        super.onStart();

        // Check if the user is signed in (non-null) and update UI
        FirebaseUser currentUser = userAuth.getCurrentUser();
        if (currentUser == null) {
            // If the user is not signed in, attempt anonymous authentication
            Log.d("Authentication", "User is not signed in. Attempting anonymous authentication.");
            attemptAnonymousAuthentication();
        } else {
            // If the user is already signed in, update UI
            Log.d("Authentication", "User is already signed in. UID: " + currentUser.getUid());
            updateUI(currentUser);
        }

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            gpsStatus.setText("GPS is ON");

        }else{
            gpsStatus.setText("GPS is OFF");
        }
    }
    /**
     * Attempt anonymous authentication using Firebase.
     */
    private void attemptAnonymousAuthentication() {
        userAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // If anonymous authentication is successful, get the current user
                    FirebaseUser user = userAuth.getCurrentUser();
                    Log.d("Authentication", "Anonymous authentication successful. UID: " + user.getUid());
                    updateUI(user);
                } else {
                    // If anonymous authentication fails, update UI accordingly and show a toast
                    Log.e("Authentication", "Anonymous authentication failed: " + task.getException());
                    updateUI(null);
                    Toast.makeText(MainActivity.this, "Anonymous authentication failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Update UI based on the current user's status.
     *
     * @param user FirebaseUser object representing the current user.
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Update UI for a signed-in user
            Log.d("Authentication", "User is signed in. UID: " + user.getUid());
            Toast.makeText(MainActivity.this, "Signed in anonymously", Toast.LENGTH_SHORT).show();
            // You can navigate to another activity or perform additional actions here
        } else {
            // Handle the case when the user is still not signed in after attempting anonymous authentication
            Log.d("Authentication", "User is still not signed in.");
            Toast.makeText(MainActivity.this, "User is not signed in", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Called when the user clicks the switch GPS button.
     *
     * @param view The view that was clicked.
     */
    public void buttonSwitchGPS(View view) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS is currently ON, turn it OFF
            turnOffGPS();
        } else {
            // GPS is currently OFF, turn it ON
            turnOnGPS();
        }
    }

    /**
     * Show a dialog to enable GPS when it is currently disabled.
     */
    private void turnOnGPS() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled. Would you like to enable it?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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
    /**
     * Show a dialog to disable GPS when it is currently enabled.
     */
    private void turnOffGPS() {
        // Check if the user wants to enable GPS
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is enabled. Would you like to disable it?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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

}