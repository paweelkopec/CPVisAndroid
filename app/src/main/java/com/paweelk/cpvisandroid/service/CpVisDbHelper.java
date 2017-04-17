package com.paweelk.cpvisandroid.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.paweelk.cpvisandroid.model.Settings;

/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 15.03.17.
 */
public class CpVisDbHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "CpVisDbHelper";
    private static final String DB_NAME = "cpvis.db";
    private static final int DB_VERSION = 2;

    public CpVisDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static class ServerContract {

        private ServerContract() {
        }

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
    }

    public static class SettingsContract {

        public static final String TABLE_SETTINGS = "settings";
        public static final String COLUMN_KEY = "KEY";
        public static final String COLUMN_VALUE = "VALUE";

        private static final String TABLE_CREATE =
                "CREATE TABLE " + TABLE_SETTINGS + " (" +
                        COLUMN_KEY + " TEXT NOT NULL  UNIQUE, " +
                        COLUMN_VALUE + " TEXT " +
                        ");";

        private static final String TABLE_INSERT =
                "INSERT INTO  " + TABLE_SETTINGS + "  ( " + COLUMN_KEY + ", " + COLUMN_VALUE + " ) " +
                        "VALUES ('intervalMillisecond', '1000');";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(DEBUG_TAG, "Tables creating");
        db.execSQL(ServerContract.TABLE_CREATE);
        db.execSQL(SettingsContract.TABLE_CREATE);
        db.execSQL(SettingsContract.TABLE_INSERT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(DEBUG_TAG, "Tables delting ");
        db.execSQL("DROP TABLE IF EXISTS " + ServerContract.TABLE_SERVERS);
        db.execSQL("DROP TABLE IF EXISTS " + SettingsContract.TABLE_SETTINGS);
        onCreate(db);
    }
}
