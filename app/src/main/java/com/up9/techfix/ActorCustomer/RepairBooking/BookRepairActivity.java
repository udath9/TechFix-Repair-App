
        package com.up9.techfix.ActorCustomer.RepairBooking;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.up9.techfix.R;
import com.up9.techfix.data.Branch;
import com.up9.techfix.data.Category;
import com.up9.techfix.data.DatabaseHelper;
import com.up9.techfix.data.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * BookRepairActivity
 *
 * Customer repair booking screen.
 *
 * Uses DatabaseHelper lists rather than Cursor for:
 * - Categories
 * - Services
 * - Branches
 *
 * Creates repairs using DatabaseHelper.createRepair().
 */
public class BookRepairActivity extends AppCompatActivity {

    // ============================================================
    // CONSTANTS
    // ============================================================

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // ============================================================
    // DATABASE
    // ============================================================

    private DatabaseHelper databaseHelper;

    // ============================================================
    // UI
    // ============================================================

    private Spinner spinnerDeviceCategory;
    private Spinner spinnerService;

    private EditText etDeviceModel;
    private EditText etProblemDescription;

    private Button btnLocation;
    private Button btnUploadImage;
    private Button btnSubmitRepair;

    private TextView tvNearestBranch;

    private ImageView ivDeviceImage;

    // ============================================================
    // DATA
    // ============================================================

    private final List<Category> categoryList =
            new ArrayList<>();

    private final List<Service> serviceList =
            new ArrayList<>();

    private final List<Branch> branchList =
            new ArrayList<>();

    private int selectedCategoryId = -1;
    private int selectedServiceId = -1;
    private int selectedBranchId = -1;

    private String selectedImageUri = null;

    /**
     * IMPORTANT:
     *
     * This is customers.id.
     *
     * It is NOT necessarily users.id.
     */
    private int customerId = -1;

    // ============================================================
    // IMAGE PICKER
    // ============================================================

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {

                        if (uri == null) {
                            return;
                        }

                        selectedImageUri =
                                uri.toString();

                        if (ivDeviceImage != null) {

                            ivDeviceImage.setImageURI(uri);

                            ivDeviceImage.setVisibility(
                                    ImageView.VISIBLE
                            );
                        }

                        Toast.makeText(
                                this,
                                "Device image selected.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
            );

    // ============================================================
    // ACTIVITY CREATE
    // ============================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_book_repair
        );

        // --------------------------------------------------------
        // DATABASE
        // --------------------------------------------------------

        databaseHelper =
                new DatabaseHelper(this);

        // --------------------------------------------------------
        // UI
        // --------------------------------------------------------

        initializeViews();

        // --------------------------------------------------------
        // CUSTOMER
        // --------------------------------------------------------

        customerId =
                getCustomerId();

        /*
         * Do NOT immediately assume that a missing SharedPreferences
         * value means the customer does not exist.
         *
         * getCustomerId() also checks the logged-in email against
         * the customers table.
         */

        if (customerId <= 0) {

            Toast.makeText(
                    this,
                    "Customer account not found. Please login again.",
                    Toast.LENGTH_LONG
            ).show();

            finish();

            return;
        }

        // --------------------------------------------------------
        // LOAD DATABASE DATA
        // --------------------------------------------------------

        loadCategories();

        loadServices();

        loadBranches();

        // --------------------------------------------------------
        // SPINNERS
        // --------------------------------------------------------

        setupCategorySpinner();

        setupServiceSpinner();

        // --------------------------------------------------------
        // BUTTONS
        // --------------------------------------------------------

        btnLocation.setOnClickListener(
                v -> findNearestBranch()
        );

        btnUploadImage.setOnClickListener(
                v -> openImagePicker()
        );

