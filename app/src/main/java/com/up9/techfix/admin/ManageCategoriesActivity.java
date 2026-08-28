package com.up9.techfix.admin;


import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;

import com.up9.techfix.R;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoriesActivity
        extends AppCompatActivity
        implements CategoryAdapter.OnCategoryActionListener {

    private RecyclerView recyclerCategories;

    private Button btnAddCategory;

    private CategoryAdapter categoryAdapter;

    private List<DeviceCategory> categoryList;

    private int editingPosition = -1;

    private final ActivityResultLauncher<Intent>
            categoryFormLauncher =
            registerForActivityResult(
                    new ActivityResultContracts
                            .StartActivityForResult(),
                    result -> {

                        if (result.getResultCode()
                                != RESULT_OK) {

                            return;
                        }

                        Intent data =
                                result.getData();

                        if (data == null) {
                            return;
                        }

                        String name =
                                data.getStringExtra(
                                        "name"
                                );

                        String description =
                                data.getStringExtra(
                                        "description"
                                );

                        if (editingPosition == -1) {

                            int newId =
                                    categoryList.size()
                                            + 1;

                            DeviceCategory category =
                                    new DeviceCategory(
                                            newId,
                                            name,
                                            description
                                    );

                            categoryList.add(
                                    category
                            );

                            categoryAdapter
                                    .notifyItemInserted(
                                            categoryList.size() - 1
                                    );

                        } else {

                            DeviceCategory category =
                                    categoryList.get(
                                            editingPosition
                                    );

                            category.setName(name);

                            category.setDescription(
                                    description
                            );

                            categoryAdapter
                                    .notifyItemChanged(
                                            editingPosition
                                    );

                            editingPosition = -1;
                        }
                    }
            );

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(
                savedInstanceState
        );

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

        recyclerCategories.setLayoutManager(
                new LinearLayoutManager(this)
        );

        categoryList =
                new ArrayList<>();

        loadSampleCategories();

        categoryAdapter =
                new CategoryAdapter(
                        categoryList,
                        this
                );

        recyclerCategories.setAdapter(
                categoryAdapter
        );

        btnAddCategory.setOnClickListener(
                v -> {

                    editingPosition = -1;

                    Intent intent =
                            new Intent(
                                    this,
                                    CategoryFormActivity.class
                            );

                    categoryFormLauncher.launch(
                            intent
                    );
                }
        );
    }

    private void loadSampleCategories() {

        categoryList.add(
                new DeviceCategory(
                        1,
                        "Mobile Phone",
                        "Mobile phone repair services"
                )
        );

        categoryList.add(
                new DeviceCategory(
                        2,
                        "Laptop",
                        "Laptop repair and maintenance"
                )
        );

        categoryList.add(
                new DeviceCategory(
                        3,
                        "Desktop",
                        "Desktop computer repair services"
                )
        );

        categoryList.add(
                new DeviceCategory(
                        4,
                        "Tablet",
                        "Tablet repair services"
                )
        );
    }

    @Override
    public void onEdit(
            DeviceCategory category
    ) {

        editingPosition =
                categoryList.indexOf(
                        category
                );

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

        categoryFormLauncher.launch(
                intent
        );
    }

    @Override
    public void onDelete(
            DeviceCategory category,
            int position
    ) {

        new AlertDialog.Builder(this)
                .setTitle(
                        "Delete Category"
                )
                .setMessage(
                        "Are you sure you want to delete "
                                + category.getName()
                                + "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            categoryList.remove(
                                    position
                            );

                            categoryAdapter
                                    .notifyItemRemoved(
                                            position
                                    );
                        }
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .show();
    }
}