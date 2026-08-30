package com.up9.techfix.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // =====================================================
    // DATABASE INFORMATION
    // =====================================================

    private static final String DATABASE_NAME = "TechFix.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }


    // =====================================================
    // CREATE DATABASE
    // =====================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        // =================================================
        // CUSTOMERS
        // =================================================

        db.execSQL(
                "CREATE TABLE customers (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "full_name TEXT NOT NULL, " +
                        "email TEXT UNIQUE NOT NULL, " +
                        "phone TEXT, " +
                        "password TEXT NOT NULL)"
        );


        // =================================================
        // SERVICES
        // =================================================

        db.execSQL(
                "CREATE TABLE services (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "image_uri TEXT, " +
                        "description TEXT, " +
                        "price REAL NOT NULL)"
        );


        // =================================================
        // BRANCHES
        // =================================================

        db.execSQL(
                "CREATE TABLE branches (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "address TEXT, " +
                        "phone TEXT, " +
                        "latitude REAL, " +
                        "longitude REAL)"
        );


        // =================================================
        // REPAIRS
        // =================================================

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


        // =================================================
        // PAYMENTS
        // =================================================

        db.execSQL(
                "CREATE TABLE payments (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "repair_id INTEGER, " +
                        "amount REAL NOT NULL, " +
                        "payment_date TEXT, " +
                        "status TEXT)"
        );
    }


    // =====================================================
    // DATABASE UPGRADE
    // =====================================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        if (oldVersion < 2) {

            // Add image_uri only if it does not already exist.
            try {
                db.execSQL(
                        "ALTER TABLE services ADD COLUMN image_uri TEXT"
                );
            } catch (Exception ignored) {
                // Column already exists.
            }
        }
    }


    // =====================================================
    // CUSTOMER REGISTRATION
    // =====================================================

    public long registerCustomer(
            String fullName,
            String email,
            String phone,
            String password
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "full_name",
                fullName
        );

        values.put(
                "email",
                email
        );

        values.put(
                "phone",
                phone
        );

        values.put(
                "password",
                password
        );

        return db.insert(
                "customers",
                null,
                values
        );
    }


    // =====================================================
    // CHECK CUSTOMER LOGIN
    // =====================================================

    public boolean checkCustomerLogin(
            String email,
            String password
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id " +
                                "FROM customers " +
                                "WHERE email = ? " +
                                "AND password = ? " +
                                "LIMIT 1",

                        new String[]{
                                email,
                                password
                        }
                );

        boolean exists =
                cursor.moveToFirst();

        cursor.close();

        return exists;
    }


    // =====================================================
    // GET CUSTOMER ID
    // =====================================================

    public int getCustomerId(
            String email,
            String password
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id " +
                                "FROM customers " +
                                "WHERE email = ? " +
                                "AND password = ? " +
                                "LIMIT 1",

                        new String[]{
                                email,
                                password
                        }
                );

        int customerId = -1;

        if (cursor.moveToFirst()) {

            customerId =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "id"
                            )
                    );
        }

        cursor.close();

        return customerId;
    }


    // =====================================================
    // GET ALL SERVICES
    // =====================================================

    public List<Service> getAllServices() {

        List<Service> serviceList =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id, name, image_uri, " +
                                "description, price " +
                                "FROM services " +
                                "ORDER BY id ASC",
                        null
                );


        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "id"
                                )
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "name"
                                )
                        );

                String imageUri =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "image_uri"
                                )
                        );

                String description =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "description"
                                )
                        );

                double price =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        "price"
                                )
                        );

                Service service =
                        new Service(
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


    // =====================================================
    // INSERT DEFAULT SERVICES
    // =====================================================

    public void insertDefaultServices() {

        SQLiteDatabase db =
                this.getWritableDatabase();

        // Prevent duplicate default services.
        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM services",
                        null
                );

        boolean hasServices = false;

        if (cursor.moveToFirst()) {
            hasServices =
                    cursor.getInt(0) > 0;
        }

        cursor.close();

        if (hasServices) {
            return;
        }

        insertService(
                db,
                "Screen Replacement",
                "screen_replacement",
                "Replacement of cracked, damaged or broken device screens.",
                8000
        );

        insertService(
                db,
                "Battery Replacement",
                "battery_replacement",
                "Replacement of damaged, weak or faulty device batteries.",
                5000
        );

        insertService(
                db,
                "Operating System Repair",
                "operating_system",
                "Repair and troubleshooting of operating system problems.",
                3000
        );

        insertService(
                db,
                "Hardware Repair",
                "hardware_repair",
                "Diagnosis and repair of faulty internal hardware components.",
                3500
        );

        insertService(
                db,
                "Software Troubleshooting",
                "software_troubleshooting",
                "Diagnosis and resolution of software-related problems.",
                2500
        );

        insertService(
                db,
                "Virus / Malware Removal",
                "virus_removal",
                "Detection and removal of viruses, malware and other unwanted software.",
                3000
        );
    }


    // =====================================================
    // INSERT TEST SERVICES
    // =====================================================
    // Used by DatabaseViewerActivity.
    // This method intentionally allows duplicates so that
    // you can test the database viewer.
    // =====================================================

    public void insertTestServices() {

        SQLiteDatabase db =
                this.getWritableDatabase();

        insertService(
                db,
                "Screen Replacement",
                "screen_replacement",
                "Replacement of cracked, damaged or broken device screens.",
                8000
        );

        insertService(
                db,
                "Battery Replacement",
                "battery_replacement",
                "Replacement of damaged, weak or faulty device batteries.",
                5000
        );

        insertService(
                db,
                "Operating System Repair",
                "operating_system",
                "Repair and troubleshooting of operating system problems.",
                3000
        );

        insertService(
                db,
                "Hardware Repair",
                "hardware_repair",
                "Diagnosis and repair of faulty internal hardware components.",
                3500
        );

        insertService(
                db,
                "Software Troubleshooting",
                "software_troubleshooting",
                "Diagnosis and resolution of software-related problems.",
                2500
        );

        insertService(
                db,
                "Virus / Malware Removal",
                "virus_removal",
                "Detection and removal of viruses, malware and other unwanted software.",
                3000
        );

        // Test service
        insertService(
                db,
                "testing",
                "testing",
                "testing testing testing testing testing testing testing testing testing.",
                2500
        );
    }


    // =====================================================
    // HELPER: INSERT SERVICE
    // =====================================================

    private long insertService(
            SQLiteDatabase db,
            String name,
            String imageUri,
            String description,
            double price
    ) {

        ContentValues values =
                new ContentValues();

        values.put(
                "name",
                name
        );

        values.put(
                "image_uri",
                imageUri
        );

        values.put(
                "description",
                description
        );

        values.put(
                "price",
                price
        );

        return db.insert(
                "services",
                null,
                values
        );
    }


    // =====================================================
    // CREATE REPAIR
    // =====================================================

    public long createRepair(
            int customerId,
            String deviceCategory,
            String deviceModel,
            int serviceId,
            String problemDescription,
            int branchId,
            String imageUri,
            String status,
            String repairDate
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "customer_id",
                customerId
        );

        values.put(
                "device_category",
                deviceCategory
        );

        values.put(
                "device_model",
                deviceModel
        );

        values.put(
                "service_id",
                serviceId
        );

        values.put(
                "problem_description",
                problemDescription
        );

        values.put(
                "branch_id",
                branchId
        );


        if (
                imageUri != null &&
                        !imageUri.trim().isEmpty()
        ) {

            values.put(
                    "image_uri",
                    imageUri
            );

        } else {

            values.putNull(
                    "image_uri"
            );
        }


        values.put(
                "status",
                status
        );

        values.put(
                "repair_date",
                repairDate
        );


        return db.insert(
                "repairs",
                null,
                values
        );
    }


    // =====================================================
    // GET BRANCH ID BY NAME
    // =====================================================

    public int getBranchIdByName(
            String branchName
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id " +
                                "FROM branches " +
                                "WHERE name = ? " +
                                "LIMIT 1",

                        new String[]{
                                branchName
                        }
                );

        int branchId = -1;

        if (cursor.moveToFirst()) {

            branchId =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "id"
                            )
                    );
        }

        cursor.close();

        return branchId;
    }


    // =====================================================
    // INSERT DEFAULT BRANCHES
    // =====================================================

    public void insertDefaultBranches() {

        SQLiteDatabase db =
                this.getWritableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM branches",
                        null
                );

        boolean hasBranches = false;

        if (cursor.moveToFirst()) {

            hasBranches =
                    cursor.getInt(0) > 0;
        }

        cursor.close();


        if (!hasBranches) {

            // =================================================
            // COLOMBO
            // =================================================

            ContentValues colombo =
                    new ContentValues();

            colombo.put(
                    "name",
                    "Colombo"
            );

            colombo.put(
                    "address",
                    "TechFix Colombo Branch"
            );

            colombo.put(
                    "phone",
                    "0112345678"
            );

            colombo.put(
                    "latitude",
                    6.9271
            );

            colombo.put(
                    "longitude",
                    79.8612
            );

            db.insert(
                    "branches",
                    null,
                    colombo
            );


            // =================================================
            // GALLE
            // =================================================

            ContentValues galle =
                    new ContentValues();

            galle.put(
                    "name",
                    "Galle"
            );

            galle.put(
                    "address",
                    "TechFix Galle Branch"
            );

            galle.put(
                    "phone",
                    "0912345678"
            );

            galle.put(
                    "latitude",
                    6.0329
            );

            galle.put(
                    "longitude",
                    80.2168
            );

            db.insert(
                    "branches",
                    null,
                    galle
            );
        }
    }
}