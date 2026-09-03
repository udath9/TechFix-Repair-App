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

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class TechnicianFormActivity extends AppCompatActivity {

    private TextView txtTechnicianFormTitle;

    private EditText edtTechnicianName;
    private EditText edtTechnicianPhone;
    private EditText edtTechnicianEmail;

    private Spinner spinnerSpecialization;
    private Spinner spinnerBranch;

    private CheckBox checkAvailable;

    private Button btnSaveTechnician;
    private Button btnCancelTechnician;

    private boolean isEditMode = false;

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

        setContentView(R.layout.technician_form);

        txtTechnicianFormTitle =
                findViewById(R.id.txtTechnicianFormTitle);

        edtTechnicianName =
                findViewById(R.id.edtTechnicianName);

        edtTechnicianPhone =
                findViewById(R.id.edtTechnicianPhone);

        edtTechnicianEmail =
                findViewById(R.id.edtTechnicianEmail);

        spinnerSpecialization =
                findViewById(R.id.spinnerSpecialization);

        spinnerBranch =
                findViewById(R.id.spinnerBranch);

        checkAvailable =
                findViewById(R.id.checkAvailable);

        btnSaveTechnician =
                findViewById(R.id.btnSaveTechnician);

        btnCancelTechnician =
                findViewById(R.id.btnCancelTechnician);

        setupSpinners();

        checkEditMode();

        btnSaveTechnician.setOnClickListener(
                v -> saveTechnician()
        );

        btnCancelTechnician.setOnClickListener(
                v -> finish()
        );
    }

    private void setupSpinners() {

        ArrayAdapter<String> specializationAdapter =
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

        ArrayAdapter<String> branchAdapter =
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

        Intent intent = getIntent();

        isEditMode =
                intent.getBooleanExtra(
                        "editMode",
                        false
                );

        if (!isEditMode) {
            return;
        }

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

            if (spinner.getItemAtPosition(i)
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

        if (phone.isEmpty()) {

            edtTechnicianPhone.setError(
                    "Enter phone number"
            );

            edtTechnicianPhone.requestFocus();

            return;
        }

        if (!phone.matches(
                "0[0-9]{9}"
        )) {

            edtTechnicianPhone.setError(
                    "Enter a valid Sri Lankan phone number"
            );

            edtTechnicianPhone.requestFocus();

            return;
        }

        if (email.isEmpty()) {

            edtTechnicianEmail.setError(
                    "Enter email"
            );

            edtTechnicianEmail.requestFocus();

            return;
        }

        if (!Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            edtTechnicianEmail.setError(
                    "Enter a valid email"
            );

            edtTechnicianEmail.requestFocus();

            return;
        }

        Intent resultIntent =
                new Intent();

        resultIntent.putExtra(
                "name",
                name
        );

        resultIntent.putExtra(
                "phone",
                phone
        );

        resultIntent.putExtra(
                "email",
                email
        );

        resultIntent.putExtra(
                "specialization",
                specialization
        );

        resultIntent.putExtra(
                "branch",
                branch
        );

        resultIntent.putExtra(
                "available",
                available
        );

        if (isEditMode) {

            resultIntent.putExtra(
                    "technicianId",
                    getIntent().getIntExtra(
                            "technicianId",
                            -1
                    )
            );
        }

        setResult(
                RESULT_OK,
                resultIntent
        );

        finish();
    }
}
