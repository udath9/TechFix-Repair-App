package com.up9.techfix.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

import java.util.ArrayList;
import java.util.List;

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

    private int serviceId = -1;

    private TechFixDatabaseHelper databaseHelper;

    private ArrayAdapter<String> categoryAdapter;

    private List<DeviceCategory> categoryList;

    private List<String> categoryNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.service_form
        );

        databaseHelper =
                new TechFixDatabaseHelper(this);

        txtServiceFormTitle =
                findViewById(
                        R.id.txtServiceFormTitle
                );

        edtServiceName =
                findViewById(
                        R.id.edtServiceName
                );

        edtServiceDescription =
                findViewById(
                        R.id.edtServiceDescription
                );

        edtServicePrice =
                findViewById(
                        R.id.edtServicePrice
                );

        edtEstimatedDays =
                findViewById(
                        R.id.edtEstimatedDays
                );

        spinnerCategory =
                findViewById(
                        R.id.spinnerCategory
                );

        btnSaveService =
                findViewById(
                        R.id.btnSaveService
                );

        btnCancelService =
                findViewById(
                        R.id.btnCancelService
                );

        loadCategories();

        checkEditMode();

        btnSaveService.setOnClickListener(
                v -> saveService()
        );


        btnCancelService.setOnClickListener(
                v -> finish()
        );
    }


    private void loadCategories() {

        categoryList =
                databaseHelper.getAllCategories();


        categoryNames =
                new ArrayList<>();


        for (DeviceCategory category :
                categoryList) {

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


        spinnerCategory.setAdapter(
                categoryAdapter
        );


        // If no categories exist

        if (categoryNames.isEmpty()) {

            Toast.makeText(
                    this,
                    "No device categories found. Please add a category first.",
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


        if (isEditMode) {

            serviceId =
                    intent.getIntExtra(
                            "serviceId",
                            -1
                    );


            txtServiceFormTitle.setText(
                    "Edit Repair Service"
            );


            btnSaveService.setText(
                    "Update Service"
            );


            edtServiceName.setText(
                    intent.getStringExtra(
                            "name"
                    )
            );


            edtServiceDescription.setText(
                    intent.getStringExtra(
                            "description"
                    )
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

                for (
                        int i = 0;
                        i < categoryNames.size();
                        i++
                ) {

                    if (
                            categoryNames
                                    .get(i)
                                    .equals(
                                            selectedCategory
                                    )
                    ) {

                        spinnerCategory.setSelection(i);

                        break;
                    }
                }
            }

        } else {

            txtServiceFormTitle.setText(
                    "Add Repair Service"
            );


            btnSaveService.setText(
                    "Save Service"
            );
        }
    }


    private void saveService() {

        String name =
                edtServiceName
                        .getText()
                        .toString()
                        .trim();


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

        if (categoryNames.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please add a device category first.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        String category =
                spinnerCategory
                        .getSelectedItem()
                        .toString();

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
                    Double.parseDouble(
                            priceText
                    );


            estimatedDays =
                    Integer.parseInt(
                            daysText
                    );

        } catch (NumberFormatException e) {

            Toast.makeText(
                    this,
                    "Please enter valid numbers.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (price <= 0) {

            edtServicePrice.setError(
                    "Price must be greater than 0"
            );

            edtServicePrice.requestFocus();

            return;
        }


        if (estimatedDays <= 0) {

            edtEstimatedDays.setError(
                    "Days must be greater than 0"
            );

            edtEstimatedDays.requestFocus();

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


        resultIntent.putExtra(
                "editMode",
                isEditMode
        );


        if (isEditMode) {

            resultIntent.putExtra(
                    "serviceId",
                    serviceId
            );
        }


        setResult(
                RESULT_OK,
                resultIntent
        );


        finish();
    }
}