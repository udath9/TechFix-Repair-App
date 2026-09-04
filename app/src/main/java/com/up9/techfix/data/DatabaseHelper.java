package com.up9.techfix.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.up9.techfix.admin.categories.DeviceCategory;
import com.up9.techfix.admin.repairsamples.RepairSample;
import com.up9.techfix.admin.services.RepairService;
import com.up9.techfix.admin.spareparts.SparePart;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // ============================================================
    // DATABASE
    // ============================================================

    private static final String DATABASE_NAME = "TechFix.db";

    /*
     * IMPORTANT:
     * Increase this whenever the database structure changes.
     */
    private static final int DATABASE_VERSION = 12;


    // ============================================================
    // TABLE NAMES
    // ============================================================

    public static final String TABLE_CUSTOMERS = "customers";
    public static final String TABLE_BRANCHES = "branches";
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_SERVICES = "services";
    public static final String TABLE_REPAIRS = "repairs";
    public static final String TABLE_PAYMENTS = "payments";
    public static final String TABLE_REPAIR_SAMPLES = "repair_samples";
    public static final String TABLE_SPARE_PARTS = "spare_parts";
    public static final String TABLE_USERS = "users";

    // ============================================================
// USERS / AUTHENTICATION
// ============================================================


    public static final String COL_USER_ID = "id";
    public static final String COL_USER_FULL_NAME = "full_name";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_PHONE = "phone";
    public static final String COL_USER_PASSWORD = "password";
    public static final String COL_USER_ROLE = "role";
    // ============================================================
    // CUSTOMER COLUMNS
    // ============================================================

    public static final String COL_CUSTOMER_ID = "id";
    public static final String COL_CUSTOMER_FULL_NAME = "full_name";
    public static final String COL_CUSTOMER_EMAIL = "email";
    public static final String COL_CUSTOMER_PHONE = "phone";
    public static final String COL_CUSTOMER_PASSWORD = "password";


    // ============================================================
    // BRANCH COLUMNS
    // ============================================================

    public static final String COL_BRANCH_ID = "id";
    public static final String COL_BRANCH_NAME = "name";
    public static final String COL_BRANCH_ADDRESS = "address";
    public static final String COL_BRANCH_PHONE = "phone";
    public static final String COL_BRANCH_LATITUDE = "latitude";
    public static final String COL_BRANCH_LONGITUDE = "longitude";


    // ============================================================
    // CATEGORY COLUMNS
    // ============================================================

    public static final String COL_CATEGORY_ID = "id";
    public static final String COL_CATEGORY_NAME = "name";
    public static final String COL_CATEGORY_DESCRIPTION = "description";
    public static final String COL_CATEGORY_PRICE_MODIFIER = "price_modifier";


    // ============================================================
    // SERVICE COLUMNS
    // ============================================================

    public static final String COL_SERVICE_ID = "id";
    public static final String COL_SERVICE_NAME = "name";
    public static final String COL_SERVICE_IMAGE_URI = "image_uri";
    public static final String COL_SERVICE_DESCRIPTION = "description";
    public static final String COL_SERVICE_PRICE = "price";
    public static final String COL_SERVICE_ESTIMATED_DAYS = "estimated_days";


    // ============================================================
    // REPAIR SAMPLE COLUMNS
    // ============================================================

    public static final String COL_SAMPLE_ID = "id";
    public static final String COL_SAMPLE_DEVICE_NAME = "device_name";
    public static final String COL_SAMPLE_CATEGORY = "category";
    public static final String COL_SAMPLE_SERVICE = "service";
    public static final String COL_SAMPLE_DESCRIPTION = "description";
    public static final String COL_SAMPLE_IMAGE_URI = "image_uri";


    // ============================================================
    // SPARE PART COLUMNS
    // ============================================================

    public static final String COL_SPARE_PART_ID = "id";
    public static final String COL_SPARE_PART_NAME = "name";
    public static final String COL_SPARE_PART_CATEGORY = "category";
    public static final String COL_SPARE_PART_NUMBER = "part_number";
    public static final String COL_SPARE_PART_QUANTITY = "quantity";
    public static final String COL_SPARE_PART_MINIMUM_STOCK = "minimum_stock";
    public static final String COL_SPARE_PART_UNIT_PRICE = "unit_price";
    public static final String COL_SPARE_PART_SUPPLIER = "supplier";
    public static final String COL_SPARE_PART_DESCRIPTION = "description";
    public static final String COL_SPARE_PART_IMAGE_URI = "image_uri";


    // ============================================================
    // REPAIR COLUMNS
    // ============================================================

    public static final String COL_REPAIR_ID = "id";
    public static final String COL_REPAIR_CUSTOMER_ID = "customer_id";
    public static final String COL_REPAIR_CATEGORY_ID = "category_id";
    public static final String COL_REPAIR_DEVICE_MODEL = "device_model";
    public static final String COL_REPAIR_SERVICE_ID = "service_id";
    public static final String COL_REPAIR_PROBLEM_DESCRIPTION = "problem_description";
    public static final String COL_REPAIR_BRANCH_ID = "branch_id";
    public static final String COL_REPAIR_IMAGE_URI = "image_uri";
    public static final String COL_REPAIR_IN_PROGRESS_PHOTO_URI =
            "in_progress_photo_uri";
    public static final String COL_REPAIR_ASSIGNED_TECHNICIAN_ID =
            "assigned_technician_id";
    public static final String COL_REPAIR_STATUS = "status";
    public static final String COL_REPAIR_DATE = "repair_date";


    // ============================================================
    // PAYMENT COLUMNS
    // ============================================================

    public static final String COL_PAYMENT_ID = "id";
    public static final String COL_PAYMENT_REPAIR_ID = "repair_id";
    public static final String COL_PAYMENT_AMOUNT = "amount";
    public static final String COL_PAYMENT_DATE = "payment_date";
    public static final String COL_PAYMENT_STATUS = "status";


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public DatabaseHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }


    // ============================================================
    // ON CREATE
    // ============================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        createUsersTable(db);

        createCustomersTable(db);
        createBranchesTable(db);
        createCategoriesTable(db);
        createServicesTable(db);
        createRepairSamplesTable(db);
        createSparePartsTable(db);
        createRepairsTable(db);
        createPaymentsTable(db);

        insertDefaultAdmin(db);
        insertDefaultTechnician(db);
    }


    // ============================================================
    // ON UPGRADE
    // ============================================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        // Authentication
        createUsersTable(db);

        // Existing shared tables
        createCustomersTable(db);
        createBranchesTable(db);
        createCategoriesTable(db);
        createServicesTable(db);
        createRepairSamplesTable(db);
        createSparePartsTable(db);
        createRepairsTable(db);
        createPaymentsTable(db);

        // --------------------------------------------------------
        // Services
        // --------------------------------------------------------

        if (!columnExists(
                db,
                TABLE_SERVICES,
                COL_SERVICE_IMAGE_URI
        )) {

            db.execSQL(
                    "ALTER TABLE " + TABLE_SERVICES +
                            " ADD COLUMN " +
                            COL_SERVICE_IMAGE_URI +
                            " TEXT"
            );
        }

        if (!columnExists(
                db,
                TABLE_SERVICES,
                COL_SERVICE_ESTIMATED_DAYS
        )) {

            db.execSQL(
                    "ALTER TABLE " + TABLE_SERVICES +
                            " ADD COLUMN " +
                            COL_SERVICE_ESTIMATED_DAYS +
                            " INTEGER NOT NULL DEFAULT 1"
            );
        }

        // --------------------------------------------------------
        // Categories
        // --------------------------------------------------------

        if (!columnExists(
                db,
                TABLE_CATEGORIES,
                COL_CATEGORY_PRICE_MODIFIER
        )) {

            db.execSQL(
                    "ALTER TABLE " + TABLE_CATEGORIES +
                            " ADD COLUMN " +
                            COL_CATEGORY_PRICE_MODIFIER +
                            " REAL NOT NULL DEFAULT 1.0"
            );
        }

        db.execSQL(
                "UPDATE " + TABLE_CATEGORIES +
                        " SET price_modifier = 1.0 " +
                        "WHERE price_modifier = 0"
        );

        // --------------------------------------------------------
        // Repair Samples
        // --------------------------------------------------------

        if (!columnExists(
                db,
                TABLE_REPAIR_SAMPLES,
                COL_SAMPLE_IMAGE_URI
        )) {

            db.execSQL(
                    "ALTER TABLE " + TABLE_REPAIR_SAMPLES +
                            " ADD COLUMN image_uri TEXT"
            );
        }

        // --------------------------------------------------------
        // Spare Parts
        // --------------------------------------------------------

        if (!columnExists(
                db,
                TABLE_SPARE_PARTS,
                COL_SPARE_PART_IMAGE_URI
        )) {

            db.execSQL(
                    "ALTER TABLE " + TABLE_SPARE_PARTS +
                            " ADD COLUMN image_uri TEXT"
            );
        }

        // --------------------------------------------------------
        // Create default accounts if they don't exist
        // --------------------------------------------------------

        insertDefaultAdmin(db);
        insertDefaultTechnician(db);
    }


    private void createUsersTable(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_USERS +
                        " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "full_name TEXT NOT NULL, " +
                        "email TEXT UNIQUE NOT NULL, " +
                        "phone TEXT, " +
                        "password TEXT NOT NULL, " +
                        "role TEXT NOT NULL" +
                        ")"
        );
    }
    private void createCustomersTable(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_CUSTOMERS +
                        " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "full_name TEXT NOT NULL, " +
                        "email TEXT UNIQUE NOT NULL, " +
                        "phone TEXT, " +
                        "password TEXT NOT NULL" +
                        ")"
        );
    }


    private void createBranchesTable(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_BRANCHES +
                        " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "address TEXT, " +
                        "phone TEXT, " +
                        "latitude REAL, " +
                        "longitude REAL" +
                        ")"
        );
    }


    private void createCategoriesTable(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_CATEGORIES +
                        " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "description TEXT, " +
                        "price_modifier REAL NOT NULL DEFAULT 1.0" +
                        ")"
        );
    }


    private void createServicesTable(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_SERVICES +
                        " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "image_uri TEXT, " +
                        "description TEXT, " +
                        "price REAL NOT NULL, " +
                        "estimated_days INTEGER NOT NULL DEFAULT 1" +
                        ")"
        );
    }


    private void createRepairSamplesTable(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_REPAIR_SAMPLES +
                        " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "device_name TEXT NOT NULL, " +
                        "category TEXT, " +
                        "service TEXT, " +
                        "description TEXT, " +
                        "image_uri TEXT" +
                        ")"
        );
    }


    private void createSparePartsTable(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_SPARE_PARTS +
                        " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "category TEXT, " +
                        "part_number TEXT, " +
                        "quantity INTEGER NOT NULL DEFAULT 0, " +
                        "minimum_stock INTEGER NOT NULL DEFAULT 0, " +
                        "unit_price REAL NOT NULL DEFAULT 0, " +
                        "supplier TEXT, " +
                        "description TEXT, " +
                        "image_uri TEXT" +
                        ")"
        );
    }


    private void createRepairsTable(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_REPAIRS +
                        " (" +
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
                        "repair_date TEXT" +
                        ")"
        );
    }


    private void createPaymentsTable(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_PAYMENTS +
                        " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "repair_id INTEGER, " +
                        "amount REAL NOT NULL, " +
                        "payment_date TEXT, " +
                        "status TEXT" +
                        ")"
        );
    }


    // ============================================================
    // DATABASE HELPERS
    // ============================================================

    private boolean columnExists(
            SQLiteDatabase db,
            String tableName,
            String columnName
    ) {

        Cursor cursor = db.rawQuery(
                "PRAGMA table_info(" + tableName + ")",
                null
        );

        boolean exists = false;

        while (cursor.moveToNext()) {

            String name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "name"
                            )
                    );

            if (columnName.equals(name)) {
                exists = true;
                break;
            }
        }

        cursor.close();

        return exists;
    }

    // ============================================================
