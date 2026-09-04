 package com.up9.techfix.admin.payments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;
import com.up9.techfix.data.Payment;

public class ManagePaymentActivity extends AppCompatActivity {

    private static final String EXTRA_PAYMENT_ID = "paymentId";
    private static final String EXTRA_REPAIR_ID = "repairId";
    private static final String EXTRA_AMOUNT = "amount";
    private static final String EXTRA_PAYMENT_DATE = "paymentDate";
    private static final String EXTRA_STATUS = "status";

    private TextView txtPaymentCustomerDetails;
    private TextView txtPaymentAppointmentDetails;

    private EditText edtPaymentAmount;
    private EditText edtPaymentDate;

    private Spinner spinnerPaymentStatus;

    private Button btnUpdatePayment;

    private DatabaseHelper databaseHelper;

    private int paymentId = -1;
    private int repairId = -1;

    private final String[] paymentStatuses = {
            "Pending",
            "Paid",
            "Failed",
            "Refunded"
    };

    public static void start(
            Context context,
            Payment payment
    ) {

        Intent intent =
                new Intent(
                        context,
                        ManagePaymentActivity.class
                );

        intent.putExtra(
                EXTRA_PAYMENT_ID,
                payment.getId()
        );

        intent.putExtra(
                EXTRA_REPAIR_ID,
                payment.getRepairId()
        );

        intent.putExtra(
                EXTRA_AMOUNT,
                payment.getAmount()
        );

        intent.putExtra(
                EXTRA_PAYMENT_DATE,
                payment.getPaymentDate()
        );

        intent.putExtra(
                EXTRA_STATUS,
                payment.getStatus()
        );

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_manage_payment
        );

        databaseHelper =
                new DatabaseHelper(this);

        initializeViews();

        setupStatusSpinner();

        loadPayment();

        btnUpdatePayment.setOnClickListener(
                v -> updatePayment()
        );
    }

    private void initializeViews() {

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

        edtPaymentDate =
                findViewById(
                        R.id.edtPaymentDate
                );

        spinnerPaymentStatus =
                findViewById(
                        R.id.spinnerPaymentStatus
                );

        btnUpdatePayment =
                findViewById(
                        R.id.btnUpdatePayment
                );
    }

    private void setupStatusSpinner() {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        paymentStatuses
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerPaymentStatus.setAdapter(
                adapter
        );
    }

    private void loadPayment() {

        Intent intent =
                getIntent();

        paymentId =
                intent.getIntExtra(
                        EXTRA_PAYMENT_ID,
                        -1
                );

        repairId =
                intent.getIntExtra(
                        EXTRA_REPAIR_ID,
                        -1
                );

        double amount =
                intent.getDoubleExtra(
                        EXTRA_AMOUNT,
                        0.0
                );

        String paymentDate =
                intent.getStringExtra(
                        EXTRA_PAYMENT_DATE
                );

        String status =
                intent.getStringExtra(
                        EXTRA_STATUS
                );

        loadRepairDetails();

        txtPaymentAppointmentDetails.setText(
                "Repair: #" +
                        repairId +
                        "\n\nPayment: #" +
                        paymentId
        );

        edtPaymentAmount.setText(
                String.valueOf(amount)
        );

        edtPaymentDate.setText(
                safeText(paymentDate)
        );

        setSpinnerValue(
                spinnerPaymentStatus,
                status
        );
    }

    private void loadRepairDetails() {

        if (repairId <= 0) {

            txtPaymentCustomerDetails.setText(
                    "Repair information not available."
            );

            return;
        }

        Cursor cursor = null;

        try {

            cursor =
                    databaseHelper.getRepairById(
                            repairId
                    );

            if (cursor != null &&
                    cursor.moveToFirst()) {

                String customerName =
                        getCursorString(
                                cursor,
                                "customer_name"
                        );

                String deviceModel =
                        getCursorString(
                                cursor,
                                "device_model"
                        );

                String serviceName =
                        getCursorString(
                                cursor,
                                "service_name"
                        );

                String branchName =
                        getCursorString(
                                cursor,
                                "branch_name"
                        );

                String details =
                        "Customer: " +
                                safeText(
                                        customerName
                                ) +

                                "\n\nDevice: " +
                                safeText(
                                        deviceModel
                                ) +

                                "\n\nService: " +
                                safeText(
                                        serviceName
                                ) +

                                "\n\nBranch: " +
                                safeText(
                                        branchName
                                );

                txtPaymentCustomerDetails.setText(
                        details
                );

            } else {

                txtPaymentCustomerDetails.setText(
                        "Repair information not found."
                );
            }

        } catch (Exception e) {

            txtPaymentCustomerDetails.setText(
                    "Unable to load repair information."
            );

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String getCursorString(
            Cursor cursor,
            String columnName
    ) {

        int index =
                cursor.getColumnIndex(
                        columnName
                );

        if (index < 0 ||
                cursor.isNull(index)) {

            return "";
        }

        return cursor.getString(index);
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

            Object item =
                    spinner.getItemAtPosition(i);

            if (item != null &&
                    item.toString()
                            .equalsIgnoreCase(
                                    value
                            )) {

                spinner.setSelection(i);

                return;
            }
        }
    }

    private void updatePayment() {

        if (paymentId <= 0) {

            Toast.makeText(
                    this,
                    "Invalid payment ID.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

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

            edtPaymentAmount.requestFocus();

            return;
        }

        if (amount < 0) {

            edtPaymentAmount.setError(
                    "Amount cannot be negative"
            );

            edtPaymentAmount.requestFocus();

            return;
        }

        String paymentDate =
                edtPaymentDate
                        .getText()
                        .toString()
                        .trim();

        if (paymentDate.isEmpty()) {

            edtPaymentDate.setError(
                    "Enter payment date"
            );

            edtPaymentDate.requestFocus();

            return;
        }

        String status =
                spinnerPaymentStatus
                        .getSelectedItem()
                        .toString();

        SQLiteDatabase db =
                databaseHelper
                        .getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                DatabaseHelper.COL_PAYMENT_AMOUNT,
                amount
        );

        values.put(
                DatabaseHelper.COL_PAYMENT_DATE,
                paymentDate
        );

        values.put(
                DatabaseHelper.COL_PAYMENT_STATUS,
                status
        );

        try {

            int updated =
                    db.update(
                            DatabaseHelper.TABLE_PAYMENTS,
                            values,
                            DatabaseHelper.COL_PAYMENT_ID +
                                    " = ?",
                            new String[]{
                                    String.valueOf(
                                            paymentId
                                    )
                            }
                    );

            if (updated > 0) {

                Toast.makeText(
                        this,
                        "Payment updated successfully.",
                        Toast.LENGTH_SHORT
                ).show();

                setResult(
                        RESULT_OK
                );

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Payment record not found.",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Unable to update payment.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private String safeText(
            String value
    ) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not available";
        }

        return value;
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}
