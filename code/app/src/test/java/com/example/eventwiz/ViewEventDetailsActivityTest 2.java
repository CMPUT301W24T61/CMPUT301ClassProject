
/*

//test incomplete
package com.example.eventwiz;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.eventwiz.ViewEventDetailsActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mock;
import static org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ViewEventDetailsActivityTest {

    @Mock
    private ViewEventDetailsActivity activity;

    @Mock
    private FirebaseFirestore mockFirestore;

    @Mock
    private DocumentReference mockDocumentReference;

    @Mock
    private Task<DocumentSnapshot> mockTask;

    @Mock
    private DocumentSnapshot mockDocumentSnapshot;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        activity = Mockito.spy(new ViewEventDetailsActivity());
        activity.db = mockFirestore;
    }

    @Test
    public void testLoadEventFromFirestore() {
        String eventId = "testEventId";

        // Mocking the Firestore document retrieval
        when(mockFirestore.collection("events").document(eventId)).thenReturn(mockDocumentReference);
        when(mockDocumentReference.get()).thenReturn(mockTask);

        // Mocking a sample documentSnapshot
        when(mockTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.toObject(Event.class)).thenReturn(getSampleEvent());

        // Triggering the method to be tested
        activity.loadEventFromFirestore(eventId);

        // Verifying that loadEventDetails() is called
        Mockito.verify(activity).loadEventDetails(getSampleEvent());
    }

    private Event getSampleEvent() {
        // Create and return a sample Event object for testing
        // You may customize this based on your Event class structure
        return new Event("Sample Event", "2024-03-08", "10:00 AM", "12:00 PM",
                "Sample Location", 100, "posterUrl", "checkInQRCode", "promotionQRCode");
    }
}

*/

