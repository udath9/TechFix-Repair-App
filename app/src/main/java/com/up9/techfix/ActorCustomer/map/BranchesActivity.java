package com.up9.techfix.ActorCustomer.map;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.up9.techfix.R;
import com.up9.techfix.data.DatabaseHelper;

import org.maplibre.android.MapLibre;
import org.maplibre.android.annotations.MarkerOptions;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.MapView;

public class BranchesActivity extends AppCompatActivity {

    private MapView mapView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapLibre.getInstance(this);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_branches);

        setupWindowInsets();

        databaseHelper = new DatabaseHelper(this);

        mapView = findViewById(R.id.mapView);

        mapView.getMapAsync(map -> {

            map.setStyle(
                    "https://tiles.openfreemap.org/styles/liberty",
                    style -> {

                        map.setCameraPosition(
                                new CameraPosition.Builder()
                                        .target(new LatLng(7.5, 80.7))
                                        .zoom(7.0)
                                        .build()
                        );

                        loadBranches(map);

                        Toast.makeText(
                                this,
                                "Branches loaded",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
            );
        });
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

    // Load all branches from the database and add them to the map.
    private void loadBranches(MapLibreMap map) {

        Cursor cursor =
                databaseHelper.getAllBranches();

        if (cursor == null) {
            return;
        }

        try {

            while (cursor.moveToNext()) {

                String branchName =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "name"
                                )
                        );

                String address =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "address"
                                )
                        );

                String phone =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "phone"
                                )
                        );

                double latitude =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        "latitude"
                                )
                        );

                double longitude =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        "longitude"
                                )
                        );

                map.addMarker(
                        new MarkerOptions()
                                .position(
                                        new LatLng(
                                                latitude,
                                                longitude
                                        )
                                )
                                .title(branchName)
                                .snippet(
                                        address
                                                + "\nPhone: "
                                                + phone
                                )
                );
            }

        } finally {
            cursor.close();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mapView != null) {
            mapView.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mapView != null) {
            mapView.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mapView != null) {
            mapView.onDestroy();
        }
    }
}