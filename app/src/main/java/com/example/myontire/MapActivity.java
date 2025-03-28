package com.example.myontire;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private TextView locationText;
    private Marker currentMarker;
    private boolean isMarkerMovedByUser = false;

    private Button Confirm;
    private Button GetCurrentLocation;
    private String address;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locationText = findViewById(R.id.location_text);
        GetCurrentLocation = findViewById(R.id.btn_get_location);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Confirm = findViewById(R.id.btn_confirm);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        GetCurrentLocation.setOnClickListener(v -> {
            isMarkerMovedByUser = false;
            getLastKnownLocation();
        });

        Confirm.setOnClickListener(v -> {
            Intent act1 = new Intent(MapActivity.this, DataBooking.class);
            act1.putExtra("Chosen_Address", address);
            startActivity(act1);
        });
    }

    @SuppressLint("MissingPermission")
    private void getLastKnownLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                updateLocationUI(location);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(2000).build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    updateLocationUI(location);
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getLastKnownLocation();
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        mMap.setOnMapClickListener(latLng -> {
            if (currentMarker != null) {
                currentMarker.remove();
            }
            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.selected_location)).draggable(true));
            isMarkerMovedByUser = true;
            updateLocationText(latLng);
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {
                isMarkerMovedByUser = true;
            }

            @Override
            public void onMarkerDrag(@NonNull Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                updateLocationText(marker.getPosition());
            }
        });
    }

    private void updateLocationUI(@NonNull Location location) {
        if (!isMarkerMovedByUser) {
            LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (currentMarker == null) {
                currentMarker = mMap.addMarker(new MarkerOptions().position(userLatLng).title(getString(R.string.your_location)).draggable(true));
            } else {
                currentMarker.setPosition(userLatLng);
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
            updateLocationText(userLatLng);
        }
    }

    private void updateLocationText(@NonNull LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                address = addresses.get(0).getAddressLine(0); // Simpan ke variabel instance
                locationText.setText(address);
            } else {
                locationText.setText(getString(R.string.address_not_found));
            }
        } catch (IOException e) {
            Log.e("MapActivity", "Error retrieving address", e);
            locationText.setText(getString(R.string.error_retrieving_address));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation();
                startLocationUpdates();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
