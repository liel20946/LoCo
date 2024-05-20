package com.example.user.loco;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.ArrayList;


public class MembersSet extends ActionBarActivity {
    public static MembersAdapter memAdapt;
    public static Context memCon;
    public static ListView membersl;
    public static boolean memison=false;
    public static Activity memAc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_set);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        memAc = this;
        memCon = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Members");
        setSupportActionBar(toolbar);
        Button leave = (Button)findViewById(R.id.leavegbtn);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.appc));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.appBarC));
            int color = getResources().getColor(R.color.appBarC);

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, bm, color);

            setTaskDescription(td);
            bm.recycle();
        }



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(20);


         membersl = (ListView) findViewById(R.id.membersL);
        final TinyDB tinydb = new TinyDB(this);


        MembersAdapter.membersArr = tinydb.getListString(TabFragment1.currentChatName+"UList");

        memAdapt = new MembersAdapter(this,MembersAdapter.membersArr);//sending the saved list of chats(users)

        membersl.setAdapter(memAdapt);

        String name = TabFragment1.currentChatName;
        int count =MembersAdapter.membersArr.size();
        String size = String.valueOf(count);
        if (name!=null)
        {
            TextView gname = (TextView) findViewById(R.id.groumNAME);
            if (gname!=null)
            {
                gname.setText(name);
            }
        }
        if (size!=null)
        {
            TextView gsize = (TextView) findViewById(R.id.memNum);
            if (gsize!=null)
            {
                gsize.setText("Number Of Members: "+size);
            }
        }


        membersl.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) findViewById(R.id.groumNAME);
                TextView textView2 = (TextView) view.findViewById(R.id.MemName);
                final String n1 = textView.getText().toString();
                final String pn=textView2.getText().toString();
                boolean admin = tinydb.getBoolean(n1+"isAdmin");

                if (admin){
                    final CharSequence[] items = {"Yep", "No Sir"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(memCon, R.style.YourAlertDialogTheme);
                    builder.setTitle("Do You Want To Kick This User?");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            if(item==0){

                                KickUser(pn,n1);
                            }
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

                return true;
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> test = new ArrayList<String>();
                new User(MainActivity.cpn);
                test.add(User.username);
                test.add(TabFragment1.currentChatName);

                String send = Communication.pattern(test, 906);
                if (MainActivity.mTcpClient != null)
                {
                    MainActivity.mTcpClient.sendMessage(send);
                }
                finish();
            }

            });


    }

    @Override
    public void onStart() {
        super.onStart();
        memison = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        memison = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        TinyDB tinydb = new TinyDB(this);
        boolean ad = tinydb.getBoolean(TabFragment1.currentChatName+"isAdmin");
        if (ad)
        {
            getMenuInflater().inflate(R.menu.adminmenu, menu);
        }
        else
        {
            getMenuInflater().inflate(R.menu.memset_menu, menu);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.op:
                Intent mmm = new Intent(this,AdminSet.class);
                startActivity(mmm);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void KickUser(String name,String ggName)
    {
        ArrayList<String> forKick = new ArrayList<String>();
        new User(memCon);
        forKick.add(User.username);
        forKick.add(name);
        forKick.add(ggName);

        String done = Communication.pattern(forKick,903);
        if (MainActivity.mTcpClient!=null)
        {
            MainActivity.mTcpClient.sendMessage(done);
        }

    }

    public static void FinishMember()
    {
        if (memAc!=null)
        {
            try {
                memAc.finish();
            }catch (Exception t){}

        }

    }
}
