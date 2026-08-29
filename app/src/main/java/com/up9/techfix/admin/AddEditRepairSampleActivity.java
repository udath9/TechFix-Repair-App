package com.up9.techfix.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class AddEditRepairSampleActivity extends AppCompatActivity {

    private TextView txtFormTitle;

    private ImageView imgRepairSample;

    private Button btnChooseImage;
    private Button btnSaveSample;
    private Button btnCancelSample;

    private EditText edtDeviceName;
    private EditText edtDescription;

    private Spinner spinnerCategory;
    private Spinner spinnerService;

    private TechFixDatabaseHelper databaseHelper;

    private boolean isEditMode = false;

    private int sampleId = -1;

    private String selectedImageUri = "";

    private final List<DeviceCategory> categoryList =
            new ArrayList<>();

    private final List<RepairService> serviceList =
            new ArrayList<>();

    /*
     * Image picker
     *
     * OpenDocument is used instead of GetContent so that
     * we can request persistent read permission for the
     * selected image.
     */
    private final ActivityResultLauncher<String[]> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.OpenDocument(),
                    uri -> {

                        if (uri != null) {

                            selectedImageUri =
                                    uri.toString();

                            imgRepairSample.setImageURI(uri);

                            try {

                                getContentResolver()
                                        .takePersistableUriPermission(
                                                uri,
                                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        );

                            } catch (SecurityException ignored) {

                                // Some image providers do not
                                // support persistent permission.
                            }
                        }
                    }
            );


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_add_edit_repair_sample
        );

        databaseHelper =
                new TechFixDatabaseHelper(this);

        initializeViews();

        loadCategories();

        loadServices();

        checkEditMode();


        btnChooseImage.setOnClickListener(
                v -> openImagePicker()
        );


        btnSaveSample.setOnClickListener(
                v -> saveRepairSample()
        );


        btnCancelSample.setOnClickListener(
                v -> finish()
        );
    }


    private void initializeViews() {

        txtFormTitle =
                findViewById(
                        R.id.txtRepairSampleFormTitle
                );


        imgRepairSample =
                findViewById(
                        R.id.imgRepairSample
                );


        btnChooseImage =
                findViewById(
                        R.id.btnChooseImage
                );


        btnSaveSample =
                findViewById(
                        R.id.btnSaveRepairSample
                );


        btnCancelSample =
                findViewById(
                        R.id.btnCancelRepairSample
                );


        edtDeviceName =
                findViewById(
                        R.id.edtRepairDeviceName
                );


        edtDescription =
                findViewById(
                        R.id.edtRepairDescription
                );


        spinnerCategory =
                findViewById(
                        R.id.spinnerRepairCategory
                );


        spinnerService =
                findViewById(
                        R.id.spinnerRepairService
                );
    }


    private void openImagePicker() {

        imagePicker.launch(
                new String[]{"image/*"}
        );
    }


    /*
     * Load device categories from SQLite
     */
    private void loadCategories() {

        categoryList.clear();

        categoryList.addAll(
                databaseHelper.getAllCategories()
        );


        List<String> categoryNames =
                new ArrayList<>();


        for (DeviceCategory category :
                categoryList) {

            categoryNames.add(
                    category.getName()
            );
        }


        if (categoryNames.isEmpty()) {

            categoryNames.add(
                    "No categories available"
            );
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


        spinnerCategory.setAdapter(adapter);
    }


    /*
     * Load repair services from SQLite
     */
    private void loadServices() {

        serviceList.clear();

        serviceList.addAll(
                databaseHelper.getAllServices()
        );


        List<String> serviceNames =
                new ArrayList<>();


        for (RepairService service :
                serviceList) {

            serviceNames.add(
                    service.getName()
            );
        }


        if (serviceNames.isEmpty()) {

            serviceNames.add(
                    "No services available"
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
    }


    /*
     * Check whether this screen is being used
     * for adding or editing a repair sample.
     */
    private void checkEditMode() {

        Intent intent =
                getIntent();


        isEditMode =
                intent.getBooleanExtra(
                        "editMode",
                        false
                );


        if (!isEditMode) {

            txtFormTitle.setText(
                    "Add Repair Sample"
            );

            btnSaveSample.setText(
                    "Save Repair Sample"
            );

            return;
        }


        sampleId =
                intent.getIntExtra(
                        "sampleId",
                        -1
                );


        txtFormTitle.setText(
                "Edit Repair Sample"
        );


        btnSaveSample.setText(
                "Update Repair Sample"
        );


        String deviceName =
                intent.getStringExtra(
                        "deviceName"
                );


        if (deviceName != null) {

            edtDeviceName.setText(
                    deviceName
            );
        }


        String description =
                intent.getStringExtra(
                        "description"
                );


        if (description != null) {

            edtDescription.setText(
                    description
            );
        }


        selectedImageUri =
                intent.getStringExtra(
                        "imageUri"
                );


        if (selectedImageUri != null
                && !selectedImageUri.isEmpty()) {

            try {

                Uri imageUri =
                        Uri.parse(
                                selectedImageUri
                        );

                imgRepairSample.setImageURI(
                        imageUri
                );

            } catch (Exception e) {

                imgRepairSample.setImageResource(
                        android.R.drawable.ic_menu_gallery
                );
            }
        }


        String selectedCategory =
                intent.getStringExtra(
                        "category"
                );


        String selectedService =
                intent.getStringExtra(
                        "service"
                );


        setSpinnerSelection(
                spinnerCategory,
                selectedCategory
        );


        setSpinnerSelection(
                spinnerService,
                selectedService
        );
    }


    /*
     * Select an existing value in a Spinner.
     */
    private void setSpinnerSelection(
            Spinner spinner,
            String value
    ) {

        if (value == null) {
            return;
        }


        ArrayAdapter<?> adapter =
                (ArrayAdapter<?>) spinner.getAdapter();


        if (adapter == null) {
            return;
        }


        for (int i = 0;
             i < adapter.getCount();
             i++) {

            Object item =
                    adapter.getItem(i);


            if (item != null
                    && item.toString()
                    .equals(value)) {

                spinner.setSelection(i);

                break;
            }
        }
    }


    /*
     * Save or update repair sample.
     */
    private void saveRepairSample() {

        String deviceName =
                edtDeviceName
                        .getText()
                        .toString()
                        .trim();


        String description =
                edtDescription
                        .getText()
                        .toString()
                        .trim();


        /*
         * Validate device name
         */
        if (deviceName.isEmpty()) {

            edtDeviceName.setError(
                    "Enter device name"
            );

            edtDeviceName.requestFocus();

            return;
        }


        /*
         * Validate categories
         */
        if (categoryList.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please create a device category first",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        /*
         * Validate services
         */
        if (serviceList.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please create a repair service first",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        /*
         * Validate image
         */
        if (selectedImageUri == null
                || selectedImageUri.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please select a repair sample image",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        /*
         * Get selected category
         */
        Object selectedCategoryItem =
                spinnerCategory.getSelectedItem();


        if (selectedCategoryItem == null) {

            Toast.makeText(
                    this,
                    "Please select a device category",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        String category =
                selectedCategoryItem
                        .toString();


        /*
         * Get selected service
         */
        Object selectedServiceItem =
                spinnerService.getSelectedItem();


        if (selectedServiceItem == null) {

            Toast.makeText(
                    this,
                    "Please select a repair service",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        String service =
                selectedServiceItem
                        .toString();


        /*
         * UPDATE
         */
        if (isEditMode) {

            if (sampleId == -1) {

                Toast.makeText(
                        this,
                        "Invalid repair sample ID",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }


            int result =
                    databaseHelper.updateRepairSample(
                            sampleId,
                            deviceName,
                            category,
                            service,
                            description,
                            selectedImageUri
                    );


            if (result > 0) {

                Toast.makeText(
                        this,
                        "Repair sample updated",
                        Toast.LENGTH_SHORT
                ).show();


                setResult(
                        RESULT_OK
                );


                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to update repair sample",
                        Toast.LENGTH_SHORT
                ).show();
            }


            return;
        }


        /*
         * INSERT
         */
        long result =
                databaseHelper.insertRepairSample(
                        deviceName,
                        category,
                        service,
                        description,
                        selectedImageUri
                );


        if (result != -1) {

            Toast.makeText(
                    this,
                    "Repair sample added",
                    Toast.LENGTH_SHORT
            ).show();


            setResult(
                    RESULT_OK
            );


            finish();

        } else {

            Toast.makeText(
                    this,
                    "Failed to add repair sample",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}