package com.example.eventwiz;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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
 * The SaveUserProfileActivity class handles the user profile creation process, including
 * uploading user information and profile picture to Firebase Firestore and Storage.
 *
 * This activity allows the user to enter personal information and upload a profile picture.
 * The information is then stored in Firebase Firestore, and the profile picture is uploaded
 * to Firebase Storage.
 *
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


    /**
     * Called when the activity is first created. Initializes UI components,
     * Firebase instances, and sets up click listeners.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_user_profile);
        eduserName = findViewById(R.id.editText_register_name);
        eduserEmail = findViewById(R.id.editText_register_email);
        eduserHomepage = findViewById(R.id.editText_homepage);
        eduserMobile = findViewById(R.id.editText_mobile);
        selectPhoto = findViewById(R.id.profile_pic_button);
        SaveProfileButton = findViewById(R.id.button_register);


        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        userStorageRef = storage.getReference();
        userAuth = FirebaseAuth.getInstance();

        retrieveAnonymousUserId();
        onEditProfileClicked();


        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PickImageFromGallery();
            }
        });


        SaveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    // Check if imageUri is null
                    if (imageUri == null) {
                        // Display an AlertDialog to inform the user about the auto-generation of the profile picture
                        AlertDialog.Builder builder = new AlertDialog.Builder(SaveUserProfileActivity.this);
                        builder.setTitle("No Profile Picture Selected");
                        builder.setMessage("Profile Picture will be Auto-Generated.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Proceed with uploading user information without a profile picture
                                dialogInterface.dismiss();
                                uploadUserInfo();
                                Intent intent = new Intent(SaveUserProfileActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        // If imageUri is not null, directly upload user information
                        uploadImage();
                        uploadUserInfo();
                        Intent intent = new Intent(SaveUserProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                } else {
                    // Show a toast indicating that the inputs are invalid
                    Toast.makeText(SaveUserProfileActivity.this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    /**
     * Launches the gallery to allow the user to pick an image for their profile.
     */
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

    /**
     * Uploads the selected image to Firebase Storage and retrieves the download URL.
     * Once the URL is obtained, it is used to update the user's profile information.
     */

    private void uploadImage(){
        //check imageuri
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

    /**
     * Uploads user information, including name, email, homepage, mobile, and profile picture URL,
     * to Firebase Firestore.
     */
    private void uploadUserInfo(){

        // Check if CurrentUserID is null
        if (TextUtils.isEmpty(CurrentUserID)) {
            // Show a toast indicating that the user ID is not available
            Toast.makeText(SaveUserProfileActivity.this, "User ID not available", Toast.LENGTH_SHORT).show();
            return;
        }
        //get text from text edit
        String name=eduserName.getText().toString();
        String email=eduserEmail.getText().toString();
        String homepage=eduserHomepage.getText().toString();
        String mobile=eduserMobile.getText().toString();

        //
        if(TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(homepage) && TextUtils.isEmpty(mobile)){
            Toast.makeText(SaveUserProfileActivity.this,"Profile Not updated!", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(SaveUserProfileActivity.this,"Profile Created Successfully!",Toast.LENGTH_SHORT).show();
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

    /**
     * Retrieves the anonymous user ID from SharedPreferences.
     * This ID is used to uniquely identify the anonymous user in the Firestore database.
     */
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


    private boolean validateInputs() {
        String name = eduserName.getText().toString().trim();
        String email = eduserEmail.getText().toString().trim();
        String homepage = eduserHomepage.getText().toString().trim();
        String mobile = eduserMobile.getText().toString().trim();

        // Regular expressions for validation
        String namePattern = "[a-zA-Z]+\\s+[a-zA-Z]+";
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String urlPattern = "(https?://)?(www\\.)?[a-zA-Z0-9]+\\.[a-zA-Z]{2,}(/\\S*)?";
        String mobilePattern = "\\d{3}-\\d{3}-\\d{4}";


        // Check if name is empty
        if (TextUtils.isEmpty(name)) {
            eduserName.setError("Name is required");
            return false;
        } else if (!name.matches(namePattern)) {
            eduserName.setError("Enter your full name");
            return false;
        }

        // Check if email is empty and is in valid format
        if (TextUtils.isEmpty(email)) {
            eduserEmail.setError("Email is required");
            return false;
        } else if (!email.matches(emailPattern)) {
            eduserEmail.setError("Enter a valid email address");
            return false;
        }

        // Check if homepage is empty and is a valid URL
        if (TextUtils.isEmpty(homepage)) {
            eduserHomepage.setError("Homepage is required");
            return false;
        } else if (!homepage.matches(urlPattern)) {
            eduserHomepage.setError("Enter a valid homepage URL");
            return false;
        }

        // Check if mobile is empty and is in valid format
        if (TextUtils.isEmpty(mobile)) {
            eduserMobile.setError("Mobile number is required");
            return false;
        } else if (!mobile.matches(mobilePattern)) {
            eduserMobile.setError("Enter a valid 10-digit mobile number in format xxx-xxx-xxxx");
            return false;
        }
        else return true;
    }

    /**
     * Populates the UI fields with the existing user profile data if in edit mode.
     */
    private void onEditProfileClicked() {
        TextView textViewRegisterHead = findViewById(R.id.textView_register_head);
        boolean isEditingProfile = getIntent().getBooleanExtra("editProfile", false);
        textViewRegisterHead.setText(isEditingProfile ? "Edit Profile" : "Create Profile");

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String userName = extras.getString("userName");
                String userEmail = extras.getString("userEmail");
                String userHomepage = extras.getString("userHomepage");
                String userMobile = extras.getString("userMobile");

                if (userName != null) eduserName.setText(userName);
                if (userEmail != null) eduserEmail.setText(userEmail);
                if (userHomepage != null) eduserHomepage.setText(userHomepage);
                if (userMobile != null) eduserMobile.setText(userMobile);
            }
        }

    }
}
