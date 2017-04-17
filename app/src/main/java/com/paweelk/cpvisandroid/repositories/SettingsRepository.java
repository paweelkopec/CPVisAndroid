package com.paweelk.cpvisandroid.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.paweelk.cpvisandroid.model.Settings;
import com.paweelk.cpvisandroid.service.CpVisDbHelper;
import com.paweelk.cpvisandroid.service.CpVisDbHelper.SettingsContract;


/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 15.03.17.
 */
public class SettingsRepository {

    private static final String DEBUG_TAG = "Settings repository";

    private SQLiteDatabase db;
    private CpVisDbHelper mCpVisDbHelper;
    private static final String[] COLUMNS = {
            SettingsContract.COLUMN_KEY,
            SettingsContract.COLUMN_VALUE,
    };

    public SettingsRepository(Context context) {
        mCpVisDbHelper = new CpVisDbHelper(context);
    }

    public void open() {
        this.db = mCpVisDbHelper.getWritableDatabase();
        Log.i(DEBUG_TAG, "CpVisDbHelper has been opened");
    }

    public void close() {
        if (mCpVisDbHelper != null) {
            mCpVisDbHelper.close();
            Log.i(DEBUG_TAG, "CpVisDbHelper has been closed");
        }
    }

    public Settings findAll() {
        Cursor cursor = null;
        Settings mSettings = new Settings();
        try {
            cursor = db.query(SettingsContract.TABLE_SETTINGS, COLUMNS, null, null, null, null, null);
            while (cursor.moveToNext()) {
                    String key = cursor.getString(cursor.getColumnIndex(SettingsContract.COLUMN_KEY));
                    String value = cursor.getString(cursor.getColumnIndex(SettingsContract.COLUMN_VALUE));
                    switch(key){
                        case "intervalMillisecond":
                            mSettings.setIntervalMillisecond(Integer.parseInt(value));
                            break;
                    }
            }
            Log.e(DEBUG_TAG, "Total rows " + cursor.getCount());
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Exception: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return mSettings;
    }



    public void update (Settings mSettings){
        String whereClause = SettingsContract.COLUMN_KEY+ " = ?";
        String[] whereArgs = { "intervalMillisecond" };
        ContentValues values = new ContentValues();
        values.put(SettingsContract.COLUMN_VALUE, String.valueOf(mSettings.getIntervalMillisecond()));
        db.update(SettingsContract.TABLE_SETTINGS, values, whereClause, whereArgs );
        Log.i(DEBUG_TAG, "Settings have been updated ");
    }



}
