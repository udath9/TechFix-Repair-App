package com.up9.techfix.ActorCustomer.service;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.up9.techfix.R;
import com.up9.techfix.ActorCustomer.RepairBooking.BookRepairActivity;

import java.util.Locale;

public class ServiceDetailsActivity extends AppCompatActivity {

    private ImageView ivServiceImage;
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

        ivServiceImage =
                findViewById(R.id.ivServiceImage);

        tvServiceTitle =
                findViewById(R.id.tvServiceTitle);

        tvServicePrice =
                findViewById(R.id.tvServicePrice);

        tvServiceDescription =
                findViewById(R.id.tvServiceDescription);

        btnBookThisService =
                findViewById(R.id.btnBookThisService);


        // Get service data sent from ServicesActivity

        Intent intent = getIntent();

        int serviceId =
                intent.getIntExtra("serviceId", -1);

        String serviceName =
                intent.getStringExtra("serviceName");

        String serviceImageUri =
                intent.getStringExtra("serviceImageUri");

        String serviceDescription =
                intent.getStringExtra("serviceDescription");

        double servicePrice =
                intent.getDoubleExtra(
                        "servicePrice",
                        0.0
                );


        // Display service image

        if (serviceImageUri != null &&
                !serviceImageUri.trim().isEmpty()) {

            int imageResourceId =
                    getResources().getIdentifier(
                            serviceImageUri.trim(),
                            "drawable",
                            getPackageName()
                    );

            if (imageResourceId != 0) {

                Drawable drawable =
                        ContextCompat.getDrawable(
                                this,
                                imageResourceId
                        );

                if (drawable != null) {

                    ivServiceImage.setImageDrawable(
                            drawable
                    );

                    ivServiceImage.setVisibility(
                            ImageView.VISIBLE
                    );
                }
            }
        }


        // Display service name

        if (serviceName != null) {

            tvServiceTitle.setText(
                    serviceName
            );
        }


        // Display service price

        tvServicePrice.setText(
                String.format(
                        Locale.getDefault(),
                        "LKR %.2f",
                        servicePrice
                )
        );


        // Display service description

        if (serviceDescription != null) {

            tvServiceDescription.setText(
                    serviceDescription
            );
        }


        // Book this service

        btnBookThisService.setOnClickListener(v -> {

            Intent bookingIntent =
                    new Intent(
                            ServiceDetailsActivity.this,
                            BookRepairActivity.class
                    );

            bookingIntent.putExtra(
                    "serviceId",
                    serviceId
            );

            startActivity(
                    bookingIntent
            );
        });
    }
}