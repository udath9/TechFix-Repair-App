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

public class ManageBranchesActivity extends AppCompatActivity
        implements BranchAdapter.OnBranchActionListener {

    private RecyclerView recyclerBranches;

    private Button btnAddBranch;

    private BranchAdapter branchAdapter;

    private List<Branch> branchList;

    private TechFixDatabaseHelper databaseHelper;

    private final ActivityResultLauncher<Intent> branchFormLauncher =
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

                        String address =
                                data.getStringExtra("address");

                        String phone =
                                data.getStringExtra("phone");

                        double latitude =
                                data.getDoubleExtra(
                                        "latitude",
                                        0
                                );

                        double longitude =
                                data.getDoubleExtra(
                                        "longitude",
                                        0
                                );

                        boolean editMode =
                                data.getBooleanExtra(
                                        "editMode",
                                        false
                                );

                        int branchId =
                                data.getIntExtra(
                                        "branchId",
                                        -1
                                );

                        if (editMode && branchId != -1) {

                            int resultCode =
                                    databaseHelper.updateBranch(
                                            branchId,
                                            name,
                                            address,
                                            phone,
                                            latitude,
                                            longitude
                                    );

                            if (resultCode > 0) {

                                Toast.makeText(
                                        this,
                                        "Branch updated successfully",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Failed to update branch",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        } else {

                            long newId =
                                    databaseHelper.insertBranch(
                                            name,
                                            address,
                                            phone,
                                            latitude,
                                            longitude
                                    );

                            if (newId != -1) {

                                Toast.makeText(
                                        this,
                                        "Branch added successfully",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Failed to add branch",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }

                        loadBranches();
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_manage_branches
        );

        recyclerBranches =
                findViewById(
                        R.id.recyclerBranches
                );

        btnAddBranch =
                findViewById(
                        R.id.btnAddBranch
                );

        databaseHelper =
                new TechFixDatabaseHelper(this);

        recyclerBranches.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadBranches();

        btnAddBranch.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            ManageBranchesActivity.this,
                            BranchFormActivity.class
                    );

            intent.putExtra(
                    "editMode",
                    false
            );

            branchFormLauncher.launch(intent);
        });
    }

    private void loadBranches() {

        branchList =
                databaseHelper.getAllBranches();

        branchAdapter =
                new BranchAdapter(
                        branchList,
                        this
                );

        recyclerBranches.setAdapter(
                branchAdapter
        );
    }

    @Override
    public void onEdit(Branch branch) {

        Intent intent =
                new Intent(
                        this,
                        BranchFormActivity.class
                );

        intent.putExtra(
                "editMode",
                true
        );

        intent.putExtra(
                "branchId",
                branch.getId()
        );

        intent.putExtra(
                "name",
                branch.getName()
        );

        intent.putExtra(
                "address",
                branch.getAddress()
        );

        intent.putExtra(
                "phone",
                branch.getPhone()
        );

        intent.putExtra(
                "latitude",
                branch.getLatitude()
        );

        intent.putExtra(
                "longitude",
                branch.getLongitude()
        );

        branchFormLauncher.launch(intent);
    }

    @Override
    public void onDelete(
            Branch branch,
            int position
    ) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Branch")
                .setMessage(
                        "Are you sure you want to delete "
                                + branch.getName()
                                + "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            int result =
                                    databaseHelper.deleteBranch(
                                            branch.getId()
                                    );

                            if (result > 0) {

                                Toast.makeText(
                                        this,
                                        "Branch deleted successfully",
                                        Toast.LENGTH_SHORT
                                ).show();

                                loadBranches();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Failed to delete branch",
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
            loadBranches();
        }
    }
}