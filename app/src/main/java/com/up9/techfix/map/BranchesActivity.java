package com.up9.techfix.map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.up9.techfix.R;

public class BranchesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_branches);

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

        Button btnColomboMap = findViewById(R.id.btnColomboMap);
        Button btnGalleMap = findViewById(R.id.btnGalleMap);

        btnColomboMap.setOnClickListener(v -> {

            openMap(
                    6.9271,
                    79.8612,
                    "TechFix Colombo"
            );

        });

        btnGalleMap.setOnClickListener(v -> {

            openMap(
                    6.0329,
                    80.2168,
                    "TechFix Galle"
            );

        });
    }

    private void openMap(
            double latitude,
            double longitude,
            String branchName) {

        Uri location = Uri.parse(
                "geo:" + latitude + "," + longitude
                        + "?q=" + latitude + "," + longitude
                        + "(" + Uri.encode(branchName) + ")"
        );

        Intent mapIntent = new Intent(
                Intent.ACTION_VIEW,
                location
        );

        if (mapIntent.resolveActivity(getPackageManager()) != null) {

            startActivity(mapIntent);

        } else {

            Toast.makeText(
                    this,
                    "No map application found.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}