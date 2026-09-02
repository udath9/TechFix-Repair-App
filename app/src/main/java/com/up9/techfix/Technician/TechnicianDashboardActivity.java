package com.up9.techfix.Technician;

import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class TechnicianDashboardActivity extends AppCompatActivity {

    Button btnViewRepairs;

    TechOpenHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_technician_dashboard);


        dbHelper = new TechOpenHelper(this);
        database = dbHelper.getWritableDatabase();

        Toast.makeText(
                this,
                "SQLite Database Connected",
                Toast.LENGTH_SHORT
        ).show();

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