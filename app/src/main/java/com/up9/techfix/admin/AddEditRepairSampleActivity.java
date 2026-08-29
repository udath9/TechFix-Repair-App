package com.up9.techfix.admin;
import com.up9.techfix.R;
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

public class AddEditRepairSampleActivity
        extends AppCompatActivity {

    private TextView txtSampleFormTitle;

    private ImageView imgSelectedSample;

    private Button btnSelectImage;
    private Button btnSaveRepairSample;

    private EditText edtSampleDevice;
    private EditText edtSampleService;
    private EditText edtSampleDescription;

    private Spinner spinnerSampleCategory;

    private Uri selectedImageUri;

    private final String[] categories = {
            "Mobile Phone",
            "Laptop",
            "Desktop Computer",
            "Tablet",
            "Other"
    };

    private final ActivityResultLauncher<String>
            imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts
                            .GetContent(),
                    uri -> {

                        if (uri != null) {

                            selectedImageUri = uri;

                            imgSelectedSample
                                    .setImageURI(uri);
                        }
                    }
            );

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_add_edit_repair_sample
        );

        txtSampleFormTitle =
                findViewById(
                        R.id.txtSampleFormTitle
                );

        imgSelectedSample =
                findViewById(
                        R.id.imgSelectedSample
                );

        btnSelectImage =
                findViewById(
                        R.id.btnSelectImage
                );

        btnSaveRepairSample =
                findViewById(
                        R.id.btnSaveRepairSample
                );

        edtSampleDevice =
                findViewById(
                        R.id.edtSampleDevice
                );

        edtSampleService =
                findViewById(
                        R.id.edtSampleService
                );

        edtSampleDescription =
                findViewById(
                        R.id.edtSampleDescription
                );

        spinnerSampleCategory =
                findViewById(
                        R.id.spinnerSampleCategory
                );

        setupCategorySpinner();

        btnSelectImage.setOnClickListener(
                v -> selectImage()
        );

        btnSaveRepairSample.setOnClickListener(
                v -> saveSample()
        );
    }

    private void setupCategorySpinner() {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categories
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerSampleCategory.setAdapter(
                adapter
        );
    }

    private void selectImage() {

        imagePicker.launch(
                "image/*"
        );
    }

    private void saveSample() {

        String device =
                edtSampleDevice
                        .getText()
                        .toString()
                        .trim();

        String service =
                edtSampleService
                        .getText()
                        .toString()
                        .trim();

        String description =
                edtSampleDescription
                        .getText()
                        .toString()
                        .trim();

        String category =
                spinnerSampleCategory
                        .getSelectedItem()
                        .toString();

        if (device.isEmpty()) {

            edtSampleDevice.setError(
                    "Enter device name"
            );

            edtSampleDevice.requestFocus();

            return;
        }

        if (service.isEmpty()) {

            edtSampleService.setError(
                    "Enter repair service"
            );

            edtSampleService.requestFocus();

            return;
        }

        if (description.isEmpty()) {

            edtSampleDescription.setError(
                    "Enter description"
            );

            edtSampleDescription.requestFocus();

            return;
        }

        Toast.makeText(
                this,
                "Repair sample saved",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }
}