package com.example.lenovo.chatapp;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.security.PublicKey;
import java.util.Date;

/**
 * Created by LENOVO on 10/16/2015.
 */
@ParseClassName("Message")
public class Message extends ParseObject {
    public String message;
    public ParseFile getEMessage() {return getParseFile("EMessage");}

    public String getId() {
        return getString("objectId");
    }

    public String getSender() {
        return getString("sender");
    }

    public String getReceiver() {
        return getString("receiver");
    }

    public String getMessage() {
        return getString("message");
    }

    public Date getTime() {
        return getUpdatedAt();
    }

    public void setEMessage(ParseFile eMessage) {
        put("EMessage", eMessage);
    }

    public void setId(String id) {
        put("objectId", id);
    }

    public void setSender(String sender) {
        put("sender", sender);
    }

    public void setReceiver(String receiver) {
        put("receiver", receiver);
    }

    public void setMessage(String message) {
        put("message", message);
    }

    public void setTime(String time) {
        put("time", time);
    }
}
