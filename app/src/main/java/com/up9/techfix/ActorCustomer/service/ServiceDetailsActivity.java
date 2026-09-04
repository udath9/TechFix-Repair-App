package com.up9.techfix.ActorCustomer.service;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.up9.techfix.R;
import com.up9.techfix.ActorCustomer.RepairBooking.BookRepairActivity;

import java.util.Locale;

public class ServiceDetailsActivity
        extends AppCompatActivity {

    private ImageView ivServiceImage;
    private TextView tvServiceTitle;
    private TextView tvServicePrice;
    private TextView tvServiceDescription;
    private Button btnBookThisService;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(
                R.layout.activity_service_details
        );

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
                findViewById(
                        R.id.ivServiceImage
                );

        tvServiceTitle =
                findViewById(
                        R.id.tvServiceTitle
                );

        tvServicePrice =
                findViewById(
                        R.id.tvServicePrice
                );

        tvServiceDescription =
                findViewById(
                        R.id.tvServiceDescription
                );

        btnBookThisService =
                findViewById(
                        R.id.btnBookThisService
                );

        Intent intent =
                getIntent();

        int serviceId =
                intent.getIntExtra(
                        "serviceId",
                        -1
                );

        String serviceName =
                intent.getStringExtra(
                        "serviceName"
                );

        String serviceImageUri =
                intent.getStringExtra(
                        "serviceImageUri"
                );

        String serviceDescription =
                intent.getStringExtra(
                        "serviceDescription"
                );

        double servicePrice =
                intent.getDoubleExtra(
                        "servicePrice",
                        0.0
                );


        loadServiceImage(
                serviceImageUri
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


        btnBookThisService.setOnClickListener(
                v -> {

                    Intent bookingIntent =
                            new Intent(
                                    this,
                                    BookRepairActivity.class
                            );

                    bookingIntent.putExtra(
                            "serviceId",
                            serviceId
                    );

                    startActivity(
                            bookingIntent
                    );
                }
        );
    }

    private void loadServiceImage(
            String imageUri
    ) {

        if (
                imageUri == null
                        ||
                        imageUri.trim().isEmpty()
        ) {

            ivServiceImage.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );

            return;
        }

        try {

            Uri uri =
                    Uri.parse(
                            imageUri
                    );

            ivServiceImage.setImageURI(
                    uri
            );

        } catch (Exception e) {

            ivServiceImage.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );
        }
    }
}