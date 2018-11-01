package com.example.tianc.androidlabs;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tianc.androidlabs.sampledata.ChatDatabaseHelper;

import java.util.ArrayList;


public class ChatWindow extends Activity {
    protected static final String ACTIVITY_NAME = "ChatWindow";
    ListView chatView;
    Button buttonChat;
    EditText editText;
    ArrayList<String> messages;
    ChatDatabaseHelper dbManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        /* lab4 */
        chatView = findViewById(R.id.chatView);
        buttonChat = findViewById(R.id.button4);
        editText = findViewById(R.id.editText3);
        messages = new ArrayList<>();

        final ChatAdapter messageAdapter;//this means chatWindow
        messageAdapter = new ChatAdapter(this);
        chatView.setAdapter(messageAdapter);

        dbManager = new ChatDatabaseHelper(this);
        final SQLiteDatabase db = dbManager.getWritableDatabase();

        String selectQurey = "select * FROM " + dbManager.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQurey,null);
        while (cursor.moveToNext())

        {
            String newMessage = cursor.getString(cursor.getColumnIndex(dbManager.KEY_MESSAGE));
            messages.add(newMessage);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + newMessage);
        }

        Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount() );


        for (int columnIndex = 0; columnIndex < cursor.getColumnCount(); columnIndex++){
            Log.i(ACTIVITY_NAME, "Column Name:" + cursor.getColumnName(columnIndex));
        }

        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = editText.getText().toString();
                messages.add(data);
                //Insert the new message into the database, contentValues object will put the new message
                ContentValues contentValues = new ContentValues();
                contentValues.put(dbManager.KEY_MESSAGE,editText.getText().toString());

                long insertCheck = db.insert(dbManager.TABLE_NAME,null,contentValues);
                Log.i(ACTIVITY_NAME, "insert data result: " + insertCheck);
                messageAdapter.notifyDataSetChanged();
                editText.setText("");

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }



    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public String getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position){
            return super.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if(position % 2 == 0) result = inflater.inflate(R.layout.chat_rot_incoming,null);
            else result = inflater.inflate(R.layout.chat_row_outgoing,null);

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }

    }
}