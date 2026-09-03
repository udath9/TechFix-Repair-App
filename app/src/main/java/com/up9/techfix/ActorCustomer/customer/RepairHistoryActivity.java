package com.up9.techfix.ActorCustomer.customer;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RepairHistoryActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private LinearLayout historyContainer;

    private int customerId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_repair_history);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
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

        databaseHelper =
                new DatabaseHelper(this);


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
        // HISTORY CONTAINER
        // =================================================

        historyContainer =
                findViewById(
                        R.id.historyContainer
                );


        // =================================================
        // LOAD HISTORY
        // =================================================

        loadRepairHistory();
    }


    // =====================================================
    // LOAD REPAIR HISTORY
    // =====================================================

    private void loadRepairHistory() {

        historyContainer.removeAllViews();


        // -------------------------------------------------
        // Check customer
        // -------------------------------------------------

        if (customerId == -1) {

            showMessage(
                    "Customer information not found.\nPlease log in again."
            );

            return;
        }


        Cursor cursor =
                databaseHelper.getRepairHistory(
                        customerId
                );


        // -------------------------------------------------
        // No repairs
        // -------------------------------------------------

        if (!cursor.moveToFirst()) {

            cursor.close();

            showMessage(
                    "No repair history found."
            );

            return;
        }


        // -------------------------------------------------
        // Read repairs
        // -------------------------------------------------

        do {

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


            String status =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "status"
                            )
                    );


            double price =
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow(
                                    "price"
                            )
                    );


            // -------------------------------------------------
            // Format date
            // -------------------------------------------------

            String formattedDate =
                    formatRepairDate(
                            repairDate
                    );


            // -------------------------------------------------
            // Create repair card
            // -------------------------------------------------

            LinearLayout repairCard =
                    createRepairCard();


            // Repair ID

            TextView repairIdText =
                    createTitle(
                            "Repair #" + repairId
                    );

            repairCard.addView(
                    repairIdText
            );


            // Repair information

            TextView repairInfo =
                    createInformation(
                            "Device: "
                                    + safeText(deviceCategory)
                                    + " - "
                                    + safeText(deviceModel)
                                    + "\n\n"

                                    + "Service: "
                                    + safeText(serviceName)
                                    + "\n\n"

                                    + "Branch: "
                                    + safeText(branchName)
                                    + "\n\n"

                                    + "Date: "
                                    + formattedDate
                                    + "\n\n"

                                    + "Status: "
                                    + safeText(status)
                                    + "\n\n"

                                    + "Price: LKR "
                                    + String.format(
                                    Locale.getDefault(),
                                    "%,.2f",
                                    price
                            )
                    );


            repairCard.addView(
                    repairInfo
            );


            historyContainer.addView(
                    repairCard
            );


        } while (cursor.moveToNext());


        cursor.close();
    }


    // =====================================================
    // CREATE REPAIR CARD
    // =====================================================

    private LinearLayout createRepairCard() {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                16,
                16,
                16,
                16
        );


        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );


        params.setMargins(
                0,
                0,
                0,
                20
        );


        card.setLayoutParams(
                params
        );


        card.setBackgroundResource(
                android.R.drawable.editbox_background
        );


        return card;
    }


    // =====================================================
    // CREATE TITLE
    // =====================================================

    private TextView createTitle(
            String text
    ) {

        TextView textView =
                new TextView(this);

        textView.setText(
                text
        );

        textView.setTextSize(
                20
        );

        textView.setTextColor(
                getResources().getColor(
                        android.R.color.black
                )
        );

        textView.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        textView.setPadding(
                0,
                0,
                0,
                12
        );

        return textView;
    }


    // =====================================================
    // CREATE INFORMATION TEXT
    // =====================================================

    private TextView createInformation(
            String text
    ) {

        TextView textView =
                new TextView(this);

        textView.setText(
                text
        );

        textView.setTextSize(
                16
        );

        return textView;
    }


    // =====================================================
    // SHOW MESSAGE
    // =====================================================

    private void showMessage(
            String message
    ) {

        TextView messageText =
                new TextView(this);

        messageText.setText(
                message
        );

        messageText.setTextSize(
                18
        );

        messageText.setGravity(
                android.view.Gravity.CENTER
        );

        messageText.setPadding(
                20,
                40,
                20,
                40
        );


        historyContainer.addView(
                messageText
        );
    }


    // =====================================================
    // FORMAT DATE
    // =====================================================

    private String formatRepairDate(
            String dateValue
    ) {

        if (
                dateValue == null
                        ||
                        dateValue.trim().isEmpty()
        ) {

            return "Unknown";
        }


        try {

            long timestamp =
                    Long.parseLong(
                            dateValue
                    );


            SimpleDateFormat formatter =
                    new SimpleDateFormat(
                            "dd MMMM yyyy",
                            Locale.getDefault()
                    );


            return formatter.format(
                    new Date(timestamp)
            );

        } catch (Exception e) {

            return dateValue;
        }
    }


    // =====================================================
    // SAFE TEXT
    // =====================================================

    private String safeText(
            String text
    ) {

        if (
                text == null
                        ||
                        text.trim().isEmpty()
        ) {

            return "Unknown";
        }

        return text;
    }
}