
package com.example.eventwiz;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

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

