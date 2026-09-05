package com.up9.techfix.admin.technicians;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;

public class TechnicianFormActivity
        extends AppCompatActivity {

    private TextView txtTechnicianFormTitle;

    private EditText edtTechnicianName;
    private EditText edtTechnicianPhone;
    private EditText edtTechnicianEmail;
    private EditText edtTechnicianPassword;

    private Spinner spinnerSpecialization;
    private Spinner spinnerBranch;

    private CheckBox checkAvailable;

    private Button btnSaveTechnician;
    private Button btnCancelTechnician;

    private DatabaseHelper databaseHelper;

    private boolean isEditMode = false;
    private int technicianId = -1;

    private final String[] specializations = {
            "Mobile Phone Repair",
            "Laptop Repair",
            "Desktop Repair",
            "Tablet Repair",
            "Hardware Repair",
            "Software Repair"
    };

    private final String[] branches = {
            "Colombo",
            "Galle"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.technician_form
        );

        databaseHelper =
                new DatabaseHelper(this);

        initializeViews();
        setupSpinners();
        checkEditMode();

        btnSaveTechnician.setOnClickListener(
                v -> saveTechnician()
        );

        btnCancelTechnician.setOnClickListener(
                v -> finish()
        );
    }

    private void initializeViews() {

        txtTechnicianFormTitle =
                findViewById(
                        R.id.txtTechnicianFormTitle
                );

        edtTechnicianName =
                findViewById(
                        R.id.edtTechnicianName
                );

        edtTechnicianPhone =
                findViewById(
                        R.id.edtTechnicianPhone
                );

        edtTechnicianEmail =
                findViewById(
                        R.id.edtTechnicianEmail
                );

        edtTechnicianPassword =
                findViewById(
                        R.id.edtTechnicianPassword
                );

        spinnerSpecialization =
                findViewById(
                        R.id.spinnerSpecialization
                );

        spinnerBranch =
                findViewById(
                        R.id.spinnerBranch
                );

        checkAvailable =
                findViewById(
                        R.id.checkAvailable
                );

        btnSaveTechnician =
                findViewById(
                        R.id.btnSaveTechnician
                );

        btnCancelTechnician =
                findViewById(
                        R.id.btnCancelTechnician
                );
    }

    private void setupSpinners() {

        ArrayAdapter<String>
                specializationAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        specializations
                );

        specializationAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerSpecialization.setAdapter(
                specializationAdapter
        );

        ArrayAdapter<String>
                branchAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        branches
                );

        branchAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerBranch.setAdapter(
                branchAdapter
        );
    }

    private void checkEditMode() {

        Intent intent =
                getIntent();

        isEditMode =
                intent.getBooleanExtra(
                        "editMode",
                        false
                );

        if (!isEditMode) {

            txtTechnicianFormTitle.setText(
                    "Add Technician"
            );

            checkAvailable.setChecked(true);

            return;
        }

        technicianId =
                intent.getIntExtra(
                        "technicianId",
                        -1
                );

        txtTechnicianFormTitle.setText(
                "Edit Technician"
        );

        edtTechnicianName.setText(
                intent.getStringExtra("name")
        );

        edtTechnicianPhone.setText(
                intent.getStringExtra("phone")
        );

        edtTechnicianEmail.setText(
                intent.getStringExtra("email")
        );

        edtTechnicianPassword.setHint(
                "Leave blank to keep current password"
        );

        checkAvailable.setChecked(
                intent.getBooleanExtra(
                        "available",
                        true
                )
        );

        setSpinnerValue(
                spinnerSpecialization,
                intent.getStringExtra(
                        "specialization"
                )
        );

        setSpinnerValue(
                spinnerBranch,
                intent.getStringExtra(
                        "branch"
                )
        );
    }

    private void setSpinnerValue(
            Spinner spinner,
            String value
    ) {

        if (value == null) {
            return;
        }

        for (int i = 0;
             i < spinner.getCount();
             i++) {

            if (spinner
                    .getItemAtPosition(i)
                    .toString()
                    .equals(value)) {

                spinner.setSelection(i);
                break;
            }
        }
    }

    private void saveTechnician() {

        String name =
                edtTechnicianName
                        .getText()
                        .toString()
                        .trim();

        String phone =
                edtTechnicianPhone
                        .getText()
                        .toString()
                        .trim();

        String email =
                edtTechnicianEmail
                        .getText()
                        .toString()
                        .trim();

        String password =
                edtTechnicianPassword
                        .getText()
                        .toString();

        String specialization =
                spinnerSpecialization
                        .getSelectedItem()
                        .toString();

        String branch =
                spinnerBranch
                        .getSelectedItem()
                        .toString();

        boolean available =
                checkAvailable.isChecked();

        if (name.isEmpty()) {

            edtTechnicianName.setError(
                    "Enter technician name"
            );

            edtTechnicianName.requestFocus();
            return;
        }

        if (phone.isEmpty()
                || !phone.matches("0[0-9]{9}")) {

            edtTechnicianPhone.setError(
                    "Enter a valid Sri Lankan phone number"
            );

            edtTechnicianPhone.requestFocus();
            return;
        }

        if (email.isEmpty()
                || !Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            edtTechnicianEmail.setError(
                    "Enter a valid email"
            );

            edtTechnicianEmail.requestFocus();
            return;
        }

        if (!isEditMode &&
                password.trim().isEmpty()) {

            edtTechnicianPassword.setError(
                    "Enter a password"
            );

            edtTechnicianPassword.requestFocus();
            return;
        }

        if (!password.trim().isEmpty()
                && password.length() < 6) {

            edtTechnicianPassword.setError(
                    "Password must be at least 6 characters"
            );

            edtTechnicianPassword.requestFocus();
            return;
        }

        if (!isEditMode) {

            long result =
                    databaseHelper.createTechnician(
                            name,
                            phone,
                            email,
                            password,
                            specialization,
                            branch,
                            available
                    );

            if (result == -2) {

                edtTechnicianEmail.setError(
                        "Email is already registered"
                );

                edtTechnicianEmail.requestFocus();
                return;
            }

            if (result == -1) {

                Toast.makeText(
                        this,
                        "Failed to create technician",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Toast.makeText(
                    this,
                    "Technician created successfully",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            int result =
                    databaseHelper.updateTechnician(
                            technicianId,
                            name,
                            phone,
                            email,
                            password,
                            specialization,
                            branch,
                            available
                    );

            if (result == -2) {

                edtTechnicianEmail.setError(
                        "Email is already registered"
                );

                edtTechnicianEmail.requestFocus();
                return;
            }

            if (result <= 0) {

                Toast.makeText(
                        this,
                        "Failed to update technician",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Toast.makeText(
                    this,
                    "Technician updated successfully",
                    Toast.LENGTH_SHORT
            ).show();
        }

        setResult(
                RESULT_OK,
                new Intent()
        );

        finish();
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}