
package com.example.eventwiz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

/**
 * This class is responsible for Saving data to the user profile
 * Works with FireBase to store data for each user
 * @author Yesith
 */
public class SaveUserProfileActivity extends AppCompatActivity {

    private EditText eduserName, eduserEmail, eduserHomepage, eduserMobile;

    private Button SaveProfileButton;
    private ImageView selectPhoto;
    public Uri imageUri;
    private Bitmap bitmap;
    private FirebaseStorage storage;

    private FirebaseFirestore firestore;

    private StorageReference userStorageRef;
    private String photoUrl;

    private FirebaseAuth userAuth;

    private String CurrentUserID;
    private String docID;

    SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_user_profile);

        // Insitantiate top support action bar
//        ActionBar actionBar = getSupportActionBar();
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle("EventWiz");
//            int color = ContextCompat.getColor(this, R.color.turqoise);
//
//            // Set the background color of the ActionBar
////            actionBar.setBackgroundDrawable(new ColorDrawable(color));
//        }

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        retrieveAnonymousUserId();

        eduserName = findViewById(R.id.editText_register_name);
        eduserEmail = findViewById(R.id.editText_register_email);
        eduserHomepage = findViewById(R.id.editText_homepage);
        eduserMobile = findViewById(R.id.editText_mobile);
        selectPhoto = findViewById(R.id.profile_pic_button);
        SaveProfileButton = findViewById(R.id.button_register);


        // create instances
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        userStorageRef = storage.getReference();

        userAuth = FirebaseAuth.getInstance();

        selectPhoto.setOnClickListener(new View.OnClickListener() {

            //check storage permission


            @Override
            public void onClick(View view) {
                //CheckStoragePermission();

                PickImageFromGallery();
            }
        });

        SaveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
                uploadUserInfo();

            }
        });



    }
    /*
    private void CheckStoragePermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL)
        }

    }
    */


    // get picture from gallery
    private void PickImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //launcher
        launcher.launch(intent);
    }

    ActivityResultLauncher<Intent> launcher
            =registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
            result -> {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            if (data != null && data.getData() != null) {
                                imageUri = data.getData();

                                // convert Image into BitMap
                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(
                                            this.getContentResolver(),
                                            imageUri
                                    );
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            //we set image into imageview
                            if (imageUri != null){
                                selectPhoto.setImageBitmap(bitmap);

                            }


                        }
                    }
    );

    // upload image to firebase

    //make a method to upload Image into firebase storage

    private void uploadImage(){
        //chekc imageuri
        if(imageUri != null){
            // create storage instance
            final StorageReference myRef = userStorageRef.child("photo/"+imageUri.getLastPathSegment());
            myRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // get downlaod URL to store in string
                    myRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if(uri != null){
                                photoUrl = uri.toString();
                                uploadUserInfo();
                            }
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SaveUserProfileActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void uploadUserInfo(){
        //get text from text edit
        String name=eduserName.getText().toString();
        String email=eduserEmail.getText().toString();
        String homepage=eduserHomepage.getText().toString();
        String mobile=eduserMobile.getText().toString();

        //
        if(TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(homepage) && TextUtils.isEmpty(mobile)){
            Toast.makeText(SaveUserProfileActivity.this,"Please Fill All Fields ", Toast.LENGTH_SHORT).show();
        }else{
            DocumentReference documentReference = firestore.collection("Users").document(CurrentUserID);
            //set all data into user class>>create class user
            UserProfile userProfile =new UserProfile(name,email,homepage,mobile,"",CurrentUserID,photoUrl);
            documentReference.set(userProfile, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        if(task.isSuccessful()){
                            //need to get doc id and set to user to store in firestore
                            docID = documentReference.getId();
                            userProfile.setUserDocID(docID);
                            // now this doc id will be sent into firestore
                            documentReference.set(userProfile, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SaveUserProfileActivity.this,"uploaded Successfully!",Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SaveUserProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });{

                            }
                        }

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SaveUserProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private void retrieveAnonymousUserId() {
        sp = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        CurrentUserID = sp.getString("anonymousUserId", null);

        if (CurrentUserID != null) {
            // Now, 'anonymousUserId' variable contains the anonymous user's ID
            Log.d("SharedPreferences", "Retrieved Anonymous User ID: " + CurrentUserID);
        } else {
            Log.e("SharedPreferences", "Failed to retrieve Anonymous User ID");
        }
    }








}