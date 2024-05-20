package com.example.user.loco;

import android.content.Context;

import java.util.ArrayList;


public class User {


    public static String username;
    public static String password;
    public static String name;
    public static String lastname;
    public static String age;
    public static String adress;

    public static ArrayList<String> UserArr;

    public User(Context context) {
        TinyDB tdb = new TinyDB(context);
        username = tdb.getString("TheUserName");
        password = tdb.getString("TheUserPass");
        name = tdb.getString("TheName");
        lastname = tdb.getString("TheUserLast");
        age = tdb.getString("TheUserAge");
        adress = tdb.getString("TheUserAdress");

    }


    public static void getInfo()//need the server to catch up
    {
        UserArr = new ArrayList<String>();
        UserArr.add(TabFragment1.currentChatName);

        String info;

        info = Communication.pattern(UserArr, 501);

        if (MainActivity.mTcpClient != null) {

            MainActivity.mTcpClient.sendMessage(info);
        }
    }

    public static void deleteUser()//deleting a user from friends
    {
        UserArr = new ArrayList<String>();
        new User(MainActivity.cpn);
        UserArr.add(TabFragment1.currentChatName);
        UserArr.add(username);
        UserArr.add("1");
        String delName;
        delName = Communication.pattern(UserArr, 601);


        //sends the message to the server
        if (MainActivity.mTcpClient != null) {

            MainActivity.mTcpClient.sendMessage(delName);
        }
    }

    public static void reportUser()//report a user
    {
        UserArr = new ArrayList<String>();
        UserArr.add(TabFragment1.currentChatName);
        new User(MainActivity.cpn);
        UserArr.add(User.username);


        String Reported;

        Reported = Communication.pattern(UserArr, 999);

        if (MainActivity.mTcpClient != null) {

            MainActivity.mTcpClient.sendMessage(Reported);
        }
    }


}
