package com.example.tianc.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {
    protected static final String ACTIVITY_NAME = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME,"In onCreate()");
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(StartActivity.this, ListItemsActivity.class),50);
            }
        });
        Button startChatBtn = findViewById(R.id.startChat);
        startChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                startActivity(new Intent(StartActivity.this, ChatWindow.class));


            }
        });

        Button weatherForecastBtn = findViewById(R.id.weather_forecast);
        weatherForecastBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Forecast");
                startActivity(new Intent(StartActivity.this, WeatherForecast.class));

            }
        });

        Button toolbar = findViewById(R.id.toolbar_button);
        toolbar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User Clicked Test Toolbar");
                startActivity(new Intent(StartActivity.this, TestToolbar.class));

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50 && resultCode == Activity.RESULT_OK) {
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
            String stringPassed = data.getStringExtra("Response");
            Toast toast = Toast.makeText(this,stringPassed, Toast.LENGTH_LONG);
            toast.show();
        }
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
}
