package com.example.hector.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hector on 21/01/2015.
 */
public class DBController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;

    public DBController(Context applicationcontext) {
        super(applicationcontext, "DBCoches.db", null, 1);
        Log.d(LOGCAT, "DBCoches.db creada");
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE Coches (id integer PRIMARY KEY AUTOINCREMENT,idfoto INTEGER, matricula TEXT,marca TEXT, modelo TEXT,motorizacion TEXT, cilindrada INTEGER,fechaCompra TEXT)";
        database.execSQL(query);
        Log.d(LOGCAT,"Coches Created");
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS Coches";
        database.execSQL(query);
        onCreate(database);
    }

    public void insertCoche(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idfoto", queryValues.get("idfoto"));
        values.put("matricula", queryValues.get("matricula"));
        values.put("marca", queryValues.get("marca"));
        values.put("modelo", queryValues.get("modelo"));
        values.put("motorizacion", queryValues.get("motorizacion"));
        values.put("cilindrada", queryValues.get("cilindrada"));
        values.put("fechaCompra", queryValues.get("fechaCompra"));
        database.insert("Coches", null, values);
        database.close();
    }
    public int updateCoche(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idfoto", queryValues.get("idfoto"));
        values.put("matricula", queryValues.get("matricula"));
        values.put("marca", queryValues.get("marca"));
        values.put("modelo", queryValues.get("modelo"));
        values.put("motorizacion", queryValues.get("motorizacion"));
        values.put("cilindrada", queryValues.get("cilindrada"));
        values.put("fechaCompra", queryValues.get("fechaCompra"));
        return database.update("Coches", values, "id" + " = ?", new String[] { queryValues.get("id") });
    }

    public void deleteAllCoches() {
        Log.d(LOGCAT,"deleteAll");
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM Coches";
        Log.d("query",deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void deleteCoche(String id) {
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM Coches where id='"+ id +"'";
        Log.d("query",deleteQuery);
        database.execSQL(deleteQuery);
    }

    public ArrayList<HashMap<String, String>> getAllCoches() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM Coches";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("idfoto", cursor.getString(1));
                map.put("matricula", cursor.getString(2));
                map.put("marca", cursor.getString(3));
                map.put("modelo", cursor.getString(4));
                map.put("motorizacion", cursor.getString(5));
                map.put("cilindrada", cursor.getString(6));
                map.put("fechaCompra", cursor.getString(7));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        // return contact list
        return wordList;
    }
    public HashMap<String, String> getCocheInfo(String id) {
        HashMap<String, String> wordList = new HashMap<String, String>();
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM Coches where id='"+id+"'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                //HashMap<String, String> map = new HashMap<String, String>();
                wordList.put("idfoto", cursor.getString(1));
                wordList.put("matricula", cursor.getString(2));
                wordList.put("marca", cursor.getString(3));
                wordList.put("modelo", cursor.getString(4));
                wordList.put("motorizacion", cursor.getString(5));
                wordList.put("cilindrada", cursor.getString(6));
                wordList.put("fechaCompra", cursor.getString(7));
                //wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
    }


}
