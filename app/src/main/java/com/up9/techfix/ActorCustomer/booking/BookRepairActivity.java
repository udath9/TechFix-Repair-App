package com.up9.techfix.ActorCustomer.booking;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

    // =====================================================
    // LOCATION
    // =====================================================

    private FusedLocationProviderClient fusedLocationClient;

    private ActivityResultLauncher<String[]> locationPermissionLauncher;


    // =====================================================
    // IMAGE
    // =====================================================

    private ActivityResultLauncher<String> imagePickerLauncher;

    private ActivityResultLauncher<String> cameraPermissionLauncher;

    private ActivityResultLauncher<Void> cameraLauncher;


    // =====================================================
    // DATABASE
    // =====================================================

    private DatabaseHelper databaseHelper;


    // =====================================================
    // UI
    // =====================================================

    private TextView tvNearestBranch;

    private Spinner spinnerDeviceCategory;

    private Spinner spinnerService;

    private EditText etDeviceModel;

    private EditText etProblemDescription;

    private Button btnLocation;

    private Button btnUploadImage;

    private ImageView ivDeviceImage;

    private Button btnSubmitRepair;


    // =====================================================
    // CATEGORIES
    // =====================================================

    // Category names displayed in the Spinner.
    private List<String> categoryNames =
            new ArrayList<>();

    // Category IDs corresponding to categoryNames.
    private List<Integer> categoryIds =
            new ArrayList<>();


    // =====================================================
    // SERVICES
    // =====================================================

    private List<Service> serviceList =
            new ArrayList<>();


    // =====================================================
    // SELECTED BRANCH
    // =====================================================

    private String selectedBranchName = null;

    private int selectedBranchId = -1;


    // =====================================================
    // IMAGE
    // =====================================================

    private String selectedImageUri = null;


    // =====================================================
    // CUSTOMER
    // =====================================================

    private int customerId = -1;


    // =====================================================
    // ON CREATE
    // =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_book_repair
        );


        // =================================================
        // DATABASE
        // =================================================

        databaseHelper =
                new DatabaseHelper(this);


        // =================================================
        // DEFAULT DATABASE DATA
        // =================================================

        // Make sure Colombo and Galle exist.
        databaseHelper.insertDefaultBranches();

        // Make sure categories exist.
        databaseHelper.insertDefaultCategories();


        // =================================================
        // LOCATION CLIENT
        // =================================================

        fusedLocationClient =
                LocationServices
                        .getFusedLocationProviderClient(this);


        // =================================================
        // GET CUSTOMER ID FROM SESSION
        // =================================================

        android.content.SharedPreferences preferences =
                getSharedPreferences(
                        "TechFixSession",
                        MODE_PRIVATE
                );


        customerId =
                preferences.getInt(
                        "customerId",
                        -1
                );


        // =================================================
        // FIND UI ELEMENTS
        // =================================================

        spinnerDeviceCategory =
                findViewById(
                        R.id.spinnerDeviceCategory
                );


        spinnerService =
                findViewById(
                        R.id.spinnerService
                );


        etDeviceModel =
                findViewById(
                        R.id.etDeviceModel
                );


        etProblemDescription =
                findViewById(
                        R.id.etProblemDescription
                );


        btnLocation =
                findViewById(
                        R.id.btnLocation
                );


        btnUploadImage =
                findViewById(
                        R.id.btnUploadImage
                );


        btnSubmitRepair =
                findViewById(
                        R.id.btnSubmitRepair
                );


        tvNearestBranch =
                findViewById(
                        R.id.tvNearestBranch
                );


        ivDeviceImage =
                findViewById(
                        R.id.ivDeviceImage
                );


        // =================================================
        // SETUP DROPDOWNS
        // =================================================

        setupDeviceCategories();

        setupServices();


        // =================================================
        // SERVICE PASSED FROM SERVICE DETAILS
        // =================================================

        int selectedServiceId =
                getIntent().getIntExtra(
                        "serviceId",
                        -1
                );


        if (selectedServiceId != -1) {

            for (
                    int i = 0;
                    i < serviceList.size();
                    i++
            ) {

                if (
                        serviceList
                                .get(i)
                                .getId()
                                ==
                                selectedServiceId
                ) {

                    // +1 because position 0 is
                    // "Select Repair Service".
                    spinnerService.setSelection(
                            i + 1
                    );

                    break;
                }
            }
        }


        // =================================================
        // SUBMIT REPAIR
        // =================================================

        btnSubmitRepair.setOnClickListener(
                v -> submitRepairRequest()
        );


        // =================================================
        // LOCATION BUTTON
        // =================================================

        btnLocation.setOnClickListener(
                v -> getCurrentLocation()
        );


        // =================================================
        // LOCATION PERMISSION
        // =================================================

        locationPermissionLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.RequestMultiplePermissions(),
                        result -> {

                            Boolean fineLocation =
                                    result.get(
                                            Manifest.permission
                                                    .ACCESS_FINE_LOCATION
                                    );

                            Boolean coarseLocation =
                                    result.get(
                                            Manifest.permission
                                                    .ACCESS_COARSE_LOCATION
                                    );


                            if (
                                    Boolean.TRUE.equals(
                                            fineLocation
                                    )
                                            ||
                                            Boolean.TRUE.equals(
                                                    coarseLocation
                                            )
                            ) {

                                getCurrentLocation();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Location permission is required to find the nearest branch.",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );


        // =================================================
        // IMAGE PICKER
        // =================================================

        imagePickerLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.GetContent(),
                        uri -> {

                            if (uri != null) {

                                selectedImageUri =
                                        uri.toString();


                                ivDeviceImage.setImageURI(
                                        uri
                                );


                                ivDeviceImage.setVisibility(
                                        View.VISIBLE
                                );


                                Toast.makeText(
                                        this,
                                        "Device image selected!",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                );


        // =================================================
        // CAMERA PERMISSION
        // =================================================

        cameraPermissionLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        isGranted -> {

                            if (isGranted) {

                                cameraLauncher.launch(
                                        null
                                );

                            } else {

                                Toast.makeText(
                                        this,
                                        "Camera permission is required to take a photo.",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                );


        // =================================================
        // CAMERA
        // =================================================

        cameraLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.TakePicturePreview(),
                        bitmap -> {

                            if (bitmap != null) {

                                /*
                                 * TakePicturePreview returns
                                 * a Bitmap rather than a permanent
                                 * content URI.
                                 *
                                 * Therefore this photo is displayed
                                 * but selectedImageUri remains null.
                                 */

                                selectedImageUri =
                                        null;


                                ivDeviceImage.setImageBitmap(
                                        bitmap
                                );


                                ivDeviceImage.setVisibility(
                                        View.VISIBLE
                                );


                                Toast.makeText(
                                        this,
                                        "Photo captured!",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                );


        // =================================================
        // UPLOAD IMAGE BUTTON
        // =================================================

        btnUploadImage.setOnClickListener(
                v -> {

                    String[] options = {
                            "Take Photo",
                            "Choose from Gallery"
                    };


                    new AlertDialog.Builder(this)
                            .setTitle(
                                    "Upload Device Image"
                            )
                            .setItems(
                                    options,
                                    (dialog, which) -> {

                                        if (which == 0) {

                                            // =================================================
                                            // CAMERA
                                            // =================================================

                                            if (
                                                    ContextCompat
                                                            .checkSelfPermission(
                                                                    this,
                                                                    Manifest.permission.CAMERA
                                                            )
                                                            ==
                                                            PackageManager.PERMISSION_GRANTED
                                            ) {

                                                cameraLauncher.launch(
                                                        null
                                                );

                                            } else {

                                                cameraPermissionLauncher.launch(
                                                        Manifest.permission.CAMERA
                                                );
                                            }

                                        } else {

                                            // =================================================
                                            // GALLERY
                                            // =================================================

                                            imagePickerLauncher.launch(
                                                    "image/*"
                                            );
                                        }
                                    }
                            )
                            .show();
                }
        );
    }


    // =====================================================
    // DEVICE CATEGORIES
    // =====================================================
    //
    // Categories are now loaded from the database.
    //
    // Spinner displays:
    //
    // Select Device Category
    // Computer
    // Laptop
    // Mobile Phone
    // Tablet
    //
    // But internally we store:
    //
    // category ID
    //
    // instead of the category name.
    // =====================================================

    private void setupDeviceCategories() {

        categoryNames.clear();

        categoryIds.clear();


        // First spinner item.
        categoryNames.add(
                "Select Device Category"
        );

        // Placeholder ID.
        categoryIds.add(-1);


        Cursor cursor =
                databaseHelper.getAllCategories();


        if (cursor.moveToFirst()) {

            do {

                int categoryId =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "id"
                                )
                        );


                String categoryName =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "name"
                                )
                        );


                categoryIds.add(
                        categoryId
                );


                categoryNames.add(
                        categoryName
                );

            } while (cursor.moveToNext());
        }


        cursor.close();


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categoryNames
                );


        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );


        spinnerDeviceCategory.setAdapter(
                adapter
        );


        if (categoryNames.size() == 1) {

            Toast.makeText(
                    this,
                    "No device categories found in database.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    // =====================================================
    // SERVICES
    // =====================================================

    private void setupServices() {

        serviceList =
                databaseHelper.getAllServices();


        List<String> serviceNames =
                new ArrayList<>();


        serviceNames.add(
                "Select Repair Service"
        );


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


        spinnerService.setAdapter(
                adapter
        );


        if (serviceList.isEmpty()) {

            Toast.makeText(
                    this,
                    "No repair services found in database.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    // =====================================================
    // SUBMIT REPAIR
    // =====================================================

    private void submitRepairRequest() {

        String deviceModel =
                etDeviceModel
                        .getText()
                        .toString()
                        .trim();


        String problemDescription =
                etProblemDescription
                        .getText()
                        .toString()
                        .trim();


        // =================================================
        // CUSTOMER
        // =================================================

        if (customerId == -1) {

            Toast.makeText(
                    this,
                    "Customer information not found. Please log in again.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        // =================================================
        // DEVICE CATEGORY
        // =================================================

        int categoryPosition =
                spinnerDeviceCategory
                        .getSelectedItemPosition();


        if (
                categoryPosition <= 0 ||
                        categoryPosition >= categoryIds.size()
        ) {

            Toast.makeText(
                    this,
                    "Please select a device category.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // =================================================
        // GET CATEGORY ID
        // =================================================

        int selectedCategoryId =
                categoryIds.get(
                        categoryPosition
                );


        if (selectedCategoryId == -1) {

            Toast.makeText(
                    this,
                    "Invalid device category selected.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // =================================================
        // SERVICE
        // =================================================

        int selectedPosition =
                spinnerService
                        .getSelectedItemPosition();


        if (
                selectedPosition <= 0 ||
                        selectedPosition > serviceList.size()
        ) {

            Toast.makeText(
                    this,
                    "Please select a repair service.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // =================================================
        // DEVICE MODEL
        // =================================================

        if (deviceModel.isEmpty()) {

            etDeviceModel.setError(
                    "Please enter your device model."
            );

            etDeviceModel.requestFocus();

            return;
        }


        // =================================================
        // PROBLEM
        // =================================================

        if (problemDescription.isEmpty()) {

            etProblemDescription.setError(
                    "Please describe the problem."
            );

            etProblemDescription.requestFocus();

            return;
        }


        // =================================================
        // BRANCH
        // =================================================

        if (selectedBranchId == -1) {

            Toast.makeText(
                    this,
                    "Please find the nearest branch first.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // =================================================
        // GET SERVICE
        // =================================================

        Service selectedService =
                serviceList.get(
                        selectedPosition - 1
                );


        int selectedServiceId =
                selectedService.getId();


        // =================================================
        // CREATE REPAIR
        // =================================================
        //
        // Notice:
        //
        // selectedCategoryId is passed instead of the
        // category name.
        //
        // assigned_technician_id is NOT passed.
        //
        // in_progress_photo_uri is NOT passed.
        //
        // DatabaseHelper automatically stores both as NULL.
        // =================================================

        long repairId =
                databaseHelper.createRepair(

                        customerId,

                        selectedCategoryId,

                        deviceModel,

                        selectedServiceId,

                        problemDescription,

                        selectedBranchId,

                        selectedImageUri,

                        "Pending",

                        String.valueOf(
                                System.currentTimeMillis()
                        )
                );


        // =================================================
        // SUCCESS
        // =================================================

        if (repairId != -1) {

            Toast.makeText(
                    this,
                    "Repair request submitted successfully!\n"
                            + "Repair ID: "
                            + repairId,
                    Toast.LENGTH_LONG
            ).show();


            finish();

        } else {

            Toast.makeText(
                    this,
                    "Failed to submit repair request.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    // =====================================================
    // GET CURRENT LOCATION
    // =====================================================

    private void getCurrentLocation() {

        // =================================================
        // CHECK PERMISSION
        // =================================================

        boolean fineGranted =
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                        ==
                        PackageManager.PERMISSION_GRANTED;


        boolean coarseGranted =
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                        ==
                        PackageManager.PERMISSION_GRANTED;


        if (!fineGranted && !coarseGranted) {

            locationPermissionLauncher.launch(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }
            );

            return;
        }


        // =================================================
        // GET LAST LOCATION
        // =================================================

        fusedLocationClient
                .getLastLocation()
                .addOnSuccessListener(
                        this,
                        location -> {

                            if (location == null) {

                                Toast.makeText(
                                        this,
                                        "Unable to get your location. Please make sure GPS is enabled.",
                                        Toast.LENGTH_LONG
                                ).show();

                                return;
                            }


                            // =================================================
                            // CUSTOMER LOCATION
                            // =================================================

                            double customerLatitude =
                                    location.getLatitude();


                            double customerLongitude =
                                    location.getLongitude();


                            // =================================================
                            // TECHFIX COLOMBO
                            // =================================================

                            double colomboLatitude =
                                    6.9271;


                            double colomboLongitude =
                                    79.8612;


                            // =================================================
                            // TECHFIX GALLE
                            // =================================================

                            double galleLatitude =
                                    6.0329;


                            double galleLongitude =
                                    80.2168;


                            // =================================================
                            // CALCULATE DISTANCE
                            // =================================================

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


                            // =================================================
                            // FIND NEAREST BRANCH
                            // =================================================

                            String nearestBranch;

                            float nearestDistance;


                            if (
                                    colomboDistance[0]
                                            <
                                            galleDistance[0]
                            ) {

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


                            // =================================================
                            // DISTANCE IN KM
                            // =================================================

                            double distanceInKm =
                                    nearestDistance / 1000.0;


                            // =================================================
                            // GET BRANCH ID
                            // =================================================

                            selectedBranchId =
                                    databaseHelper
                                            .getBranchIdByName(
                                                    nearestBranch
                                            );


                            selectedBranchName =
                                    nearestBranch;


                            // =================================================
                            // BRANCH FOUND
                            // =================================================

                            if (
                                    selectedBranchId != -1
                            ) {

                                tvNearestBranch.setText(

                                        "Nearest TechFix Branch: "
                                                + nearestBranch
                                                + "\n"
                                                + "Distance: "
                                                + String.format(
                                                Locale.getDefault(),
                                                "%.2f",
                                                distanceInKm
                                        )
                                                + " km"
                                );


                                Toast.makeText(
                                        this,
                                        "Nearest branch found: "
                                                + nearestBranch,
                                        Toast.LENGTH_SHORT
                                ).show();

                            }

                            // =================================================
                            // BRANCH NOT FOUND
                            // =================================================

                            else {

                                tvNearestBranch.setText(

                                        "Nearest branch: "
                                                + nearestBranch
                                                + "\n"
                                                + "Distance: "
                                                + String.format(
                                                Locale.getDefault(),
                                                "%.2f",
                                                distanceInKm
                                        )
                                                + " km"
                                                + "\n"
                                                + "Branch ID not found."
                                );


                                Toast.makeText(
                                        this,
                                        "Branch information could not be found in the database.",
                                        Toast.LENGTH_LONG
                                ).show();
                            }


                            // =================================================
                            // SHOW BRANCH TEXT
                            // =================================================

                            tvNearestBranch.setVisibility(
                                    View.VISIBLE
                            );
                        }
                );
    }
}