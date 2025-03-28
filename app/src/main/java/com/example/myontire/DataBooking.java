package com.example.myontire;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.Calendar;

public class DataBooking extends AppCompatActivity implements OnMapReadyCallback {

    private TextView addressTextView;
    private Button tanggal, jam;
    private TextView textViewTanggal, textViewJam;
    private MapView mapView;
    private GoogleMap gMap;
    private Button buttonsave;

    private static final int REQUEST_MAP = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_databooking);

        // Inisialisasi UI
        addressTextView = findViewById(R.id.editTextTextalamat);
        tanggal = findViewById(R.id.buttontanggal);
        jam = findViewById(R.id.buttonjam);
        textViewTanggal = findViewById(R.id.textViewtanggal);
        textViewJam = findViewById(R.id.textViewjam);
        mapView = findViewById(R.id.mapView);
        Button buttonSave = findViewById(R.id.btn_save);

        buttonSave.setOnClickListener(v -> {
            Intent intent = new Intent(DataBooking.this, RequestStatusActivity.class);
            startActivity(intent);
        });

        addressTextView.setOnClickListener(v -> {
            Intent intent = new Intent(DataBooking.this, MapActivity.class);
            startActivityForResult(intent, REQUEST_MAP);
        });

        // Inisialisasi MapView
        initMapView(savedInstanceState);

        // Mengambil alamat yang dikirim dari intent sebelumnya
        String getAddress = getIntent().getStringExtra("Chosen_Address");
        if (getAddress != null) {
            addressTextView.setText(getAddress);
        }

        // Menyesuaikan padding sesuai dengan system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // DatePicker Dialog untuk memilih tanggal
        tanggal.setOnClickListener(v -> {
            Calendar b = Calendar.getInstance();
            int year = b.get(Calendar.YEAR);
            int month = b.get(Calendar.MONTH);
            int day = b.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(DataBooking.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = String.format("%02d/%02d/%02d", selectedDay, selectedMonth + 1, selectedYear % 100);
                        textViewTanggal.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // TimePicker Dialog untuk memilih waktu
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

    }

    // Inisialisasi MapView
    private void initMapView(Bundle savedInstanceState) {
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng defaultLocation = new LatLng(-6.200000, 106.816666); // Jakarta
        gMap.addMarker(new MarkerOptions().position(defaultLocation).title("Default Location"));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }
}
