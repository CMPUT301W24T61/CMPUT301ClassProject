package com.example.eventwiz;
//https://reintech.io/blog/implementing-android-app-qr-code-scanner

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * This class is responsible for fetching permission from the user to use the camera app
 * to scan a QR Code. After the Scan QR code button is pressed on the main screen, this class
 * is called which switched to the activity_qrcode_scanner.xml.
 * Handles the actual scanning of the QR Code
 * @author Hunaid
 * @see GenerateQRCode
 */
public class QRCodeScannerActivity extends AppCompatActivity{

    private static final int PERMISSION_REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);
        Button cancelButton = findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // This will close the current activity and go back
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            } else {
                initQRCodeScanner();
            }
        } else {
            initQRCodeScanner();
        }
    }

    private void initQRCodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setOrientationLocked(true);
        integrator.setPrompt("Scan a QR code");
        integrator.initiateScan();
    }

    /**
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                //validate firebase authentication
                Intent intent = new Intent(QRCodeScannerActivity.this, SaveUserProfileActivity.class);
                startActivity(intent);
                String scannedCode = result.getContents();
//                Bitmap qrCodeBitmap = GenerateQRCode.generateEventQRCode();
//                if (qrCodeBitmap != null) {
//                    // Display the QR code in the ImageView
//                    ImageView qrCodeImageView = findViewById(R.id.qrCodeImageView);
//                    qrCodeImageView.setImageBitmap(qrCodeBitmap);
//                } else {
//                    // Handle the error, the QR code generation failed
//                    // Show error message or take appropriate action
//                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Query the 'AdministratorCodes' collection for the scanned code
                db.collection("AdministratorCodes")
                        .whereEqualTo("accessHash", scannedCode)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                        // QR Code is valid and exists in Firestore
                                        Intent intent = new Intent(QRCodeScannerActivity.this, SaveUserProfileActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // QR Code does not exist in Firestore
                                        Toast.makeText(QRCodeScannerActivity.this, "Invalid QR Code", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    // Handle the error
                                    Toast.makeText(QRCodeScannerActivity.this, "Error checking QR Code", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     *
     * @param requestCode The request code passed in requestPermissions
     * android.app.Activity, String[], int)}
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initQRCodeScanner();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }


}