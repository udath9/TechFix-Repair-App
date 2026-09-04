package com.up9.techfix.ActorCustomer.map;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.up9.techfix.R;
import com.up9.techfix.data.Branch;
import com.up9.techfix.data.DatabaseHelper;

import org.maplibre.android.MapLibre;
import org.maplibre.android.annotations.MarkerOptions;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.MapView;

import java.util.List;

public class BranchesActivity extends AppCompatActivity {

    private MapView mapView;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MapLibre.getInstance(this);

        EdgeToEdge.enable(this);

        setContentView(
                R.layout.activity_branches
        );

        setupWindowInsets();

        databaseHelper =
                new DatabaseHelper(this);

        mapView =
                findViewById(
                        R.id.mapView
                );

        mapView.getMapAsync(map -> {

            map.setStyle(
                    "https://tiles.openfreemap.org/styles/liberty",
                    style -> {

                        map.setCameraPosition(
                                new CameraPosition.Builder()
                                        .target(
                                                new LatLng(
                                                        7.5,
                                                        80.7
                                                )
                                        )
                                        .zoom(7.0)
                                        .build()
                        );

                        loadBranches(map);
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

    private void loadBranches(
            MapLibreMap map
    ) {

        List<Branch> branchList =
                databaseHelper.getAllBranches();

        if (branchList.isEmpty()) {

            Toast.makeText(
                    this,
                    "No branches available",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        for (Branch branch : branchList) {

            map.addMarker(
                    new MarkerOptions()
                            .position(
                                    new LatLng(
                                            branch.getLatitude(),
                                            branch.getLongitude()
                                    )
                            )
                            .title(
                                    branch.getName()
                            )
                            .snippet(
                                    branch.getAddress()
                                            + "\nPhone: "
                                            + branch.getPhone()
                            )
            );
        }

        Toast.makeText(
                this,
                branchList.size()
                        + " branch(es) loaded",
                Toast.LENGTH_SHORT
        ).show();
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