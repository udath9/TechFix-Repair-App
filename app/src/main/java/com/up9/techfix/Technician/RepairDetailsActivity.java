package com.up9.techfix.Technician;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;

public class RepairDetailsActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;

    int repairId;

    TextView tvCustomerName;
    TextView tvCustomerPhone;
    TextView tvCustomerEmail;

    TextView tvDeviceCategory;
    TextView tvDeviceModel;

    TextView tvService;
    TextView tvProblem;
    TextView tvRepairDate;

    TextView tvBranchName;
    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repair_details);


        repairId = getIntent().getIntExtra(
                "repair_id",
                1
        );


        dbHelper = new DatabaseHelper(this);


        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        tvCustomerEmail = findViewById(R.id.tvCustomerEmail);

        tvDeviceCategory = findViewById(R.id.tvDeviceCategory);
        tvDeviceModel = findViewById(R.id.tvDeviceModel);

        tvService = findViewById(R.id.tvService);
        tvProblem = findViewById(R.id.tvProblem);
        tvRepairDate = findViewById(R.id.tvRepairDate);

        tvBranchName = findViewById(R.id.tvBranchName);
        tvStatus = findViewById(R.id.tvStatus);

        Button btnUpdateRepair =
                findViewById(R.id.btnUpdateRepair);


        loadRepair();


        btnUpdateRepair.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RepairDetailsActivity.this,
                    UpdateRepairActivity.class
            );

            intent.putExtra(
                    "repair_id",
                    repairId
            );

            startActivity(intent);
        });
    }


    private void loadRepair() {

        Cursor cursor =
                dbHelper.getRepairById(repairId);

        if (cursor.moveToFirst()) {

            tvCustomerName.setText(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "customer_name"
                            )
                    )
            );

            tvCustomerPhone.setText(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "customer_phone"
                            )
                    )
            );

            tvCustomerEmail.setText(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "customer_email"
                            )
                    )
            );

            tvDeviceCategory.setText(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "device_category"
                            )
                    )
            );

            tvDeviceModel.setText(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "device_model"
                            )
                    )
            );

            tvService.setText(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "service"
                            )
                    )
            );

            tvProblem.setText(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "problem"
                            )
                    )
            );

            tvRepairDate.setText(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "repair_date"
                            )
                    )
            );

            tvBranchName.setText(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "branch"
                            )
                    )
            );

            tvStatus.setText(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "status"
                            )
                    )
            );
        }

        cursor.close();
    }


    @Override
    protected void onResume() {
        super.onResume();

        loadRepair();
    }
}