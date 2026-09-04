package com.up9.techfix.admin.appoiments;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;
import com.up9.techfix.data.Branch;
import com.up9.techfix.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ManageAppointmentActivity
        extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    private TextView txtManageCustomer;
    private TextView txtManageDevice;
    private TextView txtManageService;

    private Spinner spinnerManageBranch;
    private Spinner spinnerManageTechnician;
    private Spinner spinnerManageStatus;

    private EditText edtManagePrice;

    private Button btnUpdateAppointment;

    private int repairId = -1;

    private String customerName;
    private String deviceModel;
    private String serviceName;

    private String currentBranchName;
    private int currentTechnicianId = -1;
    private String currentTechnicianName;

    private String currentStatus;
    private double currentPrice = 0.0;
    private final List<Branch> branchList =
            new ArrayList<>();

    private final List<Integer> branchIds =
            new ArrayList<>();

    private final List<String> branchNames =
            new ArrayList<>();
    private final List<Integer> technicianIds =
            new ArrayList<>();

    private final List<String> technicianNames =
            new ArrayList<>();
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

        databaseHelper =
                new DatabaseHelper(this);

        initializeViews();

        readAppointmentData();

        if (repairId <= 0) {

            Toast.makeText(
                    this,
                    "Repair appointment not found.",
                    Toast.LENGTH_LONG
            ).show();

            finish();

            return;
        }

        displayAppointment();

        loadBranches();

        loadTechnicians();

        setupStatusSpinner();

        btnUpdateAppointment.setOnClickListener(
                v -> updateAppointment()
        );
    }
    private void initializeViews() {

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
    }

    private void readAppointmentData() {

        repairId =
                getIntent().getIntExtra(
                        "repairId",
                        -1
                );

        customerName =
                getIntent().getStringExtra(
                        "customerName"
                );

        /*
         * Support both old and new extra names.
         */

        deviceModel =
                getIntent().getStringExtra(
                        "deviceModel"
                );

        if (deviceModel == null) {

            deviceModel =
                    getIntent().getStringExtra(
                            "device"
                    );
        }

        serviceName =
                getIntent().getStringExtra(
                        "serviceName"
                );

        if (serviceName == null) {

            serviceName =
                    getIntent().getStringExtra(
                            "service"
                    );
        }

        currentBranchName =
                getIntent().getStringExtra(
                        "branchName"
                );

        if (currentBranchName == null) {

            currentBranchName =
                    getIntent().getStringExtra(
                            "branch"
                    );
        }

        currentTechnicianId =
                getIntent().getIntExtra(
                        "technicianId",
                        -1
                );

        currentTechnicianName =
                getIntent().getStringExtra(
                        "technicianName"
                );

        if (currentTechnicianName == null) {

            currentTechnicianName =
                    getIntent().getStringExtra(
                            "technician"
                    );
        }

        currentStatus =
                getIntent().getStringExtra(
                        "status"
                );

        currentPrice =
                getIntent().getDoubleExtra(
                        "finalPrice",
                        getIntent().getDoubleExtra(
                                "price",
                                0.0
                        )
                );
        customerName =
                safeTextOrDefault(
                        customerName,
                        "Unknown Customer"
                );

        deviceModel =
                safeTextOrDefault(
                        deviceModel,
                        "Unknown Device"
                );

        serviceName =
                safeTextOrDefault(
                        serviceName,
                        "Repair Service"
                );

        currentBranchName =
                safeTextOrDefault(
                        currentBranchName,
                        "Not Assigned"
                );

        currentTechnicianName =
                safeTextOrDefault(
                        currentTechnicianName,
                        "Not Assigned"
                );

        currentStatus =
                safeTextOrDefault(
                        currentStatus,
                        "Pending"
                );

        if ("Ready for Collection"
                .equalsIgnoreCase(currentStatus)) {

            currentStatus =
                    "Ready for Pickup";
        }
    }
    private void displayAppointment() {

        txtManageCustomer.setText(
                String.format(
                        Locale.getDefault(),
                        "Customer: %s",
                        customerName
                )
        );

        txtManageDevice.setText(
                String.format(
                        Locale.getDefault(),
                        "Device: %s",
                        deviceModel
                )
        );

        txtManageService.setText(
                String.format(
                        Locale.getDefault(),
                        "Service: %s",
                        serviceName
                )
        );

        edtManagePrice.setText(
                String.format(
                        Locale.getDefault(),
                        "%.2f",
                        currentPrice
                )
        );
    }

    private void loadBranches() {

        branchList.clear();
        branchIds.clear();
        branchNames.clear();

        List<Branch> databaseBranches =
                databaseHelper.getAllBranches();

        if (databaseBranches != null) {

            branchList.addAll(
                    databaseBranches
            );
        }

        for (Branch branch :
                branchList) {

            if (branch == null) {
                continue;
            }

            branchIds.add(
                    branch.getId()
            );

            branchNames.add(
                    safeTextOrDefault(
                            branch.getName(),
                            "Unknown Branch"
                    )
            );
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        branchNames
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerManageBranch.setAdapter(
                adapter
        );

        selectBranch(
                currentBranchName
        );
    }
    private void selectBranch(
            String branchName
    ) {

        if (branchName == null) {
            return;
        }

        for (int i = 0;
             i < branchNames.size();
             i++) {

            if (branchNames.get(i)
                    .equalsIgnoreCase(
                            branchName
                    )) {

                spinnerManageBranch
                        .setSelection(i);

                return;
            }
        }
    }

    private void loadTechnicians() {

        technicianIds.clear();
        technicianNames.clear();

        Cursor cursor = null;

        try {

            cursor =
                    databaseHelper.getAllTechnicians();

            if (cursor != null &&
                    cursor.moveToFirst()) {

                do {

                    int id =
                            cursor.getInt(
                                    cursor.getColumnIndexOrThrow(
                                            "id"
                                    )
                            );

                    String name =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "full_name"
                                    )
                            );

                    if (name == null ||
                            name.trim().isEmpty()) {

                        name =
                                "Technician #" + id;
                    }

                    technicianIds.add(id);

                    technicianNames.add(name);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Unable to load technicians.",
                    Toast.LENGTH_LONG
            ).show();

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        technicianIds.add(0, -1);

        technicianNames.add(
                0,
                "Not Assigned"
        );

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        technicianNames
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerManageTechnician.setAdapter(
                adapter
        );

        selectTechnician(
                currentTechnicianId
        );
    }

    private void selectTechnician(
            int technicianId
    ) {

        if (technicianId <= 0) {

            spinnerManageTechnician
                    .setSelection(0);

            return;
        }

        for (int i = 0;
             i < technicianIds.size();
             i++) {

            if (technicianIds.get(i)
                    == technicianId) {

                spinnerManageTechnician
                        .setSelection(i);

                return;
            }
        }
    }

    private void setupStatusSpinner() {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        statuses
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerManageStatus.setAdapter(
                adapter
        );

        selectStatus(
                currentStatus
        );
    }

    private void selectStatus(
            String status
    ) {

        if (status == null) {
            return;
        }

        String normalizedStatus =
                status.trim();

        if ("Ready for Collection"
                .equalsIgnoreCase(
                        normalizedStatus
                )) {

            normalizedStatus =
                    "Ready for Pickup";
        }

        for (int i = 0;
             i < statuses.length;
             i++) {

            if (statuses[i]
                    .equalsIgnoreCase(
                            normalizedStatus
                    )) {

                spinnerManageStatus
                        .setSelection(i);

                return;
            }
        }
    }

    private void updateAppointment() {

        int branchPosition =
                spinnerManageBranch
                        .getSelectedItemPosition();

        if (branchPosition < 0 ||
                branchPosition >= branchIds.size()) {

            Toast.makeText(
                    this,
                    "Please select a branch.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int branchId =
                branchIds.get(
                        branchPosition
                );

        int technicianPosition =
                spinnerManageTechnician
                        .getSelectedItemPosition();

        if (technicianPosition < 0 ||
                technicianPosition >= technicianIds.size()) {

            Toast.makeText(
                    this,
                    "Please select a technician.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int technicianId =
                technicianIds.get(
                        technicianPosition
                );

        String technicianName =
                technicianNames.get(
                        technicianPosition
                );

        if (technicianId <= 0) {

            technicianName =
                    "";
        }

        Object selectedStatus =
                spinnerManageStatus
                        .getSelectedItem();

        if (selectedStatus == null) {

            Toast.makeText(
                    this,
                    "Please select a status.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String status =
                selectedStatus
                        .toString()
                        .trim();
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

            edtManagePrice.requestFocus();

            return;
        }

        if (price < 0) {

            edtManagePrice.setError(
                    "Price cannot be negative"
            );

            edtManagePrice.requestFocus();

            return;
        }

        try {

            boolean updated =
                    databaseHelper.updateRepair(
                            repairId,
                            branchId,
                            technicianId,
                            technicianName,
                            status,
                            price
                    );

            if (updated) {

                Toast.makeText(
                        this,
                        "Appointment updated successfully.",
                        Toast.LENGTH_SHORT
                ).show();

                setResult(
                        RESULT_OK
                );

                finish();

            } else {

                Toast.makeText(
                        this,
                        "No appointment was updated.",
                        Toast.LENGTH_LONG
                ).show();
            }

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Update failed: " +
                            e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private String safeTextOrDefault(
            String value,
            String defaultValue
    ) {

        if (value == null ||
                value.trim().isEmpty()) {

            return defaultValue;
        }

        return value.trim();
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}