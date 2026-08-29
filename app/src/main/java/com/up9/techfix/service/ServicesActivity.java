package com.up9.techfix.service;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.up9.techfix.R;

public class ServicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_services);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars = insets.getInsets(
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

        Button btnScreenReplacement =
                findViewById(R.id.btnScreenReplacement);

        Button btnBatteryReplacement =
                findViewById(R.id.btnBatteryReplacement);

        Button btnOperatingSystemRepair =
                findViewById(R.id.btnOperatingSystemRepair);

        Button btnHardwareRepair =
                findViewById(R.id.btnHardwareRepair);

        Button btnSoftwareTroubleshooting =
                findViewById(R.id.btnSoftwareTroubleshooting);

        Button btnVirusRemoval =
                findViewById(R.id.btnVirusRemoval);


        btnScreenReplacement.setOnClickListener(v -> {

            openServiceDetails(
                    "Screen Replacement",
                    "From LKR 8,000",
                    "Replacement of cracked, damaged or broken device screens."
            );

        });


        btnBatteryReplacement.setOnClickListener(v -> {

            openServiceDetails(
                    "Battery Replacement",
                    "From LKR 5,000",
                    "Replacement of damaged, weak or faulty device batteries."
            );

        });


        btnOperatingSystemRepair.setOnClickListener(v -> {

            openServiceDetails(
                    "Operating System Repair",
                    "From LKR 3,000",
                    "Repair and troubleshooting of operating system problems."
            );

        });


        btnHardwareRepair.setOnClickListener(v -> {

            openServiceDetails(
                    "Hardware Repair",
                    "From LKR 3,500",
                    "Diagnosis and repair of faulty internal hardware components."
            );

        });


        btnSoftwareTroubleshooting.setOnClickListener(v -> {

            openServiceDetails(
                    "Software Troubleshooting",
                    "From LKR 2,500",
                    "Diagnosis and resolution of software-related problems."
            );

        });


        btnVirusRemoval.setOnClickListener(v -> {

            openServiceDetails(
                    "Virus / Malware Removal",
                    "From LKR 3,000",
                    "Detection and removal of viruses, malware and other unwanted software."
            );

        });
    }


    private void openServiceDetails(
            String serviceName,
            String servicePrice,
            String serviceDescription) {

        Intent intent = new Intent(
                ServicesActivity.this,
                ServiceDetailsActivity.class
        );

        intent.putExtra("serviceName", serviceName);
        intent.putExtra("servicePrice", servicePrice);
        intent.putExtra("serviceDescription", serviceDescription);

        startActivity(intent);
    }
}