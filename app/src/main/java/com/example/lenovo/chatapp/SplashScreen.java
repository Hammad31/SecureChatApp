package com.example.lenovo.chatapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "7vSyv24nR2necel9ZHAQhWHkYkRhqTcEZ7jIzBHM", "XoFRVbEFvjAlElvdoWJMdEu8TziEb5JXcLulpwti");
        ParseUser.enableRevocableSessionInBackground();
        ParseObject.registerSubclass(Message.class);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}
