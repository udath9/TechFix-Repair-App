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
    private static final int DATABASE_VERSION = 1;

    // Branch table
    public static final String TABLE_BRANCHES = "branches";

    public static final String COL_BRANCH_ID = "id";
    public static final String COL_BRANCH_NAME = "name";
    public static final String COL_BRANCH_ADDRESS = "address";
    public static final String COL_BRANCH_PHONE = "phone";
    public static final String COL_BRANCH_LATITUDE = "latitude";
    public static final String COL_BRANCH_LONGITUDE = "longitude";

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
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        db.execSQL(
                "DROP TABLE IF EXISTS " + TABLE_BRANCHES
        );

        onCreate(db);
    }

    // INSERT BRANCH

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

    // GET ALL BRANCHES

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

    // UPDATE BRANCH


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

    // DELETE BRANCH


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
}