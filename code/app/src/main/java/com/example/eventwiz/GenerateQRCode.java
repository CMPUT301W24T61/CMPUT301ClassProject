package com.example.eventwiz;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * This class is responsible for generating the QR Code for each event.
 * It provides methods to generate a unique string, hash the string, and create a QR code bitmap.
 * @author Hunaid
 * @see QRCodeScannerActivity
 */
public class GenerateQRCode {

    /**
     * Generates the QR code bitmap for the event.
     * @return Bitmap representing the QR code
     */
    public static Bitmap generateEventQRCode() {
        //call this function to generate QR code:
//        Bitmap qrCodeBitmap = GenerateQRCode.generateEventQRCode();
//
//        // Check if the QR code was generated successfully
//        if (qrCodeBitmap != null) {
//            // Display the QR code in the ImageView
//            qrCodeImageView.setImageBitmap(qrCodeBitmap);
//        } else {
//            // Handle the error, the QR code generation failed
//            // Show error message or take appropriate action
//        }
        String uniqueString = generateUniqueString();
        String hashedString = hashString(uniqueString);
        Log.d("Hash", hashedString);
        return generateQRCodeBitmap(hashedString);
    }

    /**
     * Creates and returns a unique user ID as a string
     * @return String unique User ID
     */
    public static String generateUniqueString() {
        return UUID.randomUUID().toString();
    }

    /**
     * Hashes the input string using SHA-256 algorithm.
     * @param input String to be hashed
     * @return String representing the hashed value
     */
    public static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Generates a QR code bitmap from the content string.
     * @param content String to be encoded into the QR code
     * @return Bitmap representing the QR code
     */
    public static Bitmap generateQRCodeBitmap(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
