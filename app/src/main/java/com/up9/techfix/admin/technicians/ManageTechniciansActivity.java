package com.up9.techfix.admin.technicians;
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

public class ManageTechniciansActivity
        extends AppCompatActivity
        implements TechnicianAdapter.OnTechnicianActionListener {

    private RecyclerView recyclerTechnicians;

    private Button btnAddTechnician;

    private TechnicianAdapter technicianAdapter;

    private List<Technician> technicianList;

    private int editingPosition = -1;

    private final ActivityResultLauncher<Intent>
            technicianFormLauncher =
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

                        String phone =
                                data.getStringExtra(
                                        "phone"
                                );

                        String email =
                                data.getStringExtra(
                                        "email"
                                );

                        String specialization =
                                data.getStringExtra(
                                        "specialization"
                                );

                        String branch =
                                data.getStringExtra(
                                        "branch"
                                );

                        boolean available =
                                data.getBooleanExtra(
                                        "available",
                                        true
                                );

                        if (editingPosition == -1) {

                            int newId =
                                    technicianList.size()
                                            + 1;

                            Technician technician =
                                    new Technician(
                                            newId,
                                            name,
                                            phone,
                                            email,
                                            specialization,
                                            branch,
                                            available
                                    );

                            technicianList.add(
                                    technician
                            );

                            technicianAdapter
                                    .notifyItemInserted(
                                            technicianList.size() - 1
                                    );

                        } else {

                            Technician technician =
                                    technicianList.get(
                                            editingPosition
                                    );

                            technician.setName(name);

                            technician.setPhone(phone);

                            technician.setEmail(email);

                            technician.setSpecialization(
                                    specialization
                            );

                            technician.setBranch(
                                    branch
                            );

                            technician.setAvailable(
                                    available
                            );

                            technicianAdapter
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
                R.layout.activity_manage_technicians
        );

        recyclerTechnicians =
                findViewById(
                        R.id.recyclerTechnicians
                );

        btnAddTechnician =
                findViewById(
                        R.id.btnAddTechnician
                );

        recyclerTechnicians.setLayoutManager(
                new LinearLayoutManager(this)
        );

        technicianList =
                new ArrayList<>();

        loadSampleTechnicians();

        technicianAdapter =
                new TechnicianAdapter(
                        technicianList,
                        this
                );

        recyclerTechnicians.setAdapter(
                technicianAdapter
        );

        btnAddTechnician.setOnClickListener(
                v -> {

                    editingPosition = -1;

                    Intent intent =
                            new Intent(
                                    this,
                                    TechnicianFormActivity.class
                            );

                    technicianFormLauncher.launch(
                            intent
                    );
                }
        );
    }

    private void loadSampleTechnicians() {

        technicianList.add(
                new Technician(
                        1,
                        "Kasun Perera",
                        "0771234567",
                        "kasun@techfix.lk",
                        "Mobile Phone Repair",
                        "Colombo",
                        true
                )
        );

        technicianList.add(
                new Technician(
                        2,
                        "Nimal Fernando",
                        "0712345678",
                        "nimal@techfix.lk",
                        "Laptop Repair",
                        "Colombo",
                        true
                )
        );

        technicianList.add(
                new Technician(
                        3,
                        "Amal Silva",
                        "0763456789",
                        "amal@techfix.lk",
                        "Desktop Repair",
                        "Galle",
                        false
                )
        );

        technicianList.add(
                new Technician(
                        4,
                        "Saman Perera",
                        "0754567890",
                        "saman@techfix.lk",
                        "Hardware Repair",
                        "Galle",
                        true
                )
        );
    }

    @Override
    public void onEdit(
            Technician technician
    ) {

        editingPosition =
                technicianList.indexOf(
                        technician
                );

        Intent intent =
                new Intent(
                        this,
                        TechnicianFormActivity.class
                );

        intent.putExtra(
                "editMode",
                true
        );

        intent.putExtra(
                "technicianId",
                technician.getId()
        );

        intent.putExtra(
                "name",
                technician.getName()
        );

        intent.putExtra(
                "phone",
                technician.getPhone()
        );

        intent.putExtra(
                "email",
                technician.getEmail()
        );

        intent.putExtra(
                "specialization",
                technician.getSpecialization()
        );

        intent.putExtra(
                "branch",
                technician.getBranch()
        );

        intent.putExtra(
                "available",
                technician.isAvailable()
        );

        technicianFormLauncher.launch(
                intent
        );
    }

    @Override
    public void onDelete(
            Technician technician,
            int position
    ) {

        new AlertDialog.Builder(this)
                .setTitle(
                        "Delete Technician"
                )
                .setMessage(
                        "Are you sure you want to delete "
                                + technician.getName()
                                + "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            technicianList.remove(
                                    position
                            );

                            technicianAdapter
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