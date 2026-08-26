package com.up9.techfix.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class BranchFormActivity extends AppCompatActivity {

    private TextView txtFormTitle;

    private EditText edtBranchName;
    private EditText edtBranchAddress;
    private EditText edtBranchPhone;
    private EditText edtLatitude;
    private EditText edtLongitude;

    private Button btnSaveBranch;
    private Button btnCancel;

    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_branch_form);

        txtFormTitle = findViewById(R.id.txtFormTitle);

        edtBranchName = findViewById(R.id.edtBranchName);
        edtBranchAddress = findViewById(R.id.edtBranchAddress);
        edtBranchPhone = findViewById(R.id.edtBranchPhone);
        edtLatitude = findViewById(R.id.edtLatitude);
        edtLongitude = findViewById(R.id.edtLongitude);

        btnSaveBranch = findViewById(R.id.btnSaveBranch);
        btnCancel = findViewById(R.id.btnCancel);

        checkEditMode();

        btnSaveBranch.setOnClickListener(v -> saveBranch());

        btnCancel.setOnClickListener(v -> finish());
    }

    private void checkEditMode() {

        Intent intent = getIntent();

        isEditMode = intent.getBooleanExtra("editMode", false);

        if (isEditMode) {

            txtFormTitle.setText("Edit Branch");

            edtBranchName.setText(
                    intent.getStringExtra("name")
            );

            edtBranchAddress.setText(
                    intent.getStringExtra("address")
            );

            edtBranchPhone.setText(
                    intent.getStringExtra("phone")
            );

            edtLatitude.setText(
                    String.valueOf(
                            intent.getDoubleExtra("latitude", 0)
                    )
            );

            edtLongitude.setText(
                    String.valueOf(
                            intent.getDoubleExtra("longitude", 0)
                    )
            );
        }
    }

    private void saveBranch() {

        String name = edtBranchName.getText().toString().trim();
        String address = edtBranchAddress.getText().toString().trim();
        String phone = edtBranchPhone.getText().toString().trim();
        String latitudeText = edtLatitude.getText().toString().trim();
        String longitudeText = edtLongitude.getText().toString().trim();

        if (name.isEmpty()) {
            edtBranchName.setError("Enter branch name");
            edtBranchName.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            edtBranchAddress.setError("Enter branch address");
            edtBranchAddress.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            edtBranchPhone.setError("Enter phone number");
            edtBranchPhone.requestFocus();
            return;
        }

        if (latitudeText.isEmpty()) {
            edtLatitude.setError("Enter latitude");
            edtLatitude.requestFocus();
            return;
        }

        if (longitudeText.isEmpty()) {
            edtLongitude.setError("Enter longitude");
            edtLongitude.requestFocus();
            return;
        }

        double latitude;
        double longitude;

        try {

            latitude = Double.parseDouble(latitudeText);
            longitude = Double.parseDouble(longitudeText);

        } catch (NumberFormatException e) {

            Toast.makeText(
                    this,
                    "Latitude and longitude must be valid numbers",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (latitude < -90 || latitude > 90) {

            edtLatitude.setError(
                    "Latitude must be between -90 and 90"
            );

            return;
        }

        if (longitude < -180 || longitude > 180) {

            edtLongitude.setError(
                    "Longitude must be between -180 and 180"
            );

            return;
        }

        Intent resultIntent = new Intent();

        resultIntent.putExtra("name", name);
        resultIntent.putExtra("address", address);
        resultIntent.putExtra("phone", phone);
        resultIntent.putExtra("latitude", latitude);
        resultIntent.putExtra("longitude", longitude);

        if (isEditMode) {

            resultIntent.putExtra(
                    "branchId",
                    getIntent().getIntExtra("branchId", -1)
            );

        }

        setResult(RESULT_OK, resultIntent);

        finish();
    }
}