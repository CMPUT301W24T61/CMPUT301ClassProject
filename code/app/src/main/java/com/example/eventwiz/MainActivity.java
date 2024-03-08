package com.example.eventwiz;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This class will handle button presses from the main screen and will call other classes
 * and activities as necessary
 * @author Hunaid
 *
 */
public class MainActivity extends AppCompatActivity {
    private FirebaseAuth userAuth;
    SharedPreferences sp;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("EventWiz");
        }


        userAuth = FirebaseAuth.getInstance();
        uid = userAuth.getUid();
        sp = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("anonymousUserId", uid);
        editor.commit();
        Log.d("SharedPreferences", "Saved Anonymous User ID: " + uid);


        Button buttonBrowseEvents = findViewById(R.id.button_browse_events);
        buttonBrowseEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BrowseEventsActivity.class);
                startActivity(intent);

            }
        });

//        Button buttonRegister = findViewById(R.id.button_register);

//        buttonBrowseEvents.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, BrowseEventsActivity.class);
//                startActivity(intent);
//
//            }
//        });

        Button buttonRegister = findViewById(R.id.button_register);

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
                Intent intent = new Intent(MainActivity.this, QRCodeScannerActivity.class);
                startActivity(intent);
            }
        });


        // Find the camera button (ImageView) by ID
        ImageView cameraButton = findViewById(R.id.button_open_camera);

        // Set an OnClickListener for the camera button (ImageView)
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle camera button click by opening the camera
                openCamera();
            }
        });
    }

    // Method to open the camera
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check if there is a camera app available
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(cameraIntent);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This is the on start function when the app is loaded. Handles loading the main page and connecting to FireBase
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
    }

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

    // Method to save anonymous user's UID to SharedPreferences











}