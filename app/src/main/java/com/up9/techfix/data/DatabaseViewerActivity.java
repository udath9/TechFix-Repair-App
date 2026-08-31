package com.up9.techfix.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.up9.techfix.R;

public class DatabaseViewerActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private Button btnAddTestServices;
    private Button btnAddTestPayment;
    private Button btnRefresh;

    private TableLayout tableCustomers;
    private TableLayout tableServices;
    private TableLayout tableBranches;
    private TableLayout tableRepairs;
    private TableLayout tablePayments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_database_viewer);


        // =====================================================
        // DATABASE
        // =====================================================

        databaseHelper =
                new DatabaseHelper(this);


        // =====================================================
        // TABLES
        // =====================================================

        tableCustomers =
                findViewById(R.id.tableCustomers);

        tableServices =
                findViewById(R.id.tableServices);

        tableBranches =
                findViewById(R.id.tableBranches);

        tableRepairs =
                findViewById(R.id.tableRepairs);

        tablePayments =
                findViewById(R.id.tablePayments);


        // =====================================================
        // BUTTONS
        // =====================================================

        btnRefresh =
                findViewById(R.id.btnRefresh);

        btnAddTestServices =
                findViewById(R.id.btnAddTestServices);

        btnAddTestPayment =
                findViewById(R.id.btnAddTestPayment);


        // =====================================================
        // DISPLAY DATABASE WHEN PAGE OPENS
        // =====================================================

        displayDatabase();


        // =====================================================
        // ADD TEST SERVICES
        // =====================================================

        btnAddTestServices.setOnClickListener(v -> {

            databaseHelper.insertTestServices();

            displayDatabase();
        });


        // =====================================================
        // ADD TEST PAYMENT
        // =====================================================

        btnAddTestPayment.setOnClickListener(v -> {

            databaseHelper.insertTestPayment();

            displayDatabase();
        });


        // =====================================================
        // REFRESH DATABASE
        // =====================================================

        btnRefresh.setOnClickListener(v -> {

            displayDatabase();
        });
    }


    // =====================================================
    // DISPLAY DATABASE
    // =====================================================

    private void displayDatabase() {

        SQLiteDatabase db =
                databaseHelper.getReadableDatabase();


        // =================================================
        // CUSTOMERS
        // =================================================

        createTable(
                db,
                "customers",
                tableCustomers
        );


        // =================================================
        // SERVICES
        // =================================================

        createTable(
                db,
                "services",
                tableServices
        );


        // =================================================
        // BRANCHES
        // =================================================

        createTable(
                db,
                "branches",
                tableBranches
        );


        // =================================================
        // REPAIRS
        // =================================================

        createTable(
                db,
                "repairs",
                tableRepairs
        );


        // =================================================
        // PAYMENTS
        // =================================================

        createTable(
                db,
                "payments",
                tablePayments
        );


        db.close();
    }


    // =====================================================
    // CREATE TABLE DISPLAY
    // =====================================================

    private void createTable(
            SQLiteDatabase db,
            String tableName,
            TableLayout tableLayout
    ) {

        // Remove existing rows
        // before refreshing.

        tableLayout.removeAllViews();


        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM " + tableName,
                        null
                );


        String[] columns =
                cursor.getColumnNames();


        // =================================================
        // HEADER ROW
        // =================================================

        TableRow headerRow =
                new TableRow(this);


        for (String column : columns) {

            TextView header =
                    createCell(
                            column.toUpperCase()
                    );


            header.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );


            header.setGravity(
                    Gravity.CENTER
            );


            headerRow.addView(header);
        }


        tableLayout.addView(
                headerRow
        );


        // =================================================
        // DATA ROWS
        // =================================================

        if (cursor.getCount() == 0) {

            TableRow emptyRow =
                    new TableRow(this);


            TextView emptyText =
                    createCell(
                            "No data"
                    );


            emptyText.setGravity(
                    Gravity.CENTER
            );


            emptyRow.addView(
                    emptyText
            );


            tableLayout.addView(
                    emptyRow
            );

        } else {

            while (cursor.moveToNext()) {

                TableRow dataRow =
                        new TableRow(this);


                for (
                        int i = 0;
                        i < columns.length;
                        i++
                ) {

                    String value =
                            cursor.getString(i);


                    if (value == null) {

                        value = "NULL";
                    }


                    TextView cell =
                            createCell(value);


                    dataRow.addView(
                            cell
                    );
                }


                tableLayout.addView(
                        dataRow
                );
            }
        }


        cursor.close();
    }


    // =====================================================
    // CREATE TABLE CELL
    // =====================================================

    private TextView createCell(
            String text
    ) {

        TextView textView =
                new TextView(this);


        textView.setText(
                text
        );


        textView.setTextSize(
                14
        );


        textView.setPadding(
                20,
                15,
                20,
                15
        );


        textView.setGravity(
                Gravity.CENTER_VERTICAL
        );


        textView.setBackgroundResource(
                android.R.drawable.editbox_background
        );


        TableRow.LayoutParams params =
                new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                );


        params.setMargins(
                1,
                1,
                1,
                1
        );


        textView.setLayoutParams(
                params
        );


        return textView;
    }
}