        btnSubmitRepair.setOnClickListener(
                v -> submitRepair()
        );
    }

    // ============================================================
    // INITIALIZE VIEWS
    // ============================================================

    private void initializeViews() {

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
    }

    // ============================================================
    // GET CUSTOMER ID
    // ============================================================

    private int getCustomerId() {

        // --------------------------------------------------------
        // 1. INTENT
        // --------------------------------------------------------

        if (getIntent() != null &&
                getIntent().hasExtra("customer_id")) {

            int intentCustomerId =
                    getIntent().getIntExtra(
                            "customer_id",
                            -1
                    );

            if (intentCustomerId > 0) {

                return intentCustomerId;
            }
        }

        // --------------------------------------------------------
        // 2. TechFixSession
        // --------------------------------------------------------

        SharedPreferences sessionPreferences =
                getSharedPreferences(
                        "TechFixSession",
                        MODE_PRIVATE
                );

        int savedCustomerId =
                sessionPreferences.getInt(
                        "customerId",
                        -1
                );

        if (savedCustomerId > 0) {

            return savedCustomerId;
        }

        // --------------------------------------------------------
        // 3. Alternative customerId key
        // --------------------------------------------------------

        savedCustomerId =
                sessionPreferences.getInt(
                        "customer_id",
                        -1
                );

        if (savedCustomerId > 0) {

            return savedCustomerId;
        }

        // --------------------------------------------------------
        // 4. GET CUSTOMER USING LOGGED-IN EMAIL
        // --------------------------------------------------------

        String email =
                sessionPreferences.getString(
                        "userEmail",
                        null
                );

        if (email != null &&
                !email.trim().isEmpty()) {

            int databaseCustomerId =
                    databaseHelper.getCustomerId(
                            email.trim()
                    );

            if (databaseCustomerId > 0) {

                /*
                 * Save the correct customer ID so future screens
                 * do not have to search again.
                 */

                sessionPreferences.edit()
                        .putInt(
                                "customerId",
                                databaseCustomerId
                        )
                        .putInt(
                                "customer_id",
                                databaseCustomerId
                        )
                        .apply();

                return databaseCustomerId;
            }
        }

        // --------------------------------------------------------
        // 5. TechFixPrefs FALLBACK
        // --------------------------------------------------------

        SharedPreferences oldPreferences =
                getSharedPreferences(
                        "TechFixPrefs",
                        MODE_PRIVATE
                );

        savedCustomerId =
                oldPreferences.getInt(
                        "customerId",
                        -1
                );

        if (savedCustomerId > 0) {

            return savedCustomerId;
        }

        savedCustomerId =
                oldPreferences.getInt(
                        "customer_id",
                        -1
                );

        if (savedCustomerId > 0) {

            return savedCustomerId;
        }

        // --------------------------------------------------------
        // CUSTOMER NOT FOUND
        // --------------------------------------------------------

        return -1;
    }

    // ============================================================
    // LOAD CATEGORIES
    // ============================================================

    private void loadCategories() {

        categoryList.clear();

        List<Category> categories =
                databaseHelper.getAllCategories();

        if (categories != null) {

            categoryList.addAll(
                    categories
            );
        }

        List<String> categoryNames =
                new ArrayList<>();

        categoryNames.add(
                "Select Device Category"
        );

        for (Category category :
                categoryList) {

            if (category != null) {

                categoryNames.add(
                        safeText(
                                category.getName()
                        )
                );
            }
        }

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

        if (categoryList.isEmpty()) {

            Toast.makeText(
                    this,
                    "No device categories found in database.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    // ============================================================
    // LOAD SERVICES
    // ============================================================

    private void loadServices() {

        serviceList.clear();

        List<Service> services =
                databaseHelper.getAllServices();

        if (services != null) {

            serviceList.addAll(
                    services
            );
        }

        List<String> serviceNames =
                new ArrayList<>();

        serviceNames.add(
                "Select Repair Service"
        );

        for (Service service :
                serviceList) {

            if (service != null) {

                serviceNames.add(
                        safeText(
                                service.getName()
                        )
                );
            }
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

    // ============================================================
    // LOAD BRANCHES
    // ============================================================

    private void loadBranches() {

        branchList.clear();

        List<Branch> branches =
                databaseHelper.getAllBranches();

        if (branches != null) {

            branchList.addAll(
                    branches
            );
        }

        if (branchList.isEmpty()) {

            Toast.makeText(
                    this,
                    "No TechFix branches found in database.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    // ============================================================
    // CATEGORY SPINNER
    // ============================================================

    private void setupCategorySpinner() {

        spinnerDeviceCategory
                .setOnItemSelectedListener(
                        new android.widget.AdapterView
                                .OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(
                                    android.widget.AdapterView<?> parent,
                                    android.view.View view,
                                    int position,
                                    long id
                            ) {

                                if (position <= 0) {

                                    selectedCategoryId =
                                            -1;

                                    return;
                                }

                                int listPosition =
                                        position - 1;

                                if (listPosition >= 0 &&
                                        listPosition <
                                                categoryList.size()) {

                                    Category category =
                                            categoryList.get(
                                                    listPosition
                                            );

                                    if (category != null) {

                                        selectedCategoryId =
                                                category.getId();
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(
                                    android.widget.AdapterView<?> parent
                            ) {

                                selectedCategoryId =
                                        -1;
                            }
                        }
                );
    }

    // ============================================================
    // SERVICE SPINNER
    // ============================================================

    private void setupServiceSpinner() {

        spinnerService
                .setOnItemSelectedListener(
                        new android.widget.AdapterView
                                .OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(
                                    android.widget.AdapterView<?> parent,
                                    android.view.View view,
                                    int position,
                                    long id
                            ) {

                                if (position <= 0) {

                                    selectedServiceId =
                                            -1;

                                    return;
                                }

                                int listPosition =
                                        position - 1;

                                if (listPosition >= 0 &&
                                        listPosition <
                                                serviceList.size()) {

                                    Service service =
                                            serviceList.get(
                                                    listPosition
                                            );

                                    if (service != null) {

                                        selectedServiceId =
                                                service.getId();
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(
                                    android.widget.AdapterView<?> parent
                            ) {

                                selectedServiceId =
                                        -1;
                            }
                        }
                );
    }

    // ============================================================
    // IMAGE PICKER
    // ============================================================

    private void openImagePicker() {

        imagePickerLauncher.launch(
                "image/*"
        );
    }

    // ============================================================
    // FIND NEAREST BRANCH
    // ============================================================

    private void findNearestBranch() {

        if (branchList.isEmpty()) {

            Toast.makeText(
                    this,
                    "No TechFix branches are available.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        // --------------------------------------------------------
        // PERMISSION
        // --------------------------------------------------------

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

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );

            return;
        }

        // --------------------------------------------------------
        // LOCATION MANAGER
        // --------------------------------------------------------

        LocationManager locationManager =
                (LocationManager)
                        getSystemService(
                                LOCATION_SERVICE
                        );

        if (locationManager == null) {

            showLocationSettingsMessage();

            return;
        }

        boolean gpsEnabled = false;

        try {

            gpsEnabled =
                    locationManager.isProviderEnabled(
                            LocationManager.GPS_PROVIDER
                    );

        } catch (Exception ignored) {

            // Ignore and handle below.
        }

        if (!gpsEnabled) {

            showLocationSettingsMessage();

            return;
        }

        // --------------------------------------------------------
        // GET LAST LOCATION
        // --------------------------------------------------------

        Location bestLocation = null;

        try {

            Location gpsLocation =
                    locationManager.getLastKnownLocation(
                            LocationManager.GPS_PROVIDER
                    );

            Location networkLocation =
                    locationManager.getLastKnownLocation(
                            LocationManager.NETWORK_PROVIDER
                    );

            if (gpsLocation != null) {

                bestLocation =
                        gpsLocation;
            }

            if (networkLocation != null) {

                if (bestLocation == null ||
                        networkLocation.getAccuracy() <
                                bestLocation.getAccuracy()) {

                    bestLocation =
                            networkLocation;
                }
            }

        } catch (SecurityException e) {

            Toast.makeText(
                    this,
                    "Location permission is required.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        // --------------------------------------------------------
        // NO LOCATION
        // --------------------------------------------------------

        if (bestLocation == null) {

            Toast.makeText(
                    this,
                    "Unable to get your current location. Please turn on GPS and try again.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        // --------------------------------------------------------
        // FIND NEAREST BRANCH
        // --------------------------------------------------------

        Branch nearestBranch = null;

        float shortestDistance =
                Float.MAX_VALUE;

        float[] distance =
                new float[1];

        for (Branch branch :
                branchList) {

            if (branch == null) {
                continue;
            }

            Location.distanceBetween(
                    bestLocation.getLatitude(),
                    bestLocation.getLongitude(),
                    branch.getLatitude(),
                    branch.getLongitude(),
                    distance
            );

            if (distance[0] <
                    shortestDistance) {

                shortestDistance =
                        distance[0];

                nearestBranch =
                        branch;
            }
        }

        // --------------------------------------------------------
        // DISPLAY RESULT
        // --------------------------------------------------------

        if (nearestBranch == null) {

            Toast.makeText(
                    this,
                    "Unable to find a nearby branch.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        selectedBranchId =
                nearestBranch.getId();

        String distanceText;

        if (shortestDistance < 1000) {

            distanceText =
                    String.format(
                            Locale.getDefault(),
                            "%.0f m",
                            shortestDistance
                    );

        } else {

            distanceText =
                    String.format(
                            Locale.getDefault(),
                            "%.2f km",
                            shortestDistance / 1000.0
                    );
        }

        String branchText =
                "Nearest Branch\n\n"
                        + safeText(
                        nearestBranch.getName()
                )
                        + "\n"
                        + safeText(
                        nearestBranch.getAddress()
                )
                        + "\nPhone: "
                        + safeText(
                        nearestBranch.getPhone()
                )
                        + "\nDistance: "
                        + distanceText;

        tvNearestBranch.setText(
                branchText
        );

        tvNearestBranch.setVisibility(
                TextView.VISIBLE
        );

        Toast.makeText(
                this,
                "Nearest branch selected.",
                Toast.LENGTH_SHORT
        ).show();
    }

    // ============================================================
    // LOCATION SETTINGS
    // ============================================================

    private void showLocationSettingsMessage() {

        Toast.makeText(
                this,
                "Please turn on GPS/location services.",
                Toast.LENGTH_LONG
        ).show();

        try {

            Intent intent =
                    new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    );

            startActivity(intent);

        } catch (Exception ignored) {

            // Ignore if settings cannot be opened.
        }
    }

    // ============================================================
    // LOCATION PERMISSION RESULT
    // ============================================================

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode !=
                LOCATION_PERMISSION_REQUEST_CODE) {

            return;
        }

        boolean granted =
                false;

        for (int result :
                grantResults) {

            if (result ==
                    PackageManager.PERMISSION_GRANTED) {

                granted = true;

                break;
            }
        }

        if (granted) {

            findNearestBranch();

        } else {

            Toast.makeText(
                    this,
                    "Location permission was denied.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    // ============================================================
    // VALIDATE FORM
    // ============================================================

    private boolean validateForm() {

        // --------------------------------------------------------
        // CUSTOMER
        // --------------------------------------------------------

        if (customerId <= 0) {

            Toast.makeText(
                    this,
                    "Customer account not found. Please login again.",
                    Toast.LENGTH_LONG
            ).show();

            return false;
        }

        // --------------------------------------------------------
        // CATEGORY
        // --------------------------------------------------------

        if (selectedCategoryId <= 0) {

            Toast.makeText(
                    this,
                    "Please select a device category.",
                    Toast.LENGTH_SHORT
            ).show();

            spinnerDeviceCategory.requestFocus();

            return false;
        }

        // --------------------------------------------------------
        // SERVICE
        // --------------------------------------------------------

        if (selectedServiceId <= 0) {

            Toast.makeText(
                    this,
                    "Please select a repair service.",
                    Toast.LENGTH_SHORT
            ).show();

            spinnerService.requestFocus();

            return false;
        }

        // --------------------------------------------------------
        // DEVICE MODEL
        // --------------------------------------------------------

        String deviceModel =
                etDeviceModel
                        .getText()
                        .toString()
                        .trim();

        if (TextUtils.isEmpty(deviceModel)) {

            etDeviceModel.setError(
                    "Please enter the device model."
            );

            etDeviceModel.requestFocus();

            return false;
        }

        // --------------------------------------------------------
        // PROBLEM DESCRIPTION
        // --------------------------------------------------------

        String problemDescription =
                etProblemDescription
                        .getText()
                        .toString()
                        .trim();

        if (TextUtils.isEmpty(
                problemDescription
        )) {

            etProblemDescription.setError(
                    "Please describe the problem."
            );

            etProblemDescription.requestFocus();

            return false;
        }

        // --------------------------------------------------------
        // BRANCH
        // --------------------------------------------------------

        if (selectedBranchId <= 0) {

            Toast.makeText(
                    this,
                    "Please find and select your nearest branch.",
                    Toast.LENGTH_LONG
            ).show();

            return false;
        }

        return true;
    }

    // ============================================================
    // SUBMIT REPAIR
    // ============================================================

    private void submitRepair() {

        // --------------------------------------------------------
        // VALIDATION
        // --------------------------------------------------------

        if (!validateForm()) {

            return;
        }

        // --------------------------------------------------------
        // DISABLE BUTTON
        // --------------------------------------------------------

        btnSubmitRepair.setEnabled(
                false
        );

        btnSubmitRepair.setText(
                "Submitting..."
        );

        // --------------------------------------------------------
        // VALUES
        // --------------------------------------------------------

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

        // --------------------------------------------------------
        // CURRENT DATE / TIME
        // --------------------------------------------------------

        String repairDate =
                String.valueOf(
                        System.currentTimeMillis()
                );

        // --------------------------------------------------------
        // CREATE REPAIR
        // --------------------------------------------------------
        //
        // Current DatabaseHelper supports:
        //
        // createRepair(
        //     customerId,
        //     categoryId,
        //     deviceModel,
        //     serviceId,
        //     problemDescription,
        //     branchId,
        //     imageUri,
        //     repairDate
        // )
        //
        // The helper automatically uses "Pending".
        // --------------------------------------------------------

        long repairId;

        try {

            repairId =
                    databaseHelper.createRepair(
                            customerId,
                            selectedCategoryId,
                            deviceModel,
                            selectedServiceId,
                            problemDescription,
                            selectedBranchId,
                            selectedImageUri,
                            repairDate
                    );

        } catch (Exception e) {

            btnSubmitRepair.setEnabled(
                    true
            );

            btnSubmitRepair.setText(
                    "Submit Repair Request"
            );

            Toast.makeText(
                    this,
                    "Error creating repair: "
                            + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        // --------------------------------------------------------
        // SUCCESS
        // --------------------------------------------------------

        if (repairId > 0) {

            Toast.makeText(
                    this,
                    "Repair request submitted successfully.\nRepair ID: "
                            + repairId,
                    Toast.LENGTH_LONG
            ).show();

            Intent resultIntent =
                    new Intent();

            resultIntent.putExtra(
                    "repair_id",
                    (int) repairId
            );

            setResult(
                    Activity.RESULT_OK,
                    resultIntent
            );

            finish();

            return;
        }

        // --------------------------------------------------------
        // FAILURE
        // --------------------------------------------------------

        btnSubmitRepair.setEnabled(
                true
        );

        btnSubmitRepair.setText(
                "Submit Repair Request"
        );

        Toast.makeText(
                this,
                "Failed to submit repair request.",
                Toast.LENGTH_LONG
        ).show();
    }

    // ============================================================
    // SAFE TEXT
    // ============================================================

    private String safeText(
            String value
    ) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not available";
        }

        return value;
    }

    // ============================================================
    // DESTROY
    // ============================================================

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {

            databaseHelper.close();

            databaseHelper = null;
        }

        super.onDestroy();
    }
}