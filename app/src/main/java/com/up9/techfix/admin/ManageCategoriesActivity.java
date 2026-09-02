package com.up9.techfix.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;

import java.util.List;

public class ManageCategoriesActivity extends AppCompatActivity
        implements CategoryAdapter.OnCategoryActionListener {

    private RecyclerView recyclerCategories;
    private Button btnAddCategory;

    private CategoryAdapter categoryAdapter;
    private List<DeviceCategory> categoryList;

    private TechFixDatabaseHelper databaseHelper;


    private final ActivityResultLauncher<Intent> categoryFormLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() != RESULT_OK) {
                            return;
                        }

                        Intent data = result.getData();

                        if (data == null) {
                            return;
                        }

                        String name =
                                data.getStringExtra("name");

                        String description =
                                data.getStringExtra("description");

                        double priceModifier =
                                data.getDoubleExtra(
                                        "priceModifier",
                                        0.0
                                );

                        boolean editMode =
                                data.getBooleanExtra(
                                        "editMode",
                                        false
                                );

                        int categoryId =
                                data.getIntExtra(
                                        "categoryId",
                                        -1
                                );


                        // UPDATE
                        if (editMode && categoryId != -1) {

                            int updateResult =
                                    databaseHelper.updateCategory(
                                            categoryId,
                                            name,
                                            description,
                                            priceModifier
                                    );

                            if (updateResult > 0) {

                                Toast.makeText(
                                        this,
                                        "Category updated successfully",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Failed to update category",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        }

                        // INSERT
                        else {

                            long newId =
                                    databaseHelper.insertCategory(
                                            name,
                                            description,
                                            priceModifier
                                    );

                            if (newId != -1) {

                                Toast.makeText(
                                        this,
                                        "Category added successfully",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Failed to add category",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }

                        loadCategories();
                    }
            );


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_manage_categories
        );

        recyclerCategories =
                findViewById(
                        R.id.recyclerCategories
                );

        btnAddCategory =
                findViewById(
                        R.id.btnAddCategory
                );

        databaseHelper =
                new TechFixDatabaseHelper(this);


        recyclerCategories.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadCategories();


        btnAddCategory.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            ManageCategoriesActivity.this,
                            CategoryFormActivity.class
                    );

            intent.putExtra(
                    "editMode",
                    false
            );

            categoryFormLauncher.launch(intent);
        });
    }


    private void loadCategories() {

        categoryList =
                databaseHelper.getAllCategories();

        categoryAdapter =
                new CategoryAdapter(
                        categoryList,
                        this
                );

        recyclerCategories.setAdapter(
                categoryAdapter
        );
    }


    @Override
    public void onEdit(DeviceCategory category) {

        Intent intent =
                new Intent(
                        this,
                        CategoryFormActivity.class
                );

        intent.putExtra(
                "editMode",
                true
        );

        intent.putExtra(
                "categoryId",
                category.getId()
        );

        intent.putExtra(
                "name",
                category.getName()
        );

        intent.putExtra(
                "description",
                category.getDescription()
        );

        intent.putExtra(
                "priceModifier",
                category.getPriceModifier()
        );

        categoryFormLauncher.launch(intent);
    }


    @Override
    public void onDelete(
            DeviceCategory category,
            int position
    ) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Category")
                .setMessage(
                        "Are you sure you want to delete "
                                + category.getName()
                                + "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            int result =
                                    databaseHelper.deleteCategory(
                                            category.getId()
                                    );

                            if (result > 0) {

                                Toast.makeText(
                                        this,
                                        "Category deleted successfully",
                                        Toast.LENGTH_SHORT
                                ).show();

                                loadCategories();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Failed to delete category",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .show();
    }


    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {
            loadCategories();
        }
    }
}