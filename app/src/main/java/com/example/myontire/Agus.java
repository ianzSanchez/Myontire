package com.example.myontire;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Agus extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dethailmecanic1);

        Button chatButton = findViewById(R.id.chatags);

        chatButton.setOnClickListener(view -> {
            Intent intent = new Intent(Agus.this, DataBooking.class);
            startActivity(intent);
        });
    }
}
