package com.up9.techfix.Technician;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class TechnicianDashboardActivity
        extends AppCompatActivity {

    Button btnViewRepairs;
    Button btnRepairHistory;

    TextView tvAssignedCount;
    TextView tvProgressCount;
    TextView tvWaitingCount;
    TextView tvCompletedCount;

    TechOpenHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_technician_dashboard
        );


        dbHelper =
                new TechOpenHelper(this);


        btnViewRepairs =
                findViewById(
                        R.id.btnViewRepairs
                );

        btnRepairHistory =
                findViewById(
                        R.id.btnRepairHistory
                );


        tvAssignedCount =
                findViewById(
                        R.id.tvAssignedCount
                );

        tvProgressCount =
                findViewById(
                        R.id.tvProgressCount
                );

        tvWaitingCount =
                findViewById(
                        R.id.tvWaitingCount
                );

        tvCompletedCount =
                findViewById(
                        R.id.tvCompletedCount
                );


        btnViewRepairs.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            TechnicianDashboardActivity.this,
                            AssignedRepairsActivity.class
                    );

            startActivity(intent);
        });


        btnRepairHistory.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
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

        int assigned =
                dbHelper.getRepairCountByStatus(
                        "Assigned"
                );

        int progress =
                dbHelper.getRepairCountByStatus(
                        "In Progress"
                );

        int waiting =
                dbHelper.getRepairCountByStatus(
                        "Waiting for Parts"
                );

        int completed =
                dbHelper.getRepairCountByStatus(
                        "Completed"
                );


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
    }
}