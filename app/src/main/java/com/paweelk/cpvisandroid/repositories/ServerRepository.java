package com.paweelk.cpvisandroid.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.paweelk.cpvisandroid.model.Server;
import com.paweelk.cpvisandroid.service.CpVisDbHelper;
import com.paweelk.cpvisandroid.service.CpVisDbHelper.ServerContract;
import java.util.ArrayList;


/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 15.03.17.
 */
public class ServerRepository {

    private static final String DEBUG_TAG = "Server repository";

    private SQLiteDatabase db;
    private CpVisDbHelper CpVisDbHelper;
    private static final String[] COLUMNS = {
            ServerContract.COLUMN_ID,
            ServerContract.COLUMN_NAME,
            ServerContract.COLUMN_URL,
            ServerContract.COLUMN_USERNAME,
            ServerContract.COLUMN_PASSWORD
    };

    public ServerRepository(Context context) {
        this.CpVisDbHelper = new CpVisDbHelper(context);
    }

    public void open() {
        this.db = this.CpVisDbHelper.getWritableDatabase();
        Log.i(DEBUG_TAG, "CpVisDbHelper otworzono");
    }

    public void close() {
        if (this.CpVisDbHelper != null) {
            this.CpVisDbHelper.close();
            Log.i(DEBUG_TAG, "CpVisDbHelper zamknięto");
        }
    }

    public ArrayList<Server> findAllOrderedByName() {
        Cursor cursor = null;
        ArrayList servers = new ArrayList();
        try {
            String orderBy = ServerContract.COLUMN_NAME + " ASC";
            cursor = db.query(ServerContract.TABLE_SERVERS, COLUMNS, null, null, null, null, orderBy);
            while (cursor.moveToNext()) {
                Server server = new Server();
                server.setId(cursor.getLong(cursor.getColumnIndex( ServerContract.COLUMN_ID)));
                server.setName(cursor.getString(cursor.getColumnIndex(ServerContract.COLUMN_NAME)));
                server.setUrl(cursor.getString(cursor.getColumnIndex(ServerContract.COLUMN_URL)));
                server.setUsername(cursor.getString(cursor.getColumnIndex(ServerContract.COLUMN_USERNAME)));
                server.setPassword(cursor.getString(cursor.getColumnIndex(ServerContract.COLUMN_PASSWORD)));
                servers.add(server);
            }
            Log.i(DEBUG_TAG, "Liczba wierszy " + cursor.getCount());
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Zgłoszono wyjątek: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return servers;
    }

    public Server findById(Long id) {
        Cursor cursor = null;
        Server server = new Server();
        try {
            String section = ServerContract.COLUMN_ID+ " = ?";
            String[] sectionArgs = { id.toString() };
            cursor = db.query(ServerContract.TABLE_SERVERS, COLUMNS, section, sectionArgs, null, null, null);
            while (cursor.moveToNext()) {
                server.setId(cursor.getLong(cursor.getColumnIndex(ServerContract.COLUMN_ID)));
                server.setName(cursor.getString(cursor.getColumnIndex(ServerContract.COLUMN_NAME)));
                server.setUrl(cursor.getString(cursor.getColumnIndex(ServerContract.COLUMN_URL)));
                server.setUsername(cursor.getString(cursor.getColumnIndex(ServerContract.COLUMN_USERNAME)));
                server.setPassword(cursor.getString(cursor.getColumnIndex(ServerContract.COLUMN_PASSWORD)));
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Exception: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return server;
    }

    public Server create (Server server){
        ContentValues values = new ContentValues();
        values.put(ServerContract.COLUMN_NAME, server.getName());
        values.put(ServerContract.COLUMN_URL, server.getUrl());
        values.put(ServerContract.COLUMN_USERNAME, server.getUsername());
        values.put(ServerContract.COLUMN_PASSWORD, server.getPassword());
        Long id = db.insert(ServerContract.TABLE_SERVERS, null, values);
        server.setId(id);
        Log.i(DEBUG_TAG, "Server "+id+" has been added.");
        return server;
    }

    public Server update (Server server){
        Long id = server.getId();
        String whereClause = ServerContract.COLUMN_ID+ " = ?";
        String[] whereArgs = { id.toString() };
        ContentValues values = new ContentValues();
        values.put(ServerContract.COLUMN_NAME, server.getName());
        values.put(ServerContract.COLUMN_URL, server.getUrl());
        values.put(ServerContract.COLUMN_USERNAME, server.getUsername());
        values.put(ServerContract.COLUMN_PASSWORD, server.getPassword());
        db.update(ServerContract.TABLE_SERVERS, values, whereClause, whereArgs );
        Log.i(DEBUG_TAG, "Server "+id+" has been updated.");
        return server;
    }

    public void delete (Long id){
        String whereClause = ServerContract.COLUMN_ID+ " = ?";
        String[] whereArgs = { id.toString() };
        db.delete(ServerContract.TABLE_SERVERS, whereClause, whereArgs);
        Log.i(DEBUG_TAG, "Usunięto server o identyfikatorze: " + id);
    }


}
