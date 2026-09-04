package com.up9.techfix.Technician;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;

import java.util.Locale;

public class UpdateRepairActivity extends AppCompatActivity {


    private Spinner spinnerStatus;

    private EditText etNotes;
    private EditText etSparePart;
    private EditText etQuantity;

    private Button btnSaveUpdate;
    private Button btnTakePhoto;

    private ImageView ivRepairPhoto;

    private DatabaseHelper dbHelper;

    private int repairId = -1;

    private int technicianId = -1;

    private Bitmap repairPhotoBitmap;

    private final String[] statuses = {
            "Assigned",
            "In Progress",
            "Waiting for Parts",
            "Testing",
            "Completed",
            "Ready for Collection"
    };

    private final ActivityResultLauncher<Void> cameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.TakePicturePreview(),
                    bitmap -> {

                        if (bitmap != null) {

                            repairPhotoBitmap = bitmap;

                            ivRepairPhoto.setImageBitmap(
                                    bitmap
                            );
                        }
                    }
            );

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_update_repair
        );

        dbHelper =
                new DatabaseHelper(this);

        initializeViews();

        readRepairData();

        if (repairId <= 0) {

            Toast.makeText(
                    this,
                    "Repair information not found.",
                    Toast.LENGTH_LONG
            ).show();

            finish();

            return;
        }

        setupStatusSpinner();

        btnTakePhoto.setOnClickListener(
                v -> openCamera()
        );

        btnSaveUpdate.setOnClickListener(
                v -> saveRepairUpdate()
        );
    }

    private void initializeViews() {

        spinnerStatus =
                findViewById(
                        R.id.spinnerStatus
                );

        etNotes =
                findViewById(
                        R.id.etNotes
                );

        etSparePart =
                findViewById(
                        R.id.etSparePart
                );

        etQuantity =
                findViewById(
                        R.id.etQuantity
                );

        btnSaveUpdate =
                findViewById(
                        R.id.btnSaveUpdate
                );

        ivRepairPhoto =
                findViewById(
                        R.id.ivRepairPhoto
                );

        btnTakePhoto =
                findViewById(
                        R.id.btnTakePhoto
                );
    }


    private void readRepairData() {

        repairId =
                getIntent().getIntExtra(
                        "repair_id",
                        -1
                );


        technicianId =
                getIntent().getIntExtra(
                        "technician_id",
                        -1
                );

        if (technicianId <= 0) {

            android.content.SharedPreferences preferences =
                    getSharedPreferences(
                            "TechFixSession",
                            MODE_PRIVATE
                    );

            technicianId =
                    preferences.getInt(
                            "technicianId",
                            -1
                    );
        }
    }


    private void setupStatusSpinner() {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        statuses
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerStatus.setAdapter(
                adapter
        );

        String currentStatus =
                getIntent().getStringExtra(
                        "status"
                );

        if (currentStatus != null &&
                !currentStatus.trim().isEmpty()) {

            selectStatus(
                    currentStatus
            );
        }
    }

    private void selectStatus(
            String status
    ) {

        if (status == null) {
            return;
        }

        String normalized =
                status.trim();

        if ("Ready for Pickup"
                .equalsIgnoreCase(
                        normalized
                )) {

            normalized =
                    "Ready for Collection";
        }

        for (int i = 0;
             i < statuses.length;
             i++) {

            if (statuses[i]
                    .equalsIgnoreCase(
                            normalized
                    )) {

                spinnerStatus.setSelection(i);

                return;
            }
        }
    }

    private void openCamera() {

        cameraLauncher.launch(null);
    }

    private void saveRepairUpdate() {

        if (repairId <= 0) {

            Toast.makeText(
                    this,
                    "Invalid repair.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (technicianId <= 0) {

            Toast.makeText(
                    this,
                    "Technician information not found.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        Object selectedStatus =
                spinnerStatus.getSelectedItem();

        if (selectedStatus == null) {

            Toast.makeText(
                    this,
                    "Please select a repair status.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String status =
                selectedStatus
                        .toString()
                        .trim();

        String notes =
                etNotes.getText()
                        .toString()
                        .trim();

        if (notes.isEmpty()) {

            etNotes.setError(
                    "Please enter technician notes"
            );

            etNotes.requestFocus();

            return;
        }

        String sparePart =
                etSparePart.getText()
                        .toString()
                        .trim();

        String quantityText =
                etQuantity.getText()
                        .toString()
                        .trim();

        int quantity = 0;

        if (!quantityText.isEmpty()) {

            try {

                quantity =
                        Integer.parseInt(
                                quantityText
                        );

            } catch (NumberFormatException e) {

                etQuantity.setError(
                        "Enter a valid quantity"
                );

                etQuantity.requestFocus();

                return;
            }

            if (quantity < 0) {

                etQuantity.setError(
                        "Quantity cannot be negative"
                );

                etQuantity.requestFocus();

                return;
            }
        }
        String photoUri = "";


        String updateDate =
                String.valueOf(
                        System.currentTimeMillis()
                );

        boolean updated =
                dbHelper.updateRepairStatus(
                        repairId,
                        status
                );

        if (!updated) {

            Toast.makeText(
                    this,
                    "Unable to update repair status.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        long updateResult =
                dbHelper.insertRepairUpdate(
                        repairId,
                        technicianId,
                        status,
                        notes,
                        sparePart,
                        quantity,
                        photoUri,
                        updateDate
                );

        if (updateResult != -1) {

            Toast.makeText(
                    this,
                    "Repair updated successfully.",
                    Toast.LENGTH_SHORT
            ).show();

            setResult(
                    RESULT_OK
            );

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Repair status updated, but update history could not be saved.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    @Override
    protected void onDestroy() {

        if (dbHelper != null) {

            dbHelper.close();
        }

        super.onDestroy();
    }
}