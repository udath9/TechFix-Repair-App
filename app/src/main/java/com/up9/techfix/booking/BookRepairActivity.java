package com.up9.techfix.booking;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class BookRepairActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private ActivityResultLauncher<String> cameraPermissionLauncher;
    private ActivityResultLauncher<Void> cameraLauncher;
    private TextView tvNearestBranch;
    private Spinner spinnerDeviceCategory;
    private Spinner spinnerService;
    private EditText etDeviceModel;
    private EditText etProblemDescription;
    private Button btnLocation;
    private Button btnUploadImage;
    private ImageView ivDeviceImage;
    private Button btnSubmitRepair;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_repair);

        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        spinnerDeviceCategory = findViewById(R.id.spinnerDeviceCategory);
        spinnerService = findViewById(R.id.spinnerService);
        etDeviceModel = findViewById(R.id.etDeviceModel);
        etProblemDescription = findViewById(R.id.etProblemDescription);
        btnLocation = findViewById(R.id.btnLocation);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnSubmitRepair = findViewById(R.id.btnSubmitRepair);
        tvNearestBranch = findViewById(R.id.tvNearestBranch);
        ivDeviceImage = findViewById(R.id.ivDeviceImage);

        setupDeviceCategories();
        setupServices();

        btnSubmitRepair.setOnClickListener(v -> {
            submitRepairRequest();
        });

        btnLocation.setOnClickListener(v -> {
            getCurrentLocation();
        });

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {

                    if (uri != null) {
                        ivDeviceImage.setImageURI(uri);
                        ivDeviceImage.setVisibility(View.VISIBLE);

                        Toast.makeText(
                                this,
                                "Device image selected!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {

                    if (isGranted) {
                        cameraLauncher.launch(null);
                    } else {
                        Toast.makeText(
                                this,
                                "Camera permission is required to take a photo.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                bitmap -> {

                    if (bitmap != null) {
                        ivDeviceImage.setImageBitmap(bitmap);
                        ivDeviceImage.setVisibility(View.VISIBLE);

                        Toast.makeText(
                                this,
                                "Photo captured!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
        btnUploadImage.setOnClickListener(v -> {

            String[] options = {
                    "Take Photo",
                    "Choose from Gallery"
            };

            new AlertDialog.Builder(this)
                    .setTitle("Upload Device Image")
                    .setItems(options, (dialog, which) -> {

                        if (which == 0) {

                            if (ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED) {

                                cameraLauncher.launch(null);

                            } else {

                                cameraPermissionLauncher.launch(
                                        Manifest.permission.CAMERA
                                );
                            }

                        } else {

                            imagePickerLauncher.launch("image/*");
                        }
                    })
                    .show();
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
    private void getCurrentLocation() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    100
            );

            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {

                    if (location != null) {

                        double customerLatitude = location.getLatitude();
                        double customerLongitude = location.getLongitude();

                        // TechFix Colombo
                        double colomboLatitude = 6.9271;
                        double colomboLongitude = 79.8612;

                        // TechFix Galle
                        double galleLatitude = 6.0329;
                        double galleLongitude = 80.2168;

                        float[] colomboDistance = new float[1];
                        float[] galleDistance = new float[1];

                        Location.distanceBetween(
                                customerLatitude,
                                customerLongitude,
                                colomboLatitude,
                                colomboLongitude,
                                colomboDistance
                        );

                        Location.distanceBetween(
                                customerLatitude,
                                customerLongitude,
                                galleLatitude,
                                galleLongitude,
                                galleDistance
                        );

                        String nearestBranch;
                        float nearestDistance;

                        if (colomboDistance[0] < galleDistance[0]) {
                            nearestBranch = "Colombo";
                            nearestDistance = colomboDistance[0];
                        } else {
                            nearestBranch = "Galle";
                            nearestDistance = galleDistance[0];
                        }

                        double distanceInKm = nearestDistance / 1000.0;

                        tvNearestBranch.setText(
                                "Nearest TechFix Branch: " + nearestBranch
                                        + "\nDistance: "
                                        + String.format("%.2f", distanceInKm)
                                        + " km"
                        );

                        tvNearestBranch.setVisibility(View.VISIBLE);

                    } else {

                        Toast.makeText(
                                this,
                                "Unable to get your location.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}