package com.up9.techfix.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;
import com.up9.techfix.map.BranchesActivity;
import com.up9.techfix.payment.PaymentActivity;
import com.up9.techfix.service.ServicesActivity;
import com.up9.techfix.booking.BookRepairActivity;


import com.up9.techfix.data.DatabaseViewerActivity;

public class CustomerHomeActivity extends AppCompatActivity {

    private Button btnServices;
    private Button btnBookRepair;
    private Button btnTrackRepair;
    private Button btnHistory;
    private Button btnPayment;
    private Button btnBranches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        btnServices = findViewById(R.id.btnServices);
        btnBookRepair = findViewById(R.id.btnBookRepair);
        btnTrackRepair = findViewById(R.id.btnTrackRepair);
        btnHistory = findViewById(R.id.btnHistory);
        btnPayment = findViewById(R.id.btnPayment);
        btnBranches = findViewById(R.id.btnBranches);

        btnServices.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerHomeActivity.this, ServicesActivity.class);
            startActivity(intent);
        });

        btnBookRepair.setOnClickListener(v -> {
            Intent intent = new Intent(
                    CustomerHomeActivity.this,
                    BookRepairActivity.class
            );
            startActivity(intent);
        });

        btnTrackRepair.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerHomeActivity.this, RepairTrackingActivity.class);
            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerHomeActivity.this, RepairHistoryActivity.class);
            startActivity(intent);
        });

        btnPayment.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerHomeActivity.this, PaymentActivity.class);
            startActivity(intent);
        });

        btnBranches.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerHomeActivity.this, BranchesActivity.class);
            startActivity(intent);
        });

        //delete later
        Button btnDatabaseViewer = findViewById(R.id.btnDatabaseViewer);

        btnDatabaseViewer.setOnClickListener(v -> {
            Intent intent = new Intent(
                    CustomerHomeActivity.this,
                    DatabaseViewerActivity.class
            );

            startActivity(intent);
        });
    }
}