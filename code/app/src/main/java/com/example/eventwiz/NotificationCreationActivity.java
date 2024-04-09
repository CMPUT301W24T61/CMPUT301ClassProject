package com.example.eventwiz;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationCreationActivity extends AppCompatActivity {

    private static final String TAG = "Activity";
    private static final String TOPIC = "/topics/my-topic";
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Successfully subscribed to topic.");
                    } else {
                        Log.e(TAG, "Failed to subscribe to topic: " + task.getException().getMessage());
                    }
                });


        // Retrieve the event ID from the intent
        eventId = getIntent().getStringExtra("eventId");

        FirebaseService.setSharedPref(getSharedPreferences("sharedPref", Context.MODE_PRIVATE));

        FirebaseInstallations.getInstance().getToken(/* forceRefresh = */ true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        InstallationTokenResult tokenResult = task.getResult();
                        String token = tokenResult.getToken();
                        FirebaseService.setToken(token);
                        EditText etToken = findViewById(R.id.etToken);

                        etToken.setText(token);

                    } else {
                        Log.e(TAG, "Failed to get Firebase Cloud Messaging token: " + task.getException().getMessage());
                    }
                });

        findViewById(R.id.buttonSend).setOnClickListener(v -> {
            String title = ((EditText) findViewById(R.id.etTitle)).getText().toString();
            String message = ((EditText) findViewById(R.id.etDescription)).getText().toString();
            String recipientToken = ((EditText) findViewById(R.id.etToken)).getText().toString();
            if (!title.isEmpty() && !message.isEmpty() && !recipientToken.isEmpty()) {
                fetchSignupUserIds(eventId, title, message, recipientToken);
            }
        });
    }

    private void sendNotification(PushNotification notification) {
        RetrofitInstance.getApi().postNotification(notification).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Notification sent successfully");
                    try {
                        // You can handle the response body here if needed
                        String responseBody = response.body().string();
                        Log.d(TAG, "Response body: " + responseBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e(TAG, "Failed to send notification: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Failed to send notification: " + t.getMessage());
            }
        });
    }


    private void fetchSignupUserIds(String eventId, String title, String message, String recipientToken) {
        mFirestore.collection("events").document(eventId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot eventDocument = task.getResult();
                        if (eventDocument.exists()) {
                            Event event = eventDocument.toObject(Event.class);
                            if (event != null && event.getSignups() != null && event.getSignups().contains(mAuth.getCurrentUser().getUid())) {
                                Log.d(TAG, "User found in signups");
                                NotificationData notificationData = new NotificationData(title, message);
                                PushNotification pushNotification = new PushNotification(notificationData, recipientToken);
                                sendNotification(pushNotification);
                                Log.d(TAG, "Notification sent");
                            } else {
                                Log.d(TAG, "User is not signed up for this event");
                            }
                        } else {
                            Log.d(TAG, "Event document doesn't exist");
                        }
                    } else {
                        Log.e(TAG, "Error getting event document: ", task.getException());
                    }
                });
    }



}
