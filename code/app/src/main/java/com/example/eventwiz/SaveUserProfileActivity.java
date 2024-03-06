
package com.example.eventwiz;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This class is responsible for saving the information provided by the user via the user profile.
 * @author Yesith
 */
public class SaveUserProfileActivity extends AppCompatActivity {

    private ImageView SelectPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_user_profile);

        ImageView SelectPhoto = findViewById(R.id.imageView_logo);
        SelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                PickImageFromGallery();
            }
        });

    }
    }
    /*
    private void PickImageFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

    }

    ?

 */

