package com.up9.techfix.admin.services;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ServiceFormActivity extends AppCompatActivity {

    private static final String TAG = "ServiceFormActivity";

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

    private DatabaseHelper databaseHelper;

    private boolean isEditMode = false;
    private int serviceId = -1;

    private String selectedImageUri = "";

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {

                        if (uri == null) {
                            return;
                        }

                        String savedUri =
                                copyImageToInternalStorage(uri);

                        if (savedUri != null) {

                            selectedImageUri = savedUri;

                            loadImage(selectedImageUri);

                            Toast.makeText(
                                    this,
                                    "Image selected successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                        } else {

                            Toast.makeText(
                                    this,
                                    "Unable to save image",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );


    private final ActivityResultLauncher<Void> cameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.TakePicturePreview(),
                    bitmap -> {

                        if (bitmap == null) {

                            Toast.makeText(
                                    this,
                                    "Camera cancelled",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        String savedUri =
                                saveCameraImage(bitmap);

                        if (savedUri != null) {

                            selectedImageUri = savedUri;

                            loadImage(selectedImageUri);

                            Toast.makeText(
                                    this,
                                    "Camera image saved successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                        } else {

                            Toast.makeText(
                                    this,
                                    "Failed to save camera image",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );


    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {

                        if (isGranted) {

                            cameraLauncher.launch(null);

                        } else {

                            Toast.makeText(
                                    this,
                                    "Camera permission is required",
                                    Toast.LENGTH_LONG
                            ).show();
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
                new DatabaseHelper(this);

        initializeViews();

        checkEditMode();

        btnUploadImage.setOnClickListener(
                v -> openGallery()
        );

        // Camera
        btnTakePhoto.setOnClickListener(
                v -> openCamera()
        );

        // Save
        btnSaveService.setOnClickListener(
                v -> saveService()
        );

        // Cancel
        btnCancelService.setOnClickListener(
                v -> {

                    setResult(
                            RESULT_CANCELED
                    );

                    finish();
                }
        );
    }

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

    private void checkEditMode() {

        isEditMode =
                getIntent().getBooleanExtra(
                        "editMode",
                        false
                );

        if (!isEditMode) {

            txtServiceFormTitle.setText(
                    "Add Service"
            );

            imgServicePreview.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );

            return;
        }

        txtServiceFormTitle.setText(
                "Edit Service"
        );

        serviceId =
                getIntent().getIntExtra(
                        "serviceId",
                        -1
                );

        String name =
                getIntent().getStringExtra(
                        "name"
                );

        String imageUri =
                getIntent().getStringExtra(
                        "imageUri"
                );

        String description =
                getIntent().getStringExtra(
                        "description"
                );

        double price =
                getIntent().getDoubleExtra(
                        "price",
                        0.0
                );

        int estimatedDays =
                getIntent().getIntExtra(
                        "estimatedDays",
                        1
                );

        if (name != null) {

            edtServiceName.setText(name);
        }

        if (description != null) {

            edtServiceDescription.setText(
                    description
            );
        }

        edtServicePrice.setText(
                String.valueOf(price)
        );

        edtServiceEstimatedDays.setText(
                String.valueOf(
                        estimatedDays
                )
        );

        if (
                imageUri != null
                        &&
                        !imageUri.trim().isEmpty()
        ) {

            selectedImageUri =
                    imageUri.trim();

            loadImage(
                    selectedImageUri
            );
        }
    }


    private void openGallery() {

        imagePickerLauncher.launch(
                "image/*"
        );
    }


    private void openCamera() {

        if (
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                )
                        ==
                        PackageManager.PERMISSION_GRANTED
        ) {

            cameraLauncher.launch(null);

        } else {

            cameraPermissionLauncher.launch(
                    Manifest.permission.CAMERA
            );
        }
    }


    private String saveCameraImage(
            Bitmap bitmap
    ) {

        FileOutputStream outputStream = null;

        try {

            String fileName =
                    "service_camera_"
                            +
                            System.currentTimeMillis()
                            +
                            ".jpg";

            File imageFile =
                    new File(
                            getFilesDir(),
                            fileName
                    );

            outputStream =
                    new FileOutputStream(
                            imageFile
                    );

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

            return Uri.fromFile(
                    imageFile
            ).toString();

        } catch (Exception e) {

            Log.e(
                    TAG,
                    "Error saving camera image",
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


    private String copyImageToInternalStorage(
            Uri sourceUri
    ) {

        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {

            ContentResolver resolver =
                    getContentResolver();

            inputStream =
                    resolver.openInputStream(
                            sourceUri
                    );

            if (inputStream == null) {

                return null;
            }

            String fileName =
                    "service_gallery_"
                            +
                            System.currentTimeMillis()
                            +
                            ".jpg";

            File imageFile =
                    new File(
                            getFilesDir(),
                            fileName
                    );

            outputStream =
                    new FileOutputStream(
                            imageFile
                    );

            byte[] buffer =
                    new byte[8192];

            int bytesRead;

            while (
                    (bytesRead =
                            inputStream.read(buffer))
                            != -1
            ) {

                outputStream.write(
                        buffer,
                        0,
                        bytesRead
                );
            }

            outputStream.flush();

            return Uri.fromFile(
                    imageFile
            ).toString();

        } catch (Exception e) {

            Log.e(
                    TAG,
                    "Error copying gallery image",
                    e
            );

            return null;

        } finally {

            if (inputStream != null) {

                try {

                    inputStream.close();

                } catch (Exception ignored) {
                }
            }

            if (outputStream != null) {

                try {

                    outputStream.close();

                } catch (Exception ignored) {
                }
            }
        }
    }


    private void loadImage(
            String imageValue
    ) {

        if (
                imageValue == null
                        ||
                        imageValue.trim().isEmpty()
        ) {

            imgServicePreview.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );

            return;
        }

        imageValue =
                imageValue.trim();

        try {


            if (
                    imageValue.startsWith(
                            "file://"
                    )
            ) {

                Uri uri =
                        Uri.parse(
                                imageValue
                        );

                imgServicePreview.setImageURI(
                        uri
                );

                imgServicePreview.setVisibility(
                        ImageView.VISIBLE
                );

                return;
            }

            if (
                    imageValue.startsWith(
                            "content://"
                    )
            ) {

                Uri uri =
                        Uri.parse(
                                imageValue
                        );

                imgServicePreview.setImageURI(
                        uri
                );

                imgServicePreview.setVisibility(
                        ImageView.VISIBLE
                );

                return;
            }


            int resourceId =
                    getResources().getIdentifier(
                            imageValue,
                            "drawable",
                            getPackageName()
                    );

            if (resourceId != 0) {

                imgServicePreview.setImageResource(
                        resourceId
                );

                imgServicePreview.setVisibility(
                        ImageView.VISIBLE
                );

            } else {

                imgServicePreview.setImageResource(
                        android.R.drawable.ic_menu_gallery
                );
            }

        } catch (Exception e) {

            Log.e(
                    TAG,
                    "Unable to load service image",
                    e
            );

            imgServicePreview.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );
        }
    }

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

        String estimatedDaysText =
                edtServiceEstimatedDays
                        .getText()
                        .toString()
                        .trim();


        if (name.isEmpty()) {

            edtServiceName.setError(
                    "Enter service name"
            );

            edtServiceName.requestFocus();

            return;
        }

        if (description.isEmpty()) {

            edtServiceDescription.setError(
                    "Enter service description"
            );

            edtServiceDescription.requestFocus();

            return;
        }

        if (priceText.isEmpty()) {

            edtServicePrice.setError(
                    "Enter service price"
            );

            edtServicePrice.requestFocus();

            return;
        }

        if (estimatedDaysText.isEmpty()) {

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
                    "Invalid price"
            );

            edtServicePrice.requestFocus();

            return;
        }

        try {

            estimatedDays =
                    Integer.parseInt(
                            estimatedDaysText
                    );

        } catch (NumberFormatException e) {

            edtServiceEstimatedDays.setError(
                    "Invalid number of days"
            );

            edtServiceEstimatedDays.requestFocus();

            return;
        }

        if (price < 0) {

            edtServicePrice.setError(
                    "Price cannot be negative"
            );

            edtServicePrice.requestFocus();

            return;
        }

        if (estimatedDays <= 0) {

            edtServiceEstimatedDays.setError(
                    "Days must be greater than 0"
            );

            edtServiceEstimatedDays.requestFocus();

            return;
        }


        try {


            if (!isEditMode) {

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
                            Toast.LENGTH_LONG
                    ).show();
                }

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
                        Toast.LENGTH_LONG
                ).show();
            }

        } catch (Exception e) {

            Log.e(
                    TAG,
                    "Database error while saving service",
                    e
            );

            Toast.makeText(
                    this,
                    "Database error: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}