package com.example.lenovo.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private Button signInButton;
    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = (Button) findViewById(R.id.signInId);
        userName = (EditText) findViewById(R.id.usernameId);
        password = (EditText) findViewById(R.id.passwordId);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uName = userName.getText().toString();
                final String pWord = password.getText().toString();

                if (!uName.equals("") || !pWord.equals("")) {
                    ParseUser.logInInBackground(uName, pWord, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (e == null) {

                                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", uName);
                                editor.putString("password", pWord);
                                editor.commit();

                                startActivity(new Intent(LoginActivity.this, HomeAcivity.class));

                            }else {

                                Toast.makeText(getApplicationContext(), "Please try again",
                                        Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(), "Please enter username and Password",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}