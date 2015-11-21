package com.example.lenovo.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button loginButton, CreateAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String user_name = sharedPreferences.getString("username", "NAN");
        String pass_word = sharedPreferences.getString("password", "NAN");

        if (user_name.equals("NAN") || pass_word.equals("NAN")) {

        } else {
            Intent i = new Intent(getApplicationContext(), HomeAcivity.class);
            i.putExtra("username", user_name);
            i.putExtra("password", pass_word);
            startActivity(i);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateAccountButton = (Button)findViewById(R.id.createButton);
        CreateAccountButton.setOnClickListener(this);

        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);


    /*
        try {
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            List<PrivateKey> list = db.getAllKeys();
            for (int i = 0; i < list.size(); i++) {
                System.out.print("Key " + list.get(i).key.length);
                System.out.println(", Username: " + list.get(i).username);
            }
        }catch (Exception e){
            System.out.println("Unable to find the database, " + e.getMessage());
        }

       */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.createButton:
                startActivity(new Intent(MainActivity.this, Create_Account.class));
                break;
        }
    }
}
