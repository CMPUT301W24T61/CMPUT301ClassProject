package com.example.eventwiz;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.ActionBar;

/**
 * This class will handle button presses from the main screen and will call other classes
 * and activities as necessary
 * @author Hunaid
 * Will need to update this with @see for classes as they are created.
 */
public class MainActivity extends AppCompatActivity {
    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("EventWiz");
        }


        userAuth = FirebaseAuth.getInstance();


        Button buttonBrowseEvents = findViewById(R.id.button_browse_events);
        buttonBrowseEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BrowseEventsActivity.class);
                startActivity(intent);

            }
        });

//        Button buttonRegister = findViewById(R.id.button_register);

//        buttonRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, SaveUserProfileActivity.class);
//                startActivity(intent);
//
//            }
//        });

//        buttonRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(intent);
//
//            }
//        });


        Button buttonScanQR = findViewById(R.id.button_scan_qr);
        buttonScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QRCodeScannerActivity.class);
                startActivity(intent);
            }
        });


        // Find the camera button (ImageView) by ID
//        ImageView cameraButton = findViewById(R.id.button_open_camera);
//
//        // Set an OnClickListener for the camera button (ImageView)
//        cameraButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Handle camera button click by opening the camera
//                openCamera();
//            }
//        });
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

    @Override
    public void onStart(){
        super.onStart();

        //check if user is signed-in)non-null) and update UI
        FirebaseUser currentUser = userAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser user){
        if (user==null){
            userAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = userAuth.getCurrentUser();
                        updateUI(user);
                    }else{
                       updateUI(null);
                    }
                }
            });
        }
    }








}