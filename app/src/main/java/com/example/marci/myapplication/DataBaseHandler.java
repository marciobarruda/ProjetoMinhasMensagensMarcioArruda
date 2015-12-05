package com.example.marci.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marci on 05/12/2015.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mensages_db";
    private static final String TABLE_MENSAGES = "mensages";

    private static final String KEY_ID = "_id";
    private static final String KEY_MENSAGE = "mensage";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MENSAGES_TABLE = "CREATE TABLE " + TABLE_MENSAGES + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MENSAGE + " TEXT" + ")";
        db.execSQL(CREATE_MENSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENSAGES);
        onCreate(db);
    }
}
