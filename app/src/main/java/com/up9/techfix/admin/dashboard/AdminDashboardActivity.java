package com.up9.techfix.admin.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;
import com.up9.techfix.admin.appoiments.ManageAppointmentsActivity;
import com.up9.techfix.admin.branches.ManageBranchesActivity;
import com.up9.techfix.admin.categories.ManageCategoriesActivity;
import com.up9.techfix.admin.payments.ManagePaymentsActivity;
import com.up9.techfix.admin.repairsamples.ManageRepairSamplesActivity;
import com.up9.techfix.admin.services.ManageServicesActivity;
import com.up9.techfix.admin.spareparts.ManageSparePartsActivity;
import com.up9.techfix.admin.technicians.ManageTechniciansActivity;
import com.up9.techfix.data.DatabaseHelper;

public class AdminDashboardActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private TextView txtTotalRepairs;
    private TextView txtPendingRepairs;
    private View btnBranches;
    private View btnCategories;
    private View btnServices;
    private View btnTechnicians;
    private View btnSpareParts;
    private View btnRepairs;
    private View btnPayments;
    private View btnLogout;
    private View btnRepairSamples;
    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_admin_dashboard
        );
        databaseHelper =
                new DatabaseHelper(this);

        txtTotalRepairs =
                findViewById(
                        R.id.txtTotalRepairs
                );

        txtPendingRepairs =
                findViewById(
                        R.id.txtPendingRepairs
                );


        btnBranches =
                findViewById(
                        R.id.btnBranches
                );

        btnCategories =
                findViewById(
                        R.id.btnCategories
                );

        btnServices =
                findViewById(
                        R.id.btnServices
                );

        btnTechnicians =
                findViewById(
                        R.id.btnTechnicians
                );

        btnSpareParts =
                findViewById(
                        R.id.btnSpareParts
                );

        btnRepairs =
                findViewById(
                        R.id.btnRepairs
                );

        btnPayments =
                findViewById(
                        R.id.btnPayments
                );

        btnRepairSamples =
                findViewById(
                        R.id.btnRepairSamples
                );

        btnLogout =
                findViewById(
                        R.id.btnLogout
                );


        loadDashboardStatistics();


        setupButtonActions();
    }

    private void loadDashboardStatistics() {

        if (databaseHelper == null) {
            return;
        }

        try {
            int totalRepairs =
                    databaseHelper.getTotalRepairs();

            txtTotalRepairs.setText(
                    String.valueOf(
                            totalRepairs
                    )
            );
            int pendingRepairs =
                    databaseHelper.getRepairCountByStatus(
                            "Pending"
                    );

            txtPendingRepairs.setText(
                    String.valueOf(
                            pendingRepairs
                    )
            );

        } catch (Exception e) {

            txtTotalRepairs.setText("0");
            txtPendingRepairs.setText("0");
        }
    }

    private void setupButtonActions() {

        btnBranches.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    AdminDashboardActivity.this,
                                    ManageBranchesActivity.class
                            );

                    startActivity(intent);
                }
        );


        btnCategories.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    AdminDashboardActivity.this,
                                    ManageCategoriesActivity.class
                            );

                    startActivity(intent);
                }
        );

        btnServices.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    AdminDashboardActivity.this,
                                    ManageServicesActivity.class
                            );

                    startActivity(intent);
                }
        );


        btnTechnicians.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    AdminDashboardActivity.this,
                                    ManageTechniciansActivity.class
                            );

                    startActivity(intent);
                }
        );


        btnSpareParts.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    AdminDashboardActivity.this,
                                    ManageSparePartsActivity.class
                            );

                    startActivity(intent);
                }
        );


        btnRepairs.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    AdminDashboardActivity.this,
                                    ManageAppointmentsActivity.class
                            );

                    startActivity(intent);
                }
        );


        btnRepairSamples.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    AdminDashboardActivity.this,
                                    ManageRepairSamplesActivity.class
                            );

                    startActivity(intent);
                }
        );

        btnPayments.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    AdminDashboardActivity.this,
                                    ManagePaymentsActivity.class
                            );

                    startActivity(intent);
                }
        );


        btnLogout.setOnClickListener(
                v -> {

                    finish();
                }
        );
    }


    @Override
    protected void onResume() {

        super.onResume();


        if (databaseHelper != null) {

            loadDashboardStatistics();
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
