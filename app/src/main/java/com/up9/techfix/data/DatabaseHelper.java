package com.up9.techfix.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database information
    private static final String DATABASE_NAME = "TechFix.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Customers table
        db.execSQL(
                "CREATE TABLE customers (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "full_name TEXT NOT NULL, " +
                        "email TEXT UNIQUE NOT NULL, " +
                        "phone TEXT, " +
                        "password TEXT NOT NULL)"
        );

        // Services table
        db.execSQL(
                "CREATE TABLE services (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "image_uri TEXT, " +
                        "description TEXT, " +
                        "price REAL NOT NULL)"
        );

        // Branches table
        db.execSQL(
                "CREATE TABLE branches (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "address TEXT, " +
                        "phone TEXT, " +
                        "latitude REAL, " +
                        "longitude REAL)"
        );

        // Repairs table
        db.execSQL(
                "CREATE TABLE repairs (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "customer_id INTEGER, " +
                        "device_category TEXT NOT NULL, " +
                        "device_model TEXT NOT NULL, " +
                        "service_id INTEGER, " +
                        "problem_description TEXT, " +
                        "branch_id INTEGER, " +
                        "image_uri TEXT, " +
                        "status TEXT, " +
                        "repair_date TEXT)"
        );

        // Payments table
        db.execSQL(
                "CREATE TABLE payments (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "repair_id INTEGER, " +
                        "amount REAL NOT NULL, " +
                        "payment_date TEXT, " +
                        "status TEXT)"
        );
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        if (oldVersion < 2) {
            db.execSQL(
                    "ALTER TABLE services ADD COLUMN image_uri TEXT"
            );
        }
    }
    public long registerCustomer(
            String fullName,
            String email,
            String phone,
            String password
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("full_name", fullName);
        values.put("email", email);
        values.put("phone", phone);
        values.put("password", password);

        return db.insert("customers", null, values);
    }
    public boolean checkCustomerLogin(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM customers WHERE email = ? AND password = ?",
                new String[]{email, password}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }
    public List<Service> getAllServices() {

        List<Service> serviceList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, name, image_uri, description, price " +
                        "FROM services ORDER BY id ASC",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name")
                );

                String imageUri = cursor.getString(
                        cursor.getColumnIndexOrThrow("image_uri")
                );

                String description = cursor.getString(
                        cursor.getColumnIndexOrThrow("description")
                );

                double price = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("price")
                );

                Service service = new Service(
                        id,
                        name,
                        imageUri,
                        description,
                        price
                );

                serviceList.add(service);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return serviceList;
    }
}