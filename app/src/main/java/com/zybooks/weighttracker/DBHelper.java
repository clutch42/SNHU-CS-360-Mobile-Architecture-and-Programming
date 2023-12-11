package com.zybooks.weighttracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weightTrackerDatabase";
    private static final int DATABASE_VERSION = 1;
    protected static final String TABLE_NAME_USERS = "users";
    protected static final String TABLE_DAILY_WEIGHT = "dailyWeight";
    protected static final String TABLE_TARGET_WEIGHT = "targetWeight";
    protected static final String COLUMN_USERNAME = "username";
    protected static final String COLUMN_PASSWORD = "password";
    protected static final String COLUMN_DATE = "date";
    protected static final String COLUMN_WEIGHT = "weight";
    protected static final String COLUMN_TARGET_WEIGHT = "targetWeight";
    protected static final String COLUMN_ID = "id";

    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USERS + " (" +
                    COLUMN_USERNAME + " TEXT PRIMARY KEY," +
                    COLUMN_PASSWORD + " TEXT)";

    private static final String CREATE_TABLE_DAILY_WEIGHT =
            "CREATE TABLE IF NOT EXISTS " + TABLE_DAILY_WEIGHT + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USERNAME + " TEXT," +
                    COLUMN_DATE + " TEXT," +
                    COLUMN_WEIGHT + " REAL)";

    private static final String CREATE_TABLE_TARGET_WEIGHT =
            "CREATE TABLE IF NOT EXISTS " + TABLE_TARGET_WEIGHT + " (" +
                    COLUMN_USERNAME + " TEXT PRIMARY KEY," +
                    COLUMN_TARGET_WEIGHT + " REAL)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_DAILY_WEIGHT);
        db.execSQL(CREATE_TABLE_TARGET_WEIGHT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY_WEIGHT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TARGET_WEIGHT);
        onCreate(db);
    }
    public void insertDailyWeight(String username, String date, double weight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_WEIGHT, weight);

        // Insert data into the dailyWeight table
        long result = db.insert(TABLE_DAILY_WEIGHT, null, values);

        // troubleshooting
        if (result != -1) {
            Log.d("DBHelper", "Daily weight inserted successfully for username: " + username + ", Date: " + date + ", Weight: " + weight);
        } else {
            Log.e("DBHelper", "Failed to insert target weight for username: " + username);
        }

        db.close();
    }

    public void insertTargetWeight(String username, double weight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_TARGET_WEIGHT, weight);

        // Insert data into the dailyWeight table
        long result = db.insertWithOnConflict(TABLE_TARGET_WEIGHT, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        // troubleshooting
        if (result != -1) {
            Log.d("DBHelper", "Target weight inserted successfully for username: " + username + ", Weight: " + weight);
        } else {
            Log.e("DBHelper", "Failed to insert target weight for username: " + username);
        }

        db.close();
    }

    public Cursor getAllDailyWeights(String username) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_USERNAME, COLUMN_DATE, COLUMN_WEIGHT};
        String selection = COLUMN_USERNAME + "=?";
        String[] selectionArgs = {username};
        return db.query(TABLE_DAILY_WEIGHT, projection, selection, selectionArgs, null, null, null);
    }

    public Cursor getTargetWeight(String username) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {COLUMN_TARGET_WEIGHT};
        String selection = COLUMN_USERNAME + "=?";
        String[] selectionArgs = {username};

        return db.query(TABLE_TARGET_WEIGHT, projection, selection, selectionArgs, null, null, null);
    }

    public void updateDailyWeight(long id, String date, double weight) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_DATE, date);
        String whereClause = "id=?";
        String[] whereArgs = {String.valueOf(id)};

        // Update data in the dailyWeight table
        int result = db.update(TABLE_DAILY_WEIGHT, values, whereClause, whereArgs);

        // Troubleshooting
        if (result > 0) {
            Log.d("DBHelper", "Daily weight updated successfully for id: " + id + ", Date: " + date + ", Weight: " + weight);
        } else {
            Log.e("DBHelper", "Failed to update daily weight for id: " + id);
        }

        db.close();
    }

    public void deleteDailyWeight(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        db.delete(TABLE_DAILY_WEIGHT, selection, selectionArgs);
        db.close();
    }

    public double getTargetWeightForUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        double targetWeight = 0.0;

        Cursor cursor = db.rawQuery("SELECT " + COLUMN_TARGET_WEIGHT + " FROM " + TABLE_TARGET_WEIGHT +
                " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            targetWeight = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TARGET_WEIGHT));
            cursor.close();
        }

        return targetWeight;
    }
}