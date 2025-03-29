package com.example.myontire;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Abdul extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dethailmecanicdua);

        Button chatButton = findViewById(R.id.chatabdl);
        ImageView backButton = findViewById(R.id.backabdl);

        chatButton.setOnClickListener(view -> {
            Intent intent = new Intent(Abdul.this, DataBooking.class);
            startActivity(intent);
        });
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(Abdul.this, FindMechanic.class);
            startActivity(intent);
        });
}}
