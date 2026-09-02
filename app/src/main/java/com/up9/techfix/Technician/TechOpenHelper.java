package com.up9.techfix.Technician;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TechOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Technician.db";
    private static final int DB_VERSION = 1;

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
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS RepairUpdates");
        db.execSQL("DROP TABLE IF EXISTS Repairs");
        db.execSQL("DROP TABLE IF EXISTS Technician");

        onCreate(db);
    }

    // Insert technician
    public long insertTechnician(
            String name,
            String email,
            String phone,
            String specialization) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("email", email);
        values.put("phone", phone);
        values.put("specialization", specialization);

        return db.insert("Technician", null, values);
    }

    // Insert repair
    public long insertRepair(
            String customerName,
            String customerPhone,
            String customerEmail,
            String deviceCategory,
            String deviceModel,
            String service,
            String problem,
            String branch,
            String repairDate,
            String status) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("customer_name", customerName);
        values.put("customer_phone", customerPhone);
        values.put("customer_email", customerEmail);
        values.put("device_category", deviceCategory);
        values.put("device_model", deviceModel);
        values.put("service", service);
        values.put("problem", problem);
        values.put("branch", branch);
        values.put("repair_date", repairDate);
        values.put("status", status);

        return db.insert("Repairs", null, values);
    }

    // Insert repair update
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
}