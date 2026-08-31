package com.up9.techfix.Technician;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class UpdateRepairActivity extends AppCompatActivity {

    Spinner spinnerStatus;
    EditText etNotes;
    EditText etSparePart;
    EditText etQuantity;
    Button btnSaveUpdate;

    ImageView ivRepairPhoto;
    Button btnTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_repair);

        // Find views
        spinnerStatus = findViewById(R.id.spinnerStatus);
        etNotes = findViewById(R.id.etNotes);
        etSparePart = findViewById(R.id.etSparePart);
        etQuantity = findViewById(R.id.etQuantity);
        btnSaveUpdate = findViewById(R.id.btnSaveUpdate);

        ivRepairPhoto = findViewById(R.id.ivRepairPhoto);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);


        // Camera button
        btnTakePhoto.setOnClickListener(v -> {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, 100);
            } else {
                Toast.makeText(
                        UpdateRepairActivity.this,
                        "Camera is not available",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });


        // Repair status list
        String[] statuses = {
                "Assigned",
                "Diagnosing",
                "Repairing",
                "Waiting for Parts",
                "Testing",
                "Completed",
                "Ready for Collection"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statuses
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerStatus.setAdapter(adapter);


        // Save repair update
        btnSaveUpdate.setOnClickListener(v -> {

            String status = spinnerStatus.getSelectedItem().toString();
            String notes = etNotes.getText().toString().trim();
            String sparePart = etSparePart.getText().toString().trim();
            String quantity = etQuantity.getText().toString().trim();

            if (notes.isEmpty()) {
                etNotes.setError("Please enter technician notes");
                etNotes.requestFocus();
                return;
            }

            Toast.makeText(
                    UpdateRepairActivity.this,
                    "Repair updated: " + status,
                    Toast.LENGTH_SHORT
            ).show();
        });
    }


    // Receive photo from camera
    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            Bundle extras = data.getExtras();

            if (extras != null) {

                Bitmap imageBitmap =
                        (Bitmap) extras.get("data");

                if (imageBitmap != null) {

                    ivRepairPhoto.setImageBitmap(imageBitmap);

                    Toast.makeText(
                            UpdateRepairActivity.this,
                            "Repair photo added",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        }
    }
}