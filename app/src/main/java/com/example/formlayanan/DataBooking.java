package com.example.formlayanan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class DataBooking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_databooking);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Button and TextView
        Button buttontanggal = findViewById(R.id.buttontanggal);
        Button buttonjam = findViewById(R.id.buttonjam);
        TextView textView5 = findViewById(R.id.textViewtanggal);
        TextView textView6 = findViewById(R.id.textViewjam);

        // Set DatePicker functionality for button
        buttontanggal.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(DataBooking.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Set the selected date in textView5
                        String selectedDate = String.format("%02d/%02d/%02d", selectedDay, selectedMonth + 1, selectedYear % 100);
                        textView5.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Set TimePicker functionality for button3
        buttonjam.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(DataBooking.this,
                    (view, selectedHour, selectedMinute) -> {
                        // Set the selected time in textView6
                        String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        textView6.setText(selectedTime);
                    }, hour, minute, true);
            timePickerDialog.show();
        });
    }
}