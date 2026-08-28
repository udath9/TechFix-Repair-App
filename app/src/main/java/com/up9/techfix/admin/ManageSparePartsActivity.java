package com.up9.techfix.admin;
import com.up9.techfix.R;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class ManageSparePartsActivity
        extends AppCompatActivity
        implements SparePartAdapter.OnSparePartActionListener {

    private RecyclerView recyclerSpareParts;

    private Button btnAddSparePart;

    private SparePartAdapter sparePartAdapter;

    private List<SparePart> sparePartList;

    private int editingPosition = -1;

    private final ActivityResultLauncher<Intent>
            sparePartFormLauncher =
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

                            int newId =
                                    sparePartList.size()
                                            + 1;

                            SparePart sparePart =
                                    new SparePart(
                                            newId,
                                            partName,
                                            partCode,
                                            category,
                                            quantity,
                                            unitPrice,
                                            supplier
                                    );

                            sparePartList.add(
                                    sparePart
                            );

                            sparePartAdapter
                                    .notifyItemInserted(
                                            sparePartList.size() - 1
                                    );

                        } else {

                            SparePart sparePart =
                                    sparePartList.get(
                                            editingPosition
                                    );

                            sparePart.setPartName(
                                    partName
                            );

                            sparePart.setPartCode(
                                    partCode
                            );

                            sparePart.setCategory(
                                    category
                            );

                            sparePart.setQuantity(
                                    quantity
                            );

                            sparePart.setUnitPrice(
                                    unitPrice
                            );

                            sparePart.setSupplier(
                                    supplier
                            );

                            sparePartAdapter
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

        recyclerSpareParts.setLayoutManager(
                new LinearLayoutManager(this)
        );

        sparePartList =
                new ArrayList<>();

        loadSampleSpareParts();

        sparePartAdapter =
                new SparePartAdapter(
                        sparePartList,
                        this
                );

        recyclerSpareParts.setAdapter(
                sparePartAdapter
        );

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

    private void loadSampleSpareParts() {

        sparePartList.add(
                new SparePart(
                        1,
                        "iPhone 13 Display",
                        "IP13-DISPLAY",
                        "Mobile Phone",
                        8,
                        35000,
                        "ABC Electronics"
                )
        );

        sparePartList.add(
                new SparePart(
                        2,
                        "Samsung A54 Battery",
                        "SA54-BAT",
                        "Mobile Phone",
                        15,
                        8500,
                        "Mobile Parts Lanka"
                )
        );

        sparePartList.add(
                new SparePart(
                        3,
                        "Laptop SSD 512GB",
                        "SSD-512",
                        "Laptop",
                        5,
                        18000,
                        "TechWorld Lanka"
                )
        );

        sparePartList.add(
                new SparePart(
                        4,
                        "Laptop Keyboard",
                        "LAP-KEY-01",
                        "Laptop",
                        0,
                        12000,
                        "Computer Parts LK"
                )
        );
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
                sparePart.getPartName()
        );

        intent.putExtra(
                "partCode",
                sparePart.getPartCode()
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
                                + sparePart.getPartName()
                                + "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            sparePartList.remove(
                                    position
                            );

                            sparePartAdapter
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