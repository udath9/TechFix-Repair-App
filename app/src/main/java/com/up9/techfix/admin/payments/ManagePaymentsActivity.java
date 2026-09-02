package com.up9.techfix.admin.payments;
import com.up9.techfix.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ManagePaymentsActivity
        extends AppCompatActivity
        implements PaymentAdapter.OnPaymentActionListener {

    private RecyclerView recyclerPayments;

    private Spinner spinnerPaymentFilter;

    private PaymentAdapter paymentAdapter;

    private List<Payment> paymentList;

    private List<Payment> filteredList;

    private int editingPosition = -1;

    private final String[] filters = {
            "All",
            "Pending",
            "Paid",
            "Failed",
            "Refunded"
    };

    private final ActivityResultLauncher<Intent>
            managePaymentLauncher =
            registerForActivityResult(
                    new ActivityResultContracts
                            .StartActivityForResult(),
                    result -> {

                        if (result.getResultCode()
                                != RESULT_OK) {

                            return;
                        }

                        Intent data =
                                result.getData();

                        if (data == null) {
                            return;
                        }

                        Payment payment =
                                paymentList.get(
                                        editingPosition
                                );

                        payment.setAmount(
                                data.getDoubleExtra(
                                        "amount",
                                        payment.getAmount()
                                )
                        );

                        payment.setPaymentMethod(
                                data.getStringExtra(
                                        "method"
                                )
                        );

                        payment.setPaymentStatus(
                                data.getStringExtra(
                                        "status"
                                )
                        );

                        applyFilter();

                        editingPosition = -1;
                    }
            );

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_manage_payments
        );

        recyclerPayments =
                findViewById(
                        R.id.recyclerPayments
                );

        spinnerPaymentFilter =
                findViewById(
                        R.id.spinnerPaymentFilter
                );

        recyclerPayments.setLayoutManager(
                new LinearLayoutManager(this)
        );

        paymentList =
                new ArrayList<>();

        filteredList =
                new ArrayList<>();

        loadSamplePayments();

        setupFilter();

        paymentAdapter =
                new PaymentAdapter(
                        filteredList,
                        this
                );

        recyclerPayments.setAdapter(
                paymentAdapter
        );

        applyFilter();
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

        spinnerPaymentFilter
                .setOnItemSelectedListener(
                        new android.widget.AdapterView
                                .OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(
                                    android.widget.AdapterView<?> parent,
                                    android.view.View view,
                                    int position,
                                    long id
                            ) {

                                applyFilter();
                            }

                            @Override
                            public void onNothingSelected(
                                    android.widget.AdapterView<?> parent
                            ) {

                            }
                        }
                );
    }

    private void applyFilter() {

        filteredList.clear();

        String selectedFilter =
                spinnerPaymentFilter
                        .getSelectedItem()
                        .toString();

        for (Payment payment :
                paymentList) {

            if (selectedFilter.equals("All")
                    || payment
                    .getPaymentStatus()
                    .equals(selectedFilter)) {

                filteredList.add(
                        payment
                );
            }
        }

        if (paymentAdapter != null) {

            paymentAdapter.notifyDataSetChanged();
        }
    }

    private void loadSamplePayments() {

        paymentList.add(
                new Payment(
                        1,
                        "P1001",
                        1001,
                        "Nimal Perera",
                        35000,
                        "Card",
                        "Paid",
                        "28/08/2026"
                )
        );

        paymentList.add(
                new Payment(
                        2,
                        "P1002",
                        1002,
                        "Saman Silva",
                        18000,
                        "Cash",
                        "Pending",
                        "28/08/2026"
                )
        );

        paymentList.add(
                new Payment(
                        3,
                        "P1003",
                        1003,
                        "Kamal Fernando",
                        8500,
                        "Online Payment",
                        "Paid",
                        "29/08/2026"
                )
        );

        paymentList.add(
                new Payment(
                        4,
                        "P1004",
                        1004,
                        "Tharindu Perera",
                        5000,
                        "Bank Transfer",
                        "Paid",
                        "29/08/2026"
                )
        );

        paymentList.add(
                new Payment(
                        5,
                        "P1005",
                        1005,
                        "Dilshan Silva",
                        12000,
                        "Card",
                        "Pending",
                        "30/08/2026"
                )
        );
    }

    @Override
    public void onManage(
            Payment payment
    ) {

        editingPosition =
                paymentList.indexOf(
                        payment
                );

        Intent intent =
                new Intent(
                        this,
                        ManagePaymentActivity.class
                );

        intent.putExtra(
                "customerName",
                payment.getCustomerName()
        );

        intent.putExtra(
                "appointmentId",
                payment.getAppointmentId()
        );

        intent.putExtra(
                "amount",
                payment.getAmount()
        );

        intent.putExtra(
                "method",
                payment.getPaymentMethod()
        );

        intent.putExtra(
                "status",
                payment.getPaymentStatus()
        );

        managePaymentLauncher.launch(
                intent
        );
    }
}