package com.up9.techfix.Technician;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class UpdateRepairActivity extends AppCompatActivity {

    Spinner spinnerStatus;
    EditText etNotes;
    EditText etSparePart;
    EditText etQuantity;
    Button btnSaveUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_repair);

        spinnerStatus = findViewById(R.id.spinnerStatus);
        etNotes = findViewById(R.id.etNotes);
        etSparePart = findViewById(R.id.etSparePart);
        etQuantity = findViewById(R.id.etQuantity);
        btnSaveUpdate = findViewById(R.id.btnSaveUpdate);

        String[] statuses = {
                "Assigned",
                "Diagnosing",
                "Repairing",
                "Waiting for Parts",
                "Testing",
                "Completed",
                "Ready for Collection"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statuses
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerStatus.setAdapter(adapter);

        btnSaveUpdate.setOnClickListener(v -> {

            String status = spinnerStatus.getSelectedItem().toString();
            String notes = etNotes.getText().toString().trim();

            if (notes.isEmpty()) {
                etNotes.setError("Please enter technician notes");
                return;
            }

            Toast.makeText(
                    UpdateRepairActivity.this,
                    "Repair updated: " + status,
                    Toast.LENGTH_SHORT
            ).show();
        });
    }
}