package com.up9.techfix.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class ServiceFormActivity extends AppCompatActivity {

    private TextView txtServiceFormTitle;

    private ImageView imgServicePreview;

    private Button btnTakePhoto;
    private Button btnUploadImage;
    private Button btnSaveService;
    private Button btnCancelService;

    private EditText edtServiceName;
    private EditText edtServiceDescription;
    private EditText edtServicePrice;
    private EditText edtServiceEstimatedDays;

    private TechFixDatabaseHelper databaseHelper;

    private boolean isEditMode = false;

    private int serviceId = -1;

    private String selectedImageUri = "";

    // ============================================================
    // IMAGE PICKER
    // ============================================================

    private final ActivityResultLauncher<String> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {

                        if (uri != null) {

                            selectedImageUri =
                                    uri.toString();

                            imgServicePreview.setImageURI(uri);
                        }
                    }
            );

    // ============================================================
    // CAMERA
    // ============================================================

    private final ActivityResultLauncher<Void> cameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.TakePicturePreview(),
                    bitmap -> {

                        if (bitmap != null) {

                            String savedUri =
                                    saveBitmapToInternalStorage(bitmap);

                            if (savedUri != null) {

                                selectedImageUri =
                                        savedUri;

                                imgServicePreview.setImageBitmap(
                                        bitmap
                                );

                            } else {

                                Toast.makeText(
                                        this,
                                        "Failed to save camera image",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.service_form
        );

        databaseHelper =
                new TechFixDatabaseHelper(this);

        initializeViews();

        checkEditMode();

        btnUploadImage.setOnClickListener(
                v -> openImagePicker()
        );

        btnTakePhoto.setOnClickListener(
                v -> openCamera()
        );

        btnSaveService.setOnClickListener(
                v -> saveService()
        );

        btnCancelService.setOnClickListener(v -> {

            setResult(
                    RESULT_CANCELED
            );

            finish();
        });
    }

    // ============================================================
    // INITIALIZE VIEWS
    // ============================================================

    private void initializeViews() {

        txtServiceFormTitle =
                findViewById(
                        R.id.txtServiceFormTitle
                );

        imgServicePreview =
                findViewById(
                        R.id.imgServicePreview
                );

        btnTakePhoto =
                findViewById(
                        R.id.btnTakePhoto
                );

        btnUploadImage =
                findViewById(
                        R.id.btnUploadImage
                );

        btnSaveService =
                findViewById(
                        R.id.btnSaveService
                );

        btnCancelService =
                findViewById(
                        R.id.btnCancelService
                );

        edtServiceName =
                findViewById(
                        R.id.edtServiceName
                );

        edtServiceDescription =
                findViewById(
                        R.id.edtServiceDescription
                );

        edtServicePrice =
                findViewById(
                        R.id.edtServicePrice
                );

        edtServiceEstimatedDays =
                findViewById(
                        R.id.edtServiceEstimatedDays
                );
    }

    // ============================================================
    // IMAGE PICKER
    // ============================================================

    private void openImagePicker() {

        imagePicker.launch("image/*");
    }

    // ============================================================
    // CAMERA
    // ============================================================

    private void openCamera() {

        cameraLauncher.launch(null);
    }

    // ============================================================
    // SAVE CAMERA IMAGE
    // ============================================================

    private String saveBitmapToInternalStorage(Bitmap bitmap) {

        File directory =
                new File(
                        getFilesDir(),
                        "service_images"
                );

        if (!directory.exists()) {

            if (!directory.mkdirs()) {
                return null;
            }
        }

        String fileName =
                "service_"
                        + System.currentTimeMillis()
                        + ".jpg";

        File imageFile =
                new File(
                        directory,
                        fileName
                );

        try {

            FileOutputStream outputStream =
                    new FileOutputStream(imageFile);

            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    outputStream
            );

            outputStream.flush();
            outputStream.close();

            return Uri.fromFile(imageFile).toString();

        } catch (IOException e) {

            e.printStackTrace();

            return null;
        }
    }

    // ============================================================
    // EDIT MODE
    // ============================================================

    private void checkEditMode() {

        Intent intent =
                getIntent();

        isEditMode =
                intent.getBooleanExtra(
                        "editMode",
                        false
                );

        if (!isEditMode) {

            txtServiceFormTitle.setText(
                    "Add Repair Service"
            );

            btnSaveService.setText(
                    "Save Service"
            );

            return;
        }

        serviceId =
                intent.getIntExtra(
                        "serviceId",
                        -1
                );

        txtServiceFormTitle.setText(
                "Edit Repair Service"
        );

        btnSaveService.setText(
                "Update Service"
        );

        edtServiceName.setText(
                intent.getStringExtra(
                        "name"
                )
        );

        edtServiceDescription.setText(
                intent.getStringExtra(
                        "description"
                )
        );

        double price =
                intent.getDoubleExtra(
                        "price",
                        0.0
                );

        edtServicePrice.setText(
                String.format(
                        Locale.getDefault(),
                        "%.2f",
                        price
                )
        );

        int estimatedDays =
                intent.getIntExtra(
                        "estimatedDays",
                        0
                );

        edtServiceEstimatedDays.setText(
                String.valueOf(
                        estimatedDays
                )
        );

        selectedImageUri =
                intent.getStringExtra(
                        "imageUri"
                );

        if (selectedImageUri != null
                && !selectedImageUri.isEmpty()) {

            try {

                imgServicePreview.setImageURI(
                        Uri.parse(
                                selectedImageUri
                        )
                );

            } catch (Exception e) {

                imgServicePreview.setImageResource(
                        android.R.drawable.ic_menu_gallery
                );
            }
        }
    }

    // ============================================================
    // SAVE SERVICE
    // ============================================================

    private void saveService() {

        String name =
                edtServiceName
                        .getText()
                        .toString()
                        .trim();

        String description =
                edtServiceDescription
                        .getText()
                        .toString()
                        .trim();

        String priceText =
                edtServicePrice
                        .getText()
                        .toString()
                        .trim();

        String daysText =
                edtServiceEstimatedDays
                        .getText()
                        .toString()
                        .trim();

        // Validate name

        if (name.isEmpty()) {

            edtServiceName.setError(
                    "Enter service name"
            );

            edtServiceName.requestFocus();

            return;
        }

        // Validate price

        if (priceText.isEmpty()) {

            edtServicePrice.setError(
                    "Enter service price"
            );

            edtServicePrice.requestFocus();

            return;
        }

        // Validate estimated days

        if (daysText.isEmpty()) {

            edtServiceEstimatedDays.setError(
                    "Enter estimated days"
            );

            edtServiceEstimatedDays.requestFocus();

            return;
        }

        double price;

        int estimatedDays;

        try {

            price =
                    Double.parseDouble(
                            priceText
                    );

        } catch (NumberFormatException e) {

            edtServicePrice.setError(
                    "Enter a valid price"
            );

            edtServicePrice.requestFocus();

            return;
        }

        try {

            estimatedDays =
                    Integer.parseInt(
                            daysText
                    );

        } catch (NumberFormatException e) {

            edtServiceEstimatedDays.setError(
                    "Enter a valid number"
            );

            edtServiceEstimatedDays.requestFocus();

            return;
        }

        if (price < 0) {

            edtServicePrice.setError(
                    "Price cannot be negative"
            );

            return;
        }

        if (estimatedDays <= 0) {

            edtServiceEstimatedDays.setError(
                    "Estimated days must be greater than 0"
            );

            return;
        }

        // ========================================================
        // UPDATE
        // ========================================================

        if (isEditMode) {

            if (serviceId == -1) {

                Toast.makeText(
                        this,
                        "Invalid service ID",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            int result =
                    databaseHelper.updateService(
                            serviceId,
                            name,
                            selectedImageUri,
                            description,
                            price,
                            estimatedDays
                    );

            if (result > 0) {

                Toast.makeText(
                        this,
                        "Service updated successfully",
                        Toast.LENGTH_SHORT
                ).show();

                setResult(
                        RESULT_OK
                );

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to update service",
                        Toast.LENGTH_SHORT
                ).show();
            }

            return;
        }

        // ========================================================
        // INSERT
        // ========================================================

        long result =
                databaseHelper.insertService(
                        name,
                        selectedImageUri,
                        description,
                        price,
                        estimatedDays
                );

        if (result != -1) {

            Toast.makeText(
                    this,
                    "Service added successfully",
                    Toast.LENGTH_SHORT
            ).show();

            setResult(
                    RESULT_OK
            );

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Failed to add service",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}