package com.up9.techfix.admin.appoiments;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class ManageAppointmentActivity
        extends AppCompatActivity {

    private TextView txtManageCustomer;
    private TextView txtManageDevice;
    private TextView txtManageService;

    private Spinner spinnerManageBranch;
    private Spinner spinnerManageTechnician;
    private Spinner spinnerManageStatus;

    private EditText edtManagePrice;

    private Button btnUpdateAppointment;

    private final String[] branches = {
            "Colombo",
            "Galle"
    };

    private final String[] technicians = {
            "Kasun Perera",
            "Nimal Fernando",
            "Amal Silva",
            "Saman Perera"
    };

    private final String[] statuses = {
            "Pending",
            "Accepted",
            "Assigned",
            "Diagnosing",
            "Repairing",
            "Ready for Pickup",
            "Completed",
            "Cancelled"
    };

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_manage_appointment
        );

        txtManageCustomer =
                findViewById(
                        R.id.txtManageCustomer
                );

        txtManageDevice =
                findViewById(
                        R.id.txtManageDevice
                );

        txtManageService =
                findViewById(
                        R.id.txtManageService
                );

        spinnerManageBranch =
                findViewById(
                        R.id.spinnerManageBranch
                );

        spinnerManageTechnician =
                findViewById(
                        R.id.spinnerManageTechnician
                );

        spinnerManageStatus =
                findViewById(
                        R.id.spinnerManageStatus
                );

        edtManagePrice =
                findViewById(
                        R.id.edtManagePrice
                );

        btnUpdateAppointment =
                findViewById(
                        R.id.btnUpdateAppointment
                );

        setupSpinners();

        loadAppointment();

        btnUpdateAppointment.setOnClickListener(
                v -> updateAppointment()
        );
    }

    private void setupSpinners() {

        setupSpinner(
                spinnerManageBranch,
                branches
        );

        setupSpinner(
                spinnerManageTechnician,
                technicians
        );

        setupSpinner(
                spinnerManageStatus,
                statuses
        );
    }

    private void setupSpinner(
            Spinner spinner,
            String[] values
    ) {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        values
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);
    }

    private void loadAppointment() {

        Intent intent = getIntent();

        String customer =
                intent.getStringExtra(
                        "customerName"
                );

        String device =
                intent.getStringExtra(
                        "device"
                );

        String service =
                intent.getStringExtra(
                        "service"
                );

        String branch =
                intent.getStringExtra(
                        "branch"
                );

        String technician =
                intent.getStringExtra(
                        "technician"
                );

        String status =
                intent.getStringExtra(
                        "status"
                );

        double price =
                intent.getDoubleExtra(
                        "price",
                        0
                );

        txtManageCustomer.setText(
                "Customer: " + customer
        );

        txtManageDevice.setText(
                "Device: " + device
        );

        txtManageService.setText(
                "Service: " + service
        );

        edtManagePrice.setText(
                String.valueOf(price)
        );

        setSpinnerValue(
                spinnerManageBranch,
                branch
        );

        setSpinnerValue(
                spinnerManageTechnician,
                technician
        );

        setSpinnerValue(
                spinnerManageStatus,
                status
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

    private void updateAppointment() {

        String branch =
                spinnerManageBranch
                        .getSelectedItem()
                        .toString();

        String technician =
                spinnerManageTechnician
                        .getSelectedItem()
                        .toString();

        String status =
                spinnerManageStatus
                        .getSelectedItem()
                        .toString();

        String priceText =
                edtManagePrice
                        .getText()
                        .toString()
                        .trim();

        if (priceText.isEmpty()) {

            edtManagePrice.setError(
                    "Enter repair price"
            );

            edtManagePrice.requestFocus();

            return;
        }

        double price;

        try {

            price =
                    Double.parseDouble(
                            priceText
                    );

        } catch (NumberFormatException e) {

            edtManagePrice.setError(
                    "Enter a valid price"
            );

            return;
        }

        if (price < 0) {

            edtManagePrice.setError(
                    "Price cannot be negative"
            );

            return;
        }

        Intent result =
                new Intent();

        result.putExtra(
                "branch",
                branch
        );

        result.putExtra(
                "technician",
                technician
        );

        result.putExtra(
                "status",
                status
        );

        result.putExtra(
                "price",
                price
        );

        setResult(
                RESULT_OK,
                result
        );

        finish();
    }
}