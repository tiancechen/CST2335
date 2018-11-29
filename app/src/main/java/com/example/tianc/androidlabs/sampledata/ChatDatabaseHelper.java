package com.example.tianc.androidlabs.sampledata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tianc.androidlabs.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "ChatDatabaseHelper";
    private static final String DATABASE_NAME = "Messages.db";
    private static int VERSION_NUM = 2;
    public static String TABLE_NAME = "MessageList";
    public static String KEY_ID = "_id", KEY_MESSAGE = "message";
    public Cursor c;


    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Calling onCreate");
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGE  + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Calling onUpgrade, oldVersion = " + oldVersion +"newVersion = " +newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);


    }
    public List<Message> getAllMessages(){
        final List<Message> list = new ArrayList<>();
        final SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_NAME, null,null,null,null,null,null,null);
        final int cIdIndex = cursor.getColumnIndex(KEY_ID);
        final int cMessageIndex = cursor.getColumnIndex(KEY_MESSAGE);
        Log.i(TAG, "Cursor's column count = " + cursor.getColumnCount());
        Log.i(TAG,"Cursor column1: " + cursor.getColumnName(cIdIndex));
        Log.i(TAG, "Cursor Column2" + cursor.getColumnName(cMessageIndex));
        for (int i =0; i<cursor.getColumnCount();i++){
            Log.i(TAG,"Column name = " + cursor.getColumnName(i));
        }
        while (cursor.moveToNext()) {
            Log.i(TAG, "SQL MESSAGE: " + getMessageFrom(cursor).getMessage());
            list.add(getMessageFrom(cursor));
        }
        return list;
    }

    public Message insertMessage(String msg) {
        final ContentValues cValue= new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        cValue.put(KEY_MESSAGE,msg);
        long searchId = db.insert(TABLE_NAME,null,cValue);
        if(searchId>0){
            Log.i(TAG,"insert succeed!");

        }else
            Log.i(TAG,"insert failed");
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID +  "= ?", new String[] { String.valueOf(searchId) });
        return cursor.moveToNext()? getMessageFrom(cursor):null;

    }

    private Message getMessageFrom(Cursor cursor) {
        final int columnIdIndex = cursor.getColumnIndex(KEY_ID);
        final int columnMessageIndex = cursor.getColumnIndex(KEY_MESSAGE);

        long id = cursor.getLong(columnIdIndex);
        String txt = cursor.getString(columnMessageIndex);

        final Message msg = new Message(id, txt);
        Log.i(TAG, "SQL message text: " + txt);

        return msg;
    }



}
