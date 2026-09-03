package com.up9.techfix.admin.spareparts;

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
import com.up9.techfix.admin.categories.DeviceCategory;
import com.up9.techfix.data.DatabaseHelper;

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

    private DatabaseHelper databaseHelper;

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

        databaseHelper =
                new DatabaseHelper(this);

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

        categoryNames.clear();

        List<DeviceCategory> categories =
                databaseHelper.getAllCategoryModels();


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


        if (categoryNames.isEmpty()) {

            Toast.makeText(
                    this,
                    "No categories found. Please add a category first.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

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

        if (minimumStockText.isEmpty()) {

            edtMinimumStock.setError(
                    "Enter minimum stock"
            );

            edtMinimumStock.requestFocus();

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

        if (quantity < 0) {

            edtPartQuantity.setError(
                    "Quantity cannot be negative"
            );

            edtPartQuantity.requestFocus();

            return;
        }


        if (minimumStock < 0) {

            edtMinimumStock.setError(
                    "Minimum stock cannot be negative"
            );

            edtMinimumStock.requestFocus();

            return;
        }

        if (price <= 0) {

            edtPartPrice.setError(
                    "Price must be greater than 0"
            );

            edtPartPrice.requestFocus();

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