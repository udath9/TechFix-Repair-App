package com.up9.techfix.admin.appoiments;

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

public class ManageAppointmentActivity extends AppCompatActivity {

    // ============================================================
    // DATABASE
    // ============================================================

    private DatabaseHelper databaseHelper;

    // ============================================================
    // VIEWS
    // ============================================================

    private TextView txtManageCustomer;
    private TextView txtManageDevice;
    private TextView txtManageService;

    private Spinner spinnerManageBranch;
    private Spinner spinnerManageTechnician;
    private Spinner spinnerManageStatus;

    private EditText edtManagePrice;

    private Button btnUpdateAppointment;

    // ============================================================
    // APPOINTMENT DATA
    // ============================================================

    private int repairId = -1;

    private String customerName;
    private String deviceModel;
    private String serviceName;

    private String currentBranchName;
    private int currentTechnicianId = -1;
    private String currentTechnicianName;

    private String currentStatus;
    private double currentPrice = 0.0;

    // ============================================================
    // BRANCH LIST
    // ============================================================

    private final List<Branch> branchList =
            new ArrayList<>();

    private final List<Integer> branchIds =
            new ArrayList<>();

    private final List<String> branchNames =
            new ArrayList<>();

    // ============================================================
    // TECHNICIAN LIST
    // ============================================================
    /*
     * Your current DatabaseHelper does not expose a
     * getAllTechnicians() method.
     *
     * Therefore we do NOT call a non-existing method.
     *
     * We use the technician information already attached
     * to the repair.
     *
     * The current technician is placed into the Spinner.
     *
     * When you later add getAllTechnicians() to DatabaseHelper,
     * this section can be expanded to show every technician.
     */

    private final List<Integer> technicianIds =
            new ArrayList<>();

    private final List<String> technicianNames =
            new ArrayList<>();

    // ============================================================
    // STATUS LIST
    // ============================================================

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

