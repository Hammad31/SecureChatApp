package com.example.lenovo.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import Model.User;

public class HomeAcivity extends AppCompatActivity {
    private ListView listView;
    ArrayList<ParseUser> users;
    UserAdapter userAdprer;
    private android.os.Handler handler = new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_acivity);
        listView = (ListView) findViewById(R.id.ListviewUsers);
        users = new ArrayList<>();

        userAdprer = new UserAdapter(HomeAcivity.this, users);
        listView.setAdapter(userAdprer);
        refressUsers();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                i.putExtra("friendId", users.get(position).getUsername());
                startActivity(i);
            }
        });


    }

    public void refressUsers() {
        UsersListing();
    }

    private void UsersListing() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> usersList, ParseException e) {
                for (int i = 0; i < usersList.size(); i++) {
                    if (e == null) {
                        if (!users.contains(usersList.get(i))) {
                            users.add(usersList.get(i));
                            userAdprer.notifyDataSetChanged();
                            listView.invalidate();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_home_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refressUsers();
                return true;
            case R.id.action_logout:
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.commit();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onBackPressed() {
    }

}
