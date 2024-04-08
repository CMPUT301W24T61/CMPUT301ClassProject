package com.example.eventwiz;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FirebaseService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "my_channel";

    public static void setSharedPref(SharedPreferences preferences) {
        sharedPref = preferences;
    }
    private static SharedPreferences sharedPref;

    public static String getToken() {
        return sharedPref != null ? sharedPref.getString("token", "") : "";
    }

    public static void setToken(String value) {
        if (sharedPref != null) {
            sharedPref.edit().putString("token", value).apply();
        }
    }

    private static final String TAG = "FirebaseService";

    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);
        // You may want to send the new token to your server for handling
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Log a message to indicate that a message has been received
        Log.d(TAG, "Message received: " + remoteMessage.getData());

        // Extract title and message from the message payload
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

        // Log the title and message
        Log.d(TAG, "Notification title: " + title);
        Log.d(TAG, "Notification message: " + message);

        // Create an intent to open the NotificationCreationActivity when the notification is clicked
        Intent intent = new Intent(this, NotificationCreationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Ensure that only one instance of NotificationCreationActivity is created
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_new_notification)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Display the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(1000); // Generate a unique ID for the notification
        notificationManager.notify(notificationID, notificationBuilder.build());

        // Log a message indicating that the notification has been displayed
        Log.d(TAG, "Notification displayed");

        // For Android Oreo and above, create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager notificationManager) {
        String channelName = "My Channel";
        String channelDescription = "Description of My Channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
        channel.setDescription(channelDescription);
        channel.enableLights(true);
        channel.setLightColor(Color.GREEN);

        notificationManager.createNotificationChannel(channel);
    }
}
