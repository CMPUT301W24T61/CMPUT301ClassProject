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

    public static void shareQRCodeImage(Bitmap bitmap, Context context) {
        if (bitmap == null) {
            Toast.makeText(context, "QR code not generated", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            File imagePath = new File(context.getCacheDir(), "images");
            File newFile = new File(imagePath, "image.png");
            Uri contentUri = FileProvider.getUriForFile(context, "com.example.eventwiz.fileprovider", newFile);

            if (contentUri != null) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(shareIntent, "Share QR Code"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error sharing QR code", Toast.LENGTH_SHORT).show();
        }
    }
}
