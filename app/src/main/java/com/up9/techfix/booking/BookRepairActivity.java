package com.up9.techfix.booking;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class BookRepairActivity extends AppCompatActivity {

    private Spinner spinnerDeviceCategory;
    private Spinner spinnerService;
    private EditText etDeviceModel;
    private EditText etProblemDescription;
    private Button btnLocation;
    private Button btnUploadImage;
    private Button btnSubmitRepair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_repair);

        spinnerDeviceCategory = findViewById(R.id.spinnerDeviceCategory);
        spinnerService = findViewById(R.id.spinnerService);
        etDeviceModel = findViewById(R.id.etDeviceModel);
        etProblemDescription = findViewById(R.id.etProblemDescription);
        btnLocation = findViewById(R.id.btnLocation);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnSubmitRepair = findViewById(R.id.btnSubmitRepair);

        setupDeviceCategories();
        setupServices();

        btnSubmitRepair.setOnClickListener(v -> {
            submitRepairRequest();
        });

        btnLocation.setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "GPS feature will be added next.",
                    Toast.LENGTH_SHORT
            ).show();
        });

        btnUploadImage.setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Camera/Gallery feature will be added next.",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    private void setupDeviceCategories() {

        String[] deviceCategories = {
                "Select Device Category",
                "Computer",
                "Laptop",
                "Mobile Phone",
                "Tablet"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                deviceCategories
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerDeviceCategory.setAdapter(adapter);
    }

    private void setupServices() {

        String[] services = {
                "Select Repair Service",
                "Screen Replacement",
                "Battery Replacement",
                "Operating System Repair",
                "Hardware Repair",
                "Software Troubleshooting",
                "Virus/Malware Removal"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                services
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerService.setAdapter(adapter);
    }

    private void submitRepairRequest() {

        String deviceModel = etDeviceModel.getText().toString().trim();
        String problemDescription =
                etProblemDescription.getText().toString().trim();

        if (spinnerDeviceCategory.getSelectedItemPosition() == 0) {
            Toast.makeText(
                    this,
                    "Please select a device category",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (spinnerService.getSelectedItemPosition() == 0) {
            Toast.makeText(
                    this,
                    "Please select a repair service",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (deviceModel.isEmpty()) {
            etDeviceModel.setError("Please enter your device model");
            etDeviceModel.requestFocus();
            return;
        }

        if (problemDescription.isEmpty()) {
            etProblemDescription.setError(
                    "Please describe the problem"
            );
            etProblemDescription.requestFocus();
            return;
        }

        String selectedCategory =
                spinnerDeviceCategory.getSelectedItem().toString();

        String selectedService =
                spinnerService.getSelectedItem().toString();

        Toast.makeText(
                this,
                "Repair request ready for " +
                        selectedCategory + " - " +
                        selectedService,
                Toast.LENGTH_LONG
        ).show();
    }
}