package com.example.user.loco;


import android.app.ActivityManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class AddFriend extends ActionBarActivity {


    public static ArrayList<String> FriendSend = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Add A Friend");
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(20);

        TinyDB tinydb = new TinyDB(this);

        toolbar.setBackgroundColor(getResources().getColor(R.color.appBarC));
        int accent = tinydb.getInt("colorAc");
        toolbar.setTitleTextColor(getResources().getColor(accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final Button sendfriendbtn = (Button) findViewById(R.id.addFriendbtn);//the add button
        EditText friend = (EditText) findViewById(R.id.FriendName);//the entered friend name

        sendfriendbtn.setBackgroundColor(getResources().getColor(accent));
        friend.setTextColor(getResources().getColor(accent));
        friend.setHintTextColor(getResources().getColor(accent));


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.appc));
            friend.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.appBarC));
            int color = getResources().getColor(R.color.appBarC);

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, bm, color);

            setTaskDescription(td);
            bm.recycle();
        }


        friend.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {//a listener for the add friend button
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendfriendbtn.performClick();//clicking the button when the user clicks the im done button in keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(sendfriendbtn.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AddFriendC(View v) {
        String name, send;
        EditText fname = (EditText) findViewById(R.id.FriendName);// the name of the friend which the user wants to add

        new User(this);
        name = fname.getText().toString();//converting the name to string

        if (!User.username.equals(name)) {
            FriendSend.add(name);
            FriendSend.add(User.username);//sending the user name

            send = Communication.pattern(FriendSend, 600);


            //sends the message to the server
            if (MainActivity.mTcpClient != null) {

                MainActivity.mTcpClient.sendMessage(send);
                Toast.makeText(this, "Friend Request Was Sent", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "You Can't Be Friend With Yourself", Toast.LENGTH_SHORT).show();
        }

        FriendSend.clear();
        finish();

    }

}
