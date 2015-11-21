package com.example.lenovo.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

import Utill.ProgressGenerator;

public class ChatActivity extends AppCompatActivity implements Serializable {
    private EditText message;
    private Button sendMessageButton;
    private static final int MAX_CHAT_MSG_TO_SHOW = 20;
    private ListView listView;
    ArrayList<Message> messages;
    ArrayList<String> stringMessage;
    ArrayList<String> messagesID;
    MessageAdapter messageAdapter;
    private android.os.Handler handler = new android.os.Handler();
    private String currentUserName;
    private String currentFriendName;
    private byte[] friendPublicKeyByte;
    PublicKey friendPublicKey;
    java.security.PrivateKey myPrivateKey;
    private byte[] myPrivateKeyByte;
    KeyFactory keyFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        getCurrentUser();

        Intent i = getIntent();
        // getting attached intent data
        currentFriendName = i.getStringExtra("friendId");
        setTitle(currentFriendName);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", currentFriendName);
        try {
            friendPublicKeyByte = query.find().get(0).getParseFile("PKey").getData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        List<PrivateKey> list = db.getAllKeys();
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j).username.equals(ParseUser.getCurrentUser().getUsername())) {
                myPrivateKeyByte = list.get(j).key;
                break;
            }
        }

        try {
            keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pri = new PKCS8EncodedKeySpec(myPrivateKeyByte);
            myPrivateKey = keyFactory.generatePrivate(pri);

            X509EncodedKeySpec pub = new X509EncodedKeySpec(friendPublicKeyByte);
            friendPublicKey = keyFactory.generatePublic(pub);

        } catch (NullPointerException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            Toast.makeText(getApplicationContext(), "Can not get keys" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        messagePosting();

        handler.postDelayed(runnable, 1000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            handler.postDelayed(this, 1000);
        }
    };

    private void getCurrentUser() {
        currentUserName = ParseUser.getCurrentUser().getUsername();

    }

    private void messagePosting() {
        message = (EditText) findViewById(R.id.etMessage);
        messagesID = new ArrayList<>();
        sendMessageButton = (Button) findViewById(R.id.buttonSend);
        listView = (ListView) findViewById(R.id.listview_chat);
        messages = new ArrayList<Message>();
        stringMessage = new ArrayList<>();
        if (myPrivateKey == null)
            Toast.makeText(getApplicationContext(), "Key is null, way?", Toast.LENGTH_SHORT).show();
        messageAdapter = new MessageAdapter(ChatActivity.this, currentUserName, stringMessage, myPrivateKey);
        listView.setAdapter(messageAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* Message m = messages.get(position);
                messages.remove(position);
                m.deleteInBackground();*/
            }
        });

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!message.getText().toString().equals("")) {
                    Message msg = new Message();
                    msg.setSender(currentUserName);
                    msg.setReceiver(currentFriendName);
                    try {
                        Cipher c = Cipher.getInstance("RSA");
                        c.init(Cipher.ENCRYPT_MODE, friendPublicKey);

                        String myMessage = new String(message.getText().toString());
                        SealedObject myEncyptedMessage = new SealedObject(myMessage, c);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutput out = null;
                        out = new ObjectOutputStream(bos);
                        out.writeObject(myEncyptedMessage);
                        byte[] eMessageBytes = bos.toByteArray();
                        ParseFile pf = new ParseFile("message.txt", eMessageBytes);
                        pf.save();
                        bos.close();
                        msg.setEMessage(pf);

                    } catch (ParseException | InvalidKeyException | NoSuchAlgorithmException | IOException | IllegalBlockSizeException | NoSuchPaddingException e) {
                        Toast.makeText(getApplicationContext(), "Message did not got Encripted", Toast.LENGTH_LONG).show();
                    }

                    try {
                        msg.save();
                        message.setText("");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Empty message!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void receiveMessage() {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.whereEqualTo("sender", currentFriendName);
        query.whereEqualTo("receiver", currentUserName);
        query.addAscendingOrder("createdAt");
        try {
            List<Message> messagesList = query.find();
            for (int i = 0; i < messagesList.size(); i++) {
                if (!messagesID.contains(messagesList.get(i).getObjectId())) {
                    try {
                        byte[] msg = messagesList.get(i).getEMessage().getData();
                        ByteArrayInputStream bis = new ByteArrayInputStream(msg);
                        ObjectInputStream ois = new ObjectInputStream(bis);
                        SealedObject myEncryptedMessage = (SealedObject) ois.readObject();
                        Cipher dec = Cipher.getInstance("RSA");
                        dec.init(Cipher.DECRYPT_MODE, myPrivateKey);
                        String ms = (String) myEncryptedMessage.getObject(dec);
                        messagesList.get(i).message = ms;
                        stringMessage.add(messagesList.get(i).message);
                        messagesID.add(messagesList.get(i).getObjectId());
                        System.out.println("A new message added: " + ms);
                        System.out.println("Message counter: " + i);
                        messageAdapter.notifyDataSetChanged();
                    } catch (BadPaddingException | IllegalBlockSizeException | ClassNotFoundException | ParseException | IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e1) {
                        Toast.makeText(getApplicationContext(), "Can not read : " + e1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (ParseException e2) {
            Toast.makeText(getApplicationContext(), "Can not read massages: " + e2.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void refreshMessages() {
        receiveMessage();
    }

}
