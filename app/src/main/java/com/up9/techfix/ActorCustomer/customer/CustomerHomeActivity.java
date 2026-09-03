package com.up9.techfix.ActorCustomer.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.ActorCustomer.RepairBooking.BookRepairActivity;
import com.up9.techfix.ActorCustomer.RepairBooking.RepairHistoryActivity;
import com.up9.techfix.ActorCustomer.RepairBooking.RepairTrackingActivity;
import com.up9.techfix.ActorCustomer.map.BranchesActivity;
import com.up9.techfix.ActorCustomer.payment.PaymentActivity;
import com.up9.techfix.ActorCustomer.service.ServicesActivity;
import com.up9.techfix.R;

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

        initializeViews();
        setupButtons();
    }

    private void initializeViews() {
        btnServices =
                findViewById(R.id.btnServices);

        btnBookRepair =
                findViewById(R.id.btnBookRepair);

        btnTrackRepair =
                findViewById(R.id.btnTrackRepair);

        btnHistory =
                findViewById(R.id.btnHistory);

        btnPayment =
                findViewById(R.id.btnPayment);

        btnBranches =
                findViewById(R.id.btnBranches);
    }

    private void setupButtons() {

        btnServices.setOnClickListener(v ->
                openActivity(
                        ServicesActivity.class
                )
        );

        btnBookRepair.setOnClickListener(v ->
                openActivity(
                        BookRepairActivity.class
                )
        );

        btnTrackRepair.setOnClickListener(v ->
                openActivity(
                        RepairTrackingActivity.class
                )
        );

        btnHistory.setOnClickListener(v ->
                openActivity(
                        RepairHistoryActivity.class
                )
        );

        btnPayment.setOnClickListener(v ->
                openActivity(
                        PaymentActivity.class
                )
        );

        btnBranches.setOnClickListener(v ->
                openActivity(
                        BranchesActivity.class
                )
        );
    }

    private void openActivity(
            Class<?> activityClass
    ) {

        Intent intent =
                new Intent(
                        CustomerHomeActivity.this,
                        activityClass
                );

        startActivity(intent);
    }
}