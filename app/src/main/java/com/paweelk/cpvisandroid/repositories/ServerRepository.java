package com.paweelk.cpvisandroid.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.paweelk.cpvisandroid.model.Server;
import com.paweelk.cpvisandroid.service.ServerDbHelper;

import java.util.ArrayList;


/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 15.03.17.
 */
public class ServerRepository {

    private static final String DEBUG_TAG = "Server repository";

    private SQLiteDatabase db;
    private ServerDbHelper serverDbHelper;
    private static final String[] COLUMNS = {
            ServerDbHelper.COLUMN_ID,
            ServerDbHelper.COLUMN_NAME,
            ServerDbHelper.COLUMN_URL,
            ServerDbHelper.COLUMN_USERNAME,
            ServerDbHelper.COLUMN_PASSWORD
    };

    public ServerRepository(Context context) {
        this.serverDbHelper = new ServerDbHelper(context);
    }

    public void open() {
        this.db = this.serverDbHelper.getWritableDatabase();
        Log.i(DEBUG_TAG, "ServerDbHelper otworzono");
    }

    public void close() {
        if (this.serverDbHelper != null) {
            this.serverDbHelper.close();
            Log.i(DEBUG_TAG, "ServerDbHelper zamknięto");
        }
    }

    public ArrayList<Server> findAllOrderedByName() {
        Cursor cursor = null;
        ArrayList servers = new ArrayList();
        try {
            String orderBy = ServerDbHelper.COLUMN_NAME + " ASC";
            cursor = db.query(ServerDbHelper.TABLE_SERVERS, COLUMNS, null, null, null, null, orderBy);
            while (cursor.moveToNext()) {
                Server server = new Server();
                server.setId(cursor.getLong(cursor.getColumnIndex(ServerDbHelper.COLUMN_ID)));
                server.setName(cursor.getString(cursor.getColumnIndex(ServerDbHelper.COLUMN_NAME)));
                server.setUrl(cursor.getString(cursor.getColumnIndex(ServerDbHelper.COLUMN_URL)));
                server.setUsername(cursor.getString(cursor.getColumnIndex(ServerDbHelper.COLUMN_USERNAME)));
                server.setPassword(cursor.getString(cursor.getColumnIndex(ServerDbHelper.COLUMN_PASSWORD)));
                servers.add(server);
            }
            Log.e(DEBUG_TAG, "Liczba wierszy " + cursor.getCount());
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
            String section = ServerDbHelper.COLUMN_ID+ " = ?";
            String[] sectionArgs = { id.toString() };
            cursor = db.query(ServerDbHelper.TABLE_SERVERS, COLUMNS, section, sectionArgs, null, null, null);
            while (cursor.moveToNext()) {
                server.setId(cursor.getLong(cursor.getColumnIndex(ServerDbHelper.COLUMN_ID)));
                server.setName(cursor.getString(cursor.getColumnIndex(ServerDbHelper.COLUMN_NAME)));
                server.setUrl(cursor.getString(cursor.getColumnIndex(ServerDbHelper.COLUMN_URL)));
                server.setUsername(cursor.getString(cursor.getColumnIndex(ServerDbHelper.COLUMN_USERNAME)));
                server.setPassword(cursor.getString(cursor.getColumnIndex(ServerDbHelper.COLUMN_PASSWORD)));
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Zgłoszono wyjątek: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return server;
    }

    public Server create (Server server){
        ContentValues values = new ContentValues();
        values.put(ServerDbHelper.COLUMN_NAME, server.getName());
        values.put(ServerDbHelper.COLUMN_URL, server.getUrl());
        values.put(ServerDbHelper.COLUMN_USERNAME, server.getUsername());
        values.put(ServerDbHelper.COLUMN_PASSWORD, server.getPassword());
        Long id = db.insert(ServerDbHelper.TABLE_SERVERS, null, values);
        server.setId(id);
        Log.i(DEBUG_TAG, "Dodano server o identyfikatorze: " + id);
        return server;
    }

    public Server update (Server server){
        Long id = server.getId();
        String whereClause = ServerDbHelper.COLUMN_ID+ " = ?";
        String[] whereArgs = { id.toString() };
        ContentValues values = new ContentValues();
        values.put(ServerDbHelper.COLUMN_NAME, server.getName());
        values.put(ServerDbHelper.COLUMN_URL, server.getUrl());
        values.put(ServerDbHelper.COLUMN_USERNAME, server.getUsername());
        values.put(ServerDbHelper.COLUMN_PASSWORD, server.getPassword());
        db.update(ServerDbHelper.TABLE_SERVERS, values, whereClause, whereArgs );
        Log.i(DEBUG_TAG, "Zaktualizowano server o identyfikatorze: " + id);
        return server;
    }

    public void delete (Long id){
        String whereClause = ServerDbHelper.COLUMN_ID+ " = ?";
        String[] whereArgs = { id.toString() };
        db.delete(ServerDbHelper.TABLE_SERVERS, whereClause, whereArgs);
        Log.i(DEBUG_TAG, "Usunięto server o identyfikatorze: " + id);
    }


}
