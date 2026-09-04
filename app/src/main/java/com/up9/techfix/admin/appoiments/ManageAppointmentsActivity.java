package com.up9.techfix.admin.appoiments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ManageAppointmentsActivity
        extends AppCompatActivity
        implements AppointmentAdapter.OnAppointmentActionListener {

    private static final String TAG =
            "ManageAppointmentsActivity";

    // ============================================================
    // VIEWS
    // ============================================================

    private RecyclerView recyclerAppointments;
    private Spinner spinnerAppointmentFilter;

    // ============================================================
    // ADAPTER
    // ============================================================

    private AppointmentAdapter appointmentAdapter;

    // ============================================================
    // DATABASE
    // ============================================================

    private DatabaseHelper databaseHelper;

    // ============================================================
    // DATA
    // ============================================================

    private final List<RepairAppointment> appointmentList =
            new ArrayList<>();

    private final List<RepairAppointment> filteredList =
            new ArrayList<>();

    // ============================================================
    // FILTERS
    // ============================================================

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

    // ============================================================
    // MANAGE APPOINTMENT RESULT
    // ============================================================

    private final ActivityResultLauncher<Intent>
            manageAppointmentLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() != RESULT_OK) {
                            return;
                        }

                        Intent data = result.getData();

                        if (data == null) {
                            loadAppointments();
                            return;
                        }

                        int repairId =
                                data.getIntExtra(
                                        "repairId",
                                        -1
                                );

                        String status =
                                data.getStringExtra(
                                        "status"
                                );

                        double price =
                                data.getDoubleExtra(
                                        "price",
                                        -1
                                );

                        // ------------------------------------------------
                        // Update database
                        // ------------------------------------------------

                        if (repairId != -1) {

                            if (status != null &&
                                    !status.trim().isEmpty()) {

                                databaseHelper.updateRepairStatus(
                                        repairId,
                                        status
                                );
                            }

                            if (price >= 0) {

                                databaseHelper.updateRepairFinalPrice(
                                        repairId,
                                        price
                                );
                            }
                        }

                        // ------------------------------------------------
                        // Reload real database data
                        // ------------------------------------------------

                        loadAppointments();
                    }
            );

    // ============================================================
    // ON CREATE
    // ============================================================

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_manage_appointments
        );

        // --------------------------------------------------------
        // Database
        // --------------------------------------------------------

        databaseHelper =
                new DatabaseHelper(this);

        // --------------------------------------------------------
        // Views
        // --------------------------------------------------------

        initializeViews();

        // --------------------------------------------------------
        // RecyclerView
        // --------------------------------------------------------

        setupRecyclerView();

        // --------------------------------------------------------
        // Filter
        // --------------------------------------------------------

        setupFilter();

        // --------------------------------------------------------
        // Adapter
        // --------------------------------------------------------

        appointmentAdapter =
                new AppointmentAdapter(
                        filteredList,
                        this
                );

        recyclerAppointments.setAdapter(
                appointmentAdapter
        );

        // --------------------------------------------------------
        // Load REAL repairs
        // --------------------------------------------------------

        loadAppointments();
    }

    // ============================================================
    // INITIALIZE VIEWS
    // ============================================================

    private void initializeViews() {

        recyclerAppointments =
                findViewById(
                        R.id.recyclerAppointments
                );

        spinnerAppointmentFilter =
                findViewById(
                        R.id.spinnerAppointmentFilter
                );
    }

    // ============================================================
    // SETUP RECYCLER VIEW
    // ============================================================

    private void setupRecyclerView() {

        recyclerAppointments.setLayoutManager(
                new LinearLayoutManager(this)
        );
    }

    // ============================================================
    // SETUP FILTER
    // ============================================================

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
                        new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(
                                    AdapterView<?> parent,
                                    android.view.View view,
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

    // ============================================================
    // LOAD REAL APPOINTMENTS / REPAIRS
    // ============================================================

    private void loadAppointments() {

        appointmentList.clear();

        Cursor cursor = null;

        try {

            /*
             * DatabaseHelper.getAllRepairs() returns a Cursor.
             *
             * The query currently provides:
             *
             * repair_id
             * customer_id
             * customer_name
             * customer_email
             * customer_phone
             * category_name
             * device_model
             * service_name
             * service_price
             * problem_description
             * branch_name
             * branch_address
             * assigned_technician_id
             * technician_name
             * status
             * repair_date
             * final_price
             */

            cursor =
                    databaseHelper.getAllRepairs();

            if (cursor == null) {

                Toast.makeText(
                        this,
                        "Unable to load repairs.",
                        Toast.LENGTH_SHORT
                ).show();

                applyFilter();
                return;
            }

            if (!cursor.moveToFirst()) {

                applyFilter();
                return;
            }

            do {

                // ====================================================
                // REPAIR ID
                // ====================================================

                int repairId =
                        getInt(
                                cursor,
                                "repair_id"
                        );

                // ====================================================
                // CUSTOMER
                // ====================================================

                int customerId =
                        getInt(
                                cursor,
                                "customer_id"
                        );

                String customerName =
                        getString(
                                cursor,
                                "customer_name"
                        );

                String customerEmail =
                        getString(
                                cursor,
                                "customer_email"
                        );

                String customerPhone =
                        getString(
                                cursor,
                                "customer_phone"
                        );

                if (customerName.isEmpty()) {
                    customerName = "Unknown Customer";
                }

                if (customerEmail.isEmpty()) {
                    customerEmail = "Not available";
                }

                if (customerPhone.isEmpty()) {
                    customerPhone = "Not available";
                }

                // ====================================================
                // CATEGORY
                // ====================================================

                String categoryName =
                        getString(
                                cursor,
                                "category_name"
                        );

                if (categoryName.isEmpty()) {
                    categoryName = "Not available";
                }

                // ====================================================
                // DEVICE
                // ====================================================

                String deviceModel =
                        getString(
                                cursor,
                                "device_model"
                        );

                if (deviceModel.isEmpty()) {
                    deviceModel = "Unknown Device";
                }

                // ====================================================
                // SERVICE
                // ====================================================

                String serviceName =
                        getString(
                                cursor,
                                "service_name"
                        );

                if (serviceName.isEmpty()) {
                    serviceName = "Repair Service";
                }

                // ====================================================
                // SERVICE PRICE
                // ====================================================

                double servicePrice =
                        getDouble(
                                cursor,
                                "service_price",
                                0.0
                        );

                // ====================================================
                // PROBLEM DESCRIPTION
                // ====================================================

                String problemDescription =
                        getString(
                                cursor,
                                "problem_description"
                        );

                if (problemDescription.isEmpty()) {
                    problemDescription = "Not provided";
                }

                // ====================================================
                // BRANCH
                // ====================================================

                String branchName =
                        getString(
                                cursor,
                                "branch_name"
                        );

                String branchAddress =
                        getString(
                                cursor,
                                "branch_address"
                        );

                if (branchName.isEmpty()) {
                    branchName = "Not Assigned";
                }

                if (branchAddress.isEmpty()) {
                    branchAddress = "Not available";
                }

                // ====================================================
                // TECHNICIAN
                // ====================================================

                int technicianId =
                        getInt(
                                cursor,
                                "assigned_technician_id"
                        );

                String technicianName =
                        getString(
                                cursor,
                                "technician_name"
                        );

                if (technicianName.isEmpty()) {
                    technicianName = "Not Assigned";
                }

                // ====================================================
                // STATUS
                // ====================================================

                String status =
                        getString(
                                cursor,
                                "status"
                        );

                if (status.isEmpty()) {
                    status = "Pending";
                }

                /*
                 * Customer side may use:
                 *
                 * Ready for Collection
                 *
                 * Admin side uses:
                 *
                 * Ready for Pickup
                 */

                if ("Ready for Collection"
                        .equalsIgnoreCase(status)) {

                    status = "Ready for Pickup";
                }

                // ====================================================
                // REPAIR DATE
                // ====================================================

                String repairDate =
                        getString(
                                cursor,
                                "repair_date"
                        );

                if (repairDate.isEmpty()) {
                    repairDate = "Not Scheduled";
                }

                // ====================================================
                // FINAL PRICE
                // ====================================================

                double finalPrice =
                        getDouble(
                                cursor,
                                "final_price",
                                0.0
                        );

                /*
                 * If the repair does not have a final price yet,
                 * use the service price as the displayed estimate.
                 */

                if (finalPrice <= 0) {
                    finalPrice = servicePrice;
                }

                // ====================================================
                // APPOINTMENT TIME
                // ====================================================

                /*
                 * Your current repairs table stores repair_date
                 * but does not have a separate appointment_time
                 * column.
                 */

                String appointmentTime = "—";

                // ====================================================
                // CREATE REPAIR APPOINTMENT
                // ====================================================

                RepairAppointment appointment =
                        new RepairAppointment(
                                repairId,
                                customerId,
                                customerName,
                                customerEmail,
                                customerPhone,
                                categoryName,
                                deviceModel,
                                serviceName,
                                problemDescription,
                                branchName,
                                branchAddress,
                                technicianId,
                                technicianName,
                                status,
                                repairDate,
                                servicePrice,
                                finalPrice
                        );

                /*
                 * appointmentTime is not currently stored in
                 * DatabaseHelper.getAllRepairs(), so set the
                 * display value separately.
                 */

                appointment.setAppointmentTime(
                        appointmentTime
                );

                appointmentList.add(
                        appointment
                );

            } while (cursor.moveToNext());

        } catch (Exception e) {

            Log.e(
                    TAG,
                    "Error loading repairs",
                    e
            );

            Toast.makeText(
                    this,
                    "Error loading repairs.",
                    Toast.LENGTH_LONG
            ).show();

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        applyFilter();
    }

    // ============================================================
    // APPLY FILTER
    // ============================================================

    private void applyFilter() {

        filteredList.clear();

        String selectedFilter = "All";

        if (spinnerAppointmentFilter != null &&
                spinnerAppointmentFilter.getSelectedItem() != null) {

            selectedFilter =
                    spinnerAppointmentFilter
                            .getSelectedItem()
                            .toString();
        }

        for (RepairAppointment appointment
                : appointmentList) {

            if ("All".equalsIgnoreCase(
                    selectedFilter
            )) {

                filteredList.add(
                        appointment
                );

                continue;
            }

            String appointmentStatus =
                    appointment.getStatus();

            if (appointmentStatus == null) {
                continue;
            }

            /*
             * Treat both names as the same status.
             */

            if ("Ready for Pickup"
                    .equalsIgnoreCase(selectedFilter) &&
                    "Ready for Collection"
                            .equalsIgnoreCase(
                                    appointmentStatus
                            )) {

                filteredList.add(
                        appointment
                );

                continue;
            }

            if (appointmentStatus.equalsIgnoreCase(
                    selectedFilter
            )) {

                filteredList.add(
                        appointment
                );
            }
        }

        if (appointmentAdapter != null) {

            appointmentAdapter.notifyDataSetChanged();
        }
    }

    // ============================================================
    // MANAGE APPOINTMENT
    // ============================================================

    @Override
    public void onManage(
            RepairAppointment appointment
    ) {

        if (appointment == null) {
            return;
        }

        Intent intent =
                new Intent(
                        this,
                        ManageAppointmentActivity.class
                );

        // --------------------------------------------------------
        // Repair ID
        // --------------------------------------------------------

        intent.putExtra(
                "repairId",
                appointment.getRepairId()
        );

        // --------------------------------------------------------
        // Customer
        // --------------------------------------------------------

        intent.putExtra(
                "customerId",
                appointment.getCustomerId()
        );

        intent.putExtra(
                "customerName",
                appointment.getCustomerName()
        );

        intent.putExtra(
                "customerEmail",
                appointment.getCustomerEmail()
        );

        intent.putExtra(
                "customerPhone",
                appointment.getCustomerPhone()
        );

        // --------------------------------------------------------
        // Category
        // --------------------------------------------------------

        intent.putExtra(
                "category",
                appointment.getCategoryName()
        );

        // --------------------------------------------------------
        // Device
        // --------------------------------------------------------

        intent.putExtra(
                "device",
                appointment.getDevice()
        );

        // --------------------------------------------------------
        // Service
        // --------------------------------------------------------

        intent.putExtra(
                "service",
                appointment.getService()
        );

        // --------------------------------------------------------
        // Problem
        // --------------------------------------------------------

        intent.putExtra(
                "problemDescription",
                appointment.getProblemDescription()
        );

        // --------------------------------------------------------
        // Branch
        // --------------------------------------------------------

        intent.putExtra(
                "branch",
                appointment.getBranch()
        );

        intent.putExtra(
                "branchAddress",
                appointment.getBranchAddress()
        );

        // --------------------------------------------------------
        // Technician
        // --------------------------------------------------------

        intent.putExtra(
                "technicianId",
                appointment.getTechnicianId()
        );

        intent.putExtra(
                "technician",
                appointment.getTechnician()
        );

        // --------------------------------------------------------
        // Date
        // --------------------------------------------------------

        intent.putExtra(
                "repairDate",
                appointment.getRepairDate()
        );

        // --------------------------------------------------------
        // Time
        // --------------------------------------------------------

        intent.putExtra(
                "appointmentTime",
                appointment.getAppointmentTime()
        );

        // --------------------------------------------------------
        // Status
        // --------------------------------------------------------

        intent.putExtra(
                "status",
                appointment.getStatus()
        );

        // --------------------------------------------------------
        // Prices
        // --------------------------------------------------------

        intent.putExtra(
                "servicePrice",
                appointment.getServicePrice()
        );

        intent.putExtra(
                "price",
                appointment.getFinalPrice()
        );

        intent.putExtra(
                "finalPrice",
                appointment.getFinalPrice()
        );

        // --------------------------------------------------------
        // Open manage screen
        // --------------------------------------------------------

        manageAppointmentLauncher.launch(
                intent
        );
    }

    // ============================================================
    // SAFE CURSOR STRING
    // ============================================================

    private String getString(
            Cursor cursor,
            String columnName
    ) {

        int index =
                cursor.getColumnIndex(
                        columnName
                );

        if (index == -1 ||
                cursor.isNull(index)) {

            return "";
        }

        return cursor.getString(index);
    }

    // ============================================================
    // SAFE CURSOR INTEGER
    // ============================================================

    private int getInt(
            Cursor cursor,
            String columnName
    ) {

        int index =
                cursor.getColumnIndex(
                        columnName
                );

        if (index == -1 ||
                cursor.isNull(index)) {

            return -1;
        }

        return cursor.getInt(index);
    }

    // ============================================================
    // SAFE CURSOR DOUBLE
    // ============================================================

    private double getDouble(
            Cursor cursor,
            String columnName,
            double defaultValue
    ) {

        int index =
                cursor.getColumnIndex(
                        columnName
                );

        if (index == -1 ||
                cursor.isNull(index)) {

            return defaultValue;
        }

        return cursor.getDouble(index);
    }

    // ============================================================
    // REFRESH WHEN ACTIVITY BECOMES VISIBLE
    // ============================================================

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {
            loadAppointments();
        }
    }

    // ============================================================
    // CLOSE DATABASE
    // ============================================================

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}
