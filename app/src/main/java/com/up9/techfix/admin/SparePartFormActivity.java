package com.up9.techfix.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class SparePartFormActivity extends AppCompatActivity {

    private EditText edtPartName;
    private EditText edtPartCode;
    private EditText edtPartQuantity;
    private EditText edtPartPrice;
    private EditText edtPartSupplier;

    private Spinner spinnerPartCategory;

    private Button btnSaveSparePart;
    private Button btnCancelSparePart;

    private boolean isEditMode = false;

    private final String[] categories = {
            "Mobile Phone",
            "Laptop",
            "Desktop",
            "Tablet"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spare_part_form);

        edtPartName =
                findViewById(R.id.edtPartName);

        edtPartCode =
                findViewById(R.id.edtPartCode);

        edtPartQuantity =
                findViewById(R.id.edtPartQuantity);

        edtPartPrice =
                findViewById(R.id.edtPartPrice);

        edtPartSupplier =
                findViewById(R.id.edtPartSupplier);

        spinnerPartCategory =
                findViewById(R.id.spinnerPartCategory);

        btnSaveSparePart =
                findViewById(R.id.btnSaveSparePart);

        btnCancelSparePart =
                findViewById(R.id.btnCancelSparePart);

        setupCategorySpinner();

        checkEditMode();

        btnSaveSparePart.setOnClickListener(
                v -> saveSparePart()
        );

        btnCancelSparePart.setOnClickListener(
                v -> finish()
        );
    }

    private void setupCategorySpinner() {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categories
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerPartCategory.setAdapter(adapter);
    }

    private void checkEditMode() {

        Intent intent = getIntent();

        isEditMode =
                intent.getBooleanExtra(
                        "editMode",
                        false
                );

        if (!isEditMode) {
            return;
        }

        setTitle("Edit Spare Part");

        edtPartName.setText(
                intent.getStringExtra("partName")
        );

        edtPartCode.setText(
                intent.getStringExtra("partCode")
        );

        edtPartQuantity.setText(
                String.valueOf(
                        intent.getIntExtra(
                                "quantity",
                                0
                        )
                )
        );

        edtPartPrice.setText(
                String.valueOf(
                        intent.getDoubleExtra(
                                "unitPrice",
                                0
                        )
                )
        );

        edtPartSupplier.setText(
                intent.getStringExtra("supplier")
        );

        String category =
                intent.getStringExtra(
                        "category"
                );

        setSpinnerValue(
                category
        );
    }

    private void setSpinnerValue(
            String value
    ) {

        if (value == null) {
            return;
        }

        for (int i = 0;
             i < categories.length;
             i++) {

            if (categories[i].equals(value)) {

                spinnerPartCategory
                        .setSelection(i);

                break;
            }
        }
    }

    private void saveSparePart() {

        String partName =
                edtPartName
                        .getText()
                        .toString()
                        .trim();

        String partCode =
                edtPartCode
                        .getText()
                        .toString()
                        .trim()
                        .toUpperCase();

        String quantityText =
                edtPartQuantity
                        .getText()
                        .toString()
                        .trim();

        String priceText =
                edtPartPrice
                        .getText()
                        .toString()
                        .trim();

        String supplier =
                edtPartSupplier
                        .getText()
                        .toString()
                        .trim();

        String category =
                spinnerPartCategory
                        .getSelectedItem()
                        .toString();

        if (partName.isEmpty()) {

            edtPartName.setError(
                    "Enter part name"
            );

            edtPartName.requestFocus();

            return;
        }

        if (partCode.isEmpty()) {

            edtPartCode.setError(
                    "Enter part code"
            );

            edtPartCode.requestFocus();

            return;
        }

        if (quantityText.isEmpty()) {

            edtPartQuantity.setError(
                    "Enter quantity"
            );

            edtPartQuantity.requestFocus();

            return;
        }

        if (priceText.isEmpty()) {

            edtPartPrice.setError(
                    "Enter unit price"
            );

            edtPartPrice.requestFocus();

            return;
        }

        if (supplier.isEmpty()) {

            edtPartSupplier.setError(
                    "Enter supplier"
            );

            edtPartSupplier.requestFocus();

            return;
        }

        int quantity;
        double price;

        try {

            quantity =
                    Integer.parseInt(
                            quantityText
                    );

            price =
                    Double.parseDouble(
                            priceText
                    );

        } catch (NumberFormatException e) {

            edtPartQuantity.setError(
                    "Enter valid numbers"
            );

            return;
        }

        if (quantity < 0) {

            edtPartQuantity.setError(
                    "Quantity cannot be negative"
            );

            return;
        }

        if (price <= 0) {

            edtPartPrice.setError(
                    "Price must be greater than 0"
            );

            return;
        }

        Intent resultIntent =
                new Intent();

        resultIntent.putExtra(
                "partName",
                partName
        );

        resultIntent.putExtra(
                "partCode",
                partCode
        );

        resultIntent.putExtra(
                "category",
                category
        );

        resultIntent.putExtra(
                "quantity",
                quantity
        );

        resultIntent.putExtra(
                "unitPrice",
                price
        );

        resultIntent.putExtra(
                "supplier",
                supplier
        );

        if (isEditMode) {

            resultIntent.putExtra(
                    "partId",
                    getIntent().getIntExtra(
                            "partId",
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