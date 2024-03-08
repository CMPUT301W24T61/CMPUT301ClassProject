package com.example.eventwiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * The ScanQRActivity class is responsible for scanning QR codes using the device's camera.
 * @author Junkai
 */
public class ScanQRActivity extends AppCompatActivity {

    /**
     * This method handles the result of the QR code scanning process.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        // Start scanner on activity start
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(true);
        integrator.setCaptureActivity(PortraitCaptureActivity.class);
        integrator.addExtra("PROMPT_MESSAGE", "Scan a QR code");
        integrator.initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // Handle cancel
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                // Handle successful scan
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
