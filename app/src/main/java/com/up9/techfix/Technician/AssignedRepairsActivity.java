package com.up9.techfix.Technician;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class AssignedRepairsActivity extends AppCompatActivity {

    Button btnRepair1;
    Button btnRepair2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_assigned_repairs);

        btnRepair1 = findViewById(R.id.btnRepair1);
        btnRepair2 = findViewById(R.id.btnRepair2);


        btnRepair1.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AssignedRepairsActivity.this,
                    RepairDetailsActivity.class
            );

            intent.putExtra("repair_id", 1);

            startActivity(intent);
        });


        btnRepair2.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AssignedRepairsActivity.this,
                    RepairDetailsActivity.class
            );

            intent.putExtra("repair_id", 2);

            startActivity(intent);
        });
    }
}