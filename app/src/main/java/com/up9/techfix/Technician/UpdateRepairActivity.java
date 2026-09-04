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

    TechOpenHelper dbHelper;

    int repairId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_repair);


        repairId = getIntent().getIntExtra(
                "repair_id",
                1
        );


        dbHelper = new TechOpenHelper(this);


        spinnerStatus = findViewById(R.id.spinnerStatus);
        etNotes = findViewById(R.id.etNotes);
        etSparePart = findViewById(R.id.etSparePart);
        etQuantity = findViewById(R.id.etQuantity);
        btnSaveUpdate = findViewById(R.id.btnSaveUpdate);

        ivRepairPhoto = findViewById(R.id.ivRepairPhoto);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);


        String[] statuses = {
                "Assigned",
                "In Progress",
                "Waiting for Parts",
                "Testing",
                "Completed",
                "Ready for Collection"
        };


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        statuses
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerStatus.setAdapter(adapter);



        btnTakePhoto.setOnClickListener(v -> {

            Intent cameraIntent =
                    new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE
                    );

            startActivityForResult(
                    cameraIntent,
                    100
            );
        });



        btnSaveUpdate.setOnClickListener(v -> {

            String status =
                    spinnerStatus
                            .getSelectedItem()
                            .toString();

            String notes =
                    etNotes
                            .getText()
                            .toString()
                            .trim();

            String sparePart =
                    etSparePart
                            .getText()
                            .toString()
                            .trim();

            String quantityText =
                    etQuantity
                            .getText()
                            .toString()
                            .trim();


            if (notes.isEmpty()) {

                etNotes.setError(
                        "Please enter technician notes"
                );

                etNotes.requestFocus();

                return;
            }


            int quantity = 0;

            if (!quantityText.isEmpty()) {

                quantity =
                        Integer.parseInt(
                                quantityText
                        );
            }


            // Update Repairs table
            boolean updated =
                    dbHelper.updateRepairStatus(
                            repairId,
                            status
                    );


            // Save Repair Update history
            long updateResult =
                    dbHelper.insertRepairUpdate(
                            repairId,
                            status,
                            notes,
                            sparePart,
                            quantity,
                            ""
                    );


            if (updated && updateResult != -1) {

                Toast.makeText(
                        UpdateRepairActivity.this,
                        "Repair updated successfully",
                        Toast.LENGTH_SHORT
                ).show();


                finish();

            } else {

                Toast.makeText(
                        UpdateRepairActivity.this,
                        "Update failed",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }


    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == 100
                && resultCode == RESULT_OK
                && data != null) {

            Bundle extras =
                    data.getExtras();

            if (extras != null) {

                Bitmap imageBitmap =
                        (Bitmap) extras.get("data");

                if (imageBitmap != null) {

                    ivRepairPhoto.setImageBitmap(
                            imageBitmap
                    );
                }
            }
        }
    }
}