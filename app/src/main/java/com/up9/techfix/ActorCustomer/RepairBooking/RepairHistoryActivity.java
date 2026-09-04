package com.up9.techfix.ActorCustomer.RepairBooking;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        setContentView(
                R.layout.activity_repair_history
        );

        databaseHelper =
                new DatabaseHelper(this);

        SharedPreferences preferences =
                getSharedPreferences(
                        "TechFixSession",
                        MODE_PRIVATE
                );

        customerId =
                preferences.getInt(
                        "customerId",
                        -1
                );

        historyContainer =
                findViewById(
                        R.id.historyContainer
                );

        loadRepairHistory();
    }

    private void loadRepairHistory() {

        historyContainer.removeAllViews();

        if (customerId == -1) {

            showMessage(
                    "Customer information not found.\n"
                            + "Please log in again."
            );

            return;
        }

        Cursor cursor =
                databaseHelper.getRepairHistory(
                        customerId
                );

        if (cursor == null) {

            showMessage(
                    "Unable to load repair history."
            );

            return;
        }

        if (!cursor.moveToFirst()) {

            cursor.close();

            showMessage(
                    "No repair history found."
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

            String categoryName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "category_name"
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
                                    "final_price"
                            )
                    );

            LinearLayout repairCard =
                    createRepairCard();

            repairCard.addView(
                    createTitle(
                            "Repair #" + repairId
                    )
            );

            String information =
                    "Device: "
                            + safeText(categoryName)
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
                            + formatRepairDate(repairDate)
                            + "\n\n"
                            + "Status: "
                            + safeText(status)
                            + "\n\n"
                            + "Price: LKR "
                            + String.format(
                            Locale.getDefault(),
                            "%,.2f",
                            price
                    );

            repairCard.addView(
                    createInformation(information)
            );

            historyContainer.addView(
                    repairCard
            );

        } while (cursor.moveToNext());

        cursor.close();
    }

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

        card.setLayoutParams(params);

        card.setBackgroundResource(
                android.R.drawable.editbox_background
        );

        return card;
    }

    private TextView createTitle(String text) {

        TextView textView =
                new TextView(this);

        textView.setText(text);
        textView.setTextSize(20);

        textView.setTypeface(
                null,
                Typeface.BOLD
        );

        textView.setPadding(
                0,
                0,
                0,
                12
        );

        return textView;
    }

    private TextView createInformation(String text) {

        TextView textView =
                new TextView(this);

        textView.setText(text);
        textView.setTextSize(16);

        return textView;
    }

    private void showMessage(String message) {

        TextView messageText =
                new TextView(this);

        messageText.setText(message);
        messageText.setTextSize(18);
        messageText.setGravity(
                Gravity.CENTER
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

    private String formatRepairDate(
            String dateValue
    ) {

        if (dateValue == null ||
                dateValue.trim().isEmpty()) {

            return "Unknown";
        }

        try {

            long timestamp =
                    Long.parseLong(dateValue);

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

    private String safeText(String text) {

        if (text == null ||
                text.trim().isEmpty()) {

            return "Unknown";
        }

        return text;
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}