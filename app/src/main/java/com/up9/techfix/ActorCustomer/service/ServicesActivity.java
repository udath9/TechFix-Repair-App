package com.up9.techfix.ActorCustomer.service;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;
import com.up9.techfix.data.Service;

import java.util.List;

public class ServicesActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private LinearLayout servicesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_services);

        setupWindowInsets();

        servicesContainer =
                findViewById(R.id.servicesContainer);

        databaseHelper =
                new DatabaseHelper(this);

        loadServices();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );
    }

    // Load all repair services from the database.
    private void loadServices() {

        servicesContainer.removeAllViews();

        List<Service> services =
                databaseHelper.getAllServices();

        LinearLayout currentRow = null;

        for (int i = 0; i < services.size(); i++) {

            if (i % 2 == 0) {

                currentRow =
                        createServiceRow();

                servicesContainer.addView(
                        currentRow
                );
            }

            Service service =
                    services.get(i);

            currentRow.addView(
                    createServiceButton(service)
            );
        }
    }

    private LinearLayout createServiceRow() {

        LinearLayout row =
                new LinearLayout(this);

        row.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row.setGravity(
                Gravity.CENTER
        );

        row.setWeightSum(2);

        LinearLayout.LayoutParams rowParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        row.setLayoutParams(rowParams);

        return row;
    }

    private Button createServiceButton(
            Service service
    ) {

        Button button =
                new Button(this);

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        0,
                        300,
                        1
                );

        buttonParams.setMargins(
                8,
                8,
                8,
                8
        );

        button.setLayoutParams(
                buttonParams
        );

        button.setText(
                service.getName()
        );

        button.setTextSize(16);
        button.setGravity(Gravity.CENTER);
        button.setAllCaps(false);

        setServiceImage(
                button,
                service.getImageUri()
        );

        button.setOnClickListener(
                v -> openServiceDetails(service)
        );

        return button;
    }

    private void setServiceImage(
            Button button,
            String imageName
    ) {

        if (imageName == null ||
                imageName.trim().isEmpty()) {

            return;
        }

        int imageResourceId =
                getResources().getIdentifier(
                        imageName.trim(),
                        "drawable",
                        getPackageName()
                );

        if (imageResourceId == 0) {
            return;
        }

        Drawable drawable =
                ContextCompat.getDrawable(
                        this,
                        imageResourceId
                );

        if (drawable == null) {
            return;
        }

        int imageSize = 80;

        drawable.setBounds(
                0,
                0,
                imageSize,
                imageSize
        );

        button.setCompoundDrawables(
                null,
                drawable,
                null,
                null
        );

        button.setCompoundDrawablePadding(12);
    }

    private void openServiceDetails(
            Service service
    ) {

        Intent intent =
                new Intent(
                        ServicesActivity.this,
                        ServiceDetailsActivity.class
                );

        intent.putExtra(
                "serviceId",
                service.getId()
        );

        intent.putExtra(
                "serviceName",
                service.getName()
        );

        intent.putExtra(
                "serviceImageUri",
                service.getImageUri()
        );

        intent.putExtra(
                "serviceDescription",
                service.getDescription()
        );

        intent.putExtra(
                "servicePrice",
                service.getPrice()
        );

        startActivity(intent);
    }
}