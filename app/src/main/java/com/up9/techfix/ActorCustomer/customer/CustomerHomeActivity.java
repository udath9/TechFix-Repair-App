package com.up9.techfix.ActorCustomer.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.up9.techfix.ActorCustomer.RepairBooking.BookRepairActivity;
import com.up9.techfix.ActorCustomer.RepairBooking.RepairHistoryActivity;
import com.up9.techfix.ActorCustomer.RepairBooking.RepairTrackingActivity;
import com.up9.techfix.ActorCustomer.auth.LoginActivity;
import com.up9.techfix.ActorCustomer.map.BranchesActivity;
import com.up9.techfix.ActorCustomer.payment.PaymentActivity;
import com.up9.techfix.ActorCustomer.service.ServicesActivity;
import com.up9.techfix.R;

public class CustomerHomeActivity extends AppCompatActivity {


    private MaterialCardView btnServices;
    private MaterialCardView btnBookRepair;
    private MaterialCardView btnTrackRepair;
    private MaterialCardView btnHistory;
    private MaterialCardView btnPayment;
    private MaterialCardView btnBranches;

    private TextView tvUserName;
    private FrameLayout btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_home);

        initializeViews();
        setupButtons();
        setupLogout();
        loadUserName();
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

        tvUserName =
                findViewById(R.id.tvUserName);

        btnLogout =
                findViewById(R.id.btnLogout);
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


    private void loadUserName() {

        if (tvUserName == null) {
            return;
        }

        tvUserName.setText(
                getCustomerDisplayName()
        );
    }

    private String getCustomerDisplayName() {

        if (getIntent() != null &&
                getIntent().hasExtra("customer_name")) {

            String intentName =
                    getIntent().getStringExtra(
                            "customer_name"
                    );

            if (intentName != null &&
                    !intentName.trim().isEmpty()) {

                return intentName.trim();
            }
        }

        SharedPreferences sessionPreferences =
                getSharedPreferences(
                        "TechFixSession",
                        MODE_PRIVATE
                );

        String[] possibleNameKeys = {
                "customerName",
                "userName",
                "name",
                "fullName"
        };

        for (String key : possibleNameKeys) {

            String storedName =
                    sessionPreferences.getString(
                            key,
                            null
                    );

            if (storedName != null &&
                    !storedName.trim().isEmpty()) {

                return storedName.trim();
            }
        }

        String email =
                sessionPreferences.getString(
                        "userEmail",
                        null
                );

        if (email != null && email.contains("@")) {

            String localPart =
                    email.substring(
                            0,
                            email.indexOf('@')
                    );

            if (!localPart.trim().isEmpty()) {

                return localPart.trim();
            }
        }

        return "Customer";
    }

    private void setupLogout() {

        if (btnLogout == null) {
            return;
        }

        btnLogout.setOnClickListener(v -> {

            SharedPreferences sessionPreferences =
                    getSharedPreferences(
                            "TechFixSession",
                            MODE_PRIVATE
                    );

            sessionPreferences.edit()
                    .clear()
                    .apply();

            SharedPreferences oldPreferences =
                    getSharedPreferences(
                            "TechFixPrefs",
                            MODE_PRIVATE
                    );

            oldPreferences.edit()
                    .clear()
                    .apply();

            Intent intent =
                    new Intent(
                            CustomerHomeActivity.this,
                            LoginActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            finish();
        });
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