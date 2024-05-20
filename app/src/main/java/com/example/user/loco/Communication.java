package com.example.user.loco;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.RemoteInput;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.MemoryCache;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;



public class Communication {


    public static ArrayList<String> dipattern = new ArrayList<String>();//used to contain the entered message form the server
    public static ArrayList<String> forconfirm;//used to send data back to the server
    public static ArrayList<String> friendsNames;
    public static ArrayList<String> GroupsNames;
    public static String KEY_TEXT_REPLY;
    public static String TheData;
    public static int ProgBarProg=0;
    public static ProgressDialog dialog;




    public static void ManageData(String data, Context Mcontext) {

        TheData = data;
        TinyDB tinydb = new TinyDB(Mcontext);
        final Vibrator vibe = (Vibrator) Mcontext.getSystemService(Context.VIBRATOR_SERVICE);
        dispatternFunc(data, dipattern);
        String Code="4343@*";
        try {
            Code = dipattern.get(0);
        }catch (Exception a){}


        switch (Code) {
            case "1000":
                //reciving image
                if (dipattern.size()>1) {
                    try {
                        int index;
                        boolean prof;
                        index=2;
                        prof=false;
                        if (dipattern.get(1).equals(User.username)) {prof=true;}
                        String imgg = dipattern.get(index);
                        new User(Mcontext);
                        byte[] b = Base64.decode(imgg, Base64.DEFAULT);
                        if (b!=null) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                                       options.inSampleSize = 2;
                            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length,options);
                            btmapToF(bitmap,prof);

                            if (MainActivity.active) {
                                if (MainActivity.active && TabFragmment2.fList != null) {
                                    TabFragmment2.fAdapter.notifyDataSetInvalidated();
                                }

                                if (MainActivity.active && TabFragment1.mList != null) {
                                    TabFragment1.mAdapter.notifyDataSetInvalidated();
                                }
                            }
                        }
                    }catch (Exception a){}
                }

                ArrayList arr = tinydb.getListString("myFriends");
                int size = arr.size();
                int Progress = tinydb.getInt("prog");

                if (size!=0)
                {
                    Progress++;
                    float prec =((float)Progress/size)*100;
                    int precI = (int)prec;
                    PicRetrieve.changeProg(precI);
                    if (precI>=100&&PicRetrieve.picAlive)
                    {
                        tinydb.putInt("prog",0);
                        ((PicRetrieve)PicRetrieve.conPic).finish();
                    }
                    else
                    {
                        tinydb.putInt("prog",Progress);
                    }

                }

                break;
            case "1001":
                dialog.dismiss();
                Toast.makeText(MainActivity.cpn, "Your Picture Was Updated", Toast.LENGTH_SHORT).show();
                break;

            case "99":
                if (Login.errotMessage != null) {
                    Login.errorLogIn(Login.errotMessage,"This User Is Already Loged In");
                    Login.EndSpin();
                }
                break;
            case "111":
                okFunc();
                String p,u;
                u = tinydb.getString("TheUserName");
                p= tinydb.getString("TheUserPass");

                tinydb.putString("Userklmn",u);
                tinydb.putString("Passklmn",p);
                break;
            case "112":
                tinydb.putBoolean("isLoged", false);
                Intent jp = new Intent(Mcontext.getApplicationContext(), MainActivity.class);
                ((MainActivity)MainActivity.cpn).finish();
                MainActivity.cpn.startActivity(jp);
                break;

            case "101":
                Toast.makeText(MainActivity.cpn, "Register was successful", Toast.LENGTH_SHORT).show();
                break;

            case "102":
                String recPass="";
                try {
                    recPass = dipattern.get(1);
                }catch (Exception a){}

                RecoveryResult.PassRecovery = recPass;

                if (Login.LogCpn != null) {
                    Intent a = new Intent(Login.LogCpn, RecoveryResult.class);
                    Login.LogCpn.startActivity(a);
                }


                break;

            case "103":
                if (Login.errotMessage != null) {
                    Login.errorLogIn(Login.errotMessage,"User Name Or Password Are Incorrect");
                    Login.EndSpin();
                } else {
                    tinydb.putBoolean("isLoged", false);
                    Refresh();
                }
                break;

            case "104":
                Toast.makeText(MainActivity.cpn, "Register failed", Toast.LENGTH_SHORT).show();

                break;

            case "105":
                Toast.makeText(MainActivity.cpn, "Info Recovery failed", Toast.LENGTH_SHORT).show();
                break;

            case "106":
                tinydb.clear();//its in a comment just for the testing
                tinydb.putBoolean("isLoged", false);

                int colrI = R.color.appBarC;
                int colorf = R.color.appc;
                int accent = R.color.LoCoOrange;
                tinydb.putInt("colorAc", accent);
                tinydb.putInt("colorInt", colrI);
                tinydb.putInt("colorF", colorf);

                if (Settings.SettingCpn != null) {
                    Intent daa = new Intent(Settings.SettingCpn, Login.class);
                    Settings.SettingCpn.startActivity(daa);
                }

                break;

            case "107":
                Toast.makeText(MainActivity.cpn, "Couldn't delete your account please try again later", Toast.LENGTH_SHORT).show();
                break;

            case "108":
                DBusers(dipattern, 1);

                if (MainActivity.active && TabFragmment2.fList != null) {
                    TabFragmment2.fAdapter.notifyDataSetInvalidated();
                }
                break;

            case "109":
                int kq;//need to checl
                ArrayList<String> fro=new ArrayList<String>();
                boolean ggMate = false;
                for (kq=0;kq<dipattern.size();kq++)
                {
                    if (dipattern.get(kq).equals("|"))
                    {
                        tinydb.putInt("TheSep",kq-1);
                        ggMate =true;
                    }
                    else {
                        fro.add(dipattern.get(kq));
                        if (ggMate)
                        {
                            tinydb.putBoolean(dipattern.get(kq)+"TG",true);
                        }
                    }
                }
                //need to check
                DBusers(fro, 0);
                if (MainActivity.active && TabFragment1.mList != null) {
                    TabFragment1.mAdapter.notifyDataSetInvalidated();
                }
                break;

