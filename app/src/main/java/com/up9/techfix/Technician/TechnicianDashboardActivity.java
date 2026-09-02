package com.up9.techfix.Technician;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class TechnicianDashboardActivity extends AppCompatActivity {

    Button btnViewRepairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_technician_dashboard);


        btnViewRepairs = findViewById(R.id.btnViewRepairs);


        btnViewRepairs.setOnClickListener(v -> {

            Intent intent = new Intent(
                    TechnicianDashboardActivity.this,
                    AssignedRepairsActivity.class
            );

            startActivity(intent);
        });
    }
}