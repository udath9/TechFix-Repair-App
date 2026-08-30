package com.up9.techfix.service;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.up9.techfix.R;
import com.up9.techfix.booking.BookRepairActivity;

import java.util.Locale;

public class ServiceDetailsActivity extends AppCompatActivity {

    private TextView tvServiceTitle;
    private TextView tvServicePrice;
    private TextView tvServiceDescription;
    private Button btnBookThisService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service_details);

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

        tvServiceTitle =
                findViewById(R.id.tvServiceTitle);

        tvServicePrice =
                findViewById(R.id.tvServicePrice);

        tvServiceDescription =
                findViewById(R.id.tvServiceDescription);

        btnBookThisService =
                findViewById(R.id.btnBookThisService);


        Intent intent = getIntent();

        String serviceName =
                intent.getStringExtra("serviceName");

        String serviceDescription =
                intent.getStringExtra("serviceDescription");

        double servicePrice =
                intent.getDoubleExtra(
                        "servicePrice",
                        0.0
                );


        if (serviceName != null) {

            tvServiceTitle.setText(
                    serviceName
            );
        }


        tvServicePrice.setText(
                String.format(
                        Locale.getDefault(),
                        "LKR %.2f",
                        servicePrice
                )
        );


        if (serviceDescription != null) {

            tvServiceDescription.setText(
                    serviceDescription
            );
        }


        btnBookThisService.setOnClickListener(v -> {

            Intent bookingIntent =
                    new Intent(
                            ServiceDetailsActivity.this,
                            BookRepairActivity.class
                    );

            bookingIntent.putExtra(
                    "selectedService",
                    serviceName
            );

            startActivity(
                    bookingIntent
            );
        });
    }
}