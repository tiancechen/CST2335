package com.example.tianc.androidlabs.sampledata;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "Message.db";
    private static int VERSION_NUM = 3;

    public static final String TABLE_NAME = "entry";
    public static final String KEY_ID = "id";
    public static final String KEY_MESSAGE = "message";
    private String TAG_SQL = ChatDatabaseHelper.class.getSimpleName();

    private SQLiteDatabase database;


    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_MESSAGE + " TEXT" +
            ")";

    public ChatDatabaseHelper (Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_TABLE);
        Log.i(TAG_SQL, "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.i(TAG_SQL, "Calling onUpgrade, oldVersion = " + oldVersion + " newVersion = " +newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(SQL_CREATE_TABLE);
        Log.i(TAG_SQL,"Calling onDowngrade, oldVersion=" + oldVer + "newVersion=" +newVer);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.i("Database ", "onOpen was called");
    }

    public void openDatabase() {
        database = this.getWritableDatabase();
    }


    public void insertEntry(String content) {
        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE, content);
        database.insert(TABLE_NAME, null, values);

    }

    public void closeDatabase() {
        if(database != null && database.isOpen()){
            database.close();
        }
    }



    public Cursor getRecords() {
        return database.query(TABLE_NAME, null, null, null, null, null, null);
    }


}
