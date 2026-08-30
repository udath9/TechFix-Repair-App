package com.up9.techfix.customer;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RepairTrackingActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private LinearLayout repairsContainer;
    private TextView tvNoRepairs;

    private int customerId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(
                R.layout.activity_repair_tracking
        );


        // =================================================
        // WINDOW INSETS
        // =================================================

        View mainView =
                findViewById(R.id.main);

        ViewCompat.setOnApplyWindowInsetsListener(
                mainView,
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );


        // =================================================
        // DATABASE
        // =================================================

        databaseHelper =
                new DatabaseHelper(this);


        // =================================================
        // GET VIEWS
        // =================================================

        repairsContainer =
                findViewById(
                        R.id.repairsContainer
                );

        tvNoRepairs =
                findViewById(
                        R.id.tvNoRepairs
                );


        // =================================================
        // GET LOGGED-IN CUSTOMER
        // =================================================

        android.content.SharedPreferences preferences =
                getSharedPreferences(
                        "TechFixSession",
                        MODE_PRIVATE
                );

        customerId =
                preferences.getInt(
                        "customerId",
                        -1
                );


        // =================================================
        // LOAD REPAIRS
        // =================================================

        loadRepairs();
    }


    // =====================================================
    // LOAD CUSTOMER REPAIRS
    // =====================================================

    private void loadRepairs() {

        repairsContainer.removeAllViews();

        if (customerId == -1) {

            tvNoRepairs.setText(
                    "Customer information not found.\n"
                            + "Please log in again."
            );

            tvNoRepairs.setVisibility(
                    View.VISIBLE
            );

            return;
        }


        Cursor cursor =
                databaseHelper.getCustomerActiveRepairs(
                        customerId
                );


        if (cursor == null ||
                !cursor.moveToFirst()) {

            if (cursor != null) {
                cursor.close();
            }

            tvNoRepairs.setText(
                    "You have no active repairs."
            );

            tvNoRepairs.setVisibility(
                    View.VISIBLE
            );

            return;
        }


        tvNoRepairs.setVisibility(
                View.GONE
        );


        do {

            createRepairCard(cursor);

        } while (cursor.moveToNext());


        cursor.close();
    }


    // =====================================================
    // CREATE REPAIR CARD
    // =====================================================

    private void createRepairCard(Cursor cursor) {

        int repairId =
                cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                "repair_id"
                        )
                );


        String deviceCategory =
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "device_category"
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


        String status =
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "status"
                        )
                );


        String branchName =
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "branch_name"
                        )
                );


        String repairDate =
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "repair_date"
                        )
                );


        // =================================================
        // CARD
        // =================================================

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                30,
                25,
                30,
                25
        );

        card.setBackgroundResource(
                android.R.drawable.dialog_holo_light_frame
        );


        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(
                0,
                0,
                0,
                25
        );

        card.setLayoutParams(
                cardParams
        );


        // =================================================
        // REPAIR ID
        // =================================================

        TextView tvRepairId =
                createTextView(
                        "Repair ID: #" + repairId,
                        20,
                        true
                );

        card.addView(
                tvRepairId
        );


        // =================================================
        // DEVICE
        // =================================================

        TextView tvDevice =
                createTextView(
                        "Device: "
                                + deviceCategory
                                + " - "
                                + deviceModel,
                        17,
                        false
                );

        card.addView(
                tvDevice
        );


        // =================================================
        // SERVICE
        // =================================================

        TextView tvService =
                createTextView(
                        "Service: "
                                + safeString(serviceName),
                        17,
                        false
                );

        card.addView(
                tvService
        );


        // =================================================
        // BRANCH
        // =================================================

        TextView tvBranch =
                createTextView(
                        "Branch: "
                                + safeString(branchName),
                        17,
                        false
                );

        card.addView(
                tvBranch
        );


        // =================================================
        // DATE
        // =================================================

        TextView tvDate =
                createTextView(
                        "Booking Date: "
                                + formatRepairDate(
                                repairDate
                        ),
                        16,
                        false
                );

        card.addView(
                tvDate
        );


        // =================================================
        // STATUS
        // =================================================

        TextView tvStatus =
                createTextView(
                        "Status: "
                                + status,
                        18,
                        true
                );

        tvStatus.setPadding(
                0,
                20,
                0,
                15
        );

        card.addView(
                tvStatus
        );


        // =================================================
        // PROGRESS INFORMATION
        // =================================================

        TextView tvProgress =
                createTextView(
                        getProgressText(status),
                        16,
                        false
                );

        card.addView(
                tvProgress
        );


        // =================================================
        // CANCEL BUTTON
        // =================================================

        if ("Pending".equalsIgnoreCase(status)) {

            Button btnCancel =
                    new Button(this);

            btnCancel.setText(
                    "Cancel Booking"
            );

            btnCancel.setOnClickListener(
                    v -> showCancelConfirmation(
                            repairId
                    )
            );

            LinearLayout.LayoutParams buttonParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            buttonParams.setMargins(
                    0,
                    20,
                    0,
                    0
            );

            btnCancel.setLayoutParams(
                    buttonParams
            );

            card.addView(
                    btnCancel
            );
        }


        repairsContainer.addView(
                card
        );
    }


    // =====================================================
    // CANCEL CONFIRMATION
    // =====================================================

    private void showCancelConfirmation(
            int repairId
    ) {

        new AlertDialog.Builder(this)

                .setTitle(
                        "Cancel Repair Booking"
                )

                .setMessage(
                        "Are you sure you want to cancel "
                                + "Repair #"
                                + repairId
                                + "?"
                )

                .setNegativeButton(
                        "No",
                        null
                )

                .setPositiveButton(
                        "Yes, Cancel",
                        (dialog, which) -> {

                            cancelRepair(
                                    repairId
                            );
                        }
                )

                .show();
    }


    // =====================================================
    // CANCEL REPAIR
    // =====================================================

    private void cancelRepair(
            int repairId
    ) {

        boolean cancelled =
                databaseHelper.cancelRepair(
                        repairId,
                        customerId
                );


        if (cancelled) {

            Toast.makeText(
                    this,
                    "Repair booking cancelled.",
                    Toast.LENGTH_LONG
            ).show();


            loadRepairs();

        } else {

            Toast.makeText(
                    this,
                    "Unable to cancel this repair. "
                            + "The repair may have already started.",
                    Toast.LENGTH_LONG
            ).show();


            loadRepairs();
        }
    }


    // =====================================================
    // PROGRESS TEXT
    // =====================================================

    private String getProgressText(
            String status
    ) {

        if (status == null) {
            return "";
        }


        switch (status) {

            case "Pending":

                return "● Appointment Received\n"
                        + "○ Repair In Progress\n"
                        + "○ Ready for Collection\n"
                        + "○ Completed";


            case "In Progress":

                return "● Appointment Received\n"
                        + "● Repair In Progress\n"
                        + "○ Ready for Collection\n"
                        + "○ Completed";


            case "Ready for Collection":

                return "● Appointment Received\n"
                        + "● Repair In Progress\n"
                        + "● Ready for Collection\n"
                        + "○ Completed";


            case "Completed":

                return "● Appointment Received\n"
                        + "● Repair In Progress\n"
                        + "● Ready for Collection\n"
                        + "● Completed";


            default:

                return "Current Status: "
                        + status;
        }
    }


    // =====================================================
    // CREATE TEXT VIEW
    // =====================================================

    private TextView createTextView(
            String text,
            float textSize,
            boolean bold
    ) {

        TextView textView =
                new TextView(this);

        textView.setText(
                text
        );

        textView.setTextSize(
                textSize
        );

        textView.setPadding(
                0,
                8,
                0,
                8
        );

        textView.setGravity(
                Gravity.START
        );


        if (bold) {

            textView.setTypeface(
                    null,
                    android.graphics.Typeface.BOLD
            );
        }


        return textView;
    }


    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safeString(
            String value
    ) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not available";
        }

        return value;
    }


    // =====================================================
    // FORMAT DATE
    // =====================================================

    private String formatRepairDate(
            String timestamp
    ) {

        try {

            long time =
                    Long.parseLong(
                            timestamp
                    );

            SimpleDateFormat formatter =
                    new SimpleDateFormat(
                            "dd MMM yyyy, hh:mm a",
                            Locale.getDefault()
                    );

            return formatter.format(
                    new Date(time)
            );

        } catch (Exception e) {

            return safeString(
                    timestamp
            );
        }
    }
}