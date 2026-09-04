package com.up9.techfix.Technician;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;

public class TechnicianDashboardActivity extends AppCompatActivity {

    private Button btnViewRepairs;
    private Button btnRepairHistory;

    private TextView tvAssignedCount;
    private TextView tvProgressCount;
    private TextView tvWaitingCount;
    private TextView tvCompletedCount;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_technician_dashboard);

        databaseHelper = new DatabaseHelper(this);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {

        btnViewRepairs = findViewById(R.id.btnViewRepairs);
        btnRepairHistory = findViewById(R.id.btnRepairHistory);

        tvAssignedCount = findViewById(R.id.tvAssignedCount);
        tvProgressCount = findViewById(R.id.tvProgressCount);
        tvWaitingCount = findViewById(R.id.tvWaitingCount);
        tvCompletedCount = findViewById(R.id.tvCompletedCount);
    }

    private void setupListeners() {

        btnViewRepairs.setOnClickListener(v -> {

            Intent intent = new Intent(
                    TechnicianDashboardActivity.this,
                    AssignedRepairsActivity.class
            );

            startActivity(intent);
        });

        btnRepairHistory.setOnClickListener(v -> {

            Intent intent = new Intent(
                    TechnicianDashboardActivity.this,
                    RepairHistoryActivity.class
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadDashboardCounts();
    }

    private void loadDashboardCounts() {

        try {

            int assigned =
                    databaseHelper.getRepairCountByStatus("Assigned");

            int progress =
                    databaseHelper.getRepairCountByStatus("In Progress");

            int waiting =
                    databaseHelper.getRepairCountByStatus("Waiting for Parts");

            int completed =
                    databaseHelper.getRepairCountByStatus("Completed");

            tvAssignedCount.setText(
                    String.valueOf(assigned)
            );

            tvProgressCount.setText(
                    String.valueOf(progress)
            );

            tvWaitingCount.setText(
                    String.valueOf(waiting)
            );

            tvCompletedCount.setText(
                    String.valueOf(completed)
            );

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Unable to load repair statistics",
                    Toast.LENGTH_SHORT
            ).show();

            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}