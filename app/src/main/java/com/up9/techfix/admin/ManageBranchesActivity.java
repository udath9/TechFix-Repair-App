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

public class ManageBranchesActivity extends AppCompatActivity
        implements BranchAdapter.OnBranchActionListener {

    private RecyclerView recyclerBranches;

    private Button btnAddBranch;

    private BranchAdapter branchAdapter;

    private List<Branch> branchList;

    private int editingPosition = -1;

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

                        if (editingPosition == -1) {

                            int newId =
                                    branchList.size() + 1;

                            Branch newBranch =
                                    new Branch(
                                            newId,
                                            name,
                                            address,
                                            phone,
                                            latitude,
                                            longitude
                                    );

                            branchList.add(newBranch);

                        } else {

                            Branch branch =
                                    branchList.get(
                                            editingPosition
                                    );

                            branch.setName(name);
                            branch.setAddress(address);
                            branch.setPhone(phone);
                            branch.setLatitude(latitude);
                            branch.setLongitude(longitude);

                            editingPosition = -1;
                        }

                        branchAdapter.notifyDataSetChanged();
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

        recyclerBranches.setLayoutManager(
                new LinearLayoutManager(this)
        );

        branchList =
                new ArrayList<>();

        loadSampleBranches();

        branchAdapter =
                new BranchAdapter(
                        branchList,
                        this
                );

        recyclerBranches.setAdapter(
                branchAdapter
        );

        btnAddBranch.setOnClickListener(v -> {

            editingPosition = -1;

            Intent intent =
                    new Intent(
                            ManageBranchesActivity.this,
                            BranchFormActivity.class
                    );

            branchFormLauncher.launch(intent);
        });
    }

    private void loadSampleBranches() {

        branchList.add(
                new Branch(
                        1,
                        "Colombo Branch",
                        "Colombo, Sri Lanka",
                        "011-2345678",
                        6.9271,
                        79.8612
                )
        );

        branchList.add(
                new Branch(
                        2,
                        "Galle Branch",
                        "Galle, Sri Lanka",
                        "091-2345678",
                        6.0329,
                        80.2168
                )
        );
    }

    @Override
    public void onEdit(Branch branch) {

        editingPosition =
                branchList.indexOf(branch);

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

                            branchList.remove(position);

                            branchAdapter.notifyItemRemoved(
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