package com.up9.techfix.booking;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;
import com.up9.techfix.data.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookRepairActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;

    private ActivityResultLauncher<String> imagePickerLauncher;
    private ActivityResultLauncher<String> cameraPermissionLauncher;
    private ActivityResultLauncher<Void> cameraLauncher;

    private DatabaseHelper databaseHelper;

    private TextView tvNearestBranch;
    private Spinner spinnerDeviceCategory;
    private Spinner spinnerService;
    private EditText etDeviceModel;
    private EditText etProblemDescription;
    private Button btnLocation;
    private Button btnUploadImage;
    private ImageView ivDeviceImage;
    private Button btnSubmitRepair;

    // Store services loaded from database
    private List<Service> serviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_repair);

        databaseHelper = new DatabaseHelper(this);

        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        spinnerDeviceCategory =
                findViewById(R.id.spinnerDeviceCategory);

        spinnerService =
                findViewById(R.id.spinnerService);

        etDeviceModel =
                findViewById(R.id.etDeviceModel);

        etProblemDescription =
                findViewById(R.id.etProblemDescription);

        btnLocation =
                findViewById(R.id.btnLocation);

        btnUploadImage =
                findViewById(R.id.btnUploadImage);

        btnSubmitRepair =
                findViewById(R.id.btnSubmitRepair);

        tvNearestBranch =
                findViewById(R.id.tvNearestBranch);

        ivDeviceImage =
                findViewById(R.id.ivDeviceImage);


        // Setup dropdowns
        setupDeviceCategories();
        setupServices();


        // Get service selected from ServiceDetailsActivity
        int selectedServiceId =
                getIntent().getIntExtra("serviceId", -1);

        if (selectedServiceId != -1) {

            for (int i = 0; i < serviceList.size(); i++) {

                if (serviceList.get(i).getId() == selectedServiceId) {

                    // +1 because position 0 is "Select Repair Service"
                    spinnerService.setSelection(i + 1);

                    break;
                }
            }
        }


        // Submit repair
        btnSubmitRepair.setOnClickListener(v -> {
            submitRepairRequest();
        });


        // Location
        btnLocation.setOnClickListener(v -> {
            getCurrentLocation();
        });


        // Gallery
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


        // Camera permission
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


        // Camera
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


        // Upload image button
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

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
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

        // Get services directly from SQLite
        serviceList =
                databaseHelper.getAllServices();

        List<String> serviceNames =
                new ArrayList<>();

        // First item
        serviceNames.add("Select Repair Service");

        // Add database services
        for (Service service : serviceList) {

            serviceNames.add(
                    service.getName()
            );
        }


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        serviceNames
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerService.setAdapter(adapter);


        if (serviceList.isEmpty()) {

            Toast.makeText(
                    this,
                    "No repair services found in database.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    private void submitRepairRequest() {

        String deviceModel =
                etDeviceModel.getText()
                        .toString()
                        .trim();

        String problemDescription =
                etProblemDescription.getText()
                        .toString()
                        .trim();


        // Check device category
        if (spinnerDeviceCategory.getSelectedItemPosition() == 0) {

            Toast.makeText(
                    this,
                    "Please select a device category",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // Check service
        if (spinnerService.getSelectedItemPosition() == 0) {

            Toast.makeText(
                    this,
                    "Please select a repair service",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // Check device model
        if (deviceModel.isEmpty()) {

            etDeviceModel.setError(
                    "Please enter your device model"
            );

            etDeviceModel.requestFocus();

            return;
        }


        // Check problem
        if (problemDescription.isEmpty()) {

            etProblemDescription.setError(
                    "Please describe the problem"
            );

            etProblemDescription.requestFocus();

            return;
        }


        String selectedCategory =
                spinnerDeviceCategory
                        .getSelectedItem()
                        .toString();


        int selectedPosition =
                spinnerService.getSelectedItemPosition();


        // Get actual Service object from database
        Service selectedService =
                serviceList.get(selectedPosition - 1);


        String selectedServiceName =
                selectedService.getName();


        int selectedServiceId =
                selectedService.getId();


        Toast.makeText(
                this,
                "Repair request ready:\n" +
                        selectedCategory +
                        " - " +
                        selectedServiceName +
                        "\nService ID: " +
                        selectedServiceId,
                Toast.LENGTH_LONG
        ).show();
    }


    private void getCurrentLocation() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
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


        fusedLocationClient
                .getLastLocation()
                .addOnSuccessListener(
                        this,
                        location -> {

                            if (location != null) {

                                double customerLatitude =
                                        location.getLatitude();

                                double customerLongitude =
                                        location.getLongitude();


                                // TechFix Colombo
                                double colomboLatitude = 6.9271;
                                double colomboLongitude = 79.8612;


                                // TechFix Galle
                                double galleLatitude = 6.0329;
                                double galleLongitude = 80.2168;


                                float[] colomboDistance =
                                        new float[1];

                                float[] galleDistance =
                                        new float[1];


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


                                if (colomboDistance[0]
                                        < galleDistance[0]) {

                                    nearestBranch =
                                            "Colombo";

                                    nearestDistance =
                                            colomboDistance[0];

                                } else {

                                    nearestBranch =
                                            "Galle";

                                    nearestDistance =
                                            galleDistance[0];
                                }


                                double distanceInKm =
                                        nearestDistance / 1000.0;


                                tvNearestBranch.setText(
                                        "Nearest TechFix Branch: "
                                                + nearestBranch
                                                + "\nDistance: "
                                                + String.format(
                                                Locale.getDefault(),
                                                "%.2f",
                                                distanceInKm
                                        )
                                                + " km"
                                );


                                tvNearestBranch
                                        .setVisibility(
                                                View.VISIBLE
                                        );

                            } else {

                                Toast.makeText(
                                        this,
                                        "Unable to get your location.",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                );
    }
}