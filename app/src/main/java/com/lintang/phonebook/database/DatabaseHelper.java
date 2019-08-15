package com.lintang.phonebook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.lintang.phonebook.database.DatabaseContract.PersonCollumns.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "phonedb";
    private static final int VERSION = 1;

    private static final String CREATE_TABLE=String.format(
            "CREATE TABLE %s (" +
            "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
            "%s TEXT NOT NULL ," +
            "%s TEXT NOT NULL ," +
            "%s TEXT NOT NULL " +
            " )",
            TABLE_NAME,
            _ID,
            NAME,
            EMAIL,
            PHONE);

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
