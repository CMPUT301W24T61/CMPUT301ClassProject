package com.example.eventwiz;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Organizer {
    private String name;
    private List<Event> organizedEvents;
    private Map<String, Event> eventMap;
    private Context context; // Context is needed to access file storage

    public Organizer(String name, Context context) {
        this.name = name;
        this.organizedEvents = new ArrayList<>();
        this.eventMap = new HashMap<>();
        this.context = context; // Initialize context
    }

    public Event createEvent(String name, String description, String date, String startTime, String endTime, String location, int maxAttendees) {
        Event newEvent = new Event(name, description, date, startTime,endTime, location, maxAttendees);
        organizedEvents.add(newEvent);
        eventMap.put(newEvent.getId(), newEvent);

        try {
            String checkInQRCodePath = generateCheckInQRCode(newEvent);
            String promotionQRCodePath = generatePromotionQRCode(newEvent);
            newEvent.setCheckInQRCode(checkInQRCodePath);
            newEvent.setPromotionQRCode(promotionQRCodePath);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        return newEvent;
    }

    public String generateCheckInQRCode(Event event) throws WriterException, IOException {
        Bitmap bitmap = generateQRCodeBitmap("CHECKIN_" + event.getId());
        return saveBitmapToFile(bitmap, "checkin_" + event.getId() + ".png");
    }

    public String generatePromotionQRCode(Event event) throws WriterException, IOException {
        Bitmap bitmap = generateQRCodeBitmap("PROMO_" + event.getId());
        return saveBitmapToFile(bitmap, "promo_" + event.getId() + ".png");
    }

    private Bitmap generateQRCodeBitmap(String data) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? android.graphics.Color.BLACK : android.graphics.Color.WHITE);
            }
        }
        return bmp;
    }

    private String saveBitmapToFile(Bitmap bitmap, String fileName) throws IOException {
        File file = new File(context.getExternalFilesDir(null), fileName);
        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        out.close();
        return Uri.fromFile(file).toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getOrganizedEvents() {
        return new ArrayList<>(organizedEvents);
    }
}
