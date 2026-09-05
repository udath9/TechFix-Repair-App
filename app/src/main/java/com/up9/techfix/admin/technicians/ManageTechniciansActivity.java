package com.up9.techfix.admin.technicians;

import android.content.Intent;
import android.database.Cursor;
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
import com.up9.techfix.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ManageTechniciansActivity
        extends AppCompatActivity
        implements TechnicianAdapter.OnTechnicianActionListener {

    private RecyclerView recyclerTechnicians;
    private Button btnAddTechnician;

    private TechnicianAdapter technicianAdapter;
    private List<Technician> technicianList;

    private DatabaseHelper databaseHelper;

    private int editingTechnicianId = -1;

    private final ActivityResultLauncher<Intent>
            technicianFormLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode()
                                != RESULT_OK) {

                            return;
                        }

                        loadTechnicians();
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_manage_technicians
        );

        databaseHelper =
                new DatabaseHelper(this);


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

        technicianAdapter =
                new TechnicianAdapter(
                        technicianList,
                        this
                );

        recyclerTechnicians.setAdapter(
                technicianAdapter
        );

        btnAddTechnician.setOnClickListener(
                v -> openAddForm()
        );

        loadTechnicians();
    }

    private void loadTechnicians() {

        technicianList.clear();

        Cursor cursor =
                databaseHelper.getAllTechnicians();

        if (cursor != null) {

            try {

                while (cursor.moveToNext()) {

                    int id =
                            cursor.getInt(
                                    cursor.getColumnIndexOrThrow(
                                            "id"
                                    )
                            );

                    String name =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "full_name"
                                    )
                            );

                    String phone =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "phone"
                                    )
                            );

                    String email =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "email"
                                    )
                            );

                    String specialization =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "specialization"
                                    )
                            );

                    String branch =
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                            "branch"
                                    )
                            );

                    boolean available =
                            cursor.getInt(
                                    cursor.getColumnIndexOrThrow(
                                            "available"
                                    )
                            ) == 1;

                    technicianList.add(
                            new Technician(
                                    id,
                                    safe(name),
                                    safe(phone),
                                    safe(email),
                                    "",
                                    safe(specialization),
                                    safe(branch),
                                    available
                            )
                    );
                }

            } finally {

                cursor.close();
            }
        }

        technicianAdapter.notifyDataSetChanged();
    }

    private String safe(String value) {

        return value == null ? "" : value;
    }

    private void openAddForm() {

        editingTechnicianId = -1;

        Intent intent =
                new Intent(
                        this,
                        TechnicianFormActivity.class
                );

        intent.putExtra(
                "editMode",
                false
        );

        technicianFormLauncher.launch(intent);
    }

    @Override
    public void onEdit(
            Technician technician
    ) {

        editingTechnicianId =
                technician.getId();

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

                            int result =
                                    databaseHelper
                                            .deleteTechnician(
                                                    technician.getId()
                                            );

                            if (result > 0) {

                                Toast.makeText(
                                        this,
                                        "Technician deleted",
                                        Toast.LENGTH_SHORT
                                ).show();

                                loadTechnicians();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Unable to delete technician",
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
            loadTechnicians();
        }
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}