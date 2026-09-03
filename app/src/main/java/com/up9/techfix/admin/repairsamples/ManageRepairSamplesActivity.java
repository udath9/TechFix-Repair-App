package com.up9.techfix.admin.repairsamples;

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
import com.up9.techfix.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ManageRepairSamplesActivity
        extends AppCompatActivity
        implements RepairSampleAdapter.OnRepairSampleActionListener {

    private RecyclerView recyclerRepairSamples;

    private Button btnAddRepairSample;

    private RepairSampleAdapter sampleAdapter;

    private List<RepairSample> sampleList;

    private DatabaseHelper databaseHelper;

    private final ActivityResultLauncher<Intent>
            repairSampleFormLauncher =
            registerForActivityResult(
                    new ActivityResultContracts
                            .StartActivityForResult(),
                    result -> {

                        if (result.getResultCode()
                                == RESULT_OK) {

                            loadRepairSamples();
                        }
                    }
            );

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

        btnAddRepairSample =
                findViewById(
                        R.id.btnAddRepairSample
                );

        databaseHelper =
                new DatabaseHelper(this);

        sampleList =
                new ArrayList<>();

        recyclerRepairSamples.setLayoutManager(
                new LinearLayoutManager(this)
        );

        sampleAdapter =
                new RepairSampleAdapter(
                        sampleList,
                        this
                );

        recyclerRepairSamples.setAdapter(
                sampleAdapter
        );

        loadRepairSamples();

        btnAddRepairSample.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    this,
                                    AddEditRepairSampleActivity.class
                            );

                    intent.putExtra(
                            "editMode",
                            false
                    );

                    repairSampleFormLauncher.launch(
                            intent
                    );
                }
        );
    }

    private void loadRepairSamples() {

        sampleList.clear();

        sampleList.addAll(
                databaseHelper
                        .getAllRepairSamples()
        );

        sampleAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null
                && sampleAdapter != null) {

            loadRepairSamples();
        }
    }

    @Override
    public void onEdit(
            RepairSample sample
    ) {

        Intent intent =
                new Intent(
                        this,
                        AddEditRepairSampleActivity.class
                );

        intent.putExtra(
                "editMode",
                true
        );

        intent.putExtra(
                "sampleId",
                sample.getId()
        );

        intent.putExtra(
                "deviceName",
                sample.getDeviceName()
        );

        intent.putExtra(
                "category",
                sample.getCategory()
        );

        intent.putExtra(
                "service",
                sample.getService()
        );

        intent.putExtra(
                "description",
                sample.getDescription()
        );

        intent.putExtra(
                "imageUri",
                sample.getImageUri()
        );

        repairSampleFormLauncher.launch(
                intent
        );
    }

    @Override
    public void onDelete(
            RepairSample sample,
            int position
    ) {

        new AlertDialog.Builder(this)
                .setTitle(
                        "Delete Repair Sample"
                )
                .setMessage(
                        "Are you sure you want to delete "
                                + sample.getDeviceName()
                                + "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            databaseHelper.deleteRepairSample(
                                    sample.getId()
                            );

                            loadRepairSamples();
                        }
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .show();
    }
}