package com.up9.techfix.admin.categories;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class CategoryFormActivity extends AppCompatActivity {

    private EditText edtCategoryName;
    private EditText edtCategoryDescription;
    private EditText edtPriceModifier;

    private Button btnSaveCategory;
    private Button btnCancelCategory;

    private boolean editMode = false;
    private int categoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.category_form
        );

        edtCategoryName =
                findViewById(
                        R.id.edtCategoryName
                );

        edtCategoryDescription =
                findViewById(
                        R.id.edtCategoryDescription
                );

        edtPriceModifier =
                findViewById(
                        R.id.edtPriceModifier
                );

        btnSaveCategory =
                findViewById(
                        R.id.btnSaveCategory
                );

        btnCancelCategory =
                findViewById(
                        R.id.btnCancelCategory
                );


        editMode =
                getIntent().getBooleanExtra(
                        "editMode",
                        false
                );

        if (editMode) {

            categoryId =
                    getIntent().getIntExtra(
                            "categoryId",
                            -1
                    );

            String name =
                    getIntent().getStringExtra(
                            "name"
                    );

            String description =
                    getIntent().getStringExtra(
                            "description"
                    );

            double priceModifier =
                    getIntent().getDoubleExtra(
                            "priceModifier",
                            1.0
                    );

            edtCategoryName.setText(
                    name
            );

            edtCategoryDescription.setText(
                    description
            );

            edtPriceModifier.setText(
                    String.valueOf(
                            priceModifier
                    )
            );

            btnSaveCategory.setText(
                    "Update Category"
            );

        } else {

            edtPriceModifier.setText(
                    "1.0"
            );
        }


        btnSaveCategory.setOnClickListener(
                v -> saveCategory()
        );

        btnCancelCategory.setOnClickListener(
                v -> finish()
        );
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
                edtPriceModifier
                        .getText()
                        .toString()
                        .trim();

        if (TextUtils.isEmpty(name)) {

            edtCategoryName.setError(
                    "Enter category name"
            );

            edtCategoryName.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(modifierText)) {

            edtPriceModifier.setError(
                    "Enter price modifier"
            );

            edtPriceModifier.requestFocus();

            return;
        }

        double priceModifier;

        try {

            priceModifier =
                    Double.parseDouble(
                            modifierText
                    );

        } catch (NumberFormatException e) {

            edtPriceModifier.setError(
                    "Enter a valid number"
            );

            edtPriceModifier.requestFocus();

            return;
        }

        if (priceModifier <= 0) {

            edtPriceModifier.setError(
                    "Price modifier must be greater than 0"
            );

            edtPriceModifier.requestFocus();

            return;
        }

        getIntent().putExtra(
                "name",
                name
        );

        getIntent().putExtra(
                "description",
                description
        );

        getIntent().putExtra(
                "priceModifier",
                priceModifier
        );

        getIntent().putExtra(
                "editMode",
                editMode
        );

        if (editMode) {

            getIntent().putExtra(
                    "categoryId",
                    categoryId
            );
        }

        setResult(
                RESULT_OK,
                getIntent()
        );

        Toast.makeText(
                this,
                editMode
                        ? "Category ready to update"
                        : "Category ready to save",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }
}