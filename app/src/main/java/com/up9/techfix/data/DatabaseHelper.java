package com.up9.techfix.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.up9.techfix.data.Branch;
import com.up9.techfix.data.Category;
import com.up9.techfix.admin.repairsamples.RepairSample;
import com.up9.techfix.data.Service;
import com.up9.techfix.admin.spareparts.SparePart;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TechFix.db";
    private static final int DATABASE_VERSION = 12;

    // =====================================================
    // TABLE NAMES
    // =====================================================

    public static final String TABLE_CUSTOMERS = "customers";
    public static final String TABLE_BRANCHES = "branches";
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_SERVICES = "services";
    public static final String TABLE_REPAIRS = "repairs";
    public static final String TABLE_PAYMENTS = "payments";
    public static final String TABLE_REPAIR_SAMPLES = "repair_samples";
    public static final String TABLE_SPARE_PARTS = "spare_parts";
    public static final String TABLE_USERS = "users";

    // =====================================================
    // USERS / AUTHENTICATION
    // =====================================================

    public static final String COL_USER_ID = "id";
    public static final String COL_USER_FULL_NAME = "full_name";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_PHONE = "phone";
    public static final String COL_USER_PASSWORD = "password";
    public static final String COL_USER_ROLE = "role";

    // =====================================================
    // CUSTOMER COLUMNS
    // =====================================================

    public static final String COL_CUSTOMER_ID = "id";
    public static final String COL_CUSTOMER_FULL_NAME = "full_name";
    public static final String COL_CUSTOMER_EMAIL = "email";
    public static final String COL_CUSTOMER_PHONE = "phone";
    public static final String COL_CUSTOMER_PASSWORD = "password";

    // =====================================================
    // BRANCH COLUMNS
    // =====================================================

    public static final String COL_BRANCH_ID = "id";
    public static final String COL_BRANCH_NAME = "name";
    public static final String COL_BRANCH_ADDRESS = "address";
    public static final String COL_BRANCH_PHONE = "phone";
    public static final String COL_BRANCH_LATITUDE = "latitude";
    public static final String COL_BRANCH_LONGITUDE = "longitude";

    // =====================================================
    // CATEGORY COLUMNS
    // =====================================================

    public static final String COL_CATEGORY_ID = "id";
    public static final String COL_CATEGORY_NAME = "name";
    public static final String COL_CATEGORY_DESCRIPTION = "description";
    public static final String COL_CATEGORY_PRICE_MODIFIER = "price_modifier";

    // =====================================================
    // SERVICE COLUMNS
    // =====================================================

    public static final String COL_SERVICE_ID = "id";
    public static final String COL_SERVICE_NAME = "name";
    public static final String COL_SERVICE_IMAGE_URI = "image_uri";
    public static final String COL_SERVICE_DESCRIPTION = "description";
    public static final String COL_SERVICE_PRICE = "price";
    public static final String COL_SERVICE_ESTIMATED_DAYS = "estimated_days";

    // =====================================================
    // REPAIR SAMPLE COLUMNS
    // =====================================================

    public static final String COL_SAMPLE_ID = "id";
    public static final String COL_SAMPLE_DEVICE_NAME = "device_name";
    public static final String COL_SAMPLE_CATEGORY = "category";
    public static final String COL_SAMPLE_SERVICE = "service";
    public static final String COL_SAMPLE_DESCRIPTION = "description";
    public static final String COL_SAMPLE_IMAGE_URI = "image_uri";

    // =====================================================
    // SPARE PART COLUMNS
    // =====================================================

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

    // =====================================================
    // REPAIR COLUMNS
    // =====================================================

    public static final String COL_REPAIR_ID = "id";
    public static final String COL_REPAIR_CUSTOMER_ID = "customer_id";
    public static final String COL_REPAIR_CATEGORY_ID = "category_id";
    public static final String COL_REPAIR_DEVICE_MODEL = "device_model";
    public static final String COL_REPAIR_SERVICE_ID = "service_id";
    public static final String COL_REPAIR_PROBLEM_DESCRIPTION =
            "problem_description";
    public static final String COL_REPAIR_BRANCH_ID = "branch_id";
    public static final String COL_REPAIR_IMAGE_URI = "image_uri";
    public static final String COL_REPAIR_IN_PROGRESS_PHOTO_URI =
            "in_progress_photo_uri";
    public static final String COL_REPAIR_ASSIGNED_TECHNICIAN_ID =
            "assigned_technician_id";
    public static final String COL_REPAIR_TECHNICIAN_NAME =
            "technician_name";
    public static final String COL_REPAIR_STATUS = "status";
    public static final String COL_REPAIR_DATE = "repair_date";
    public static final String COL_FINAL_PRICE = "final_price";

    // =====================================================
    // PAYMENT COLUMNS
    // =====================================================

    public static final String COL_PAYMENT_ID = "id";
    public static final String COL_PAYMENT_REPAIR_ID = "repair_id";
    public static final String COL_PAYMENT_AMOUNT = "amount";
    public static final String COL_PAYMENT_DATE = "payment_date";
    public static final String COL_PAYMENT_STATUS = "status";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public DatabaseHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    // =====================================================
    // ON CREATE
    // =====================================================

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

    // =====================================================
    // ON UPGRADE
    // =====================================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        createUsersTable(db);
        createCustomersTable(db);
        createBranchesTable(db);
        createCategoriesTable(db);
        createServicesTable(db);
        createRepairSamplesTable(db);
        createSparePartsTable(db);
        createRepairsTable(db);
        createPaymentsTable(db);

        if (!columnExists(
                db,
                TABLE_SERVICES,
                COL_SERVICE_IMAGE_URI
        )) {

            db.execSQL(
                    "ALTER TABLE " +
                            TABLE_SERVICES +
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
                    "ALTER TABLE " +
                            TABLE_SERVICES +
                            " ADD COLUMN " +
                            COL_SERVICE_ESTIMATED_DAYS +
                            " INTEGER NOT NULL DEFAULT 1"
            );
        }

        if (!columnExists(
                db,
                TABLE_CATEGORIES,
                COL_CATEGORY_PRICE_MODIFIER
        )) {

            db.execSQL(
                    "ALTER TABLE " +
                            TABLE_CATEGORIES +
                            " ADD COLUMN " +
                            COL_CATEGORY_PRICE_MODIFIER +
                            " REAL NOT NULL DEFAULT 1.0"
            );
        }

        db.execSQL(
                "UPDATE " +
                        TABLE_CATEGORIES +
                        " SET " +
                        COL_CATEGORY_PRICE_MODIFIER +
                        " = 1.0 " +
                        "WHERE " +
                        COL_CATEGORY_PRICE_MODIFIER +
                        " = 0"
        );

        if (!columnExists(
                db,
                TABLE_REPAIR_SAMPLES,
                COL_SAMPLE_IMAGE_URI
        )) {

            db.execSQL(
                    "ALTER TABLE " +
                            TABLE_REPAIR_SAMPLES +
                            " ADD COLUMN " +
                            COL_SAMPLE_IMAGE_URI +
                            " TEXT"
            );
        }

        if (!columnExists(
                db,
                TABLE_SPARE_PARTS,
                COL_SPARE_PART_IMAGE_URI
        )) {

            db.execSQL(
                    "ALTER TABLE " +
                            TABLE_SPARE_PARTS +
                            " ADD COLUMN " +
                            COL_SPARE_PART_IMAGE_URI +
                            " TEXT"
            );
        }

        if (!columnExists(
                db,
                TABLE_REPAIRS,
                COL_REPAIR_TECHNICIAN_NAME
        )) {

            db.execSQL(
                    "ALTER TABLE " +
                            TABLE_REPAIRS +
                            " ADD COLUMN " +
                            COL_REPAIR_TECHNICIAN_NAME +
                            " TEXT"
            );
        }

        if (!columnExists(
                db,
                TABLE_REPAIRS,
                COL_FINAL_PRICE
        )) {

            db.execSQL(
                    "ALTER TABLE " +
                            TABLE_REPAIRS +
                            " ADD COLUMN " +
                            COL_FINAL_PRICE +
                            " REAL DEFAULT 0"
            );
        }

        insertDefaultAdmin(db);
        insertDefaultTechnician(db);
    }

    // =====================================================
    // CREATE USERS TABLE
    // =====================================================

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

    // =====================================================
    // CREATE CUSTOMERS TABLE
    // =====================================================

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

    // =====================================================
    // CREATE BRANCHES TABLE
    // =====================================================

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

    // =====================================================
    // CREATE CATEGORIES TABLE
    // =====================================================

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

    // =====================================================
    // CREATE SERVICES TABLE
    // =====================================================

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

    // =====================================================
    // CREATE REPAIR SAMPLES TABLE
    // =====================================================

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

    // =====================================================
    // CREATE SPARE PARTS TABLE
    // =====================================================

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

    // =====================================================
    // CREATE REPAIRS TABLE
    // =====================================================

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
                        "technician_name TEXT, " +
                        "status TEXT DEFAULT 'Pending', " +
                        "repair_date TEXT, " +
                        "final_price REAL DEFAULT 0" +
                        ")"
        );
    }

    // =====================================================
    // CREATE PAYMENTS TABLE
    // =====================================================

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

    // =====================================================
    // COLUMN EXISTS
    // =====================================================

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
                            cursor.getColumnIndexOrThrow("name")
                    );

            if (columnName.equals(name)) {
                exists = true;
                break;
            }
        }

        cursor.close();

        return exists;
    }

    // =====================================================
    // LOGIN USER MODEL
    // =====================================================

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

    // =====================================================
    // LOGIN - ALL ROLES
    // =====================================================

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

    // =====================================================
    // CUSTOMER REGISTRATION
    // =====================================================

    public long registerCustomer(
            String fullName,
            String email,
            String phone,
            String password
    ) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {

            Cursor checkCursor = db.rawQuery(
                    "SELECT id FROM " +
                            TABLE_USERS +
                            " WHERE email = ? LIMIT 1",
                    new String[]{
                            email
                    }
            );

            boolean emailExists =
                    checkCursor.moveToFirst();

            checkCursor.close();

            if (emailExists) {
                return -1;
            }

            ContentValues userValues =
                    new ContentValues();

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

            long userId =
                    db.insert(
                            TABLE_USERS,
                            null,
                            userValues
                    );

            if (userId == -1) {
                return -1;
            }

            ContentValues customerValues =
                    new ContentValues();

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

            long customerId =
                    db.insert(
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

    // =====================================================
    // CUSTOMER LOGIN
    // =====================================================

    public boolean checkCustomerLogin(
            String email,
            String password
    ) {

        LoginUser user =
                loginUser(
                        email,
                        password
                );

        return user != null &&
                "CUSTOMER".equalsIgnoreCase(
                        user.getRole()
                );
    }

    // =====================================================
    // DEFAULT ADMIN
    // =====================================================

    private void insertDefaultAdmin(SQLiteDatabase db) {

        Cursor cursor = db.rawQuery(
                "SELECT id FROM " +
                        TABLE_USERS +
                        " WHERE email = ? LIMIT 1",
                new String[]{
                        "admin@techfix.com"
                }
        );

        boolean exists =
                cursor.moveToFirst();

        cursor.close();

        if (exists) {
            return;
        }

        ContentValues values =
                new ContentValues();

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

    // =====================================================
    // DEFAULT TECHNICIAN
    // =====================================================

    private void insertDefaultTechnician(SQLiteDatabase db) {

        Cursor cursor = db.rawQuery(
                "SELECT id FROM " +
                        TABLE_USERS +
                        " WHERE email = ? LIMIT 1",
                new String[]{
                        "technician@techfix.com"
                }
        );

        boolean exists =
                cursor.moveToFirst();

        cursor.close();

        if (exists) {
            return;
        }

        ContentValues values =
                new ContentValues();

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

    // =====================================================
    // GET CUSTOMER ID
    // =====================================================

    public int getCustomerId(
            String email,
            String password
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT c.id " +
                                "FROM " +
                                TABLE_CUSTOMERS +
                                " c " +
                                "INNER JOIN " +
                                TABLE_USERS +
                                " u " +
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
            customerId = cursor.getInt(0);
        }

        cursor.close();

        return customerId;
    }

    // =====================================================
    // SERVICES
    // =====================================================

    public List<Service> getAllServices() {

        List<Service> serviceList =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id, name, image_uri, " +
                                "description, price, " +
                                "estimated_days " +
                                "FROM " +
                                TABLE_SERVICES +
                                " ORDER BY id DESC",
                        null
                );

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String imageUri = cursor.getString(2);
            String description = cursor.getString(3);
            double price = cursor.getDouble(4);
            int estimatedDays = cursor.getInt(5);

            serviceList.add(
                    new Service(
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

        return serviceList;
    }

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

    // =====================================================
    // CATEGORIES
    // =====================================================

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

    public List<Category> getAllCategories() {

        List<Category> categories =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id, name, description, " +
                                "price_modifier " +
                                "FROM " +
                                TABLE_CATEGORIES +
                                " ORDER BY id ASC",
                        null
                );

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            double priceModifier = cursor.getDouble(3);

            categories.add(
                    new Category(
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

    // =====================================================
    // BRANCHES
    // =====================================================

    public List<Branch> getAllBranches() {

        List<Branch> branchList =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id, name, address, phone, " +
                                "latitude, longitude " +
                                "FROM " +
                                TABLE_BRANCHES +
                                " ORDER BY id ASC",
                        null
                );

        try {

            while (cursor.moveToNext()) {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("name")
                        );

                String address =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("address")
                        );

                String phone =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("phone")
                        );

                double latitude =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow("latitude")
                        );

                double longitude =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow("longitude")
                        );

                branchList.add(
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

        } finally {

            cursor.close();
        }

        return branchList;
    }

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

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_BRANCH_NAME,
                name
        );

        values.put(
                COL_BRANCH_ADDRESS,
                address
        );

        values.put(
                COL_BRANCH_PHONE,
                phone
        );

        values.put(
                COL_BRANCH_LATITUDE,
                latitude
        );

        values.put(
                COL_BRANCH_LONGITUDE,
                longitude
        );

        return db.insert(
                TABLE_BRANCHES,
                null,
                values
        );
    }

    public List<Branch> getAllBranchModels() {

        List<Branch> branches =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id, name, address, phone, " +
                                "latitude, longitude " +
                                "FROM " +
                                TABLE_BRANCHES +
                                " ORDER BY id ASC",
                        null
                );

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String address = cursor.getString(2);
            String phone = cursor.getString(3);
            double latitude = cursor.getDouble(4);
            double longitude = cursor.getDouble(5);

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

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_BRANCH_NAME,
                name
        );

        values.put(
                COL_BRANCH_ADDRESS,
                address
        );

        values.put(
                COL_BRANCH_PHONE,
                phone
        );

        values.put(
                COL_BRANCH_LATITUDE,
                latitude
        );

        values.put(
                COL_BRANCH_LONGITUDE,
                longitude
        );

        return db.update(
                TABLE_BRANCHES,
                values,
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }

    public int deleteBranch(int id) {

        SQLiteDatabase db =
                getWritableDatabase();

        return db.delete(
                TABLE_BRANCHES,
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }

    // =====================================================
    // REPAIR SAMPLES
    // =====================================================

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

            int id = cursor.getInt(0);
            String deviceName = cursor.getString(1);
            String category = cursor.getString(2);
            String service = cursor.getString(3);
            String description = cursor.getString(4);
            String imageUri = cursor.getString(5);

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

    // =====================================================
    // SPARE PARTS
    // =====================================================

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

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String category = cursor.getString(2);
            String partNumber = cursor.getString(3);
            int quantity = cursor.getInt(4);
            int minimumStock = cursor.getInt(5);
            double unitPrice = cursor.getDouble(6);
            String supplier = cursor.getString(7);
            String description = cursor.getString(8);
            String imageUri = cursor.getString(9);

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

    public int updateStockQuantity(
            int sparePartId,
            int newQuantity
    ) {

        return updateSparePartStock(
                sparePartId,
                newQuantity
        );
    }

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

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String category = cursor.getString(2);
            String partNumber = cursor.getString(3);
            int quantity = cursor.getInt(4);
            int minimumStock = cursor.getInt(5);
            double unitPrice = cursor.getDouble(6);
            String supplier = cursor.getString(7);
            String description = cursor.getString(8);
            String imageUri = cursor.getString(9);

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

    public List<SparePart> getLowStockItems() {

        return getLowStockSpareParts();
    }

    // =====================================================
    // CREATE REPAIR
    // =====================================================
    public Cursor getAllTechnicians() {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.query(
                "technicians",
                new String[]{
                        "id",
                        "name"
                },
                null,
                null,
                null,
                null,
                "name ASC"
        );
    }
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

        values.putNull(
                COL_REPAIR_TECHNICIAN_NAME
        );

        values.put(
                COL_REPAIR_STATUS,
                "Pending"
        );

        values.put(
                COL_REPAIR_DATE,
                repairDate
        );

        values.put(
                COL_FINAL_PRICE,
                0.0
        );

        return db.insert(
                TABLE_REPAIRS,
                null,
                values
        );
    }

    // =====================================================
    // CUSTOMER REPAIRS
    // =====================================================

    public Cursor getCustomerRepairs(
            int customerId
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "r.id AS repair_id, " +
                        "r.customer_id, " +
                        "r.category_id, " +
                        "c.name AS category_name, " +
                        "r.device_model, " +
                        "r.service_id, " +
                        "s.name AS service_name, " +
                        "s.price AS service_price, " +
                        "r.problem_description, " +
                        "r.branch_id, " +
                        "b.name AS branch_name, " +
                        "b.address AS branch_address, " +
                        "r.image_uri, " +
                        "r.in_progress_photo_uri, " +
                        "r.assigned_technician_id, " +
                        "r.technician_name, " +
                        "r.status, " +
                        "r.repair_date, " +
                        "r.final_price " +
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

    // =====================================================
    // CUSTOMER REPAIR BY ID
    // =====================================================

    public Cursor getRepairById(
            int repairId,
            int customerId
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "r.id AS repair_id, " +
                        "r.device_model, " +
                        "r.problem_description, " +
                        "r.image_uri, " +
                        "r.in_progress_photo_uri, " +
                        "r.status, " +
                        "r.repair_date, " +
                        "r.technician_name, " +
                        "r.final_price, " +
                        "c.name AS category_name, " +
                        "s.name AS service_name, " +
                        "s.price AS service_price, " +
                        "b.name AS branch_name, " +
                        "b.address AS branch_address " +
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
                        "WHERE r.id = ? " +
                        "AND r.customer_id = ? " +
                        "LIMIT 1",
                new String[]{
                        String.valueOf(repairId),
                        String.valueOf(customerId)
                }
        );
    }
    public boolean updateRepair(
            int repairId,
            int branchId,
            int technicianId,
            String technicianName,
            String status,
            double finalPrice
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "branch_id",
                branchId
        );

        values.put(
                "technician_id",
                technicianId
        );

        values.put(
                "technician_name",
                technicianName
        );

        values.put(
                "status",
                status
        );

        values.put(
                "final_price",
                finalPrice
        );

        int rows =
                db.update(
                        "repairs",
                        values,
                        "id = ?",
                        new String[]{
                                String.valueOf(repairId)
                        }
                );

        return rows > 0;
    }

    // =====================================================
    // CANCEL REPAIR
    // =====================================================

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

    // =====================================================
    // GET ALL REPAIRS
    // =====================================================

    public Cursor getAllRepairs() {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "r.id AS repair_id, " +
                        "r.customer_id, " +
                        "cu.full_name AS customer_name, " +
                        "cu.email AS customer_email, " +
                        "cu.phone AS customer_phone, " +
                        "r.category_id, " +
                        "c.name AS category_name, " +
                        "r.device_model, " +
                        "r.service_id, " +
                        "s.name AS service_name, " +
                        "s.price AS service_price, " +
                        "r.problem_description, " +
                        "r.branch_id, " +
                        "b.name AS branch_name, " +
                        "b.address AS branch_address, " +
                        "r.image_uri, " +
                        "r.in_progress_photo_uri, " +
                        "r.assigned_technician_id, " +
                        "r.technician_name, " +
                        "r.status, " +
                        "r.repair_date, " +
                        "r.final_price " +
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
// ADMIN - GET ALL REPAIRS AS LIST
// ============================================================

    public List<com.up9.techfix.admin.appoiments.RepairAppointment>
    getAllRepairAppointments() {

        List<com.up9.techfix.admin.appoiments.RepairAppointment> list =
                new ArrayList<>();

        Cursor cursor = getAllRepairs();

        if (cursor == null) {
            return list;
        }

        try {

            int repairIdColumn =
                    cursor.getColumnIndex("id");

            int customerIdColumn =
                    cursor.getColumnIndex("customer_id");

            int deviceModelColumn =
                    cursor.getColumnIndex("device_model");

            int problemDescriptionColumn =
                    cursor.getColumnIndex("problem_description");

            int assignedTechnicianIdColumn =
                    cursor.getColumnIndex("assigned_technician_id");

            int statusColumn =
                    cursor.getColumnIndex("status");

            int repairDateColumn =
                    cursor.getColumnIndex("repair_date");

            int customerNameColumn =
                    cursor.getColumnIndex("customer_name");

            int customerPhoneColumn =
                    cursor.getColumnIndex("customer_phone");

            int categoryNameColumn =
                    cursor.getColumnIndex("category_name");

            int serviceNameColumn =
                    cursor.getColumnIndex("service_name");

            int branchNameColumn =
                    cursor.getColumnIndex("branch_name");


            while (cursor.moveToNext()) {

                int repairId =
                        repairIdColumn >= 0
                                ? cursor.getInt(repairIdColumn)
                                : -1;

                int customerId =
                        customerIdColumn >= 0
                                ? cursor.getInt(customerIdColumn)
                                : -1;

                int technicianId =
                        assignedTechnicianIdColumn >= 0
                                ? cursor.getInt(
                                assignedTechnicianIdColumn
                        )
                                : -1;


                String customerName =
                        customerNameColumn >= 0
                                ? cursor.getString(
                                customerNameColumn
                        )
                                : "";

                String customerPhone =
                        customerPhoneColumn >= 0
                                ? cursor.getString(
                                customerPhoneColumn
                        )
                                : "";

                String categoryName =
                        categoryNameColumn >= 0
                                ? cursor.getString(
                                categoryNameColumn
                        )
                                : "";

                String deviceModel =
                        deviceModelColumn >= 0
                                ? cursor.getString(
                                deviceModelColumn
                        )
                                : "";

                String serviceName =
                        serviceNameColumn >= 0
                                ? cursor.getString(
                                serviceNameColumn
                        )
                                : "";

                String problemDescription =
                        problemDescriptionColumn >= 0
                                ? cursor.getString(
                                problemDescriptionColumn
                        )
                                : "";

                String branchName =
                        branchNameColumn >= 0
                                ? cursor.getString(
                                branchNameColumn
                        )
                                : "";

                String status =
                        statusColumn >= 0
                                ? cursor.getString(
                                statusColumn
                        )
                                : "Pending";

                String repairDate =
                        repairDateColumn >= 0
                                ? cursor.getString(
                                repairDateColumn
                        )
                                : "";


                if (customerName == null ||
                        customerName.trim().isEmpty()) {

                    customerName = "Unknown Customer";
                }

                if (customerPhone == null ||
                        customerPhone.trim().isEmpty()) {

                    customerPhone = "Not available";
                }

                if (categoryName == null ||
                        categoryName.trim().isEmpty()) {

                    categoryName = "Not available";
                }

                if (deviceModel == null ||
                        deviceModel.trim().isEmpty()) {

                    deviceModel = "Unknown Device";
                }

                if (serviceName == null ||
                        serviceName.trim().isEmpty()) {

                    serviceName = "Repair Service";
                }

                if (problemDescription == null ||
                        problemDescription.trim().isEmpty()) {

                    problemDescription = "Not available";
                }

                if (branchName == null ||
                        branchName.trim().isEmpty()) {

                    branchName = "Not Assigned";
                }

                if (status == null ||
                        status.trim().isEmpty()) {

                    status = "Pending";
                }

                if (repairDate == null ||
                        repairDate.trim().isEmpty()) {

                    repairDate = "Not Scheduled";
                }


                if ("Ready for Collection"
                        .equalsIgnoreCase(status)) {

                    status = "Ready for Pickup";
                }


                com.up9.techfix.admin.appoiments.RepairAppointment appointment =
                        new com.up9.techfix.admin.appoiments.RepairAppointment(

                                repairId,

                                customerId,

                                customerName,

                                "",

                                customerPhone,

                                categoryName,

                                deviceModel,

                                serviceName,

                                problemDescription,

                                branchName,

                                "",

                                technicianId,

                                technicianId > 0
                                        ? "Technician #" +
                                          technicianId
                                        : "Not Assigned",

                                status,

                                repairDate,

                                0.0,

                                0.0
                        );


                appointment.setAppointmentTime("");

                list.add(appointment);
            }

        } finally {

            cursor.close();
        }

        return list;
    }
    // =====================================================
    // GET REPAIR BY ID - ADMIN
    // =====================================================

    public Cursor getRepairById(
            int repairId
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "r.id AS repair_id, " +
                        "r.customer_id, " +
                        "cu.full_name AS customer_name, " +
                        "cu.email AS customer_email, " +
                        "cu.phone AS customer_phone, " +
                        "r.category_id, " +
                        "c.name AS category_name, " +
                        "r.device_model, " +
                        "r.service_id, " +
                        "s.name AS service_name, " +
                        "s.price AS service_price, " +
                        "r.problem_description, " +
                        "r.branch_id, " +
                        "b.name AS branch_name, " +
                        "b.address AS branch_address, " +
                        "r.image_uri, " +
                        "r.in_progress_photo_uri, " +
                        "r.assigned_technician_id, " +
                        "r.technician_name, " +
                        "r.status, " +
                        "r.repair_date, " +
                        "r.final_price " +
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
                        "WHERE r.id = ? " +
                        "LIMIT 1",
                new String[]{
                        String.valueOf(repairId)
                }
        );
    }

    // =====================================================
    // UPDATE REPAIR STATUS
    // =====================================================

    public boolean updateRepairStatus(
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

        int rows =
                db.update(
                        TABLE_REPAIRS,
                        values,
                        "id = ?",
                        new String[]{
                                String.valueOf(repairId)
                        }
                );

        return rows > 0;
    }
    // =====================================================
