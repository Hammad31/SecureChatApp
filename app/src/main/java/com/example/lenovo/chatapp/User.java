package com.example.lenovo.chatapp;

import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName("User")
public class User extends ParseObject {
    public String getUsername(){
        return getString("usernmae");
    }

    public String getUserPassword(){
        return getString("password");
    }

    public String getUserEmail(){
        return getString("email");
    }

    public String getUserPublicKey(){
        return getString("PublicKey");
    }


}
