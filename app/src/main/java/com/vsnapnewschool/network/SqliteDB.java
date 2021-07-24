package com.vsnapnewschool.network;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteDB extends SQLiteOpenHelper {

    public Context con;
    public static final String DATABASE_NAME = "NetworkCheckDB.db";
    private static final int DATABASE_VERSION = 1;
    public Context context;
    //Table Name
    public static final String NETWORK_DETAILS = "NETWORK_DETAILS";
    //Column Names
    public static final String MOBILE_NUMBER = "MOBILE_NUMBER";
    public static final String TIME = "TIME";
    public static final String INTERNET_SPEED = "INTERNET_SPEED";

    public SqliteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
        con = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + NETWORK_DETAILS +
                        "("
                        + "MOBILE_NUMBER text unique primary key," +
                        " TIME text," +
                        "INTERNET_SPEED text" +
                        ")"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NETWORK_DETAILS);

    }
    public void addNetworkDetails(String mobile_number, String timestamp,String internet_speed ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(MOBILE_NUMBER, mobile_number);
        initialValues.put(TIME, timestamp);
        initialValues.put(INTERNET_SPEED, internet_speed);
        db.insert(NETWORK_DETAILS, null, initialValues);
        db.close();
    }

    public void updateNetworkDetails(String mobile_number, String timestamp,String internet_speed ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(TIME, timestamp);
        initialValues.put(INTERNET_SPEED, internet_speed);
        db.update(NETWORK_DETAILS, initialValues, "MOBILE_NUMBER=" + mobile_number, null);
        db.close();
    }

    public boolean checkNetworkDetails(String mobile_number) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + NETWORK_DETAILS + " where MOBILE_NUMBER =?";
        Log.d("query", query);
        Cursor cursor = db.rawQuery(query, new String[]{mobile_number});
        if (cursor.getCount() > 0) {
            return true;
        } else
            return false;
    }

    public Cursor getNetWorkDetails(String mobile_number) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + NETWORK_DETAILS + " where MOBILE_NUMBER =?";
        Log.d("query", query);
        Cursor cursor = db.rawQuery(query, new String[]{mobile_number});
        Log.d("cursor", String.valueOf(cursor));
        return cursor;
    }
}
