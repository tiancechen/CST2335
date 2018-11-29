package com.example.tianc.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    protected static final String ACTIVITY_NAME = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME,"In onCreate()");
        Button loginButton = findViewById(R.id.loginButton);
        final EditText loginEmail = findViewById(R.id.login_name);
        final SharedPreferences prefs = getSharedPreferences("preferredEmail", Context.MODE_PRIVATE);
        loginEmail.setText(prefs.getString("DefaultEmail","email@domain.com"));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("DefaultEmail",loginEmail.getText().toString());
                edit.apply();

                Intent intent = new Intent(LoginActivity.this,StartActivity.class);
                startActivity(intent);
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
}