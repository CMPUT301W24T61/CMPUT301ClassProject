package com.example.eventwiz;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

public class Organizer implements Serializable {

    private String id;

    public Organizer() {
        this.id = UUID.randomUUID().toString();
    }


    public Bitmap generateCheckInQRCode(String data) throws WriterException {
        return generateQRCodeBitmap("CHECKIN_" + data);
    }

    public Bitmap generatePromotionQRCode(String data) throws WriterException {
        return generateQRCodeBitmap("PROMO_" + data);
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
}
