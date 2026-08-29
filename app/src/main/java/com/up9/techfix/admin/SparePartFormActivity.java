package com.up9.techfix.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

import java.util.ArrayList;
import java.util.List;

public class SparePartFormActivity extends AppCompatActivity {

    private EditText edtPartName;
    private EditText edtPartCode;
    private EditText edtPartQuantity;
    private EditText edtMinimumStock;
    private EditText edtPartPrice;
    private EditText edtPartSupplier;

    private Spinner spinnerPartCategory;

    private Button btnSaveSparePart;
    private Button btnCancelSparePart;

    private boolean isEditMode = false;

    private TechFixDatabaseHelper databaseHelper;

    private ArrayAdapter<String> categoryAdapter;

    private final List<String> categoryNames =
            new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.spare_part_form
        );

        // Database
        databaseHelper =
                new TechFixDatabaseHelper(this);


        // Find views
        edtPartName =
                findViewById(
                        R.id.edtPartName
                );

        edtPartCode =
                findViewById(
                        R.id.edtPartCode
                );

        edtPartQuantity =
                findViewById(
                        R.id.edtPartQuantity
                );

        edtMinimumStock =
                findViewById(
                        R.id.edtMinimumStock
                );

        edtPartPrice =
                findViewById(
                        R.id.edtPartPrice
                );

        edtPartSupplier =
                findViewById(
                        R.id.edtPartSupplier
                );

        spinnerPartCategory =
                findViewById(
                        R.id.spinnerPartCategory
                );

        btnSaveSparePart =
                findViewById(
                        R.id.btnSaveSparePart
                );

        btnCancelSparePart =
                findViewById(
                        R.id.btnCancelSparePart
                );


        // Setup category spinner
        setupCategorySpinner();


        // Check whether this is Add or Edit
        checkEditMode();


        // Save button
        btnSaveSparePart.setOnClickListener(
                v -> saveSparePart()
        );


        // Cancel button
        btnCancelSparePart.setOnClickListener(
                v -> finish()
        );
    }


    /**
     * Load categories from SQLite database
     */
    private void setupCategorySpinner() {

        categoryNames.clear();

        List<DeviceCategory> categories =
                databaseHelper.getAllCategories();


        for (DeviceCategory category : categories) {

            categoryNames.add(
                    category.getName()
            );
        }


        categoryAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categoryNames
                );


        categoryAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );


        spinnerPartCategory.setAdapter(
                categoryAdapter
        );


        // If there are no categories
        if (categoryNames.isEmpty()) {

            Toast.makeText(
                    this,
                    "No categories found. Please add a category first.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    /**
     * Check whether the form is in edit mode
     */
    private void checkEditMode() {

        Intent intent =
                getIntent();


        isEditMode =
                intent.getBooleanExtra(
                        "editMode",
                        false
                );


        if (!isEditMode) {

            setTitle(
                    "Add Spare Part"
            );

            return;
        }


        setTitle(
                "Edit Spare Part"
        );


        edtPartName.setText(
                intent.getStringExtra(
                        "partName"
                )
        );


        edtPartCode.setText(
                intent.getStringExtra(
                        "partCode"
                )
        );


        edtPartQuantity.setText(
                String.valueOf(
                        intent.getIntExtra(
                                "quantity",
                                0
                        )
                )
        );


        edtMinimumStock.setText(
                String.valueOf(
                        intent.getIntExtra(
                                "minimumStock",
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
                intent.getStringExtra(
                        "supplier"
                )
        );


        String category =
                intent.getStringExtra(
                        "category"
                );


        setSpinnerValue(
                category
        );
    }


    /**
     * Select the existing category when editing
     */
    private void setSpinnerValue(
            String value
    ) {

        if (value == null) {
            return;
        }


        for (
                int i = 0;
                i < categoryNames.size();
                i++
        ) {

            if (
                    categoryNames
                            .get(i)
                            .equals(value)
            ) {

                spinnerPartCategory.setSelection(
                        i
                );

                break;
            }
        }
    }


    /**
     * Save spare part
     */
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


        String minimumStockText =
                edtMinimumStock
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


        // Check category
        if (
                spinnerPartCategory
                        .getSelectedItem() == null
        ) {

            Toast.makeText(
                    this,
                    "Please select a category",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        String category =
                spinnerPartCategory
                        .getSelectedItem()
                        .toString();


        // Validate part name
        if (partName.isEmpty()) {

            edtPartName.setError(
                    "Enter part name"
            );

            edtPartName.requestFocus();

            return;
        }


        // Validate part code
        if (partCode.isEmpty()) {

            edtPartCode.setError(
                    "Enter part code"
            );

            edtPartCode.requestFocus();

            return;
        }


        // Validate quantity
        if (quantityText.isEmpty()) {

            edtPartQuantity.setError(
                    "Enter quantity"
            );

            edtPartQuantity.requestFocus();

            return;
        }


        // Validate minimum stock
        if (minimumStockText.isEmpty()) {

            edtMinimumStock.setError(
                    "Enter minimum stock"
            );

            edtMinimumStock.requestFocus();

            return;
        }


        // Validate price
        if (priceText.isEmpty()) {

            edtPartPrice.setError(
                    "Enter unit price"
            );

            edtPartPrice.requestFocus();

            return;
        }


        // Validate supplier
        if (supplier.isEmpty()) {

            edtPartSupplier.setError(
                    "Enter supplier"
            );

            edtPartSupplier.requestFocus();

            return;
        }


        int quantity;
        int minimumStock;
        double price;


        try {

            quantity =
                    Integer.parseInt(
                            quantityText
                    );


            minimumStock =
                    Integer.parseInt(
                            minimumStockText
                    );


            price =
                    Double.parseDouble(
                            priceText
                    );

        } catch (
                NumberFormatException e
        ) {

            Toast.makeText(
                    this,
                    "Please enter valid numbers",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // Quantity validation
        if (quantity < 0) {

            edtPartQuantity.setError(
                    "Quantity cannot be negative"
            );

            edtPartQuantity.requestFocus();

            return;
        }


        // Minimum stock validation
        if (minimumStock < 0) {

            edtMinimumStock.setError(
                    "Minimum stock cannot be negative"
            );

            edtMinimumStock.requestFocus();

            return;
        }


        // Price validation
        if (price <= 0) {

            edtPartPrice.setError(
                    "Price must be greater than 0"
            );

            edtPartPrice.requestFocus();

            return;
        }


        // Create result intent
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
                "minimumStock",
                minimumStock
        );


        resultIntent.putExtra(
                "unitPrice",
                price
        );


        resultIntent.putExtra(
                "supplier",
                supplier
        );


        // If editing, return ID
        if (isEditMode) {

            resultIntent.putExtra(
                    "partId",
                    getIntent().getIntExtra(
                            "partId",
                            -1
                    )
            );
        }


        // Return result
        setResult(
                RESULT_OK,
                resultIntent
        );


        finish();
    }
}