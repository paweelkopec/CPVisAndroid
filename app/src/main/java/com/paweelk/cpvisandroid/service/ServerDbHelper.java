package com.paweelk.cpvisandroid.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 15.03.17.
 */
public class ServerDbHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "ServerDbHelper";
    private static final String DB_NAME = "servers.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_SERVERS = "SERVERS";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_URL = "URL";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_PASSWORD = "PASSWORD";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_SERVERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_URL + " TEXT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT " +
                    ")";

    public ServerDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        Log.i(DEBUG_TAG, "Utworzono tabelę " + TABLE_SERVERS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVERS);
        Log.i(DEBUG_TAG, "Usunięto tabelę " + TABLE_SERVERS);
        onCreate(db);
    }
}
