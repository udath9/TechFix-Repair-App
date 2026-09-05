package com.up9.techfix.ActorCustomer.RepairBooking;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;

import java.io.InputStream;
import java.util.Locale;

public class RepairTrackingActivity extends AppCompatActivity {

    private static final String TAG =
            "RepairTrackingActivity";

    private DatabaseHelper databaseHelper;

    private LinearLayout repairsContainer;
    private TextView tvNoRepairs;

    private int customerId = -1;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_repair_tracking
        );

        databaseHelper =
                new DatabaseHelper(this);

        repairsContainer =
                findViewById(
                        R.id.repairsContainer
                );

        tvNoRepairs =
                findViewById(
                        R.id.tvNoRepairs
                );

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

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {
            loadRepairs();
        }
    }

    private void loadRepairs() {

        repairsContainer.removeAllViews();

        if (customerId <= 0) {

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

        if (cursor == null) {

            showNoRepairsMessage(
                    "Unable to load repairs."
            );

            return;
        }

        if (!cursor.moveToFirst()) {

            cursor.close();

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

    private void createRepairCard(
            Cursor cursor
    ) {

        int repairId =
                cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                "repair_id"
                        )
                );

        String categoryName =
                getColumnValue(
                        cursor,
                        "category_name"
                );

        String deviceModel =
                getColumnValue(
                        cursor,
                        "device_model"
                );

        String serviceName =
                getColumnValue(
                        cursor,
                        "service_name"
                );

        String branchName =
                getColumnValue(
                        cursor,
                        "branch_name"
                );

        String branchAddress =
                getColumnValue(
                        cursor,
                        "branch_address"
                );

        String repairDate =
                getColumnValue(
                        cursor,
                        "repair_date"
                );

        String technicianName =
                getColumnValue(
                        cursor,
                        "technician_name"
                );

        String status =
                getColumnValue(
                        cursor,
                        "status"
                );

        MaterialCardView card =
                createCard();

        LinearLayout cardContent =
                new LinearLayout(this);

        cardContent.setOrientation(
                LinearLayout.VERTICAL
        );

        cardContent.setPadding(
                20,
                20,
                20,
                20
        );

        card.addView(cardContent);

        // =========================================
        // HEADER
        // =========================================

        LinearLayout header =
                new LinearLayout(this);

        header.setOrientation(
                LinearLayout.HORIZONTAL
        );

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        TextView repairIdText =
                createTextView(
                        "Repair #" + repairId,
                        20,
                        true
                );

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        header.addView(
                repairIdText,
                titleParams
        );

        TextView statusBadge =
                createStatusBadge(status);

        header.addView(
                statusBadge
        );

        cardContent.addView(header);

        // =========================================
        // DEVICE INFORMATION
        // =========================================

        cardContent.addView(
                createSectionTitle(
                        "Device Information"
                )
        );

        cardContent.addView(
                createInfoRow(
                        "Category",
                        safeString(categoryName)
                )
        );

        cardContent.addView(
                createInfoRow(
                        "Model",
                        safeString(deviceModel)
                )
        );

        // =========================================
        // SERVICE INFORMATION
        // =========================================

        cardContent.addView(
                createSectionTitle(
                        "Repair Service"
                )
        );

        cardContent.addView(
                createInfoRow(
                        "Service",
                        safeString(serviceName)
                )
        );

        cardContent.addView(
                createInfoRow(
                        "Booking Date",
                        safeString(repairDate)
                )
        );


        cardContent.addView(
                createSectionTitle(
                        "Repair Branch"
                )
        );

        cardContent.addView(
                createInfoRow(
                        "Branch",
                        safeString(branchName)
                )
        );

        cardContent.addView(
                createInfoRow(
                        "Address",
                        safeString(branchAddress)
                )
        );

        if (!isEmpty(technicianName)) {

            cardContent.addView(
                    createSectionTitle(
                            "Assigned Technician"
                    )
            );

            cardContent.addView(
                    createInfoRow(
                            "Technician",
                            technicianName
                    )
            );
        }

        cardContent.addView(
                createSectionTitle(
                        "Repair Progress"
                )
        );

        cardContent.addView(
                createProgressView(status)
        );


        String customerImageUri =
                getColumnValue(
                        cursor,
                        "image_uri"
                );

        String progressImageUri =
                getColumnValue(
                        cursor,
                        "in_progress_photo_uri"
                );

        if (!isEmpty(customerImageUri)
                || !isEmpty(progressImageUri)) {

            cardContent.addView(
                    createSectionTitle(
                            "Repair Photos"
                    )
            );

            cardContent.addView(
                    createImageSection(
                            customerImageUri,
                            progressImageUri
                    )
            );
        }

        if ("Pending".equalsIgnoreCase(
                safeString(status)
        )) {

            Button btnCancel =
                    new Button(this);

            btnCancel.setText(
                    "Cancel Booking"
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

            btnCancel.setOnClickListener(
                    v -> showCancelConfirmation(
                            repairId
                    )
            );

            cardContent.addView(
                    btnCancel
            );
        }

        repairsContainer.addView(card);
    }

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

        textView.setTextColor(
                0xFF222222
        );

        textView.setPadding(
                0,
                8,
                0,
                8
        );

        if (bold) {

            textView.setTypeface(
                    null,
                    Typeface.BOLD
            );
        }

        return textView;
    }

    private MaterialCardView createCard() {

        MaterialCardView card =
                new MaterialCardView(this);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                0,
                0,
                18
        );

        card.setLayoutParams(
                params
        );

        card.setRadius(
                20
        );

        card.setCardElevation(
                4
        );

        card.setUseCompatPadding(
                true
        );

        card.setStrokeWidth(
                0
        );

        return card;
    }

    private TextView createSectionTitle(
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

        textView.setTypeface(
                null,
                Typeface.BOLD
        );

        textView.setTextColor(
                0xFF222222
        );

        textView.setPadding(
                0,
                22,
                0,
                8
        );

        return textView;
    }

    private TextView createInfoRow(
            String label,
            String value
    ) {

        TextView textView =
                new TextView(this);

        textView.setText(
                label + "\n" + value
        );

        textView.setTextSize(
                14
        );

        textView.setTextColor(
                0xFF555555
        );

        textView.setPadding(
                0,
                5,
                0,
                5
        );

        return textView;
    }

    private TextView createStatusBadge(
            String status
    ) {

        String safeStatus =
                safeString(status);

        TextView badge =
                new TextView(this);

        badge.setText(
                safeStatus
        );

        badge.setTextSize(
                12
        );

        badge.setTypeface(
                null,
                Typeface.BOLD
        );

        badge.setGravity(
                Gravity.CENTER
        );

        badge.setPadding(
                18,
                10,
                18,
                10
        );

        String normalized =
                safeStatus.toLowerCase(
                        Locale.ROOT
                );

        if ("pending".equals(normalized)) {

            badge.setTextColor(
                    0xFF8A5A00
            );

            badge.setBackgroundColor(
                    0xFFFFF1CC
            );

        } else if ("assigned".equals(normalized)) {

            badge.setTextColor(
                    0xFF1557A6
            );

            badge.setBackgroundColor(
                    0xFFE8F0FE
            );

        } else if ("in progress".equals(normalized)) {

            badge.setTextColor(
                    0xFF8A4B00
            );

            badge.setBackgroundColor(
                    0xFFFFE8CC
            );

        } else if ("waiting for parts"
                .equals(normalized)) {

            badge.setTextColor(
                    0xFF6A4C93
            );

            badge.setBackgroundColor(
                    0xFFF0E6FA
            );

        } else if ("testing".equals(normalized)) {

            badge.setTextColor(
                    0xFF00695C
            );

            badge.setBackgroundColor(
                    0xFFE0F2F1
            );

        } else if ("ready for collection"
                .equals(normalized)
                || "completed".equals(normalized)) {

            badge.setTextColor(
                    0xFF287A3D
            );

            badge.setBackgroundColor(
                    0xFFE6F4EA
            );

        } else if ("cancelled"
                .equals(normalized)) {

            badge.setTextColor(
                    0xFFC62828
            );

            badge.setBackgroundColor(
                    0xFFFFEBEE
            );

        } else {

            badge.setTextColor(
                    0xFF444444
            );

            badge.setBackgroundColor(
                    0xFFEFEFEF
            );
        }

        return badge;
    }

    private LinearLayout createProgressView(
            String status
    ) {

        LinearLayout container =
                new LinearLayout(this);

        container.setOrientation(
                LinearLayout.VERTICAL
        );

        container.setPadding(
                10,
                8,
                10,
                5
        );

        String normalizedStatus =
                status == null
                        ? ""
                        : status.toLowerCase(
                        Locale.ROOT
                );

        if ("cancelled".equals(
                normalizedStatus
        )) {

            TextView cancelled =
                    createTextView(
                            "● Appointment Cancelled",
                            14,
                            true
                    );

            cancelled.setTextColor(
                    0xFFC62828
            );

            container.addView(
                    cancelled
            );

            return container;
        }

        String[] stages = {
                "Appointment Received",
                "Accepted",
                "Assigned",
                "In Progress",
                "Waiting for Parts",
                "Testing",
                "Ready for Collection",
                "Completed"
        };

        int currentStage =
                getStatusStage(
                        normalizedStatus
                );

        for (
                int i = 0;
                i < stages.length;
                i++
        ) {

            boolean completed =
                    i <= currentStage;

            TextView stage =
                    createTextView(
                            (completed
                                    ? "● "
                                    : "○ ")
                                    + stages[i],
                            14,
                            completed
                    );

            if (completed) {

                stage.setTextColor(
                        0xFF1A73E8
                );

            } else {

                stage.setTextColor(
                        0xFF999999
                );
            }

            container.addView(
                    stage
            );
        }

        return container;
    }

    private int getStatusStage(
            String status
    ) {

        if ("pending".equals(status)) {
            return 0;
        }

        if ("accepted".equals(status)) {
            return 1;
        }

        if ("assigned".equals(status)) {
            return 2;
        }

        if ("in progress".equals(status)) {
            return 3;
        }

        if ("waiting for parts".equals(status)) {
            return 4;
        }

        if ("testing".equals(status)) {
            return 5;
        }

        if ("ready for collection".equals(status)) {
            return 6;
        }

        if ("completed".equals(status)) {
            return 7;
        }

        return 0;
    }

    private LinearLayout createImageSection(
            String customerImageUri,
            String progressImageUri
    ) {

        HorizontalScrollView scrollView =
                new HorizontalScrollView(this);

        scrollView.setHorizontalScrollBarEnabled(
                false
        );

        LinearLayout container =
                new LinearLayout(this);

        container.setOrientation(
                LinearLayout.HORIZONTAL
        );

        if (!isEmpty(customerImageUri)) {

            container.addView(
                    createImageCard(
                            "Customer Photo",
                            customerImageUri
                    )
            );
        }

        if (!isEmpty(progressImageUri)) {

            container.addView(
                    createImageCard(
                            "Repair Progress",
                            progressImageUri
                    )
            );
        }

        scrollView.addView(
                container
        );

        return createImageWrapper(
                scrollView
        );
    }

    private LinearLayout createImageWrapper(
            View view
    ) {

        LinearLayout wrapper =
                new LinearLayout(this);

        wrapper.setOrientation(
                LinearLayout.VERTICAL
        );

        wrapper.addView(view);

        return wrapper;
    }

    private LinearLayout createImageCard(
            String label,
            String imageUri
    ) {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        LinearLayout.LayoutParams boxParams =
                new LinearLayout.LayoutParams(
                        220,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        boxParams.setMargins(
                0,
                0,
                12,
                0
        );

        box.setLayoutParams(
                boxParams
        );

        ImageView imageView =
                new ImageView(this);

        imageView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        170
                )
        );

        imageView.setScaleType(
                ImageView.ScaleType.CENTER_CROP
        );

        imageView.setBackgroundColor(
                0xFFEAEAEA
        );

        loadImageIntoView(
                imageView,
                imageUri
        );

        box.addView(
                imageView
        );

        TextView labelView =
                createTextView(
                        label,
                        13,
                        false
                );

        labelView.setGravity(
                Gravity.CENTER
        );

        box.addView(
                labelView
        );

        return box;
    }
    private void loadImageIntoView(
            ImageView imageView,
            String imageLocation
    ) {

        if (imageView == null) {
            return;
        }

        if (imageLocation == null ||
                imageLocation.trim().isEmpty()) {

            imageView.setVisibility(
                    ImageView.GONE
            );

            return;
        }

        try {

            String cleanPath =
                    imageLocation.trim();

            java.io.File imageFile =
                    new java.io.File(
                            cleanPath
                    );

            if (imageFile.exists() &&
                    imageFile.isFile()) {

                android.graphics.Bitmap bitmap =
                        android.graphics.BitmapFactory
                                .decodeFile(
                                        imageFile.getAbsolutePath()
                                );

                if (bitmap != null) {

                    imageView.setImageBitmap(
                            bitmap
                    );

                    imageView.setVisibility(
                            ImageView.VISIBLE
                    );

                    return;
                }
            }

            Uri imageUri =
                    Uri.parse(
                            cleanPath
                    );

            if ("content".equalsIgnoreCase(
                    imageUri.getScheme()
            ) ||
                    "file".equalsIgnoreCase(
                            imageUri.getScheme()
                    )) {

                imageView.setImageURI(
                        imageUri
                );

                imageView.setVisibility(
                        ImageView.VISIBLE
                );

                return;
            }

        } catch (Exception e) {

            android.util.Log.e(
                    "RepairTrackingActivity",
                    "Failed to load image: "
                            + imageLocation,
                    e
            );
        }

        imageView.setVisibility(
                ImageView.GONE
        );
    }

    private void showCancelConfirmation(
            int repairId
    ) {

        new AlertDialog.Builder(this)

                .setTitle(
                        "Cancel Repair Booking"
                )

                .setMessage(
                        "Are you sure you want to cancel Repair #"
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
                                cancelRepair(
                                        repairId
                                )
                )

                .show();
    }

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

        } else {

            Toast.makeText(
                    this,
                    "Unable to cancel this repair.",
                    Toast.LENGTH_LONG
            ).show();
        }

        loadRepairs();
    }

    private String getColumnValue(
            Cursor cursor,
            String columnName
    ) {

        int index =
                cursor.getColumnIndex(
                        columnName
                );

        if (index == -1 ||
                cursor.isNull(index)) {

            return null;
        }

        return cursor.getString(index);
    }
    private String safeString(
            String value
    ) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not available";
        }

        return value;
    }

    private boolean isEmpty(
            String value
    ) {

        return value == null ||
                value.trim().isEmpty();
    }

    private void showNoRepairsMessage(
            String message
    ) {

        tvNoRepairs.setText(
                message
        );

        tvNoRepairs.setVisibility(
                View.VISIBLE
        );
    }
    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}