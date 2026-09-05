package com.up9.techfix.Technician;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;

public class TechnicianDashboardActivity extends AppCompatActivity {

    private TextView txtWelcome;
    private TextView txtAssignedCount;
    private TextView txtInProgressCount;
    private TextView txtWaitingCount;
    private TextView txtCompletedCount;

    private Button btnAssignedRepairs;
    private Button btnRepairHistory;
    private Button btnLogout;

    private DatabaseHelper databaseHelper;

    private int technicianId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_dashboard);

        databaseHelper = new DatabaseHelper(this);

        SharedPreferences preferences =
                getSharedPreferences(
                        "TechFixSession",
                        MODE_PRIVATE
                );

        technicianId = preferences.getInt(
                "technicianId",
                -1
        );

        String technicianName =
                preferences.getString(
                        "technicianName",
                        "Technician"
                );

        if (technicianId == -1) {

            Toast.makeText(
                    this,
                    "Technician session not found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        initializeViews();

        txtWelcome.setText(
                "Welcome, " + technicianName
        );

        setupButtons();

        loadDashboardCounts();
    }

    private void initializeViews() {

        txtWelcome = findViewById(
                R.id.txtWelcome
        );

        txtAssignedCount = findViewById(
                R.id.txtAssignedCount
        );

        txtInProgressCount = findViewById(
                R.id.txtInProgressCount
        );

        txtWaitingCount = findViewById(
                R.id.txtWaitingCount
        );

        txtCompletedCount = findViewById(
                R.id.txtCompletedCount
        );

        btnAssignedRepairs = findViewById(
                R.id.btnAssignedRepairs
        );

        btnRepairHistory = findViewById(
                R.id.btnRepairHistory
        );

        btnLogout = findViewById(
                R.id.btnLogout
        );
    }

    private void setupButtons() {

        btnAssignedRepairs.setOnClickListener(v -> {

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

        btnLogout.setOnClickListener(v -> logout());
    }

    private void loadDashboardCounts() {

        int assigned =
                databaseHelper.getTechnicianRepairCountByStatus(
                        technicianId,
                        "Assigned"
                );

        int inProgress =
                databaseHelper.getTechnicianRepairCountByStatus(
                        technicianId,
                        "In Progress"
                );

        int waiting =
                databaseHelper.getTechnicianRepairCountByStatus(
                        technicianId,
                        "Waiting for Parts"
                );

        int completed =
                databaseHelper.getTechnicianRepairCountByStatus(
                        technicianId,
                        "Completed"
                );

        txtAssignedCount.setText(
                String.valueOf(assigned)
        );

        txtInProgressCount.setText(
                String.valueOf(inProgress)
        );

        txtWaitingCount.setText(
                String.valueOf(waiting)
        );

        txtCompletedCount.setText(
                String.valueOf(completed)
        );
    }

    private void logout() {

        getSharedPreferences(
                "TechFixSession",
                MODE_PRIVATE
        )
                .edit()
                .clear()
                .apply();

        Intent intent = new Intent(
                TechnicianDashboardActivity.this,
                com.up9.techfix.ActorCustomer.auth.LoginActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null
                && technicianId != -1) {

            loadDashboardCounts();
        }
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}