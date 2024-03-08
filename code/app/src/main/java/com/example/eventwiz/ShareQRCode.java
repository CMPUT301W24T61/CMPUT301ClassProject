package com.example.eventwiz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareQRCode {

    public static void shareQRCodeImage(Bitmap bitmap, String text, Context context) {
        if (bitmap == null) {
            Toast.makeText(context, "QR code not generated", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // create the directory if it doesn't exist
            File imageFile = new File(cachePath, "image.png");
            FileOutputStream stream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            // Share Intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");

            // Add the QR code image
            Uri imageUri = FileProvider.getUriForFile(context, "com.example.eventwiz.fileprovider", imageFile);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

            // Add text if provided
            if (text != null && !text.isEmpty()) {
                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            }

            // Grant read permission to the receiving app
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Launch the share activity
            Intent chooser = Intent.createChooser(shareIntent, "Share QR Code");
            if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(chooser);
            } else {
                Toast.makeText(context, "No apps available for sharing", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error sharing QR code", Toast.LENGTH_SHORT).show();
        }
    }
}
