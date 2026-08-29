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

public class ManageServicesActivity extends AppCompatActivity
        implements ServiceAdapter.OnServiceActionListener {

    private RecyclerView recyclerServices;
    private Button btnAddService;

    private ServiceAdapter serviceAdapter;

    private List<RepairService> serviceList;

    private TechFixDatabaseHelper databaseHelper;

    private int editingPosition = -1;

    private final ActivityResultLauncher<Intent> serviceFormLauncher =
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

                        String category =
                                data.getStringExtra("category");

                        String description =
                                data.getStringExtra("description");

                        double price =
                                data.getDoubleExtra("price", 0);

                        int estimatedDays =
                                data.getIntExtra("estimatedDays", 1);

                        boolean isEditMode =
                                data.getBooleanExtra(
                                        "editMode",
                                        false
                                );

                        if (isEditMode) {

                            int serviceId =
                                    data.getIntExtra(
                                            "serviceId",
                                            -1
                                    );

                            if (serviceId == -1) {
                                return;
                            }

                            databaseHelper.updateService(
                                    serviceId,
                                    name,
                                    category,
                                    description,
                                    price,
                                    estimatedDays
                            );

                        } else {

                            databaseHelper.insertService(
                                    name,
                                    category,
                                    description,
                                    price,
                                    estimatedDays
                            );
                        }

                        loadServices();

                        editingPosition = -1;
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_manage_services
        );

        recyclerServices =
                findViewById(
                        R.id.recyclerServices
                );

        btnAddService =
                findViewById(
                        R.id.btnAddService
                );

        databaseHelper =
                new TechFixDatabaseHelper(this);

        serviceList =
                new ArrayList<>();

        recyclerServices.setLayoutManager(
                new LinearLayoutManager(this)
        );

        serviceAdapter =
                new ServiceAdapter(
                        serviceList,
                        this
                );

        recyclerServices.setAdapter(
                serviceAdapter
        );

        loadServices();

        btnAddService.setOnClickListener(v -> {

            editingPosition = -1;

            Intent intent =
                    new Intent(
                            ManageServicesActivity.this,
                            ServiceFormActivity.class
                    );

            intent.putExtra(
                    "editMode",
                    false
            );

            serviceFormLauncher.launch(intent);
        });
    }

    private void loadServices() {

        serviceList.clear();

        serviceList.addAll(
                databaseHelper.getAllServices()
        );

        serviceAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null
                && serviceAdapter != null) {

            loadServices();
        }
    }

    @Override
    public void onEdit(RepairService service) {

        editingPosition =
                serviceList.indexOf(service);

        Intent intent =
                new Intent(
                        this,
                        ServiceFormActivity.class
                );

        intent.putExtra(
                "editMode",
                true
        );

        intent.putExtra(
                "serviceId",
                service.getId()
        );

        intent.putExtra(
                "name",
                service.getName()
        );

        intent.putExtra(
                "category",
                service.getCategory()
        );

        intent.putExtra(
                "description",
                service.getDescription()
        );

        intent.putExtra(
                "price",
                service.getPrice()
        );

        intent.putExtra(
                "estimatedDays",
                service.getEstimatedDays()
        );

        serviceFormLauncher.launch(intent);
    }

    @Override
    public void onDelete(
            RepairService service,
            int position
    ) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Repair Service")
                .setMessage(
                        "Are you sure you want to delete "
                                + service.getName()
                                + "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            databaseHelper.deleteService(
                                    service.getId()
                            );

                            loadServices();
                        }
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .show();
    }
}