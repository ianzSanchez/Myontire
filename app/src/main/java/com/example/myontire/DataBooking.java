package com.example.myontire;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DataBooking extends AppCompatActivity implements OnMapReadyCallback {

    private TextView addressTextView, textViewTanggal, textViewJam;
    private Button tanggal, jam, buttonSave;
    private MapView mapView;
    private GoogleMap gMap;
    private FirebaseFirestore db;
    private double selectedLatitude = 0.0;
    private double selectedLongitude = 0.0;
    private static final int REQUEST_MAP = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_databooking);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        addressTextView = findViewById(R.id.editTextTextalamat);
        tanggal = findViewById(R.id.buttontanggal);
        jam = findViewById(R.id.buttonjam);
        textViewTanggal = findViewById(R.id.textViewtanggal);
        textViewJam = findViewById(R.id.textViewjam);
        mapView = findViewById(R.id.mapView);
        buttonSave = findViewById(R.id.btn_save);

        EditText editTextNama = findViewById(R.id.editTextTextnama);
        EditText editTextMerek = findViewById(R.id.editTextTextMerek);
        EditText editTextKM = findViewById(R.id.editTextTextKM);
        EditText editTextKeluhan = findViewById(R.id.editTextTextKeluhan);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String getAddress = getIntent().getStringExtra("Chosen_Address");
        if (getAddress != null) {
            addressTextView.setText(getAddress);
        }

        addressTextView.setOnClickListener(v -> {
            Intent intent = new Intent(DataBooking.this, MapActivity.class);
            startActivityForResult(intent, REQUEST_MAP);
        });

        tanggal.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(DataBooking.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = String.format("%02d/%02d/%02d", selectedDay, selectedMonth + 1, selectedYear % 100);
                        textViewTanggal.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        jam.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(DataBooking.this,
                    (view, selectedHour, selectedMinute) -> {
                        String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        textViewJam.setText(selectedTime);
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        initMapView(savedInstanceState);

        // LOGIKA BUTTON SAVE
        buttonSave.setOnClickListener(v -> {
            String nama = editTextNama.getText().toString().trim();
            String alamat = addressTextView.getText().toString().trim();
            String tanggalStr = textViewTanggal.getText().toString().trim();
            String jamStr = textViewJam.getText().toString().trim();
            String merek = editTextMerek.getText().toString().trim();
            String km = editTextKM.getText().toString().trim();
            String keluhan = editTextKeluhan.getText().toString().trim();

            if (nama.isEmpty() || alamat.isEmpty() || tanggalStr.isEmpty() || jamStr.isEmpty() ||
                    merek.isEmpty() || km.isEmpty() || keluhan.isEmpty()) {
                Toast.makeText(this, "Please complete all fields before proceeding", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> booking = new HashMap<>();
            booking.put("nama", nama);
            booking.put("alamat", alamat);
            booking.put("tanggal", tanggalStr);
            booking.put("jam", jamStr);
            booking.put("merek", merek);
            booking.put("kilometer", km);
            booking.put("keluhan", keluhan);
            booking.put("timestamp", System.currentTimeMillis());

            db.collection("bookings")
                    .add(booking)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Booking successfully done!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DataBooking.this, RequestStatusActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to save data. Please try again.", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MAP && resultCode == RESULT_OK && data != null) {
            String getAddress = data.getStringExtra("Chosen_Address");
            double lat = data.getDoubleExtra("Chosen_Latitude", 0.0); // Ambil lintang
            double lng = data.getDoubleExtra("Chosen_Longitude", 0.0); // Ambil bujur

            if (getAddress != null) {
                addressTextView.setText(getAddress);
                selectedLatitude = lat; // Simpan lintang
                selectedLongitude = lng; // Simpan bujur

                // Perbarui peta di DataBooking setelah mendapatkan lokasi baru
                if (gMap != null) {
                    updateMapWithSelectedLocation();
                }
            }
        }
    }


    private void initMapView(Bundle savedInstanceState) {
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        // Panggil metode untuk memperbarui peta dengan lokasi yang dipilih
        updateMapWithSelectedLocation();
    }

    // Metode baru untuk memperbarui peta dengan lokasi yang dipilih
    private void updateMapWithSelectedLocation() {
        // Jika belum ada lokasi yang dipilih dari MapActivity, gunakan default
        if (selectedLatitude == 0.0 && selectedLongitude == 0.0) {
            // Ini akan terjadi pertama kali saat DataBooking dibuka
            // Anda bisa menggunakan lokasi default atau biarkan kosong
            LatLng defaultLocation = new LatLng(-6.200000, 106.816666); // Jakarta
            gMap.clear(); // Hapus marker lama
            gMap.addMarker(new MarkerOptions().position(defaultLocation).title("Default Location"));
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
        } else {
            // Gunakan lokasi yang diterima dari MapActivity
            LatLng chosenLocation = new LatLng(selectedLatitude, selectedLongitude);
            gMap.clear(); // Hapus marker lama
            gMap.addMarker(new MarkerOptions().position(chosenLocation).title("Selected Location"));
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chosenLocation, 15));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) mapView.onLowMemory();
    }
}