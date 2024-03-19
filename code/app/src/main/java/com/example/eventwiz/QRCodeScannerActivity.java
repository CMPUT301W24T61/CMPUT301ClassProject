package com.example.eventwiz;
//https://reintech.io/blog/implementing-android-app-qr-code-scanner

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private static boolean userAdmin = false;
    public static boolean isUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(boolean status) {
        userAdmin= status;
    }



    private static final int PERMISSION_REQUEST_CAMERA = 1;

    private static final String ADMIN_QR_CODE_HASH = "9d249f377060a7ed85b770bcecf6f207031b0f49bb2bf3f0d5f7bf5f1645976f";

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

    /**
     * Initializes QR code scanner
     */
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
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            if (result.getContents() == null) {
//                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//                //validate firebase authentication
////                Intent intent = new Intent(QRCodeScannerActivity.this, SaveUserProfileActivity.class);
////                startActivity(intent);
//                String scannedCode = result.getContents();
//                Log.d("SCannedContent", scannedCode);
//                if(scannedCode.equals(ADMIN_QR_CODE_HASH)) {
//                    //this is an admin so redirect to admin activity
//                    //set this user as admin
//                    Toast.makeText(this, "Welcome Admin", Toast.LENGTH_LONG).show();
//                    this.setUserAdmin(true);
//                    Intent intent = new Intent(QRCodeScannerActivity.this, AdminDashboard.class);
//                    startActivity(intent);
//                }
//                else{
//                    Toast.makeText(this, scannedCode, Toast.LENGTH_LONG).show();
//                    //querying events for hashCode
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                    db.collection("events")
//                            .whereEqualTo("hashCode", scannedCode)
//                            .get()
//                            .addOnCompleteListener(task -> {
//                                if (task.isSuccessful() && task.getResult() != null) {
//                                    if (!task.getResult().isEmpty()) {
//                                        Toast.makeText(QRCodeScannerActivity.this, "Checked in", Toast.LENGTH_LONG).show();
//                                        // Event with the matching hash code found
//                                        DocumentSnapshot eventDocument = task.getResult().getDocuments().get(0);
//                                        // Assuming 'Event' is your model class
//                                        Event event = eventDocument.toObject(Event.class);
//                                        Intent intent = new Intent(QRCodeScannerActivity.this, ViewEventDetailsActivity.class);
//                                        intent.putExtra("eventId", eventDocument.getId()); // Pass the event ID
//                                        startActivity(intent);
//                                    } else {
//                                        // No matching event found
//                                        Toast.makeText(QRCodeScannerActivity.this, "Event not found.", Toast.LENGTH_LONG).show();
//                                    }
//                                } else {
//                                    // Task failed with an exception
//                                    Toast.makeText(QRCodeScannerActivity.this, "Error searching for event.", Toast.LENGTH_LONG).show();
//                                }
//                            });
//                }
//
//
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }


        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
            } else {
                handleScannedCode(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleScannedCode(String scannedCode) {
        if (scannedCode.equals(ADMIN_QR_CODE_HASH)) {
            Toast.makeText(this, "Welcome Admin", Toast.LENGTH_LONG).show();
            // Redirect to admin activity

            this.setUserAdmin(true);
            Intent intent = new Intent(QRCodeScannerActivity.this, AdminDashboard.class);
            startActivity(intent);
        } else {

            AttendeeService.checkInOrPromotion(this, scannedCode);
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