// LOGIN USER MODEL
// ============================================================

    public static class LoginUser {

        private final int id;
        private final String fullName;
        private final String email;
        private final String phone;
        private final String role;

        public LoginUser(
                int id,
                String fullName,
                String email,
                String phone,
                String role
        ) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.phone = phone;
            this.role = role;
        }

        public int getId() {
            return id;
        }

        public String getFullName() {
            return fullName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getRole() {
            return role;
        }
    }
    // ============================================================
// LOGIN - ALL ROLES
// ============================================================

    public LoginUser loginUser(
            String email,
            String password
    ) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, full_name, email, phone, role " +
                        "FROM " + TABLE_USERS +
                        " WHERE email = ? " +
                        "AND password = ? " +
                        "LIMIT 1",
                new String[]{
                        email,
                        password
                }
        );

        LoginUser user = null;

        if (cursor.moveToFirst()) {

            int id = cursor.getInt(0);

            String fullName = cursor.getString(1);

            String userEmail = cursor.getString(2);

            String phone = cursor.getString(3);

            String role = cursor.getString(4);

            user = new LoginUser(
                    id,
                    fullName,
                    userEmail,
                    phone,
                    role
            );
        }

        cursor.close();

        return user;
    }
    // ============================================================
// CUSTOMER REGISTRATION
// ============================================================

    public long registerCustomer(
            String fullName,
            String email,
            String phone,
            String password
    ) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {

            // ----------------------------------------------------
            // Check whether email already exists
            // ----------------------------------------------------

            Cursor checkCursor = db.rawQuery(
                    "SELECT id FROM " + TABLE_USERS +
                            " WHERE email = ? LIMIT 1",
                    new String[]{
                            email
                    }
            );

            boolean emailExists = checkCursor.moveToFirst();

            checkCursor.close();

            if (emailExists) {
                return -1;
            }

            // ----------------------------------------------------
            // Create USER account
            // ----------------------------------------------------

            ContentValues userValues = new ContentValues();

            userValues.put(
                    COL_USER_FULL_NAME,
                    fullName
            );

            userValues.put(
                    COL_USER_EMAIL,
                    email
            );

            userValues.put(
                    COL_USER_PHONE,
                    phone
            );

            userValues.put(
                    COL_USER_PASSWORD,
                    password
            );

            userValues.put(
                    COL_USER_ROLE,
                    "CUSTOMER"
            );

            long userId = db.insert(
                    TABLE_USERS,
                    null,
                    userValues
            );

            if (userId == -1) {
                return -1;
            }

            // ----------------------------------------------------
            // Create CUSTOMER profile
            // ----------------------------------------------------

            ContentValues customerValues = new ContentValues();

            customerValues.put(
                    COL_CUSTOMER_FULL_NAME,
                    fullName
            );

            customerValues.put(
                    COL_CUSTOMER_EMAIL,
                    email
            );

            customerValues.put(
                    COL_CUSTOMER_PHONE,
                    phone
            );

            customerValues.put(
                    COL_CUSTOMER_PASSWORD,
                    password
            );

            long customerId = db.insert(
                    TABLE_CUSTOMERS,
                    null,
                    customerValues
            );

            if (customerId == -1) {

                db.delete(
                        TABLE_USERS,
                        "id = ?",
                        new String[]{
                                String.valueOf(userId)
                        }
                );

                return -1;
            }

            db.setTransactionSuccessful();

            return customerId;

        } catch (Exception e) {

            return -1;

        } finally {

            db.endTransaction();
        }
    }

    // ============================================================
    // CUSTOMER LOGIN
    // ============================================================

