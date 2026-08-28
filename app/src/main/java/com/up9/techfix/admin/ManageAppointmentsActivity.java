package com.up9.techfix.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;

import java.util.ArrayList;
import java.util.List;

public class ManageAppointmentsActivity
        extends AppCompatActivity
        implements AppointmentAdapter.OnAppointmentActionListener {

    private RecyclerView recyclerAppointments;

    private Spinner spinnerAppointmentFilter;

    private AppointmentAdapter appointmentAdapter;

    private List<RepairAppointment> appointmentList;

    private List<RepairAppointment> filteredList;

    private int editingPosition = -1;

    private final String[] filters = {
            "All",
            "Pending",
            "Accepted",
            "Assigned",
            "Diagnosing",
            "Repairing",
            "Ready for Pickup",
            "Completed",
            "Cancelled"
    };

    private final ActivityResultLauncher<Intent>
            manageAppointmentLauncher =
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

                        RepairAppointment appointment =
                                appointmentList.get(
                                        editingPosition
                                );

                        appointment.setBranch(
                                data.getStringExtra(
                                        "branch"
                                )
                        );

                        appointment.setTechnician(
                                data.getStringExtra(
                                        "technician"
                                )
                        );

                        appointment.setStatus(
                                data.getStringExtra(
                                        "status"
                                )
                        );

                        appointment.setEstimatedPrice(
                                data.getDoubleExtra(
                                        "price",
                                        appointment
                                                .getEstimatedPrice()
                                )
                        );

                        appointmentAdapter
                                .notifyItemChanged(
                                        editingPosition
                                );

                        editingPosition = -1;

                        applyFilter();
                    }
            );

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_manage_appointments
        );

        recyclerAppointments =
                findViewById(
                        R.id.recyclerAppointments
                );

        spinnerAppointmentFilter =
                findViewById(
                        R.id.spinnerAppointmentFilter
                );

        recyclerAppointments.setLayoutManager(
                new LinearLayoutManager(this)
        );

        appointmentList =
                new ArrayList<>();

        filteredList =
                new ArrayList<>();

        loadSampleAppointments();

        setupFilter();

        appointmentAdapter =
                new AppointmentAdapter(
                        filteredList,
                        this
                );

        recyclerAppointments.setAdapter(
                appointmentAdapter
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

        spinnerAppointmentFilter.setAdapter(
                adapter
        );

        spinnerAppointmentFilter
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

        if (filteredList == null) {
            return;
        }

        filteredList.clear();

        String selectedFilter =
                spinnerAppointmentFilter
                        .getSelectedItem()
                        .toString();

        for (RepairAppointment appointment
                : appointmentList) {

            if (selectedFilter.equals("All")
                    || appointment
                    .getStatus()
                    .equals(selectedFilter)) {

                filteredList.add(
                        appointment
                );
            }
        }

        if (appointmentAdapter != null) {

            appointmentAdapter.notifyDataSetChanged();
        }
    }

    private void loadSampleAppointments() {

        appointmentList.add(
                new RepairAppointment(
                        1001,
                        "Nimal Perera",
                        "iPhone 13",
                        "Screen Replacement",
                        "Colombo",
                        "Kasun Perera",
                        "28/08/2026",
                        "10:30 AM",
                        "Pending",
                        35000
                )
        );

        appointmentList.add(
                new RepairAppointment(
                        1002,
                        "Saman Silva",
                        "Dell Inspiron 15",
                        "Laptop Repair",
                        "Colombo",
                        "Nimal Fernando",
                        "28/08/2026",
                        "11:00 AM",
                        "Repairing",
                        18000
                )
        );

        appointmentList.add(
                new RepairAppointment(
                        1003,
                        "Kamal Fernando",
                        "Samsung Galaxy A54",
                        "Battery Replacement",
                        "Galle",
                        "Amal Silva",
                        "29/08/2026",
                        "09:30 AM",
                        "Assigned",
                        8500
                )
        );

        appointmentList.add(
                new RepairAppointment(
                        1004,
                        "Tharindu Perera",
                        "HP Laptop",
                        "Windows Installation",
                        "Galle",
                        "Saman Perera",
                        "29/08/2026",
                        "01:00 PM",
                        "Completed",
                        5000
                )
        );

        appointmentList.add(
                new RepairAppointment(
                        1005,
                        "Dilshan Silva",
                        "iPad Pro",
                        "Charging Port Repair",
                        "Colombo",
                        "Kasun Perera",
                        "30/08/2026",
                        "02:00 PM",
                        "Diagnosing",
                        12000
                )
        );
    }

    @Override
    public void onManage(
            RepairAppointment appointment
    ) {

        editingPosition =
                appointmentList.indexOf(
                        appointment
                );

        Intent intent =
                new Intent(
                        this,
                        ManageAppointmentActivity.class
                );

        intent.putExtra(
                "customerName",
                appointment.getCustomerName()
        );

        intent.putExtra(
                "device",
                appointment.getDevice()
        );

        intent.putExtra(
                "service",
                appointment.getService()
        );

        intent.putExtra(
                "branch",
                appointment.getBranch()
        );

        intent.putExtra(
                "technician",
                appointment.getTechnician()
        );

        intent.putExtra(
                "status",
                appointment.getStatus()
        );

        intent.putExtra(
                "price",
                appointment.getEstimatedPrice()
        );

        manageAppointmentLauncher.launch(
                intent
        );
    }
}