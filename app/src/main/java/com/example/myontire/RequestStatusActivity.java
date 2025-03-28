package com.example.myontire;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import android.os.Handler;

public class RequestStatusActivity extends AppCompatActivity {

    private TextView statusText, estimatedTime;
    private ProgressBar statusProgressBar;
    private Button cancelRequestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_status_layout);

        // Initialize UI components
        statusText = findViewById(R.id.requestStatusText);
        estimatedTime = findViewById(R.id.requestEstimatedTime);
        statusProgressBar = findViewById(R.id.statusProgressBar);
        cancelRequestButton = findViewById(R.id.cancelRequestButton);

        // Intent otomatis setelah 5 detik
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(RequestStatusActivity.this, pagewa.class);
                startActivity(intent);
                finish();
            }
        }, 5000); // 5000 ms = 5 detik

        // Cancel request action
        cancelRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusText.setText("Request Canceled");
                estimatedTime.setVisibility(View.GONE);
                statusProgressBar.setVisibility(View.GONE);
                Toast.makeText(RequestStatusActivity.this, "Request has been canceled", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(RequestStatusActivity.this, homepage.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
            }
        });
    }
}
