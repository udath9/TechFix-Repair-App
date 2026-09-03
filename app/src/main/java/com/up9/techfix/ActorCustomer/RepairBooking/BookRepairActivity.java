package com.up9.techfix.ActorCustomer.RepairBooking;

import android.Manifest;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookRepairActivity extends AppCompatActivity {

    // Location
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String[]> locationPermissionLauncher;

    // Image
    private ActivityResultLauncher<String> imagePickerLauncher;
    private ActivityResultLauncher<String> cameraPermissionLauncher;
    private ActivityResultLauncher<Void> cameraLauncher;
    private String selectedImageUri;

    // Database
    private DatabaseHelper databaseHelper;

    // UI
    private TextView tvNearestBranch;
    private Spinner spinnerDeviceCategory;
    private Spinner spinnerService;
    private EditText etDeviceModel;
    private EditText etProblemDescription;
    private Button btnLocation;
    private Button btnUploadImage;
    private ImageView ivDeviceImage;
    private Button btnSubmitRepair;

    // Categories
    private final List<String> categoryNames = new ArrayList<>();
    private final List<Integer> categoryIds = new ArrayList<>();

    // Services
    private List<Service> serviceList = new ArrayList<>();

    // Selected branch
    private String selectedBranchName;
    private int selectedBranchId = -1;

    // Customer
    private int customerId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_repair);

        databaseHelper = new DatabaseHelper(this);

        databaseHelper.insertDefaultBranches();
        databaseHelper.insertDefaultCategories();

        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        SharedPreferences preferences =
                getSharedPreferences("TechFixSession", MODE_PRIVATE);

        customerId = preferences.getInt("customerId", -1);

        initializeViews();
        setupDeviceCategories();
        setupServices();
        setupLocation();
        setupImagePickers();
        setupButtons();
        selectServiceFromIntent();
    }

    private void initializeViews() {
        spinnerDeviceCategory = findViewById(R.id.spinnerDeviceCategory);
        spinnerService = findViewById(R.id.spinnerService);
        etDeviceModel = findViewById(R.id.etDeviceModel);
        etProblemDescription = findViewById(R.id.etProblemDescription);
        btnLocation = findViewById(R.id.btnLocation);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnSubmitRepair = findViewById(R.id.btnSubmitRepair);
        tvNearestBranch = findViewById(R.id.tvNearestBranch);
        ivDeviceImage = findViewById(R.id.ivDeviceImage);
    }

    private void setupLocation() {
        locationPermissionLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.RequestMultiplePermissions(),
                        result -> {

                            Boolean fineLocation =
                                    result.get(
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                    );

                            Boolean coarseLocation =
                                    result.get(
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                    );

                            if (Boolean.TRUE.equals(fineLocation)
                                    || Boolean.TRUE.equals(coarseLocation)) {

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
    }

    private void setupImagePickers() {
        imagePickerLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.GetContent(),
                        uri -> {

                            if (uri == null) {
                                return;
                            }

                            String permanentUri =
                                    copyImageToInternalStorage(uri);

                            if (permanentUri != null) {

                                selectedImageUri = permanentUri;

                                ivDeviceImage.setImageURI(
                                        Uri.parse(permanentUri)
                                );

                                ivDeviceImage.setVisibility(View.VISIBLE);

                                Toast.makeText(
                                        this,
                                        "Device image selected!",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Unable to save the selected image.",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );

        cameraPermissionLauncher =
                registerForActivityResult(
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

        cameraLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.TakePicturePreview(),
                        bitmap -> {

                            if (bitmap == null) {
                                return;
                            }

                            String permanentUri =
                                    saveCameraImage(bitmap);

                            if (permanentUri != null) {

                                selectedImageUri = permanentUri;

                                ivDeviceImage.setImageURI(
                                        Uri.parse(permanentUri)
                                );

                                ivDeviceImage.setVisibility(View.VISIBLE);

                                Toast.makeText(
                                        this,
                                        "Photo captured!",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Unable to save camera photo.",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
    }

    private void setupButtons() {
        btnSubmitRepair.setOnClickListener(
                v -> submitRepairRequest()
        );

        btnLocation.setOnClickListener(
                v -> getCurrentLocation()
        );

        btnUploadImage.setOnClickListener(
                v -> showImageOptions()
        );
    }

    private void showImageOptions() {
        String[] options = {
                "Take Photo",
                "Choose from Gallery"
        };

        new AlertDialog.Builder(this)
                .setTitle("Upload Device Image")
                .setItems(
                        options,
                        (dialog, which) -> {

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
                        }
                )
                .show();
    }

    // Service can be pre-selected when opened from Service Details.
    private void selectServiceFromIntent() {
        int selectedServiceId =
                getIntent().getIntExtra("serviceId", -1);

        if (selectedServiceId == -1) {
            return;
        }

        for (int i = 0; i < serviceList.size(); i++) {

            if (serviceList.get(i).getId() == selectedServiceId) {

                spinnerService.setSelection(i + 1);
                break;
            }
        }
    }

    // Save gallery image to app-private storage.
    private String copyImageToInternalStorage(Uri sourceUri) {
        try (
                InputStream inputStream =
                        getContentResolver().openInputStream(sourceUri)
        ) {

            if (inputStream == null) {
                return null;
            }

            String fileName =
                    "repair_image_"
                            + System.currentTimeMillis()
                            + ".jpg";

            File imageFile =
                    new File(getFilesDir(), fileName);

            try (FileOutputStream outputStream =
                         new FileOutputStream(imageFile)) {

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
            }

            return Uri.fromFile(imageFile).toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    // Save camera image to app-private storage.
    private String saveCameraImage(Bitmap bitmap) {
        String fileName =
                "repair_camera_"
                        + System.currentTimeMillis()
                        + ".jpg";

        File imageFile =
                new File(getFilesDir(), fileName);

        try (FileOutputStream outputStream =
                     new FileOutputStream(imageFile)) {

            boolean success =
                    bitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            90,
                            outputStream
                    );

            outputStream.flush();

            if (!success) {
                return null;
            }

            return Uri.fromFile(imageFile).toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    private void setupDeviceCategories() {
        categoryNames.clear();
        categoryIds.clear();

        categoryNames.add("Select Device Category");
        categoryIds.add(-1);

        Cursor cursor = databaseHelper.getAllCategories();

        if (cursor.moveToFirst()) {

            do {

                int categoryId =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")
                        );

                String categoryName =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("name")
                        );

                categoryIds.add(categoryId);
                categoryNames.add(categoryName);

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

        spinnerDeviceCategory.setAdapter(adapter);

        if (categoryNames.size() == 1) {

            Toast.makeText(
                    this,
                    "No device categories found in database.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void setupServices() {
        serviceList = databaseHelper.getAllServices();

        List<String> serviceNames = new ArrayList<>();

        serviceNames.add("Select Repair Service");

        for (Service service : serviceList) {
            serviceNames.add(service.getName());
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
                etDeviceModel.getText().toString().trim();

        String problemDescription =
                etProblemDescription.getText().toString().trim();

        if (customerId == -1) {

            Toast.makeText(
                    this,
                    "Customer information not found. Please log in again.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        int categoryPosition =
                spinnerDeviceCategory.getSelectedItemPosition();

        if (categoryPosition <= 0
                || categoryPosition >= categoryIds.size()) {

            Toast.makeText(
                    this,
                    "Please select a device category.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int selectedCategoryId =
                categoryIds.get(categoryPosition);

        if (selectedCategoryId == -1) {

            Toast.makeText(
                    this,
                    "Invalid device category selected.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int selectedPosition =
                spinnerService.getSelectedItemPosition();

        if (selectedPosition <= 0
                || selectedPosition > serviceList.size()) {

            Toast.makeText(
                    this,
                    "Please select a repair service.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (deviceModel.isEmpty()) {

            etDeviceModel.setError(
                    "Please enter your device model."
            );

            etDeviceModel.requestFocus();
            return;
        }

        if (problemDescription.isEmpty()) {

            etProblemDescription.setError(
                    "Please describe the problem."
            );

            etProblemDescription.requestFocus();
            return;
        }

        if (selectedBranchId == -1) {

            Toast.makeText(
                    this,
                    "Please find the nearest branch first.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Service selectedService =
                serviceList.get(selectedPosition - 1);

        int selectedServiceId =
                selectedService.getId();

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
                        String.valueOf(System.currentTimeMillis())
                );

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

    // Find the nearest TechFix branch using the customer's location.
    private void getCurrentLocation() {
        boolean fineGranted =
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;

        boolean coarseGranted =
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;

        if (!fineGranted && !coarseGranted) {

            locationPermissionLauncher.launch(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }
            );

            return;
        }

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

                            double customerLatitude =
                                    location.getLatitude();

                            double customerLongitude =
                                    location.getLongitude();

                            double colomboLatitude = 6.9271;
                            double colomboLongitude = 79.8612;

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

                            double distanceInKm =
                                    nearestDistance / 1000.0;

                            selectedBranchId =
                                    databaseHelper.getBranchIdByName(
                                            nearestBranch
                                    );

                            selectedBranchName = nearestBranch;

                            if (selectedBranchId != -1) {

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

                            } else {

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

                            tvNearestBranch.setVisibility(
                                    View.VISIBLE
                            );
                        }
                );
    }
}