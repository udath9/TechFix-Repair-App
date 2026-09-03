package com.up9.techfix.ActorCustomer.CustomerData;

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
import com.up9.techfix.data.DatabaseHelper;

public class DatabaseViewerActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private Button btnRefresh;

    private TableLayout tableCustomers;
    private TableLayout tableServices;
    private TableLayout tableCategories;
    private TableLayout tableBranches;
    private TableLayout tableRepairs;
    private TableLayout tablePayments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_database_viewer);

        databaseHelper = new DatabaseHelper(this);

        tableCustomers = findViewById(R.id.tableCustomers);
        tableServices = findViewById(R.id.tableServices);
        tableCategories = findViewById(R.id.tableCategories);
        tableBranches = findViewById(R.id.tableBranches);
        tableRepairs = findViewById(R.id.tableRepairs);
        tablePayments = findViewById(R.id.tablePayments);

        btnRefresh = findViewById(R.id.btnRefresh);

        displayDatabase();

        btnRefresh.setOnClickListener(v -> displayDatabase());
    }

    private void displayDatabase() {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        createTable(db, "customers", tableCustomers);
        createTable(db, "services", tableServices);
        createTable(db, "categories", tableCategories);
        createTable(db, "branches", tableBranches);
        createTable(db, "repairs", tableRepairs);
        createTable(db, "payments", tablePayments);

        db.close();
    }

    private void createTable(
            SQLiteDatabase db,
            String tableName,
            TableLayout tableLayout
    ) {

        tableLayout.removeAllViews();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + tableName,
                null
        );

        String[] columns = cursor.getColumnNames();

        TableRow headerRow = new TableRow(this);

        for (String column : columns) {

            TextView header = createCell(
                    column.toUpperCase()
            );

            header.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

            header.setGravity(Gravity.CENTER);

            headerRow.addView(header);
        }

        tableLayout.addView(headerRow);

        if (cursor.getCount() == 0) {

            TableRow emptyRow = new TableRow(this);

            TextView emptyText = createCell("No data");

            emptyText.setGravity(Gravity.CENTER);

            emptyRow.addView(emptyText);

            tableLayout.addView(emptyRow);

        } else {

            while (cursor.moveToNext()) {

                TableRow dataRow = new TableRow(this);

                for (int i = 0; i < columns.length; i++) {

                    String value = cursor.getString(i);

                    if (value == null) {
                        value = "NULL";
                    }

                    dataRow.addView(
                            createCell(value)
                    );
                }

                tableLayout.addView(dataRow);
            }
        }

        cursor.close();
    }

    private TextView createCell(String text) {

        TextView textView = new TextView(this);

        textView.setText(text);
        textView.setTextSize(14);
        textView.setPadding(20, 15, 20, 15);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setBackgroundResource(
                android.R.drawable.editbox_background
        );

        TableRow.LayoutParams params =
                new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(1, 1, 1, 1);

        textView.setLayoutParams(params);

        return textView;
    }
}