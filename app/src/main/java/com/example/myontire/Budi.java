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

public class Budi extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailmechanic4);

        Button chatButton = findViewById(R.id.chatb);
        ImageView backButton = findViewById(R.id.backb);

        chatButton.setOnClickListener(view -> {
            Intent intent = new Intent(Budi.this, DataBooking.class);
            startActivity(intent);
        });
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(Budi.this, FindMechanic.class);
            startActivity(intent);
        });
    }
}