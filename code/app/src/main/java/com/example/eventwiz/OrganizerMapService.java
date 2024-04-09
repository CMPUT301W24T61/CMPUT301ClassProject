package com.example.eventwiz;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizerMapService extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Error: Map fragment is null", Toast.LENGTH_SHORT).show();
        }

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Once the map is ready, call geocodeAndShowOnMap method
        String address = getIntent().getStringExtra("eventLocation");
        if (address != null && !address.isEmpty()) {
            geocodeAndShowOnMap(address);
        }
        // Call method to retrieve GeoPoint data
        getCheckInLocationsFromFirestore();
    }

    private void geocodeAndShowOnMap(String address) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            assert addresses != null;
            if (!addresses.isEmpty()) {
                Address location = addresses.get(0);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(address));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            } else {
                Toast.makeText(OrganizerMapService.this, "Failed to geocode address", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(OrganizerMapService.this, "Failed to geocode address: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Method to retrieve GeoPoint data for each check-in from Firestore
    private void getCheckInLocationsFromFirestore() {
        String eventId = getIntent().getStringExtra("eventId");

        if (eventId == null || eventId.trim().isEmpty()) {
            Toast.makeText(OrganizerMapService.this, "Error: Invalid event ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get reference to the event document
        DocumentReference eventRef = db.collection("events").document(eventId);

        // Query the event document to get the list of user IDs signed up for the event
        eventRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Event event = documentSnapshot.toObject(Event.class);
                if (event != null) {
                    Map<String,Integer> signedUpUserIds = event.getCheckInsCount();
                    if (signedUpUserIds != null && !signedUpUserIds.isEmpty()) {
                        // Iterate through each signed up user ID
                        for (String userId : signedUpUserIds.keySet()) {
                            // Get reference to the user document
                            DocumentReference userRef = db.collection("Users").document(userId);
                            // Query the user document to get the GeoPoint data for the last check-in location
                            userRef.get().addOnSuccessListener(userDocumentSnapshot -> {
                                if (userDocumentSnapshot.exists()) {
                                    GeoPoint checkInLocation = userDocumentSnapshot.getGeoPoint("lastCheckInLocation");
                                    if (checkInLocation != null) {
                                        // Display the location on the map
                                        showLocationOnMap(checkInLocation.getLatitude(), checkInLocation.getLongitude());
                                    }
                                }
                            }).addOnFailureListener(e -> {
                                // Handle failure to fetch user document
                                Toast.makeText(OrganizerMapService.this, "Failed to fetch user document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    } else {
                        Toast.makeText(OrganizerMapService.this, "No attendees have checked in", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(OrganizerMapService.this, "Event document does not exist", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            // Handle failure to fetch event document
            Toast.makeText(OrganizerMapService.this, "Failed to fetch event document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    // Method to display location on map using latitude and longitude
    private void showLocationOnMap(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Check-in Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
}
