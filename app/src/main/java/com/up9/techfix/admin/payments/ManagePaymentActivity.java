package com.up9.techfix.admin.payments;
import com.up9.techfix.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ManagePaymentActivity
        extends AppCompatActivity {

    private TextView txtPaymentCustomerDetails;
    private TextView txtPaymentAppointmentDetails;

    private EditText edtPaymentAmount;

    private Spinner spinnerPaymentMethod;
    private Spinner spinnerPaymentStatus;

    private Button btnUpdatePayment;

    private final String[] paymentMethods = {
            "Cash",
            "Card",
            "Bank Transfer",
            "Online Payment"
    };

    private final String[] paymentStatuses = {
            "Pending",
            "Paid",
            "Failed",
            "Refunded"
    };

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_manage_payment
        );

        txtPaymentCustomerDetails =
                findViewById(
                        R.id.txtPaymentCustomerDetails
                );

        txtPaymentAppointmentDetails =
                findViewById(
                        R.id.txtPaymentAppointmentDetails
                );

        edtPaymentAmount =
                findViewById(
                        R.id.edtPaymentAmount
                );

        spinnerPaymentMethod =
                findViewById(
                        R.id.spinnerPaymentMethod
                );

        spinnerPaymentStatus =
                findViewById(
                        R.id.spinnerPaymentStatus
                );

        btnUpdatePayment =
                findViewById(
                        R.id.btnUpdatePayment
                );

        setupSpinners();

        loadPayment();

        btnUpdatePayment.setOnClickListener(
                v -> updatePayment()
        );
    }

    private void setupSpinners() {

        setupSpinner(
                spinnerPaymentMethod,
                paymentMethods
        );

        setupSpinner(
                spinnerPaymentStatus,
                paymentStatuses
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

    private void loadPayment() {

        Intent intent = getIntent();

        String customer =
                intent.getStringExtra(
                        "customerName"
                );

        int appointmentId =
                intent.getIntExtra(
                        "appointmentId",
                        0
                );

        double amount =
                intent.getDoubleExtra(
                        "amount",
                        0
                );

        String method =
                intent.getStringExtra(
                        "method"
                );

        String status =
                intent.getStringExtra(
                        "status"
                );

        txtPaymentCustomerDetails.setText(
                "Customer: " + customer
        );

        txtPaymentAppointmentDetails.setText(
                "Appointment: #" + appointmentId
        );

        edtPaymentAmount.setText(
                String.valueOf(amount)
        );

        setSpinnerValue(
                spinnerPaymentMethod,
                method
        );

        setSpinnerValue(
                spinnerPaymentStatus,
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

    private void updatePayment() {

        String amountText =
                edtPaymentAmount
                        .getText()
                        .toString()
                        .trim();

        if (amountText.isEmpty()) {

            edtPaymentAmount.setError(
                    "Enter payment amount"
            );

            edtPaymentAmount.requestFocus();

            return;
        }

        double amount;

        try {

            amount =
                    Double.parseDouble(
                            amountText
                    );

        } catch (NumberFormatException e) {

            edtPaymentAmount.setError(
                    "Enter a valid amount"
            );

            return;
        }

        if (amount < 0) {

            edtPaymentAmount.setError(
                    "Amount cannot be negative"
            );

            return;
        }

        String method =
                spinnerPaymentMethod
                        .getSelectedItem()
                        .toString();

        String status =
                spinnerPaymentStatus
                        .getSelectedItem()
                        .toString();

        Intent result =
                new Intent();

        result.putExtra(
                "amount",
                amount
        );

        result.putExtra(
                "method",
                method
        );

        result.putExtra(
                "status",
                status
        );

        setResult(
                RESULT_OK,
                result
        );

        finish();
    }
}