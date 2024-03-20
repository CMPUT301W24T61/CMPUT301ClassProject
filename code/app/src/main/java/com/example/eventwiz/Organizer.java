package com.example.eventwiz;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * The Organizer class represents an organizer entity in the application.
 * An organizer can generate QR codes for event check-ins and promotions.
 * @author Junkai
 */
public class Organizer implements Serializable {

    private String id;

    /**
     * Constructs an Organizer object with a randomly generated ID.
     */
    public Organizer() {
        this.id = UUID.randomUUID().toString();
    }


    /**
     * Generates a QR code for event check-in with the provided data.
     *
     * @param data The data to be encoded in the QR code.
     * @return The generated QR code bitmap.
     * @throws WriterException If an error occurs during QR code generation.
     */
    public Bitmap generateCheckInQRCode(String data) throws WriterException {
        return generateQRCodeBitmap("" + data);
    }

    /**
     * Generates a QR code for event promotion with the provided data.
     *
     * @param data The data to be encoded in the QR code.
     * @return The generated QR code bitmap.
     * @throws WriterException If an error occurs during QR code generation.
     */
    public Bitmap generatePromotionQRCode(String data) throws WriterException {
        return generateQRCodeBitmap("" + data);
    }

    private Bitmap generateQRCodeBitmap(String data) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512);
        return createBitmapFromBitMatrix(bitMatrix);
    }

    private Bitmap createBitmapFromBitMatrix(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }
    /**
     * Creates and returns a unique user ID as a string
     * @return String unique User ID
     */
    public String generateUniqueString() {
        return UUID.randomUUID().toString();
    }

    /**
     * Hashes the input string using SHA-256 algorithm.
     * @param input String to be hashed
     * @return String representing the hashed value
     */
    public String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String bytesToHex(byte[] hash) {
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
}
