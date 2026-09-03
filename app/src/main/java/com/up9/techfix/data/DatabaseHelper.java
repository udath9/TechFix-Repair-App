package com.up9.techfix.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TechFix.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create all database tables.
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE customers (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "full_name TEXT NOT NULL, " +
                        "email TEXT UNIQUE NOT NULL, " +
                        "phone TEXT, " +
                        "password TEXT NOT NULL)"
        );

        db.execSQL(
                "CREATE TABLE services (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "image_uri TEXT, " +
                        "description TEXT, " +
                        "price REAL NOT NULL)"
        );

        db.execSQL(
                "CREATE TABLE branches (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "address TEXT, " +
                        "phone TEXT, " +
                        "latitude REAL, " +
                        "longitude REAL)"
        );

        db.execSQL(
                "CREATE TABLE categories (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "description TEXT, " +
                        "price_modifier REAL NOT NULL DEFAULT 1.0)"
        );

        db.execSQL(
                "CREATE TABLE repairs (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "customer_id INTEGER, " +
                        "category_id INTEGER, " +
                        "device_model TEXT NOT NULL, " +
                        "service_id INTEGER, " +
                        "problem_description TEXT, " +
                        "branch_id INTEGER, " +
                        "image_uri TEXT, " +
                        "in_progress_photo_uri TEXT, " +
                        "assigned_technician_id INTEGER, " +
                        "status TEXT, " +
                        "repair_date TEXT)"
        );

        db.execSQL(
                "CREATE TABLE payments (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "repair_id INTEGER, " +
                        "amount REAL NOT NULL, " +
                        "payment_date TEXT, " +
                        "status TEXT)"
        );
    }

    // Update databases created with an older schema.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            try {
                db.execSQL("ALTER TABLE services ADD COLUMN image_uri TEXT");
            } catch (Exception ignored) {
                // Column already exists.
            }
        }
    }

    // Register a new customer.
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

    // Check customer login credentials.
    public boolean checkCustomerLogin(
            String email,
            String password
    ) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM customers " +
                        "WHERE email = ? AND password = ? LIMIT 1",
                new String[]{email, password}
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();

        return exists;
    }

    // Get customer ID from login credentials.
    public int getCustomerId(
            String email,
            String password
    ) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM customers " +
                        "WHERE email = ? AND password = ? LIMIT 1",
                new String[]{email, password}
        );

        int customerId = -1;

        if (cursor.moveToFirst()) {
            customerId = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
            );
        }

        cursor.close();

        return customerId;
    }

    // Get all available services.
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

    // Insert default services when the table is empty.
    public void insertDefaultServices() {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM services",
                null
        );

        boolean hasServices = false;

        if (cursor.moveToFirst()) {
            hasServices = cursor.getInt(0) > 0;
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

    // Insert a service into the database.
    private long insertService(
            SQLiteDatabase db,
            String name,
            String imageUri,
            String description,
            double price
    ) {
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("image_uri", imageUri);
        values.put("description", description);
        values.put("price", price);

        return db.insert("services", null, values);
    }

    // Get all device categories.
    public Cursor getAllCategories() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT id, name, description, price_modifier " +
                        "FROM categories ORDER BY id ASC",
                null
        );
    }

    // Get a category name by ID.
    public String getCategoryNameById(int categoryId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT name FROM categories " +
                        "WHERE id = ? LIMIT 1",
                new String[]{String.valueOf(categoryId)}
        );

        String categoryName = null;

        if (cursor.moveToFirst()) {
            categoryName = cursor.getString(
                    cursor.getColumnIndexOrThrow("name")
            );
        }

        cursor.close();

        return categoryName;
    }

    // Get the price modifier for a category.
    public double getCategoryPriceModifier(int categoryId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT price_modifier FROM categories " +
                        "WHERE id = ? LIMIT 1",
                new String[]{String.valueOf(categoryId)}
        );

        double priceModifier = 1.0;

        if (cursor.moveToFirst()) {
            priceModifier = cursor.getDouble(
                    cursor.getColumnIndexOrThrow("price_modifier")
            );
        }

        cursor.close();

        return priceModifier;
    }

    // Insert default categories when the table is empty.
    public void insertDefaultCategories() {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM categories",
                null
        );

        boolean hasCategories = false;

        if (cursor.moveToFirst()) {
            hasCategories = cursor.getInt(0) > 0;
        }

        cursor.close();

        if (hasCategories) {
            return;
        }

        insertCategory(
                db,
                "Computer",
                "Desktop computer repair services.",
                1.0
        );

        insertCategory(
                db,
                "Laptop",
                "Laptop repair services.",
                1.0
        );

        insertCategory(
                db,
                "Mobile Phone",
                "Mobile phone repair services.",
                1.0
        );

        insertCategory(
                db,
                "Tablet",
                "Tablet repair services.",
                1.0
        );
    }

    // Insert a category into the database.
    private long insertCategory(
            SQLiteDatabase db,
            String name,
            String description,
            double priceModifier
    ) {
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("description", description);
        values.put("price_modifier", priceModifier);

        return db.insert("categories", null, values);
    }

    // Create a new repair booking.
    public long createRepair(
            int customerId,
            int categoryId,
            String deviceModel,
            int serviceId,
            String problemDescription,
            int branchId,
            String imageUri,
            String status,
            String repairDate
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("customer_id", customerId);
        values.put("category_id", categoryId);
        values.put("device_model", deviceModel);
        values.put("service_id", serviceId);
        values.put("problem_description", problemDescription);
        values.put("branch_id", branchId);

        if (imageUri != null && !imageUri.trim().isEmpty()) {
            values.put("image_uri", imageUri);
        } else {
            values.putNull("image_uri");
        }

        // Technician adds the progress photo later.
        values.putNull("in_progress_photo_uri");

        // Admin assigns the technician later.
        values.putNull("assigned_technician_id");

        values.put("status", status);
        values.put("repair_date", repairDate);

        return db.insert("repairs", null, values);
    }

    // Get a branch ID by branch name.
    public int getBranchIdByName(String branchName) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM branches " +
                        "WHERE name = ? LIMIT 1",
                new String[]{branchName}
        );

        int branchId = -1;

        if (cursor.moveToFirst()) {
            branchId = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
            );
        }

        cursor.close();

        return branchId;
    }

    // Get all branches.
    public Cursor getAllBranches() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT id, name, address, phone, latitude, longitude " +
                        "FROM branches",
                null
        );
    }

    // Insert default branches when the table is empty.
    public void insertDefaultBranches() {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM branches",
                null
        );

        boolean hasBranches = false;

        if (cursor.moveToFirst()) {
            hasBranches = cursor.getInt(0) > 0;
        }

        cursor.close();

        if (hasBranches) {
            return;
        }

        ContentValues colombo = new ContentValues();

        colombo.put("name", "Colombo");
        colombo.put("address", "TechFix Colombo Branch");
        colombo.put("phone", "0112345678");
        colombo.put("latitude", 6.9271);
        colombo.put("longitude", 79.8612);

        db.insert("branches", null, colombo);

        ContentValues galle = new ContentValues();

        galle.put("name", "Galle");
        galle.put("address", "TechFix Galle Branch");
        galle.put("phone", "0912345678");
        galle.put("latitude", 6.0329);
        galle.put("longitude", 80.2168);

        db.insert("branches", null, galle);
    }

    // Get active repairs for a customer.
    public Cursor getCustomerActiveRepairs(int customerId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "r.id AS repair_id, " +
                        "r.category_id, " +
                        "c.name AS category_name, " +
                        "c.price_modifier AS price_modifier, " +
                        "r.device_model, " +
                        "r.problem_description, " +
                        "r.status, " +
                        "r.repair_date, " +
                        "r.image_uri, " +
                        "r.in_progress_photo_uri, " +
                        "r.assigned_technician_id, " +
                        "s.name AS service_name, " +
                        "s.price AS service_price, " +
                        "b.name AS branch_name " +
                        "FROM repairs r " +
                        "LEFT JOIN categories c ON r.category_id = c.id " +
                        "LEFT JOIN services s ON r.service_id = s.id " +
                        "LEFT JOIN branches b ON r.branch_id = b.id " +
                        "WHERE r.customer_id = ? " +
                        "AND r.status != 'Completed' " +
                        "AND r.status != 'Cancelled' " +
                        "ORDER BY r.id DESC",
                new String[]{String.valueOf(customerId)}
        );
    }

    // Cancel a pending repair.
    public boolean cancelRepair(
            int repairId,
            int customerId
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("status", "Cancelled");

        int rowsUpdated = db.update(
                "repairs",
                values,
                "id = ? AND customer_id = ? AND status = ?",
                new String[]{
                        String.valueOf(repairId),
                        String.valueOf(customerId),
                        "Pending"
                }
        );

        return rowsUpdated > 0;
    }

    // Get repair history for a customer.
    public Cursor getRepairHistory(int customerId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "repairs.id AS repair_id, " +
                        "repairs.category_id, " +
                        "categories.name AS category_name, " +
                        "categories.price_modifier AS price_modifier, " +
                        "repairs.device_model, " +
                        "services.name AS service_name, " +
                        "branches.name AS branch_name, " +
                        "repairs.repair_date, " +
                        "repairs.status, " +
                        "repairs.image_uri, " +
                        "repairs.in_progress_photo_uri, " +
                        "services.price, " +
                        "(services.price * categories.price_modifier) AS final_price " +
                        "FROM repairs " +
                        "LEFT JOIN categories ON repairs.category_id = categories.id " +
                        "LEFT JOIN services ON repairs.service_id = services.id " +
                        "LEFT JOIN branches ON repairs.branch_id = branches.id " +
                        "WHERE repairs.customer_id = ? " +
                        "ORDER BY repairs.id DESC",
                new String[]{String.valueOf(customerId)}
        );
    }

    // Get repairs that are ready for payment.
    public Cursor getUnpaidRepairs(int customerId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "repairs.id AS repair_id, " +
                        "repairs.category_id, " +
                        "categories.name AS category_name, " +
                        "categories.price_modifier AS price_modifier, " +
                        "repairs.device_model, " +
                        "services.name AS service_name, " +
                        "services.price AS service_price, " +
                        "(services.price * categories.price_modifier) AS amount, " +
                        "branches.name AS branch_name, " +
                        "repairs.status " +
                        "FROM repairs " +
                        "LEFT JOIN categories ON repairs.category_id = categories.id " +
                        "LEFT JOIN services ON repairs.service_id = services.id " +
                        "LEFT JOIN branches ON repairs.branch_id = branches.id " +
                        "WHERE repairs.customer_id = ? " +
                        "AND repairs.status IN ('Ready for Collection', 'Completed') " +
                        "AND repairs.id NOT IN (" +
                        "SELECT repair_id FROM payments WHERE status = 'Paid'" +
                        ") " +
                        "ORDER BY repairs.id DESC",
                new String[]{String.valueOf(customerId)}
        );
    }

    // Get payment history for a customer.
    public Cursor getPaymentHistory(int customerId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "payments.id AS payment_id, " +
                        "payments.repair_id, " +
                        "payments.amount, " +
                        "payments.payment_date, " +
                        "payments.status, " +
                        "services.name AS service_name, " +
                        "categories.name AS category_name " +
                        "FROM payments " +
                        "INNER JOIN repairs ON payments.repair_id = repairs.id " +
                        "LEFT JOIN services ON repairs.service_id = services.id " +
                        "LEFT JOIN categories ON repairs.category_id = categories.id " +
                        "WHERE repairs.customer_id = ? " +
                        "ORDER BY payments.id DESC",
                new String[]{String.valueOf(customerId)}
        );
    }

    // Create a payment record.
    public long createPayment(
            int repairId,
            double amount,
            String paymentDate,
            String status
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("repair_id", repairId);
        values.put("amount", amount);
        values.put("payment_date", paymentDate);
        values.put("status", status);

        return db.insert("payments", null, values);
    }
}