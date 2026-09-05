package com.up9.techfix.Technician;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;
import com.up9.techfix.Technician.RepairHistoryAdapter;
import com.up9.techfix.data.DatabaseHelper;

public class RepairHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerRepairHistory;
    private TextView txtNoHistory;

    private DatabaseHelper databaseHelper;

    private int technicianId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_history);

        recyclerRepairHistory = findViewById(
                R.id.recyclerRepairHistory
        );

        txtNoHistory = findViewById(
                R.id.txtNoHistory
        );

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

        if (technicianId == -1) {

            Toast.makeText(
                    this,
                    "Technician session not found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        recyclerRepairHistory.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadRepairHistory();
    }

    private void loadRepairHistory() {

        Cursor cursor =
                databaseHelper.getRepairHistoryForTechnician(
                        technicianId
                );

        if (cursor == null || cursor.getCount() == 0) {

            txtNoHistory.setVisibility(
                    TextView.VISIBLE
            );

            recyclerRepairHistory.setVisibility(
                    RecyclerView.GONE
            );

            if (cursor != null) {
                cursor.close();
            }

            return;
        }

        txtNoHistory.setVisibility(
                TextView.GONE
        );

        recyclerRepairHistory.setVisibility(
                RecyclerView.VISIBLE
        );

        RepairHistoryAdapter adapter =
                new RepairHistoryAdapter(
                        this,
                        cursor
                );

        recyclerRepairHistory.setAdapter(
                adapter
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null
                && technicianId != -1) {

            loadRepairHistory();
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