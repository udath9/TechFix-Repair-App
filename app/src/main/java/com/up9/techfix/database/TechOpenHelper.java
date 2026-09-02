package com.up9.techfix.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TechOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TechFix.db";
    private static final int DATABASE_VERSION = 1;

    public TechOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Customers table
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS customers (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "full_name TEXT," +
                        "email TEXT," +
                        "phone TEXT)"
        );

        // Services table
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS services (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "image_uri TEXT," +
                        "description TEXT," +
                        "price REAL)"
        );

        // Branches table
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS branches (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "address TEXT," +
                        "phone TEXT)"
        );

        // Repairs table
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS repairs (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "customer_id INTEGER," +
                        "device_category TEXT," +
                        "device_model TEXT," +
                        "service_id INTEGER," +
                        "problem_description TEXT," +
                        "branch_id INTEGER," +
                        "image_uri TEXT," +
                        "status TEXT," +
                        "repair_date TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS repairs");
        db.execSQL("DROP TABLE IF EXISTS customers");
        db.execSQL("DROP TABLE IF EXISTS services");
        db.execSQL("DROP TABLE IF EXISTS branches");

        onCreate(db);
    }
}