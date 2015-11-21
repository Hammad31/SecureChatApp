package Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by LENOVO on 10/15/2015.
 */
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
