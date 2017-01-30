package com.uoit.calvin.thesis_2016;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class TransactionDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "transDB";
    private static final String TABLE_NAME = "transactions";

    private static final String KEY_ID = "id";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_TIMESTAMPS = "timestamps";
    private static final String KEY_YEAR = "year";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DAY = "day";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_USER = "user";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_GENERAL = "general";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME+ " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_MESSAGE + " TEXT," +
                    KEY_TIMESTAMPS + " TEXT," +
                    KEY_GENERAL + " TEXT," +
                    KEY_LOCATION + " TEXT," +
                    KEY_CATEGORY + " TEXT," +
                    KEY_AMOUNT + " REAL, " +
                    KEY_USER + " TEXT," +
                    KEY_YEAR + " REAL," +
                    KEY_MONTH + " REAL," +
                    KEY_DAY + " REAL" + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    TransactionDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    boolean addTransactions(Transaction trans) {
        SQLiteDatabase db = this.getWritableDatabase();

        Helper helper = new Helper();

        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE, trans.getMessage());
        values.put(KEY_TIMESTAMPS, trans.getTimestamp());
        values.put(KEY_AMOUNT, trans.getAmount());
        values.put(KEY_GENERAL, trans.getGeneral());
        values.put(KEY_LOCATION, trans.getLocation());
        values.put(KEY_CATEGORY, trans.getCategory());
        values.put(KEY_YEAR, helper.getYear(trans.getDate()));
        values.put(KEY_MONTH, helper.getMonth(trans.getDate()));
        values.put(KEY_DAY, helper.getDay(trans.getDate()));

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close();
        return true;
    }

    public void updateTransaction(Transaction trans) {
        SQLiteDatabase db = this.getWritableDatabase();

        Helper helper = new Helper();

        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE, trans.getMessage());
        values.put(KEY_TIMESTAMPS, trans.getTimestamp());
        values.put(KEY_AMOUNT, trans.getAmount());
        values.put(KEY_GENERAL, trans.getGeneral());
        values.put(KEY_LOCATION, trans.getLocation());
        values.put(KEY_CATEGORY, trans.getCategory());
        values.put(KEY_YEAR, helper.getYear(trans.getDate()));
        values.put(KEY_MONTH, helper.getMonth(trans.getDate()));
        values.put(KEY_DAY, helper.getDay(trans.getDate()));
        db.update(TABLE_NAME, values,  KEY_ID+"="+ trans.getId(), null);
    }


    void deleteTransactions(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = " + id, null);
        db.close();
    }

    List<Transaction> getAllData() {
        List<Transaction> transList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            Transaction trans = getTransaction(cursor);
            transList.add(trans);
            cursor.moveToNext();
        }

        cursor.close();

        return transList;
    }

    Transaction getTransByID(long id) {
        Transaction trans = new Transaction();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_ID + "='" + id + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){

            trans = getTransaction(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return trans;
    }

    List<Transaction> getTransByTag(String tag) {
        List<Transaction> transList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String s = cursor.getString(cursor.getColumnIndex(KEY_MESSAGE));
            Helper helper = new Helper();
            List<String> tagStrList = (helper.tagListToString(helper.parseTag(s)));
            if (tagStrList.contains(tag)) {
                Transaction trans = getTransaction(cursor);
                transList.add(trans);
            }
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return transList;
    }

    private Transaction getTransaction(Cursor cursor) {
        Transaction trans = new Transaction();

        trans.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
        trans.setMessage(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
        trans.setTags(new Helper().parseTag(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE))));
        trans.setGeneral(cursor.getString(cursor.getColumnIndex(KEY_GENERAL)));
        trans.setLocation(cursor.getString(cursor.getColumnIndex(KEY_LOCATION)));
        trans.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
        trans.setTimestamp(cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMPS)));
        trans.setAmount(cursor.getFloat(cursor.getColumnIndex(KEY_AMOUNT)));

        return trans;
    }


}