// ============================================================
// CUSTOMER LOGIN - COMPATIBILITY
// ============================================================

    public boolean checkCustomerLogin(
            String email,
            String password
    ) {

        LoginUser user = loginUser(
                email,
                password
        );

        return user != null &&
                "CUSTOMER".equalsIgnoreCase(
                        user.getRole()
                );
    }
    // ============================================================
// DEFAULT ADMIN ACCOUNT
// ============================================================

    private void insertDefaultAdmin(SQLiteDatabase db) {

        Cursor cursor = db.rawQuery(
                "SELECT id FROM " + TABLE_USERS +
                        " WHERE email = ? LIMIT 1",
                new String[]{
                        "admin@techfix.com"
                }
        );

        boolean exists = cursor.moveToFirst();

        cursor.close();

        if (exists) {
            return;
        }

        ContentValues values = new ContentValues();

        values.put(
                COL_USER_FULL_NAME,
                "TechFix Administrator"
        );

        values.put(
                COL_USER_EMAIL,
                "admin@techfix.com"
        );

        values.put(
                COL_USER_PHONE,
                "0112345678"
        );

        values.put(
                COL_USER_PASSWORD,
                "admin123"
        );

        values.put(
                COL_USER_ROLE,
                "ADMIN"
        );

        db.insert(
                TABLE_USERS,
                null,
                values
        );
    }
    // ============================================================
