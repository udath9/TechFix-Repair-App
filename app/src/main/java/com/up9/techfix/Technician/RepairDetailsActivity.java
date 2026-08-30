package com.up9.techfix.Technician;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class RepairDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repair_details);

        Button btnUpdateRepair = findViewById(R.id.btnUpdateRepair);

        btnUpdateRepair.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RepairDetailsActivity.this,
                    UpdateRepairActivity.class
            );

            startActivity(intent);
        });
    }
}