    // ============================================================
    // ON CREATE
    // ============================================================

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_manage_appointment
        );

        // --------------------------------------------------------
        // DATABASE
        // --------------------------------------------------------

        databaseHelper =
                new DatabaseHelper(this);

        // --------------------------------------------------------
        // INITIALIZE VIEWS
        // --------------------------------------------------------

        initializeViews();

        // --------------------------------------------------------
        // GET DATA FROM ManageAppointmentsActivity
        // --------------------------------------------------------

        readAppointmentData();

        // --------------------------------------------------------
        // VALIDATE REPAIR ID
        // --------------------------------------------------------

        if (repairId == -1) {

            Toast.makeText(
                    this,
                    "Repair appointment not found.",
                    Toast.LENGTH_LONG
            ).show();

            finish();

            return;
        }

        // --------------------------------------------------------
        // DISPLAY APPOINTMENT
        // --------------------------------------------------------

        displayAppointment();

        // --------------------------------------------------------
        // LOAD BRANCHES
        // --------------------------------------------------------

        loadBranches();

        // --------------------------------------------------------
        // LOAD TECHNICIAN
        // --------------------------------------------------------

        loadTechnician();

        // --------------------------------------------------------
        // LOAD STATUS
        // --------------------------------------------------------

        setupStatusSpinner();

        // --------------------------------------------------------
        // UPDATE BUTTON
        // --------------------------------------------------------

        btnUpdateAppointment.setOnClickListener(
                v -> updateAppointment()
        );
    }

    // ============================================================
    // INITIALIZE VIEWS
    // ============================================================

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

    // ============================================================
    // READ APPOINTMENT DATA
    // ============================================================

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

        deviceModel =
                getIntent().getStringExtra(
                        "deviceModel"
                );

        serviceName =
                getIntent().getStringExtra(
                        "serviceName"
                );

        currentBranchName =
                getIntent().getStringExtra(
                        "branchName"
                );

        currentTechnicianId =
                getIntent().getIntExtra(
                        "technicianId",
                        -1
                );

        currentTechnicianName =
                getIntent().getStringExtra(
                        "technicianName"
                );

        currentStatus =
                getIntent().getStringExtra(
                        "status"
                );

        currentPrice =
                getIntent().getDoubleExtra(
                        "finalPrice",
                        0.0
                );

        // --------------------------------------------------------
        // SAFETY DEFAULTS
        // --------------------------------------------------------

        if (customerName == null ||
                customerName.trim().isEmpty()) {

            customerName =
                    "Unknown Customer";
        }

        if (deviceModel == null ||
                deviceModel.trim().isEmpty()) {

            deviceModel =
                    "Unknown Device";
        }

        if (serviceName == null ||
                serviceName.trim().isEmpty()) {

            serviceName =
                    "Repair Service";
        }

        if (currentBranchName == null ||
                currentBranchName.trim().isEmpty()) {

            currentBranchName =
                    "Not Assigned";
        }

        if (currentTechnicianName == null ||
                currentTechnicianName.trim().isEmpty()) {

            currentTechnicianName =
                    "Not Assigned";
        }

        if (currentStatus == null ||
                currentStatus.trim().isEmpty()) {

            currentStatus =
                    "Pending";
        }

        if ("Ready for Collection"
                .equalsIgnoreCase(currentStatus)) {

            currentStatus =
                    "Ready for Pickup";
        }
    }

    // ============================================================
    // DISPLAY APPOINTMENT
    // ============================================================

    private void displayAppointment() {

        txtManageCustomer.setText(
                String.format(
                        Locale.getDefault(),
                        "Customer: %s",
                        safeText(customerName)
                )
        );

        txtManageDevice.setText(
                String.format(
                        Locale.getDefault(),
                        "Device: %s",
                        safeText(deviceModel)
                )
        );

        txtManageService.setText(
                String.format(
                        Locale.getDefault(),
                        "Service: %s",
                        safeText(serviceName)
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

    // ============================================================
    // LOAD BRANCHES
    // ============================================================

    private void loadBranches() {

        branchList.clear();
        branchIds.clear();
        branchNames.clear();

        /*
         * IMPORTANT:
         *
         * DatabaseHelper.getAllBranches()
         * returns List<Branch>.
         *
         * Therefore NO Cursor is used here.
         */

        List<Branch> databaseBranches =
                databaseHelper.getAllBranches();

        if (databaseBranches != null) {

            branchList.addAll(
                    databaseBranches
            );
        }

        // --------------------------------------------------------
        // Convert Branch objects into Spinner data
        // --------------------------------------------------------

        for (Branch branch : branchList) {

            if (branch == null) {
                continue;
            }

            branchIds.add(
                    branch.getId()
            );

            branchNames.add(
                    safeText(
                            branch.getName()
                    )
            );
        }

        // --------------------------------------------------------
        // CREATE SPINNER ADAPTER
        // --------------------------------------------------------

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

        // --------------------------------------------------------
        // SELECT CURRENT BRANCH
        // --------------------------------------------------------

        selectBranch(
                currentBranchName
        );
    }

    // ============================================================
    // SELECT CURRENT BRANCH
    // ============================================================

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

    // ============================================================
    // LOAD TECHNICIAN
    // ============================================================

    private void loadTechnician() {

        technicianIds.clear();
        technicianNames.clear();

        /*
         * The current DatabaseHelper does not have
         * getAllTechnicians().
         *
         * We therefore use the technician already stored
         * in the RepairAppointment.
         */

        if (currentTechnicianId > 0 &&
                currentTechnicianName != null &&
                !currentTechnicianName
                        .trim()
                        .isEmpty() &&
                !"Not Assigned".equalsIgnoreCase(
                        currentTechnicianName
                )) {

            technicianIds.add(
                    currentTechnicianId
            );

            technicianNames.add(
                    currentTechnicianName
            );

        } else {

            /*
             * No technician has been assigned yet.
             *
             * We use -1 to represent "Not Assigned".
             */

            technicianIds.add(-1);

            technicianNames.add(
                    "Not Assigned"
            );
        }

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
    }

    // ============================================================
    // STATUS SPINNER
    // ============================================================

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

    // ============================================================
    // SELECT STATUS
    // ============================================================

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

    // ============================================================
    // UPDATE APPOINTMENT
    // ============================================================

    private void updateAppointment() {

        // --------------------------------------------------------
        // VALIDATE BRANCH
        // --------------------------------------------------------

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

        // --------------------------------------------------------
        // VALIDATE TECHNICIAN
        // --------------------------------------------------------

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

        // --------------------------------------------------------
        // GET STATUS
        // --------------------------------------------------------

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

        // --------------------------------------------------------
        // GET PRICE
        // --------------------------------------------------------

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

        // --------------------------------------------------------
        // PRICE VALIDATION
        // --------------------------------------------------------

        if (price < 0) {

            edtManagePrice.setError(
                    "Price cannot be negative"
            );

            edtManagePrice.requestFocus();

            return;
        }

        // --------------------------------------------------------
        // GET SELECTED BRANCH
        // --------------------------------------------------------

        int branchId =
                branchIds.get(
                        branchPosition
                );

        // --------------------------------------------------------
        // GET SELECTED TECHNICIAN
        // --------------------------------------------------------

        int technicianId =
                technicianIds.get(
                        technicianPosition
                );

        String technicianName =
                technicianNames.get(
                        technicianPosition
                );

        // --------------------------------------------------------
        // UPDATE DATABASE
        // --------------------------------------------------------

        boolean updated =
                databaseHelper.updateRepair(
                        repairId,
                        branchId,
                        technicianId,
                        technicianName,
                        status,
                        price
                );

        // --------------------------------------------------------
        // SUCCESS
        // --------------------------------------------------------

        if (updated) {

            Toast.makeText(
                    this,
                    "Appointment updated successfully.",
                    Toast.LENGTH_SHORT
            ).show();

            /*
             * Tell ManageAppointmentsActivity that the
             * database was successfully updated.
             */

            setResult(
                    RESULT_OK
            );

            finish();

            return;
        }

        // --------------------------------------------------------
        // FAILED
        // --------------------------------------------------------

        Toast.makeText(
                this,
                "Unable to update appointment.",
                Toast.LENGTH_LONG
        ).show();
    }

    // ============================================================
    // SAFE TEXT
    // ============================================================

    private String safeText(
            String value
    ) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not available";
        }

        return value.trim();
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