package com.example.tianc.androidlabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends Activity {
    protected static final String ACTIVITY_NAME = "ListItemsActivity";
    protected static final int REQUEST_IMAGE_CAPTURE = 1;

    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        Log.i(ACTIVITY_NAME,"In onCreate()");
        imageButton = findViewById(R.id.ibtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        Switch sw1 = findViewById(R.id.sw);
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CharSequence text;
                int duration;
                if (isChecked) {
                    text = getString(R.string.swOn);
                    duration = Toast.LENGTH_SHORT;
                }else {
                    text = getString(R.string.swOff);
                    duration = Toast.LENGTH_LONG;
                }
                Toast toast = Toast.makeText(ListItemsActivity.this,text,duration);
                toast.show();
            }
        });
        CheckBox cb = findViewById(R.id.cb);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
                    builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("Response", getString(R.string.response));
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog

                        }
                    }).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(ACTIVITY_NAME,"In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(ACTIVITY_NAME,"In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(ACTIVITY_NAME,"In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i(ACTIVITY_NAME,"In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) throws NullPointerException{
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageButton.setImageBitmap(imageBitmap);
        }
    }
}
