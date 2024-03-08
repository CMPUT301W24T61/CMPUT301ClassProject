package com.example.eventwiz;




import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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


public class ViewProfileActivity extends AppCompatActivity {

    private TextView tvUserName, tvUserEmail, tvUserMobile, tvUserHomepage;

    private Button editProfileBtn;

    private String CurrentUserID;
    //private String docID;

    SharedPreferences sp;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);




        tvUserName = findViewById(R.id.text_name);
        tvUserEmail = findViewById(R.id.text_email);
        tvUserMobile = findViewById(R.id.text_mobile);
        tvUserHomepage = findViewById(R.id.text_homepage);
        editProfileBtn =  findViewById(R.id.edit_profile);



        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewProfileActivity.this, SaveUserProfileActivity.class);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onStart() {

        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentID = user.getUid();
        DocumentReference ref;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        ref = firestore.collection("Users").document(currentID);
        ref.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){

                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                            String nameResult = task.getResult().getString("userName");
                            String emailResult = task.getResult().getString("userEmail");
                            String mobileResult = task.getResult().getString("userMobile");
                            String hpResult = task.getResult().getString("userHomepage");

                            tvUserName.setText(nameResult);
                            tvUserEmail.setText(emailResult);
                            tvUserHomepage.setText(hpResult);
                            tvUserMobile.setText(mobileResult);

                        }else{
                            Toast.makeText(ViewProfileActivity.this, "Profile Does Not exist!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private String retrieveAnonymousUserId() {
        sp = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        CurrentUserID = sp.getString("anonymousUserId", null);

        if (CurrentUserID != null) {
            // Now, 'anonymousUserId' variable contains the anonymous user's ID
            Log.d("SharedPreferences", "Retrieved Anonymous User ID: " + CurrentUserID);
        } else {
            Log.e("SharedPreferences", "Failed to retrieve Anonymous User ID");
        }
        return CurrentUserID;
    }

}