            case "110":
                DBusers(dipattern, 2);
                if (MainActivity.active && TabFragment3.rList != null) //need to check why tab fragment 3 isnt created
                {
                    TabFragment3.rAdapter.notifyDataSetChanged();
                }
                break;


            case "200":
                String idt, doneConId;
                boolean typeB;
                boolean goOn;
                int idInt;
                String temp;
                String gUn;

                if (dipattern.size()>5)
                {
                    String type = dipattern.get(4);

                    if (type.equals("c"))//checking if the message is a group or a friend message
                    {
                        goOn = DoesFriendEx(dipattern.get(1));
                        typeB = false;
                        temp = dipattern.get(5);//the message content
                        gUn = "1";
                    } else {
                        goOn = DoesGroupEx(dipattern.get(1));
                        typeB = true;
                        int i, toStart = 0;
                        String tempGUN = "";
                        String tempMMM = "";
                        String toBreak = dipattern.get(5);

                        for (i = 0; i < toBreak.length(); i++) {
                            if (toBreak.charAt(i) != ',') {
                                tempGUN += toBreak.charAt(i);
                            } else {
                                toStart = i + 1;
                                break;
                            }
                        }

                        for (i = toStart; i < toBreak.length(); i++) {
                            tempMMM += toBreak.charAt(i);
                        }

                        temp = tempMMM;
                        gUn = tempGUN;
                    }
                    if (goOn)
                    {
                        if (!In_Chat.InChatActive)
                        {
                            int forUNcount = tinydb.getInt(dipattern.get(1)+"u");
                            forUNcount++;
                            tinydb.putInt(dipattern.get(1)+"u",forUNcount);
                        }


                        ChatCustomAdapter.AllChatData = tinydb.getListString(dipattern.get(1));
                        ChatCustomAdapter.AllChatDirection = tinydb.getListInt(dipattern.get(1) + "d");
                        ChatCustomAdapter.VArray = tinydb.getListInt(dipattern.get(1) + "v");
                        ChatCustomAdapter.TimeArray = tinydb.getListString(dipattern.get(1) + "t");
                        ChatCustomAdapter.MessageIdChat = tinydb.getListString(dipattern.get(1) + "i");
                        ChatCustomAdapter.GroupMessUser = tinydb.getListString(dipattern.get(1) + "g");

                        if (In_Chat.mAdapter2 != null&&TabFragment1.currentChatName.equals(dipattern.get(1))) {
                            In_Chat.mAdapter2.addMessage(temp, 0, 0, dipattern.get(3), dipattern.get(2), gUn);//if its not the first message

                        } else//if its the first message
                        {

                            ChatCustomAdapter.AllChatData.add(temp);
                            ChatCustomAdapter.AllChatDirection.add(0);
                            ChatCustomAdapter.VArray.add(0);
                            ChatCustomAdapter.TimeArray.add(dipattern.get(3));
                            ChatCustomAdapter.MessageIdChat.add(dipattern.get(2));
                            ChatCustomAdapter.GroupMessUser.add(gUn);
                        }
                        //saving the data
                        tinydb.putListString(dipattern.get(1), ChatCustomAdapter.AllChatData);
                        tinydb.putListInt(dipattern.get(1) + "d", ChatCustomAdapter.AllChatDirection);
                        tinydb.putListInt(dipattern.get(1) + "v", ChatCustomAdapter.VArray);
                        tinydb.putListString(dipattern.get(1) + "t", ChatCustomAdapter.TimeArray);
                        tinydb.putListString(dipattern.get(1) + "i", ChatCustomAdapter.MessageIdChat);
                        tinydb.putListString(dipattern.get(1) + "g", ChatCustomAdapter.GroupMessUser);

                        idt = tinydb.getString(dipattern.get(1) + "idCount");

                        if (idt.equals("")) {
                            idt = "0";
                            doneConId = idt;
                            tinydb.putString(dipattern.get(1) + "idCount", doneConId);
                        } else {
                            idInt = Integer.valueOf(idt);
                            idInt++;

                            doneConId = String.valueOf(idInt);
                            tinydb.putString(dipattern.get(1) + "idCount", doneConId);
                        }


                        //sending a confirmation to the server
                        String confirm;
                        forconfirm = new ArrayList<String>();
                        new User(Mcontext);
                        forconfirm.add(User.username);
                        forconfirm.add(dipattern.get(1));
                        forconfirm.add(dipattern.get(2));
                        confirm = Communication.pattern(forconfirm, 201);

                        //sends the message to the server
                        if (MainActivity.mTcpClient != null) {

                            MainActivity.mTcpClient.sendMessage(confirm);

                            if (In_Chat.InChatActive&&TabFragment1.currentChatName.equals(dipattern.get(1)))
                            {
                                new User(Mcontext);
                                final String imin="209@"+TabFragment1.currentChatName+","+User.username+"*";
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        // this code will be executed after 2 seconds
                                        MainActivity.mTcpClient.sendMessage(imin);
                                    }
                                }, 500);

                            }

                        }

                        String beforeCN= TabFragment1.currentChatName;
                        TabFragment1.currentChatName = dipattern.get(1);
                        String NotStr = dipattern.get(1) + ":" + temp;

                        boolean specific = In_Chat.JustTheStateMute(Mcontext);
                        if (!specific)
                        {
                            if (dipattern.get(1).equals(beforeCN)&&In_Chat.InChatActive) {
                                vibe.vibrate(100);

                            } else {
                                String me;
                                if (typeB)
                                {
                                    me = "From "+gUn+" in " +dipattern.get(1)+": "+ temp;

                                }
                                else {me  = NotStr;}
                                showNotification(In_Chat.class, me, Mcontext);//show a notification
                            }

                        }

                        boolean tg = tinydb.getBoolean(dipattern.get(1)+"TG");
                        if(!typeB||!tg)
                        {
                            MoveChatToTop(dipattern.get(1), Mcontext, typeB);
                        }

                        if (MainActivity.active) {
                            if (!typeB && TabFragmment2.fList != null) {
                                TabFragmment2.fAdapter.notifyDataSetChanged();
                            } else if (typeB && TabFragment1.mList != null) {
                                TabFragment1.mAdapter.notifyDataSetChanged();
                            }

                        }
                    }
                }
                break;


            case "201"://// TODO: 5/6/2016 need to check why its not working well and if the changes helped 
                if (dipattern.size()>2)
                {
                    String vvname = dipattern.get(1);
                    String idvv = dipattern.get(2);
                    int doneIndex2;
                    try
                    {
                        ChatCustomAdapter.VArray = tinydb.getListInt(vvname + "v");
                        doneIndex2 = Integer.valueOf(idvv);

                        if (ChatCustomAdapter.VArray.size()>doneIndex2) {
                            try {
                                ChatCustomAdapter.VArray.set(doneIndex2, 2);
                            }catch (Exception a){}


                            tinydb.putListInt(vvname + "v", ChatCustomAdapter.VArray);
                            if (In_Chat.InChatActive) {
                                In_Chat.mAdapter2.notifyDataSetChanged();
                            }
                        }
                    }catch (Exception a){}
                }
                break;


            case "202":
                if (dipattern.size()>2)
                {
                    String vname = dipattern.get(1);
                    String id = dipattern.get(2);
                    int doneIndex;
                    try
                    {
                        ChatCustomAdapter.VArray = tinydb.getListInt(vname + "v");

                        doneIndex = Integer.valueOf(id);

                        if (ChatCustomAdapter.VArray.size()>doneIndex) {
                            try {
                                ChatCustomAdapter.VArray.set(doneIndex, 1);
                            }catch (Exception v){}


                            tinydb.putListInt(vname + "v", ChatCustomAdapter.VArray);
                            if (In_Chat.InChatActive) {
                                In_Chat.mAdapter2.notifyDataSetChanged();
                            }
                        }
                    }catch (Exception a){}

                }
                break;
            case "209":
                if (dipattern.size()>1)
                {
                    try {
                        int i;
                        ArrayList<Integer> ttype=tinydb.getListInt(dipattern.get(1)+"v");
                        for (i=0;i<ttype.size();i++)
                        {
                            if (ttype.get(i)==2)
                            {
                                ttype.set(i,3);
                            }
                        }
                        tinydb.putListInt(dipattern.get(1)+"v",ttype);
                        if (In_Chat.InChatActive&&TabFragment1.currentChatName.equals(dipattern.get(1)))
                        {
                            ChatCustomAdapter.VArray = ttype;
                            In_Chat.mAdapter2.notifyDataSetChanged();
                        }
                    }catch (Exception a){}
                }

                break;


            case "300":
                //adding a chat group
                try
                {
                    if (dipattern.size()>1)
                    {
                        int i;
                        new User(Mcontext);
                        Toast.makeText(MainActivity.cpn, "Your location was updated", Toast.LENGTH_SHORT).show();

                        MyCustomAdapter.mListItems = tinydb.getListString("myUsers");
                        int seps = tinydb.getInt("TheSep");

                        if (seps==2)
                        {
                            for (i = 0; i < seps; i++) {
                                if (!MyCustomAdapter.mListItems.get(i).equals(User.adress)) {

                                    MyCustomAdapter.mListItems.set(i,dipattern.get(1));
                                }
                            }
                        }

                        else
                        {
                            MyCustomAdapter.mListItems.add(1,dipattern.get(1));
                            tinydb.putInt("TheSep",2);
                        }
                        tinydb.putListString("myUsers", MyCustomAdapter.mListItems);

                        String NotGStr = "You were added to " + dipattern.get(1);
                        TabFragment1.currentChatName =dipattern.get(1);
                        if (!MainActivity.active && !In_Chat.InChatActive) {
                            showNotification(In_Chat.class, NotGStr, Mcontext);//show a notification
                        } else {
                            Toast.makeText(MainActivity.cpn, "You Were Added To A New Group", Toast.LENGTH_SHORT).show();
                            vibe.vibrate(100);
                        }

                        if (MainActivity.active) {
                            TabFragment1.mAdapter.notifyDataSetChanged();
                        }
                    }
                }catch (Exception a){}

                break;

            case "500":
                int kqa;//need to checl
                ArrayList<String> fro1=new ArrayList<String>();
                boolean goOnm=false;
                for (kqa=0;kqa<dipattern.size();kqa++)
                {
                    if (dipattern.get(kqa).equals("|"))
                    {
                        tinydb.putInt("TheSep",kqa-1);
                        goOnm=true;
                    }
                    else {
                        fro1.add(dipattern.get(kqa));
                        if (goOnm)
                        {
                            tinydb.putBoolean(dipattern.get(kqa)+"TG",true);
                        }
                    }

                }
                DBusers(fro1, 0);
                TabFragment1.mAdapter.notifyDataSetInvalidated();
                Toast.makeText(MainActivity.cpn, "Your information was updated", Toast.LENGTH_SHORT).show();
                break;

            case "501":
                MyCustomAdapter.mListItems = tinydb.getListString("myUsers");

                if (dipattern.size() > 5) {
                    if (dipattern.get(5).equals(User.username)) {

                        tinydb.putString("TheName", dipattern.get(1));
                        tinydb.putString("TheUserLast", dipattern.get(2));
                        tinydb.putString("TheUserAge", dipattern.get(3));
                        tinydb.putString("TheUserAdress", dipattern.get(4));

                    } else {
                        String  name, last, age, adress;
                        name = dipattern.get(1);
                        last = dipattern.get(2);
                        age = dipattern.get(3);
                        adress = dipattern.get(4);


                        //starting a dialog for the user
                        final CharSequence[] items = {"First Name: " + name, "Last Name: " + last, "Age: " + age, "Address: " + adress};

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.cpn, R.style.YourAlertDialogTheme);
                        builder.setTitle(TabFragment1.currentChatName);
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                }

                break;


            case "600":
                //adding the request to the request list
                if (dipattern.size()>1)
                {
                    try{
                        RequestCostumAdapter.rListItems = tinydb.getListString("myRequests");
                        int isAlreadyT = RequestCostumAdapter.rListItems.indexOf(dipattern.get(1));
                        if (isAlreadyT==-1) {

                            RequestCostumAdapter.rListItems.add(dipattern.get(1));
                            tinydb.putListString("myRequests", RequestCostumAdapter.rListItems);

                            if (MainActivity.active && TabFragment3.rList != null) //need to check why tab fragment 3 isnt created
                            {
                                TabFragment3.rAdapter.notifyDataSetChanged();
                            }

                            String NotRStr = dipattern.get(1) + " Wants to be your friend!";

                            showNotification(MainActivity.class, NotRStr, Mcontext);//show a notification

                        }
                    }catch (Exception a){}

                }
                break;


            case "601":
                //deleting a friend
                if (dipattern.size()>1) {
                    FriendCustomAdapter.FrListItems = tinydb.getListString("myFriends");
                    FriendCustomAdapter.FrListItems.remove(dipattern.get(1));
                    tinydb.putListString("myFriends", FriendCustomAdapter.FrListItems);

                    if (MainActivity.active) {
                        TabFragmment2.fAdapter.notifyDataSetChanged();
                    }
                    In_Chat.FinishChat();

                    String NotDFStr = dipattern.get(1) + " is not your friend anymore...";

                    showNotification(MainActivity.class, NotDFStr, Mcontext);//show a notification

                }

                break;


            case "602":
                //adding a friend
                if (dipattern.size()>1) {
                    if (!DoesFriendEx(dipattern.get(1))) {
                        FriendCustomAdapter.FrListItems = tinydb.getListString("myFriends");
                        FriendCustomAdapter.FrListItems.add(dipattern.get(1));
                        tinydb.putListString("myFriends", FriendCustomAdapter.FrListItems);

                        if (MainActivity.active) {
                            TabFragmment2.fAdapter.notifyDataSetChanged();
                        }

                        String NotFStr = dipattern.get(1) + " Is now your friend!";

                        showNotification(MainActivity.class, NotFStr, Mcontext);//show a notification


                    }
                }
                break;

            case "605":
                Toast.makeText(MainActivity.cpn, "User Was Not Found", Toast.LENGTH_SHORT).show();
                break;

            case "607":
                if (dipattern.size()>1) {
                    if (!DoesFriendEx(dipattern.get(1))) {
                        RequestCostumAdapter.rListItems = tinydb.getListString("myRequests");
                        RequestCostumAdapter.rListItems.remove(dipattern.get(1));

                        tinydb.putListString("myRequests", RequestCostumAdapter.rListItems);

                        FriendCustomAdapter.FrListItems = tinydb.getListString("myFriends");
                        FriendCustomAdapter.FrListItems.add(dipattern.get(1));
                        tinydb.putListString("myFriends", FriendCustomAdapter.FrListItems);

                        if (MainActivity.active) {
                            TabFragmment2.fAdapter.notifyDataSetChanged();
                            TabFragment3.rAdapter.notifyDataSetChanged();
                        }

                        String NotFStr = dipattern.get(1) + " Is now your friend!";
                        if (!MainActivity.active && !In_Chat.InChatActive) {
                            showNotification(MainActivity.class, NotFStr, Mcontext);//show a notification
                        } else {
                            Toast.makeText(MainActivity.cpn, "You Have A New Friend", Toast.LENGTH_SHORT).show();
                            vibe.vibrate(100);
                        }

                    }

                }
                break;

            case "608":
                Toast.makeText(MainActivity.cpn, "You Already Sent A Request For This User", Toast.LENGTH_SHORT).show();
                break;
            case "902":
                Toast.makeText(MainActivity.cpn, "Cant Create The Chat Group", Toast.LENGTH_SHORT).show();
                break;
            case "903":
                Toast.makeText(MainActivity.cpn, "The Group Name Or Password are inccorect", Toast.LENGTH_SHORT).show();
                break;
            case "905":
                Toast.makeText(MainActivity.cpn, "Cant Delete The Group", Toast.LENGTH_SHORT).show();
                break;
            case "907":
                Toast.makeText(MainActivity.cpn, "Cant Kick The User", Toast.LENGTH_SHORT).show();
                break;
            case "909":
                Toast.makeText(MainActivity.cpn, "Error Leaving The Group", Toast.LENGTH_SHORT).show();
                break;
            case "911":
                Toast.makeText(MainActivity.cpn, "Cant Update Group Info", Toast.LENGTH_SHORT).show();
                break;
            case "913":
                Toast.makeText(MainActivity.cpn, "Cant Update Group Password", Toast.LENGTH_SHORT).show();
                break;
            case "915":
                try{
                    if (dipattern.size()>2)
                    {
                        String oldn = dipattern.get(1);
                        String newn=dipattern.get(2);

                        boolean oldgad = tinydb.getBoolean(oldn+"isAdmin");

                        if (!oldn.equals(newn))
                        {
                            ReplaceGroupName(oldn,newn,tinydb);
                            Toast.makeText(MainActivity.cpn, "Group Name Was Changed", Toast.LENGTH_SHORT).show();
                        }

                        tinydb.putBoolean(newn+"isAdmin",oldgad);
                        tinydb.putBoolean(newn+"TG",true);
                    }
                }catch (Exception a){}


                break;
            case "900":
                if (dipattern.size()>1)
                {
                    ArrayList<String> tou = new ArrayList<String>();
                    tou.add("you");
                    tinydb.putListString(dipattern.get(1)+"UList",tou);
                    try
                    {

                        MyCustomAdapter.mListItems = tinydb.getListString("myUsers");
                        tinydb.putBoolean(dipattern.get(1)+"TG",true);
                        AddToChatG(Mcontext, dipattern.get(1));

                        if (MainActivity.active) {
                            TabFragment1.mAdapter.notifyDataSetChanged();
                        }

                    }catch (Exception a){}

                }
                break;
            case "901":
                if (dipattern.size()>2)
                {
                    try {
                        MyCustomAdapter.mListItems = tinydb.getListString("myUsers");
                        AddToChatG(Mcontext, dipattern.get(1));
                        tinydb.putBoolean(dipattern.get(1)+"TG",true);
                        String ddn;
                        new User(Mcontext);
                        ArrayList<String> userNames = new ArrayList<String>();
                        for (int i =2;i<dipattern.size();i++)
                        {
                            if (dipattern.get(i).equals(User.username)){ddn="you";}
                            else {ddn=dipattern.get(i);}
                            userNames.add(ddn);
                        }
                        tinydb.putListString(dipattern.get(1)+"UList",userNames);
                        if (MainActivity.active) {
                            TabFragment1.mAdapter.notifyDataSetChanged();
                        }
                        if (MembersSet.memison)
                        {
                            MembersAdapter.membersArr = userNames;
                            MembersSet.memAdapt.notifyDataSetChanged();

                        }
                        String tts = " You Were Added to "+dipattern.get(1);
                        showNotification(MainActivity.class, tts, Mcontext);
                    }catch (Exception a){}

                }
                break;
            case "921":
                if (dipattern.size()>2)
                {
                    try {
                        ArrayList<String> userNames1 = tinydb.getListString(dipattern.get(1)+"UList");
                        userNames1.add(dipattern.get(2));
                        tinydb.putListString(dipattern.get(1)+"UList",userNames1);
                        if (MembersSet.memison)
                        {
                            MembersAdapter.membersArr = userNames1;
                            MembersSet.memAdapt.notifyDataSetChanged();

                        }
                        String tts = dipattern.get(2)+" Was Added to "+dipattern.get(1);
                        showNotification(MainActivity.class, tts, Mcontext);
                    }catch (Exception b){}
                }
                break;
            case "908":
                if (dipattern.size()>1)
                {
                    try {
                        ArrayList<String> userNames1 = tinydb.getListString(dipattern.get(2)+"UList");
                        userNames1.remove(dipattern.get(1));
                        tinydb.putListString(dipattern.get(2)+"UList",userNames1);
                        if (MembersSet.memison)
                        {
                            MembersAdapter.membersArr = userNames1;
                            MembersSet.memAdapt.notifyDataSetChanged();

                        }
                        /*
                        AdminSet.FinishAdmin();
                        MembersSet.FinishMember();
                        In_Chat.FinishChat();
                        */
                        finishHim();



                        String tts = dipattern.get(1)+" Left "+dipattern.get(2);
                        showNotification(MainActivity.class, tts, Mcontext);
                    }catch (Exception b){}
                }

                break;
            case "906":
                if (dipattern.size()>1)
                {
                    try {
                        ArrayList<String> userNames1 = tinydb.getListString(dipattern.get(1)+"UList");
                        userNames1.remove(dipattern.get(2));
                        tinydb.putListString(dipattern.get(1)+"UList",userNames1);
                        if (MembersSet.memison)
                        {
                            MembersAdapter.membersArr = userNames1;
                            MembersSet.memAdapt.notifyDataSetChanged();

                        }
                        String tts = dipattern.get(2)+" Was Kicked "+dipattern.get(1);
                        showNotification(MainActivity.class, tts, Mcontext);
                    }catch (Exception b){}
                }
                break;
            case "904":
                if (dipattern.size()>1) {
                    try
                    {
                        MyCustomAdapter.mListItems = tinydb.getListString("myUsers");
                        int index123 =MyCustomAdapter.mListItems.indexOf(dipattern.get(1));
                        if (index123!=-1)
                        {
                            MyCustomAdapter.mListItems.remove(dipattern.get(1));
                        }
                        tinydb.putListString("myUsers", MyCustomAdapter.mListItems);

                        if (MainActivity.active) {
                            TabFragment1.mAdapter.notifyDataSetChanged();
                        }
                        /*
                        AdminSet.FinishAdmin();
                        MembersSet.FinishMember();
                        In_Chat.FinishChat();
                        */
                        finishHim();

                        String NotDFStr =  " You are not in "+dipattern.get(1)+ " anymore..." ;
                        showNotification(MainActivity.class, NotDFStr, Mcontext);//show a notification

                    }catch (Exception a){}

                }
                break;
            case "936":
                if (dipattern.size()>2)
                {
                    try {
                        tinydb.putBoolean(dipattern.get(1)+"TG",true);
                        new User(Mcontext);
                        String add2;
                        ArrayList<String> userNames = new ArrayList<String>();
                        for (int i =2;i<dipattern.size();i++)
                        {
                            if (dipattern.get(i).equals(User.username)){add2="you";}
                            else {add2=dipattern.get(i);}
                            userNames.add(add2);
                        }
                        tinydb.putListString(dipattern.get(1)+"UList",userNames);

                        if (MembersSet.memison)
                        {
                            MembersAdapter.membersArr = userNames;
                            MembersSet.memAdapt.notifyDataSetChanged();
                        }
                    }catch (Exception a){}

                }

                break;

            case "946":
                if (dipattern.size()>4)
                {
                    try {
                        String gname = dipattern.get(1);
                        String time,pass;
                        pass = dipattern.get(2);
                        time = dipattern.get(3);

                        new User(Mcontext);

                        tinydb.putString(gname+"pass",pass);
                        tinydb.putString(gname+"time",time);
                        tinydb.putBoolean(gname+"isAdmin",true);

                        tinydb.putBoolean(gname+"TG",true);
                        String addd;
                        ArrayList<String> userNames = new ArrayList<String>();
                        for (int i =4;i<dipattern.size();i++)
                        {
                            if (dipattern.get(i).equals(User.username)){addd="you";}
                            else {addd=dipattern.get(i);}
                            userNames.add(addd);
                        }
                        tinydb.putListString(dipattern.get(1)+"UList",userNames);

                        if (MembersSet.memison)
                        {
                            MembersAdapter.membersArr = userNames;
                            MembersSet.memAdapt.notifyDataSetChanged();
                        }

                    }catch (Exception b){}
                }
                break;
            case "910":
                if (dipattern.size()>4)
                {
                    try{
                        String oldn = dipattern.get(2);
                        String newn=dipattern.get(3);
                        String newtime,newpass;
                        newpass = dipattern.get(4);
                        newtime = dipattern.get(1);
                        boolean oldgad = tinydb.getBoolean(oldn+"isAdmin");

                        if (!oldn.equals(newn))
                        {
                            ReplaceGroupName(oldn,newn,tinydb);
                            Toast.makeText(MainActivity.cpn, "Group Name Was Changed", Toast.LENGTH_SHORT).show();
                        }


                        tinydb.putString(newn+"pass",newpass);
                        tinydb.putString(newn+"time",newtime);
                        tinydb.putBoolean(newn+"isAdmin",oldgad);
                        tinydb.putBoolean(newn+"TG",true);
                    }catch (Exception a){}

                }

                break;

            case "1100":
                if (dipattern.size()>1)
                {
                    try {
                        if (dipattern.get(1).equals(TabFragment1.currentChatName)&&In_Chat.InChatActive)
                        {
                            In_Chat.changeToolBar("Online");
                        }
                    }catch (Exception a){}
                }

                break;
            case "1101":
                if (dipattern.size()>2)
                {
                    try {
                        if (dipattern.get(1).equals(TabFragment1.currentChatName)&&In_Chat.InChatActive)
                        {
                            In_Chat.changeToolBar("Last Seen At "+dipattern.get(2));
                        }
                    }catch (Exception a){}
                }

                break;

            default:
                if (!MainActivity.ServerOnline)
                {
                    MainActivity.ActionBarServer("1");
                    MainActivity.ServerOnline=true;
                }

                String forstatus;
                new User(Mcontext);

                if (MainActivity.active&&MainActivity.onetimeting)
                {
                    forstatus = "1100@"+User.username+"*";
                    if (MainActivity.mTcpClient!=null)
                    {
                        MainActivity.mTcpClient.sendMessage(forstatus);
                    }
                    MainActivity.onetimeting=false;
                }

                if (Build.VERSION.SDK_INT<21&&!MainActivity.active)
                {
                    if (MainActivity.mTcpClient!=null)
                    {

                        MainActivity.mTcpClient.sendMessage("1101@"+User.username+"*");
                    }
                }
        }

        dipattern.clear();//clearing the array for new data
    }


    public static String pattern(ArrayList<String> str1, int code)//building the string for the server by the pattern
    {
        int i;

        String num = String.valueOf(code);
        String str2 = num + "@";

        int count = str1.size();

        for (i = 0; i < count; i++) {
            if (i == count - 1) {
                str2 += str1.get(i);
            } else {
                str2 += str1.get(i) + ",";
            }

        }
        str2 = str2 + "*";
        return str2;

    }

    public static void dispatternFunc(String masage, ArrayList<String> splitmasge) {
        //taking the server message apart so its easier to use
        String[] parts,forPic,doneimgg;
        splitmasge.clear();
        int count;
        int commas = 0;
        String temp = "";
        parts = masage.split("@");
        splitmasge.add(parts[0]); // add the code
        char[] mess = parts[parts.length - 1].toCharArray();

        if (parts[0].equals("1000"))
        {
            forPic = parts[1].split(",");
            splitmasge.add(forPic[0]);
            if (forPic.length>1)
            {
                if (!forPic[1].equals("*"))
                {
                    doneimgg = forPic[1].split("\\*");
                    String imgge = doneimgg[0];
                    splitmasge.add(imgge);
                }
            }
        }
        else if (parts[0].equals("111"))
        {
            forPic=masage.split("111@");
            splitmasge.add(forPic[1]);
        }
        else {
            if (mess.length > 0) {
                for (count = 0; count < mess.length; count++) {
                    if (mess[count] != ',' && mess[count] != '*') {
                        temp += mess[count];
                    } else {
                        if (mess[count] == ',') {
                            commas++;

                            if (parts[0].equals("200")) {
                                if (commas > 4)//4 because the server needs to add a letter - g or c right before the message content
                                {
                                    //do nothing because its a part of the message
                                    temp += mess[count];
                                } else {
                                    splitmasge.add(temp);
                                    temp = "";
                                }

                            } else {

                                splitmasge.add(temp);
                                temp = "";
                            }

                        } else {
                            if (parts[0].equals("100")) {
                                splitmasge.add(temp);
                                temp = "";
                            } else {
                                if (count == mess.length - 1) {
                                    splitmasge.add(temp);
                                    temp = "";
                                } else {
                                    temp += mess[count];
                                }
                            }

                        }

                    }
                }

            }
        }

    }

    public static void showNotification(Class<?> activityToLaunch, String message, Context c) {//showing a notification by the parameters
        //checking if the notifications are on
        TinyDB tdb = new TinyDB(MainActivity.cpn);
        boolean state = tdb.getBoolean("toggle");
        boolean vibeB = tdb.getBoolean("vibe");
        if (state) {
            PendingIntent pi = PendingIntent.getActivity(c, 0, new Intent(c, activityToLaunch), 0);
            KEY_TEXT_REPLY = "key_text_reply";
            String replyLabel = "Reply";

            if (vibeB) {
                Notification notification;
                if (Build.VERSION.SDK_INT >= 23&&dipattern.get(0).equals("200")) {
                    // Key for the string that's delivered in the action's intent.
                    RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                            .setLabel(replyLabel)
                            .build();


                    Notification.Action action =
                            new Notification.Action.Builder(R.drawable.ic_send_black_36dp,
                                    replyLabel, pi)
                                    .addRemoteInput(remoteInput)
                                    .build();
                    notification = new Notification.Builder(c)
                            .setTicker("LoCo")
                            .setSmallIcon(R.drawable.ic_stat_lc)
                            .setContentTitle("LoCo")
                            .setContentText(message)
                            .setContentIntent(pi)
                            .setAutoCancel(true)
                            .addAction(action)
                            .setLights(0x0000FF, 1500, 2500)
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setPriority(Notification.PRIORITY_MAX)
                            .build();
                }
                else
                {
                    notification = new Notification.Builder(c)
                            .setTicker("LoCo")
                            .setSmallIcon(R.drawable.ic_stat_lc)
                            .setContentTitle("LoCo")
                            .setContentText(message)
                            .setContentIntent(pi)
                            .setAutoCancel(true)
                            .setLights(0x0000FF, 1500, 2500)
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setPriority(Notification.PRIORITY_MAX)
                            .build();
                }


                NotificationManager notificationManager = (NotificationManager) c.getSystemService(c.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notification);
            } else {
                Notification notification2;
                if (Build.VERSION.SDK_INT >= 23&&dipattern.get(0).equals("200")) {
                    // Key for the string that's delivered in the action's intent.
                    RemoteInput remoteInput2 = new RemoteInput.Builder(KEY_TEXT_REPLY)
                            .setLabel(replyLabel)
                            .build();


                    Notification.Action action2 =
                            new Notification.Action.Builder(R.drawable.ic_send_black_36dp,
                                    replyLabel, pi)
                                    .addRemoteInput(remoteInput2)
                                    .build();

                    notification2 = new Notification.Builder(c)
                            .setTicker("LoCo")
                            .setSmallIcon(R.drawable.ic_stat_lc)
                            .setContentTitle("LoCo")
                            .setContentText(message)
                            .addAction(action2)
                            .setContentIntent(pi)
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                            .setVibrate(new long[]{0l})
                            .setPriority(Notification.PRIORITY_MAX)
                            .build();
                }
                else
                {
                    notification2 = new Notification.Builder(c)
                            .setTicker("LoCo")
                            .setSmallIcon(R.drawable.ic_stat_lc)
                            .setContentTitle("LoCo")
                            .setContentText(message)
                            .setContentIntent(pi)
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                            .setVibrate(new long[]{0l})
                            .setPriority(Notification.PRIORITY_MAX)
                            .build();
                }

                NotificationManager notificationManager = (NotificationManager) c.getSystemService(c.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notification2);

            }

        }

        PowerManager mgr = (PowerManager)c.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = mgr.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK| PowerManager.ACQUIRE_CAUSES_WAKEUP ,"tag");
        wakeLock.acquire();

    }

    public static void MoveChatToTop(String chatName, Context c, boolean friend) {
        TinyDB tinydb = new TinyDB(c);
        try {
            if (!friend) {
                FriendCustomAdapter.FrListItems = tinydb.getListString("myFriends");
                int index = FriendCustomAdapter.FrListItems.indexOf(chatName);
                if (index!=-1)
                {
                    FriendCustomAdapter.FrListItems.remove(index);
                    FriendCustomAdapter.FrListItems.add(0, chatName);
                    tinydb.putListString("myFriends", FriendCustomAdapter.FrListItems);
                }
            } else {
                MyCustomAdapter.mListItems = tinydb.getListString("myUsers");
                int index = MyCustomAdapter.mListItems.indexOf(chatName);
                if (index!=-1) {
                    MyCustomAdapter.mListItems.remove(index);
                    MyCustomAdapter.mListItems.add(0, chatName);
                    tinydb.putListString("myUsers", MyCustomAdapter.mListItems);
                }
            }
        }catch (Exception a){}

    }


    public static boolean DoesFriendEx(String name) {
        boolean ans = false;
        int i;
        TinyDB tdb = new TinyDB(MainActivity.cpn);

        friendsNames = tdb.getListString("myFriends");

        for (i = 0; i < friendsNames.size(); i++) {
            if (friendsNames.get(i).equals(name)) {
                ans = true;
            }
        }

        return ans;
    }
    public static boolean DoesGroupEx(String name) {
        boolean ans = false;
        int i;
        TinyDB tdb = new TinyDB(MainActivity.cpn);

        GroupsNames = tdb.getListString("myUsers");

        for (i = 0; i < GroupsNames.size(); i++) {
            if (GroupsNames.get(i).equals(name)) {
                ans = true;
            }
        }

        return ans;
    }

    public static void DBusers(ArrayList<String> List, int numT) {
        TinyDB tdb = new TinyDB(MainActivity.cpn);
        int i;
        String TOBlock = "";
        if (List.size() > 1 ) {
            switch (numT) {

                case 0:
                    MyCustomAdapter.mListItems = new ArrayList<String>();

                    for (i = 1; i < List.size(); i++) {

                        if (!TOBlock.equals(List.get(i))) {
                            if (i==2)
                            {
                                if (!List.get(i).equals(List.get(i-1)))
                                {
                                    MyCustomAdapter.mListItems.add(List.get(i));
                                }
                            }
                            else
                            {
                                MyCustomAdapter.mListItems.add(List.get(i));
                            }

                        }


                    }

                    tdb.putListString("myUsers", MyCustomAdapter.mListItems);
                    break;

                case 1:
                    FriendCustomAdapter.FrListItems = new ArrayList<String>();

                    for (i = 1; i < List.size(); i++) {
                        if (!TOBlock.equals(List.get(i))) {
                            FriendCustomAdapter.FrListItems.add(List.get(i));
                        }

                    }
                    tdb.putListString("myFriends", FriendCustomAdapter.FrListItems);
                    break;

                case 2:
                    RequestCostumAdapter.rListItems = new ArrayList<String>();

                    for (i = 1; i < List.size(); i++) {

                        if (!TOBlock.equals(List.get(i))) {
                            RequestCostumAdapter.rListItems.add(List.get(i));
                        }

                    }
                    tdb.putListString("myRequests", RequestCostumAdapter.rListItems);
                    break;

                default:

            }
        }

    }

    public static void AddToChatG(Context cc, String name) {
        TinyDB tinydb = new TinyDB(cc);
        Vibrator vibe = (Vibrator) cc.getSystemService(Context.VIBRATOR_SERVICE);
        MyCustomAdapter.mListItems = tinydb.getListString("myUsers");
        MyCustomAdapter.mListItems.add(name);
        tinydb.putListString("myUsers", MyCustomAdapter.mListItems);

        if (MainActivity.active && TabFragment1.mAdapter != null) {
            TabFragment1.mAdapter.notifyDataSetChanged();
        }

        String NotGStr = "You were added to " + name;
        TabFragment1.currentChatName = name;
        if (!MainActivity.active && !In_Chat.InChatActive) {
            showNotification(In_Chat.class, NotGStr, cc);//show a notification
        } else {
            Toast.makeText(MainActivity.cpn, "You Were Added To A New Group", Toast.LENGTH_SHORT).show();
            vibe.vibrate(100);
        }

    }

    public static void okFunc() {
        Login.EndSpin();
        int k;
        boolean killData;
        String data = TheData;
        TinyDB tinydb = new TinyDB(MainActivity.cpn);
        tinydb.putBoolean("isLoged", true);
        Intent intent = new Intent("finish_activity");

        String infoPlz;


        killData = tinydb.getBoolean("KillEm");

        forconfirm = new ArrayList<String>();
        new User(MainActivity.cpn);
        String theUserName = User.username;
        String theUserPass = User.password;
        forconfirm.add(User.username);
        infoPlz = Communication.pattern(forconfirm, 501);

        if (killData) {
            tinydb.clear();
            tinydb.putBoolean("isLoged", true);
            tinydb.putString("TheUserName", theUserName);
            tinydb.putString("TheUserPass", theUserPass);
            tinydb.putBoolean("toggle", true);
            tinydb.putBoolean("vibe", true);
            tinydb.putBoolean("Cleaned",true);


            int colrI = R.color.appBarC;
            int colorf = R.color.appc;
            int accent = R.color.LoCoOrange;
            tinydb.putInt("colorAc", accent);
            tinydb.putInt("colorInt", colrI);
            tinydb.putInt("colorF", colorf);
            tinydb.putBoolean("fksplash", true);

            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/LoCo";
            File fl = new File(file_path);
            if (fl.exists())
            {
                try {
                    FileUtils.deleteDirectory(fl);
                }catch (Exception a){}

            }

        }

        //sends the message to the server
        if (MainActivity.mTcpClient != null) {

            MainActivity.mTcpClient.sendMessage(infoPlz);
        }

        if (Login.LogActive) {
            MainActivity.cpn.sendBroadcast(intent);
        }

        if (dipattern.size() > 1) {
            String[] strArr,formove;
            strArr = data.split("111@");
            formove = strArr[1].split("\\*");
            for (k=0;k<formove.length;k++)
            {
                ManageData(formove[k]+"*", MainActivity.cpn);
            }
        }

        if (killData) {
            Refresh();
        }


    }

    public static void Refresh() {
        Intent i = MainActivity.cpn.getApplicationContext().getPackageManager()
                .getLaunchIntentForPackage(MainActivity.cpn.getApplicationContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        MainActivity.cpn.getApplicationContext().startActivity(i);
    }



    public static void JustPutIt(View vv,String name)
    {

        TinyDB tdb = new TinyDB(MainActivity.cpn);
        String PicPath = tdb.getString(name);//need to be name

        File imgFile = new  File(PicPath);
        if(imgFile.exists()){
            CircleImageView iv = (CircleImageView) vv.findViewById(R.id.imageViewf);

            Glide.with(MainActivity.cpn)
                    .load(imgFile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(iv);




        }
    }

    public static void SendProfilePic(Context c)
    {
        dialog = ProgressDialog.show(c, "",
               "Loading. If It Takes Too Long Please Restart The App...", true);


        TinyDB tdb = new TinyDB(MainActivity.cpn);
        String str = tdb.getString("uriS");
        if (str!=null&!str.equals(""))//just for test should'nt be here
        {
            //dialog.show();
            MainActivity.mTcpClient.sendFile(str);
        }


    }

    public static void btmapToF(Bitmap bmp,boolean prof)
    {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/LoCo";
        File dir = new File(file_path);
        if(!dir.exists())
        {
             dir.mkdirs();
        }
        if (!prof)
        {
            try{
                File file = new File(dir,dipattern.get(1)+"p"+".jpg");
                TinyDB tdb = new TinyDB(MainActivity.cpn);
                tdb.putString(dipattern.get(1)+"p",  file.getPath());
                FileOutputStream fOut = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                fOut.flush();
                fOut.close();}
            catch (Exception e) { Log.e("MYAPP", "exception", e);}
        }
        else
        {
            TinyDB tdb = new TinyDB(MainActivity.cpn);
            String ur = tdb.getString("uriS");
            if (ur.equals("")||ur.equals("uriS.jpg"))
            {
                try{
                    File file = new File(dir,"proPicS"+".jpg");

                    tdb.putString("uriS",  file.getPath());
                    FileOutputStream fOut = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                    fOut.flush();
                    fOut.close();
                }
                catch (Exception e) {}
            }
        }
    }

    public static void ReplaceGroupName(String oldname,String newname,TinyDB tinydb)
    {
        try {
            if (!oldname.equals(newname))
            {
                ArrayList <String> toSendArray = tinydb.getListString(oldname);
                ArrayList <Integer> Directions = tinydb.getListInt(oldname + "d");
                ArrayList <Integer> Vstate = tinydb.getListInt(oldname + "v");
                ArrayList <String> TaRR = tinydb.getListString(oldname + "t");
                ArrayList <String> idforView = tinydb.getListString(oldname + "i");
                ArrayList <String> forGroupM = tinydb.getListString(oldname + "g");
                ArrayList <String> Ulist = tinydb.getListString(oldname+"UList");

                tinydb.putListString(newname,toSendArray);
                tinydb.putListString(newname+"t",TaRR);
                tinydb.putListString(newname+"i",idforView);
                tinydb.putListString(newname+"g",forGroupM);
                tinydb.putListInt(newname+"d",Directions);
                tinydb.putListInt(newname+"v",Vstate);
                tinydb.putListString(newname+"UList",Ulist);

                ArrayList <String> groups = tinydb.getListString("myUsers");
                int index = groups.indexOf(oldname);
                groups.add(index,newname);
                groups.remove(index+1);

                tinydb.putListString("myUsers",groups);
                MyCustomAdapter.mListItems=groups;


                MembersSet.FinishMember();
                In_Chat.FinishChat();


                TabFragment1.mAdapter.notifyDataSetChanged();
                TabFragment1.currentChatName=newname;

            }

        }catch (Exception s){}

    }

    public static void finishHim()
    {
        AdminSet.FinishAdmin();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
                MembersSet.FinishMember();
            }
        }, 1000);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
                In_Chat.FinishChat();
            }
        }, 1000);


    }

}
