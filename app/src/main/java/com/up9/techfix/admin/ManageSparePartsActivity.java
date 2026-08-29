package com.up9.techfix.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.up9.techfix.R;

import java.util.ArrayList;
import java.util.List;

public class ManageSparePartsActivity
        extends AppCompatActivity
        implements SparePartAdapter.OnSparePartActionListener {

    private RecyclerView recyclerSpareParts;

    private Button btnAddSparePart;

    private SparePartAdapter sparePartAdapter;

    private List<SparePart> sparePartList;

    private TechFixDatabaseHelper databaseHelper;

    private int editingPosition = -1;

    private final ActivityResultLauncher<Intent>
            sparePartFormLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
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

                        saveSparePartToDatabase(data);
                    }
            );

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_manage_spare_parts
        );

        recyclerSpareParts =
                findViewById(
                        R.id.recyclerSpareParts
                );

        btnAddSparePart =
                findViewById(
                        R.id.btnAddSparePart
                );

        databaseHelper =
                new TechFixDatabaseHelper(this);

        recyclerSpareParts.setLayoutManager(
                new LinearLayoutManager(this)
        );

        sparePartList =
                new ArrayList<>();

        sparePartAdapter =
                new SparePartAdapter(
                        sparePartList,
                        this
                );

        recyclerSpareParts.setAdapter(
                sparePartAdapter
        );

        loadSpareParts();

        btnAddSparePart.setOnClickListener(
                v -> {

                    editingPosition = -1;

                    Intent intent =
                            new Intent(
                                    this,
                                    SparePartFormActivity.class
                            );

                    sparePartFormLauncher.launch(
                            intent
                    );
                }
        );
    }

    private void loadSpareParts() {

        sparePartList.clear();

        sparePartList.addAll(
                databaseHelper.getAllSpareParts()
        );

        sparePartAdapter.notifyDataSetChanged();
    }

    private void saveSparePartToDatabase(
            Intent data
    ) {

        String partName =
                data.getStringExtra(
                        "partName"
                );

        String partCode =
                data.getStringExtra(
                        "partCode"
                );

        String category =
                data.getStringExtra(
                        "category"
                );

        int quantity =
                data.getIntExtra(
                        "quantity",
                        0
                );

        int minimumStock =
                data.getIntExtra(
                        "minimumStock",
                        0
                );

        double unitPrice =
                data.getDoubleExtra(
                        "unitPrice",
                        0
                );

        String supplier =
                data.getStringExtra(
                        "supplier"
                );

        if (editingPosition == -1) {

            long result =
                    databaseHelper.insertSparePart(
                            partName,
                            category,
                            partCode,
                            quantity,
                            minimumStock,
                            unitPrice,
                            supplier,
                            "",
                            ""
                    );

            if (result != -1) {

                loadSpareParts();
            }

        } else {

            SparePart sparePart =
                    sparePartList.get(
                            editingPosition
                    );

            databaseHelper.updateSparePart(
                    sparePart.getId(),
                    partName,
                    category,
                    partCode,
                    quantity,
                    minimumStock,
                    unitPrice,
                    supplier,
                    sparePart.getDescription(),
                    sparePart.getImageUri()
            );

            editingPosition = -1;

            loadSpareParts();
        }
    }

    @Override
    public void onEdit(
            SparePart sparePart
    ) {

        editingPosition =
                sparePartList.indexOf(
                        sparePart
                );

        Intent intent =
                new Intent(
                        this,
                        SparePartFormActivity.class
                );

        intent.putExtra(
                "editMode",
                true
        );

        intent.putExtra(
                "partId",
                sparePart.getId()
        );

        intent.putExtra(
                "partName",
                sparePart.getName()
        );

        intent.putExtra(
                "partCode",
                sparePart.getPartNumber()
        );

        intent.putExtra(
                "category",
                sparePart.getCategory()
        );

        intent.putExtra(
                "quantity",
                sparePart.getQuantity()
        );

        intent.putExtra(
                "minimumStock",
                sparePart.getMinimumStock()
        );

        intent.putExtra(
                "unitPrice",
                sparePart.getUnitPrice()
        );

        intent.putExtra(
                "supplier",
                sparePart.getSupplier()
        );

        sparePartFormLauncher.launch(
                intent
        );
    }

    @Override
    public void onDelete(
            SparePart sparePart,
            int position
    ) {

        new AlertDialog.Builder(this)
                .setTitle(
                        "Delete Spare Part"
                )
                .setMessage(
                        "Are you sure you want to delete "
                                + sparePart.getName()
                                + "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            databaseHelper.deleteSparePart(
                                    sparePart.getId()
                            );

                            loadSpareParts();
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

        if (databaseHelper != null
                && sparePartAdapter != null) {

            loadSpareParts();
        }
    }
}