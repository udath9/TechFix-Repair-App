package com.up9.techfix.admin.services;

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
import com.up9.techfix.data.DatabaseHelper;
import com.up9.techfix.data.Service;

import java.util.ArrayList;
import java.util.List;

public class ManageServicesActivity extends AppCompatActivity
        implements ServiceAdapter.OnServiceActionListener {

    private RecyclerView recyclerServices;
    private Button btnAddService;

    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;

    private DatabaseHelper databaseHelper;

    private final ActivityResultLauncher<Intent> serviceFormLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == RESULT_OK) {
                            loadServices();
                        }
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
                new DatabaseHelper(this);

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

        btnAddService.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    this,
                                    ServiceFormActivity.class
                            );

                    intent.putExtra(
                            "editMode",
                            false
                    );

                    serviceFormLauncher.launch(intent);
                }
        );

        loadServices();
    }

    private void loadServices() {

        List<Service> newServices =
                databaseHelper.getAllServices();

        serviceList.clear();

        if (newServices != null) {
            serviceList.addAll(newServices);
        }

        serviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEdit(Service service) {

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
                "imageUri",
                service.getImageUri()
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
            Service service,
            int position
    ) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Service")
                .setMessage(
                        "Are you sure you want to delete "
                                + service.getName()
                                + "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            int result =
                                    databaseHelper.deleteService(
                                            service.getId()
                                    );

                            if (result > 0) {

                                Toast.makeText(
                                        this,
                                        "Service deleted successfully",
                                        Toast.LENGTH_SHORT
                                ).show();

                                loadServices();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Failed to delete service",
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

        if (
                databaseHelper != null
                        &&
                        serviceAdapter != null
        ) {
            loadServices();
        }
    }
}