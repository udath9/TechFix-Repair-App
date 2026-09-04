package com.up9.techfix.Technician;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class InProgressRepairsActivity extends AppCompatActivity {

    Button btnViewDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_in_progress_repairs);

        btnViewDetails = findViewById(R.id.btnViewDetails);

        btnViewDetails.setOnClickListener(v -> {

            Intent intent = new Intent(
                    InProgressRepairsActivity.this,
                    RepairDetailsActivity.class
            );

            startActivity(intent);
        });
    }
}