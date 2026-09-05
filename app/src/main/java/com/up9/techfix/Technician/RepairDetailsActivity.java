package com.up9.techfix.Technician;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;

public class RepairDetailsActivity extends AppCompatActivity {

    private TextView tvCustomerName;
    private TextView tvCustomerPhone;
    private TextView tvCustomerEmail;

    private TextView tvDeviceCategory;
    private TextView tvDeviceModel;

    private TextView tvService;
    private TextView tvProblem;
    private TextView tvRepairDate;

    private TextView tvBranchName;
    private TextView tvBranchAddress;
    private TextView tvBranchPhone;

    private TextView tvStatus;

    private ImageView ivDeviceImage;
    private Button btnUpdateRepair;

    private DatabaseHelper databaseHelper;

    private int repairId;
    private int technicianId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_details);

        databaseHelper = new DatabaseHelper(this);

        repairId = getIntent().getIntExtra("repair_id", -1);

        technicianId = getIntent().getIntExtra(
                "technician_id",
                getSharedPreferences("TechFixSession", MODE_PRIVATE)
                        .getInt("technicianId", -1)
        );

        if (repairId == -1) {
            Toast.makeText(
                    this,
                    "Invalid repair",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        initializeViews();
        loadRepairDetails();
    }

    private void initializeViews() {

        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        tvCustomerEmail = findViewById(R.id.tvCustomerEmail);

        tvDeviceCategory = findViewById(R.id.tvDeviceCategory);
        tvDeviceModel = findViewById(R.id.tvDeviceModel);

        tvService = findViewById(R.id.tvService);
        tvProblem = findViewById(R.id.tvProblem);
        tvRepairDate = findViewById(R.id.tvRepairDate);

        tvBranchName = findViewById(R.id.tvBranchName);
        tvBranchAddress = findViewById(R.id.tvBranchAddress);
        tvBranchPhone = findViewById(R.id.tvBranchPhone);

        tvStatus = findViewById(R.id.tvStatus);

        ivDeviceImage = findViewById(R.id.ivDeviceImage);

        btnUpdateRepair = findViewById(R.id.btnUpdateRepair);

        btnUpdateRepair.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RepairDetailsActivity.this,
                    UpdateRepairActivity.class
            );

            intent.putExtra("repair_id", repairId);
            intent.putExtra("technician_id", technicianId);

            startActivity(intent);
        });
    }

    private void loadRepairDetails() {

        Cursor cursor = databaseHelper.getRepairById(repairId);

        if (cursor == null || !cursor.moveToFirst()) {

            Toast.makeText(
                    this,
                    "Repair not found",
                    Toast.LENGTH_SHORT
            ).show();

            if (cursor != null) {
                cursor.close();
            }

            finish();
            return;
        }

        try {

            // -----------------------------
            // Customer
            // -----------------------------

            tvCustomerName.setText(
                    getColumnString(cursor, "customer_name")
            );

            tvCustomerPhone.setText(
                    getColumnString(cursor, "customer_phone")
            );

            tvCustomerEmail.setText(
                    getColumnString(cursor, "customer_email")
            );


            // -----------------------------
            // Device
            // -----------------------------

            tvDeviceCategory.setText(
                    getColumnString(cursor, "category_name")
            );

            tvDeviceModel.setText(
                    getColumnString(cursor, "device_model")
            );


            // -----------------------------
            // Repair
            // -----------------------------

            tvService.setText(
                    getColumnString(cursor, "service_name")
            );

            tvProblem.setText(
                    getColumnString(cursor, "problem_description")
            );

            tvRepairDate.setText(
                    getColumnString(cursor, "repair_date")
            );


            // -----------------------------
            // Branch
            // -----------------------------

            tvBranchName.setText(
                    getColumnString(cursor, "branch_name")
            );

            tvBranchAddress.setText(
                    getColumnString(cursor, "branch_address")
            );

            tvBranchPhone.setText(
                    getColumnString(cursor, "branch_phone")
            );


            // -----------------------------
            // Status
            // -----------------------------

            tvStatus.setText(
                    getColumnString(cursor, "status")
            );


            // -----------------------------
            // Device Image
            // -----------------------------

            String imageUri = getColumnString(
                    cursor,
                    "image_uri"
            );

            if (!imageUri.equals("Not available")
                    && !imageUri.trim().isEmpty()) {

                try {

                    ivDeviceImage.setImageURI(
                            Uri.parse(imageUri)
                    );

                } catch (Exception e) {

                    ivDeviceImage.setImageResource(
                            android.R.drawable.ic_menu_gallery
                    );
                }

            } else {

                ivDeviceImage.setImageResource(
                        android.R.drawable.ic_menu_gallery
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            Toast.makeText(
                    this,
                    "Unable to load repair details",
                    Toast.LENGTH_SHORT
            ).show();

        } finally {

            cursor.close();
        }
    }

    private String getColumnString(
            Cursor cursor,
            String columnName
    ) {

        int index = cursor.getColumnIndex(columnName);

        if (index == -1 || cursor.isNull(index)) {
            return "Not available";
        }

        String value = cursor.getString(index);

        if (value == null || value.trim().isEmpty()) {
            return "Not available";
        }

        return value;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null && repairId != -1) {
            loadRepairDetails();
        }
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}