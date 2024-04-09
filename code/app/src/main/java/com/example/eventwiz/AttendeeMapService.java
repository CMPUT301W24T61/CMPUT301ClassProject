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


import java.io.IOException;
import java.util.List;

public class AttendeeMapService extends AppCompatActivity implements OnMapReadyCallback {

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

        getLocationFromFirestore();

    }

    private void getLocationFromFirestore() {
        String eventId = getIntent().getStringExtra("eventId");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null || eventId == null || eventId.trim().isEmpty()) {
            Toast.makeText(AttendeeMapService.this, "Error: Invalid event ID or user not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference eventRef = db.collection("events").document(eventId);
        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String address = document.getString("location");
                    geocodeAndShowOnMap(address);
                }
            } else {
                // Handle failed task
                Toast.makeText(AttendeeMapService.this, "Failed to fetch event details: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
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
                Toast.makeText(AttendeeMapService.this, "Failed to geocode address", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(AttendeeMapService.this, "Failed to geocode address: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Once the map is ready, call geocodeAndShowOnMap method
        String address = getIntent().getStringExtra("eventLocation");
        if (address != null && !address.isEmpty()) {
            geocodeAndShowOnMap(address);
        }
    }
}
