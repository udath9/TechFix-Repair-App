package com.up9.techfix.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TechFixDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TechFix.db";
    private static final int DATABASE_VERSION = 3;

    // Branch table
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

    // REPAIR SERVICES TABLE
    public static final String TABLE_SERVICES = "repair_services";

    public static final String COL_SERVICE_ID = "id";
    public static final String COL_SERVICE_NAME = "name";
    public static final String COL_SERVICE_CATEGORY = "category";
    public static final String COL_SERVICE_DESCRIPTION = "description";
    public static final String COL_SERVICE_PRICE = "price";
    public static final String COL_SERVICE_ESTIMATED_DAYS = "estimated_days";
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
                        COL_CATEGORY_DESCRIPTION + " TEXT" +
                        ")";

        db.execSQL(createCategoriesTable);

        String createServicesTable =
                "CREATE TABLE " + TABLE_SERVICES + " (" +
                        COL_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_SERVICE_NAME + " TEXT NOT NULL, " +
                        COL_SERVICE_CATEGORY + " TEXT NOT NULL, " +
                        COL_SERVICE_DESCRIPTION + " TEXT, " +
                        COL_SERVICE_PRICE + " REAL NOT NULL, " +
                        COL_SERVICE_ESTIMATED_DAYS + " INTEGER NOT NULL" +
                        ")";

        db.execSQL(createServicesTable);
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
                            COL_SERVICE_CATEGORY + " TEXT NOT NULL, " +
                            COL_SERVICE_DESCRIPTION + " TEXT, " +
                            COL_SERVICE_PRICE + " REAL NOT NULL, " +
                            COL_SERVICE_ESTIMATED_DAYS + " INTEGER NOT NULL" +
                            ")";

            db.execSQL(createServicesTable);
        }
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

        List<Branch> branches =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

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

        SQLiteDatabase db =
                getWritableDatabase();

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
            String description
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

        List<DeviceCategory> categories =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

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

                categories.add(
                        new DeviceCategory(
                                id,
                                name,
                                description
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
            String description
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

        SQLiteDatabase db =
                getWritableDatabase();

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
            String category,
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
                COL_SERVICE_CATEGORY,
                category
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

                String category =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_SERVICE_CATEGORY
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
                                category,
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
            String category,
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
                COL_SERVICE_CATEGORY,
                category
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
}