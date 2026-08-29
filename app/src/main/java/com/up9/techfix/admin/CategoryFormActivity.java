package com.up9.techfix.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class CategoryFormActivity extends AppCompatActivity {

    private TextView txtCategoryFormTitle;

    private EditText edtCategoryName;
    private EditText edtCategoryDescription;

    private Button btnSaveCategory;
    private Button btnCancelCategory;

    private boolean isEditMode = false;

    private int categoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_form);

        txtCategoryFormTitle =
                findViewById(R.id.txtCategoryFormTitle);

        edtCategoryName =
                findViewById(R.id.edtCategoryName);

        edtCategoryDescription =
                findViewById(R.id.edtCategoryDescription);

        btnSaveCategory =
                findViewById(R.id.btnSaveCategory);

        btnCancelCategory =
                findViewById(R.id.btnCancelCategory);

        checkEditMode();

        btnSaveCategory.setOnClickListener(
                v -> saveCategory()
        );

        btnCancelCategory.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void checkEditMode() {

        Intent intent = getIntent();

        isEditMode = intent.getBooleanExtra(
                "editMode",
                false
        );

        if (isEditMode) {

            categoryId = intent.getIntExtra(
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
                    intent.getStringExtra("name")
            );

            edtCategoryDescription.setText(
                    intent.getStringExtra("description")
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

        if (name.isEmpty()) {

            edtCategoryName.setError(
                    "Enter category name"
            );

            edtCategoryName.requestFocus();

            return;
        }

        Intent resultIntent = new Intent();

        resultIntent.putExtra(
                "name",
                name
        );

        resultIntent.putExtra(
                "description",
                description
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