package com.up9.techfix.admin;

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

            // We will connect this later
            // startActivity(new Intent(this, ManageBranchesActivity.class));

        });

        btnCategories.setOnClickListener(v -> {

            // We will connect this later

        });

        btnServices.setOnClickListener(v -> {

            // We will connect this later

        });

        btnTechnicians.setOnClickListener(v -> {

            // We will connect this later

        });

        btnSpareParts.setOnClickListener(v -> {

            // We will connect this later

        });

        btnRepairs.setOnClickListener(v -> {

            // We will connect this later

        });

        btnPayments.setOnClickListener(v -> {

            // We will connect this later

        });

        btnLogout.setOnClickListener(v -> {

            finish();

        });
    }
}