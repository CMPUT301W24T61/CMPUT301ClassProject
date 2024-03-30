package com.example.eventwiz;




import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

/**
 * ViewProfileActivity displays the user profile information, allowing users to view their details.
 * Users can edit their profile by navigating to the SaveUserProfileActivity.
 * The profile information is retrieved from the Firestore database and displayed on the screen.
 *
 * @author yesith
 * @version 1.0
 * @since 2024-03-08
 */public class ViewProfileActivity extends AppCompatActivity {

    private TextView tvUserName, tvUserEmail, tvUserMobile, tvUserHomepage;
    private ImageView ivProfileImage; // Add ImageView for profile picture

    private Button editProfileBtn;
    private ImageButton backButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        tvUserName = findViewById(R.id.text_name);
        tvUserEmail = findViewById(R.id.text_email);
        tvUserMobile = findViewById(R.id.text_mobile);
        tvUserHomepage = findViewById(R.id.text_homepage);
        editProfileBtn = findViewById(R.id.edit_profile);
        ivProfileImage = findViewById(R.id.image_profile); // Initialize ImageView
        backButton = findViewById(R.id.BackArrow);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfileActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewProfileActivity.this, SaveUserProfileActivity.class);
                // Pass user profile information as extras to the intent
                intent.putExtra("userName", tvUserName.getText().toString());
                intent.putExtra("userEmail", tvUserEmail.getText().toString());
                intent.putExtra("userMobile", tvUserMobile.getText().toString());
                intent.putExtra("userHomepage", tvUserHomepage.getText().toString());
                intent.putExtra("editProfile", true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String currentID = user.getUid();
        DocumentReference ref = db.collection("Users").document(currentID);

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String nameResult = document.getString("userName");
                        String emailResult = document.getString("userEmail");
                        String mobileResult = document.getString("userMobile");
                        String hpResult = document.getString("userHomepage");

                        tvUserName.setText(nameResult);
                        tvUserEmail.setText(emailResult);
                        tvUserHomepage.setText(hpResult);
                        tvUserMobile.setText(mobileResult);

                        // Load profile picture
                        String profilePicUrl = document.getString("profilePicImage");
                        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                            loadProfileImage(profilePicUrl);
                        }
                    } else {
                        Toast.makeText(ViewProfileActivity.this, "Profile Does Not Exist!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ViewProfileActivity.this, "Failed to Retrieve Profile!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to load profile image from URL into ImageView
    private void loadProfileImage(String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_default_profile_icon) // Placeholder image while loading
                .error(R.drawable.ic_logo_round) // Error image if loading fails
                .into(ivProfileImage);
    }
}