// DEFAULT TECHNICIAN ACCOUNT
// ============================================================

    private void insertDefaultTechnician(SQLiteDatabase db) {

        Cursor cursor = db.rawQuery(
                "SELECT id FROM " + TABLE_USERS +
                        " WHERE email = ? LIMIT 1",
                new String[]{
                        "technician@techfix.com"
                }
        );

        boolean exists = cursor.moveToFirst();

        cursor.close();

        if (exists) {
            return;
        }

        ContentValues values = new ContentValues();

        values.put(
                COL_USER_FULL_NAME,
                "TechFix Technician"
        );

        values.put(
                COL_USER_EMAIL,
                "technician@techfix.com"
        );

        values.put(
                COL_USER_PHONE,
                "0771234567"
        );

        values.put(
                COL_USER_PASSWORD,
                "tech123"
        );

        values.put(
                COL_USER_ROLE,
                "TECHNICIAN"
        );

        db.insert(
                TABLE_USERS,
                null,
                values
        );
    }


    public int getCustomerId(
            String email,
            String password
    ) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT c.id " +
                        "FROM " + TABLE_CUSTOMERS + " c " +
                        "INNER JOIN " + TABLE_USERS + " u " +
                        "ON c.email = u.email " +
                        "WHERE u.email = ? " +
                        "AND u.password = ? " +
                        "AND u.role = 'CUSTOMER' " +
                        "LIMIT 1",
                new String[]{
                        email,
                        password
                }
        );

        int customerId = -1;

        if (cursor.moveToFirst()) {
            customerId = cursor.getInt(0);
        }

        cursor.close();

        return customerId;
    }


    // ============================================================
    // GET CUSTOMER ID BY EMAIL
    // ============================================================

    public int getCustomerId(String email) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id FROM " +
                                TABLE_CUSTOMERS +
                                " WHERE email = ? " +
                                "LIMIT 1",
                        new String[]{
                                email
                        }
                );

        int customerId = -1;

        if (cursor.moveToFirst()) {

            customerId =
                    cursor.getInt(0);
        }

        cursor.close();

        return customerId;
    }


    // ============================================================
    // CUSTOMER SERVICES
    // ============================================================

    /*
     * This method is intentionally for the CUSTOMER side.
     *
     * Customer Service model:
     *
     * Service(
     *     id,
     *     name,
     *     imageUri,
     *     description,
     *     price
     * )
     */
    public List<Service> getAllServices() {

        List<Service> services =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id, name, image_uri, " +
                                "description, price " +
                                "FROM " + TABLE_SERVICES +
                                " ORDER BY id ASC",
                        null
                );

        while (cursor.moveToNext()) {

            int id =
                    cursor.getInt(0);

            String name =
                    cursor.getString(1);

            String imageUri =
                    cursor.getString(2);

            String description =
                    cursor.getString(3);

            double price =
                    cursor.getDouble(4);

            services.add(
                    new Service(
                            id,
                            name,
                            imageUri,
                            description,
                            price
                    )
            );
        }

        cursor.close();

        return services;
    }


    // ============================================================
    // INSERT DEFAULT SERVICES
    // ============================================================

    public void insertDefaultServices() {

        SQLiteDatabase db =
                getWritableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM " +
                                TABLE_SERVICES,
                        null
                );

        boolean empty = true;

        if (cursor.moveToFirst()) {
            empty = cursor.getInt(0) == 0;
        }

        cursor.close();

        if (!empty) {
            return;
        }

        insertService(
                db,
                "Screen Replacement",
                "",
                "Replace damaged or broken screens.",
                15000,
                2
        );

        insertService(
                db,
                "Battery Replacement",
                "",
                "Replace damaged or weak batteries.",
                8000,
                1
        );

        insertService(
                db,
                "Software Repair",
                "",
                "Operating system and software related repairs.",
                5000,
                1
        );

        insertService(
                db,
                "Hardware Repair",
                "",
                "General hardware diagnosis and repair.",
                10000,
                3
        );
    }


    private long insertService(
            SQLiteDatabase db,
            String name,
            String imageUri,
            String description,
            double price,
            int estimatedDays
    ) {

        ContentValues values =
                new ContentValues();

        values.put(
                COL_SERVICE_NAME,
                name
        );

        values.put(
                COL_SERVICE_IMAGE_URI,
                imageUri
        );

        values.put(
                COL_SERVICE_DESCRIPTION,
                description
        );

        values.put(
                COL_SERVICE_PRICE,
                price
        );

        values.put(
                COL_SERVICE_ESTIMATED_DAYS,
                estimatedDays
        );

        return db.insert(
                TABLE_SERVICES,
                null,
                values
        );
    }


    // ============================================================
    // ADMIN SERVICE - INSERT
    // ============================================================

    public long insertService(
            String name,
            String imageUri,
            String description,
            double price,
            int estimatedDays
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_SERVICE_NAME,
                name
        );

        values.put(
                COL_SERVICE_IMAGE_URI,
                imageUri
        );

        values.put(
                COL_SERVICE_DESCRIPTION,
                description
        );

        values.put(
                COL_SERVICE_PRICE,
                price
        );

        values.put(
                COL_SERVICE_ESTIMATED_DAYS,
                estimatedDays
        );

        return db.insert(
                TABLE_SERVICES,
                null,
                values
        );
    }


    // ============================================================
    // ADMIN SERVICE - GET
    // ============================================================

    public List<RepairService> getAllServiceModels() {

        List<RepairService> services =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id, name, image_uri, " +
                                "description, price, " +
                                "estimated_days " +
                                "FROM " + TABLE_SERVICES +
                                " ORDER BY id ASC",
                        null
                );

        while (cursor.moveToNext()) {

            int id =
                    cursor.getInt(0);

            String name =
                    cursor.getString(1);

            String imageUri =
                    cursor.getString(2);

            String description =
                    cursor.getString(3);

            double price =
                    cursor.getDouble(4);

            int estimatedDays =
                    cursor.getInt(5);

            services.add(
                    new RepairService(
                            id,
                            name,
                            imageUri,
                            description,
                            price,
                            estimatedDays
                    )
            );
        }

        cursor.close();

        return services;
    }


    // ============================================================
    // ADMIN SERVICE - UPDATE
    // ============================================================

    public int updateService(
            int id,
            String name,
            String imageUri,
            String description,
            double price,
            int estimatedDays
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_SERVICE_NAME,
                name
        );

        values.put(
                COL_SERVICE_IMAGE_URI,
                imageUri
        );

        values.put(
                COL_SERVICE_DESCRIPTION,
                description
        );

        values.put(
                COL_SERVICE_PRICE,
                price
        );

        values.put(
                COL_SERVICE_ESTIMATED_DAYS,
                estimatedDays
        );

        return db.update(
                TABLE_SERVICES,
                values,
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }


    // ============================================================
    // ADMIN SERVICE - DELETE
    // ============================================================

    public int deleteService(int id) {

        SQLiteDatabase db =
                getWritableDatabase();

        return db.delete(
                TABLE_SERVICES,
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }


    // ============================================================
    // CUSTOMER CATEGORIES
    // ============================================================

    /*
     * Customer BookRepairActivity uses Cursor.
     */
    public Cursor getAllCategories() {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT id, name, description, " +
                        "price_modifier " +
                        "FROM " + TABLE_CATEGORIES +
                        " ORDER BY id ASC",
                null
        );
    }


    // ============================================================
    // ADMIN CATEGORIES - INSERT
    // ============================================================

    public long insertCategory(
            String name,
            String description,
            double priceModifier
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_CATEGORY_NAME,
                name
        );

        values.put(
                COL_CATEGORY_DESCRIPTION,
                description
        );

        values.put(
                COL_CATEGORY_PRICE_MODIFIER,
                priceModifier
        );

        return db.insert(
                TABLE_CATEGORIES,
                null,
                values
        );
    }


    // ============================================================
    // ADMIN CATEGORIES - GET
    // ============================================================

    public List<DeviceCategory> getAllCategoryModels() {

        List<DeviceCategory> categories =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id, name, description, " +
                                "price_modifier " +
                                "FROM " + TABLE_CATEGORIES +
                                " ORDER BY id ASC",
                        null
                );

        while (cursor.moveToNext()) {

            int id =
                    cursor.getInt(0);

            String name =
                    cursor.getString(1);

            String description =
                    cursor.getString(2);

            double priceModifier =
                    cursor.getDouble(3);

            categories.add(
                    new DeviceCategory(
                            id,
                            name,
                            description,
                            priceModifier
                    )
            );
        }

        cursor.close();

        return categories;
    }


    // ============================================================
    // CATEGORY NAME
    // ============================================================

    public String getCategoryNameById(
            int categoryId
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT name FROM " +
                                TABLE_CATEGORIES +
                                " WHERE id = ? " +
                                "LIMIT 1",
                        new String[]{
                                String.valueOf(categoryId)
                        }
                );

        String name = "";

        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }

        cursor.close();

        return name;
    }


    // ============================================================
    // CATEGORY PRICE MODIFIER
    // ============================================================

    public double getCategoryPriceModifier(
            int categoryId
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT price_modifier FROM " +
                                TABLE_CATEGORIES +
                                " WHERE id = ? " +
                                "LIMIT 1",
                        new String[]{
                                String.valueOf(categoryId)
                        }
                );

        double modifier = 1.0;

        if (cursor.moveToFirst()) {
            modifier = cursor.getDouble(0);
        }

        cursor.close();

        return modifier;
    }


    // ============================================================
    // DEFAULT CATEGORIES
    // ============================================================

    public void insertDefaultCategories() {

        SQLiteDatabase db =
                getWritableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM " +
                                TABLE_CATEGORIES,
                        null
                );

        boolean empty = true;

        if (cursor.moveToFirst()) {
            empty = cursor.getInt(0) == 0;
        }

        cursor.close();

        if (!empty) {
            return;
        }

        insertCategory(
                db,
                "Mobile Phone",
                "Mobile phone repair category.",
                1.0
        );

        insertCategory(
                db,
                "Laptop",
                "Laptop repair category.",
                1.0
        );

        insertCategory(
                db,
                "Desktop",
                "Desktop computer repair category.",
                1.0
        );

        insertCategory(
                db,
                "Tablet",
                "Tablet repair category.",
                1.0
        );
    }


    private long insertCategory(
            SQLiteDatabase db,
            String name,
            String description,
            double priceModifier
    ) {

        ContentValues values =
                new ContentValues();

        values.put(
                COL_CATEGORY_NAME,
                name
        );

        values.put(
                COL_CATEGORY_DESCRIPTION,
                description
        );

        values.put(
                COL_CATEGORY_PRICE_MODIFIER,
                priceModifier
        );

        return db.insert(
                TABLE_CATEGORIES,
                null,
                values
        );
    }


    // ============================================================
    // ADMIN CATEGORY - UPDATE
    // ============================================================

    public int updateCategory(
            int id,
            String name,
            String description,
            double priceModifier
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_CATEGORY_NAME,
                name
        );

        values.put(
                COL_CATEGORY_DESCRIPTION,
                description
        );

        values.put(
                COL_CATEGORY_PRICE_MODIFIER,
                priceModifier
        );

        return db.update(
                TABLE_CATEGORIES,
                values,
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }


    // ============================================================
    // ADMIN CATEGORY - DELETE
    // ============================================================

    public int deleteCategory(int id) {

        SQLiteDatabase db =
                getWritableDatabase();

        return db.delete(
                TABLE_CATEGORIES,
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }


    // ============================================================
    // CUSTOMER BRANCHES
    // ============================================================

    public List<Branch> getAllBranches() {

        List<Branch> branchList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, name, address, phone, latitude, longitude " +
                        "FROM branches " +
                        "ORDER BY id ASC",
                null
        );

        try {

            while (cursor.moveToNext()) {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name")
                );

                String address = cursor.getString(
                        cursor.getColumnIndexOrThrow("address")
                );

                String phone = cursor.getString(
                        cursor.getColumnIndexOrThrow("phone")
                );

                double latitude = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("latitude")
                );

                double longitude = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("longitude")
                );

                Branch branch = new Branch(
                        id,
                        name,
                        address,
                        phone,
                        latitude,
                        longitude
                );

                branchList.add(branch);
            }

        } finally {

            cursor.close();
        }

        return branchList;
    }


    // ============================================================
    // GET BRANCH ID
    // ============================================================

    public int getBranchIdByName(
            String branchName
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id FROM " +
                                TABLE_BRANCHES +
                                " WHERE name = ? " +
                                "LIMIT 1",
                        new String[]{
                                branchName
                        }
                );

        int branchId = -1;

        if (cursor.moveToFirst()) {
            branchId = cursor.getInt(0);
        }

        cursor.close();

        return branchId;
    }


    public long insertBranch(
            String name,
            String address,
            String phone,
            double latitude,
            double longitude
    ) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("address", address);
        values.put("phone", phone);
        values.put("latitude", latitude);
        values.put("longitude", longitude);

        return db.insert(
                "branches",
                null,
                values
        );
    }


    // ============================================================
    // ADMIN BRANCH - GET
    // ============================================================

    public List<Branch> getAllBranchModels() {

        List<Branch> branches =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id, name, address, phone, " +
                                "latitude, longitude " +
                                "FROM " + TABLE_BRANCHES +
                                " ORDER BY id ASC",
                        null
                );

        while (cursor.moveToNext()) {

            int id =
                    cursor.getInt(0);

            String name =
                    cursor.getString(1);

            String address =
                    cursor.getString(2);

            String phone =
                    cursor.getString(3);

            double latitude =
                    cursor.getDouble(4);

            double longitude =
                    cursor.getDouble(5);

            branches.add(
                    new Branch(
                            id,
                            name,
                            address,
                            phone,
                            latitude,
                            longitude
                    )
            );
        }

        cursor.close();

        return branches;
    }


    public int updateBranch(
            int id,
            String name,
            String address,
            String phone,
            double latitude,
            double longitude
    ) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("address", address);
        values.put("phone", phone);
        values.put("latitude", latitude);
        values.put("longitude", longitude);

        return db.update(
                "branches",
                values,
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }
    public int deleteBranch(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(
                "branches",
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }


    // ============================================================
    // REPAIR SAMPLES - INSERT
    // ============================================================

    public long insertRepairSample(
            String deviceName,
            String category,
            String service,
            String description,
            String imageUri
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_SAMPLE_DEVICE_NAME,
                deviceName
        );

        values.put(
                COL_SAMPLE_CATEGORY,
                category
        );

        values.put(
                COL_SAMPLE_SERVICE,
                service
        );

        values.put(
                COL_SAMPLE_DESCRIPTION,
                description
        );

        if (imageUri != null) {

            values.put(
                    COL_SAMPLE_IMAGE_URI,
                    imageUri
            );

        } else {

            values.putNull(
                    COL_SAMPLE_IMAGE_URI
            );
        }

        return db.insert(
                TABLE_REPAIR_SAMPLES,
                null,
                values
        );
    }


    // ============================================================
    // REPAIR SAMPLES - GET
    // ============================================================

    public List<RepairSample> getAllRepairSamples() {

        List<RepairSample> samples =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id, device_name, " +
                                "category, service, " +
                                "description, image_uri " +
                                "FROM " +
                                TABLE_REPAIR_SAMPLES +
                                " ORDER BY id ASC",
                        null
                );

        while (cursor.moveToNext()) {

            int id =
                    cursor.getInt(0);

            String deviceName =
                    cursor.getString(1);

            String category =
                    cursor.getString(2);

            String service =
                    cursor.getString(3);

            String description =
                    cursor.getString(4);

            String imageUri =
                    cursor.getString(5);

            samples.add(
                    new RepairSample(
                            id,
                            deviceName,
                            category,
                            service,
                            description,
                            imageUri
                    )
            );
        }

        cursor.close();

        return samples;
    }


    // ============================================================
    // REPAIR SAMPLES - UPDATE
    // ============================================================

    public int updateRepairSample(
            int id,
            String deviceName,
            String category,
            String service,
            String description,
            String imageUri
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_SAMPLE_DEVICE_NAME,
                deviceName
        );

        values.put(
                COL_SAMPLE_CATEGORY,
                category
        );

        values.put(
                COL_SAMPLE_SERVICE,
                service
        );

        values.put(
                COL_SAMPLE_DESCRIPTION,
                description
        );

        if (imageUri != null) {

            values.put(
                    COL_SAMPLE_IMAGE_URI,
                    imageUri
            );

        } else {

            values.putNull(
                    COL_SAMPLE_IMAGE_URI
            );
        }

        return db.update(
                TABLE_REPAIR_SAMPLES,
                values,
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }


    // ============================================================
    // REPAIR SAMPLES - DELETE
    // ============================================================

    public int deleteRepairSample(int id) {

        SQLiteDatabase db =
                getWritableDatabase();

        return db.delete(
                TABLE_REPAIR_SAMPLES,
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }


    // ============================================================
    // SPARE PARTS - INSERT
    // ============================================================

    public long insertSparePart(
            String name,
            String category,
            String partNumber,
            int quantity,
            int minimumStock,
            double unitPrice,
            String supplier,
            String description,
            String imageUri
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_SPARE_PART_NAME,
                name
        );

        values.put(
                COL_SPARE_PART_CATEGORY,
                category
        );

        values.put(
                COL_SPARE_PART_NUMBER,
                partNumber
        );

        values.put(
                COL_SPARE_PART_QUANTITY,
                quantity
        );

        values.put(
                COL_SPARE_PART_MINIMUM_STOCK,
                minimumStock
        );

        values.put(
                COL_SPARE_PART_UNIT_PRICE,
                unitPrice
        );

        values.put(
                COL_SPARE_PART_SUPPLIER,
                supplier
        );

        values.put(
                COL_SPARE_PART_DESCRIPTION,
                description
        );

        if (imageUri != null) {

            values.put(
                    COL_SPARE_PART_IMAGE_URI,
                    imageUri
            );

        } else {

            values.putNull(
                    COL_SPARE_PART_IMAGE_URI
            );
        }

        return db.insert(
                TABLE_SPARE_PARTS,
                null,
                values
        );
    }


    // ============================================================
    // SPARE PARTS - GET
    // ============================================================

    public List<SparePart> getAllSpareParts() {

        List<SparePart> spareParts =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id, name, category, " +
                                "part_number, quantity, " +
                                "minimum_stock, unit_price, " +
                                "supplier, description, " +
                                "image_uri " +
                                "FROM " +
                                TABLE_SPARE_PARTS +
                                " ORDER BY id ASC",
                        null
                );

        while (cursor.moveToNext()) {

            int id =
                    cursor.getInt(0);

            String name =
                    cursor.getString(1);

            String category =
                    cursor.getString(2);

            String partNumber =
                    cursor.getString(3);

            int quantity =
                    cursor.getInt(4);

            int minimumStock =
                    cursor.getInt(5);

            double unitPrice =
                    cursor.getDouble(6);

            String supplier =
                    cursor.getString(7);

            String description =
                    cursor.getString(8);

            String imageUri =
                    cursor.getString(9);

            spareParts.add(
                    new SparePart(
                            id,
                            name,
                            category,
                            partNumber,
                            quantity,
                            minimumStock,
                            unitPrice,
                            supplier,
                            description,
                            imageUri
                    )
            );
        }

        cursor.close();

        return spareParts;
    }


    // ============================================================
    // SPARE PARTS - UPDATE
    // ============================================================

    public int updateSparePart(
            int id,
            String name,
            String category,
            String partNumber,
            int quantity,
            int minimumStock,
            double unitPrice,
            String supplier,
            String description,
            String imageUri
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_SPARE_PART_NAME,
                name
        );

        values.put(
                COL_SPARE_PART_CATEGORY,
                category
        );

        values.put(
                COL_SPARE_PART_NUMBER,
                partNumber
        );

        values.put(
                COL_SPARE_PART_QUANTITY,
                quantity
        );

        values.put(
                COL_SPARE_PART_MINIMUM_STOCK,
                minimumStock
        );

        values.put(
                COL_SPARE_PART_UNIT_PRICE,
                unitPrice
        );

        values.put(
                COL_SPARE_PART_SUPPLIER,
                supplier
        );

        values.put(
                COL_SPARE_PART_DESCRIPTION,
                description
        );

        if (imageUri != null) {

            values.put(
                    COL_SPARE_PART_IMAGE_URI,
                    imageUri
            );

        } else {

            values.putNull(
                    COL_SPARE_PART_IMAGE_URI
            );
        }

        return db.update(
                TABLE_SPARE_PARTS,
                values,
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }


    // ============================================================
    // SPARE PART STOCK
    // ============================================================

    public int updateSparePartStock(
            int id,
            int quantity
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_SPARE_PART_QUANTITY,
                quantity
        );

        return db.update(
                TABLE_SPARE_PARTS,
                values,
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }


    /*
     * Alias used by some admin code.
     */
    public int updateStockQuantity(
            int sparePartId,
            int newQuantity
    ) {

        return updateSparePartStock(
                sparePartId,
                newQuantity
        );
    }


    // ============================================================
    // SPARE PARTS - DELETE
    // ============================================================

    public int deleteSparePart(int id) {

        SQLiteDatabase db =
                getWritableDatabase();

        return db.delete(
                TABLE_SPARE_PARTS,
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }


    // ============================================================
    // LOW STOCK
    // ============================================================

    public List<SparePart> getLowStockSpareParts() {

        List<SparePart> list =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id, name, category, " +
                                "part_number, quantity, " +
                                "minimum_stock, unit_price, " +
                                "supplier, description, " +
                                "image_uri " +
                                "FROM " +
                                TABLE_SPARE_PARTS +
                                " WHERE quantity <= " +
                                "minimum_stock " +
                                "ORDER BY quantity ASC",
                        null
                );

        while (cursor.moveToNext()) {

            int id =
                    cursor.getInt(0);

            String name =
                    cursor.getString(1);

            String category =
                    cursor.getString(2);

            String partNumber =
                    cursor.getString(3);

            int quantity =
                    cursor.getInt(4);

            int minimumStock =
                    cursor.getInt(5);

            double unitPrice =
                    cursor.getDouble(6);

            String supplier =
                    cursor.getString(7);

            String description =
                    cursor.getString(8);

            String imageUri =
                    cursor.getString(9);

            list.add(
                    new SparePart(
                            id,
                            name,
                            category,
                            partNumber,
                            quantity,
                            minimumStock,
                            unitPrice,
                            supplier,
                            description,
                            imageUri
                    )
            );
        }

        cursor.close();

        return list;
    }


    /*
     * Alias for older admin code.
     */
    public List<SparePart> getLowStockItems() {

        return getLowStockSpareParts();
    }


    // ============================================================
    // CUSTOMER - CREATE REPAIR
    // ============================================================

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

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_REPAIR_CUSTOMER_ID,
                customerId
        );

        values.put(
                COL_REPAIR_CATEGORY_ID,
                categoryId
        );

        values.put(
                COL_REPAIR_DEVICE_MODEL,
                deviceModel
        );

        values.put(
                COL_REPAIR_SERVICE_ID,
                serviceId
        );

        values.put(
                COL_REPAIR_PROBLEM_DESCRIPTION,
                problemDescription
        );

        values.put(
                COL_REPAIR_BRANCH_ID,
                branchId
        );

        if (imageUri != null &&
                !imageUri.trim().isEmpty()) {

            values.put(
                    COL_REPAIR_IMAGE_URI,
                    imageUri
            );

        } else {

            values.putNull(
                    COL_REPAIR_IMAGE_URI
            );
        }

        values.putNull(
                COL_REPAIR_IN_PROGRESS_PHOTO_URI
        );

        values.putNull(
                COL_REPAIR_ASSIGNED_TECHNICIAN_ID
        );

        values.put(
                COL_REPAIR_STATUS,
                status
        );

        values.put(
                COL_REPAIR_DATE,
                repairDate
        );

        return db.insert(
                TABLE_REPAIRS,
                null,
                values
        );
    }


    /*
     * Compatibility overload for customer code that does not
     * explicitly provide a status.
     */
    public long createRepair(
            int customerId,
            int categoryId,
            String deviceModel,
            int serviceId,
            String problemDescription,
            int branchId,
            String imageUri,
            String repairDate
    ) {

        return createRepair(
                customerId,
                categoryId,
                deviceModel,
                serviceId,
                problemDescription,
                branchId,
                imageUri,
                "Pending",
                repairDate
        );
    }


    // ============================================================
    // CUSTOMER - ACTIVE REPAIRS
    // ============================================================

    public Cursor getCustomerActiveRepairs(
            int customerId
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

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

                        "FROM " +
                        TABLE_REPAIRS +
                        " r " +

                        "LEFT JOIN " +
                        TABLE_CATEGORIES +
                        " c ON r.category_id = c.id " +

                        "LEFT JOIN " +
                        TABLE_SERVICES +
                        " s ON r.service_id = s.id " +

                        "LEFT JOIN " +
                        TABLE_BRANCHES +
                        " b ON r.branch_id = b.id " +

                        "WHERE r.customer_id = ? " +

                        "AND r.status NOT IN " +
                        "('Completed', 'Cancelled') " +

                        "ORDER BY r.id DESC",

                new String[]{
                        String.valueOf(customerId)
                }
        );
    }


    // ============================================================
    // CUSTOMER - CANCEL REPAIR
    // ============================================================

    public boolean cancelRepair(
            int repairId,
            int customerId
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_REPAIR_STATUS,
                "Cancelled"
        );

        int rows =
                db.update(
                        TABLE_REPAIRS,
                        values,
                        "id = ? " +
                                "AND customer_id = ? " +
                                "AND status = ?",
                        new String[]{
                                String.valueOf(repairId),
                                String.valueOf(customerId),
                                "Pending"
                        }
                );

        return rows > 0;
    }


    /*
     * Compatibility method for older admin/customer code.
     */
    public int cancelRepair(int repairId) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_REPAIR_STATUS,
                "Cancelled"
        );

        return db.update(
                TABLE_REPAIRS,
                values,
                "id = ?",
                new String[]{
                        String.valueOf(repairId)
                }
        );
    }


    // ============================================================
    // CUSTOMER - REPAIR HISTORY
    // ============================================================

    public Cursor getRepairHistory(
            int customerId
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT " +

                        "r.id AS repair_id, " +

                        "r.category_id, " +

                        "c.name AS category_name, " +

                        "c.price_modifier AS price_modifier, " +

                        "r.device_model, " +

                        "s.name AS service_name, " +

                        "b.name AS branch_name, " +

                        "r.repair_date, " +

                        "r.status, " +

                        "r.image_uri, " +

                        "r.in_progress_photo_uri, " +

                        "s.price AS service_price, " +

                        "(s.price * " +
                        "COALESCE(c.price_modifier, 1.0)) " +
                        "AS final_price " +

                        "FROM " +
                        TABLE_REPAIRS +
                        " r " +

                        "LEFT JOIN " +
                        TABLE_CATEGORIES +
                        " c ON r.category_id = c.id " +

                        "LEFT JOIN " +
                        TABLE_SERVICES +
                        " s ON r.service_id = s.id " +

                        "LEFT JOIN " +
                        TABLE_BRANCHES +
                        " b ON r.branch_id = b.id " +

                        "WHERE r.customer_id = ? " +

                        "ORDER BY r.id DESC",

                new String[]{
                        String.valueOf(customerId)
                }
        );
    }


    // ============================================================
    // CUSTOMER - UNPAID REPAIRS
    // ============================================================

    public Cursor getUnpaidRepairs(
            int customerId
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT " +

                        "r.id AS repair_id, " +

                        "r.category_id, " +

                        "c.name AS category_name, " +

                        "r.device_model, " +

                        "s.name AS service_name, " +

                        "s.price AS service_price, " +

                        "COALESCE(c.price_modifier, 1.0) " +
                        "AS price_modifier, " +

                        "(s.price * " +
                        "COALESCE(c.price_modifier, 1.0)) " +
                        "AS amount, " +

                        "b.name AS branch_name, " +

                        "r.status " +

                        "FROM " +
                        TABLE_REPAIRS +
                        " r " +

                        "LEFT JOIN " +
                        TABLE_CATEGORIES +
                        " c ON r.category_id = c.id " +

                        "LEFT JOIN " +
                        TABLE_SERVICES +
                        " s ON r.service_id = s.id " +

                        "LEFT JOIN " +
                        TABLE_BRANCHES +
                        " b ON r.branch_id = b.id " +

                        "WHERE r.customer_id = ? " +

                        "AND r.status IN " +
                        "('Ready for Collection', 'Completed') " +

                        "AND NOT EXISTS (" +

                        "SELECT 1 FROM " +
                        TABLE_PAYMENTS +
                        " p " +

                        "WHERE p.repair_id = r.id " +

                        "AND p.status = 'Paid'" +

                        ") " +

                        "ORDER BY r.id DESC",

                new String[]{
                        String.valueOf(customerId)
                }
        );
    }


    // ============================================================
    // CUSTOMER - PAYMENT HISTORY
    // ============================================================

    public Cursor getPaymentHistory(
            int customerId
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT " +

                        "p.id AS payment_id, " +

                        "p.repair_id AS repair_id, " +

                        "p.amount, " +

                        "p.payment_date, " +

                        "p.status, " +

                        "r.device_model, " +

                        "s.name AS service_name, " +

                        "c.name AS category_name " +

                        "FROM " +
                        TABLE_PAYMENTS +
                        " p " +

                        "INNER JOIN " +
                        TABLE_REPAIRS +
                        " r ON p.repair_id = r.id " +

                        "LEFT JOIN " +
                        TABLE_SERVICES +
                        " s ON r.service_id = s.id " +

                        "LEFT JOIN " +
                        TABLE_CATEGORIES +
                        " c ON r.category_id = c.id " +

                        "WHERE r.customer_id = ? " +

                        "ORDER BY p.id DESC",

                new String[]{
                        String.valueOf(customerId)
                }
        );
    }


    // ============================================================
    // CREATE PAYMENT
    // ============================================================

    public long createPayment(
            int repairId,
            double amount,
            String paymentDate,
            String status
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_PAYMENT_REPAIR_ID,
                repairId
        );

        values.put(
                COL_PAYMENT_AMOUNT,
                amount
        );

        values.put(
                COL_PAYMENT_DATE,
                paymentDate
        );

        values.put(
                COL_PAYMENT_STATUS,
                status
        );

        long paymentId =
                db.insert(
                        TABLE_PAYMENTS,
                        null,
                        values
                );

        /*
         * Do not change repair status to "Paid"
         * automatically unless your customer UI expects it.
         *
         * A payment record is enough to identify
         * the repair as paid.
         */

        return paymentId;
    }


    // ============================================================
    // ADMIN - GET ALL REPAIRS
    // ============================================================

    public Cursor getAllRepairs() {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT " +

                        "r.id, " +

                        "r.customer_id, " +

                        "r.category_id, " +

                        "r.device_model, " +

                        "r.service_id, " +

                        "r.problem_description, " +

                        "r.branch_id, " +

                        "r.image_uri, " +

                        "r.in_progress_photo_uri, " +

                        "r.assigned_technician_id, " +

                        "r.status, " +

                        "r.repair_date, " +

                        "cu.full_name AS customer_name, " +

                        "cu.phone AS customer_phone, " +

                        "c.name AS category_name, " +

                        "s.name AS service_name, " +

                        "s.price AS service_price, " +

                        "b.name AS branch_name " +

                        "FROM " +
                        TABLE_REPAIRS +
                        " r " +

                        "LEFT JOIN " +
                        TABLE_CUSTOMERS +
                        " cu ON r.customer_id = cu.id " +

                        "LEFT JOIN " +
                        TABLE_CATEGORIES +
                        " c ON r.category_id = c.id " +

                        "LEFT JOIN " +
                        TABLE_SERVICES +
                        " s ON r.service_id = s.id " +

                        "LEFT JOIN " +
                        TABLE_BRANCHES +
                        " b ON r.branch_id = b.id " +

                        "ORDER BY r.id DESC",

                null
        );
    }


    // ============================================================
    // ADMIN - UPDATE REPAIR STATUS
    // ============================================================

    public int updateRepairStatus(
            int repairId,
            String status
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_REPAIR_STATUS,
                status
        );

        return db.update(
                TABLE_REPAIRS,
                values,
                "id = ?",
                new String[]{
                        String.valueOf(repairId)
                }
        );
    }


    // ============================================================
    // ADMIN - ASSIGN TECHNICIAN
    // ============================================================

    public int assignTechnician(
            int repairId,
            int technicianId
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_REPAIR_ASSIGNED_TECHNICIAN_ID,
                technicianId
        );

        return db.update(
                TABLE_REPAIRS,
                values,
                "id = ?",
                new String[]{
                        String.valueOf(repairId)
                }
        );
    }


    // ============================================================
    // ADMIN / TECHNICIAN - PROGRESS PHOTO
    // ============================================================

    public int updateRepairInProgressPhoto(
            int repairId,
            String imageUri
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        if (imageUri != null) {

            values.put(
                    COL_REPAIR_IN_PROGRESS_PHOTO_URI,
                    imageUri
            );

        } else {

            values.putNull(
                    COL_REPAIR_IN_PROGRESS_PHOTO_URI
            );
        }

        return db.update(
                TABLE_REPAIRS,
                values,
                "id = ?",
                new String[]{
                        String.valueOf(repairId)
                }
        );
    }


    // ============================================================
    // ADMIN DASHBOARD - TOTAL REPAIRS
    // ============================================================

    public int getTotalRepairs() {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM " +
                                TABLE_REPAIRS,
                        null
                );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();

        return count;
    }


    // ============================================================
    // ADMIN DASHBOARD - PENDING REPAIRS
    // ============================================================

    public int getPendingRepairs() {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM " +
                                TABLE_REPAIRS +
                                " WHERE status = 'Pending'",
                        null
                );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();

        return count;
    }


    // ============================================================
    // ADMIN - USER/CUSTOMER INFORMATION
    // ============================================================

    public Cursor getAllCustomers() {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT id, full_name, email, phone " +
                        "FROM " + TABLE_CUSTOMERS +
                        " ORDER BY id ASC",
                null
        );
    }


    public Cursor getCustomerById(
            int customerId
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT id, full_name, email, phone " +
                        "FROM " + TABLE_CUSTOMERS +
                        " WHERE id = ? LIMIT 1",
                new String[]{
                        String.valueOf(customerId)
                }
        );
    }


    // ============================================================
    // CLOSE
    // ============================================================

    @Override
    public synchronized void close() {
        super.close();
    }
}