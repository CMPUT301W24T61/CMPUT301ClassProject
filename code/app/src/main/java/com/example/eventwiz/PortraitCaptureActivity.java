package com.example.eventwiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.journeyapps.barcodescanner.CaptureActivity;

/**
 * The PortraitCaptureActivity class extends CaptureActivity and provides additional functionality
 * for capturing QR codes in portrait orientation.
 * @author Junkai
 */
public class PortraitCaptureActivity extends CaptureActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button goBackButton = new Button(this);
        goBackButton.setText("Go Back");
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate back to initial page
                Intent intent = new Intent(PortraitCaptureActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        this.addContentView(goBackButton, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.CENTER_HORIZONTAL
        ));
    }
}
