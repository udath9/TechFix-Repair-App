package com.up9.techfix.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.up9.techfix.admin.branches.Branch;
import com.up9.techfix.admin.repairsamples.RepairSample;
import com.up9.techfix.admin.services.RepairService;
import com.up9.techfix.admin.spareparts.SparePart;
import com.up9.techfix.admin.spareparts.categories.DeviceCategory;

import java.util.ArrayList;
import java.util.List;

public class TechFixDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TechFix.db";
    private static final int DATABASE_VERSION = 8;

    // BRANCH TABLE


    public static final String TABLE_BRANCHES = "branches";

    public static final String COL_BRANCH_ID = "id";
    public static final String COL_BRANCH_NAME = "name";
    public static final String COL_BRANCH_ADDRESS = "address";
    public static final String COL_BRANCH_PHONE = "phone";
    public static final String COL_BRANCH_LATITUDE = "latitude";
    public static final String COL_BRANCH_LONGITUDE = "longitude";


    // CATEGORY TABLE


    public static final String TABLE_CATEGORIES = "categories";

    public static final String COL_CATEGORY_ID = "id";
    public static final String COL_CATEGORY_NAME = "name";
    public static final String COL_CATEGORY_DESCRIPTION = "description";
    public static final String COL_CATEGORY_PRICE_MODIFIER = "price_modifier";

    // REPAIR SERVICES TABLE


    public static final String TABLE_SERVICES = "services";

    public static final String COL_SERVICE_ID = "id";
    public static final String COL_SERVICE_NAME = "name";
    public static final String COL_SERVICE_IMAGE_URI = "image_uri";
    public static final String COL_SERVICE_DESCRIPTION = "description";
    public static final String COL_SERVICE_PRICE = "price";
    public static final String COL_SERVICE_ESTIMATED_DAYS = "estimated_days";


    // REPAIR SAMPLE TABLE


    public static final String TABLE_REPAIR_SAMPLES = "repair_samples";

    public static final String COL_SAMPLE_ID = "id";
    public static final String COL_SAMPLE_DEVICE_NAME = "device_name";
    public static final String COL_SAMPLE_CATEGORY = "category";
    public static final String COL_SAMPLE_SERVICE = "service";
    public static final String COL_SAMPLE_DESCRIPTION = "description";
    public static final String COL_SAMPLE_IMAGE_URI = "image_uri";

    // SPARE PARTS TABLE
  

    public static final String TABLE_SPARE_PARTS = "spare_parts";

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


  

    public TechFixDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


  

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createBranchesTable =
                "CREATE TABLE " + TABLE_BRANCHES + " (" +
                        COL_BRANCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_BRANCH_NAME + " TEXT NOT NULL, " +
                        COL_BRANCH_ADDRESS + " TEXT NOT NULL, " +
                        COL_BRANCH_PHONE + " TEXT NOT NULL, " +
                        COL_BRANCH_LATITUDE + " REAL NOT NULL, " +
                        COL_BRANCH_LONGITUDE + " REAL NOT NULL" +
                        ")";

        db.execSQL(createBranchesTable);


        String createCategoriesTable =
                "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                        COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_CATEGORY_NAME + " TEXT NOT NULL, " +
                        COL_CATEGORY_DESCRIPTION + " TEXT, " +
                        COL_CATEGORY_PRICE_MODIFIER + " REAL NOT NULL DEFAULT 0" +
                        ")";

        db.execSQL(createCategoriesTable);

        String createServicesTable =
                "CREATE TABLE " + TABLE_SERVICES + " (" +
                        COL_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_SERVICE_NAME + " TEXT NOT NULL, " +
                        COL_SERVICE_IMAGE_URI + " TEXT, " +
                        COL_SERVICE_DESCRIPTION + " TEXT, " +
                        COL_SERVICE_PRICE + " REAL NOT NULL, " +
                        COL_SERVICE_ESTIMATED_DAYS + " INTEGER NOT NULL" +
                        ")";

        db.execSQL(createServicesTable);


        String createRepairSamplesTable =
                "CREATE TABLE " + TABLE_REPAIR_SAMPLES + " (" +
                        COL_SAMPLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_SAMPLE_DEVICE_NAME + " TEXT NOT NULL, " +
                        COL_SAMPLE_CATEGORY + " TEXT NOT NULL, " +
                        COL_SAMPLE_SERVICE + " TEXT NOT NULL, " +
                        COL_SAMPLE_DESCRIPTION + " TEXT, " +
                        COL_SAMPLE_IMAGE_URI + " TEXT" +
                        ")";

        db.execSQL(createRepairSamplesTable);


        String createSparePartsTable =
                "CREATE TABLE " + TABLE_SPARE_PARTS + " (" +
                        COL_SPARE_PART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_SPARE_PART_NAME + " TEXT NOT NULL, " +
                        COL_SPARE_PART_CATEGORY + " TEXT NOT NULL, " +
                        COL_SPARE_PART_NUMBER + " TEXT, " +
                        COL_SPARE_PART_QUANTITY + " INTEGER NOT NULL DEFAULT 0, " +
                        COL_SPARE_PART_MINIMUM_STOCK + " INTEGER NOT NULL DEFAULT 0, " +
                        COL_SPARE_PART_UNIT_PRICE + " REAL NOT NULL DEFAULT 0, " +
                        COL_SPARE_PART_SUPPLIER + " TEXT, " +
                        COL_SPARE_PART_DESCRIPTION + " TEXT, " +
                        COL_SPARE_PART_IMAGE_URI + " TEXT" +
                        ")";

        db.execSQL(createSparePartsTable);
    }
    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        if (oldVersion < 2) {

            String createCategoriesTable =
                    "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                            COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COL_CATEGORY_NAME + " TEXT NOT NULL, " +
                            COL_CATEGORY_DESCRIPTION + " TEXT" +
                            ")";

            db.execSQL(createCategoriesTable);
        }

        if (oldVersion < 3) {

            String createServicesTable =
                    "CREATE TABLE " + TABLE_SERVICES + " (" +
                            COL_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COL_SERVICE_NAME + " TEXT NOT NULL, " +
                            COL_SERVICE_IMAGE_URI + " TEXT, " +
                            COL_SERVICE_DESCRIPTION + " TEXT, " +
                            COL_SERVICE_PRICE + " REAL NOT NULL, " +
                            COL_SERVICE_ESTIMATED_DAYS + " INTEGER NOT NULL" +
                            ")";

            db.execSQL(createServicesTable);
        }

        if (oldVersion < 4) {

            String createRepairSamplesTable =
                    "CREATE TABLE " + TABLE_REPAIR_SAMPLES + " (" +
                            COL_SAMPLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COL_SAMPLE_DEVICE_NAME + " TEXT NOT NULL, " +
                            COL_SAMPLE_CATEGORY + " TEXT NOT NULL, " +
                            COL_SAMPLE_SERVICE + " TEXT NOT NULL, " +
                            COL_SAMPLE_DESCRIPTION + " TEXT, " +
                            COL_SAMPLE_IMAGE_URI + " TEXT" +
                            ")";

            db.execSQL(createRepairSamplesTable);
        }

        if (oldVersion < 5) {

            String createSparePartsTable =
                    "CREATE TABLE " + TABLE_SPARE_PARTS + " (" +
                            COL_SPARE_PART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COL_SPARE_PART_NAME + " TEXT NOT NULL, " +
                            COL_SPARE_PART_CATEGORY + " TEXT NOT NULL, " +
                            COL_SPARE_PART_NUMBER + " TEXT, " +
                            COL_SPARE_PART_QUANTITY + " INTEGER NOT NULL DEFAULT 0, " +
                            COL_SPARE_PART_MINIMUM_STOCK + " INTEGER NOT NULL DEFAULT 0, " +
                            COL_SPARE_PART_UNIT_PRICE + " REAL NOT NULL DEFAULT 0, " +
                            COL_SPARE_PART_SUPPLIER + " TEXT, " +
                            COL_SPARE_PART_DESCRIPTION + " TEXT, " +
                            COL_SPARE_PART_IMAGE_URI + " TEXT" +
                            ")";

            db.execSQL(createSparePartsTable);
        }

        if (oldVersion < 6) {

            db.execSQL(
                    "ALTER TABLE " + TABLE_CATEGORIES +
                            " ADD COLUMN " +
                            COL_CATEGORY_PRICE_MODIFIER +
                            " REAL NOT NULL DEFAULT 0"
            );
        }

        // Version 7 intentionally does nothing.
        // Repair samples are NOT deleted.
    }


    public long insertBranch(
            String name,
            String address,
            String phone,
            double latitude,
            double longitude
    ) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_BRANCH_NAME, name);
        values.put(COL_BRANCH_ADDRESS, address);
        values.put(COL_BRANCH_PHONE, phone);
        values.put(COL_BRANCH_LATITUDE, latitude);
        values.put(COL_BRANCH_LONGITUDE, longitude);

        long result =
                db.insert(
                        TABLE_BRANCHES,
                        null,
                        values
                );

        db.close();

        return result;
    }


    public List<Branch> getAllBranches() {

        List<Branch> branches = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_BRANCHES,
                        null,
                        null,
                        null,
                        null,
                        null,
                        COL_BRANCH_ID + " ASC"
                );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COL_BRANCH_ID
                                )
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_BRANCH_NAME
                                )
                        );

                String address =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_BRANCH_ADDRESS
                                )
                        );

                String phone =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_BRANCH_PHONE
                                )
                        );

                double latitude =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        COL_BRANCH_LATITUDE
                                )
                        );

                double longitude =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        COL_BRANCH_LONGITUDE
                                )
                        );

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

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

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

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_BRANCH_NAME, name);
        values.put(COL_BRANCH_ADDRESS, address);
        values.put(COL_BRANCH_PHONE, phone);
        values.put(COL_BRANCH_LATITUDE, latitude);
        values.put(COL_BRANCH_LONGITUDE, longitude);

        int result =
                db.update(
                        TABLE_BRANCHES,
                        values,
                        COL_BRANCH_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result;
    }


    public int deleteBranch(int id) {

        SQLiteDatabase db = getWritableDatabase();

        int result =
                db.delete(
                        TABLE_BRANCHES,
                        COL_BRANCH_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result;
    }


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

        long result =
                db.insert(
                        TABLE_CATEGORIES,
                        null,
                        values
                );

        db.close();

        return result;
    }


    public List<DeviceCategory> getAllCategories() {

        List<DeviceCategory> categories = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_CATEGORIES,
                        null,
                        null,
                        null,
                        null,
                        null,
                        COL_CATEGORY_ID + " ASC"
                );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COL_CATEGORY_ID
                                )
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_CATEGORY_NAME
                                )
                        );

                String description =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_CATEGORY_DESCRIPTION
                                )
                        );
                double priceModifier =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        COL_CATEGORY_PRICE_MODIFIER
                                )
                        );

                categories.add(
                        new DeviceCategory(
                                id,
                                name,
                                description,
                                priceModifier
                        )
                );

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return categories;
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

        int result =
                db.update(
                        TABLE_CATEGORIES,
                        values,
                        COL_CATEGORY_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result;
    }


    public int deleteCategory(int id) {

        SQLiteDatabase db = getWritableDatabase();

        int result =
                db.delete(
                        TABLE_CATEGORIES,
                        COL_CATEGORY_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result;
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

        long result =
                db.insert(
                        TABLE_SERVICES,
                        null,
                        values
                );

        db.close();

        return result;
    }


    public List<RepairService> getAllServices() {

        List<RepairService> services =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_SERVICES,
                        null,
                        null,
                        null,
                        null,
                        null,
                        COL_SERVICE_ID + " ASC"
                );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COL_SERVICE_ID
                                )
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_SERVICE_NAME
                                )
                        );

                String imageUri =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_SERVICE_IMAGE_URI
                                )
                        );

                String description =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_SERVICE_DESCRIPTION
                                )
                        );

                double price =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        COL_SERVICE_PRICE
                                )
                        );

                int estimatedDays =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COL_SERVICE_ESTIMATED_DAYS
                                )
                        );

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

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return services;
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

        int result =
                db.update(
                        TABLE_SERVICES,
                        values,
                        COL_SERVICE_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result;
    }


    public int deleteService(int id) {

        SQLiteDatabase db =
                getWritableDatabase();

        int result =
                db.delete(
                        TABLE_SERVICES,
                        COL_SERVICE_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result;
    }

    public long insertRepairSample(
            String deviceName,
            String category,
            String service,
            String description,
            String imageUri
    ) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_SAMPLE_DEVICE_NAME, deviceName);
        values.put(COL_SAMPLE_CATEGORY, category);
        values.put(COL_SAMPLE_SERVICE, service);
        values.put(COL_SAMPLE_DESCRIPTION, description);
        values.put(COL_SAMPLE_IMAGE_URI, imageUri);

        long result =
                db.insert(
                        TABLE_REPAIR_SAMPLES,
                        null,
                        values
                );

        db.close();

        return result;
    }


    public List<RepairSample> getAllRepairSamples() {

        List<RepairSample> samples = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_REPAIR_SAMPLES,
                        null,
                        null,
                        null,
                        null,
                        null,
                        COL_SAMPLE_ID + " ASC"
                );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COL_SAMPLE_ID
                                )
                        );

                String deviceName =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_SAMPLE_DEVICE_NAME
                                )
                        );

                String category =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_SAMPLE_CATEGORY
                                )
                        );

                String service =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_SAMPLE_SERVICE
                                )
                        );

                String description =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_SAMPLE_DESCRIPTION
                                )
                        );

                String imageUri =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_SAMPLE_IMAGE_URI
                                )
                        );

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

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

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

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_SAMPLE_DEVICE_NAME, deviceName);
        values.put(COL_SAMPLE_CATEGORY, category);
        values.put(COL_SAMPLE_SERVICE, service);
        values.put(COL_SAMPLE_DESCRIPTION, description);
        values.put(COL_SAMPLE_IMAGE_URI, imageUri);

        int result =
                db.update(
                        TABLE_REPAIR_SAMPLES,
                        values,
                        COL_SAMPLE_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result;
    }


    public int deleteRepairSample(int id) {

        SQLiteDatabase db = getWritableDatabase();

        int result =
                db.delete(
                        TABLE_REPAIR_SAMPLES,
                        COL_SAMPLE_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result;
    }


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

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_SPARE_PART_NAME, name);
        values.put(COL_SPARE_PART_CATEGORY, category);
        values.put(COL_SPARE_PART_NUMBER, partNumber);
        values.put(COL_SPARE_PART_QUANTITY, quantity);
        values.put(COL_SPARE_PART_MINIMUM_STOCK, minimumStock);
        values.put(COL_SPARE_PART_UNIT_PRICE, unitPrice);
        values.put(COL_SPARE_PART_SUPPLIER, supplier);
        values.put(COL_SPARE_PART_DESCRIPTION, description);
        values.put(COL_SPARE_PART_IMAGE_URI, imageUri);

        long result =
                db.insert(
                        TABLE_SPARE_PARTS,
                        null,
                        values
                );

        db.close();

        return result;
    }


    public List<SparePart> getAllSpareParts() {

        List<SparePart> spareParts = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_SPARE_PARTS,
                        null,
                        null,
                        null,
                        null,
                        null,
                        COL_SPARE_PART_ID + " ASC"
                );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COL_SPARE_PART_ID
                                )
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_SPARE_PART_NAME
                                )
                        );

                String category =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_SPARE_PART_CATEGORY
                                )
                        );

                String partNumber =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_SPARE_PART_NUMBER
                                )
                        );

                int quantity =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COL_SPARE_PART_QUANTITY
                                )
                        );

                int minimumStock =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COL_SPARE_PART_MINIMUM_STOCK
                                )
                        );

                double unitPrice =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        COL_SPARE_PART_UNIT_PRICE
                                )
                        );

                String supplier =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_SPARE_PART_SUPPLIER
                                )
                        );

                String description =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_SPARE_PART_DESCRIPTION
                                )
                        );

                String imageUri =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_SPARE_PART_IMAGE_URI
                                )
                        );

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

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

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

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_SPARE_PART_NAME, name);
        values.put(COL_SPARE_PART_CATEGORY, category);
        values.put(COL_SPARE_PART_NUMBER, partNumber);
        values.put(COL_SPARE_PART_QUANTITY, quantity);
        values.put(COL_SPARE_PART_MINIMUM_STOCK, minimumStock);
        values.put(COL_SPARE_PART_UNIT_PRICE, unitPrice);
        values.put(COL_SPARE_PART_SUPPLIER, supplier);
        values.put(COL_SPARE_PART_DESCRIPTION, description);
        values.put(COL_SPARE_PART_IMAGE_URI, imageUri);

        int result =
                db.update(
                        TABLE_SPARE_PARTS,
                        values,
                        COL_SPARE_PART_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result;
    }


    public int deleteSparePart(int id) {

        SQLiteDatabase db = getWritableDatabase();

        int result =
                db.delete(
                        TABLE_SPARE_PARTS,
                        COL_SPARE_PART_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result;
    }


    public int updateStockQuantity(
            int sparePartId,
            int newQuantity
    ) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(
                COL_SPARE_PART_QUANTITY,
                newQuantity
        );

        int result =
                db.update(
                        TABLE_SPARE_PARTS,
                        values,
                        COL_SPARE_PART_ID + " = ?",
                        new String[]{
                                String.valueOf(sparePartId)
                        }
                );

        db.close();

        return result;
    }


    public List<SparePart> getLowStockItems() {

        List<SparePart> lowStockItems = new ArrayList<>();

        List<SparePart> allParts =
                getAllSpareParts();

        for (SparePart part : allParts) {

            if (part.getQuantity()
                    <= part.getMinimumStock()) {

                lowStockItems.add(part);
            }
        }

        return lowStockItems;
    }
}