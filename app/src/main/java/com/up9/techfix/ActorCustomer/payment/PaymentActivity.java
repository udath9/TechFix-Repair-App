package com.up9.techfix.ActorCustomer.payment;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class PaymentActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private LinearLayout currentPaymentContainer;
    private LinearLayout paymentHistoryContainer;

    private int customerId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(
                R.layout.activity_payment
        );


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
        // GET CONTAINERS
        // =================================================

        currentPaymentContainer =
                findViewById(
                        R.id.currentPaymentContainer
                );

        paymentHistoryContainer =
                findViewById(
                        R.id.paymentHistoryContainer
                );


        // =================================================
        // LOAD DATA
        // =================================================

        loadCurrentPayments();

        loadPaymentHistory();
    }


    // =====================================================
    // CURRENT / UNPAID PAYMENTS
    // =====================================================

    private void loadCurrentPayments() {

        currentPaymentContainer.removeAllViews();


        if (customerId == -1) {

            showMessage(
                    currentPaymentContainer,
                    "Customer information not found.\nPlease log in again."
            );

            return;
        }


        Cursor cursor =
                databaseHelper.getUnpaidRepairs(
                        customerId
                );


        if (!cursor.moveToFirst()) {

            cursor.close();

            showMessage(
                    currentPaymentContainer,
                    "No outstanding payments."
            );

            return;
        }


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


            String status =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "status"
                            )
                    );


            double amount =
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow(
                                    "amount"
                            )
                    );


            createCurrentPaymentCard(
                    repairId,
                    deviceCategory,
                    deviceModel,
                    serviceName,
                    branchName,
                    status,
                    amount
            );


        } while (cursor.moveToNext());


        cursor.close();
    }


    // =====================================================
    // CURRENT PAYMENT CARD
    // =====================================================

    private void createCurrentPaymentCard(
            int repairId,
            String deviceCategory,
            String deviceModel,
            String serviceName,
            String branchName,
            String status,
            double amount
    ) {

        LinearLayout card =
                createCard();


        TextView title =
                createTitle(
                        "Repair #" + repairId
                );

        card.addView(title);


        TextView information =
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

                                + "Status: "
                                + safeText(status)
                                + "\n\n"

                                + "Amount Due: LKR "
                                + String.format(
                                Locale.getDefault(),
                                "%,.2f",
                                amount
                        )
                );


        card.addView(
                information
        );


        Button payButton =
                new Button(this);

        payButton.setText(
                "Pay Now"
        );


        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        buttonParams.setMargins(
                0,
                16,
                0,
                0
        );


        payButton.setLayoutParams(
                buttonParams
        );


        payButton.setOnClickListener(
                v -> showPaymentConfirmation(
                        repairId,
                        amount
                )
        );


        card.addView(
                payButton
        );


        currentPaymentContainer.addView(
                card
        );
    }


    // =====================================================
    // PAYMENT CONFIRMATION
    // =====================================================

    private void showPaymentConfirmation(
            int repairId,
            double amount
    ) {

        new AlertDialog.Builder(this)

                .setTitle(
                        "Confirm Payment"
                )

                .setMessage(
                        "Repair #" + repairId
                                + "\n\n"
                                + "Amount: LKR "
                                + String.format(
                                Locale.getDefault(),
                                "%,.2f",
                                amount
                        )
                                + "\n\n"
                                + "Confirm payment?"
                )

                .setNegativeButton(
                        "Cancel",
                        null
                )

                .setPositiveButton(
                        "Pay",
                        (dialog, which) ->
                                processPayment(
                                        repairId,
                                        amount
                                )
                )

                .show();
    }


    // =====================================================
    // PROCESS PAYMENT
    // =====================================================

    private void processPayment(
            int repairId,
            double amount
    ) {

        String paymentDate =
                String.valueOf(
                        System.currentTimeMillis()
                );


        long paymentId =
                databaseHelper.createPayment(
                        repairId,
                        amount,
                        paymentDate,
                        "Paid"
                );


        if (paymentId != -1) {

            Toast.makeText(
                    this,
                    "Payment successful!\n"
                            + "Payment ID: "
                            + paymentId,
                    Toast.LENGTH_LONG
            ).show();


            // Refresh both sections

            loadCurrentPayments();

            loadPaymentHistory();

        } else {

            Toast.makeText(
                    this,
                    "Payment failed.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    // =====================================================
    // PAYMENT HISTORY
    // =====================================================

    private void loadPaymentHistory() {

        paymentHistoryContainer.removeAllViews();


        if (customerId == -1) {

            showMessage(
                    paymentHistoryContainer,
                    "Customer information not found."
            );

            return;
        }


        Cursor cursor =
                databaseHelper.getPaymentHistory(
                        customerId
                );


        if (!cursor.moveToFirst()) {

            cursor.close();

            showMessage(
                    paymentHistoryContainer,
                    "No payment history found."
            );

            return;
        }


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
                                    "amount"
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
                                    "status"
                            )
                    );


            String serviceName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "service_name"
                            )
                    );


            createPaymentHistoryCard(
                    paymentId,
                    repairId,
                    amount,
                    paymentDate,
                    status,
                    serviceName
            );


        } while (cursor.moveToNext());


        cursor.close();
    }


    // =====================================================
    // PAYMENT HISTORY CARD
    // =====================================================

    private void createPaymentHistoryCard(
            int paymentId,
            int repairId,
            double amount,
            String paymentDate,
            String status,
            String serviceName
    ) {

        LinearLayout card =
                createCard();


        TextView title =
                createTitle(
                        "Payment #" + paymentId
                );


        card.addView(
                title
        );


        TextView information =
                createInformation(
                        "Repair #: "
                                + repairId
                                + "\n\n"

                                + "Service: "
                                + safeText(serviceName)
                                + "\n\n"

                                + "Date: "
                                + formatPaymentDate(
                                paymentDate
                        )
                                + "\n\n"

                                + "Amount: LKR "
                                + String.format(
                                Locale.getDefault(),
                                "%,.2f",
                                amount
                        )
                                + "\n\n"

                                + "Status: "
                                + safeText(status)
                );


        card.addView(
                information
        );


        paymentHistoryContainer.addView(
                card
        );
    }


    // =====================================================
    // CREATE CARD
    // =====================================================

    private LinearLayout createCard() {

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
    // TITLE
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
    // INFORMATION
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
    // MESSAGE
    // =====================================================

    private void showMessage(
            LinearLayout container,
            String message
    ) {

        TextView textView =
                new TextView(this);

        textView.setText(
                message
        );

        textView.setTextSize(
                18
        );

        textView.setGravity(
                Gravity.CENTER
        );

        textView.setPadding(
                20,
                40,
                20,
                40
        );


        container.addView(
                textView
        );
    }


    // =====================================================
    // FORMAT DATE
    // =====================================================

    private String formatPaymentDate(
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