package com.up9.techfix.Technician;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

        String createTable =
                "CREATE TABLE Techniciandetails (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT, " +
                        "email TEXT, " +
                        "password TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Techniciandetails");

        onCreate(db);
    }

    // Insert technician
    public boolean insertUser(String username, String email, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("username", username);
        values.put("email", email);
        values.put("password", password);

        long result = db.insert(
                "Techniciandetails",
                null,
                values
        );

        db.close();

        return result != -1;
    }

    // Check login
    public boolean checkUser(String username, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Techniciandetails WHERE username=? AND password=?",
                new String[]{username, password}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    // Check username
    public boolean usernameExists(String username) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Techniciandetails WHERE username=?",
                new String[]{username}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }
}