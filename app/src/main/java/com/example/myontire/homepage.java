package com.example.myontire;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);

        int[] buttonIds = {
                R.id.buttonps, R.id.buttonbr, R.id.buttonrsp, R.id.buttontm,
                R.id.buttonet, R.id.buttonac, R.id.buttonext,
                R.id.buttoninter, R.id.buttonat
        };

        for (int id : buttonIds) {
            Button button = findViewById(id);
            if (button != null) {
                button.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), FindMechanic.class);
                    startActivity(intent);
                });
            }
        }
        Button urgent = findViewById(R.id.buttonurgent);
        urgent.setOnClickListener(view -> {
            Intent intent = new Intent(homepage.this, DataBooking.class);
            startActivity(intent);
        });
    }
}
