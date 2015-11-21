package com.example.lenovo.chatapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import Security.Final_RSA;
import Utill.ProgressGenerator;

public class Create_Account extends AppCompatActivity implements ProgressGenerator.OnCompleteListener {
    private EditText txtEmail, txtPassword, txtUsername;
    private ActionProcessButton btnCreateAccount;
    private ProgressGenerator progressGenerator;
    private Button uploadImage;
    private Bitmap userimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtEmail = (EditText) findViewById(R.id.userEmail);
        txtPassword = (EditText) findViewById(R.id.userPassword);
        txtUsername = (EditText) findViewById(R.id.userNameAccount);

        uploadImage = (Button) findViewById(R.id.upload_image);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), StaticData.GET_FROM_GALLERY);
            }


        });

        progressGenerator = new ProgressGenerator(this);
        btnCreateAccount = (ActionProcessButton) findViewById(R.id.creatAccount);
        btnCreateAccount.setMode(ActionProcessButton.Mode.PROGRESS);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    private void createAccount() {
        final String email = txtEmail.getText().toString();
        final String userName = txtUsername.getText().toString();
        final String password = txtPassword.getText().toString();
        if (email.equals("") || userName.equals("") || password.equals("")) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(Create_Account.this);
            dialog.setTitle("Empty Fields");
            dialog.setMessage("Please fill all fields");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            final ParseUser user = new ParseUser();
            user.setUsername(userName);
            user.setEmail(email);
            user.setPassword(password);
            if (userimage != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // image quality 1 - 100
                userimage.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] imageBytes = stream.toByteArray();

                ParseFile imageFile = new ParseFile(userName + "_image", imageBytes);
                try {
                    imageFile.save();
                    user.put("image", imageFile);
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(), "Your Image did not uploaded.!", Toast.LENGTH_SHORT).show();
                }
            }
            try {
                Final_RSA rsa = new Final_RSA();
                ParseFile pf = new ParseFile("PKey.txt", rsa.publicKeyBytes);
                pf.save();
                user.put("PKey", pf);
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                PrivateKey privateKey = new PrivateKey(rsa.privateKeyBytes, userName);
                db.addPrivateKey(privateKey);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(), "Doing it", Toast.LENGTH_SHORT).show();
                        progressGenerator.start(btnCreateAccount);
                        btnCreateAccount.setEnabled(false);
                        txtEmail.setEnabled(false);
                        txtPassword.setEnabled(false);
                        txtUsername.setEnabled(false);

                        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", userName);
                        editor.putString("password", password);
                        editor.commit();
                        logUserIn(userName, password);
                    } else {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == StaticData.GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                userimage = null;
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                userimage = bitmap;
                ImageView mImg;
                mImg = (ImageView) findViewById(R.id.userImage);
                mImg.setImageBitmap(userimage);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void logUserIn(String userName, String password) {

        if (!userName.equals("") || !password.equals("")) {
            ParseUser.logInInBackground(userName, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (e == null) {
                        StaticData.username = parseUser.getUsername();
                    } else {
                    }
                }
            });
        }
    }

    @Override
    public void onComplete() {
        startActivity(new Intent(Create_Account.this, HomeAcivity.class));
    }
}
