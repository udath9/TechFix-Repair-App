package com.up9.techfix.admin.payments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;
import com.up9.techfix.data.Payment;

import java.util.ArrayList;
import java.util.List;

public class ManagePaymentsActivity
        extends AppCompatActivity
        implements PaymentAdapter.OnPaymentActionListener {

    private RecyclerView recyclerPayments;
    private Spinner spinnerPaymentFilter;

    private DatabaseHelper databaseHelper;

    private PaymentAdapter paymentAdapter;

    private final List<Payment> paymentList =
            new ArrayList<>();

    private final List<Payment> filteredList =
            new ArrayList<>();

    private final String[] filters = {
            "All",
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
                R.layout.activity_manage_payments
        );

        databaseHelper =
                new DatabaseHelper(this);

        initializeViews();

        setupRecyclerView();

        setupFilter();

        setupAdapter();

        loadPayments();
    }
    private void initializeViews() {

        recyclerPayments =
                findViewById(
                        R.id.recyclerPayments
                );

        spinnerPaymentFilter =
                findViewById(
                        R.id.spinnerPaymentFilter
                );
    }

    private void setupRecyclerView() {

        recyclerPayments.setLayoutManager(
                new LinearLayoutManager(this)
        );
    }

    private void setupAdapter() {

        paymentAdapter =
                new PaymentAdapter(
                        filteredList,
                        this
                );

        recyclerPayments.setAdapter(
                paymentAdapter
        );
    }

    private void setupFilter() {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        filters
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerPaymentFilter.setAdapter(
                adapter
        );

        spinnerPaymentFilter.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        applyFilter();
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent
                    ) {

                        // Nothing required
                    }
                }
        );
    }

    private void loadPayments() {

        paymentList.clear();

        SQLiteDatabase db =
                databaseHelper.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = db.rawQuery(
                    "SELECT " +

                            "p.id AS payment_id, " +
                            "p.repair_id AS repair_id, " +
                            "p.amount AS payment_amount, " +
                            "p.payment_date AS payment_date, " +
                            "p.status AS payment_status, " +

                            "cu.full_name AS customer_name, " +

                            "r.device_model AS device_model, " +

                            "s.name AS service_name, " +

                            "b.name AS branch_name " +

                            "FROM " +
                            DatabaseHelper.TABLE_PAYMENTS +
                            " p " +

                            "INNER JOIN " +
                            DatabaseHelper.TABLE_REPAIRS +
                            " r " +

                            "ON p.repair_id = r.id " +

                            "LEFT JOIN " +
                            DatabaseHelper.TABLE_CUSTOMERS +
                            " cu " +

                            "ON r.customer_id = cu.id " +

                            "LEFT JOIN " +
                            DatabaseHelper.TABLE_SERVICES +
                            " s " +

                            "ON r.service_id = s.id " +

                            "LEFT JOIN " +
                            DatabaseHelper.TABLE_BRANCHES +
                            " b " +

                            "ON r.branch_id = b.id " +

                            "ORDER BY p.id DESC",

                    null
            );

            if (cursor.moveToFirst()) {

                do {

                    int paymentId =
                            cursor.getInt(
                                    cursor.getColumnIndexOrThrow(
                                            "payment_id"
                                    )
                            );

                    int repairId =
                            cursor.getInt(
                                    cursor.getColumnIndexOrThrow(
                                            "repair_id"
                                    )
                            );

                    double amount =
                            cursor.getDouble(
                                    cursor.getColumnIndexOrThrow(
                                            "payment_amount"
                                    )
                            );

                    String paymentDate =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "payment_date"
                                    )
                            );

                    String status =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "payment_status"
                                    )
                            );

                    String customerName =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "customer_name"
                                    )
                            );

                    String deviceModel =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "device_model"
                                    )
                            );

                    String serviceName =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "service_name"
                                    )
                            );

                    String branchName =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "branch_name"
                                    )
                            );

                    paymentList.add(
                            new Payment(
                                    paymentId,
                                    repairId,
                                    safeText(customerName),
                                    safeText(deviceModel),
                                    safeText(serviceName),
                                    safeText(branchName),
                                    amount,
                                    paymentDate,
                                    safeText(status)
                            )
                    );

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Error loading payments: " +
                            e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        applyFilter();
    }

    private void applyFilter() {

        if (paymentAdapter == null) {
            return;
        }

        filteredList.clear();

        String selectedFilter;

        if (spinnerPaymentFilter.getSelectedItem() == null) {

            selectedFilter = "All";

        } else {

            selectedFilter =
                    spinnerPaymentFilter
                            .getSelectedItem()
                            .toString();
        }

        for (Payment payment :
                paymentList) {

            String paymentStatus =
                    payment.getStatus();

            if (selectedFilter.equalsIgnoreCase(
                    "All"
            )) {

                filteredList.add(payment);

            } else if (
                    paymentStatus != null &&
                            paymentStatus.equalsIgnoreCase(
                                    selectedFilter
                            )
            ) {

                filteredList.add(payment);
            }
        }

        paymentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onManage(
            Payment payment
    ) {

        ManagePaymentActivity.start(
                this,
                payment
        );
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {

            loadPayments();
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

    // =========================================================
    // CLOSE DATABASE
    // =========================================================

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {

            databaseHelper.close();
        }

        super.onDestroy();
    }
}