package com.up9.techfix.admin;
import com.up9.techfix.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ManageRepairSamplesActivity
        extends AppCompatActivity
        implements RepairSampleAdapter.OnSampleActionListener {

    private RecyclerView recyclerRepairSamples;

    private Spinner spinnerSampleFilter;

    private RepairSampleAdapter sampleAdapter;

    private List<RepairSample> sampleList;

    private List<RepairSample> filteredList;

    private final String[] filters = {
            "All",
            "Mobile Phone",
            "Laptop",
            "Desktop Computer",
            "Tablet",
            "Other"
    };

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_manage_repair_samples
        );

        recyclerRepairSamples =
                findViewById(
                        R.id.recyclerRepairSamples
                );

        spinnerSampleFilter =
                findViewById(
                        R.id.spinnerSampleFilter
                );

        findViewById(
                R.id.btnAddRepairSample
        ).setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    this,
                                    AddEditRepairSampleActivity.class
                            );

                    startActivity(intent);
                }
        );

        recyclerRepairSamples.setLayoutManager(
                new LinearLayoutManager(this)
        );

        sampleList =
                new ArrayList<>();

        filteredList =
                new ArrayList<>();

        loadSampleData();

        setupFilter();

        sampleAdapter =
                new RepairSampleAdapter(
                        filteredList,
                        this
                );

        recyclerRepairSamples.setAdapter(
                sampleAdapter
        );

        applyFilter();
    }

    private void loadSampleData() {

        sampleList.add(
                new RepairSample(
                        1,
                        "iPhone 13",
                        "Mobile Phone",
                        "Screen Replacement",
                        "Damaged display replaced successfully.",
                        R.drawable.iphone_repair
                )
        );

        sampleList.add(
                new RepairSample(
                        2,
                        "Dell Inspiron 15",
                        "Laptop",
                        "Laptop Repair",
                        "Laptop motherboard and cooling system repaired.",
                        R.drawable.laptop_repair
                )
        );

        sampleList.add(
                new RepairSample(
                        3,
                        "Samsung Galaxy A54",
                        "Mobile Phone",
                        "Battery Replacement",
                        "Old battery replaced with a new battery.",
                        R.drawable.samsung_repair
                )
        );
    }

    private void setupFilter() {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        filters
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerSampleFilter.setAdapter(
                adapter
        );

        spinnerSampleFilter
                .setOnItemSelectedListener(
                        new android.widget.AdapterView
                                .OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(
                                    android.widget.AdapterView<?> parent,
                                    android.view.View view,
                                    int position,
                                    long id
                            ) {

                                applyFilter();
                            }

                            @Override
                            public void onNothingSelected(
                                    android.widget.AdapterView<?> parent
                            ) {

                            }
                        }
                );
    }

    private void applyFilter() {

        filteredList.clear();

        String selected =
                spinnerSampleFilter
                        .getSelectedItem()
                        .toString();

        for (RepairSample sample :
                sampleList) {

            if (selected.equals("All")
                    || sample
                    .getCategory()
                    .equals(selected)) {

                filteredList.add(sample);
            }
        }

        if (sampleAdapter != null) {

            sampleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEdit(
            RepairSample sample
    ) {

        Toast.makeText(
                this,
                "Edit sample #" + sample.getId(),
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onDelete(
            RepairSample sample
    ) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Repair Sample")
                .setMessage(
                        "Are you sure you want to delete this sample?"
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            sampleList.remove(sample);

                            applyFilter();

                            Toast.makeText(
                                    this,
                                    "Sample deleted",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                )
                .show();
    }
}