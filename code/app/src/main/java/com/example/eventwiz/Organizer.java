package com.example.eventwiz;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.Serializable;
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
     * @param eventId The eventId to be encoded in the QR code.
     * @return The generated QR code bitmap.
     * @throws WriterException If an error occurs during QR code generation.
     */
    public Bitmap generateCheckInQRCode(String eventId) throws WriterException {
        return generateQRCodeBitmap("CHECKIN_" + eventId);
    }

    /**
     * Generates a QR code for event promotion with the provided data.
     *
     * @param eventId The eventId to be encoded in the QR code.
     * @return The generated QR code bitmap.
     * @throws WriterException If an error occurs during QR code generation.
     */
    public Bitmap generatePromotionQRCode(String eventId) throws WriterException {
        return generateQRCodeBitmap("PROMO_" + eventId);
    }

    private Bitmap generateQRCodeBitmap(String eventId) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(eventId, BarcodeFormat.QR_CODE, 512, 512);
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
}
