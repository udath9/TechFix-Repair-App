package com.up9.techfix.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class ServiceFormActivity extends AppCompatActivity {

    private TextView txtServiceFormTitle;

    private EditText edtServiceName;
    private EditText edtServiceDescription;
    private EditText edtServicePrice;
    private EditText edtEstimatedDays;

    private Spinner spinnerCategory;

    private Button btnSaveService;
    private Button btnCancelService;

    private boolean isEditMode = false;

    private String[] categories = {
            "Mobile Phone",
            "Laptop",
            "Desktop",
            "Tablet"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.service_form);

        txtServiceFormTitle =
                findViewById(R.id.txtServiceFormTitle);

        edtServiceName =
                findViewById(R.id.edtServiceName);

        edtServiceDescription =
                findViewById(R.id.edtServiceDescription);

        edtServicePrice =
                findViewById(R.id.edtServicePrice);

        edtEstimatedDays =
                findViewById(R.id.edtEstimatedDays);

        spinnerCategory =
                findViewById(R.id.spinnerCategory);

        btnSaveService =
                findViewById(R.id.btnSaveService);

        btnCancelService =
                findViewById(R.id.btnCancelService);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categories
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerCategory.setAdapter(adapter);

        checkEditMode();

        btnSaveService.setOnClickListener(
                v -> saveService()
        );

        btnCancelService.setOnClickListener(
                v -> finish()
        );
    }

    private void checkEditMode() {

        Intent intent = getIntent();

        isEditMode =
                intent.getBooleanExtra(
                        "editMode",
                        false
                );

        if (isEditMode) {

            txtServiceFormTitle.setText(
                    "Edit Repair Service"
            );

            edtServiceName.setText(
                    intent.getStringExtra("name")
            );

            edtServiceDescription.setText(
                    intent.getStringExtra("description")
            );

            edtServicePrice.setText(
                    String.valueOf(
                            intent.getDoubleExtra(
                                    "price",
                                    0
                            )
                    )
            );

            edtEstimatedDays.setText(
                    String.valueOf(
                            intent.getIntExtra(
                                    "estimatedDays",
                                    1
                            )
                    )
            );

            String selectedCategory =
                    intent.getStringExtra(
                            "category"
                    );

            if (selectedCategory != null) {

                for (int i = 0; i < categories.length; i++) {

                    if (categories[i].equals(
                            selectedCategory
                    )) {

                        spinnerCategory.setSelection(i);

                        break;
                    }
                }
            }
        }
    }

    private void saveService() {

        String name =
                edtServiceName
                        .getText()
                        .toString()
                        .trim();

        String category =
                spinnerCategory
                        .getSelectedItem()
                        .toString();

        String description =
                edtServiceDescription
                        .getText()
                        .toString()
                        .trim();

        String priceText =
                edtServicePrice
                        .getText()
                        .toString()
                        .trim();

        String daysText =
                edtEstimatedDays
                        .getText()
                        .toString()
                        .trim();

        if (name.isEmpty()) {

            edtServiceName.setError(
                    "Enter service name"
            );

            edtServiceName.requestFocus();

            return;
        }

        if (description.isEmpty()) {

            edtServiceDescription.setError(
                    "Enter description"
            );

            edtServiceDescription.requestFocus();

            return;
        }

        if (priceText.isEmpty()) {

            edtServicePrice.setError(
                    "Enter repair price"
            );

            edtServicePrice.requestFocus();

            return;
        }

        if (daysText.isEmpty()) {

            edtEstimatedDays.setError(
                    "Enter estimated repair days"
            );

            edtEstimatedDays.requestFocus();

            return;
        }

        double price;

        int estimatedDays;

        try {

            price =
                    Double.parseDouble(priceText);

            estimatedDays =
                    Integer.parseInt(daysText);

        } catch (NumberFormatException e) {

            edtServicePrice.setError(
                    "Enter valid numbers"
            );

            return;
        }

        if (price <= 0) {

            edtServicePrice.setError(
                    "Price must be greater than 0"
            );

            return;
        }

        if (estimatedDays <= 0) {

            edtEstimatedDays.setError(
                    "Days must be greater than 0"
            );

            return;
        }

        Intent resultIntent =
                new Intent();

        resultIntent.putExtra(
                "name",
                name
        );

        resultIntent.putExtra(
                "category",
                category
        );

        resultIntent.putExtra(
                "description",
                description
        );

        resultIntent.putExtra(
                "price",
                price
        );

        resultIntent.putExtra(
                "estimatedDays",
                estimatedDays
        );

        if (isEditMode) {

            resultIntent.putExtra(
                    "serviceId",
                    getIntent().getIntExtra(
                            "serviceId",
                            -1
                    )
            );
        }

        setResult(
                RESULT_OK,
                resultIntent
        );

        finish();
    }
}