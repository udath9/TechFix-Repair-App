package com.up9.techfix.ActorCustomer.RepairBooking;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
        setContentView(R.layout.activity_repair_tracking);

        setupWindowInsets();

        databaseHelper = new DatabaseHelper(this);

        repairsContainer =
                findViewById(R.id.repairsContainer);

        tvNoRepairs =
                findViewById(R.id.tvNoRepairs);

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

        loadRepairs();
    }

    private void setupWindowInsets() {
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
    }

    // Load the logged-in customer's active repairs.
    private void loadRepairs() {
        repairsContainer.removeAllViews();

        if (customerId == -1) {

            showNoRepairsMessage(
                    "Customer information not found.\n"
                            + "Please log in again."
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

            showNoRepairsMessage(
                    "You have no active repairs."
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

    private void createRepairCard(Cursor cursor) {

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

        String customerImageUri =
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "image_uri"
                        )
                );

        String progressImageUri =
                cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "in_progress_photo_uri"
                        )
                );

        LinearLayout card =
                createCard();

        card.addView(
                createTextView(
                        "Repair ID: #" + repairId,
                        20,
                        true
                )
        );

        card.addView(
                createTextView(
                        "Device: "
                                + safeString(categoryName)
                                + " - "
                                + safeString(deviceModel),
                        17,
                        false
                )
        );

        card.addView(
                createTextView(
                        "Service: "
                                + safeString(serviceName),
                        17,
                        false
                )
        );

        card.addView(
                createTextView(
                        "Branch: "
                                + safeString(branchName),
                        17,
                        false
                )
        );

        card.addView(
                createTextView(
                        "Booking Date: "
                                + formatRepairDate(repairDate),
                        16,
                        false
                )
        );

        addRepairImages(
                card,
                customerImageUri,
                progressImageUri
        );

        TextView tvStatus =
                createTextView(
                        "Status: "
                                + safeString(status),
                        18,
                        true
                );

        tvStatus.setPadding(
                0,
                20,
                0,
                15
        );

        card.addView(tvStatus);

        card.addView(
                createTextView(
                        getProgressText(status),
                        16,
                        false
                )
        );

        if ("Pending".equalsIgnoreCase(status)) {

            Button btnCancel =
                    new Button(this);

            btnCancel.setText(
                    "Cancel Booking"
            );

            btnCancel.setOnClickListener(
                    v -> showCancelConfirmation(repairId)
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

            card.addView(btnCancel);
        }

        repairsContainer.addView(card);
    }

    private LinearLayout createCard() {
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

        card.setLayoutParams(cardParams);

        return card;
    }

    // Display the customer's uploaded image and technician progress image.
    private void addRepairImages(
            LinearLayout card,
            String customerImageUri,
            String progressImageUri
    ) {

        TextView title =
                createTextView(
                        "Repair Photos",
                        17,
                        true
                );

        title.setPadding(
                0,
                15,
                0,
                10
        );

        card.addView(title);

        LinearLayout imageContainer =
                new LinearLayout(this);

        imageContainer.setOrientation(
                LinearLayout.HORIZONTAL
        );

        imageContainer.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams containerParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        150
                );

        imageContainer.setLayoutParams(
                containerParams
        );

        imageContainer.addView(
                createImageBox(
                        "Customer Photo",
                        customerImageUri
                )
        );

        imageContainer.addView(
                createImageBox(
                        "Progress Photo",
                        progressImageUri
                )
        );

        card.addView(imageContainer);
    }

    private LinearLayout createImageBox(
            String label,
            String imageUri
    ) {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams boxParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                );

        boxParams.setMargins(
                5,
                5,
                5,
                5
        );

        box.setLayoutParams(boxParams);

        ImageView imageView =
                new ImageView(this);

        LinearLayout.LayoutParams imageParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        110
                );

        imageView.setLayoutParams(imageParams);

        imageView.setScaleType(
                ImageView.ScaleType.CENTER_CROP
        );

        imageView.setBackgroundResource(
                android.R.drawable.dialog_holo_light_frame
        );

        if (imageUri != null &&
                !imageUri.trim().isEmpty()) {

            try {

                imageView.setImageURI(
                        Uri.parse(imageUri)
                );

            } catch (Exception e) {

                imageView.setImageResource(
                        android.R.drawable.ic_menu_report_image
                );
            }

        } else {

            imageView.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );
        }

        box.addView(imageView);

        TextView labelView =
                createTextView(
                        label,
                        13,
                        false
                );

        labelView.setGravity(
                Gravity.CENTER
        );

        box.addView(labelView);

        return box;
    }

    private void showCancelConfirmation(
            int repairId
    ) {

        new AlertDialog.Builder(this)
                .setTitle("Cancel Repair Booking")
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
                        (dialog, which) ->
                                cancelRepair(repairId)
                )
                .show();
    }

    private void cancelRepair(int repairId) {

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

        } else {

            Toast.makeText(
                    this,
                    "Unable to cancel this repair. "
                            + "The repair may have already started.",
                    Toast.LENGTH_LONG
            ).show();
        }

        loadRepairs();
    }

    private String getProgressText(String status) {

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
                return "Current Status: " + status;
        }
    }

    private TextView createTextView(
            String text,
            float textSize,
            boolean bold
    ) {

        TextView textView =
                new TextView(this);

        textView.setText(text);
        textView.setTextSize(textSize);

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
                    Typeface.BOLD
            );
        }

        return textView;
    }

    private void showNoRepairsMessage(String message) {

        tvNoRepairs.setText(message);

        tvNoRepairs.setVisibility(
                View.VISIBLE
        );
    }

    private String safeString(String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not available";
        }

        return value;
    }

    private String formatRepairDate(String timestamp) {

        if (timestamp == null ||
                timestamp.trim().isEmpty()) {

            return "Not available";
        }

        try {

            long time =
                    Long.parseLong(timestamp);

            SimpleDateFormat formatter =
                    new SimpleDateFormat(
                            "dd MMM yyyy, hh:mm a",
                            Locale.getDefault()
                    );

            return formatter.format(
                    new Date(time)
            );

        } catch (Exception e) {

            return timestamp;
        }
    }
}