// CUSTOMER ACTIVE REPAIRS
// =====================================================

    public Cursor getCustomerActiveRepairs(int customerId) {

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "r.id AS repair_id, " +
                        "r.category_id, " +
                        "c.name AS category_name, " +
                        "r.device_model, " +
                        "r.service_id, " +
                        "s.name AS service_name, " +
                        "r.problem_description, " +
                        "r.branch_id, " +
                        "b.name AS branch_name, " +
                        "b.address AS branch_address, " +
                        "r.image_uri, " +
                        "r.in_progress_photo_uri, " +
                        "r.assigned_technician_id, " +
                        "r.technician_name, " +
                        "r.status, " +
                        "r.repair_date, " +
                        "r.final_price " +
                        "FROM " +
                        TABLE_REPAIRS + " r " +
                        "LEFT JOIN " +
                        TABLE_CATEGORIES + " c " +
                        "ON r.category_id = c.id " +
                        "LEFT JOIN " +
                        TABLE_SERVICES + " s " +
                        "ON r.service_id = s.id " +
                        "LEFT JOIN " +
                        TABLE_BRANCHES + " b " +
                        "ON r.branch_id = b.id " +
                        "WHERE r.customer_id = ? " +
                        "AND r.status NOT IN ('Completed', 'Cancelled') " +
                        "ORDER BY r.id DESC",
                new String[]{
                        String.valueOf(customerId)
                }
        );
    }

    // =====================================================
    // ASSIGN TECHNICIAN
    // =====================================================

    public boolean assignTechnician(
            int repairId,
            int technicianId,
            String technicianName
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_REPAIR_ASSIGNED_TECHNICIAN_ID,
                technicianId
        );

        values.put(
                COL_REPAIR_TECHNICIAN_NAME,
                technicianName
        );

        values.put(
                COL_REPAIR_STATUS,
                "Assigned"
        );

        int rows =
                db.update(
                        TABLE_REPAIRS,
                        values,
                        "id = ?",
                        new String[]{
                                String.valueOf(repairId)
                        }
                );

        return rows > 0;
    }

    // =====================================================
    // ASSIGN TECHNICIAN - ID ONLY
    // =====================================================

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

    // =====================================================
    // UPDATE FINAL PRICE
    // =====================================================

    public boolean updateRepairFinalPrice(
            int repairId,
            double finalPrice
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_FINAL_PRICE,
                finalPrice
        );

        int rows =
                db.update(
                        TABLE_REPAIRS,
                        values,
                        "id = ?",
                        new String[]{
                                String.valueOf(repairId)
                        }
                );

        return rows > 0;
    }

    // =====================================================
    // GET PENDING REPAIRS
    // =====================================================

    public Cursor getPendingRepairs() {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "r.id AS repair_id, " +
                        "cu.full_name AS customer_name, " +
                        "cu.phone AS customer_phone, " +
                        "c.name AS category_name, " +
                        "r.device_model, " +
                        "s.name AS service_name, " +
                        "b.name AS branch_name, " +
                        "r.status, " +
                        "r.repair_date " +
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
                        "WHERE r.status = 'Pending' " +
                        "ORDER BY r.id DESC",
                null
        );
    }

    // =====================================================
    // REPAIR COUNT BY STATUS
    // =====================================================

    public int getRepairCountByStatus(
            String status
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) " +
                                "FROM " +
                                TABLE_REPAIRS +
                                " WHERE status = ?",
                        new String[]{
                                status
                        }
                );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();

        return count;
    }

    // =====================================================
    // REPAIR HISTORY
    // =====================================================

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

    // =====================================================
    // UNPAID REPAIRS
    // =====================================================

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

    // =====================================================
    // PAYMENT HISTORY
    // =====================================================

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

    // =====================================================
    // CREATE PAYMENT
    // =====================================================

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

        return db.insert(
                TABLE_PAYMENTS,
                null,
                values
        );
    }

    // =====================================================
    // UPDATE REPAIR IN-PROGRESS PHOTO
    // =====================================================

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

    // =====================================================
    // DASHBOARD - TOTAL REPAIRS
    // =====================================================

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

    // =====================================================
    // DASHBOARD - PENDING REPAIRS
    // =====================================================



    // =====================================================
    // CUSTOMERS
    // =====================================================

    public Cursor getAllCustomers() {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT id, full_name, email, phone " +
                        "FROM " +
                        TABLE_CUSTOMERS +
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
                        "FROM " +
                        TABLE_CUSTOMERS +
                        " WHERE id = ? " +
                        "LIMIT 1",
                new String[]{
                        String.valueOf(customerId)
                }
        );
    }

    // =====================================================
    // CLOSE DATABASE
    // =====================================================

    @Override
    public synchronized void close() {
        super.close();
    }
}