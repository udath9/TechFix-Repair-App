package com.up9.techfix.ActorCustomer.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;
import com.up9.techfix.ActorCustomer.customer.CustomerHomeActivity;
import com.up9.techfix.admin.dashboard.AdminDashboardActivity;
import com.up9.techfix.admin.technicians.ManageTechniciansActivity;
import com.up9.techfix.admin.technicians.ManageTechniciansActivity;
import com.up9.techfix.data.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private EditText etEmail;
    private EditText etPassword;

    private Button btnLogin;

    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etEmail);

        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);

        tvRegister = findViewById(R.id.tvRegister);

        // ----------------------------------------------------
        // LOGIN BUTTON
        // ----------------------------------------------------

        btnLogin.setOnClickListener(v -> {

            validateLogin();

        });

        // ----------------------------------------------------
        // REGISTER
        // ----------------------------------------------------

        tvRegister.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);

        });
    }

    // ========================================================
    // VALIDATE LOGIN
    // ========================================================

    private void validateLogin() {

        String email =
                etEmail.getText()
                        .toString()
                        .trim();

        String password =
                etPassword.getText()
                        .toString();

        // ----------------------------------------------------
        // Email validation
        // ----------------------------------------------------

        if (email.isEmpty()) {

            etEmail.setError(
                    "Please enter your email"
            );

            etEmail.requestFocus();

            return;
        }

        if (!Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            etEmail.setError(
                    "Please enter a valid email"
            );

            etEmail.requestFocus();

            return;
        }

        // ----------------------------------------------------
        // Password validation
        // ----------------------------------------------------

        if (password.isEmpty()) {

            etPassword.setError(
                    "Please enter your password"
            );

            etPassword.requestFocus();

            return;
        }

        // ----------------------------------------------------
        // LOGIN
        // ----------------------------------------------------

        DatabaseHelper.LoginUser user =
                databaseHelper.loginUser(
                        email,
                        password
                );

        if (user == null) {

            Toast.makeText(
                    this,
                    "Invalid email or password",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // ----------------------------------------------------
        // Save session
        // ----------------------------------------------------

        SharedPreferences preferences =
                getSharedPreferences(
                        "TechFixSession",
                        MODE_PRIVATE
                );

        preferences.edit()
                .putInt(
                        "userId",
                        user.getId()
                )
                .putString(
                        "userName",
                        user.getFullName()
                )
                .putString(
                        "userEmail",
                        user.getEmail()
                )
                .putString(
                        "userRole",
                        user.getRole()
                )
                .apply();

        // ----------------------------------------------------
        // Customer
        // ----------------------------------------------------

        if ("CUSTOMER".equalsIgnoreCase(
                user.getRole()
        )) {

            int customerId =
                    databaseHelper.getCustomerId(
                            email
                    );

            preferences.edit()
                    .putInt(
                            "customerId",
                            customerId
                    )
                    .apply();

            Toast.makeText(
                    this,
                    "Customer login successful!",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    LoginActivity.this,
                    CustomerHomeActivity.class
            );

            startActivity(intent);

            finish();

            return;
        }

        // ----------------------------------------------------
        // Admin
        // ----------------------------------------------------

        if ("ADMIN".equalsIgnoreCase(
                user.getRole()
        )) {

            Toast.makeText(
                    this,
                    "Admin login successful!",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    LoginActivity.this,
                    AdminDashboardActivity.class
            );

            startActivity(intent);

            finish();

            return;
        }

        // ----------------------------------------------------
        // Technician
        // ----------------------------------------------------

        if ("TECHNICIAN".equalsIgnoreCase(
                user.getRole()
        )) {

            Toast.makeText(
                    this,
                    "Technician login successful!",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    LoginActivity.this,
                    ManageTechniciansActivity.class
            );

            startActivity(intent);

            finish();

            return;
        }

        // ----------------------------------------------------
        // Unknown role
        // ----------------------------------------------------

        Toast.makeText(
                this,
                "Unknown user role.",
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}