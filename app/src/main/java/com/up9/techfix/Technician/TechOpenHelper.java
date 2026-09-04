package com.up9.techfix.Technician;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TechOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Technician.db";
    private static final int DB_VERSION = 2;

    public TechOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE Technician (" +
                        "technician_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, " +
                        "email TEXT, " +
                        "phone TEXT, " +
                        "specialization TEXT)"
        );

        db.execSQL(
                "CREATE TABLE Repairs (" +
                        "repair_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "customer_name TEXT, " +
                        "customer_phone TEXT, " +
                        "customer_email TEXT, " +
                        "device_category TEXT, " +
                        "device_model TEXT, " +
                        "service TEXT, " +
                        "problem TEXT, " +
                        "branch TEXT, " +
                        "repair_date TEXT, " +
                        "status TEXT)"
        );

        db.execSQL(
                "CREATE TABLE RepairUpdates (" +
                        "update_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "repair_id INTEGER, " +
                        "status TEXT, " +
                        "notes TEXT, " +
                        "spare_part TEXT, " +
                        "quantity INTEGER, " +
                        "photo TEXT)"
        );


        insertSampleRepairs(db);
    }

    private void insertSampleRepairs(SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put("customer_name", "Nimal Perera");
        values.put("customer_phone", "0771234567");
        values.put("customer_email", "nimal@gmail.com");
        values.put("device_category", "Mobile Phone");
        values.put("device_model", "Samsung Galaxy A54");
        values.put("service", "Screen Replacement");
        values.put("problem", "Screen is cracked");
        values.put("branch", "Colombo");
        values.put("repair_date", "30 August 2026");
        values.put("status", "Assigned");

        db.insert("Repairs", null, values);


        ContentValues values2 = new ContentValues();

        values2.put("customer_name", "Kamal Silva");
        values2.put("customer_phone", "0712345678");
        values2.put("customer_email", "kamal@gmail.com");
        values2.put("device_category", "Laptop");
        values2.put("device_model", "Dell Inspiron 15");
        values2.put("service", "Windows Installation");
        values2.put("problem", "Windows system problem");
        values2.put("branch", "Colombo");
        values2.put("repair_date", "31 August 2026");
        values2.put("status", "In Progress");

        db.insert("Repairs", null, values2);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS RepairUpdates");
        db.execSQL("DROP TABLE IF EXISTS Repairs");
        db.execSQL("DROP TABLE IF EXISTS Technician");

        onCreate(db);
    }


    public Cursor getAllRepairs() {

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM Repairs",
                null
        );
    }


    public Cursor getRepairsByStatus(String status) {

        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                "Repairs",
                null,
                "status = ?",
                new String[]{status},
                null,
                null,
                "repair_id DESC"
        );
    }


    public Cursor getRepairById(int repairId) {

        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                "Repairs",
                null,
                "repair_id = ?",
                new String[]{String.valueOf(repairId)},
                null,
                null,
                null
        );
    }


    public boolean updateRepairStatus(int repairId, String newStatus) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("status", newStatus);

        int result = db.update(
                "Repairs",
                values,
                "repair_id = ?",
                new String[]{String.valueOf(repairId)}
        );

        return result > 0;
    }


    public long insertRepairUpdate(
            int repairId,
            String status,
            String notes,
            String sparePart,
            int quantity,
            String photo) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("repair_id", repairId);
        values.put("status", status);
        values.put("notes", notes);
        values.put("spare_part", sparePart);
        values.put("quantity", quantity);
        values.put("photo", photo);

        return db.insert("RepairUpdates", null, values);
    }


    public int getRepairCountByStatus(String status) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM Repairs WHERE status = ?",
                new String[]{status}
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();

        return count;
    }
}