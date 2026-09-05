package com.up9.techfix.Technician;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateRepairActivity extends AppCompatActivity {

    private static final String TAG =
            "UpdateRepairActivity";

    private TextView tvDevice;
    private Spinner spinnerStatus;

    private EditText etNotes;
    private EditText etSparePart;
    private EditText etQuantity;

    private ImageView ivRepairPhoto;

    private Button btnTakePhoto;
    private Button btnSaveUpdate;


    private DatabaseHelper databaseHelper;

    private int repairId = -1;
    private int technicianId = -1;


    private String photoUri = "";

    private final ActivityResultLauncher<Void> cameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.TakePicturePreview(),
                    bitmap -> {

                        if (bitmap == null) {

                            Toast.makeText(
                                    this,
                                    "No photo was captured.",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        ivRepairPhoto.setImageBitmap(
                                bitmap
                        );

                        ivRepairPhoto.setVisibility(
                                ImageView.VISIBLE
                        );


                        String savedPath =
                                saveRepairPhoto(
                                        bitmap
                                );

                        if (savedPath == null) {

                            photoUri = "";

                            Toast.makeText(
                                    this,
                                    "Photo was captured but could not be saved.",
                                    Toast.LENGTH_LONG
                            ).show();

                            return;
                        }

                        photoUri =
                                savedPath;

                        Toast.makeText(
                                this,
                                "Repair photo saved.",
                                Toast.LENGTH_SHORT
                        ).show();
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

        databaseHelper =
                new DatabaseHelper(this);

        repairId =
                getIntent().getIntExtra(
                        "repair_id",
                        -1
                );

        SharedPreferences preferences =
                getSharedPreferences(
                        "TechFixSession",
                        MODE_PRIVATE
                );

        technicianId =
                getIntent().getIntExtra(
                        "technician_id",
                        preferences.getInt(
                                "technicianId",
                                -1
                        )
                );

        if (repairId <= 0) {

            Toast.makeText(
                    this,
                    "Invalid repair.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        if (technicianId <= 0) {

            Toast.makeText(
                    this,
                    "Technician session not found.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        initializeViews();

        setupStatusSpinner();

        loadDeviceDetails();
    }

    private void initializeViews() {

        tvDevice =
                findViewById(
                        R.id.tvDevice
                );

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

        ivRepairPhoto =
                findViewById(
                        R.id.ivRepairPhoto
                );

        btnTakePhoto =
                findViewById(
                        R.id.btnTakePhoto
                );

        btnSaveUpdate =
                findViewById(
                        R.id.btnSaveUpdate
                );

        btnTakePhoto.setOnClickListener(
                v -> cameraLauncher.launch(null)
        );

        btnSaveUpdate.setOnClickListener(
                v -> saveRepairUpdate()
        );
    }


    private void setupStatusSpinner() {

        String[] statuses = {

                "Assigned",

                "In Progress",

                "Waiting for Parts",

                "Testing",

                "Completed",

                "Ready for Collection"
        };

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
    }

    private void loadDeviceDetails() {

        android.database.Cursor cursor =
                databaseHelper.getRepairById(
                        repairId
                );

        if (cursor == null ||
                !cursor.moveToFirst()) {

            Toast.makeText(
                    this,
                    "Repair not found.",
                    Toast.LENGTH_SHORT
            ).show();

            if (cursor != null) {

                cursor.close();
            }

            finish();

            return;
        }
        int deviceIndex =
                cursor.getColumnIndex(
                        "device_model"
                );

        if (deviceIndex != -1 &&
                !cursor.isNull(deviceIndex)) {

            tvDevice.setText(
                    cursor.getString(
                            deviceIndex
                    )
            );

        } else {

            tvDevice.setText(
                    "Device not available"
            );
        }

        int statusIndex =
                cursor.getColumnIndex(
                        "status"
                );

        if (statusIndex != -1 &&
                !cursor.isNull(statusIndex)) {

            String currentStatus =
                    cursor.getString(
                            statusIndex
                    );

            ArrayAdapter<String> adapter =
                    (ArrayAdapter<String>)
                            spinnerStatus.getAdapter();

            int spinnerPosition =
                    adapter.getPosition(
                            currentStatus
                    );

            if (spinnerPosition >= 0) {

                spinnerStatus.setSelection(
                        spinnerPosition
                );
            }
        }

        cursor.close();
    }

    private void saveRepairUpdate() {
        String status =
                spinnerStatus
                        .getSelectedItem()
                        .toString()
                        .trim();

        String notes =
                etNotes
                        .getText()
                        .toString()
                        .trim();

        String sparePart =
                etSparePart
                        .getText()
                        .toString()
                        .trim();

        String quantityText =
                etQuantity
                        .getText()
                        .toString()
                        .trim();

        if (notes.isEmpty()) {

            etNotes.setError(
                    "Please enter repair notes."
            );

            etNotes.requestFocus();

            return;
        }

        int quantity = 0;

        if (!quantityText.isEmpty()) {

            try {

                quantity =
                        Integer.parseInt(
                                quantityText
                        );

                if (quantity < 0) {

                    etQuantity.setError(
                            "Quantity cannot be negative."
                    );

                    etQuantity.requestFocus();

                    return;
                }

            } catch (NumberFormatException e) {

                etQuantity.setError(
                        "Enter a valid quantity."
                );

                etQuantity.requestFocus();

                return;
            }
        }

        String updateDate =
                new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()
                ).format(
                        new Date()
                );

        boolean statusUpdated =
                databaseHelper.updateRepairStatus(
                        repairId,
                        status
                );

        if (!statusUpdated) {

            Toast.makeText(
                    this,
                    "Failed to update repair status.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (!photoUri.isEmpty()) {

            boolean photoUpdated =
                    databaseHelper.updateRepairInProgressPhoto(
                            repairId,
                            photoUri
                    );

            if (!photoUpdated) {

                Log.e(
                        TAG,
                        "Failed to save in-progress photo to repairs table."
                );
            }
        }

        long updateResult =
                databaseHelper.insertRepairUpdate(
                        repairId,
                        technicianId,
                        status,
                        notes,
                        sparePart,
                        quantity,
                        photoUri,
                        updateDate
                );

        if (updateResult == -1) {

            Toast.makeText(
                    this,
                    "Status updated, but repair history could not be saved.",
                    Toast.LENGTH_LONG
            ).show();

        } else {

            Toast.makeText(
                    this,
                    "Repair updated successfully.",
                    Toast.LENGTH_SHORT
            ).show();
        }

        Intent intent =
                new Intent(
                        UpdateRepairActivity.this,
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

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
        );

        startActivity(
                intent
        );

        finish();
    }

    private String saveRepairPhoto(
            Bitmap bitmap
    ) {

        if (bitmap == null) {

            return null;
        }

        FileOutputStream outputStream =
                null;

        try {

            File imageDirectory =
                    new File(
                            getFilesDir(),
                            "repair_updates"
                    );

            if (!imageDirectory.exists()) {

                boolean created =
                        imageDirectory.mkdirs();

                if (!created &&
                        !imageDirectory.exists()) {

                    return null;
                }
            }

            String fileName =
                    "repair_"
                            + repairId
                            + "_"
                            + System.currentTimeMillis()
                            + ".jpg";

            File imageFile =
                    new File(
                            imageDirectory,
                            fileName
                    );

            outputStream =
                    new FileOutputStream(
                            imageFile
                    );

            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    outputStream
            );

            outputStream.flush();

            return imageFile.getAbsolutePath();

        } catch (Exception e) {

            Log.e(
                    TAG,
                    "Failed to save repair photo.",
                    e
            );

            return null;

        } finally {

            if (outputStream != null) {

                try {

                    outputStream.close();

                } catch (Exception ignored) {
                }
            }
        }
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {

            databaseHelper.close();

            databaseHelper = null;
        }

        super.onDestroy();
    }
}