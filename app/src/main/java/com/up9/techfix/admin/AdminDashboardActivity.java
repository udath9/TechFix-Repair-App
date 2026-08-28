package com.up9.techfix.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button btnBranches;
    private Button btnCategories;
    private Button btnServices;
    private Button btnTechnicians;
    private Button btnSpareParts;
    private Button btnRepairs;
    private Button btnPayments;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_dashboard);

        btnBranches = findViewById(R.id.btnBranches);
        btnCategories = findViewById(R.id.btnCategories);
        btnServices = findViewById(R.id.btnServices);
        btnTechnicians = findViewById(R.id.btnTechnicians);
        btnSpareParts = findViewById(R.id.btnSpareParts);
        btnRepairs = findViewById(R.id.btnRepairs);
        btnPayments = findViewById(R.id.btnPayments);
        btnLogout = findViewById(R.id.btnLogout);

        btnBranches.setOnClickListener(v -> {
            Intent intent =
                    new Intent(
                            AdminDashboardActivity.this,
                            ManageBranchesActivity.class
                    );

            startActivity(intent);
        });

        btnCategories.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AdminDashboardActivity.this,
                            ManageCategoriesActivity.class
                    );

            startActivity(intent);

        });

        btnServices.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AdminDashboardActivity.this,
                            ManageServicesActivity.class
                    );

            startActivity(intent);

        });

        btnTechnicians.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AdminDashboardActivity.this,
                            ManageTechniciansActivity.class
                    );

            startActivity(intent);
        });

        btnSpareParts.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AdminDashboardActivity.this,
                            ManageSparePartsActivity.class
                    );

            startActivity(intent);
        });

        btnRepairs.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AdminDashboardActivity.this,
                            ManageAppointmentsActivity.class
                    );

            startActivity(intent);

        });

        btnPayments.setOnClickListener(v -> {


        });

        btnLogout.setOnClickListener(v -> {

            finish();

        });
    }
}