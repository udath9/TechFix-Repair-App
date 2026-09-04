package com.up9.techfix.ActorCustomer.service;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.up9.techfix.ActorCustomer.service.ServiceDetailsActivity;
import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;
import com.up9.techfix.data.Service;

import java.util.List;

public class ServicesActivity extends AppCompatActivity {

    private static final String TAG = "ServicesActivity";

    private DatabaseHelper databaseHelper;

    private LinearLayout servicesContainer;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(
                savedInstanceState
        );

        EdgeToEdge.enable(this);

        setContentView(
                R.layout.activity_services
        );

        setupWindowInsets();

        servicesContainer =
                findViewById(
                        R.id.servicesContainer
                );

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

    private void loadServices() {

        if (servicesContainer == null) {
            return;
        }

        servicesContainer.removeAllViews();

        List<Service> services =
                databaseHelper.getAllServices();

        if (
                services == null
                        ||
                        services.isEmpty()
        ) {

            return;
        }

        LinearLayout currentRow =
                null;

        for (
                int i = 0;
                i < services.size();
                i++
        ) {

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
                    createServiceButton(
                            service
                    )
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

        row.setWeightSum(
                2
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        row.setLayoutParams(
                params
        );

        return row;
    }


    private Button createServiceButton(
            Service service
    ) {

        Button button =
                new Button(this);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        300,
                        1
                );

        params.setMargins(
                8,
                8,
                8,
                8
        );

        button.setLayoutParams(
                params
        );

        button.setText(
                service.getName()
        );

        button.setTextSize(
                16
        );

        button.setGravity(
                Gravity.CENTER
        );

        button.setAllCaps(
                false
        );

        setServiceImage(
                button,
                service.getImageUri()
        );

        button.setOnClickListener(
                v -> openServiceDetails(
                        service
                )
        );

        return button;
    }

    private void setServiceImage(
            Button button,
            String imageValue
    ) {

        if (
                imageValue == null
                        ||
                        imageValue.trim().isEmpty()
        ) {

            return;
        }

        imageValue =
                imageValue.trim();

        try {


            if (
                    imageValue.startsWith(
                            "file://"
                    )
            ) {

                Uri uri =
                        Uri.parse(
                                imageValue
                        );

                Drawable drawable =
                        Drawable.createFromPath(
                                uri.getPath()
                        );

                if (drawable != null) {

                    setButtonDrawable(
                            button,
                            drawable
                    );
                }

                return;
            }

            if (
                    imageValue.startsWith(
                            "content://"
                    )
            ) {

                Uri uri =
                        Uri.parse(
                                imageValue
                        );

                try {

                    Drawable drawable =
                            Drawable.createFromStream(
                                    getContentResolver()
                                            .openInputStream(uri),
                                    imageValue
                            );

                    if (drawable != null) {

                        setButtonDrawable(
                                button,
                                drawable
                        );
                    }

                } catch (Exception e) {

                    Log.e(
                            TAG,
                            "Unable to load content URI",
                            e
                    );
                }

                return;
            }


            int resourceId =
                    getResources().getIdentifier(
                            imageValue,
                            "drawable",
                            getPackageName()
                    );

            if (resourceId != 0) {

                Drawable drawable =
                        ContextCompat.getDrawable(
                                this,
                                resourceId
                        );

                if (drawable != null) {

                    setButtonDrawable(
                            button,
                            drawable
                    );
                }
            }

        } catch (Exception e) {

            Log.e(
                    TAG,
                    "Unable to load service image",
                    e
            );
        }
    }


    private void setButtonDrawable(
            Button button,
            Drawable drawable
    ) {

        int imageSize =
                120;

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

        button.setCompoundDrawablePadding(
                12
        );
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

        intent.putExtra(
                "estimatedDays",
                service.getEstimatedDays()
        );

        startActivity(
                intent
        );
    }


    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {

            loadServices();
        }
    }
}
