package com.michalpolic.weather.android.database.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Michal on 10.6.2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int sDATABASE_VERSION = 4;
    public static final String sDATABASE_NAME = "weather";

    public static final String sWEATHER_TABLE_NAME = "weather";
    private static final String sWEATHER_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + sWEATHER_TABLE_NAME + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "date INTEGER," +
                    "city TEXT," +
                    "description TEXT," +
                    "icon TEXT," +
                    "temperature REAL," +
                    "precipitation REAL," +
                    "humidity REAL," +
                    "pressure REAL," +
                    "wind REAL," +
                    "direction REAL);";


    public DatabaseHelper(Context context) {
        super(context, sDATABASE_NAME, null, sDATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sWEATHER_TABLE_CREATE);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sWEATHER_TABLE_CREATE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + sWEATHER_TABLE_NAME);
        db.execSQL(sWEATHER_TABLE_CREATE);
    }
}