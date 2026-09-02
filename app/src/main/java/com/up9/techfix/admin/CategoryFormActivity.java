package com.up9.techfix.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

import java.util.Locale;

public class CategoryFormActivity extends AppCompatActivity {

    private TextView txtCategoryFormTitle;

    private EditText edtCategoryName;
    private EditText edtCategoryDescription;
    private EditText edtCategoryPriceModifier;

    private Button btnSaveCategory;
    private Button btnCancelCategory;

    private boolean isEditMode = false;

    private int categoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.category_form
        );

        txtCategoryFormTitle =
                findViewById(
                        R.id.txtCategoryFormTitle
                );

        edtCategoryName =
                findViewById(
                        R.id.edtCategoryName
                );

        edtCategoryDescription =
                findViewById(
                        R.id.edtCategoryDescription
                );

        edtCategoryPriceModifier =
                findViewById(
                        R.id.edtCategoryPriceModifier
                );

        btnSaveCategory =
                findViewById(
                        R.id.btnSaveCategory
                );

        btnCancelCategory =
                findViewById(
                        R.id.btnCancelCategory
                );

        checkEditMode();

        btnSaveCategory.setOnClickListener(
                v -> saveCategory()
        );

        btnCancelCategory.setOnClickListener(v -> {

            setResult(
                    RESULT_CANCELED
            );

            finish();
        });
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

            categoryId =
                    intent.getIntExtra(
                            "categoryId",
                            -1
                    );

            txtCategoryFormTitle.setText(
                    "Edit Device Category"
            );

            btnSaveCategory.setText(
                    "Update Category"
            );

            edtCategoryName.setText(
                    intent.getStringExtra(
                            "name"
                    )
            );

            edtCategoryDescription.setText(
                    intent.getStringExtra(
                            "description"
                    )
            );

            double priceModifier =
                    intent.getDoubleExtra(
                            "priceModifier",
                            0.0
                    );

            edtCategoryPriceModifier.setText(
                    String.format(
                            Locale.getDefault(),
                            "%.2f",
                            priceModifier
                    )
            );

        } else {

            txtCategoryFormTitle.setText(
                    "Add Device Category"
            );

            btnSaveCategory.setText(
                    "Save Category"
            );
        }
    }

    private void saveCategory() {

        String name =
                edtCategoryName
                        .getText()
                        .toString()
                        .trim();

        String description =
                edtCategoryDescription
                        .getText()
                        .toString()
                        .trim();

        String modifierText =
                edtCategoryPriceModifier
                        .getText()
                        .toString()
                        .trim();

        if (name.isEmpty()) {

            edtCategoryName.setError(
                    "Enter category name"
            );

            edtCategoryName.requestFocus();

            return;
        }

        if (modifierText.isEmpty()) {

            edtCategoryPriceModifier.setError(
                    "Enter price modifier"
            );

            edtCategoryPriceModifier.requestFocus();

            return;
        }

        double priceModifier;

        try {

            priceModifier =
                    Double.parseDouble(
                            modifierText
                    );

        } catch (NumberFormatException e) {

            edtCategoryPriceModifier.setError(
                    "Enter a valid price modifier"
            );

            edtCategoryPriceModifier.requestFocus();

            return;
        }

        Intent resultIntent =
                new Intent();

        resultIntent.putExtra(
                "name",
                name
        );

        resultIntent.putExtra(
                "description",
                description
        );

        resultIntent.putExtra(
                "priceModifier",
                priceModifier
        );

        resultIntent.putExtra(
                "editMode",
                isEditMode
        );

        if (isEditMode) {

            if (categoryId == -1) {

                edtCategoryName.setError(
                        "Invalid category ID"
                );

                return;
            }

            resultIntent.putExtra(
                    "categoryId",
                    categoryId
            );
        }

        setResult(
                RESULT_OK,
                resultIntent
        );

        finish();
    }
}