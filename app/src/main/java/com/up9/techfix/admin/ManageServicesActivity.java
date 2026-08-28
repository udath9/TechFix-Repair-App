package com.up9.techfix.admin;
import com.up9.techfix.R;
import android.os.Bundle;


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

public class ManageServicesActivity
        extends AppCompatActivity
        implements ServiceAdapter.OnServiceActionListener {

    private RecyclerView recyclerServices;

    private Button btnAddService;

    private ServiceAdapter serviceAdapter;

    private List<RepairService> serviceList;

    private int editingPosition = -1;

    private final ActivityResultLauncher<Intent>
            serviceFormLauncher =
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

                        String category =
                                data.getStringExtra(
                                        "category"
                                );

                        String description =
                                data.getStringExtra(
                                        "description"
                                );

                        double price =
                                data.getDoubleExtra(
                                        "price",
                                        0
                                );

                        int estimatedDays =
                                data.getIntExtra(
                                        "estimatedDays",
                                        1
                                );

                        if (editingPosition == -1) {

                            int newId =
                                    serviceList.size()
                                            + 1;

                            RepairService service =
                                    new RepairService(
                                            newId,
                                            name,
                                            category,
                                            description,
                                            price,
                                            estimatedDays
                                    );

                            serviceList.add(service);

                            serviceAdapter
                                    .notifyItemInserted(
                                            serviceList.size() - 1
                                    );

                        } else {

                            RepairService service =
                                    serviceList.get(
                                            editingPosition
                                    );

                            service.setName(name);

                            service.setCategory(
                                    category
                            );

                            service.setDescription(
                                    description
                            );

                            service.setPrice(price);

                            service.setEstimatedDays(
                                    estimatedDays
                            );

                            serviceAdapter
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

        recyclerServices.setLayoutManager(
                new LinearLayoutManager(this)
        );

        serviceList =
                new ArrayList<>();

        loadSampleServices();

        serviceAdapter =
                new ServiceAdapter(
                        serviceList,
                        this
                );

        recyclerServices.setAdapter(
                serviceAdapter
        );

        btnAddService.setOnClickListener(
                v -> {

                    editingPosition = -1;

                    Intent intent =
                            new Intent(
                                    this,
                                    ServiceFormActivity.class
                            );

                    serviceFormLauncher.launch(
                            intent
                    );
                }
        );
    }

    private void loadSampleServices() {

        serviceList.add(
                new RepairService(
                        1,
                        "Screen Replacement",
                        "Mobile Phone",
                        "Replacement of damaged mobile phone screens",
                        25000,
                        2
                )
        );

        serviceList.add(
                new RepairService(
                        2,
                        "Battery Replacement",
                        "Mobile Phone",
                        "Replacement of damaged or weak batteries",
                        8000,
                        1
                )
        );

        serviceList.add(
                new RepairService(
                        3,
                        "Windows Installation",
                        "Laptop",
                        "Operating system installation and setup",
                        5000,
                        1
                )
        );

        serviceList.add(
                new RepairService(
                        4,
                        "Keyboard Replacement",
                        "Laptop",
                        "Laptop keyboard replacement service",
                        12000,
                        2
                )
        );
    }

    @Override
    public void onEdit(
            RepairService service
    ) {

        editingPosition =
                serviceList.indexOf(
                        service
                );

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

        serviceFormLauncher.launch(
                intent
        );
    }

    @Override
    public void onDelete(
            RepairService service,
            int position
    ) {

        new AlertDialog.Builder(this)
                .setTitle(
                        "Delete Repair Service"
                )
                .setMessage(
                        "Are you sure you want to delete "
                                + service.getName()
                                + "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            serviceList.remove(
                                    position
                            );

                            serviceAdapter
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