package com.up9.techfix.Technician;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;

public class AssignedRepairsActivity extends AppCompatActivity {

    private RecyclerView recyclerAssignedRepairs;
    private TextView txtNoAssignedRepairs;

    private DatabaseHelper databaseHelper;

    private TechnicianRepairAdapter technicianRepairAdapter;

    private Cursor currentCursor;

    private int technicianId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_assigned_repairs
        );

        recyclerAssignedRepairs =
                findViewById(
                        R.id.recyclerAssignedRepairs
                );

        txtNoAssignedRepairs =
                findViewById(
                        R.id.txtNoAssignedRepairs
                );

        databaseHelper =
                new DatabaseHelper(this);

        technicianId =
                getSharedPreferences(
                        "TechFixSession",
                        MODE_PRIVATE
                ).getInt(
                        "technicianId",
                        -1
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

        recyclerAssignedRepairs.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadAssignedRepairs();
    }

    private void loadAssignedRepairs() {

        closeCurrentCursor();

        currentCursor =
                databaseHelper.getAssignedRepairsForTechnician(
                        technicianId
                );

        if (currentCursor == null ||
                currentCursor.getCount() == 0) {

            txtNoAssignedRepairs.setText(
                    "No repairs are currently assigned to you."
            );

            txtNoAssignedRepairs.setVisibility(
                    TextView.VISIBLE
            );

            recyclerAssignedRepairs.setVisibility(
                    RecyclerView.GONE
            );

            closeCurrentCursor();

            return;
        }

        txtNoAssignedRepairs.setVisibility(
                TextView.GONE
        );

        recyclerAssignedRepairs.setVisibility(
                RecyclerView.VISIBLE
        );

        technicianRepairAdapter =
                new TechnicianRepairAdapter(
                        this,
                        currentCursor,
                        repairId -> {

                            Intent intent =
                                    new Intent(
                                            AssignedRepairsActivity.this,
                                            RepairDetailsActivity.class
                                    );

                            intent.putExtra(
                                    "repair_id",
                                    repairId
                            );

                            intent.putExtra(
                                    "technician_id",
                                    technicianId
                            );

                            startActivity(intent);
                        }
                );

        recyclerAssignedRepairs.setAdapter(
                technicianRepairAdapter
        );
    }

    private void closeCurrentCursor() {

        if (currentCursor != null &&
                !currentCursor.isClosed()) {

            currentCursor.close();
        }

        currentCursor = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null &&
                technicianId != -1) {

            loadAssignedRepairs();
        }
    }

    @Override
    protected void onDestroy() {

        closeCurrentCursor();

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}