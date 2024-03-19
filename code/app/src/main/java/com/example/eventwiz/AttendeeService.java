package com.example.eventwiz;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AttendeeService {
    private static boolean checkIn = false;
    public interface EventQueryListener {
        void onEventFound(DocumentSnapshot eventDocument);
        void onEventNotFound();
        void onEventQueryError();
    }

    public static void checkInOrPromotion(Context context, String scannedCode) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        queryEventByHashCode(db, context, scannedCode, new EventQueryListener() {
            @Override
            public void onEventFound(DocumentSnapshot eventDocument) {

                Intent intent = new Intent(context, ViewEventDetailsActivity.class);
                intent.putExtra("eventId", eventDocument.getId()); // Pass the event ID
                context.startActivity(intent);
            }

            @Override
            public void onEventNotFound() {
                Toast.makeText(context, "Event not found with Hash Code.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onEventQueryError() {
                Toast.makeText(context, "Error searching for event with Hash Code.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private static void queryEventByHashCode(FirebaseFirestore db, Context context, String hashCode, EventQueryListener listener) {
        db.collection("events")
                .whereEqualTo("hashCode", hashCode)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        if (!task.getResult().isEmpty()) {
                            DocumentSnapshot eventDocument = task.getResult().getDocuments().get(0);
                            checkIn = true;
                            Toast.makeText(context, "Checked in", Toast.LENGTH_LONG).show();
                            listener.onEventFound(eventDocument);
                        } else {
                            // If not found in hashCode field, check promotionHashCode field
                            queryEventByPromotionHashCode(db, context, hashCode, listener);
                        }
                    } else {
                        listener.onEventQueryError();
                    }
                });
    }

    private static void queryEventByPromotionHashCode(FirebaseFirestore db, Context context, String promotionHashCode, EventQueryListener listener) {
        db.collection("events")
                .whereEqualTo("promotionHashCode", promotionHashCode)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        if (!task.getResult().isEmpty()) {
                            DocumentSnapshot eventDocument = task.getResult().getDocuments().get(0);
                            listener.onEventFound(eventDocument);
                            checkIn= false;
                            Toast.makeText(context, "Promotion Code", Toast.LENGTH_LONG).show();
                        } else {
                            listener.onEventNotFound();
                            Toast.makeText(context, "Promotion Code not found", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        listener.onEventQueryError();
                        Toast.makeText(context, "Error searching for event with Promotion Code.", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
