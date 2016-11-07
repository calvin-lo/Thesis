package com.uoit.calvin.thesis2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TagDBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tagCloudDB";
    private static final String TABLE_NAME = "tagCloud";

    private static  final String KEY_ID = "id";
    private static final String KEY_TAG = "tag";

    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_TAG + TEXT_TYPE + "UNIQUE" + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    TagDBHelper(Context context) {
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

    boolean addTag(Tag tag) {

        if (!checkDuplicate(tag)) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_TAG, tag.toString());

            // Inserting Row
            db.insert(TABLE_NAME, null, values);
            db.close();
        }
        return true;
    }

    private boolean checkDuplicate(Tag tag) {
        return (new ArrayList<>(Arrays.asList(getTagsStringList()))).contains(tag.toString());
    }

    boolean deleteTag(String tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_TAG + " = ?", new String[] {tag});
        db.close();
        return true;
    }


    Tag[] getTagsList() {
        List<Tag> tagList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            Tag tag = new Tag(cursor.getString(cursor.getColumnIndex(KEY_TAG)));
            tagList.add(tag);
            cursor.moveToNext();
        }
        cursor.close();
        return tagList.toArray(new Tag[tagList.size()]);
    }

    String[] getTagsStringList() {
        List<String> tagList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            String tag = cursor.getString(cursor.getColumnIndex(KEY_TAG));
            tagList.add(tag);
            cursor.moveToNext();
        }
        cursor.close();
        return tagList.toArray(new String[tagList.size()]);
    }

}